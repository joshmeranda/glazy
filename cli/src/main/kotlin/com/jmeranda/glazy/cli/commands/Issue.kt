package com.jmeranda.glazy.cli.commands

import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.ParentCommand

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.service.IssueService
import picocli.CommandLine

enum class State(val state: String) {
    OPEN("open"),
    CLOSED("closed"),
    ALL("all"),
}

enum class Sort(val sortByValue: String) {
    CREATED("created"),
    UPDATED("updated"),
    COMMENTS("comments"),
}

enum class SortDirection(val direction: String) {
    ASCEND("asc"),
    DESCEND("desc"),
}

/**
 * Display an Issue to the console.
 * @param issue The issue to display.
 */
fun displayIssue(issue: Issue) {
    println("[${issue.number}] ${issue.title}")
}

/**
 * Parent command for all issue operations.
 * @property repo The github repository object on which to run issue
 *     operations.
 * @property token The personal access token to use for authentication.
 */
@Command(name="issue")
class Issue(private val repo: Repo, private var token: String?): Runnable {
    private val service = IssueService(this.repo, this.token)

    override fun run() {
    }

    /**
     * Sub-command to list repository issues or issue.
     * @param number The number of the target issue, if non specified
     *     all 'open' issues are found.
     */
    @Command(name="list", description=["List issue(s) from the repository."])
    fun list(
            @Option(names=["-n", "--number"], description=["The number of the desired issue"])
                number: Int? = null
    ) {
        val issueList: List<Issue> = if (number == null) {
            this.service.getAllIssues()
        } else {
            listOf(this.service.getIssue(number))
        }

        for (issue: Issue in issueList) {
            displayIssue(issue)
        }
    }

    /**
     * Sub-command to add an issue to the repository.
     * @param title The title for the new issue.
     * @param body The body for the new issue.
     * @param milestone The milestone number for the new issue.
     * @param labels The labels for the new issue.
     * @param assignees The user logins to be assigned to the new issue.
     */
    @Command(name="add", description=["Create a new issue in the repository."])
    fun add(
            @Option(names=["-t", "--title"], description=["The title of the new issue."], required=true)
                title: String,
            @Option(names=["-b", "--body"], description=["The body of the new issue."])
                body: String? = null,
            @Option(names=["-m", "--milestone"], description=["The number of the milestone for the new issue."])
                milestone: Int? = null,
            @Option(names=["-l", "--labels"], description=["The labels for the new issue"])
                labels: List<String>? = null,
            @Option(names=["-a", "--assignees"], description=["The user logins for users to be assigned to the issue."])
                assignees: List<String>? = null
    ) {
        val issue = this.service.createIssue(title, body, milestone, labels, assignees)
        displayIssue(issue)
    }

    /**
     * Sub-command to send a patch to a repository issue.
     * @param number The number of the issue to patch.
     * @param title The patched title for the issue.
     * @param body The patched body of the issue.
     * @param state The patched state of the issue.
     * @param milestone The patched milestone of  issue.
     * @param labels The patched labels of the issue.
     * @param assignees The patched list of assignees for the issue.w
     */
    @Command(name="patch", description=["Send a patch to an issue."])
    fun patch(
            @Option(names=["-n", "--number"], description=["The number of the issue to patch."], required=true)
                number: Int,
            @Option(names=["-t", "--title"], description=["The patched issue of the issue."], required=true)
                title: String,
            @Option(names=["-b", "--body"], description=["The patched body of the issue."])
                body: String? = null,
            @Option(names=["-s", "--state"], description=["The patched state of the issue."])
                state: State? = null,
            @Option(names=["-m", "--milestone"], description=["The patched number of the milestone for the issue."])
                milestone: Int? = null,
            @Option(names=["-l", "--labels"], description=["The patched labels for the issue"])
                labels: List<String>? = null,
            @Option(names=["-a", "--assignees"], description=["The patched user logins for users to be assigned to the issue."])
                assignees: List<String>? = null
    ) {
        val issue = this.service.editIssue(number, title, body, state.toString(), milestone, labels, assignees)
        displayIssue(issue)
    }
}