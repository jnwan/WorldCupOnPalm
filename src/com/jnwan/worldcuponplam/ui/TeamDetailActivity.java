package com.jnwan.worldcuponplam.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import com.jnwan.worldcuponplam.R;
import com.jnwan.worldcuponplam.model.PlayerDetail;
import com.jnwan.worldcuponplam.model.TeamContent;
import com.jnwan.worldcuponplam.model.TeamInfo;

public class TeamDetailActivity extends Activity{
  private ActionBar actionBar;
  private Context context;
  private ArrayList<PlayerDetail> forward;
  private ArrayList<PlayerDetail> guard;
  private ArrayList<PlayerDetail> keeper;
  private ListView listView;
  private ArrayList<PlayerDetail> midfield;
  private String team;
  private TeamInfo teamInfo;
  private DisplayMetrics dm ;
  
  private void initData() {
    keeper = teamInfo.players.get("�Ž�");
    guard = teamInfo.players.get("����");
    midfield = teamInfo.players.get("�г�");
    forward = teamInfo.players.get("ǰ��");
    Log.d("size", String.valueOf(teamInfo.players.get("�Ž�")));
    Log.d("size", String.valueOf(keeper.size()));
  }
  
  protected void onCreate(Bundle bundle){
       super.onCreate(bundle);
       setContentView(R.layout.teamdetail);
       dm = getResources().getDisplayMetrics();
       context = this;
       Toast.makeText(context, "������������Ѷ����", 0).show();
       team = getIntent().getExtras().getString("team");
       teamInfo = TeamContent.NATIONS.get(team);
       initData();
       listView = (ListView)findViewById(R.id.team_detail_list);
       listView.setAdapter(new MyAdapter());
       actionBar = getActionBar();
       actionBar.setTitle(teamInfo.toString());
       actionBar.setDisplayUseLogoEnabled(false);
       actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4caffd")));
  }
  private class MyAdapter extends BaseAdapter{

	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		switch (position) {
		case 0: {
			TextView teamlabel = new TextView(context);
			teamlabel.setText("��Ӽ��");
			teamlabel.setTextSize(18);
			teamlabel.setTextColor(Color.rgb(45, 139, 0));
			return teamlabel;
		}
		case 1: {
			TextView teambrief = new TextView(context);
			teambrief.setText("        "+teamInfo.briefIntr);
			return teambrief;
		}
		case 2: {
			TextView playerlabel = new TextView(context);
			playerlabel.setText("��Ա����");
			playerlabel.setTextSize(18);
			playerlabel.setTextColor(Color.rgb(45, 139, 0));
			return playerlabel;
		}
		case 3: {
			View rootView = LayoutInflater.from(context).inflate(R.layout.team_detail_list, null);
			TextView positionLabel = (TextView) rootView.findViewById(R.id.position_label);
			positionLabel.setVisibility(View.VISIBLE);
			positionLabel.setText("�Ž�");
			positionLabel.setTextSize(15);
			LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.player_list);
			for(int i = 0; i < keeper.size(); i++){
				PlayerDetail p = keeper.get(i);
				View playerView = LayoutInflater.from(context).inflate(R.layout.singleplayer, null);
				ImageView playerImage = (ImageView) playerView.findViewById(R.id.player_logo);
				TextView playerLabel = (TextView) playerView.findViewById(R.id.player_label);
				playerImage.setImageResource(p.photo);
				playerLabel.setText(p.toString());
				final int[] index = new int[1];
				index[0] = i;
				playerView.setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, PlayerDetailActivity.class);
		                intent.putExtra("position", "�Ž�");
		                intent.putExtra("team", team);
		                intent.putExtra("index", index[0]);
		                startActivity(intent);			
					}
				});
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.width = dm.widthPixels/5;
				ll.gravity = Gravity.CENTER;
				layout.addView(playerView,ll);
			}
			return rootView;
		}
		case 4: {
			View rootView = LayoutInflater.from(context).inflate(R.layout.team_detail_list, null);
			TextView positionLabel = (TextView) rootView.findViewById(R.id.position_label);
			positionLabel.setVisibility(View.VISIBLE);
			positionLabel.setText("����");
			positionLabel.setTextSize(15);
			LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.player_list);
			if(guard.size() <= 5){
			for(int i = 0; i < guard.size(); i++){
				PlayerDetail p = guard.get(i);
				View playerView = LayoutInflater.from(context).inflate(R.layout.singleplayer, null);
				ImageView playerImage = (ImageView) playerView.findViewById(R.id.player_logo);
				TextView playerLabel = (TextView) playerView.findViewById(R.id.player_label);
				playerImage.setImageResource(p.photo);
				playerLabel.setText(p.toString());
				final int[] index = new int[1];
				index[0] = i;
				playerView.setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, PlayerDetailActivity.class);
		                intent.putExtra("position", "����");
		                intent.putExtra("team", team);
		                intent.putExtra("index", index[0]);
		                startActivity(intent);			
					}
				});
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.width = dm.widthPixels/5;
				ll.gravity = Gravity.CENTER;
				layout.addView(playerView,ll);
			}
			return rootView;
			}
			else{
				for(int i = 0; i < 5; i++){
					PlayerDetail p = guard.get(i);
					View playerView = LayoutInflater.from(context).inflate(R.layout.singleplayer, null);
					ImageView playerImage = (ImageView) playerView.findViewById(R.id.player_logo);
					TextView playerLabel = (TextView) playerView.findViewById(R.id.player_label);
					playerImage.setImageResource(p.photo);
					playerLabel.setText(p.toString());
					final int[] index = new int[1];
					index[0] = i;
					playerView.setOnClickListener(new OnClickListener() {			
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context, PlayerDetailActivity.class);
			                intent.putExtra("position", "����");
			                intent.putExtra("team", team);
			                intent.putExtra("index", index[0]);
			                startActivity(intent);			
						}
					});
					LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
					ll.width = dm.widthPixels/5;
					ll.gravity = Gravity.CENTER;
					layout.addView(playerView,ll);
				}
				return rootView;
			}
		}
		case 5: {
			View rootView = LayoutInflater.from(context).inflate(R.layout.team_detail_list, null);
			TextView positionLabel = (TextView) rootView.findViewById(R.id.position_label);
			positionLabel.setVisibility(View.GONE);
			LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.player_list);
			for(int i = 5; i < guard.size(); i++){
				PlayerDetail p = guard.get(i);
				View playerView = LayoutInflater.from(context).inflate(R.layout.singleplayer, null);
				ImageView playerImage = (ImageView) playerView.findViewById(R.id.player_logo);
				TextView playerLabel = (TextView) playerView.findViewById(R.id.player_label);
				playerImage.setImageResource(p.photo);
				playerLabel.setText(p.toString());
				final int[] index = new int[1];
				index[0] = i;
				playerView.setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, PlayerDetailActivity.class);
		                intent.putExtra("position", "����");
		                intent.putExtra("team", team);
		                intent.putExtra("index", index[0]);
		                startActivity(intent);			
					}
				});
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.width = dm.widthPixels/5;
				ll.gravity = Gravity.CENTER;
				layout.addView(playerView,ll);
			}
			return rootView;
		}
		case 6: {
			View rootView = LayoutInflater.from(context).inflate(R.layout.team_detail_list, null);
			TextView positionLabel = (TextView) rootView.findViewById(R.id.position_label);
			positionLabel.setVisibility(View.VISIBLE);
			positionLabel.setText("�г�");
			positionLabel.setTextSize(16);
			LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.player_list);
			if(midfield.size() <= 5){
			for(int i = 0; i < midfield.size(); i++){
				PlayerDetail p = midfield.get(i);
				View playerView = LayoutInflater.from(context).inflate(R.layout.singleplayer, null);
				ImageView playerImage = (ImageView) playerView.findViewById(R.id.player_logo);
				TextView playerLabel = (TextView) playerView.findViewById(R.id.player_label);
				playerImage.setImageResource(p.photo);
				playerLabel.setText(p.toString());
				final int[] index = new int[1];
				index[0] = i;
				playerView.setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, PlayerDetailActivity.class);
		                intent.putExtra("position", "�г�");
		                intent.putExtra("team", team);
		                intent.putExtra("index", index[0]);
		                startActivity(intent);			
					}
				});
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.width = dm.widthPixels/5;
				ll.gravity = Gravity.CENTER;
				layout.addView(playerView,ll);
			}
			return rootView;
			}
			else{
				for(int i = 0; i < 5; i++){
					PlayerDetail p = midfield.get(i);
					View playerView = LayoutInflater.from(context).inflate(R.layout.singleplayer, null);
					ImageView playerImage = (ImageView) playerView.findViewById(R.id.player_logo);
					TextView playerLabel = (TextView) playerView.findViewById(R.id.player_label);
					playerImage.setImageResource(p.photo);
					playerLabel.setText(p.toString());
					final int[] index = new int[1];
					index[0] = i;
					playerView.setOnClickListener(new OnClickListener() {			
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context, PlayerDetailActivity.class);
			                intent.putExtra("position", "�г�");
			                intent.putExtra("team", team);
			                intent.putExtra("index", index[0]);
			                startActivity(intent);			
						}
					});
					LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
					ll.width = dm.widthPixels/5;
					ll.gravity = Gravity.CENTER;
					layout.addView(playerView,ll);
				}
				return rootView;
			}
			
		}
		case 7: {
			View rootView = LayoutInflater.from(context).inflate(R.layout.team_detail_list, null);
			TextView positionLabel = (TextView) rootView.findViewById(R.id.position_label);
			positionLabel.setVisibility(View.GONE);
			LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.player_list);
			for(int i = 5; i < midfield.size(); i++){
				PlayerDetail p = midfield.get(i);
				View playerView = LayoutInflater.from(context).inflate(R.layout.singleplayer, null);
				ImageView playerImage = (ImageView) playerView.findViewById(R.id.player_logo);
				TextView playerLabel = (TextView) playerView.findViewById(R.id.player_label);
				playerImage.setImageResource(p.photo);
				playerLabel.setText(p.toString());
				final int[] index = new int[1];
				index[0] = i;
				playerView.setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, PlayerDetailActivity.class);
		                intent.putExtra("position", "�г�");
		                intent.putExtra("team", team);
		                intent.putExtra("index", index[0]);
		                startActivity(intent);			
					}
				});
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.width = dm.widthPixels/5;
				ll.gravity = Gravity.CENTER;
				layout.addView(playerView,ll);
			}
			return rootView;
		}
		case 8: {
			View rootView = LayoutInflater.from(context).inflate(R.layout.team_detail_list, null);
			TextView positionLabel = (TextView) rootView.findViewById(R.id.position_label);
			positionLabel.setVisibility(View.VISIBLE);
			positionLabel.setText("ǰ��");
			positionLabel.setTextSize(15);
			LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.player_list);
			if(forward.size() <= 5){
			for(int i = 0; i < forward.size(); i++){
				PlayerDetail p = forward.get(i);
				View playerView = LayoutInflater.from(context).inflate(R.layout.singleplayer, null);
				ImageView playerImage = (ImageView) playerView.findViewById(R.id.player_logo);
				TextView playerLabel = (TextView) playerView.findViewById(R.id.player_label);
				playerImage.setImageResource(p.photo);
				playerLabel.setText(p.toString());
				final int[] index = new int[1];
				index[0] = i;
				playerView.setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, PlayerDetailActivity.class);
		                intent.putExtra("position", "ǰ��");
		                intent.putExtra("team", team);
		                intent.putExtra("index", index[0]);
		                startActivity(intent);			
					}
				});
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.width = dm.widthPixels/5;
				ll.gravity = Gravity.CENTER;
				layout.addView(playerView,ll);
			}
			return rootView;
			}
			else{
				for(int i = 0; i < 5; i++){
					PlayerDetail p = forward.get(i);
					View playerView = LayoutInflater.from(context).inflate(R.layout.singleplayer, null);
					ImageView playerImage = (ImageView) playerView.findViewById(R.id.player_logo);
					TextView playerLabel = (TextView) playerView.findViewById(R.id.player_label);
					playerImage.setImageResource(p.photo);
					playerLabel.setText(p.toString());
					final int[] index = new int[1];
					index[0] = i;
					playerView.setOnClickListener(new OnClickListener() {			
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context, PlayerDetailActivity.class);
			                intent.putExtra("position", "ǰ��");
			                intent.putExtra("team", team);
			                intent.putExtra("index", index[0]);
			                startActivity(intent);			
						}
					});
					LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
					ll.width = dm.widthPixels/5;
					ll.gravity = Gravity.CENTER;
					layout.addView(playerView,ll);
				}
				return rootView;
			}
			
		}
		case 9: {
			View rootView = LayoutInflater.from(context).inflate(R.layout.team_detail_list, null);
			TextView positionLabel = (TextView) rootView.findViewById(R.id.position_label);
			positionLabel.setVisibility(View.GONE);
			LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.player_list);
			for(int i = 5; i < forward.size(); i++){
				PlayerDetail p = forward.get(i);
				View playerView = LayoutInflater.from(context).inflate(R.layout.singleplayer, null);
				ImageView playerImage = (ImageView) playerView.findViewById(R.id.player_logo);
				TextView playerLabel = (TextView) playerView.findViewById(R.id.player_label);
				playerImage.setImageResource(p.photo);
				playerLabel.setText(p.toString());
				final int[] index = new int[1];
				index[0] = i;
				playerView.setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, PlayerDetailActivity.class);
		                intent.putExtra("position", "ǰ��");
		                intent.putExtra("team", team);
		                intent.putExtra("index", index[0]);
		                startActivity(intent);			
					}
				});
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.width = dm.widthPixels/5;
				ll.gravity = Gravity.CENTER;
				layout.addView(playerView,ll);
			}
			return rootView;
		}
		default:
			return null;
		}
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}
	  
  }
}
