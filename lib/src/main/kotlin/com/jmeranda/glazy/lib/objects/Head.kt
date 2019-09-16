package com.jmeranda.glazy.lib.objects

/**
 * TODO figure out what this actually is
 */
data class Head (
        val label: String,
        val ref: String,
        val sha: String,
        val user: Owner,
        val repo: Repo
)