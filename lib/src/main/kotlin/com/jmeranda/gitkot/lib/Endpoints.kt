package com.jmeranda.gitkot.lib

import khttp.get
import com.beust.klaxon.Klaxon
import com.beust.klaxon.FieldRenamer

/**
 * data class representing the
 */
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

/* Allow for simpler Json deserialization (EG) stud_puffin -> studPuffin*/
val fieldRenamer = object: FieldRenamer {
    override fun toJson(fieldName: String) = FieldRenamer.camelToUnderscores(fieldName)
    override fun fromJson(fieldName: String) = FieldRenamer.underscoreToCamel(fieldName)
}
internal val klaxon = Klaxon().fieldRenamer(fieldRenamer)

const val BASE_URL: String = "https://api.github.com/"

/**
 * Get a class representing all available github endpoint as of v3.
 *
 * @return Endpoint? deserialized json response
 */
fun getEnpoints(): Endpoints? {
    val endpointsAsJson: String = get(BASE_URL).text
    return klaxon.parse<Endpoints>(endpointsAsJson)
}