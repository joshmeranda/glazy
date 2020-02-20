package com.jmeranda.glazy.lib.service

import java.lang.System
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

import com.jmeranda.glazy.lib.objects.Repo
import com.jmeranda.glazy.lib.objects.RootEndpoints

private var CACHE_PATH = "${System.getProperty("user.home")}/.cache/glazy"

private var ENDPOINT_CACHE_PATH = "$CACHE_PATH/root_endpoints.json"

private val mapper: ObjectMapper = jacksonObjectMapper()
    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)

/**
 * Ensure the existence of the
 *
 * @param cachePath The path to cache storage.
 * @param isDir The cache path is to be a directory.
 */
private fun ensureCache(cachePath: String, isDir: Boolean = false) {
    // Determine if the path exists.
    if (!Files.exists(Paths.get(cachePath))) {
        val cacheFile = File(cachePath)

        // Create the cachePat.
        if (isDir) {
            cacheFile.mkdirs()
        } else {
            cacheFile.parentFile.mkdirs()
            File(cachePath).createNewFile()
        }
    }
}

/**
 * Set the location of the cache.
 *
 * @param path The path to the directory in which to store and read  cached data.
 */
fun setCacheLocation(path: String) {
    CACHE_PATH = path
    ENDPOINT_CACHE_PATH = "$CACHE_PATH/root_endpoints.json"
}

/**
 * Retrieve aa repository from the cache.
 *
 * @param user The owner of the repository.
 * @param name The name of the repository.
 * @return A repository with the given owner and name, or null if not found.
 */
fun repo(user: String, name: String): Repo? {
    val fileName = "$CACHE_PATH/$user/$name.json"
    if (!Files.exists(Paths.get(fileName))) return null
    val target = File(fileName)

    return mapper.readValue(target)
}

/**
 * Retrieve root endpoints from the cache.
 *
 * @return A root endpoints object if available, null otherwise.
 */
fun rootEndpoints(): RootEndpoints? {
    if (! Files.exists(Paths.get(ENDPOINT_CACHE_PATH))) return null

    val target = File(ENDPOINT_CACHE_PATH)

    return mapper.readValue(target)
}

/**
 * Store a repository to the cache.
 *
 * @param data The repository to write to the cache.
 */
fun write(data: Repo) {
    val repoCachePath = "$CACHE_PATH/${data.fullName}.json"

    // Ensure the owner directory as well as the repository file.
    ensureCache(repoCachePath)

    val repoAsJson = mapper.writeValueAsString(data)

    try {
        File(repoCachePath).writeText(repoAsJson)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Store a cache of root endpoints.
 *
 * @param data The root endpoint values to write to the cache.
 */
fun write(data: RootEndpoints) {
    ensureCache(ENDPOINT_CACHE_PATH)

    try {
        File(ENDPOINT_CACHE_PATH).writeText(mapper.writeValueAsString((data)))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Clear the cache of all data.
 */
fun clear() {
    val cacheDir = File(CACHE_PATH)

    cacheDir.deleteRecursively()
}

/**
 * Refresh the cache of a specific repository.
 *
 * @param user The repository owner username.
 * @param name The repository name.
 * @param service The service to utilize for cache refresh.
 */
fun refresh(user: String, name: String, service: RepoService) {
    val path = "$CACHE_PATH/$user/$name"

    // If no such cached repository, do nothing
    if (! Files.exists(Paths.get(path))) return

    // Replace cached repo data.
    File(path).delete()
    write(service.getRepo(user, name) ?: return)
}

/**
 * Refresh all cache data.
 */
fun refresh() {
    if (! Files.exists(Paths.get(CACHE_PATH))) return

    val service = RepoService(getToken())
    val cacheDir = File(CACHE_PATH)

    /* Iterate through all cached data ignore access tokens */
    for (file in cacheDir.listFiles() ?: arrayOf()) {

        /* If file is a directory, refresh cached repository data */
        if (file.isDirectory) {
            for (cachedRepo in file.listFiles() ?: arrayOf()) {
                refresh(file.name, cacheDir.name, service)
            }
        }

        /* Refresh endpoints data */
        if (file.name == ENDPOINT_CACHE_PATH) {
            write(
                rootEndpoints()
                    ?: continue)
        }

    }
}
