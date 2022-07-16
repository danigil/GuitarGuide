package com.melons.melon.guitarguide;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomButton extends LinearLayout {

    private Button button;

    private TextView TV;

    boolean isChecked;

    public CustomButton(Context context, AttributeSet attrs){
        super(context,attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Options, 0, 0);

        String Text = a.getString(R.styleable.Options_text);

        @SuppressWarnings("ResourceAsColor")
        int valueColor = a.getColor(R.styleable.Options_textBGcolor,
                R.color.colorAccent);

        String direction = a.getString(R.styleable.Options_direction);
        isChecked = a.getBoolean(R.styleable.Options_isChecked,false);

        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        inflate(context,direction,Text);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked) {
                    button.setBackgroundResource(R.drawable.ic_radio_button_unchecked);
                    TV.setVisibility(INVISIBLE);
                    isChecked = !isChecked;
                }
                else {
                    button.setBackgroundResource(R.drawable.ic_radio_button_checked);
                    TV.setVisibility(VISIBLE);
                    isChecked = !isChecked;
                }

            }
        });

    }

    public CustomButton(Context context) {
        this(context,null);
    }

    private void inflate(Context context, String direction,String text){
        if(direction.equals("left")) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.view_button_options_left, this, true);

            TV = (TextView) getChildAt(0);
            TV.setText(text);
            button = (Button) getChildAt(2);

            if(isChecked)
            {
                TV.setVisibility(VISIBLE);
                button.setBackgroundResource(R.drawable.ic_radio_button_checked);
            }

        } else if(direction.equals("right")){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.view_button_options_right, this, true);

            button = (Button) getChildAt(0);
            TV = (TextView) getChildAt(2);
            TV.setText(text);

            if(isChecked)
            {
                TV.setVisibility(VISIBLE);
                button.setBackgroundResource(R.drawable.ic_radio_button_checked);
            }
        } else
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.view_button_options_right, this, true);

            button = (Button) getChildAt(0);
            TV = (TextView) getChildAt(2);
            TV.setText(text);

            if(isChecked)
            {
                TV.setVisibility(VISIBLE);
                button.setBackgroundResource(R.drawable.ic_radio_button_checked);
            }
        }
    }
}
