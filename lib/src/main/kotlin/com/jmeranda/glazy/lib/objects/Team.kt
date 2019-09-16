package com.jmeranda.glazy.lib.objects

/**
 * Describes a github team.
 */
data class Team (
        val id: Int,
        val nodeId: String,
        val url:String,
        val htmlUrl: String,
        val name: String,
        val slug: String,
        val description: String,
        val privacy: String,
        val permission: String,
        val membersUrl: String,
        val repositoriesUrl: String,
        val parent: Team? = null
)