package com.jmeranda.glazy.cli.commands

import picocli.CommandLine.Command
import picocli.CommandLine.Option

import com.jmeranda.glazy.lib.objects.Issue
import com.jmeranda.glazy.lib.service.IssueService
import com.jmeranda.glazy.lib.service.RepoService

import com.jmeranda.glazy.cli.getRepoName
import com.jmeranda.glazy.lib.service.CacheService

/**
 * State enum for patching issues.
 */
enum class State(val state: String) {
    OPEN("open"),
    CLOSED("closed"),
    ALL("all"),
}

/**
 * Display an [issue] to the console.
 */
fun displayIssue(issue: Issue) {
    println("[${issue.number}] ${issue.title}")
}

/**
 * Class to provide a [service] to perform operations and a [token] for
 * authentication. All issue sub-commands must inherit from this class.
 */
open class IssueCommand {
    var service: IssueService? = null
    private var token: String? = null

    init {
        val (user, name) = getRepoName()

        if (user != null) { token = CacheService.token(user) }

        if (name != null && user != null) {
            this.service = IssueService(
                    RepoService(this.token).getRepo(user, name),
                    token)
        }
    }
}

/**
 * Parent for all issue sub-commands.
 */
@Command(name="issue",
        description=["Perform operations on repository issues"],
        mixinStandardHelpOptions=true)
class IssueParent

/**
 * List repository issues, will list all issues according to the value
 * of [number], if null all issues are listed.
 */
@Command(name="list",
        description=["List repository issues."],
        mixinStandardHelpOptions=true)
class IssueList: Runnable, IssueCommand() {
    @Option(names=["-n", "--number"],
            description=["The number of the desired issue."],
            paramLabel="N")
    private val number: Int? = null

    override fun run() {
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
 * Create repository issues given a [title], [body], [milestone],
 * [labels], and [assignees].
 */
@Command(name="add",
        description=["Create a new issue in the repository."],
        mixinStandardHelpOptions = true)
class IssueAdd: Runnable, IssueCommand() {
    @Option(names=["-t", "--title"],
            description=["The title of the new issue."],
            paramLabel="STRING",
            required=true)
    private var title: String = ""

    @Option(names=["-b", "--body"],
            description=["The body of the new issue."],
            paramLabel="STRING")
    private var body: String? = null

    @Option(names=["-m", "--milestone"],
            description=["The number of the milestone for the new issue."],
            paramLabel="N")
    private var milestone: Int? = null

    @Option(names=["-l", "--labels"],
            description=["The labels for the new issue, as comma separated strings."],
            split=",",
            paramLabel="LABEL")
    private var labels: List<String>? = null

    @Option(names=["-a", "--assignees"],
            description=["The user logins for users to be assigned to the issue, as comma separated strings."],
            split=",",
            paramLabel="[LOGIN...]")
    private var assignees: List<String>? = null

    override fun run() {
        // Create the issue.
        val issue = this.service?.createIssue(title, body, milestone, labels, assignees)
        displayIssue(issue ?: return)
    }
}

/**
 * Replace repository issue number [number] by replacing issue content
 * with values of [title], [body], [state], [milestone], [labels],
 * and [assignees].
 */
@Command(name="patch",
        description=["Send a patch to an issue."],
        mixinStandardHelpOptions=true)
class IssuePatch: Runnable, IssueCommand() {
    @Option(names=["-n", "--number"],
            description=["The number of the issue to patch."],
            paramLabel="N")
    private var number: Int = -1

    @Option(names=["-t", "--title"],
            description=["The patched issue of the issue."],
            paramLabel="STRING")
    private var title: String? = null

    @Option(names=["-b", "--body"],
            description=["The patched body of the issue."],
            paramLabel="STRING")
    private var body: String? = null

    @Option(names=["-s", "--state"],
            description=["The patched state of the issue."],
            paramLabel="[open,closed,all]")
    private var state: State? = null

    @Option(names=["-m", "--milestone"],
            description=["The patched number of the milestone for the issue."],
            paramLabel="N")
    private var milestone: Int? = null

    @Option(names=["-l", "--labels"],
            description=["The patched labels for the issue, as comma separated strings."],
            paramLabel="[LABELS...]")
    private var labels: List<String>? = null

    @Option(names=["-a", "--assignees"],
            description=["The patched user logins for users to be assigned to the issue, as comma separated strings."],
            paramLabel="[LOGIN...]",
            split=",")
    private var assignees: List<String>? = null

    override fun run() {
        // Patch the issue.
        val issue = this.service?.editIssue(this.number, this.title, this.body,
                this.state.toString(), this.milestone, this.labels, this.assignees)
        displayIssue(issue ?: return)
    }
}