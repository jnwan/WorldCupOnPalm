package com.jnwan.worldcuponplam.ui;



import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.jnwan.worldcuponplam.R;


public class MainActivity extends FragmentActivity implements View.OnClickListener

{

    private ActionBar actionBar;

    private BroadcastFragment broadcastFragment;

    private ImageView broadcastImage;

    private FrameLayout content;

    private DataFragment dataFragment;

    private ImageView dataImage;


    private FragmentManager fragmentManager;       

    private NewsFragment newsFragment;

    private ImageView newsImage;

    private TeamFragment teamFragment;

    private ImageView teamImage;

    
    @Override

    protected void onCreate(final Bundle bundle) {

        super.onCreate(bundle);
        
        actionBar = getActionBar();

        actionBar.setBackgroundDrawable((Drawable)new ColorDrawable(Color.parseColor("#4caffd")));

        setContentView(R.layout.main_activity);

        initViews();

        content.removeAllViews();

        fragmentManager = getSupportFragmentManager();

        setTabSelection(0);

    }
    
    private void clearSelection() {

        teamImage.setImageResource(R.drawable.team_unselected);

        broadcastImage.setImageResource(R.drawable.broadcast_unselected);

        dataImage.setImageResource(R.drawable.data_unselected);

        newsImage.setImageResource(R.drawable.news_unselected);
       

    }

    

    private void hideFragments(FragmentTransaction fragmentTransaction) {

        if (teamFragment != null) {

            fragmentTransaction.hide(teamFragment);

        }
        if (broadcastFragment != null) {

            fragmentTransaction.hide(broadcastFragment);

        }

        if (dataFragment != null) {

            fragmentTransaction.hide(dataFragment);

        }

        if (newsFragment != null) {

            fragmentTransaction.hide(newsFragment);

        }

    }

    

    private void initViews() {

        content = (FrameLayout)findViewById(R.id.fragment_container);

        teamImage = (ImageView)findViewById(R.id.teamimage);

        broadcastImage = (ImageView)findViewById(R.id.broadcastimage);

        dataImage = (ImageView)findViewById(R.id.dataimage);

        newsImage = (ImageView)findViewById(R.id.newsimage);

        teamImage.setOnClickListener(this);

        broadcastImage.setOnClickListener(this);

        dataImage.setOnClickListener(this);

        newsImage.setOnClickListener(this);

    }

    

    private void setTabSelection(int n) {

        clearSelection();

        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();

        hideFragments(beginTransaction);

        switch (n) {
        case 0: {

            teamImage.setImageResource(R.drawable.team_selected);

            if (teamFragment == null) {

                beginTransaction.add(R.id.fragment_container, teamFragment = new TeamFragment());

                break;

            }

            beginTransaction.show(teamFragment);

            break;

        }

        case 1: {

            broadcastImage.setImageResource(R.drawable.broadcast_selected);

            if (broadcastFragment == null) {

                beginTransaction.add(R.id.fragment_container, broadcastFragment = new BroadcastFragment());

                break;

            }

            beginTransaction.show(broadcastFragment);

            break;

        }

        case 2: {

            dataImage.setImageResource(R.drawable.data_selected);

            if (dataFragment == null) {

                beginTransaction.add(R.id.fragment_container, dataFragment = new DataFragment());

                break;

            }

            beginTransaction.show(dataFragment);

            break;

        }

            default: {

                newsImage.setImageResource(R.drawable.news_selected);

                if (newsFragment == null) {
                    beginTransaction.add(R.id.fragment_container, newsFragment = new NewsFragment());
                    break;
                }

                beginTransaction.show(newsFragment);
                break;

            }

            

        }

        beginTransaction.commit();

    }

    

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.teamimage: {

                setTabSelection(0);
                break;
            }

            case R.id.broadcastimage: {

                setTabSelection(1);
                break;
            }

            case R.id.dataimage: {

                setTabSelection(2);
                break;
            }

            case R.id.newsimage: {

                setTabSelection(3);
                break;
            }

        }

    }

    
    @Override 
    public void onBackPressed() { 
    	 AlertDialog.Builder factory = new AlertDialog.Builder(this);     
         factory.setMessage("真的要退出吗？");  								    
         factory.setPositiveButton("返回", new DialogInterface.OnClickListener() {  
               public void onClick(DialogInterface dialog, int which) {  
                   dialog.dismiss();
               }  
         }); 
         factory.setNegativeButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				System.exit(0);
			}        	 
         });
         factory.create();
         factory.show();
    } 
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        menu.add(0,1,0,"关于");
        return true;
    }

    public boolean onOptionsItemSelected(final MenuItem menuItem) {
    	if(menuItem.getItemId() == 1){
             Intent intent = new Intent(this,AboutActivity.class);
             startActivity(intent);
        }
    	return true;

    }

}