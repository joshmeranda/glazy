package com.jmeranda.glazy.entry.commands

import com.jmeranda.glazy.lib.objects.*

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties


/**
 * Build a three letter description of a users permissions level.
 *
 * The three letters refer to admin, push, and pull respectively. While all users with push
 * permissions have pull permissions, both are listed to differentiate between a user with only
 * pull acess ('p') and a user with push access ('pp')
 *
 * @param user The user object who permissions to represent as a string.
 * @return A three letter string denoting a users permission level.
 */
fun getPermissionString(user: User): String {
    return StringBuilder()
        .append(if (user.permissions!!.admin) { "a" } else { " " })
        .append(if (user.permissions!!.push) { "p" } else { " " })
        .append(if (user.permissions!!.pull) { "p" } else { " " })
        .toString()
}

/**
 * Display a label to the console.
 */
fun displayLabel(label: Label) {
    println(label.name)
}

/**
 * Display an issue to the console.
 *
 * @param issue The issue to display.
 */
fun displayIssue(issue: Issue) {
    println("[${issue.number}] ${issue.title}")
}

/**
 * Display a pull request.
 *
 * @param pullRequest
 * @param fields The specific fields to display in addition to the default ones.
 */
fun displayPullRequest(pullRequest: PullRequest, fields: List<String>?) {
    var details = "[${pullRequest.number}] ${pullRequest.title}\n" +
            "draft : ${pullRequest.draft}\n" +
            "head: ${pullRequest.head.label}\n" +
            "base: ${pullRequest.base.label}\n" +
            "created: ${pullRequest.createdAt}"

    val badFields = mutableListOf<String>()

    // Concatenate additional fields to the details string.
    for (field in fields ?: listOf()) {
        // If property exists in class add to details, if not add to badFields.
        try {
            // Get repo property via input fields.
            val property = pullRequest::class
                    .memberProperties
                    .first { it.name == field }
                    as? KProperty1<PullRequest, Any>
            // Print the field name and value to the console.
            if (property != null) details += "\n$field: ${property.get(pullRequest)}"
        } catch (e: Exception) {
            badFields.add(field)
        }
    }

    // Notify user of unrecognized fields
    if (badFields.size > 0) details += "\n\nglazy: Could not recognize field(s) '${badFields.joinToString()}'.\n" +
            "Please see 'https://developer.github.com/v3/repos/#list-your-repositories' for available fields"

    println(details)
}

/**
 * Display a repository.
 *
 * @param repo The repository to display/
 * @param fields The list of fields to display in addition to the default ones.
 */
fun displayRepo(repo: Repo, fields: List<String>?) {
    var details = "full name: ${repo.fullName}\n" +
            "private: ${repo.private}\n" +
            "created: ${repo.createdAt}\n" +
            "https url: ${repo.cloneUrl}\n" +
            "ssh url: ${repo.sshUrl}\n"

    if (repo.isTemplate != null) {
        details += "is template: ${repo.isTemplate}"
    }

    if (repo.template != null) {
        details += "template: ${repo.template}"
    }

    val badFields = mutableListOf<String>()

    // Concatenate additional fields to the details string.
    for (field in fields ?: listOf()) {
        // If property exists in class add to details, if not add to badFields.
        try {
            // Get repo property via input fields.
            val property = repo::class
                    .memberProperties
                    .first { it.name == field }
                    as? KProperty1<Repo, Any>
            // Print the field name and value to the console.
            if (property != null) details += "\n$field: ${property.get(repo)}"
        } catch (e: Exception) {
            badFields.add(field)
        }
    }

    // Notify user of unrecognized fields
    if (badFields.size > 0) details += "\n\nglazy: Could not recognize field(s) '${badFields.joinToString()}'.\n" +
            "Please see 'https://developer.github.com/v3/repos/#list-your-repositories' for available fields"

    println(details)
}

/**
 * Display a collaborator.
 */
fun displayCollaborator(collaborator: User) {
    println(" ${getPermissionString(collaborator)} ${collaborator.login}")
}

/**
 * Display a collaborator invitation.
 */
fun displayInvite(invite: Invite) {
    println("User '${invite.inviter.login}' added '${invite.invitee.login}' as a collaborator.")
}
