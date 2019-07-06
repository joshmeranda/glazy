package com.jmeranda.gitkot.cli

data class Boi(
        val name: String,
        val age: Int)

class EntryPoint {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Hello World!")
        }
    }
}