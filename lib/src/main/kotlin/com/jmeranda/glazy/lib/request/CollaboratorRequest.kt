package com.jmeranda.glazy.lib.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.jmeranda.glazy.lib.handler.Request

interface CollaboratorRequest : Request {
    val targetUser: String?
}

/**
 * Request class for GET requests.
 */
data class CollaboratorGetAllRequest (
    @JsonIgnore override val user: String,
    @JsonIgnore override val name: String,
    @JsonIgnore override val targetUser: String? = null,
    var affiliation: String
) : CollaboratorRequest

/**
 * Request class for POST requests.
 */
data class CollaboratorPutRequest (
    @JsonIgnore override val user: String,
    @JsonIgnore override val name: String,
    @JsonIgnore override val targetUser: String,
    var permission: String
) : CollaboratorRequest

/**
 * Request class for DELETE requests.
 */
data class CollaboratorDeleteRequest (
    @JsonIgnore override val user: String,
    @JsonIgnore override val targetUser: String,
    override val name: String
) : CollaboratorRequest