package com.jmeranda.glazy.cli

import kotlin.system.exitProcess

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.service.RepoService

fun main(args: Array<String>) {
    println("=== READING ===")
    val (user: String?, name: String?) = getRepoName()

    println("user: $user\nrepo: $name")

    if (user == null || name == null) { exitProcess(1) }

    val service = RepoService()
    val repo: Repo? = service.getRepo(name, user)

    println("=== DONE ===")
}