package com.jmeranda.glazy.cli

import picocli.CommandLine
import picocli.CommandLine.Command

import com.jmeranda.glazy.cli.commands.*

/**
 * Main command entry point.
 */
@Command(name = "glazy",
        description = ["A command line interface to the github api."],
        mixinStandardHelpOptions = true)
class Glazy

fun main(args: Array<String>) {
    CommandLine(Glazy())
            .addSubcommand(CommandLine(IssueParent())
                    .addSubcommand(IssueList())
                    .addSubcommand(IssueAdd())
                    .addSubcommand(IssuePatch()))
            .addSubcommand(CommandLine(RepoParent())
                    .addSubcommand(RepoShow())
                    .addSubcommand(RepoList())
                    .addSubcommand(RepoInit())
                    .addSubcommand(RepoPatch())
                    .addSubcommand(RepoDelete())
                    .addSubcommand(RepoTransfer())
                    .setToggleBooleanFlags(true))
            .addSubcommand(CommandLine(CacheParent())
                    .addSubcommand(ClearCache())
                    .addSubcommand(RefreshCache())
                    .addSubcommand(TokenCache())
                    .setToggleBooleanFlags(true))
            .execute(*args)
}