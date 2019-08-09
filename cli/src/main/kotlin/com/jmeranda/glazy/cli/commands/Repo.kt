package com.jmeranda.glazy.cli.commands

import picocli.CommandLine.Option
import picocli.CommandLine.Command
import picocli.CommandLine.ParentCommand

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.service.RepoService

fun displayRepo(repo: Repo) {
    println("name: ${repo.name}")
    println("owner: ${repo.owner.login}")
    println("created: ${repo.createdAt}")
    println("clone url: ${repo.cloneUrl}")
}

/**
 * Parent command for all repo operations.
 *
 * @property parent Reference to the parent command instance.
 * @property service The repo service used to interact with the github api.
 */
@Command(name = "repo",
        description = ["Perform operations on a  repository."],
        mixinStandardHelpOptions = true
)
class RepoParent(): Runnable {
    @ParentCommand
    val parent: Glazy? = null
    lateinit var service: RepoService

    /**
     * Instantiate the service required by sub-commands.
     *
     * Any sub-command which requires the use of an issue service will
     * be required to call this method or service erro will be thrown.
     */
    override fun run() {
        this.parent?.run()
        this.service = this.parent?.repoService ?: return
    }
}

/**
 * Sub-command to show information about a repository.
 *
 * If no user or name arguments are passed as arguments, the values
 * parsed in the glazy command are used to show the current repo.
 *
 * @property parent Reference to the parent command instance.
 * @property user The repository owner login.
 * @property name The name of the repository.
 */
@Command(name = "show",
        description = ["Show details about a repository"],
        mixinStandardHelpOptions = true
)
class RepoShow(): Runnable {
    @ParentCommand
    private val parent: RepoParent? = null

    @Option(names = ["-u", "--user"],
            description = ["The user login for the desired repository."],
            paramLabel = "LOGIN"
    )
    var user: String? = null

    @Option(names = ["-n", "--name"],
            description = ["The name of the desired repository"],
            paramLabel = "NAME"
    )
    var name: String? = null

    override fun run() {
        this.parent?.run()

        /* use the value parsed in the Glazy parent command if non */
        /* passed as argument */
        this.user = if (this.user == null) {
            this.parent?.parent?.user
        } else {
            this.user
        }

        this.name = if ( this.name == null) {
            this.parent?.parent?.name
        } else {
            this.name
        }

        val repo = this.parent?.service?.getRepo(this.name?: return,
                this.user ?: return)
        displayRepo(repo ?: return)
    }
}

class RepoInit() {
}

class RepoPatch() {
}

class RepoDelete() {
}

class RepoArchive() {
}

class RepoTransfer() {
}