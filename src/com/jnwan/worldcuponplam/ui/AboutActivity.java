package com.jnwan.worldcuponplam.ui;

import com.jnwan.worldcuponplam.R;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable((Drawable)new ColorDrawable(Color.parseColor("#4caffd")));
		TextView textView = (TextView) findViewById(R.id.about);
		textView.setText("◊˜’ﬂ£∫Jnwan\n” œ‰£∫ooqqoo2000@gmail.com");
	}
      
}
