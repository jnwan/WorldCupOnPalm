package com.jnwan.worldcuponplam.ui;

import com.jnwan.worldcuponplam.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DataCursorAdapter2 extends SimpleCursorAdapter{
    private Context context;
	@SuppressWarnings("deprecation")
	public DataCursorAdapter2(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.context = context;
	}

	@Override
	public View getView(int position,View converView,ViewGroup parent) {
		View view = null;
		if(converView == null){
			view = LayoutInflater.from(context).inflate(R.layout.datashooter_item, parent,false);			
		}
		else{
			view = converView;
		}
		if(position % 2 == 1){
			view.setBackgroundColor(Color.rgb(219, 238, 244));
		}
		else{
			view.setBackgroundColor(Color.WHITE);
		}
		return super.getView(position,view,parent);
	}
	

}