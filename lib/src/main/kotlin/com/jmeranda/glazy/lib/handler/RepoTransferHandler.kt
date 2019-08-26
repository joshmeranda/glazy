package com.jmeranda.glazy.lib.handler

import khttp.post
import khttp.responses.Response

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.request.RepoTransferRequest

class RepoTransferHandler(
        private val request: RepoTransferRequest,
        token: String?
): Handler<Repo>(token) {
    override fun handleRequest() {
        val data = this.getAuthorizationHeaders()
                .toMutableMap<String, Any?>()
                .put("team_ids", this.request.teamIds)
        val response: Response = post(this.getRequestUrl(),
                data = data)

        if (response.statusCode != 202) {
            println(response.jsonObject.get("message"))
        } else {
            println("transferred")
        }
    }
    
    override fun getRequestUrl(): String = Handler.endpoints.repositoryUrl
            .replace("{owner}", this.request.newOwner)
            .replace("{repo}", this.request.name)
            .plus("/transfer")
}