package com.jmeranda.glazy.cli.commands

import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.ParentCommand

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.service.IssueService
import com.jmeranda.glazy.lib.service.RepoService

import com.jmeranda.glazy.cli.getRepoName
import com.jmeranda.glazy.lib.service.CacheService

enum class State(val state: String) {
    OPEN("open"),
    CLOSED("closed"),
    ALL("all"),
}

/**
 * Display an Issue to the console.
 *
 * @param issue The issue to display.
 */
fun displayIssue(issue: Issue) {
    println("[${issue.number}] ${issue.title}")
}

open class IssueCommand() {
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
 * Parent command for all issue operations.
 */
@Command(name="issue",
        description=["Perform operations on repository issues"],
        mixinStandardHelpOptions=true)
class IssueParent() {
}

/**
 * Sub-command to list repository issues.
 *
 * @property parent Reference to the parent command instance.
 * @property number The number of the issue to retrieve, if none is
 *     provided then all repository issues are  retrieved.
 */
@Command(name="list",
        description=["List repository issues."],
        mixinStandardHelpOptions=true)
class IssueList(): Runnable, IssueCommand() {
    @ParentCommand
    private val parent: IssueParent? = null

    @Option(names=["-n", "--number"],
            description=["The number of the desired issue."],
            paramLabel="N")
    private val number: Int? = null

    override fun run() {
        val issueList: List<Issue?>? = if (this.number == null) {
            this.service?.getAllIssues()
        } else  {
            listOf(this.service?.getIssue(this.number))
        }

        for (issue: Issue? in issueList ?: listOf()) {
            displayIssue(issue ?: continue)
        }
    }
}

/**
 * Sub-command to add an issue to the repository.
 *
 * @property parent Reference to the parent command instance.
 * @property title The title for the new issue.
 * @property body The body for the new issue.
 * @property milestone The milestone number for the new issue.
 * @property labels The labels for the new issue.
 * @property assignees The user logins to be assigned to the new issue.
 */
@Command(name="add",
        description=["Create a new issue in the repository."],
        mixinStandardHelpOptions = true)
class IssueAdd(): Runnable, IssueCommand() {
    @ParentCommand
    private val parent: IssueParent? = null

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
        val issue = this.service?.createIssue(title, body, milestone, labels, assignees)
        displayIssue(issue ?: return)
    }
}

/**
 * Sub-command to send a patch to a repository issue.
 *
 * Currently all options are required to avoid overwriting fields meant
 * to be ignored with a null value.
 *
 * @property parent Reference to the parent command instance.
 * @property number The number of the issue to patch.
 * @property title The patched title for the issue.
 * @property body The patched body of the issue.
 * @property state The patched state of the issue.
 * @property milestone The patched milestone of  issue.
 * @property labels The patched labels of the issue.
 * @property assignees The patched list of assignees for the issue.
 */
@Command(name="patch",
        description=["Send a patch to an issue."],
        mixinStandardHelpOptions=true)
class IssuePatch(): Runnable, IssueCommand() {
    @ParentCommand
    private val parent: IssueParent? = null

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
        val issue = this.service?.editIssue(this.number, this.title, this.body,
                this.state.toString(), this.milestone, this.labels, this.assignees)
        displayIssue(issue ?: return)
    }
}