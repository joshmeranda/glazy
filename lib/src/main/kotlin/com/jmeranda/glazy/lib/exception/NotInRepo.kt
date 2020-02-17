package com.jmeranda.glazy.lib.exception

class NotInRepo(
        dir: String
) : Exception("Current working directory '$dir' is not a git repository.")