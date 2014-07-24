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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
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
import com.jnwan.worldcuponplam.model.LinkInfo;
import com.jnwan.worldcuponplam.model.SinaItem;
import com.jnwan.worldcuponplam.myview.XListView;

@SuppressLint("ShowToast")
public class SinaNewsFragment extends Fragment implements XListView.IXListViewListener,View.OnClickListener{
	private Context context;
    private View rootView;
    private LinearLayout layout;
    private TextView errorText;
    private Button errorButton;
    private ProgressBar progressbar;
    private XListView mListView;
    private MyAdapter adapter;
    private String lastTime;
    private ArrayList<String> data;
    private ArrayList<SinaItem> localdata;
    private String more;
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
			mListView.setDividerHeight(0);
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
            HttpGet httpGet = new HttpGet("http://jnwan.dyndns-server.com/sina.php");
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

                String topline = jsonObject.getString("sina_topline");
                String listnews = jsonObject.getString("sina_listnews");
                
                data.add(topline);
                data.add(listnews);

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
				localdata = new ArrayList<>();
				parseTopline();
				parseNews();
				localdata.add(null);
				switch (state) {
				case "INIT":
					mListView = new XListView(context);
					mListView.setCacheColorHint(Color.rgb(0, 0, 0));
					LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
					layout.addView(mListView,ll);
					mListView.setXListViewListener(SinaNewsFragment.this);
					mListView.setDividerHeight(0);
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

		private void parseNews() {
			Element doc = Jsoup.parse(data.get(1));
			Element mores = doc.select("a").first();
			SinaItem moreItem = new SinaItem();
			moreItem.item.add(new LinkInfo(mores.text(), mores.attr("href")));
			more = mores.attr("href");
			moreItem.setType("title");
			localdata.add(moreItem);
			Element ul = doc.select("ul").first();
			for(Element e : ul.select("li")){
				SinaItem sinaItem = new SinaItem();
				for(Element a : e.select("a")){
					sinaItem.item.add(new LinkInfo(a.text(), a.attr("href")));
				}
				sinaItem.setType("list");
				localdata.add(sinaItem);
			}
		}

		private void parseTopline() {
			 Element doc = Jsoup.parse(data.get(0));
    		 Element div = doc.select("div").first();
    		 for(Element e : div.children()){
    			 if(e.tagName().equals("h2")){
    				 SinaItem sinaItem = new SinaItem();
    				 for(Element a : e.select("a")){
    					 sinaItem.item.add(new LinkInfo(a.text(), a.attr("href")));
    				 }
    				 sinaItem.setType("main");
    				 localdata.add(sinaItem);
    			 }
    			 else{
    				 SinaItem sinaItem = new SinaItem();
    				 for(Element a:e.select("a")){
    					 sinaItem.item.add(new LinkInfo(a.text(), a.attr("href")));
    				 }
    				 sinaItem.setType("assist");
    				 localdata.add(sinaItem);
    			 }
	        }
		}
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
	@Override
	public void onClick(View v) {
		hideError();
		init();
		
	}
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return localdata.size();
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
			return false;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SinaItem item = localdata.get(position);
			View myView;
			LinearLayout linearLayout;
			if(item == null){
				View eachView = LayoutInflater.from(context).inflate(R.layout.textview_center, null);
				TextView textView = (TextView) eachView.findViewById(R.id.textview);
				textView.setText("查看更多...");
				textView.setTextSize(19);
				textView.setGravity(Gravity.CENTER);
				eachView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,MyWebActivity.class);
						intent.putExtra("link", more);
						startActivity(intent);
					}
				});
				eachView.setBackgroundColor(Color.rgb(219, 238, 244));
				return eachView;
			}
			else{
			
			myView = LayoutInflater.from(context).inflate(R.layout.news_item_center, null);
			linearLayout = (LinearLayout) myView.findViewById(R.id.news_item_center);
			
			
			
			for(final LinkInfo linkInfo : item.item){
				View eachView = LayoutInflater.from(context).inflate(R.layout.textview, null);
				TextView textView = (TextView) eachView.findViewById(R.id.textview);
				textView.setText(linkInfo.text);
				textView.setTextSize(17);
				if(item.type.equals("main")){
					textView.setTextColor(Color.RED);
					textView.setTextSize(20);
				}
				if(item.type.equals("assist")){
					textView.setTextSize(16);
				}
				if(item.type.equals("title")){
					textView.setTextColor(Color.parseColor("#003300"));
					textView.setText("焦点要闻");
					textView.setTextSize(18);
				}
				eachView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,MyWebActivity.class);
						intent.putExtra("link", linkInfo.link);
						startActivity(intent);					
					}
				});
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				linearLayout.addView(eachView,ll);
			}
			myView.setBackgroundColor(Color.rgb(219, 238, 244));
			return myView;
		}
			
		}
		
	}

}

