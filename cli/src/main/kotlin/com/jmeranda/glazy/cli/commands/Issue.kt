package com.jmeranda.glazy.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int

import com.jmeranda.glazy.lib.Issue
import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.service.IssueService

/**
 * Issue command to summarize an issue or list of issues.
 *
 * @property repo The repo to search for issues in.
 * @property token The access token of the user.
 */
class Issue(
        private val repo: Repo,
        private val token: String? = null
): CliktCommand() {
    private val service = IssueService(repo, token)

    /* Options */
    private val detailed by option("-d", "--detailed",
            help = "Display a more detailed description.")
            .flag(default = false)
    private val all by option("-a", "--all",
            help = "Display all issues, meaning closed and open issues.")
            .flag(default = false)

    /* Arguments */
    private val number: Int? by argument(help = "Specify the issue number.")
            .int()
            .optional()

    /**
     * Print summary about an issue.
     *
     * @param issue The issue to be summarized.
     * @param detailed Show more information about an issue.
     */
    private fun showIssue(issue: Issue, detailed: Boolean) {
        println("${issue.number} ] ${issue.title}")

        if (detailed) {
            println("opened: ${issue.closedAt}")
            println("assigned to: ${issue.assignee?.login}")
            println(issue.body)
        }
    }

    override fun run() {
        var issues: List<Issue> = listOf()

        issues = if (this.number == null) {
            service.getAllIssues()
        } else  {
            listOf(service.getIssue(this.number!!))
        }

        for (issue in issues) {
            if (issue.state == "open" || this.all) {
                this.showIssue(issue, this.detailed)
            }
        }
    }
}