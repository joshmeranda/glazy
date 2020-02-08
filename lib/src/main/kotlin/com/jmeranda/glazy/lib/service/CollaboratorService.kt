package com.jmeranda.glazy.lib.service

import com.jmeranda.glazy.lib.handler.*
import com.jmeranda.glazy.lib.objects.GitObject
import com.jmeranda.glazy.lib.objects.Invite
import com.jmeranda.glazy.lib.objects.User
import com.jmeranda.glazy.lib.request.CollaboratorDeleteRequest
import com.jmeranda.glazy.lib.request.CollaboratorGetAllRequest
import com.jmeranda.glazy.lib.request.CollaboratorPostRequest

/**
 * Service providing access to operating on repository issues.
 *
 * @see [Service]
 */
class CollaboratorService(user: String, name: String, token: String?): Service(user, name, token) {
    /**
     * Retrieve a lists of all collaborators for a repository.
     *
     * @param affiliation The affiliation which listed user must have, can be one of outside,
     *      direct, or all.
     * @return The list of all users which have the specified affiliation.
     */
    fun getAllCollaborators(affiliation: String): List<User>? {
        val request = CollaboratorGetAllRequest(this.user, this.name, null, affiliation)
        val header = GlazySimpleHeader(this.token)
        val url = GlazySimpleCollaboratorUrl(request)
        val handler = GetHandler(header, url, User::class)

        return handler.handleListRequest()
            ?.map { obj -> obj as User }

    }

    /**
     * Send an invite to a user to collaborate on a repository.
     *
     * @param targetUser The username of the invitee.
     * @param permission The permission level the should have once accepte. Can be one of push,
     *      pull, or admin.
     * @return The invite created.
     */
    fun addCollaborator(targetUser: String, permission: String): Invite? {
        val request = CollaboratorPostRequest(this.user, this.name, targetUser, permission)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyCollaboratorUrl(request)
        val handler = PostHandler(header, url, Invite::class)

        return handler.handleRequest() as Invite?
    }

    /**
     * Remove a user as a collaborator of a repository.
     *
     * @param targetUser The username of target user to remove.
     */
    fun removeCollaborator(targetUser: String) {
        val request = CollaboratorDeleteRequest(this.user, this.name, targetUser)
        val header = GlazySimpleHeader(this.token)
        val url = GlazyCollaboratorUrl(request)
        val handler = DeleteHandler(header, url, GitObject::class)

        return handler.handleNoRequest()
    }
}