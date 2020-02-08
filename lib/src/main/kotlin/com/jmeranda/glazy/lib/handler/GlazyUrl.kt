package com.jmeranda.glazy.lib.handler

import com.jmeranda.glazy.lib.request.*

/**
 * Interface for all url cretion classes.
 *
 * @property request The request from which the endpoint url is created.
 * @property requestUrl The endpoint url.
 */
interface GlazyUrl {
    val request: Request?
    val requestUrl: String
}

/**
 * Interface for all request types in [com.jmeranda.glazy.lib.request]
 */
interface Request {
    val user: String
    val name: String
}

/**
 * Url class for the basic endpoint for all repository issues.
 *
 * @param request The request to be used for url creation.
 * @see [GlazyUrl]
 */
class GlazySimpleIssueUrl(override val request: Request) : GlazyUrl {
    override val requestUrl = Handler.endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
            .plus("/issues")
}

/**
 * url class for an endpoint to a specific repository issue.
 *
 * @param request The request to be used for url creation.
 * @see [GlazyUrl]
 */
class GlazyIssueUrl(override val request: IssueRequest) : GlazyUrl {
    override val requestUrl = Handler.endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
            .plus("/issues/${request.number}")
}

/**
 * Url class for an endpoint to a specific repository.
 *
 * @param request The request to be used for url creation.
 * @see [GlazyUrl]
 */
class GlazyRepoUrl(override val request: Request) : GlazyUrl {
    override val requestUrl = Handler.endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
}

/**
 * Url class for an endpoint to all repositories for an authenticated user.
 *
 * @param request The request to be used for url creation.
 * @see [GlazyUrl]
 */
class GlazyCurrentUserRepoUrl(override val request: Request? = null) : GlazyUrl {
    override var requestUrl: String = Handler.endpoints.currentUserRepositoriesUrl
            .replace(Regex("\\{.*}"), "")
    init {
        if (request is RepoGetRequest) {
            var parameters = String()

            if (request.visibility != null) {
                parameters += "${if (parameters.isEmpty()) {
                    "?"
                } else {
                    ""
                }}visibility=${request.visibility}"
            }

            if (request.affiliation != null) {
                parameters += "${if (parameters.isEmpty()) {
                    "?"
                } else {
                    "&"
                }}affiliation=${request.affiliation}"
            }

            if (parameters.isNotEmpty()) {
                requestUrl += parameters
            }
        }
    }
}

/**
 * Url class for an endpoint to a repositories pull requests.
 *
 * @param request The request to be used for url creation.
 * @see [GlazyUrl]
 */
class GlazySimplePullUrl(override val request: Request) : GlazyUrl {
    override val requestUrl = Handler.endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
            .plus("/pulls")
}

/**
 * Url class for an endpoint to a specific pull request.
 *
 * @param request The request to be used for url creation.
 * @see [GlazyUrl]
 */
class GlazyPullUrl(override val request: PullRequest) : GlazyUrl {
    override val requestUrl = Handler.endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
            .plus("/pulls/${request.number}")
}

/**
 * Url class for an endpoint to a repositories labels.
 *
 * @param request The request to be used for url creation.
 * @see [GlazyUrl]
 */
class GlazySimpleLabelUrl(override val request: Request) : GlazyUrl {
    override val requestUrl = Handler.endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
            .plus("/labels")
}

/**
 * Url class for an endpoint to a specific label.
 *
 * @param request The request to be used for url creation.
 * @see [GlazyUrl]
 */
class GlazyLabelUrl(override val request: LabelRequest) : GlazyUrl {
    override val requestUrl = Handler.endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
            .plus("/labels/${request.label}")
}

/**
 * Url class for an endpoint to a repositories forks.
 *
 * @param request The request to be used for url creation.
 * @see [GlazyUrl]
 */
class GlazyForkUrl(override val request: Request) : GlazyUrl {
    override val requestUrl = Handler.endpoints.repositoryUrl
            .replace("{owner}", this.request.user)
            .replace("{repo}", this.request.name)
            .plus("/forks")
}


/**
 * Url class for the basic endpoint of all collaborators.
 */
class GlazySimpleCollaboratorUrl(override val request: CollaboratorRequest) : GlazyUrl {
    override val requestUrl = Handler.endpoints.repositoryUrl
        .replace("{owner}", this.request.user)
        .replace("{repo}", this.request.name)
        .plus("/collaborators")
}

/**
 * Url class for an endpoint to a collaborator.
 */
class GlazyCollaboratorUrl(override val request: CollaboratorRequest) : GlazyUrl {
    override val requestUrl = Handler.endpoints.repositoryUrl
        .replace("{owner}", this.request.user)
        .replace("{repo}", this.request.name)
        .plus("/collaborators/${request.targetUser}")
}