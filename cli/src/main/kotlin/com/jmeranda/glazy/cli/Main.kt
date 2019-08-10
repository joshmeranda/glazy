package com.jmeranda.glazy.cli

import picocli.CommandLine

import com.jmeranda.glazy.cli.commands.*

fun main(args: Array<String>) {
    CommandLine(Glazy())
            .addSubcommand(CommandLine(IssueParent())
                    .addSubcommand(IssueList())
                    .addSubcommand(IssueAdd())
                    .addSubcommand(IssuePatch()))
            .addSubcommand(CommandLine(RepoParent())
                    .addSubcommand(RepoShow())
                    .addSubcommand(RepoInit())
                    .setToggleBooleanFlags(true))
            .execute(*args)
}