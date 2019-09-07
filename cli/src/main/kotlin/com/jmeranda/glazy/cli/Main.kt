package com.jmeranda.glazy.cli

import picocli.CommandLine
import picocli.CommandLine.Command

import com.jmeranda.glazy.cli.commands.*

/**
 * Parent for all sub-commands.
 */
@Command(name = "glazy",
        description = ["A command line interface to the github api."],
        mixinStandardHelpOptions = true)
class Glazy

fun main(args: Array<String>) {
    CommandLine(Glazy())
            // Issue sub-command
            .addSubcommand(CommandLine(IssueParent())
                    .addSubcommand(IssueList())
                    .addSubcommand(IssueAdd())
                    .addSubcommand(IssuePatch()))
            .setCaseInsensitiveEnumValuesAllowed(true)
            // Repo sub-command
            .addSubcommand(CommandLine(RepoParent())
                    .addSubcommand(RepoShow())
                    .addSubcommand(RepoList())
                    .addSubcommand(RepoInit())
                    .addSubcommand(RepoPatch())
                    .addSubcommand(RepoDelete())
                    .addSubcommand(RepoTransfer())
                    .setToggleBooleanFlags(true))
            // Cache sub-command
            .addSubcommand(CommandLine(CacheParent())
                    .addSubcommand(ClearCache())
                    .addSubcommand(RefreshCache())
                    .addSubcommand(TokenCache())
                    .setToggleBooleanFlags(true))
            .execute(*args)
}