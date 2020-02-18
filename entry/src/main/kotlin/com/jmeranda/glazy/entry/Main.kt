package com.jmeranda.glazy.entry

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.IExecutionExceptionHandler
import picocli.CommandLine.Mixin
import picocli.CommandLine.Option
import picocli.CommandLine.Spec
import picocli.CommandLine.Model.CommandSpec

import com.jmeranda.glazy.entry.commands.*
import com.jmeranda.glazy.lib.exception.NotInRepo
import java.lang.Exception
import java.time.format.DateTimeFormatter

/**
 * Exception handler to specially handle NotInRepo by notifying user.
 */
class OutsideGitHandler() : IExecutionExceptionHandler {
    override fun handleExecutionException(e: Exception?, cl: CommandLine?, pr: CommandLine.ParseResult?): Int {
        if (e is NotInRepo) {
            cl?.err?.println(e.message);
        } else {
            e?.printStackTrace()
        }

        return 1
    }
}

class Verbose {
    @Option(names=["-v" ,"--verbose"], description=["Show verbose output."])
    val verbose: Boolean? = null
}

/**
 * Parent for all sub-commands.
 */
@Command(name = "glazy",
        description = ["A command line interface to the github api."],
        mixinStandardHelpOptions = true)
class Glazy: Runnable {
    @Mixin
    val verbose: Verbose? = null

    @Spec lateinit var spec: CommandSpec

    /**
     * When run before all child classes, with end program if no sub-command is passed as an argument.
     *
     * @throws CommandLine.ParameterException When no sub-command is entered by terminal.
     */
    override fun run() {
        throw CommandLine.ParameterException(this.spec.commandLine(),
                "Missing required sub-command")
    }
}

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

fun main(args: Array<String>) {
    val cli = CommandLine(Glazy())
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
                    .addSubcommand(RepoTemplate())
                    .addSubcommand(RepoTransfer())
                    .addSubcommand(RepoPatch())
                    .addSubcommand(RepoDelete())
                    .addSubcommand(RepoFork())
                    .setToggleBooleanFlags(true))
            // Cache sub-command
            .addSubcommand(CommandLine(CacheParent())
                    .addSubcommand(ClearCache())
                    .addSubcommand(RefreshCache())
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
            // Collaborator sub-command
            .addSubcommand(CommandLine(CollaboratorParent())
                    .addSubcommand(CollaboratorList())
                    .addSubcommand(CollaboratorAdd())
                    .addSubcommand(CollaboratorRemove())
                    .setToggleBooleanFlags(true))
            .setExecutionExceptionHandler(OutsideGitHandler())

    cli.execute(*args)
}
