package com.github.popalay.tutors

class Builder(var textColorRes: Int = 0,
              var shadowColorRes: Int = 0,
              var textSizeRes: Int = 0,
              var completeIconRes: Int = 0,
              var nextButtonTextRes: Int = 0,
              var completeButtonTextRes: Int = 0,
              var spacingRes: Int = 0,
              var lineWidthRes: Int = 0,
              var cancelable: Boolean = false) {

    constructor(init: Builder.() -> Unit) : this() {
        init()
    }

    fun textColorRes(init: Builder.() -> Int) = apply { textColorRes = init() }

    fun shadowColorRes(init: Builder.() -> Int) = apply { shadowColorRes = init() }

    fun textSizeRes(init: Builder.() -> Int) = apply { textSizeRes = init() }

    fun completeIconRes(init: Builder.() -> Int) = apply { completeIconRes = init() }

    fun nextButtonTextRes(init: Builder.() -> Int) = apply { nextButtonTextRes = init() }

    fun completeButtonTextRes(init: Builder.() -> Int) = apply { completeButtonTextRes = init() }

    fun spacingRes(init: Builder.() -> Int) = apply { spacingRes = init() }

    fun lineWidthRes(init: Builder.() -> Int) = apply { lineWidthRes = init() }

    fun cancelable(init: Builder.() -> Boolean) = apply { cancelable = init() }

    fun build() = Tutors.newInstance(this)

}
