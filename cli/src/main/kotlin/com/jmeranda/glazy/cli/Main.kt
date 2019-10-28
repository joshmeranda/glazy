package com.jmeranda.glazy.cli

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Spec
import picocli.CommandLine.Model.CommandSpec

import com.jmeranda.glazy.cli.commands.*

/**
 * Parent for all sub-commands.
 */
@Command(name = "glazy",
        description = ["A command line interface to the github api."],
        mixinStandardHelpOptions = true)
class Glazy: Runnable {
    @Spec lateinit var spec: CommandSpec

    override fun run() {
        throw CommandLine.ParameterException(this.spec.commandLine(),
                "Missing required subcommand")
    }
}

fun main(args: Array<String>) {
    CommandLine(Glazy())
            // Issue sub-command
            .addSubcommand(CommandLine(
                    IssueParent())
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
                    .addSubcommand(RepoFork())
                    .setToggleBooleanFlags(true))
            // Cache sub-command
            .addSubcommand(CommandLine(CacheParent())
                    .addSubcommand(ClearCache())
                    .addSubcommand(RefreshCache())
                    .addSubcommand(TokenCache())
                    .setToggleBooleanFlags(true))
            // Pull Request sub-command
            .addSubcommand(CommandLine(PullParent())
                    .addSubcommand(PullList())
                    .addSubcommand(PullInit())
                    .addSubcommand(PullUpdate()))
            // Label sub-command
            .addSubcommand(CommandLine(LabelParent())
                    .addSubcommand(LabelList())
                    .addSubcommand(LabelAdd())
                    .addSubcommand(LabelDelete())
                    .addSubcommand(LabelPatch())
                    .setToggleBooleanFlags(true))
            .execute(*args)
}
