/**
 * Store some API responses to limit the amount of requests made.
 */
package com.jmeranda.glazy.lib.handler

import java.lang.System
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

import com.beust.klaxon.Klaxon

import com.jmeranda.glazy.lib.Repo
import com.jmeranda.glazy.lib.RootEndpoints

val CACHE_DIR = "${System.getProperty("user.home")}/.cache/glazy"

/**
 * Reads and Writes cached API data in json format.
 *
 * All cached data is stored in .cache/lazy in the users HOME directory.
 */
class ResponseCache {
    init {
        if (! Files.exists(Paths.get(CACHE_DIR))) {
            File(CACHE_DIR).mkdir()
        }
    }

    /**
     * Write repository data to .cache/glazy in HOME directory.
     *
     * @param data Repo to be cached.
     */
    fun write(data: Repo) {

        if (! Files.exists(Paths.get("$CACHE_DIR/${data.owner.login}"))) {
            File("$CACHE_DIR/${data.owner.login}").mkdir()
        }

        val fileName = "$CACHE_DIR/${data.fullName}.json"
        val dest = File(fileName)
        val repoAsJson: String = Klaxon().toJsonString(data)

        try {
            dest.createNewFile()
            dest.writeText(repoAsJson)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Write root endpoints dat to .cache/glazy in HOME directory.
     *
     * @param data Root endpoints to be cached.
     */
    fun write(data: RootEndpoints) {
        val fileName = "$CACHE_DIR/root_endpoints.json"
        val dest = File(fileName)
        val endpointsAsJson: String = Klaxon().toJsonString(data)

        try {
            dest.createNewFile()
            dest.writeText(endpointsAsJson)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *  Read cached repository data.
     *
     *  @param name The name of the repo to be cached.
     *  @param user The login name of the repositories owner to be read.
     *
     *  @return Repo if cached data exists, null otherwise.
     */
    fun repo(name: String, user: String): Repo? {
        val fileName = "$CACHE_DIR/$user/$name.json"
        if (!Files.exists(Paths.get(fileName))) { return null }
        val target = File(fileName)

        return Klaxon().parse<Repo>(target)

    }

    /**
     * Read cached root endpoints data.
     *
     * @return RootEndpoints if cached data exists, null otherwise
     */
    fun endpoints(): RootEndpoints? {
        val fileName = "$CACHE_DIR/root_endpoints.json"
        if (! Files.exists(Paths.get(fileName))) { return null }
        val target = File(fileName)

        return Klaxon().parse<RootEndpoints>(target)
    }
}