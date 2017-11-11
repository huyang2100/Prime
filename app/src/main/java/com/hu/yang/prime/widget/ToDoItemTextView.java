package com.hu.yang.prime.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.hu.yang.prime.R;

/**
 * Created by yanghu on 2017/8/18.
 */

public class ToDoItemTextView extends AppCompatTextView {
    private Paint marginPaint;
    private Paint linePaint;
    private int paperColor;
    private float margin;
    private int padding;

    public ToDoItemTextView(Context context) {
        super(context);
        init(context);
    }

    public ToDoItemTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ToDoItemTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Get a reference to our resource table.
        Resources myResources = getResources();
        // Create the paint brushes we will use in the onDraw method.
        marginPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        marginPaint.setColor(myResources.getColor(R.color.notepad_margin));
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(myResources.getColor(R.color.notepad_lines));

        // Get the paper background color and the margin width.
        paperColor = myResources.getColor(R.color.notepad_paper);
        margin = myResources.getDimension(R.dimen.notepad_margin);
        padding = (int) myResources.getDimension(R.dimen.notepad_padding);
        setPadding(padding,padding,padding,padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Color as paper
        canvas.drawColor(paperColor);

        // Draw ruled lines
//        canvas.drawLine(0, 0, 0, getMeasuredHeight(), linePaint);
//        canvas.drawLine(0, getMeasuredHeight(),
//                getMeasuredWidth(), getMeasuredHeight(),
//                linePaint);

        // Draw margin
        canvas.drawLine(margin, 0, margin, getMeasuredHeight(), marginPaint);

        // Move the text across from the margin
        canvas.save();
        canvas.translate(margin, 0);

        // Use the TextView to render the text
        super.onDraw(canvas);
        canvas.restore();
    }
}
