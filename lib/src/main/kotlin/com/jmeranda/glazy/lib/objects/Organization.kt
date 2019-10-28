package com.jmeranda.glazy.lib.objects

data class Organization (
        val login: String,
        val id: Int,
        val nodeId: String,
        val description: String? = null,
        val gravatarId: String? = null,
        val url: String,
        val htmlUrl: String? = null,
        val followersUrl: String? = null,
        val followingUrl: String? = null,
        val gistsUrl: String? = null,
        val starredUrl: String? = null,
        val subscriptionsUrl: String? = null,
        val organizationsUrl: String? = null,
        val reposUrl: String,
        val eventsUrl: String? = null,
        val receivedEventsUrl: String? = null,
        val hooksUrl: String? = null,
        val issuesUrl: String? = null,
        val membersUrl: String? = null,
        val publicMembersUrl: String? = null,
        val avatarUrl: String? = null,
        val type: String,
        val siteAdmin: Boolean? = null
)