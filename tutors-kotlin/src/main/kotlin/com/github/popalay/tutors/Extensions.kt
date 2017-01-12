package com.github.popalay.tutors

inline fun <T> Int.ifNotZero(default: T, block: Int.(Int) -> T): T {
    return if (this != 0) block(this) else default
}

