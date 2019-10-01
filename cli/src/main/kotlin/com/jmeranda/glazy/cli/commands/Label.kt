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
    @Option(names = ["-r", "--random"],
            description = ["Generate a random value for the color"])
    var randomColor: Boolean = false

    @Option(names = ["-c", "--color"],
            description = ["The color to use for the label."])
    lateinit var color: String
}

open class LabelCommand {
    var service: LabelService? = null
    private var token: String? = null

    init {
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
class LabelParent: Runnable {
    @Spec lateinit var spec: CommandSpec

    override fun run() {
        throw CommandLine.ParameterException(this.spec.commandLine(),
                "Missing required subcommand")
    }
}

@Command(name = "list",
        description = ["List all available repository labels."],
        mixinStandardHelpOptions = true)
class LabelList: Runnable, LabelCommand() {
    override fun run() {
        // Retrieve repository labels.
        val labelList: List<Label>? = this.service?.getAllLabels()

        // Display all retrieved labels.
        for (label: Label? in labelList ?: listOf()) {
            displayLabel(label ?: continue)
        }
    }
}

@Command(name = "add",
        description = ["Create a new label for the repository."],
        mixinStandardHelpOptions = true)
class LabelAdd: Runnable, LabelCommand() {
    @Option(names = ["-l", "--label"],
            description = ["The name of the new label."],
            required = true)
    private lateinit var label: String

    @ArgGroup(exclusive = true, multiplicity = "1")
    private lateinit var color: Color

    @Option(names = ["-d", "--description"],
            description = ["An optional description of the labael."])
    private var description: String? = null

    override fun run() {
        val realColor: String = when {
            this.color.randomColor -> (0x0..0xFFFFFF).random().toString(16)
            else -> this.color.color ?: String()
        }

        val label = this.service?.createLabel(this.label, realColor, this.description)
        displayLabel(label ?: return)
    }
}

@Command(name = "delete",
        description = ["Delete aa repository lbael."],
        mixinStandardHelpOptions = true)
class LabelDelete: Runnable, LabelCommand() {
    @Option(names = ["-l", "--labels"],
            description = ["The name of the label to remove."],
            required = true)
    private lateinit var label: String

    override fun run() {
        this.service?.deleteLabel(this.label)
    }
}