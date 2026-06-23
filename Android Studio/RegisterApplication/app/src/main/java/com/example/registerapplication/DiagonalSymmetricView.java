package com.example.registerapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class DiagonalSymmetricView extends View {
    private Paint pinkPaint;
    private Paint bluePaint;
    private Path leftTrianglePath;
    private Path rightTrianglePath;

    public DiagonalSymmetricView(Context context) {
        super(context);
        init();
    }

    public DiagonalSymmetricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DiagonalSymmetricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        pinkPaint = new Paint();
        pinkPaint.setColor(Color.parseColor("#FFC0CB"));
        pinkPaint.setStyle(Paint.Style.FILL);

        bluePaint = new Paint();
        bluePaint.setColor(Color.parseColor("#A1F5E0"));
        bluePaint.setStyle(Paint.Style.FILL);

        leftTrianglePath = new Path();
        rightTrianglePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        leftTrianglePath.reset();
        leftTrianglePath.moveTo(0, 0);
        leftTrianglePath.lineTo(w, h);
        leftTrianglePath.lineTo(0, h);
        leftTrianglePath.close();

        rightTrianglePath.reset();
        rightTrianglePath.moveTo(w, 0);
        rightTrianglePath.lineTo(0, h);
        rightTrianglePath.lineTo(w, h);
        rightTrianglePath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(leftTrianglePath, pinkPaint);
        canvas.drawPath(rightTrianglePath, bluePaint);
    }
}