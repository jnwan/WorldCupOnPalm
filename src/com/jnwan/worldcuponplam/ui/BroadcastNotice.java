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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.jnwan.worldcuponplam.model.MyDate;
import com.jnwan.worldcuponplam.model.TeamContent;
import com.jnwan.worldcuponplam.myview.XListView;

public class BroadcastNotice extends Fragment implements XListView.IXListViewListener,View.OnClickListener{
    private View rootView;
    private LinearLayout layout;
    private TextView errorText;
    private Button errorButton;
    private ProgressBar progressbar;
    private Map<String, ArrayList<BroadcastDetail>> data;
    private ArrayList<BroadcastDetail> dataDayAfterTomo;
    private ArrayList<BroadcastDetail> dataToday;
    private ArrayList<BroadcastDetail> dataTomorrow;
    private XListView mListView;
    private MyDate today;
    private MyDate dayAfter;
    private MyDate tomorrow;
    private Context context;
    private MyAdapter adapter;
    private String lastTime = "";
    private int div1,div2,size;
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
			setTime();
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
					Toast.makeText(context, "无法连接服务器", Toast.LENGTH_SHORT).show();
					Onload();
				}
				progressbar.setVisibility(View.GONE);
			}
			else{
				dataToday = data.get(today.toString());
				if(dataToday == null){
					dataToday = new ArrayList<>();
					dataToday.add(null);
				}
				dataTomorrow = data.get(tomorrow.toString());
				if(dataTomorrow == null){
					dataTomorrow = new ArrayList<>();
					dataTomorrow.add(null);
				}
				dataDayAfterTomo = data.get(dayAfter.toString());
				if(dataDayAfterTomo == null){
					dataDayAfterTomo = new ArrayList<>();
					dataDayAfterTomo.add(null);
				}
				div1 = dataToday.size();
				div2 = div1 + dataTomorrow.size();
				size = div2 + dataDayAfterTomo.size();
				switch (state) {
				case "INIT":
					mListView = new XListView(context);
					mListView.setCacheColorHint(Color.rgb(0, 0, 0));
					LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
					layout.addView(mListView,ll);
					mListView.setXListViewListener(BroadcastNotice.this);
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

                 data = new HashMap<>();

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

                     String time = String.valueOf(month) + String.valueOf(date);

                    if (!data.containsKey(time)) {

                        data.put(time, new ArrayList<BroadcastDetail>());

                    }

                   data.get(time).add(new BroadcastDetail(team1, team2, type, history, new GameTime(month, date, hour, min), score1,score2));

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
	private void setTime() {
		 int month = 1 + Calendar.getInstance().get(Calendar.MONTH);
         int date = Calendar.getInstance().get(Calendar.DATE);
        today = new MyDate(month, date);
        if (date == 29) {
            tomorrow = new MyDate(month, date + 1);
            dayAfter = new MyDate(month + 1, 1);
            return;
        }
        if (date == 30) {
            tomorrow = new MyDate(month + 1, 1);
            dayAfter = new MyDate(month + 1, 2);
            return;
        }
        tomorrow = new MyDate(month, date + 1);
        dayAfter = new MyDate(month, date + 2);		
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
            setTime();
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
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		data = null;
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		data = null;
	}

	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return size;
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
			View myView = LayoutInflater.from(context).inflate(R.layout.broadcast_list_item, null);
			TextView datelabel = (TextView) myView.findViewById(R.id.date_label);
			ImageView image1 = (ImageView) myView.findViewById(R.id.broadcast_team1_logo);
			ImageView image2 = (ImageView) myView.findViewById(R.id.broadcast_team2_logo);
			TextView teamlabel1 = (TextView) myView.findViewById(R.id.broadcast_team1_label);
			TextView teamlabel2 = (TextView) myView.findViewById(R.id.broadcast_team2_label);
			TextView typeTextView = (TextView) myView.findViewById(R.id.broadcast_type_label);
			TextView timeTextView = (TextView) myView.findViewById(R.id.broadcast_time_label);
			TextView scoreTextView = (TextView) myView.findViewById(R.id.broadcast_score_label);
			if(position >= 0 && position < div1){
				if(dataToday.get(position) == null){
					View view = LayoutInflater.from(context).inflate(R.layout.broadcast_empty, null);
					TextView textView = (TextView) view.findViewById(R.id.broadcast_empty_label);
					String label = "今 天";
					textView.setText(label);
					return view;
				}
				else{
					BroadcastDetail bDetail = dataToday.get(position);
					final String team1 = bDetail.team1;
					final String team2 = bDetail.team2;
					String type = bDetail.type;
					final String history = bDetail.history;
					GameTime time = bDetail.gametime;
					int score1 = bDetail.score1;
					int score2 = bDetail.score2;
					image1.setImageResource(TeamContent.NATIONS.get(team1).logo);
					image2.setImageResource(TeamContent.NATIONS.get(team2).logo);
					teamlabel1.setText(TeamContent.NATIONS.get(team1).toString());
					teamlabel2.setText(TeamContent.NATIONS.get(team2).toString());
					if(type.equals("小组赛")){
					   typeTextView.setText(type+"   "+TeamContent.NATIONS.get(team1).group+"组");
					}
					else{
					   typeTextView.setText(type);
					}
					if(score1 == -1){
					    timeTextView.setText("时 间");
					    scoreTextView.setText(getTime(time));
					    myView.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								final CharSequence[] items = {TeamContent.NATIONS.get(team1).toString(), TeamContent.NATIONS.get(team2).toString(), "交战历史"}; 
								AlertDialog.Builder builder = new AlertDialog.Builder(context);
								builder.setTitle("选择");
								builder.setItems(items, new DialogInterface.OnClickListener() {
								    public void onClick(DialogInterface dialog, int item) {
								        if(item == 0){
								        	Intent intent = new Intent(context,TeamDetailActivity.class);
								        	intent.putExtra("team", team1);
								            startActivity(intent);
								            dialog.dismiss();
								        }
								        else if(item == 1){
								        	Intent intent = new Intent(context,TeamDetailActivity.class);
								        	intent.putExtra("team", team2);
								            startActivity(intent);
								            dialog.dismiss();
								        }
								        else{
								        	 AlertDialog.Builder factory = new AlertDialog.Builder(context);   
								             factory.setTitle("交战历史");  
								             factory.setMessage(history);  								    
								             factory.setPositiveButton("返回", new DialogInterface.OnClickListener() {  
								                   public void onClick(DialogInterface dialog, int which) {  
								                       dialog.dismiss();
								                   }  
								             }); 
								             factory.create();
								             factory.show();
								             dialog.dismiss();
								        }
								    }
								});
								AlertDialog alert = builder.create();
                                alert.show();
								
							}
						});
					}
					else{
						timeTextView.setText("比 分");
					    scoreTextView.setText(String.valueOf(score1)+" : "+String.valueOf(score2));
                        myView.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								final String[] items = {TeamContent.NATIONS.get(team1).toString(), TeamContent.NATIONS.get(team2).toString()}; 
								AlertDialog.Builder builder = new AlertDialog.Builder(context);
								builder.setTitle("选择");
								builder.setItems(items, new DialogInterface.OnClickListener() {
								    public void onClick(DialogInterface dialog, int item) {
								        if(item == 0){
								        	Intent intent = new Intent(context,TeamDetailActivity.class);
								        	intent.putExtra("team", team1);
								            startActivity(intent);
								            dialog.dismiss();
								        }
								        else if(item == 1){
								        	Intent intent = new Intent(context,TeamDetailActivity.class);
								        	intent.putExtra("team", team2);
								            startActivity(intent);
								            dialog.dismiss();
								        }
								    }
								});
								AlertDialog alert = builder.create();
                                alert.show();
								
							}
						});
					}
					if(position == 0){
						datelabel.setVisibility(View.VISIBLE);
						String label = "今 天";
						datelabel.setText(label);
					}
					else{
						datelabel.setVisibility(View.GONE);
					}	
					//myView.setBackgroundColor(Color.rgb(219, 238, 244));
					return myView;
				}
			}
			else if(position >= div1 && position < div2){
				if(dataTomorrow.get(position-div1) == null){
					View view = LayoutInflater.from(context).inflate(R.layout.broadcast_empty, null);
					TextView textView = (TextView) view.findViewById(R.id.broadcast_empty_label);
					String label = "明 天";
					textView.setText(label);
					return view;
				}
				else{
					BroadcastDetail bDetail = dataTomorrow.get(position-div1);
					final String team1 = bDetail.team1;
					final String team2 = bDetail.team2;
					String type = bDetail.type;
					final String history = bDetail.history;
					GameTime time = bDetail.gametime;
					int score1 = bDetail.score1;
					int score2 = bDetail.score2;
					image1.setImageResource(TeamContent.NATIONS.get(team1).logo);
					image2.setImageResource(TeamContent.NATIONS.get(team2).logo);
					teamlabel1.setText(TeamContent.NATIONS.get(team1).toString());
					teamlabel2.setText(TeamContent.NATIONS.get(team2).toString());
					if(type.equals("小组赛")){
						   typeTextView.setText(type+"   "+TeamContent.NATIONS.get(team1).group+"组");
						}
						else{
						   typeTextView.setText(type);
						}
					if(score1 == -1){
					    timeTextView.setText("时 间");
					    scoreTextView.setText(getTime(time));
  myView.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								final CharSequence[] items = {TeamContent.NATIONS.get(team1).toString(), TeamContent.NATIONS.get(team2).toString(), "交战历史"}; 
								AlertDialog.Builder builder = new AlertDialog.Builder(context);
								builder.setTitle("选择");
								builder.setItems(items, new DialogInterface.OnClickListener() {
								    public void onClick(DialogInterface dialog, int item) {
								        if(item == 0){
								        	Intent intent = new Intent(context,TeamDetailActivity.class);
								        	intent.putExtra("team", team1);
								            startActivity(intent);
								            dialog.dismiss();
								        }
								        else if(item == 1){
								        	Intent intent = new Intent(context,TeamDetailActivity.class);
								        	intent.putExtra("team", team2);
								            startActivity(intent);
								            dialog.dismiss();
								        }
								        else{
								        	 AlertDialog.Builder factory = new AlertDialog.Builder(context);   
								             factory.setTitle("交战历史");  
								             factory.setMessage(history);  								    
								             factory.setPositiveButton("返回", new DialogInterface.OnClickListener() {  
								                   public void onClick(DialogInterface dialog, int which) {  
								                       dialog.dismiss();
								                   }  
								             }); 
								             factory.create();
								             factory.show();
								             dialog.dismiss();
								        }
								    }
								});
								AlertDialog alert = builder.create();
                                alert.show();
								
							}
						});
					}
					else{
						timeTextView.setText("比 分");
					    scoreTextView.setText(String.valueOf(score1)+" : "+String.valueOf(score2));
myView.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								final CharSequence[] items = {TeamContent.NATIONS.get(team1).toString(), TeamContent.NATIONS.get(team2).toString()}; 
								AlertDialog.Builder builder = new AlertDialog.Builder(context);
								builder.setTitle("选择");
								builder.setItems(items, new DialogInterface.OnClickListener() {
								    public void onClick(DialogInterface dialog, int item) {
								        if(item == 0){
								        	Intent intent = new Intent(context,TeamDetailActivity.class);
								        	intent.putExtra("team", team1);
								            startActivity(intent);
								            dialog.dismiss();
								        }
								        else if(item == 1){
								        	Intent intent = new Intent(context,TeamDetailActivity.class);
								        	intent.putExtra("team", team2);
								            startActivity(intent);
								            dialog.dismiss();
								        }
								    }
								});
								AlertDialog alert = builder.create();
                                alert.show();
								
							}
						});
					}
					if(position == div1){
						datelabel.setVisibility(View.VISIBLE);
						String label = "明 天";
						datelabel.setText(label);
					}
					else{
						datelabel.setVisibility(View.GONE);
					}
					//myView.setBackgroundColor(Color.rgb(219, 238, 244));
					return myView;
				}
			}
			if(position >= div2 && position < size){
				if(dataDayAfterTomo.get(position-div2) == null){
					View view = LayoutInflater.from(context).inflate(R.layout.broadcast_empty, null);
					TextView textView = (TextView) view.findViewById(R.id.broadcast_empty_label);
					String label = "后 天";
					textView.setText(label);
					return view;
				}
				else{
					BroadcastDetail bDetail = dataDayAfterTomo.get(position-div2);
					final String team1 = bDetail.team1;
					final String team2 = bDetail.team2;
					String type = bDetail.type;
					final String history = bDetail.history;
					GameTime time = bDetail.gametime;
					int score1 = bDetail.score1;
					int score2 = bDetail.score2;
					image1.setImageResource(TeamContent.NATIONS.get(team1).logo);
					image2.setImageResource(TeamContent.NATIONS.get(team2).logo);
					teamlabel1.setText(TeamContent.NATIONS.get(team1).toString());
					teamlabel2.setText(TeamContent.NATIONS.get(team2).toString());
					if(type.equals("小组赛")){
						   typeTextView.setText(type+"   "+TeamContent.NATIONS.get(team1).group+"组");
						}
						else{
						   typeTextView.setText(type);
						}
					if(score1 == -1){
					    timeTextView.setText("时 间");
					    scoreTextView.setText(getTime(time));
  myView.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								final CharSequence[] items = {TeamContent.NATIONS.get(team1).toString(), TeamContent.NATIONS.get(team2).toString(), "交战历史"}; 
								AlertDialog.Builder builder = new AlertDialog.Builder(context);
								builder.setTitle("选择");
								builder.setItems(items, new DialogInterface.OnClickListener() {
								    public void onClick(DialogInterface dialog, int item) {
								        if(item == 0){
								        	Intent intent = new Intent(context,TeamDetailActivity.class);
								        	intent.putExtra("team", team1);
								            startActivity(intent);
								            dialog.dismiss();
								        }
								        else if(item == 1){
								        	Intent intent = new Intent(context,TeamDetailActivity.class);
								        	intent.putExtra("team", team2);
								            startActivity(intent);
								            dialog.dismiss();
								        }
								        else{
								        	 AlertDialog.Builder factory = new AlertDialog.Builder(context);   
								             factory.setTitle("交战历史");  
								             factory.setMessage(history);  								    
								             factory.setPositiveButton("返回", new DialogInterface.OnClickListener() {  
								                   public void onClick(DialogInterface dialog, int which) {  
								                       dialog.dismiss();
								                   }  
								             }); 
								             factory.create();
								             factory.show();
								             dialog.dismiss();
								        }
								    }
								});
								AlertDialog alert = builder.create();
                                alert.show();
								
							}
						});
					}
					else{
						timeTextView.setText("比 分");
					    scoreTextView.setText(String.valueOf(score1)+" : "+String.valueOf(score2));
myView.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								final CharSequence[] items = {TeamContent.NATIONS.get(team1).toString(), TeamContent.NATIONS.get(team2).toString()}; 
								AlertDialog.Builder builder = new AlertDialog.Builder(context);
								builder.setTitle("选择");
								builder.setItems(items, new DialogInterface.OnClickListener() {
								    public void onClick(DialogInterface dialog, int item) {
								        if(item == 0){
								        	Intent intent = new Intent(context,TeamDetailActivity.class);
								        	intent.putExtra("team", team1);
								            startActivity(intent);
								            dialog.dismiss();
								        }
								        else if(item == 1){
								        	Intent intent = new Intent(context,TeamDetailActivity.class);
								        	intent.putExtra("team", team2);
								            startActivity(intent);
								            dialog.dismiss();
								        }
								    }
								});
								AlertDialog alert = builder.create();
                                alert.show();
								
							}
						});
					}
					if(position == div2){
						datelabel.setVisibility(View.VISIBLE);
						String label = "后 天";
						datelabel.setText(label);
					}
					else{
						datelabel.setVisibility(View.GONE);
					}
					//myView.setBackgroundColor(Color.rgb(219, 238, 244));
					return myView;
				}
			}
			return null;
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

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}
	    
		
	}

}
