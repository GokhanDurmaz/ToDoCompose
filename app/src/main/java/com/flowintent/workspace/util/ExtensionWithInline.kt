package com.flowintent.workspace.util

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

// Extension ları, higher order fonksiyonlarla kullanabiliyoruz.

inline fun Int.pow(pow: Int = 2, scope: (Int) -> Unit) {
    scope.invoke(this.toDouble().pow(pow).toInt())
}

inline fun Int.sqrt(scope: (Int) -> Unit) {
    scope.invoke(sqrt(this.toDouble()).toInt())
}

inline infix fun Int.abs(scope: (Int) -> Unit) {
    scope.invoke(abs(this))
}

fun main() {
    5.pow { n -> println(n) }
    100.sqrt { n -> println(n) }

    2.pow(3) { n ->
        println(n)
    }

    -50 abs { n -> println(n) }

    object: foointerface.foosub1 {
        override fun foosub1fun() {
            TODO("Not yet implemented")
        }

        override fun foo5() {
            TODO("Not yet implemented")
        }
    }

    object: foointerface.foosub2 {
        override fun foo5() {
            TODO("Not yet implemented")
        }
    }
}


sealed interface foointerface {
    interface foosub1: foointerface {
        fun foosub1fun()
    }
    interface foosub2: foointerface
    interface foosub3: foointerface

    data class foo7(val str: String)

    fun foo5()

    fun foo66() {

    }

    companion object {
        const val num: Int = 2
    }
}

sealed class fooclass: foointerface {
    interface foosub1 {
        fun foosub1fun()
    }
    interface foosub2
    interface foosub3

    data class foo7(val str: String)

    fun foo55() {

    }

    companion object {
        const val num: Int = 2
    }
}
