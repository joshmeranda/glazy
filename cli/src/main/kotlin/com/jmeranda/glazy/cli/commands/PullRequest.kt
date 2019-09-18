package com.jmeranda.glazy.cli.commands

import picocli.CommandLine.Command

open class PullCommand

@Command(name = "pull",
        description = ["Perform operations on pull requests"],
        mixinStandardHelpOptions = true)
class PullParent

@Command(name = "list",
        description = ["List pull requests"],
        mixinStandardHelpOptions = true)
class PullList: PullCommand()