package com.github.popalay.tutorssample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.popalay.tutors.TutorialDialog
import com.github.popalay.tutors.TutorialListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val tutorials: MutableMap<String, View> = HashMap()
    private lateinit var dialog: TutorialDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTutorials()
        initViews()
    }

    private fun initTutorials() {
        tutorials.put("This's a toolbar", toolbar)
        tutorials.put("This's a button", button_hello)
        tutorials.put("This's a borderless button", button_ok)
        tutorials.put("This's a text", text_description)
    }

    private fun initViews() {

        setSupportActionBar(toolbar)

        dialog = TutorialDialog.create{}

        var iterator: MutableIterator<MutableMap.MutableEntry<String, View>>? = null

        button_show.setOnClickListener {
            iterator = tutorials.iterator()
            showTutorial(iterator)

        }

        dialog.setTutorialListener(object : TutorialListener {

            override fun onNext() {
                showTutorial(iterator)
            }

            override fun onComplete() {
                dialog.closeTutorial()
            }

            override fun onCompleteAll() {
                dialog.closeTutorial()
            }

        })
    }

    private fun showTutorial(iterator: MutableIterator<MutableMap.MutableEntry<String, View>>?) {
        iterator?.let {
            if (iterator.hasNext()) {
                val next = iterator.next()
                dialog.showTutorial(supportFragmentManager, next.value, next.key, !iterator.hasNext())
            }
        }
    }
}
