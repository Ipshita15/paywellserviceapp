package com.cloudwell.paywell.services.customView;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 18/4/19.
 */
public class HTMLTextView extends androidx.appcompat.widget.AppCompatTextView {

    public HTMLTextView(Context context) {
        super(context);
        init();
    }

    public HTMLTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HTMLTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setText(Html.fromHtml(getText().toString()));
    }
}
