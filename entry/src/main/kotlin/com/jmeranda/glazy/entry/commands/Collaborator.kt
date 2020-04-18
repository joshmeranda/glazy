package com.jmeranda.glazy.entry.commands

import com.jmeranda.glazy.entry.Verbose
import com.jmeranda.glazy.lib.exception.NotInRepo
import com.jmeranda.glazy.lib.handler.Handler
import com.jmeranda.glazy.lib.objects.User
import com.jmeranda.glazy.lib.service.CollaboratorService
import com.jmeranda.glazy.lib.service.getToken
import com.jmeranda.glazy.lib.service.getRepoName

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Mixin
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters

/**
 * Parent class for all collaborator commands.
 *
 * @property service The [com.jmeranda.glazy.lib.service.CollaboratorService] utilized by collaborator commands.
 * @property token Te token to use for api authentication.
 */
sealed class CollaboratorCommand {
    @Mixin
    var verbose: Verbose? = null

    protected var service: CollaboratorService? = null
    private var token: String? = null

    /**
     * Initialize the collaborator service.
     *
     * @return The collaborator service for a specific repositoru or null is an error occurs.
     * @throws NotInRepo when the current working directory is not in a git repository.s
     */
    protected fun initService(): CollaboratorService? {
        val (user, name) = getRepoName()

        if (user != null) token = getToken()
        if (user != null && name != null) this.service = CollaboratorService(user, name, token)

        Handler.verbose = this.verbose?.verbose ?: false
        return this.service
    }
}

/**
 * Parent command for all collaborator sub-commands.
 */
@Command(name="collab",
    description=["Perform operations on repository collaborators."],
    mixinStandardHelpOptions=true)
class CollaboratorParent : Runnable {
    @CommandLine.Spec
    lateinit var spec: CommandLine.Model.CommandSpec

    /**
     * When run before all child classes, with end program if no sub-command is passed as an argument.
     *
     * @throws CommandLine.ParameterException When no sub-command is entered by terminal.
     */
    override fun run() {
        throw CommandLine.ParameterException(this.spec.commandLine(),
            "Missing required subcommand")
    }
}

/**
 * List all collaborators on a repository.
 *
 * @property affiliation The affiliation of users to list.
 */
@Command(name="list",
    description=["List all repository collaborators."],
    mixinStandardHelpOptions=true)
class CollaboratorList : Runnable, CollaboratorCommand() {
    @Option(names=["--affiliation"],
        description=["The affiliation of collaborators to list (outside, direct, all) defaults to all."])
    var affiliation: String = "all"

    override fun run() {
        this.initService() ?: return

        for (user: User in this.service?.getAllCollaborators(this.affiliation) ?: listOf()) {
            displayCollaborator(user)
        }
    }
}

/**
 * Add a collaborator to a repository.
 *
 * @property permissions The permissions for the new collaborators.
 * @property collaborators The list of collaboratorsr to add.
 */
@Command(name="add",
    description=["Add collaborators to the repository."],
    mixinStandardHelpOptions=true)
class CollaboratorAdd : Runnable, CollaboratorCommand() {
    @Parameters(index="0", description=["The permissions to apply to the new collaborators (pull, push, admin, maintain, triage)."])
    private var permissions: String = "pull"

    @Parameters(index="1", arity="1..*", description=["The username to add as a collaborator"])
    private lateinit var collaborators: List<String>

    override fun run() {
        this.initService() ?: return

        for (collaborator: String in this.collaborators ?: listOf()) {
            displayInvite(this.service?.addCollaborator(collaborator, permissions) ?: continue)
        }
    }
}

/**
 * Remove a user as a collaborator from a repository.
 *
 * @property collaborators The list of collaborators to remove.
 */
@Command(name="remove",
    description=["Remove collaborators to the repository."],
    mixinStandardHelpOptions=true)
class CollaboratorRemove : Runnable, CollaboratorCommand() {
    @Parameters(index="0..*", description=["The users to remove as collaborators."])
    private val collaborators: List<String>? = null

    override fun run() {
        this.initService() ?: return

        for (collaborator: String in this.collaborators ?: listOf()) {
            this.service?.removeCollaborator(collaborator)
            println("'${collaborator}' removed as a collaborator")
        }
    }
}
