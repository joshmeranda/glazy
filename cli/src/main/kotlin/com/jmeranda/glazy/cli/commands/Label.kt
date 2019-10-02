package com.jmeranda.glazy.cli.commands

import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Spec
import picocli.CommandLine.ArgGroup
import picocli.CommandLine.Model.CommandSpec
import com.jmeranda.glazy.cli.getRepoName
import com.jmeranda.glazy.lib.objects.Label
import com.jmeranda.glazy.lib.service.CacheService
import com.jmeranda.glazy.lib.service.LabelService
import picocli.CommandLine

class Color {
    @Option(names = ["-c", "--color"],
            description = ["The color to use for the label."])
    var color: String? = null

    @Option(names = ["-r", "--random-color"],
            description = ["Use a randomly generated hex value for the color."])
    var randomColor = false
}

open class LabelCommand {
    protected lateinit var service: LabelService
    private var token: String? = null

    protected fun startService() {
        val (user, name) = getRepoName()

        if (user != null) token = CacheService.token(user)

        if (name != null && user != null) {
            this.service = LabelService(user, name, token)
        }
    }

    companion object {
        fun displayLabel(label: Label) {
            println(label.name)
        }
    }
}

@Command(name = "label",
        description = ["Perform operations on repository labels"],
        mixinStandardHelpOptions = true)
class LabelParent : Runnable {
    @Spec lateinit var spec: CommandSpec

    override fun run() {
        throw CommandLine.ParameterException(this.spec.commandLine(),
                "Missing required subcommand")
    }
}

@Command(name = "list",
        description = ["List all available repository labels."],
        mixinStandardHelpOptions = true)
class LabelList : Runnable, LabelCommand() {
    override fun run() {
        this.startService()

        // Retrieve repository labels.
        val labelList: List<Label>? = this.service.getAllLabels()

        // Display all retrieved labels.
        for (label: Label? in labelList ?: listOf()) {
            displayLabel(label ?: continue)
        }
    }
}

@Command(name = "add",
        description = ["Create a new label for the repository."],
        mixinStandardHelpOptions = true)
class LabelAdd : Runnable, LabelCommand() {
    @Option(names = ["-l", "--label"],
            description = ["The name of the new label."],
            required = true)
    private lateinit var label: String

    @ArgGroup(exclusive = true, multiplicity = "0..1")
    private val color: Color? = null

    @Option(names = ["-d", "--description"],
            description = ["An optional description of the label."])
    private var description: String? = null

    override fun run() {
        this.startService()

        val realColor: String = when {
            // If no color or randoms specified, generate random hex code.
            this.color == null  || this.color.randomColor -> (0x0..0xFFFFFF).random().toString(16)
            else -> this.color.color ?: String()
        }

        val label = this.service.createLabel(this.label, realColor, this.description)
        displayLabel(label ?: return)
    }
}

@Command(name = "delete",
        description = ["Delete aa repository lbael."],
        mixinStandardHelpOptions = true)
class LabelDelete : Runnable, LabelCommand() {
    @Option(names = ["-l", "--labels"],
            description = ["The name of the label to remove."],
            required = true)
    private lateinit var label: String

    override fun run() {
        this.startService()
        this.service.deleteLabel(this.label)
    }
}

@Command(name = "patch",
        description = ["Patch a repository label."],
        mixinStandardHelpOptions = true)
class LabelPatch : Runnable, LabelCommand() {
    @Option(names = ["-l", "--label"],
            description = ["The name of the label to edit."],
            required = true)
    private lateinit var label: String

    @Option(names = ["-n", "--new-label"],
            description = ["The new name for the label."])
    private var newLabel: String? = null

    @ArgGroup(exclusive = true, multiplicity = "0..1")
    private val color: Color? = null

    @Option(names = ["-d", "--description"],
            description = ["An optional description of the labael."])
    private var description: String? = null

    override fun run() {
        this.startService()

        val realColor: String = when {
            // If no color or randoms specified, generate random hex code.
            this.color == null  || this.color.randomColor -> (0x0..0xFFFFFF).random().toString(16)
            else -> this.color.color ?: String()
        }

        val label = this.service.patchLabel(this.label, this.newLabel, realColor, this.description)
        displayLabel(label ?: return)
    }
}