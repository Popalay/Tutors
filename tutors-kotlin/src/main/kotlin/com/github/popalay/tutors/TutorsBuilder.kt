package com.github.popalay.tutors

class TutorsBuilder(var textColorRes: Int = 0,
                    var shadowColorRes: Int = 0,
                    var textSizeRes: Int = 0,
                    var completeIconRes: Int = 0,
                    var spacingRes: Int = 0,
                    var lineWidthRes: Int = 0,
                    var cancelable: Boolean = false) {

    constructor(init: TutorsBuilder.() -> Unit) : this() {
        init()
    }

    fun textColorRes(init: TutorsBuilder.() -> Int) = apply { textColorRes = init() }

    fun shadowColorRes(init: TutorsBuilder.() -> Int) = apply { shadowColorRes = init() }

    fun textSizeRes(init: TutorsBuilder.() -> Int) = apply { textSizeRes = init() }

    fun completeIconRes(init: TutorsBuilder.() -> Int) = apply { completeIconRes = init() }

    fun spacingRes(init: TutorsBuilder.() -> Int) = apply { spacingRes = init() }

    fun lineWidthRes(init: TutorsBuilder.() -> Int) = apply { lineWidthRes = init() }

    fun cancelable(init: TutorsBuilder.() -> Boolean) = apply { cancelable = init() }

    fun build() = Tutors.newInstance(this)

}
