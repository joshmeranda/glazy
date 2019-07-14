package com.jmeranda.glazy.cli

import com.jmeranda.glazy.lib.Issue
import kotlin.system.exitProcess

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.exception.NoSuchRepo
import com.jmeranda.glazy.lib.service.*

fun main(args: Array<String>) {
    var (user: String?, name: String?) = getRepoName()

    if (user == null || name == null) { exitProcess(1) }

    name = "bash-full"

    val service = RepoService()
    val repo: Repo = service.getRepo(name, user) ?: throw NoSuchRepo(name)
    val issueService = IssueService(repo)

//    val issue = issueService.getIssue(7)
//    println("issue: ${issue.title}")

    val issues = issueService.getAllIssues()
    for (i: Issue in issues) {
        println("issue: ${i.title}")
    }
}