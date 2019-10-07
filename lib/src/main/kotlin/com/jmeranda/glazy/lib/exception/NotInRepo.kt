package com.jmeranda.glazy.lib.exception

class NotInRepo(
        private var dir: String
) : Exception("Current working directory '$dir' is not a git repository.")