package com.jmeranda.gitkot.client

abstract class Request(endpointPath: String) {
    protected val BASE_URL: String = "https://api.github.com/"

    protected val endpdointPath: String = endpointPath

}