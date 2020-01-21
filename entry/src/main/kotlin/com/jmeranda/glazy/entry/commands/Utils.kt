import com.jmeranda.glazy.lib.objects.Issue
import com.jmeranda.glazy.lib.objects.Label
import com.jmeranda.glazy.lib.objects.PullRequest
import com.jmeranda.glazy.lib.objects.Repo

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

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
            "clone url: ${repo.cloneUrl}"

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
