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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jnwan.worldcuponplam.R;
import com.jnwan.worldcuponplam.model.DataPointDetail;
import com.jnwan.worldcuponplam.model.TeamContent;
import com.jnwan.worldcuponplam.myview.XListView;

public class DataPoint extends Fragment implements XListView.IXListViewListener,View.OnClickListener{
	    private static ArrayList<DataPointDetail> data;
	    private Context context;
	    private View rootView;
	    private LinearLayout layout;
	    private TextView errorText;
	    private Button errorButton;
	    private ProgressBar progressbar;
	    private XListView mListView;
	    private MyAdapter adapter;
	    private String lastTime;
	    private ArrayList<DataPointDetail> localData;
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
			mListView.addHeaderView(LayoutInflater.from(context).inflate(R.layout.datapoint_header, null));
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
            HttpGet httpGet = new HttpGet("http://jnwan.dyndns-server.com/teampoints.php");
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

                String name = jsonObject.getString("team_name");

                String group = jsonObject.getString("team_group");

                int point = jsonObject.getInt("team_points");

                int session = jsonObject.getInt("team_sessions");

                int win = jsonObject.getInt("team_wins");
                
                int ties = jsonObject.getInt("team_ties");

                int loses = jsonObject.getInt("team_loses");

                int GD = jsonObject.getInt("team_GD");
                
               data.add(new DataPointDetail(name, group, point, session, win, ties, loses, GD));

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
				localData = new ArrayList<>(data);
				switch (state) {
				case "INIT":
					mListView = new XListView(context);
					mListView.setCacheColorHint(Color.rgb(0, 0, 0));
					LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
					layout.addView(mListView,ll);
					mListView.setXListViewListener(DataPoint.this);
					adapter = new MyAdapter();
					mListView.setAdapter(adapter);
					mListView.addHeaderView(LayoutInflater.from(context).inflate(R.layout.datapoint_header, null));
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
		public View getView(int position, View convertView, ViewGroup parent) {
			View myView = LayoutInflater.from(context).inflate(R.layout.datapoint_item, null);
			TextView label = (TextView) myView.findViewById(R.id.datapoint_label);
			TextView rank = (TextView) myView.findViewById(R.id.datapoint_rank);
			TextView name = (TextView) myView.findViewById(R.id.datapoint_name);
			TextView session = (TextView) myView.findViewById(R.id.datapoint_session);
			TextView win = (TextView) myView.findViewById(R.id.datapoint_win);
			TextView tie = (TextView) myView.findViewById(R.id.datapoint_tie);
			TextView lose = (TextView) myView.findViewById(R.id.datapoint_lose);
			TextView GD = (TextView) myView.findViewById(R.id.datapoint_GD);
			TextView point = (TextView) myView.findViewById(R.id.datapoint_point);
			int ranks = position%4 + 1;
			rank.setText(String.valueOf(ranks));
			if(TeamContent.NATIONS.get(localData.get(position).teamName) == null){
				Log.d("name", TeamContent.NATIONS.get(localData.get(position).teamName).toString());
				System.out.print(localData.get(position).teamName);
			}
			//System.out.print(localData.get(position).teamName);
			name.setText(TeamContent.NATIONS.get(localData.get(position).teamName).toString());
			session.setText(String.valueOf(localData.get(position).sessions));
		    win.setText(String.valueOf(localData.get(position).wins));
			lose.setText(String.valueOf(localData.get(position).loses));
			tie.setText(String.valueOf(localData.get(position).ties));
		    GD.setText(String.valueOf(localData.get(position).GD));
			point.setText(String.valueOf(localData.get(position).point));
			
			if(position % 4 == 0){
				label.setVisibility(View.VISIBLE);
				char c = (char) ('A'+position/4);
				label.setText(String.valueOf(c)+" 组");
			}
			else{
				label.setVisibility(View.GONE);
			}
			if(position % 2 == 1){
				myView.setBackgroundColor(Color.rgb(219, 238, 244));
			}
			return myView;
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
}
