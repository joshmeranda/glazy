package com.jmeranda.gitkot.client

import khttp.get
import com.beust.klaxon.Klaxon
import com.beust.klaxon.FieldRenamer

data class Endpoints (
        val currentUserUrl: String,
        val currentUserAuthorizationsHtmlUrl: String,
        val authorizationsUrl: String,
        val codeSearchUrl: String,
        val commitSearchUrl: String,
        val emailsUrl: String,
        val emojisUrl: String,
        val eventUrl: String,
        val eventsUrl: String,
        val feedsUrl: String,
        val followersUrl: String,
        val followingUrl: String,
        val gistsUrl: String,
        val hubUrl: String,
        val issueSearchUrl: String,
        val issuesUrl: String,
        val keysUrl: String,
        val notificationsUrl: String,
        val organizationRepositoriesUrl: String,
        val organizationUrl: String,
        val publicGistsUrl: String,
        val rateLimitUrl: String,
        val repositoryUrl: String,
        val currentUserRepositoriesUrl: String,
        val starredUrl: String,
        val starredGistsUrl: String,
        val teamUrl: String,
        val userUrl: String,
        val userOrganizationsUrl: String,
        val usrRepositoriesUrl: String,
        val userSearchUrl: String
        )

val fieldRenamer = object: FieldRenamer {
    override fun toJson(fieldName: String) = FieldRenamer.camelToUnderscores(fieldName)
    override fun fromJson(fieldName: String) = FieldRenamer.underscoreToCamel(fieldName)
}
internal val klaxon = Klaxon().fieldRenamer(fieldRenamer)

fun getEnpoints(apiUrl: String): Endpoints {
    val endpointsAsJson: String = get(apiUrl).text
    return klaxon.parse<Endpoints>(endpointsAsJson)
}