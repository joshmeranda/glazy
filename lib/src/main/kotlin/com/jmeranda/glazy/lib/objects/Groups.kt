package com.jmeranda.glazy.lib.objects

/**
 * Describes a github team.
 */
data class Team(
        override val id: Int,
        override val nodeId: String,
        val createdAt: String,
        val description: String,
        val htmlUrl: String,
        val membersCount: Int,
        val membersUrl: String,
        val name: String,
        val organization: Organization,
        val parent: Team? = null,
        val permission: String,
        val privacy: String,
        val reposCount: Int,
        val repositoriesUrl: String,
        val slug: String,
        val updatedAt: String,
        val url: String
) : GitObject(id, nodeId)

/**
 * Describes a github organization.
 */
data class Organization(
        override val id: Int,
        override val nodeId: String,
        val avatarUrl: String? = null,
        val blog: String? = null,
        val company: String? = null,
        val createdAt: String? = null,
        val description: String? = null,
        val email: String? = null,
        val eventsUrl: String? = null,
        val followersUrl: String,
        val followingUrl: String,
        val gistsUrl: String,
        val gravatarId: String? = null,
        val hasOrganizationProjects: Boolean,
        val hasRepositoryProjects: Boolean,
        val hooksUrl: String? = null,
        val htmlUrl: String? = null,
        val isVerified: Boolean,
        val issuesUrl: String? = null,
        val location: String? = null,
        val login: String,
        val membersUrl: String? = null,
        val name: String? = null,
        val organizationsUrl: String? = null,
        val publicGists: Int,
        val publicMembersUrl: String? = null,
        val publicRepos: Int,
        val receivedEventsUrl: String? = null,
        val reposUrl: String,
        val siteAdmin: Boolean,
        val starredUrl: String? = null,
        val subscriptionsUrl: String? = null,
        val type: String,
        val url: String
) : GitObject(id, nodeId)