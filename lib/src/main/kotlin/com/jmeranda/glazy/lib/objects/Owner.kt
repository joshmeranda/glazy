package com.jmeranda.glazy.lib.objects

/**
 * Describes the owner of a repository.
 */
data class Owner (
        val login: String,
        val id: Int,
        val nodeId: String,
        val avatarUrl: String,
        val gravatarId: String? = null,
        val url: String,
        val htmlUrl: String,
        val followersUrl: String,
        val followingUrl: String,
        val gistsUrl: String,
        val starredUrl: String,
        val subscriptionsUrl: String,
        val organizationsUrl: String,
        val reposUrl: String,
        val eventsUrl: String,
        val receivedEventsUrl: String,
        val type: String,
        val siteAdmin: Boolean
)