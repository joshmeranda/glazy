package com.jmeranda.glazy.entry.commands

import com.jmeranda.glazy.entry.Verbose
import picocli.CommandLine
import picocli.CommandLine.ArgGroup
import picocli.CommandLine.Command
import picocli.CommandLine.Mixin
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import picocli.CommandLine.Spec
import picocli.CommandLine.Model.CommandSpec

import com.jmeranda.glazy.lib.handler.Handler
import com.jmeranda.glazy.lib.objects.Label
import com.jmeranda.glazy.lib.service.LabelService
import com.jmeranda.glazy.lib.service.getToken
import com.jmeranda.glazy.lib.service.getRepoName

/**
 * Parent class for all label commands.
 *
 * @property service The [com.jmeranda.glazy.lib.service.LabelService] utilized bu label commands.
 * @property token The token to use for api authentication.
 */
sealed class LabelCommand {
    @Mixin
    var verbose: Verbose? = null
    protected lateinit var service: LabelService
    private var token: String? = null

    protected fun initService() {
        val (user, name) = getRepoName()

        if (user != null) token = getToken()

        if (name != null && user != null) {
            this.service = LabelService(user, name, token)
        }
        Handler.verbose = this.verbose?.verbose ?: false
    }

    companion object {
        /**
         * Option group for specifying the color of a label.
         *
         * @property color Uses the specified color.
         * @property randomColor Generates a random color
         */
        class Color {
            @Option(names = ["-c", "--color"],
                    description = ["The color to use for the label."])
            var color: String? = null

            @Option(names = ["-r", "--random-color"],
                    description = ["Use a randomly generated hex value for the color."])
            var randomColor = false
        }
    }
}

/**
 * Provide parent class for commands with label title as positional parameter.
 *
 * @property label The title for the label.
 */
sealed class LabelParameterCommand : LabelCommand() {
    @Parameters(index = "0", description = ["The number of the target label."])
    protected lateinit var label: String
}

/**
 * Parent command for all label sub-commands.
 */
@Command(name = "label",
        description = ["Perform operations on repository labels"],
        mixinStandardHelpOptions = true)
class LabelParent : Runnable {
    @Spec lateinit var spec: CommandSpec

    /**
     * When run before all child classes, with end program if no sub-command is passed as an argument.
     *
     * @throws CommandLine.ParameterException When no sub-command is entered by terminal.
     */
    override fun run() {
        throw CommandLine.ParameterException(this.spec.commandLine(),
                "Missing required subcommand")
    }
}

/**
 * List all labels in a repository.
 */
@Command(name = "list",
        description = ["List all available repository labels."],
        mixinStandardHelpOptions = true)
class LabelList : Runnable, LabelCommand() {
    override fun run() {
        this.initService()

        // Retrieve repository labels.
        val labelList: List<Label>? = this.service.getAllLabels()

        // Display all retrieved labels.
        for (label: Label? in labelList ?: listOf()) {
            displayLabel(label ?: continue)
        }
    }
}

/**
 * Create new label for a repository.
 *
 * @property color The color for the new label.
 * @property description The description for the new label.
 */
@Command(name = "add",
        description = ["Create a new label for the repository."],
        mixinStandardHelpOptions = true)
class LabelAdd : Runnable, LabelParameterCommand() {
    @ArgGroup(exclusive = true, multiplicity = "0..1")
    private val color: Companion.Color? = null

    @Option(names = ["-d", "--description"],
            description = ["An optional description of the label."])
    private var description: String? = null

    override fun run() {
        this.initService()

        val realColor: String = when {
            // If no color or randoms specified, generate random hex code.
            this.color == null  || this.color.randomColor -> (0x0..0xFFFFFF).random().toString(16)
            else -> this.color.color ?: String()
        }

        val label = this.service.createLabel(this.label, realColor, this.description)
        displayLabel(label ?: return)
    }
}

/**
 * Delete existing repository labels.
 */
@Command(name = "delete",
        description = ["Delete aa repository lbael."],
        mixinStandardHelpOptions = true)
class LabelDelete : Runnable, LabelParameterCommand() {
    override fun run() {
        this.initService()
        this.service.deleteLabel(this.label)
    }
}

/**
 * Edit exisiting repository labels.
 *
 * @property newLabel The new title for the label.
 * @property color The new color for the label.s
 * @property description The new description for the label.
 */
@Command(name = "patch",
        description = ["Patch a repository label."],
        mixinStandardHelpOptions = true)
class LabelPatch : Runnable, LabelParameterCommand() {
    @Option(names = ["-n", "--new-label"],
            description = ["The new name for the label."])
    private var newLabel: String? = null

    @ArgGroup(exclusive = true, multiplicity = "0..1")
    private val color: Companion.Color? = null

    @Option(names = ["-d", "--description"],
            description = ["An optional description of the labael."])
    private var description: String? = null

    override fun run() {
        this.initService()
        val realColor: String = when {
            // If no color or randoms specified, generate random hex code.
            this.color == null  || this.color.randomColor -> (0x0..0xFFFFFF).random().toString(16)
            else -> this.color.color ?: String()
        }

        val label = this.service.patchLabel(this.label, this.newLabel, realColor, this.description)
        displayLabel(label ?: return)
    }
}