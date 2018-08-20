//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.felink.corelib.webview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.widget.TextView;

public class WarningInfoTextView extends TextView {
    private String text;
    private int height;
    private int width;
    private String loadText = "";
    private int i = 0;
    private boolean isLoading = false;
    private boolean isRunning = false;
    private long lastTime = 0L;
    private Runnable runnable = new Runnable() {
        public void run() {
            long dis = System.currentTimeMillis() - WarningInfoTextView.this.lastTime;
            if(dis >= 500L) {
                WarningInfoTextView.this.lastTime = System.currentTimeMillis();
                WarningInfoTextView.this.invalidate();
            } else {
                WarningInfoTextView.this.postInvalidateDelayed(dis);
            }

            WarningInfoTextView.this.isRunning = false;
        }
    };

    public WarningInfoTextView(Context context) {
        super(context);
        this.init(context);
    }

    public WarningInfoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    private void init(Context context) {
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(this.isLoading) {
            if(this.text != null) {
                if(this.height > 0 && this.width > 0) {
                    this.comLoadText();
                    canvas.save();
                    float fontWidth = this.getFontWidth(this.getPaint(), this.text);
                    float fontHeight = this.getFontHeight(this.getPaint());
                    float y = ((float)this.height + fontHeight) / 2.0F;
                    float x = ((float)this.width - fontWidth) / 2.0F;
                    float dot_x = ((float)this.width - fontWidth) / 2.0F + fontWidth + 2.0F;
                    canvas.drawText(this.loadText, dot_x, y, this.getPaint());
                    canvas.restore();
                    if(!this.isRunning) {
                        this.isRunning = true;
                        this.postDelayed(this.runnable, 500L);
                    }

                } else {
                    this.width = (int)(this.getFontWidth(this.getPaint(), this.text) + this.getFontWidth(this.getPaint(), "...") * 2.0F);
                    this.setWidth(this.width);
                    this.height = this.getHeight();
                }
            }
        }
    }

    private void comLoadText() {
        if(this.i >= 4) {
            this.i = 0;
        }

        this.loadTextText();
        ++this.i;
    }

    private void loadTextText() {
        if(this.i == 0) {
            this.loadText = "";
        } else {
            this.loadText = this.loadText + ".";
        }

    }

    private float getFontHeight(Paint paint) {
        FontMetrics localFontMetrics = paint.getFontMetrics();
        return (float)Math.ceil((double)(localFontMetrics.descent - localFontMetrics.ascent));
    }

    private float getFontWidth(Paint paint, String text) {
        return paint.measureText(text);
    }

    public void startProcess(String text) {
        this.text = text;
        this.isLoading = true;
        this.setText(text);
    }
}
