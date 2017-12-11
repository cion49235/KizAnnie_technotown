package annie.kiz.view.technotown.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import annie.kiz.view.technotown.R;
import annie.kiz.view.technotown.adapter.SubAdapter;
import annie.kiz.view.technotown.data.Pause_DBOpenHelper;
import annie.kiz.view.technotown.data.Sub_Data;
import annie.kiz.view.technotown.mediaplayer.ContinueMediaPlayer;
import annie.kiz.view.technotown.util.Crypto;
import annie.kiz.view.technotown.util.Utils;
import annie.kiz.view.technotown.videoplayer.CustomVideoPlayer;

import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdViewListener;
import com.admixer.CustomPopup;
import com.admixer.CustomPopupListener;
import com.admixer.InterstitialAd;
import com.admixer.InterstitialAdListener;
import com.google.android.gms.ads.AdView;
public class SubActivity extends Activity implements OnItemClickListener, OnClickListener, OnScrollListener, AdViewListener, InterstitialAdListener{
	public static Context context;
	public ConnectivityManager connectivityManger;
	public NetworkInfo mobile;
	public NetworkInfo wifi;
	public static SubAdapter sub_adapter;
	public static ListView listview_main;
	public int SDK_INT = android.os.Build.VERSION.SDK_INT;
	public static LinearLayout layout_nodata;
	public static RelativeLayout ad_layout;
	public static boolean loadingMore = true;
	public static boolean exeFlag;
	public Handler handler = new Handler();
	public static int start_index;
	public static int itemsPerPage = 50;
	public static int current_position = 0;
	public boolean flag;
	public Sub_ParseAsync sub_parseAsync = null;
	public ProgressDialog progressDialog = null;
	public static Pause_DBOpenHelper pause_mydb;
	public static NotificationManager notificationManager;
	public static Notification notification;
	public static int noti_state = 1;
	public static TextView txt_main_title;
	public static int TotalRow;;
	public static ArrayList<Sub_Data> list;
	public static LinearLayout layout_progress;
	public static Button bt_category, bt_intent_favorite, bt_plus_coin;
	public static LinearLayout action_layout;
	public static Button bt_all_select, bt_play_video, bt_home;
	public Cursor cursor;
	public static AlertDialog alertDialog;
	public static int category_which = 0;
	public static SharedPreferences settings,pref;
	public Editor edit;
	public boolean retry_alert = false;
	public static String num, subject, thumb;
	public String searchKeyword;
	private AdView adView;
	public static com.admixer.InterstitialAd interstialAd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.sub_activity);
	setVolumeControlStream(AudioManager.STREAM_MUSIC);
	context = this;
	
	pref = getSharedPreferences(context.getString(R.string.txt_main_activity36), Activity.MODE_PRIVATE);
	MainActivity.coin = pref.getInt("coin", MainActivity.coin);
	
	num = getIntent().getStringExtra("num");
	subject = getIntent().getStringExtra("subject");
	thumb = getIntent().getStringExtra("thumb");
	
	txt_main_title = (TextView)findViewById(R.id.txt_main_title);
	txt_main_title.setText(subject);
	
	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "0pv7we6d");
	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/6752137777");
	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/7873647755");
	
	addBannerView();
		
	start_index = 1;
	layout_nodata = (LinearLayout)findViewById(R.id.layout_nodata);
	layout_progress = (LinearLayout)findViewById(R.id.layout_progress);
	action_layout = (LinearLayout)findViewById(R.id.action_layout);
	listview_main = (ListView)findViewById(R.id.listview_main);
	bt_category = (Button)findViewById(R.id.bt_category);
	bt_category.setText(context.getString(R.string.txt_main_activity21));
	bt_category.setTextColor(Color.BLACK);
	bt_all_select = (Button)findViewById(R.id.bt_all_select);
	bt_play_video = (Button)findViewById(R.id.bt_play_video);
	bt_home = (Button)findViewById(R.id.bt_home);
	bt_intent_favorite = (Button)findViewById(R.id.bt_intent_favorite);
	bt_plus_coin = (Button)findViewById(R.id.bt_plus_coin);
	bt_all_select.setOnClickListener(this);
	bt_play_video.setOnClickListener(this);
	bt_category.setOnClickListener(this);
	bt_home.setOnClickListener(this);
	bt_intent_favorite.setOnClickListener(this);
	bt_plus_coin.setOnClickListener(this);
	pause_mydb = new Pause_DBOpenHelper(this);
	list = new ArrayList<Sub_Data>();
	list.clear();
	retry_alert = true;
	displaylist();	
	exit_handler();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		retry_alert = false;
    	
		settings = getSharedPreferences(context.getString(R.string.txt_main_activity36), MODE_PRIVATE);
		edit = settings.edit();
		edit.putInt("category_which", 0);
		edit.commit();
		
		current_position = 0;
    	start_index = 1;
		loadingMore = true;
		exeFlag = false;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		layout_progress.setVisibility(View.GONE);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if(sub_adapter != null){
			sub_adapter.notifyDataSetChanged();	
		}
