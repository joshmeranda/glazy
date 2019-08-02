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
class Issue(private val repo: Repo, private val token: String?) {
    private val service = IssueService(repo, token)

    /**
     * Subcommand to list repository issues or issue.
     * @param number The number of the target issue, if non specified
     *     all 'open' issues are found.
     */
    @Command(name="list", description=["List issue(s) from the repository."])
    fun list(@Option(names=["-n", "--number"], description=["The number of the desired issue"]) number: Int? = null) {
        val issueList: List<Issue> = if (number == null) {
            this.service.getAllIssues()
        } else {
            listOf(this.service.getIssue(number))
        }

        for (issue: Issue in issueList) {
            displayIssue(issue)
        }
    }
}