package com.jnwan.worldcuponplam.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jnwan.worldcuponplam.R;
import com.jnwan.worldcuponplam.model.DataHelper;
import com.jnwan.worldcuponplam.model.DataPointDetail;
import com.jnwan.worldcuponplam.model.DataShooterDetail;
import com.jnwan.worldcuponplam.myview.XListView;

public class DataShooterCursor extends Fragment implements XListView.IXListViewListener,View.OnClickListener{
	 private static ArrayList<DataShooterDetail> data;
	    private Context context;
	    private View rootView;
	    private LinearLayout layout;
	    private TextView errorText;
	    private Button errorButton;
	    private ProgressBar progressbar;
	    private XListView mListView;
	    private DataCursorAdapter2 adapter;
	    private String lastTime;
	    private ArrayList<DataPointDetail> localData;
	    private DataHelper helper;
	    private String DB_NAME = "SHOOTER";
	    private int DB_VERSION = 1;
	    private Cursor cursor;
	    private SQLiteDatabase db;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_item,container, false);
		context = getActivity();
	    layout = (LinearLayout) rootView.findViewById(R.id.fragment_layout);
	    errorText = (TextView) rootView.findViewById(R.id.fragment_errormessage);
	    errorButton = (Button) rootView.findViewById(R.id.fragment_errorbutton);
	    progressbar = (ProgressBar) rootView.findViewById(R.id.fragment_progressbar);
	    errorButton.setOnClickListener(this);
	    hideError();
	    progressbar.setVisibility(View.GONE);
	    init();
	    return rootView;
	}
	@SuppressWarnings("deprecation")
	private void init() {
		if(data == null){
			getDataFromServer();
			return;
		}
		else{
			helper = new DataHelper(context, DB_NAME, null, DB_VERSION);
			db = helper.getReadableDatabase();
			cursor = db.query("shooter_table", new String[] { "_id", "rank", "player", "national_team", "club_team", "goal" }, null, null, null, null, null);
			adapter = new DataCursorAdapter2(context,R.layout.datashooter_item,cursor,new String[]{"rank", "player", "national_team", "club_team", "goal"},new int[]{R.id.datashooter_rank,R.id.datashooter_player,R.id.datashooter_nation,R.id.datashooter_club,R.id.datashooter_goal});			
			mListView = new XListView(context);
			mListView.setCacheColorHint(Color.rgb(0, 0, 0));
			LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
			layout.addView(mListView,ll);
			mListView.setXListViewListener(this);

			mListView.addHeaderView(LayoutInflater.from(context).inflate(R.layout.datashooter_header, null));
			mListView.setAdapter(adapter);
		}
		
	}
	private void getDataFromServer() {
		if(isConnectingToInternet(context)){
			new RequestTask("INIT").execute();
		}
		else{
		   showError(0);
		}
	}
	public String getcurTime() {
		int hour = Calendar.getInstance().get(Calendar.HOUR);
		int min = Calendar.getInstance().get(Calendar.MINUTE);
		String s1 = "";
		String s2 = "";
		if(hour < 10){
			s1 = "0";
		}
		if(min < 10){
			s2 = "0";
		}
		return s1+hour+":"+s2+min;
	}
	
	private class RequestTask extends AsyncTask<Void,Integer,String>{
     private String state;  
		public RequestTask(String string) {
			this.state = string;
		}

		@Override
		protected String doInBackground(Void... params) {
			return getData();
		}
		private String getData() {
			String string = null;
         HttpGet httpGet = new HttpGet("http://jnwan.dyndns-server.com/shooter.php");
         DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
         defaultHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
         try {
              HttpResponse execute = defaultHttpClient.execute(httpGet);
              int statusCode = execute.getStatusLine().getStatusCode();
             string = null;
             if (statusCode == 200) {
                 string = EntityUtils.toString(execute.getEntity());
                 return MyJSON(string);
             }
             return null;
         }
         catch (ClientProtocolException ex) {
             ex.printStackTrace();
         }
         catch (IOException ex2) {
             ex2.printStackTrace();
         }
			return null;
		}
		

		private String MyJSON(String string) {
		try {
			JSONArray jsonArray = new JSONArray(string);

         data = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); ++i) {

             JSONObject jsonObject = jsonArray.getJSONObject(i);

             String name = jsonObject.getString("shooter_player");

             String nation = jsonObject.getString("shooter_nationalteam");
             
             String club = jsonObject.getString("shooter_clubteam");

             int goal = jsonObject.getInt("shooter_goal");
             
             data.add(new DataShooterDetail(name, nation, club, goal));

        }
        return "Success";

	    }

     catch (JSONException ex) {

         ex.printStackTrace();

         return null;
     }

    }

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(mListView == null){
				progressbar.setVisibility(View.VISIBLE);
			}
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result == null){
				if(mListView == null){
					showError(1);
				}
				else{
					Onload();
					Toast.makeText(context, "无法连接服务器", Toast.LENGTH_SHORT).show();
				}
				progressbar.setVisibility(View.GONE);
			}
			else{
				helper = new DataHelper(context, DB_NAME, null, DB_VERSION);
				db = helper.getWritableDatabase();
				db.execSQL("delete from shooter_table");
                for(int i = 0; i < data.size(); i++){
                	DataShooterDetail dd = data.get(i);
                	int rank = i + 1;
                	String name = dd.player;
                	String nation = dd.national_team;
                	String club = dd.club_team;
                	int goal = dd.goal;
                	ContentValues cv = new ContentValues();
                	cv.put("rank", rank);
                	cv.put("player", name);
                	cv.put("national_team", nation);
                	cv.put("club_team", club);
                	cv.put("goal", goal);
                	db.insert("shooter_table", null, cv);                	
                }
				switch (state) {
				case "INIT":
					mListView = new XListView(context);
					mListView.setCacheColorHint(Color.rgb(0, 0, 0));
					LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
					layout.addView(mListView,ll);
					mListView.setXListViewListener(DataShooterCursor.this);
					db = helper.getReadableDatabase();
					cursor = db.query("shooter_table", new String[] { "_id", "rank", "player", "national_team", "club_team", "goal" }, null, null, null, null, null);
					adapter = new DataCursorAdapter2(context,R.layout.datashooter_item,cursor,new String[]{"rank", "player", "national_team", "club_team", "goal"},new int[]{R.id.datashooter_rank,R.id.datashooter_player,R.id.datashooter_nation,R.id.datashooter_club,R.id.datashooter_goal});			
					mListView.setAdapter(adapter);
					mListView.addHeaderView(LayoutInflater.from(context).inflate(R.layout.datashooter_header, null));
					progressbar.setVisibility(View.GONE);
					hideError();
					Onload();
					lastTime = getcurTime();
					break;

				default:
					cursor.requery();
					adapter.notifyDataSetChanged();
					Onload();
					lastTime = getcurTime();
					break;
				}
			}
			
		}
	}
		
	@Override
	public void onClick(View v) {
		hideError();
		init();		
	}
	@Override
	public void onRefresh() {
		if (isConnectingToInternet(context)) {
         new RequestTask("REFRESH").execute();
         return;
     }
     Toast.makeText(this.context, "无法连接网络", 0).show();
     Onload();
		
	}
	private void hideError() {
		errorButton.setVisibility(View.GONE);
		errorText.setVisibility(View.GONE);	
	}
	private void showError(int situation){
		errorButton.setVisibility(View.VISIBLE);
		errorText.setVisibility(View.VISIBLE);
		if(situation == 0){
			errorText.setText("无法连接网络");
		}
		else{
			errorText.setText("无法连接服务器");
		}
	}
	public static boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return false;
		}
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		return (info != null) && info.isAvailable();
	}
	
	private void Onload(){
		mListView.stopRefresh();
		mListView.setRefreshTime(lastTime);
	}
	 public void onDestroy()
	  {
	    super.onDestroy();
	    if (helper != null) {
	      helper.close();
	    }
	    if (this.cursor != null) {
	      cursor.close();
	    }
	  }
}