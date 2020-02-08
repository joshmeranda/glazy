package com.jmeranda.glazy.lib.objects

data class Invite (
    override val id: Int,
    override val nodeId: String,
    val repository: Repo,
    val invitee: User,
    val inviter: User,
    val permission: String,
    val createdAt: String,
    val url: String,
    val htmlUrl: String
) : GitObject(id, nodeId)