package com.example.prismfit.common.core.ext

fun String.replaceLastDotByDollar(): String? {
    val index = this.lastIndexOf('.')
    return if (index != -1) {
        String(this.toCharArray().apply { set(index, '$') })
    } else {
        null
    }
}