//		Log.i("dsu", "onRestart");
	}
	
	public static void event_alertdialog(int event_coin){
		SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.txt_main_activity36), MODE_PRIVATE);
		Editor edit = settings.edit();
		edit.putInt("coin", MainActivity.coin + event_coin);
		edit.commit();
		
		pref = context.getSharedPreferences(context.getString(R.string.txt_main_activity36), Activity.MODE_PRIVATE);
		MainActivity.coin = pref.getInt("coin", MainActivity.coin);
		alertDialog = new AlertDialog.Builder(context)
	    .setTitle(context.getString(R.string.txt_event_coin_ment1) + "" + event_coin)
		.setIcon(R.drawable.bt_plus_coin_normal)
		.setMessage(context.getString(R.string.txt_event_coin_ment2) +" "+context.getString(R.string.txt_alert_search_ment4) + MainActivity.coin)
		.setPositiveButton(context.getString(R.string.txt_alert_search_button_yes), new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				txt_main_title.setText(context.getString(R.string.app_name) +  "  " + context.getString(R.string.txt_coin) + Integer.toString(MainActivity.coin));
				dialog.dismiss();
			}
		}).show();
	}
	
	public void exit_handler(){
    	handler = new Handler(){
    		@Override
    		public void handleMessage(Message msg) {
    			if(msg.what == 0){
    				flag = false;
    			}
    		}
    	};
    }
	
	public void addBannerView() {
    	AdInfo adInfo = new AdInfo("0pv7we6d");
    	adInfo.setTestMode(false);
        com.admixer.AdView adView = new com.admixer.AdView(this);
        adView.setAdInfo(adInfo, this);
        adView.setAdViewListener(this);
        ad_layout = (RelativeLayout)findViewById(R.id.ad_layout);
        if(ad_layout != null){
        	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ad_layout.addView(adView, params);	
        }
    }
	
	public void addInterstitialView() {
    	if(interstialAd == null) {
        	AdInfo adInfo = new AdInfo("0pv7we6d");
//        	adInfo.setTestMode(false);
        	interstialAd = new com.admixer.InterstitialAd(this);
        	interstialAd.setAdInfo(adInfo, this);
        	interstialAd.setInterstitialAdListener(this);
        	interstialAd.startInterstitial();
    	}
    }
	
	public void displaylist(){
		sub_parseAsync = new Sub_ParseAsync();
		sub_parseAsync.execute();
		if (SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			listview_main.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		listview_main.setOnItemClickListener(this);
		listview_main.setItemsCanFocus(false);
		listview_main.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listview_main.setOnScrollListener(this);
	}
	
	public class Sub_ParseAsync extends AsyncTask<String, Integer, String>{
		String Response;
		Sub_Data sub_data;
		String total_row;
		String parse_num;
		String parse_subject;
		String parse_thumb;
		String parse_portal;
		ArrayList<Sub_Data> menuItems = new ArrayList<Sub_Data>();
		String i;
		public Sub_ParseAsync(){
		}
			@Override
			protected String doInBackground(String... params) {
				String sTag;
				if(Integer.parseInt(num) > 0 && Integer.parseInt(num) < 301){
					i = "1";
				}else if(Integer.parseInt(num) > 300 && Integer.parseInt(num) < 601){
					i = "2";
				}else if(Integer.parseInt(num) > 600 && Integer.parseInt(num) < 901){
					i = "3";
				}else if(Integer.parseInt(num) > 900 && Integer.parseInt(num) < 1201){
					i = "4";
				}else if(Integer.parseInt(num) > 1200 && Integer.parseInt(num) < 1501){
					i = "5";
				}else if(Integer.parseInt(num) > 1500 && Integer.parseInt(num) < 1801){
					i = "6";
				}else if(Integer.parseInt(num) > 1800 && Integer.parseInt(num) < 2101){
					i = "7";
				}else if(Integer.parseInt(num) > 2100 && Integer.parseInt(num) < 2401){
					i = "8";
				}else if(Integer.parseInt(num) > 2400 && Integer.parseInt(num) < 2701){
					i = "9";
				}else if(Integer.parseInt(num) > 2700 && Integer.parseInt(num) < 3001){
					i = "10";
				}else if(Integer.parseInt(num) > 3000){
					i = "11";
				}
				try{
				   String data = Crypto.decrypt(Utils.data, context.getString(R.string.txt_str8));
		           String str = data+i+".php?view="+num; 
		           HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(str).openConnection();
		           HttpURLConnection.setFollowRedirects(false);
		           localHttpURLConnection.setConnectTimeout(15000);
		           localHttpURLConnection.setReadTimeout(15000); 
		           localHttpURLConnection.setRequestMethod("GET");
		           localHttpURLConnection.connect();
		           InputStream inputStream = new URL(str).openStream(); //open Stream�� ����Ͽ� InputStream�� �����մϴ�.
		           XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
		           XmlPullParser xpp = factory.newPullParser();
		           xpp.setInput(inputStream, "EUC-KR"); //euc-kr�� �� �����մϴ�. utf-8�� �ϴϱ� ����������
		           int eventType = xpp.getEventType();
		           while (eventType != XmlPullParser.END_DOCUMENT) {
			        	if (eventType == XmlPullParser.START_DOCUMENT) {
			        	}else if (eventType == XmlPullParser.END_DOCUMENT) {
			        	}else if (eventType == XmlPullParser.START_TAG){
			        		sTag = xpp.getName();
			        		if(sTag.equals("videoid")){
			        			sub_data = new Sub_Data();
			        			Response = xpp.nextText()+"";
			            	}else if(sTag.equals("subject")){
			            		parse_subject = xpp.nextText()+"";
			            	}else if(sTag.equals("portal")){
			            		parse_portal = xpp.nextText()+"";
			            	}else if(sTag.equals("thumb")){
			            		parse_thumb = xpp.nextText()+"";
			            	}
			        	} else if (eventType == XmlPullParser.END_TAG){
			            	sTag = xpp.getName();
			            	if(sTag.equals("Content")){
			            		sub_data.videoid = Response;
			            		sub_data.subject = parse_subject;
			            		sub_data.portal = parse_portal;
			            		sub_data.thumb = parse_thumb;
			            		list.add(sub_data);
			            	}
			            } else if (eventType == XmlPullParser.TEXT) {
			            }
			            eventType = xpp.next();
			        }
		         }
		         catch (SocketTimeoutException localSocketTimeoutException)
		         {
		         }
		         catch (ClientProtocolException localClientProtocolException)
		         {
		         }
		         catch (IOException localIOException)
		         {
		         }
		         catch (Resources.NotFoundException localNotFoundException)
		         {
		         }
		         catch (XmlPullParserException localXmlPullParserException)
		         {
		         }
		         catch (NullPointerException NullPointerException)
		         {
		         }
				 catch (Exception e)
		         {
		         }
		         return Response;
			}
			
			@Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            layout_progress.setVisibility(View.VISIBLE);
	        }
			@Override
			protected void onPostExecute(String Response) {
				super.onPostExecute(Response);
				layout_progress.setVisibility(View.GONE);
				try{
					if(Response != null){
						for (int i = 0; i < list.size(); i++) {
							sub_adapter = new SubAdapter(context, menuItems, listview_main);
							listview_main.setAdapter(sub_adapter);
							listview_main.setFocusable(true);
							listview_main.setSelected(true);
							menuItems.add(list.get(i));
							if(listview_main.getCount() == 0){
								layout_nodata.setVisibility(View.VISIBLE);
							}else{
								layout_nodata.setVisibility(View.GONE);
							}
					}
					}else{
						layout_nodata.setVisibility(View.VISIBLE);
						Retry_AlertShow(context.getString(R.string.sub6_txt8));
					}
				}catch(NullPointerException e){
				}
			}
			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
			}
		}
	
	@Override
	public void onClick(View view) {
		if(view == bt_category){
			final String channel_title[] = {
					context.getString(R.string.txt_channel_title0),
					context.getString(R.string.txt_channel_title1),
					context.getString(R.string.txt_channel_title2),
					context.getString(R.string.txt_channel_title3),
					context.getString(R.string.txt_channel_title4),
					context.getString(R.string.txt_channel_title5),
					context.getString(R.string.txt_channel_title6),
					context.getString(R.string.txt_channel_title7),
					context.getString(R.string.txt_channel_title8),
					context.getString(R.string.txt_channel_title9),
					context.getString(R.string.txt_channel_title10)
			};
			settings = getSharedPreferences(context.getString(R.string.txt_main_activity36), MODE_PRIVATE);
			edit = settings.edit();
			pref = getSharedPreferences(context.getString(R.string.txt_main_activity36), Activity.MODE_PRIVATE);
			category_which = pref.getInt("category_which", 0);
			
			alertDialog = new AlertDialog.Builder(context)
			.setTitle(context.getString(R.string.txt_love_ment))
			.setSingleChoiceItems(channel_title, category_which, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which == 0){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						list = new ArrayList<Sub_Data>();
						list.clear();
						num = "901";
						displaylist();
						bt_category.setText(channel_title[0]);
						edit.putInt("category_which", 0);
					}else if(which == 1){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						list = new ArrayList<Sub_Data>();
						list.clear();
						num = "902";
						displaylist();
						bt_category.setText(channel_title[1]);
						edit.putInt("category_which", 1);
					}else if(which == 2){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						list = new ArrayList<Sub_Data>();
						list.clear();
						num = "903";
						displaylist();
						bt_category.setText(channel_title[2]);
						edit.putInt("category_which", 2);
					}else if(which == 3){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						list = new ArrayList<Sub_Data>();
						list.clear();
						num = "904";
						displaylist();
						bt_category.setText(channel_title[3]);
						edit.putInt("category_which", 3);
					}else if(which == 4){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						list = new ArrayList<Sub_Data>();
						list.clear();
						num = "905";
						displaylist();
						bt_category.setText(channel_title[4]);
						edit.putInt("category_which", 4);
					}else if(which == 5){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						list = new ArrayList<Sub_Data>();
						list.clear();
						num = "906";
						displaylist();
						bt_category.setText(channel_title[5]);
						edit.putInt("category_which", 5);
					}else if(which == 6){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						list = new ArrayList<Sub_Data>();
						list.clear();
						num = "907";
						displaylist();
						bt_category.setText(channel_title[6]);
						edit.putInt("category_which", 6);
					}else if(which == 7){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						list = new ArrayList<Sub_Data>();
						list.clear();
						num = "908";
						displaylist();
						bt_category.setText(channel_title[7]);
						edit.putInt("category_which", 7);
					}else if(which == 8){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						list = new ArrayList<Sub_Data>();
						list.clear();
						num = "909";
						displaylist();
						bt_category.setText(channel_title[8]);
						edit.putInt("category_which", 8);
					}else if(which == 9){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						list = new ArrayList<Sub_Data>();
						list.clear();
						num = "910";
						displaylist();
						bt_category.setText(channel_title[9]);
						edit.putInt("category_which", 9);
					}else if(which == 10){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						list = new ArrayList<Sub_Data>();
						list.clear();
						num = "911";
						displaylist();
						bt_category.setText(channel_title[10]);
						edit.putInt("category_which", 10);
					}
					edit.commit();
					dialog.dismiss();
				}
			}).show();
			
		}else if(view == bt_all_select){
			if(bt_all_select.isSelected()){
				bt_all_select.setSelected(false);
				bt_all_select.setText(context.getString(R.string.txt_continue_activity4));
				for(int i=0; i < listview_main.getCount(); i++){
					listview_main.setItemChecked(i, false);
				}
				action_layout.setVisibility(View.GONE);
			}else{
				bt_all_select.setSelected(true);
				bt_all_select.setText(context.getString(R.string.txt_continue_activity7));
				for(int i=0; i < listview_main.getCount(); i++){
					listview_main.setItemChecked(i, true);
				}
			}
		}else if(view == bt_play_video){
			pref = getSharedPreferences(context.getString(R.string.txt_main_activity36), Activity.MODE_PRIVATE);
			MainActivity.coin = pref.getInt("coin", MainActivity.coin);
//			if(MainActivity.coin > 0){
				SparseBooleanArray sba = listview_main.getCheckedItemPositions();
				ArrayList<String> array_videoid = new ArrayList<String>();
				ArrayList<String> array_subject = new ArrayList<String>();
				ArrayList<String> array_portal = new ArrayList<String>();
				if(sba.size() != 0){
//					Main_Data tmp_main_data = (Main_Data)main_adapter.getItem(0);
//					String tmp_portal = tmp_main_data.portal;
//					if(tmp_portal.equals("youtube")){
//						for(int i = 0; i < listview_main.getCount(); i++){
//							if(sba.get(i)){
//									Sub_Data sub_data = (Sub_Data)sub_adapter.getItem(i);
//									String videoid = sub_data.videoid;
//									String subject = sub_data.subject;
//									String portal = sub_data.portal;
//									array_videoid.add(videoid);
//									array_subject.add(subject);
//									array_portal.add(portal);
//									sba = listview_main.getCheckedItemPositions();
//							}
//						}
//						if(array_videoid.size() != 0){
//							Intent intent = new Intent(context, CustomYoutubePlayer.class);
//							intent.putExtra("array_videoid", array_videoid);
//							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//							startActivity(intent);
//						}
//					}else{
						for(int i = listview_main.getCount() -1; i>=0; i--){
							if(sba.get(i)){
								Sub_Data sub_data = (Sub_Data)sub_adapter.getItem(i);
								String videoid = sub_data.videoid;
								String subject = sub_data.subject;
								String portal = sub_data.portal;
								array_videoid.add(videoid);
								array_subject.add(subject);
								array_portal.add(portal);
								sba = listview_main.getCheckedItemPositions();
							}
						}
						if(array_videoid.size() != 0){
							Intent intent = new Intent(context, CustomVideoPlayer.class);
							intent.putExtra("array_videoid", array_videoid);
							intent.putExtra("array_subject", array_subject);
							intent.putExtra("array_portal", array_portal);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
							startActivity(intent);
						}
//					}
				}
//			}else{
//				alertDialog = new AlertDialog.Builder(this)
//			    .setTitle(context.getString(R.string.txt_plus_coin_ment1))
//				.setIcon(R.drawable.bt_plus_coin_normal)
//				.setMessage(context.getString(R.string.txt_plus_coin_ment2))
//				.setPositiveButton(context.getString(R.string.txt_alert_button_yes), new DialogInterface.OnClickListener() {				
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						Toast.makeText(context, context.getString(R.string.txt_coin_ment), Toast.LENGTH_LONG).show();
//						addInterstitialView();
//					}
//				})
//				.setNegativeButton(context.getString(R.string.txt_alert_button_no), new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				}).show();
//			}
		}else if(view == bt_intent_favorite){
			Intent intent = new Intent(this, FavoriteActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}else if(view == bt_plus_coin){
			alertDialog = new AlertDialog.Builder(this)
		    .setTitle(context.getString(R.string.txt_plus_coin_ment1))
			.setIcon(R.drawable.bt_plus_coin_normal)
			.setMessage(context.getString(R.string.txt_plus_coin_ment2))
			.setPositiveButton(context.getString(R.string.txt_alert_button_yes), new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(context, context.getString(R.string.txt_coin_ment), Toast.LENGTH_LONG).show();
					addInterstitialView();
				}
			})
			.setNegativeButton(context.getString(R.string.txt_alert_button_no), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
		}else if(view == bt_home){
			onBackPressed();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		int selectd_count = 0;
    	SparseBooleanArray sba = listview_main.getCheckedItemPositions();
		if(sba.size() != 0){
			for(int i = listview_main.getCount() -1; i>=0; i--){
				if(sba.get(i)){
					sba = listview_main.getCheckedItemPositions();
					selectd_count++;
				}
			}
		}
		if(selectd_count == 0){
			action_layout.setVisibility(View.GONE);
		}else{
			action_layout.setVisibility(View.VISIBLE);
		}
		if(sub_adapter != null){
			sub_adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(view == listview_main){
			if(totalItemCount != 0 && firstVisibleItem  > 1 ){
				listview_main.setFastScrollEnabled(true);
			}else{
				listview_main.setFastScrollEnabled(false);
			}
		}
	}
	
	public void Retry_AlertShow(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(SubActivity.this);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setInverseBackgroundForced(true);
		builder.setNeutralButton(context.getString(R.string.txt_main_activity14), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				 current_position = 0;
            	 loadingMore = true;
            	 exeFlag = false;
            	 sub_parseAsync = new Sub_ParseAsync();
            	 sub_parseAsync.execute();
            	 dialog.dismiss();
			}
		});
		builder.setNegativeButton(context.getString(R.string.txt_main_activity13), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
             	 dialog.dismiss();
			}
		});
		AlertDialog myAlertDialog = builder.create();
		if(retry_alert) myAlertDialog.show();
	}
	
	public static void setNotification_Cancel(){
    	if(notificationManager != null) notificationManager.cancel(noti_state);
    }
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	//** BannerAd �̺�Ʈ�� *************
	@Override
	public void onClickedAd(String arg0, com.admixer.AdView arg1) {
	}

	@Override
	public void onFailedToReceiveAd(int arg0, String arg1,
			com.admixer.AdView arg2) {
	}

	@Override
	public void onReceivedAd(String arg0, com.admixer.AdView arg1) {
	}

	@Override
	public void onInterstitialAdClosed(InterstitialAd arg0) {
		interstialAd = null;
		SharedPreferences settings = getSharedPreferences(context.getString(R.string.txt_main_activity36), MODE_PRIVATE);
		Editor edit = settings.edit();
		edit.putInt("coin", MainActivity.coin+1);
		edit.commit();
		
		pref = getSharedPreferences(context.getString(R.string.txt_main_activity36), Activity.MODE_PRIVATE);
		MainActivity.coin = pref.getInt("coin", MainActivity.coin);
		alertDialog = new AlertDialog.Builder(this)
	    .setTitle(context.getString(R.string.txt_alert_search_ment3))
		.setIcon(R.drawable.bt_plus_coin_normal)
		.setMessage(context.getString(R.string.txt_alert_search_ment4) + MainActivity.coin)
		.setPositiveButton(context.getString(R.string.txt_alert_search_button_yes), new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}

	@Override
	public void onInterstitialAdFailedToReceive(int arg0, String arg1,
			InterstitialAd arg2) {
		interstialAd = null;
		Toast.makeText(context, context.getString(R.string.txt_plus_coin_ment3), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onInterstitialAdReceived(String arg0, InterstitialAd arg1) {
		interstialAd = null;
	}

	@Override
	public void onInterstitialAdShown(String arg0, InterstitialAd arg1) {
	}

	@Override
	public void onLeftClicked(String arg0, InterstitialAd arg1) {
	}

	@Override
	public void onRightClicked(String arg0, InterstitialAd arg1) {
	}
}
