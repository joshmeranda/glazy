package com.jmeranda.glazy.lib.handler

import com.jmeranda.glazy.lib.request.*

/**
 * Interface for all request types in [com.jmeranda.glazy.lib.request]
 */
interface Request {
    val user: String
    val name: String
}

/**
 * Retrieve the root endpoint url for issues.
 *
 * @param request The request to be used for url creation.
 * @return The root endpoint url for issues.
 */
fun issueRootUrl(request: Request) = Handler.endpoints.repositoryUrl
            .replace("{owner}", request.user)
            .replace("{repo}", request.name)
            .plus("/issues")

/**
 * Retrieve the endpoint for a specific issue.
 *
 * @param request The request to be used for url creation.
 * @return The endpoint url for issues.
 */
fun issueUrl(request: IssueRequest) = Handler.endpoints.repositoryUrl
            .replace("{owner}", request.user)
            .replace("{repo}", request.name)
            .plus("/issues/${request.number}")

/**
 * Retrieve the root endpoint for a specific repository.
 *
 * @param request The request to be used for url creation.
 * @return The endpoint url for a repository.
 */
fun repoUrl(request: Request) = Handler.endpoints.repositoryUrl
    .replace("{owner}", request.user)
    .replace("{repo}", request.name)

/**
 * Retrieve the root endpoint for an authenticated user's repositories.
 *
 * @return The root endpoint url for authenticated users.
 */
fun currentUserRepoUrl() = Handler.endpoints.currentUserRepositoriesUrl
    .replace(Regex("\\{.*}"), "")

/**
 * Retrieve the root endpoint for an authenticated user's repositories with filters.
 *
 * @param request The request to be used for parameter creation.
 * @return The root endpoint with the proper query parameters.
 */
fun currentUserRepoUrlQuery(request: RepoGetRequest): String {
    var url = Handler.endpoints.currentUserRepositoriesUrl
        .replace(Regex("\\{.*}"), "")
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
        url += parameters
    }

    return url
}

/**
 * Retrieve the root endpoint url for repositories pull requests.
 *
 * @param request The request to use for url creation.
 * @return The root endpoint for pull requests.
 */
fun pullRootUrl(request: Request) = Handler.endpoints.repositoryUrl
    .replace("{owner}", request.user)
    .replace("{repo}", request.name)
    .plus("/pulls")

/**
 * Retrieve the endpoint for a specific repository pull request.
 *
 * @param request The request to use for url creation.
 * @return The endpoint url for a pull request.
 */
fun pullUrl(request: PullRequest) = Handler.endpoints.repositoryUrl
    .replace("{owner}", request.user)
    .replace("{repo}", request.name)
    .plus("/pulls/${request.number}")

/**
 * Retrieve the root endpoint url for a repositories labels.
 *
 * @param request The request to use for url creation.
 * @return The root endpoint url for labels.
 */
fun labelRootUrl(request: Request) = Handler.endpoints.repositoryUrl
    .replace("{owner}", request.user)
    .replace("{repo}", request.name)
    .plus("/labels")

/**
 * Retrieve the endpoint url for a specific label.
 *
 * @param request The request to use for url creation.
 * @return The endpoint url for a specific label.
 */
fun labelUrl(request: LabelRequest) = Handler.endpoints.repositoryUrl
    .replace("{owner}", request.user)
    .replace("{repo}", request.name)
    .plus("/labels/${request.label}")

/**
 * Retrieve the root endpoint url for a repository's forks.
 *
 * @param request The request to use for url creation.
 * @return The root endpoint url for a repository's forks.
 */
fun forkRootUrl(request: Request) = Handler.endpoints.repositoryUrl
    .replace("{owner}", request.user)
    .replace("{repo}", request.name)
    .plus("/forks")

/**
 * Retrieve the root endpoint url for a repositories collaborators.
 *
 * @param request The request to use for url creation.
 * @return The root endpoint url for all collaborators.
 */
fun collaboratorRootUrl(request: Request) = Handler.endpoints.repositoryUrl
    .replace("{owner}", request.user)
    .replace("{repo}", request.name)
    .plus("/collaborators")

/**
 * Retrieve the endpoint url for a specific collaborator.
 *
 * @param request The request to use for url creation.
 * @return The endpoint url for a specific collaborator.
 */
fun collaboratorUrl(request: CollaboratorRequest) = Handler.endpoints.repositoryUrl
    .replace("{owner}", request.user)
    .replace("{repo}", request.name)
    .plus("/collaborators/${request.targetUser}")

fun templateUrl(request: RepoTemplateRequest) = Handler.endpoints.repositoryUrl
    .replace("{owner}", request.templateOwner)
    .replace("{repo}", request.templateName)
    .plus("/generate")