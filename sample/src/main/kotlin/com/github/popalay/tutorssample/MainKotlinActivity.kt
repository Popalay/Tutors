package com.github.popalay.tutorssample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.popalay.tutors.TutorialListener
import com.github.popalay.tutors.Tutors
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainKotlinActivity : AppCompatActivity() {

    private val tutorials: MutableMap<String, View> = LinkedHashMap()
    private lateinit var tutors: Tutors

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTutorials()
        initViews()
    }

    private fun initTutorials() {
        tutorials.put("It's a toolbar", toolbar)
        tutorials.put("It's a button", button_hello)
        tutorials.put("It's a borderless button", button_ok)
        tutorials.put("It's a text", text_description)
    }

    private fun initViews() {

        setSupportActionBar(toolbar)

        tutors = Tutors.create {
            textColorRes = android.R.color.white
            shadowColorRes = R.color.shadow
            textSizeRes = R.dimen.textNormal
            completeIconRes = R.drawable.ic_cross_24_white
            spacingRes = R.dimen.spacingNormal
            lineWidthRes = R.dimen.lineWidth
            cancelable = false
        }

        var iterator: MutableIterator<MutableMap.MutableEntry<String, View>>? = null

        button_show.setOnClickListener {
            iterator = tutorials.iterator()
            showTutorial(iterator)

        }

        tutors.setListener(object : TutorialListener {

            override fun onNext() {
                showTutorial(iterator)
            }

            override fun onComplete() {
                tutors.close()
            }

            override fun onCompleteAll() {
                tutors.close()
            }

        })
    }

    private fun showTutorial(iterator: MutableIterator<MutableMap.MutableEntry<String, View>>?) {
        iterator?.let {
            if (iterator.hasNext()) {
                val next = iterator.next()
                tutors.show(supportFragmentManager, next.value, next.key, !iterator.hasNext())
            }
        }
    }
}
