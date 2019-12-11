package com.jmeranda.glazy.lib.objects

/**
 * Describes the owner of a repository.
 */
data class User(
        override val id: Int,
        override val nodeId: String,
        val avatarUrl: String,
        val eventsUrl: String,
        val followersUrl: String,
        val followingUrl: String,
        val gistsUrl: String,
        val gravatarId: String? = null,
        val htmlUrl: String,
        val login: String,
        val organizationsUrl: String,
        val receivedEventsUrl: String,
        val reposUrl: String,
        val siteAdmin: Boolean,
        val starredUrl: String,
        val subscriptionsUrl: String,
        val type: String,
        val url: String
) : GitObject(id, nodeId)