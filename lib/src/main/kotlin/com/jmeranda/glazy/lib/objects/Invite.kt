package com.jmeranda.glazy.lib.objects

import com.fasterxml.jackson.annotation.JsonIgnore

data class Invite (
    override val id: Int,
    @JsonIgnore override val nodeId: String = String(),
    val repository: Repo,
    val invitee: User,
    val inviter: User,
    val permissions: String,
    val createdAt: String,
    val url: String,
    val htmlUrl: String
) : GitObject(id, nodeId)