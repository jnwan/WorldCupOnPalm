package com.jnwan.worldcuponplam.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.jnwan.worldcuponplam.model.DataAssistDetail;
import com.jnwan.worldcuponplam.model.DataShooterDetail;
import com.jnwan.worldcuponplam.model.DataShooterDetail;
import com.jnwan.worldcuponplam.model.TeamContent;
import com.jnwan.worldcuponplam.myview.XListView;

public class DataAssist extends Fragment implements XListView.IXListViewListener,View.OnClickListener{
	    private static ArrayList<DataAssistDetail> data;
	    private Context context;
	    private View rootView;
	    private LinearLayout layout;
	    private TextView errorText;
	    private Button errorButton;
	    private ProgressBar progressbar;
	    private XListView mListView;
	    private MyAdapter adapter;
	    private String lastTime;
	    private ArrayList<DataAssistDetail> localData;
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
			mListView.addHeaderView(LayoutInflater.from(context).inflate(R.layout.dataassist_header, null));
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
            HttpGet httpGet = new HttpGet("http://jnwan.dyndns-server.com/assist.php");
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

                String name = jsonObject.getString("assist_player");

                String nation = jsonObject.getString("assist_nationalteam");
                
                String club = jsonObject.getString("assist_clubteam");

                int assist = jsonObject.getInt("assist_assist");
                
                data.add(new DataAssistDetail(name, nation, club, assist));

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
					mListView.setXListViewListener(DataAssist.this);
					adapter = new MyAdapter();
					mListView.setAdapter(adapter);
					mListView.addHeaderView(LayoutInflater.from(context).inflate(R.layout.dataassist_header, null));
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
			View myView = LayoutInflater.from(context).inflate(R.layout.datashooter_item, null);
			TextView rank = (TextView) myView.findViewById(R.id.datashooter_rank);
			TextView name = (TextView) myView.findViewById(R.id.datashooter_player);
			TextView nation = (TextView) myView.findViewById(R.id.datashooter_nation);
			TextView club = (TextView) myView.findViewById(R.id.datashooter_club);
			TextView goal = (TextView) myView.findViewById(R.id.datashooter_goal);
			int ranks = position + 1;
			rank.setText(String.valueOf(ranks));
            name.setText(localData.get(position).player);
            nation.setText(localData.get(position).national_team);
            club.setText(localData.get(position).club_team);
            goal.setText(String.valueOf(localData.get(position).assist));			
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

