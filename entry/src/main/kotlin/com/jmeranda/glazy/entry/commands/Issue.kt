package com.jmeranda.glazy.entry.commands

import com.jmeranda.glazy.entry.Verbose
import picocli.CommandLine
import picocli.CommandLine.ArgGroup
import picocli.CommandLine.Command
import picocli.CommandLine.Mixin
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import picocli.CommandLine.Spec
import picocli.CommandLine.Model.CommandSpec

import com.jmeranda.glazy.lib.exception.NotInRepo
import com.jmeranda.glazy.lib.handler.Handler
import com.jmeranda.glazy.lib.objects.Issue
import com.jmeranda.glazy.lib.service.IssueService
import com.jmeranda.glazy.lib.service.getToken
import com.jmeranda.glazy.lib.service.getRepoName

/**
 * Parent class for all issue commands.
 *
 * @property service The [com.jmeranda.glazy.lib.service.IssueService] utilized by issue commands.
 * @property token The token to use for api authentication.
 */
sealed class IssueCommand {
    @Mixin
    var verbose: Verbose? = null
    protected var service: IssueService? = null
    private var token: String? = null

    /**
     * Initializes the issue service.
     *
     * @return The issue service for a specific repository or null if an error occurs.
     * @throws NotInRepo when the current working directory is not in a git repository.s
     */
    protected fun initService(): IssueService? {
        val (user, name) = getRepoName()

        if (user != null) token = getToken()
        if (user != null && name != null) this.service = IssueService(user, name, token)

        Handler.verbose = this.verbose?.verbose ?: false
        return this.service
    }
}

/**
 * Parent command for all issue sub-commands.
 */
@Command(name="issue",
        description=["Perform operations on repository issues"],
        mixinStandardHelpOptions=true)
class IssueParent : Runnable {
    @Spec lateinit var spec: CommandSpec

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
 * List repository issues with either a specific issue or all open.
 *
 * @property number The number of the issue too list.
 */
@Command(name="list",
        description=["List repository issues."],
        mixinStandardHelpOptions=true)
class IssueList : Runnable, IssueCommand() {
    @Option(names=["-n", "--number"],
            description=["The number of the desired issue."])
    private val number: Int? = null

    override fun run() {
        this.initService() ?: return

        // Retrieve repository issues.
        val issueList: List<Issue?>? = if (this.number == null) {
            this.service?.getAllIssues()
        } else  {
            listOf(this.service?.getIssue(this.number))
        }

        // Display all retrieved issues.
        for (issue: Issue? in issueList ?: listOf()) {
            displayIssue(issue ?: continue)
        }
    }
}

/**
 * Create new issues for a repository.
 *
 * @property title The title for the new issue.
 * @property body The body describing the issue.
 * @property milestone The milestone which is to be associated with the new issue.
 * @property labels The list of labels  with which to mark the new issue.
 * @property assignees The list of people who are to be assigned to resolve the issue.
 */
@Command(name="add",
        description=["Create a new issue in the repository."],
        mixinStandardHelpOptions = true)
class IssueAdd: Runnable, IssueCommand() {
    @Parameters(index = "0", description=["The title of the new issue."])
    private lateinit var title: String

    @Option(names=["-b", "--body"],
            description=["The body of the new issue."])
    private var body: String? = null

    @Option(names=["-m", "--milestone"],
            description=["The number of the milestone for the new issue."])
    private var milestone: Int? = null

    @Option(names=["-l", "--labels"],
            description=["The labels for the new issue."],
            arity="1..*")
    private var labels: List<String>? = null

    @Option(names=["-a", "--assignees"],
            description=["The user logins for users to be assigned to the issue."],
            arity="1..*")
    private var assignees: List<String>? = null

    override fun run() {
        this.initService() ?: return
        // Create the issue.
        val issue = this.service?.createIssue(title, body, milestone, labels, assignees) ?: return
        displayIssue(issue)
    }
}

/**
 * Modify the state and or content of an existing repository issue.
 *
 * @property number The number of the issue to patch.
 * @property title The new title for the issue.
 * @property body The new body for the issue.
 * @property state The new state for the issue (either open or closed).
 * @property milestone The new milestone to attatch to the issue.
 * @property labels The labels with which to mark the issue.
 * @property assignees The list of people who are assigned to resolve the issue.
 */
@Command(name="patch",
        description=["Send a patch to an issue."],
        mixinStandardHelpOptions=true)
class IssuePatch: Runnable, IssueCommand() {
    @Parameters(index = "0", description=["The number of the issue to patch."])
    private var number: Int = -1

    @Option(names=["-t", "--title"],
            description=["The patched issue of the issue."])
    private var title: String? = null

    @Option(names=["-b", "--body"],
            description=["The patched body of the issue."])
    private var body: String? = null

    @ArgGroup(exclusive = true, multiplicity = "0..1")
    private var state: State? = null

    @Option(names=["-m", "--milestone"],
            description=["The patched number of the milestone for the issue."])
    private var milestone: Int? = null

    @Option(names=["-l", "--labels"],
            description=["The patched labels for the issue."],
            arity="1..*")
    private var labels: List<String>? = null

    @Option(names=["-a", "--assignees"],
            description=["The patched user logins for users to be assigned to the issue"],
            arity="1..*")
    private var assignees: List<String>? = null

    override fun run() {
        this.initService() ?: return
        val state = when {
            this.state == null -> null
            this.state?.open ?: false -> "open"
            this.state?.closed ?: false -> "closed"
            else -> null
        }

        // Patch the issue.
        val issue = this.service?.editIssue(this.number,
                this.title,
                this.body,
                state,
                this.milestone,
                this.labels,
                this.assignees
        ) ?: return

        displayIssue(issue)
    }

    companion object {
        class State {
            @Option(names = ["--open"],
                    description = ["Mark the issue as open."])
            var open: Boolean = false

            @Option(names = ["--closed"],
                    description = ["Mark the issue as closed."])
            var closed: Boolean = false
        }
    }
}