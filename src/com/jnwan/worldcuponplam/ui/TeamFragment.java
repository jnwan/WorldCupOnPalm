package com.jnwan.worldcuponplam.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jnwan.worldcuponplam.R;
import com.jnwan.worldcuponplam.model.TeamContent;



public class TeamFragment extends Fragment

{

    private ListAdapter adapter;

    private Context context;

    private ListView listView;

    private View rootView;

    private ArrayList<ArrayList<String>> teams;
    
    private DisplayMetrics dm ;

    
    @Override

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        context = getActivity();

        rootView = layoutInflater.inflate(R.layout.teamlayout, viewGroup, false);

        listView = (ListView)rootView.findViewById(R.id.team_list);

        initData();

        initView();

        return rootView;

    }
    private void initData() {

        teams = new ArrayList<ArrayList<String>>();
        
        dm = getResources().getDisplayMetrics();

        for (int i = 0; i < 8; ++i) {
            teams.add(new ArrayList<String>());
        }
        for(String string : TeamContent.NATIONS.keySet()){
        	int index = TeamContent.NATIONS.get(string).group.toCharArray()[0] - 'A';
        	teams.get(index).add(string);
        }

    }

    

    private void initView() {

        adapter = new MyAdapter(context);

        listView.setAdapter(adapter);

    }

    

    private class MyAdapter extends BaseAdapter

    {
    	 private Context context;//用于接收传递过来的Context对象
         public MyAdapter(Context context) {
             super();
             this.context = context;
         }
         
        public int getCount() {
            return 8;
        }

        

        public Object getItem(int n) {
            return null;
        }

        

        public long getItemId(int position) {

            return position;

        }

        

        @SuppressWarnings("deprecation")
		public View getView(int position, View view, ViewGroup viewGroup) {

            View myview = LayoutInflater.from(context).inflate(R.layout.teamlist, null);

            LinearLayout linearLayout = (LinearLayout)myview.findViewById(R.id.team_list_layout);
         

            TextView textView = (TextView)myview.findViewById(R.id.group_label);
            

            for (String s : teams.get(position)) {

                View teamview = LayoutInflater.from(context).inflate(R.layout.singleteam, null);

                ImageView teamlogo = (ImageView)teamview.findViewById(R.id.team_logo);

                TextView teamlabel = (TextView)teamview.findViewById(R.id.team_label);

                teamlogo.setImageResource(TeamContent.NATIONS.get(s).logo);

                teamlabel.setText(TeamContent.NATIONS.get(s).toString());

                LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                ll.width = (dm.widthPixels - 18)/4;

                ll.gravity = Gravity.CENTER;
                
                final String[] team = new String[1];
                
                team[0] = s;

                teamview.setOnClickListener(new View.OnClickListener() {                    

                    public void onClick(View view) {

                        Intent intent = new Intent(context, TeamDetailActivity.class);

                        intent.putExtra("team", team[0]);

                        startActivity(intent);

                    }

                });
                
                linearLayout.addView(teamview, ll);

            }

            textView.setText(String.valueOf((char)('A'+position)) + "\n" + "组");

            myview.setBackgroundColor(Color.rgb(219, 238, 244));

            return myview;

        }

        

        public boolean isEnabled(int n) {

            return false;

        }

    }

}
