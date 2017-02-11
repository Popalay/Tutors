package com.popalay.tutors;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class Tutors extends DialogFragment {

    private static final String ARG_TEXT_COLOR = "TEXT_COLOR";
    private static final String ARG_TEXT_SIZE = "TEXT_SIZE";
    private static final String ARG_SHADOW_COLOR = "SHADOW_COLOR";
    private static final String ARG_COMPLETE_ICON = "COMPLETE_ICON";
    private static final String ARG_SPACING = "SPACING";
    private static final String ARG_LINE_WIDTH = "LINE_WIDTH";
    private static final String ARG_CANCELABLE = "CANCELABLE";

    private int textColorRes;
    private int shadowColorRes;
    private int textSizeRes;
    private int completeIconRes;
    private int spacingRes;
    private int lineWidthRes;
    private boolean cancelableCustom;

    private TutorialListener listener;

    static Tutors newInstance(TutorsBuilder builder) {
        final Bundle args = new Bundle();
        final Tutors fragment = new Tutors();

        args.putInt(ARG_TEXT_COLOR, builder.getTextColorRes());
        args.putInt(ARG_SHADOW_COLOR, builder.getShadowColorRes());
        args.putInt(ARG_TEXT_SIZE, builder.getTextSizeRes());
        args.putInt(ARG_COMPLETE_ICON, builder.getCompleteIconRes());
        args.putInt(ARG_SPACING, builder.getSpacingRes());
        args.putInt(ARG_LINE_WIDTH, builder.getLineWidthRes());
        args.putBoolean(ARG_CANCELABLE, builder.isCancelable());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgs(getArguments());
        setRetainInstance(true);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = createLayout();
        initViews(((TutorialLayout) view));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        final Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setDimAmount(0f);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    public void setListener(TutorialListener listener) {
        this.listener = listener;
    }

    public void show(FragmentManager fragmentManager, final View view, final CharSequence text, final boolean isLast) {
        if (!isVisible()) {
            show(fragmentManager, this.getClass().getName());
        }
        view.post(new Runnable() {
            @Override
            public void run() {
                final TutorialLayout layout = (TutorialLayout) Tutors.this.getView();
                if (layout == null) {
                    return;
                }
                layout.showTutorial(view, text, isLast);
            }
        });
    }

    public void close() {
        dismiss();
        final TutorialLayout layout = (TutorialLayout) Tutors.this.getView();
        if (layout == null) {
            return;
        }
        layout.closeTutorial();
    }

    private void getArgs(Bundle args) {
        this.textColorRes = args.getInt(ARG_TEXT_COLOR);
        this.shadowColorRes = args.getInt(ARG_SHADOW_COLOR);
        this.textSizeRes = args.getInt(ARG_TEXT_SIZE);
        this.completeIconRes = args.getInt(ARG_COMPLETE_ICON);
        this.spacingRes = args.getInt(ARG_SPACING);
        this.lineWidthRes = args.getInt(ARG_LINE_WIDTH);
        this.cancelableCustom = args.getBoolean(ARG_CANCELABLE);
    }

    private TutorialLayout createLayout() {
        final TutorsBuilder builder = new TutorsBuilder()
                .textColorRes(textColorRes)
                .shadowColorRes(shadowColorRes)
                .textSizeRes(textSizeRes)
                .completeIconRes(completeIconRes)
                .spacingRes(spacingRes)
                .lineWidthRes(lineWidthRes);
        return new TutorialLayout(getContext(), builder);
    }

    private void initViews(TutorialLayout view) {
        view.setTutorialListener(listener);
        setCancelable(cancelableCustom);
    }
}
