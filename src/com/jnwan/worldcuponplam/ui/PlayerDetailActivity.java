package com.jnwan.worldcuponplam.ui;

import com.jnwan.worldcuponplam.R;
import com.jnwan.worldcuponplam.model.PlayerDetail;
import com.jnwan.worldcuponplam.model.TeamContent;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerDetailActivity extends Activity{
	private ActionBar actionBar;
    private TextView birth;
    private TextView brief;
    private TextView briefLabel;
    private TextView club;
    private TextView hweight;
    private ImageView imageView;
    private TextView infoLabel;
    private TextView name;
    private PlayerDetail player;
    private TextView position;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playerdetail);
		String team = getIntent().getExtras().getString("team");
		String pos = getIntent().getExtras().getString("position");		
		int index = getIntent().getExtras().getInt("index");
		player = TeamContent.NATIONS.get(team).players.get(pos).get(index);
		actionBar = getActionBar();
		birth = (TextView) findViewById(R.id.player_birth);
		brief = (TextView) findViewById(R.id.player_info);
		briefLabel = (TextView) findViewById(R.id.player_brief_label);
		club = (TextView) findViewById(R.id.player_club);
		hweight = (TextView) findViewById(R.id.player_wheight);
		imageView = (ImageView) findViewById(R.id.player_photo);
		infoLabel = (TextView) findViewById(R.id.player_info_label);
		name = (TextView) findViewById(R.id.player_name);
		position = (TextView) findViewById(R.id.player_position);
		initView();
	}


	private void initView() {
		imageView.setImageResource(player.photo);
		name.setText("    "+player.name);
		birth.setText("    "+player.birth);
		position.setText("    "+player.position);
		hweight.setText("    "+player.wheight);
		club.setText("    "+player.club);
		brief.setText("        "+player.briefInfo);
		briefLabel.setText("基本信息");
		infoLabel.setText("球员简介");
	    actionBar.setTitle(player.name);
	    actionBar.setDisplayUseLogoEnabled(false);
	    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4caffd")));
	}

}
