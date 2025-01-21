package com.zhouguan.learnjetpack

class Dependency {
    val libraries = ArrayList<String>()

    fun implementation(library: String) {
        libraries.add(library)
    }
}

fun dependencies(block: Dependency.() -> Unit): List<String> {
    val dependency = Dependency()
    dependency.block()
    return dependency.libraries
}

fun main() {
    val libraries = dependencies {
        implementation("com.squareup.okhttp3:okhttp:3.12.1")
        implementation("com.squareup.retrofit2:retrofit:2.5.0")
    }

    for (lib in libraries) {
        println(lib)
    }
}