package com.github.popalay.tutors

interface TutorialListener {

    fun onNext() {}

    fun onComplete() {}

    fun onCompleteAll() {}
}