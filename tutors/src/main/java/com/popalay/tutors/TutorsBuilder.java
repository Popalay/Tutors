package com.popalay.tutors;

public class TutorsBuilder {

    private int textColorRes;
    private int shadowColorRes;
    private int textSizeRes;
    private int completeIconRes;
    private int spacingRes;
    private int lineWidthRes;
    private boolean cancelable;

    public TutorsBuilder textColorRes(int textColorRes) {
        this.textColorRes = textColorRes;
        return this;
    }

    public TutorsBuilder shadowColorRes(int shadowColorRes) {
        this.shadowColorRes = shadowColorRes;
        return this;
    }

    public TutorsBuilder textSizeRes(int textSizeRes) {
        this.textSizeRes = textSizeRes;
        return this;
    }

    public TutorsBuilder completeIconRes(int completeIconRes) {
        this.completeIconRes = completeIconRes;
        return this;
    }

    public TutorsBuilder spacingRes(int spacingRes) {
        this.spacingRes = spacingRes;
        return this;
    }

    public TutorsBuilder lineWidthRes(int lineWidthRes) {
        this.lineWidthRes = lineWidthRes;
        return this;
    }

    public TutorsBuilder cancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public int getTextColorRes() {
        return textColorRes;
    }

    public int getShadowColorRes() {
        return shadowColorRes;
    }

    public int getTextSizeRes() {
        return textSizeRes;
    }

    public int getCompleteIconRes() {
        return completeIconRes;
    }

    public int getSpacingRes() {
        return spacingRes;
    }

    public int getLineWidthRes() {
        return lineWidthRes;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public Tutors build() {
        return Tutors.newInstance(this);
    }
}
