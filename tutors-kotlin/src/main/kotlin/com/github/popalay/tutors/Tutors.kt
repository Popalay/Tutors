package com.github.popalay.tutors

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window

class Tutors : DialogFragment() {

    private object ARGS {
        const val TEXT_COLOR = "TEXT_COLOR"
        const val TEXT_SIZE = "TEXT_SIZE"
        const val SHADOW_COLOR = "SHADOW_COLOR"
        const val COMPLETE_ICON = "COMPLETE_ICON"
        const val SPACING = "SPACING"
        const val LINE_WIDTH = "LINE_WIDTH"
        const val CANCELABLE = "CANCELABLE"
    }

    private var textColorRes: Int = 0
    private var shadowColorRes: Int = 0
    private var textSizeRes: Int = 0
    private var completeIconRes: Int = 0
    private var spacingRes: Int = 0
    private var lineWidthRes: Int = 0
    private var cancelableCustom: Boolean = false

    private var listener: TutorialListener? = null

    companion object {
        fun newInstance(builder: TutorsBuilder): Tutors {
            val args = Bundle()
            val fragment = Tutors()

            args.putInt(ARGS.TEXT_COLOR, builder.textColorRes)
            args.putInt(ARGS.SHADOW_COLOR, builder.shadowColorRes)
            args.putInt(ARGS.TEXT_SIZE, builder.textSizeRes)
            args.putInt(ARGS.COMPLETE_ICON, builder.completeIconRes)
            args.putInt(ARGS.SPACING, builder.spacingRes)
            args.putInt(ARGS.LINE_WIDTH, builder.lineWidthRes)
            args.putBoolean(ARGS.CANCELABLE, builder.cancelable)

            fragment.arguments = args
            return fragment
        }

        fun create(init: TutorsBuilder.() -> Unit) = TutorsBuilder(init).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs(arguments)
        retainInstance = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = createLayout()
        initViews(view)
        return view
    }

    override fun onStart() {
        super.onStart()
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setDimAmount(0f)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    fun setListener(listener: TutorialListener) {
        this.listener = listener
    }

    fun show(fragmentManager: FragmentManager, view: View, text: CharSequence, isLast: Boolean = false) {
        if (!isVisible) {
            show(fragmentManager, this.javaClass.name)
        }
        view.post { (this.view as TutorialLayout).showTutorial(view, text, isLast) }
    }

    fun close() {
        dismiss()
        (this.view as TutorialLayout).closeTutorial()
    }

    private fun getArgs(args: Bundle) {
        this.textColorRes = args.getInt(ARGS.TEXT_COLOR)
        this.shadowColorRes = args.getInt(ARGS.SHADOW_COLOR)
        this.textSizeRes = args.getInt(ARGS.TEXT_SIZE)
        this.completeIconRes = args.getInt(ARGS.COMPLETE_ICON)
        this.spacingRes = args.getInt(ARGS.SPACING)
        this.lineWidthRes = args.getInt(ARGS.LINE_WIDTH)
        this.cancelableCustom = args.getBoolean(ARGS.CANCELABLE)
    }

    private fun createLayout(): TutorialLayout {
        val builder = TutorsBuilder(
                this.textColorRes,
                this.shadowColorRes,
                this.textSizeRes,
                this.completeIconRes,
                this.spacingRes,
                this.lineWidthRes
        )
        return TutorialLayout(context, builder)
    }

    private fun initViews(view: TutorialLayout) {
        view.setTutorialListener(listener)
        isCancelable = cancelableCustom
    }

}
