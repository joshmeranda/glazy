package com.jmeranda.glazy.cli.commands

import picocli.CommandLine.Option
import picocli.CommandLine.Command


@Command(name = "repo",
        description = ["Perform operations on a  repository."],
        mixinStandardHelpOptions = true
)
class RepoParent(): Runnable {
    override fun run() {
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