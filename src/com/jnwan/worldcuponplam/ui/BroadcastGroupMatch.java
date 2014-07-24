package com.jnwan.worldcuponplam.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jnwan.worldcuponplam.R;
import com.jnwan.worldcuponplam.model.BroadcastDetail;
import com.jnwan.worldcuponplam.model.GameTime;
import com.jnwan.worldcuponplam.model.TeamContent;
import com.jnwan.worldcuponplam.myview.XListView;

public class BroadcastGroupMatch extends Fragment implements XListView.IXListViewListener,View.OnClickListener{
    private View rootView;
    private LinearLayout layout;
    private TextView errorText;
    private Button errorButton;
    private ProgressBar progressbar;
    private ArrayList<BroadcastDetail> data;
    private ArrayList<BroadcastDetail> localData;
    private Map<Integer, Boolean> needLabel;
    private Map<String, Boolean> dateMap;
    private XListView mListView;
    private Context context;
    private MyAdapter adapter;
    private String lastTime = "";
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
			mListView = new XListView(context);
			mListView.setCacheColorHint(Color.rgb(0, 0, 0));
			LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
			layout.addView(mListView,ll);
			mListView.setXListViewListener(this);
			adapter = new MyAdapter();
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
	
	private class RequestTask extends AsyncTask<Void,Integer,String>{
        private String state;  
		public RequestTask(String string) {
			this.state = string;
		}
		@Override
		protected String doInBackground(Void... params) {
			return getData();
		}
		
		

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			if(mListView == null){
				progressbar.setVisibility(View.VISIBLE);
			}
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result == null){
				if(mListView == null){
					showError(1);
				}
				else{
					Toast.makeText(context, "无法连接服务器", Toast.LENGTH_SHORT).show();
					Onload();
				}
				progressbar.setVisibility(View.GONE);
			}
			else{
                 localData = new ArrayList<>();
                 needLabel = new HashMap<>();
                 dateMap = new HashMap<>();
                 Log.d("size", String.valueOf(data.size()));
                 for(int i = 0; i < data.size(); i++){
                	 BroadcastDetail b = data.get(i);
                	 String key = String.valueOf(b.gametime.month)+String.valueOf(b.gametime.day);
                	 if(dateMap.containsKey(key)){
                		 localData.add(b);
                	 }
                	 else{
                		 needLabel.put(i, true);
                		 dateMap.put(key, true);
                		 localData.add(b);
                	 }
                 }
				switch (state) {
				case "INIT":
					mListView = new XListView(context);
					mListView.setCacheColorHint(Color.rgb(0, 0, 0));
					LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
					layout.addView(mListView,ll);
					mListView.setXListViewListener(BroadcastGroupMatch.this);
					adapter = new MyAdapter();
					mListView.setAdapter(adapter);
					progressbar.setVisibility(View.GONE);
					hideError();
					Onload();
					lastTime = getcurTime();
					break;

				default:
					adapter.notifyDataSetChanged();
					Onload();
					lastTime = getcurTime();
					break;
				}
			}
			
		}

		private String getData() {
			String string = null;
            HttpGet httpGet = new HttpGet("http://jnwan.dyndns-server.com/broadcast.php");
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

                     String team1 = jsonObject.getString("broadcast_team1");

                     String team2 = jsonObject.getString("broadcast_team2");

                     String type = jsonObject.getString("broadcast_type");

                     String history = jsonObject.getString("broadcast_history");

                     int month = jsonObject.getInt("broadcast_month");

                     int date = jsonObject.getInt("broadcast_day");

                     int hour = jsonObject.getInt("broadcast_hour");

                     int min = jsonObject.getInt("broadcast_min");

                     int score1 = jsonObject.getInt("broadcast_score1");

                     int score2 = jsonObject.getInt("broadcast_score2");

                   if(type.equals("小组赛"))
                    data.add(new BroadcastDetail(team1, team2, type, history, new GameTime(month, date, hour, min), score1,score2));

                }
                return "Success";

            }

            catch (JSONException ex) {

                ex.printStackTrace();

                return null;

            }
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
	
	private void Onload(){
		mListView.stopRefresh();
		mListView.setRefreshTime(lastTime);
	}
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return localData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View myView = LayoutInflater.from(context).inflate(R.layout.broadcast_list_item, null);
			TextView datelabel = (TextView) myView.findViewById(R.id.date_label);
			ImageView image1 = (ImageView) myView.findViewById(R.id.broadcast_team1_logo);
			ImageView image2 = (ImageView) myView.findViewById(R.id.broadcast_team2_logo);
			TextView teamlabel1 = (TextView) myView.findViewById(R.id.broadcast_team1_label);
			TextView teamlabel2 = (TextView) myView.findViewById(R.id.broadcast_team2_label);
			TextView typeTextView = (TextView) myView.findViewById(R.id.broadcast_type_label);
			TextView timeTextView = (TextView) myView.findViewById(R.id.broadcast_time_label);
			TextView scoreTextView = (TextView) myView.findViewById(R.id.broadcast_score_label);
			BroadcastDetail b = localData.get(position);
			if(needLabel.containsKey(position)){
				datelabel.setVisibility(View.VISIBLE);
				datelabel.setText(String.valueOf(b.gametime.month)+"月"+String.valueOf(b.gametime.day)+"日");
			}
			else{
				datelabel.setVisibility(View.GONE);
			}
			String team1 = b.team1;
			String team2 = b.team2;
			GameTime time = b.gametime;
			int score1 = b.score1;
			int score2 = b.score2;
			image1.setImageResource(TeamContent.NATIONS.get(team1).logo);
			image2.setImageResource(TeamContent.NATIONS.get(team2).logo);
			teamlabel1.setText(TeamContent.NATIONS.get(team1).toString());
			teamlabel2.setText(TeamContent.NATIONS.get(team2).toString());
			typeTextView.setText(TeamContent.NATIONS.get(team1).group+"    组");
			if(score1 == -1){
			    timeTextView.setText("时间");
			    scoreTextView.setText(getTime(time));
			}
			else{
				timeTextView.setText("比分");
			    scoreTextView.setText(String.valueOf(score1)+"  :  "+String.valueOf(score2));
			}
			myView.setBackgroundColor(Color.rgb(219, 238, 244));
			return myView;
		}
	    private String getTime( GameTime gameTime) {
	         int hour = gameTime.hour;
	         int minute = gameTime.minute;
	        String s;
	        if (hour < 10) {
	            s =  "0" + String.valueOf(hour);
	        }
	        else {
	            s = String.valueOf(hour);
	        }
	        String s2;
	        if (minute < 10) {
            s2 =  "0" + String.valueOf(minute);
	        }
	        else {
	            s2 = String.valueOf(minute);
	        }
           return s + " : " + s2;
	    }
		
	}

}
