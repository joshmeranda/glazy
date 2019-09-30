package com.jmeranda.glazy.cli.commands

import picocli.CommandLine.Command
import picocli.CommandLine.Spec
import picocli.CommandLine.Model.CommandSpec
import com.jmeranda.glazy.cli.getRepoName
import com.jmeranda.glazy.lib.objects.Label
import com.jmeranda.glazy.lib.service.CacheService
import com.jmeranda.glazy.lib.service.LabelService
import picocli.CommandLine

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