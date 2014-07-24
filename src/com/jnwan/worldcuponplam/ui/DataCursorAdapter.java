package com.jnwan.worldcuponplam.ui;

import com.jnwan.worldcuponplam.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DataCursorAdapter extends SimpleCursorAdapter{
    private Context context;
	public DataCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.context = context;
	}

	@Override
	public View getView(int position,View converView,ViewGroup parent) {
		View view = null;
		TextView label = null;
		if(converView == null){
			view = LayoutInflater.from(context).inflate(R.layout.datapoint_item, parent,false);			
		}
		else{
			view = converView;
		}
		label = (TextView) view.findViewById(R.id.datapoint_label);
		if(position % 4 == 0){
			label.setVisibility(View.VISIBLE);
			char c = (char) (position/4 +'A');
			label.setText(String.valueOf(c)+"ื้");
		}
		else{
			label.setVisibility(View.GONE);
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
