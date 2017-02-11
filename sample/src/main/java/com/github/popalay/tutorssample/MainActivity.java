package com.github.popalay.tutorssample;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.popalay.tutors.TutorialListener;
import com.popalay.tutors.Tutors;
import com.popalay.tutors.TutorsBuilder;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TutorialListener {

    private Toolbar toolbar;
    private Button buttonHello;
    private TextView textDescription;
    private Button buttonShow;
    private Button buttonOk;
    private Map<String, View> tutorials;
    private Iterator<Map.Entry<String, View>> iterator;
    private Tutors tutors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initTutorials();
    }

    private void initTutorials() {
        tutorials = new LinkedHashMap<>();
        tutorials.put("It's a toolbar", toolbar);
        tutorials.put("It's a button", buttonHello);
        tutorials.put("It's a borderless button", buttonOk);
        tutorials.put("It's a text", textDescription);
    }

    private void initViews() {
        this.buttonOk = (Button) findViewById(R.id.button_ok);
        this.buttonShow = (Button) findViewById(R.id.button_show);
        this.textDescription = (TextView) findViewById(R.id.text_description);
        this.buttonHello = (Button) findViewById(R.id.button_hello);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        tutors = new TutorsBuilder()
                .textColorRes(android.R.color.white)
                .shadowColorRes(R.color.shadow)
                .textSizeRes(R.dimen.textNormal)
                .completeIconRes(R.drawable.ic_cross_24_white)
                .spacingRes(R.dimen.spacingNormal)
                .lineWidthRes(R.dimen.lineWidth)
                .cancelable(false)
                .build();

        buttonShow.setOnClickListener(this);

        tutors.setListener(this);

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                MainActivity.this.buttonHello.setAlpha((Float) animation.getAnimatedValue());
            }
        });

        animator.setDuration(500);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(-1);
        animator.start();
    }

    @Override
    public void onClick(View view) {
        iterator = tutorials.entrySet().iterator();
        showTutorial(iterator);
    }

    @Override
    public void onNext() {
        showTutorial(iterator);
    }

    @Override
    public void onComplete() {
        tutors.close();
    }

    @Override
    public void onCompleteAll() {
        tutors.close();
    }

    private void showTutorial(Iterator<Map.Entry<String, View>> iterator) {
        if (iterator == null) {
            return;
        }
        if (iterator.hasNext()) {
            Map.Entry<String, View> next = iterator.next();
            tutors.show(getSupportFragmentManager(), next.getValue(), next.getKey(), !iterator.hasNext());
        }
    }
}
