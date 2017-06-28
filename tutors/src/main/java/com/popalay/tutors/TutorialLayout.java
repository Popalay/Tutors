package com.popalay.tutors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class TutorialLayout extends FrameLayout {

    private int textColor;
    private int shadowColor;
    private float textSize;
    private Drawable completeIcon;
    private int spacing;
    private float lineWidth;

    private boolean isLast;

    private int x;
    private int y;

    private TutorialListener tutorialListener;
    private TextView text;
    private Bitmap bitmap;
    private View lastTutorialView;
    private Paint paint;
    private Paint holePaint;

    public TutorialLayout(Context context, @Nullable TutorsBuilder builder) {
        super(context);
        init(context, builder);
    }

    public TutorialLayout(Context context) {
        super(context);
        init(context, null);
    }

    public TutorialLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
    }

    public TutorialLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, null);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public TutorialLayout(Context context,
            AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, null);
    }

    public void setTutorialListener(TutorialListener tutorialListener) {
        this.tutorialListener = tutorialListener;
    }

    public void showTutorial(View view, CharSequence text, boolean isLast) {
        this.isLast = isLast;

        if (this.bitmap != null) {
            this.bitmap.recycle();
        }
        if (this.lastTutorialView != null) {
            this.lastTutorialView.setDrawingCacheEnabled(false);
        }

        final int[] location = new int[2];
        this.lastTutorialView = view;
        view.getLocationInWindow(location);
        view.setDrawingCacheEnabled(true);

        this.text.setText(text);
        this.bitmap = view.getDrawingCache();
        this.x = location[0];
        this.y = location[1] - getStatusBarHeight();

        this.setVisibility(View.VISIBLE);
        moveText(!inTop());
        postInvalidate();
    }

    public void closeTutorial() {
        setVisibility(View.GONE);
        if (this.bitmap != null) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
        if (lastTutorialView != null) {
            this.lastTutorialView.setDrawingCacheEnabled(false);
            this.lastTutorialView = null;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        moveText(!inTop());
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recycleResources();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap == null) {
            return;
        }

        canvas.drawBitmap(this.bitmap, this.x, this.y, holePaint);
        final boolean inTop = inTop();

        final float lineX = this.x + this.bitmap.getWidth() / 2;
        final float lineY = this.y + (inTop ? this.bitmap.getHeight() + spacing : -spacing);
        final float lineYEnd = inTop ? this.text.getTop() - spacing : text.getBottom() + spacing;

        canvas.drawLine(lineX, lineY, lineX, lineYEnd, paint);
    }

    private boolean inTop() {
        if (this.bitmap == null) {
            return true;
        }
        int viewCenter = this.bitmap.getHeight() / 2 + this.y;
        int parentCenter = this.getHeight() / 2;
        return viewCenter < parentCenter || parentCenter == 0;
    }

    private void init(Context context, @Nullable TutorsBuilder builder) {
        setVisibility(View.GONE);

        if (isInEditMode()) {
            return;
        }

        setWillNotDraw(false);

        applyAttrs(context, builder);

        holePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        holePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(textColor);
        paint.setStrokeWidth(lineWidth);

        setPadding(spacing, spacing, spacing, spacing);
        setBackgroundColor(shadowColor);
        setClickable(true);
        setFocusable(true);

        initText(context);
        initCross(context);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TutorialLayout.this.tutorialListener != null) {
                    if (TutorialLayout.this.isLast) {
                        TutorialLayout.this.tutorialListener.onComplete();
                    } else {
                        TutorialLayout.this.tutorialListener.onNext();
                    }
                }
            }
        });
    }

    private void applyAttrs(Context context, @Nullable TutorsBuilder builder) {
        this.textColor = Color.WHITE;
        this.textSize = getResources().getDimension(R.dimen.textNormal);
        this.shadowColor = ContextCompat.getColor(context, R.color.shadow);
        this.completeIcon = ContextCompat.getDrawable(context, R.drawable.ic_cross_24_white);
        this.spacing = (int) getResources().getDimension(R.dimen.spacingNormal);
        this.lineWidth = getResources().getDimension(R.dimen.lineWidth);

        if (builder == null) {
            return;
        }

        this.textColor = builder.getTextColorRes() != 0 ? ContextCompat.getColor(context, builder.getTextColorRes())
                : this.textColor;

        this.textSize = builder.getTextSizeRes() != 0 ? getResources().getDimension(builder.getTextSizeRes())
                : this.textSize;

        this.shadowColor = builder.getShadowColorRes() != 0 ? ContextCompat
                .getColor(context, builder.getShadowColorRes()) : this.shadowColor;

        this.completeIcon = builder.getCompleteIconRes() != 0 ? ContextCompat
                .getDrawable(context, builder.getCompleteIconRes()) : this.completeIcon;

        this.spacing = builder.getSpacingRes() != 0 ? (int) getResources().getDimension(builder.getSpacingRes())
                : this.spacing;

        this.lineWidth = builder.getLineWidthRes() != 0 ? getResources().getDimension(builder.getLineWidthRes())
                : this.lineWidth;
    }

    private void initCross(Context context) {
        ImageView cross = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.TOP | Gravity.END;
        cross.setLayoutParams(layoutParams);
        cross.setImageDrawable(completeIcon);

        this.addView(cross);

        cross.setOnClickListener(new OnClickListener() {
            public final void onClick(View it) {
                if (TutorialLayout.this.tutorialListener != null) {
                    TutorialLayout.this.tutorialListener.onCompleteAll();
                }
            }
        });
    }

    private void initText(Context context) {
        this.text = new TextView(context);
        this.text.setTextColor(this.textColor);
        this.text.setGravity(Gravity.CENTER);
        this.text.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.textSize);

        this.addView(this.text);
    }

    private void moveText(boolean top) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER | (top ? Gravity.TOP : Gravity.BOTTOM);
        int margin = this.spacing * 3;
        if (top) {
            layoutParams.setMargins(0, margin, 0, 0);
        } else {
            layoutParams.setMargins(0, 0, 0, margin);
        }
        this.text.setLayoutParams(layoutParams);
    }

    private void recycleResources() {
        if (this.bitmap != null) {
            this.bitmap.recycle();
        }
        this.bitmap = null;
        if (this.lastTutorialView != null) {
            this.lastTutorialView.setDrawingCacheEnabled(false);
        }
        this.lastTutorialView = null;
        this.paint = null;
        this.holePaint = null;
    }

    private int getStatusBarHeight() {
        int height = 0;
        int resId = this.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            height = this.getContext().getResources().getDimensionPixelSize(resId);
        }
        return height;
    }
}
