package com.jmeranda.glazy.lib.handler

import com.jmeranda.glazy.lib.request.IssueRequest
import com.jmeranda.glazy.lib.request.PullRequest

interface GlazyUrl {
    val request: Request?
    val requestUrl: String
}

interface Request {
    val user: String
    val name: String
}

class GlazySimpleIssueUrl(override val request: Request) : GlazyUrl {
    override val requestUrl = Handler.endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
            .plus("/issues")
}

// TODO use repo issues url
class GlazyIssueUrl(override val request: IssueRequest) : GlazyUrl {
    override val requestUrl = Handler.endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
            .plus("/issues/${request.number}")
}

class GlazyRepoUrl(override val request: Request) : GlazyUrl {
    override val requestUrl = Handler.endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
}

class GlazyCurrentUserRepoUrl : GlazyUrl {
    override val request: Request? = null
    override val requestUrl: String = Handler.endpoints.currentUserRepositoriesUrl
            .replace(Regex("\\{.*}"), "")
}

class GlazySimplePullUrl(override val request: Request) : GlazyUrl {
    override val requestUrl = Handler.endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
            .plus("/pulls")
}

// TODO user repo pulls url
class GlazyPullUrl(override val request: PullRequest) : GlazyUrl {
    override val requestUrl = Handler.endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
            .plus("/pulls/${request.number}")
}