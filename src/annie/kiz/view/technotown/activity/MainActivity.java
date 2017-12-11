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

import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdViewListener;
import com.admixer.CustomPopup;
import com.admixer.CustomPopupListener;
import com.admixer.InterstitialAd;
import com.admixer.InterstitialAdListener;
import com.admixer.PopupInterstitialAdOption;
import com.google.android.gms.ads.AdView;
import com.mogua.localization.KoreanTextMatch;
import com.mogua.localization.KoreanTextMatcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import annie.kiz.view.technotown.R;
import annie.kiz.view.technotown.adapter.MainAdapter;
import annie.kiz.view.technotown.data.Favorite_DBopenHelper;
import annie.kiz.view.technotown.data.Main_Data;
import annie.kiz.view.technotown.data.Pause_DBOpenHelper;
import annie.kiz.view.technotown.util.Crypto;
import annie.kiz.view.technotown.util.PreferenceUtil;
import annie.kiz.view.technotown.util.Utils;
import kr.co.inno.autocash.service.AutoServiceActivity;
public class MainActivity extends Activity implements OnItemClickListener, OnClickListener, OnScrollListener, AdViewListener, CustomPopupListener, InterstitialAdListener{
	public static Context context;
	public ConnectivityManager connectivityManger;
	public NetworkInfo mobile;
	public NetworkInfo wifi;
	public static MainAdapter main_adapter;
	public static GridView listview_main;
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
	public Main_ParseAsync main_parseAsync = null;
	public ProgressDialog progressDialog = null;
	public static Favorite_DBopenHelper favorite_mydb;
	public static Pause_DBOpenHelper pause_mydb;
	public static NotificationManager notificationManager;
	public static Notification notification;
	public static int noti_state = 1;
	public static TextView txt_main_title;
	public static int TotalRow;;
	public static ArrayList<Main_Data> list;
	public static LinearLayout layout_progress;
	public static Button bt_category, bt_intent_favorite, bt_plus_coin, bt_bug_send;
	public static LinearLayout action_layout;
	public Cursor cursor;
	public static AlertDialog alertDialog;
	public static int category_which = 0;
	public static SharedPreferences settings,pref;
	public Editor edit;
	public boolean retry_alert = false;
//	public String num;
	public static EditText edit_searcher;
	public static ImageButton bt_home, bt_search_result; 
	public static String searchKeyword = "";
	private AdView adView;
	public static int coin = 20;
	public static int event_count = 1;
	public static int[] event_coin_count = {
		10,20,30,40,50,60,70,80,90,100,
		110,120,130,140,150,160,170,180,190,200,
		210,220,230,240,250,260,270,280,290,300,
		310,320,330,340};
	public static com.admixer.InterstitialAd interstialAd;
	KoreanTextMatch match1, match2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main_activity);
	setVolumeControlStream(AudioManager.STREAM_MUSIC);
	context = this;
	
	pref = getSharedPreferences(context.getString(R.string.txt_main_activity36), Activity.MODE_PRIVATE);
	coin = pref.getInt("coin", coin);
	event_count = pref.getInt("event_count", event_count);
//	Log.e("dsu", "on_event_count : " + event_count);
	if(event_count == event_coin_count[0]){
//		event_alertdialog(5);
	}else if(event_count == event_coin_count[1]){
//		event_alertdialog(10);
	}else if(event_count == event_coin_count[2]){
//		event_alertdialog(15);
	}else if(event_count == event_coin_count[3]){
//		event_alertdialog(5);
	}else if(event_count == event_coin_count[4]){
//		event_alertdialog(10);
	}else if(event_count == event_coin_count[5]){
//		event_alertdialog(15);
	}else if(event_count == event_coin_count[6]){
//		event_alertdialog(5);
	}else if(event_count == event_coin_count[7]){
//		event_alertdialog(10);
	}else if(event_count == event_coin_count[8]){
//		event_alertdialog(15);
	}else if(event_count == event_coin_count[9]){
//		event_alertdialog(5);
	}else if(event_count == event_coin_count[10]){
//		event_alertdialog(10);
	}else if(event_count == event_coin_count[11]){
//		event_alertdialog(15);
	}else if(event_count == event_coin_count[12]){
//		event_alertdialog(5);
	}else if(event_count == event_coin_count[13]){
//		event_alertdialog(10);
	}else if(event_count == event_coin_count[14]){
//		event_alertdialog(15);
	}else if(event_count == event_coin_count[15]){
//		event_alertdialog(5);
	}else if(event_count == event_coin_count[16]){
//		event_alertdialog(10);
	}else if(event_count == event_coin_count[17]){
//		event_alertdialog(15);
	}else if(event_count == event_coin_count[18]){
//		event_alertdialog(5);
	}else if(event_count == event_coin_count[19]){
//		event_alertdialog(10);
	}else if(event_count == event_coin_count[20]){
//		event_alertdialog(15);
	}else if(event_count == event_coin_count[21]){
//		event_alertdialog(5);
	}else if(event_count == event_coin_count[22]){
//		event_alertdialog(10);
	}else if(event_count == event_coin_count[23]){
//		event_alertdialog(15);
	}else if(event_count == event_coin_count[24]){
		event_alertdialog(5);
	}else if(event_count == event_coin_count[25]){
//		event_alertdialog(10);
	}else if(event_count == event_coin_count[26]){
//		event_alertdialog(15);
	}else if(event_count == event_coin_count[27]){
//		event_alertdialog(5);
	}else if(event_count == event_coin_count[28]){
//		event_alertdialog(10);
	}else if(event_count == event_coin_count[29]){
//		event_alertdialog(15);
	}else if(event_count == event_coin_count[30]){
		event_alertdialog(5);
	}else if(event_count == event_coin_count[31]){
//		event_alertdialog(10);
	}else if(event_count == event_coin_count[32]){
//		event_alertdialog(15);
	}else if(event_count == event_coin_count[33]){
//		event_alertdialog(30);
	}
	
	txt_main_title = (TextView)findViewById(R.id.txt_main_title);
	txt_main_title.setText(context.getString(R.string.app_name));
	
	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "0pv7we6d");
	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/6752137777");
	AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/7873647755");
	
	CustomPopup.setCustomPopupListener(this);
	CustomPopup.startCustomPopup(this, "0pv7we6d");
	
	addBannerView();
		
	start_index = 1;
	layout_nodata = (LinearLayout)findViewById(R.id.layout_nodata);
	layout_progress = (LinearLayout)findViewById(R.id.layout_progress);
	action_layout = (LinearLayout)findViewById(R.id.action_layout);
	listview_main = (GridView)findViewById(R.id.listview_main);
	bt_category = (Button)findViewById(R.id.bt_category);
	bt_category.setText(context.getString(R.string.txt_main_activity21));
	bt_category.setTextColor(Color.BLACK);
	edit_searcher = (EditText)findViewById(R.id.edit_searcher);
	bt_home = (ImageButton)findViewById(R.id.bt_home);
	bt_search_result = (ImageButton)findViewById(R.id.bt_search_result);
	bt_intent_favorite = (Button)findViewById(R.id.bt_intent_favorite);
	bt_plus_coin = (Button)findViewById(R.id.bt_plus_coin);
	bt_bug_send = (Button)findViewById(R.id.bt_bug_send);
	bt_home.setOnClickListener(this);
	bt_search_result.setOnClickListener(this);
	bt_category.setOnClickListener(this);
	bt_intent_favorite.setOnClickListener(this);
	bt_plus_coin.setOnClickListener(this);
	bt_bug_send.setOnClickListener(this);
	pause_mydb = new Pause_DBOpenHelper(this);
	favorite_mydb = new Favorite_DBopenHelper(this);
	list = new ArrayList<Main_Data>();
	list.clear();
	retry_alert = true;
	seacher_start();
	displaylist();	
	exit_handler();
	auto_service();
	}
	
	private void auto_service() {
        Intent intent = new Intent(context, AutoServiceActivity.class);
        context.stopService(intent);
        context.startService(intent);
    }
	
	@Override
	protected void onStart() {
		super.onStart();
		PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, false);
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
		// Custom Popup ����
		CustomPopup.stopCustomPopup();
		
		searchKeyword = "";
		edit_searcher.setText("");
    	
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
		if(main_adapter != null){
			main_adapter.notifyDataSetChanged();	
		}
//		Log.i("dsu", "onRestart");
	}
	
	public static void event_alertdialog(int event_coin){
		SharedPreferences settings = context.getSharedPreferences(context.getString(R.string.txt_main_activity36), MODE_PRIVATE);
		Editor edit = settings.edit();
		edit.putInt("coin", coin + event_coin);
		edit.commit();
		
		pref = context.getSharedPreferences(context.getString(R.string.txt_main_activity36), Activity.MODE_PRIVATE);
		coin = pref.getInt("coin", coin);
		alertDialog = new AlertDialog.Builder(context)
	    .setTitle(context.getString(R.string.txt_event_coin_ment1) + "" + event_coin)
		.setIcon(R.drawable.bt_plus_coin_normal)
		.setMessage(context.getString(R.string.txt_event_coin_ment2) +" "+context.getString(R.string.txt_alert_search_ment4) + coin)
		.setPositiveButton(context.getString(R.string.txt_alert_search_button_yes), new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				txt_main_title.setText(context.getString(R.string.app_name) +  "  " + context.getString(R.string.txt_coin) + Integer.toString(coin));
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
    	AdInfo adInfo = new AdInfo("0pv7we6d");
		adInfo.setInterstitialTimeout(0); 
		adInfo.setUseRTBGPSInfo(false);
		adInfo.setMaxRetryCountInSlot(-1);
		adInfo.setBackgroundAlpha(true); 

		PopupInterstitialAdOption adConfig = new PopupInterstitialAdOption();
		adConfig.setDisableBackKey(true);
		adConfig.setButtonLeft(context.getString(R.string.txt_finish_no), "#234234");
		adConfig.setButtonRight(context.getString(R.string.txt_finish_yes), "#234234");
		adConfig.setButtonFrameColor(null);
		adInfo.setInterstitialAdType(AdInfo.InterstitialAdType.Popup, adConfig);
		
		interstialAd = new InterstitialAd(this);
		interstialAd.setAdInfo(adInfo, this);
		interstialAd.setInterstitialAdListener(this);
		interstialAd.startInterstitial();
    }
	
	
	public void seacher_start(){
		edit_searcher.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable arg0) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					searchKeyword = s.toString().toLowerCase();
//					Log.e("dsu", "�˻��� : " + searchKeyword);
				} catch (Exception e) {
				}
			}
		});
	}
	
	public void displaylist(){
		main_parseAsync = new Main_ParseAsync();
		main_parseAsync.execute();
		if (SDK_INT >= Build.VERSION_CODES.HONEYCOMB){ //����� ���������� ���� ������ API ���}
			listview_main.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		listview_main.setOnItemClickListener(this);
		listview_main.setOnScrollListener(this);
	}
	
	public class Main_ParseAsync extends AsyncTask<String, Integer, String>{
		String Response;
		Main_Data main_data;
		String num;
		String subject;
		String thumb;
		String portal;
		ArrayList<Main_Data> menuItems = new ArrayList<Main_Data>();
		String data;
		String sTag;
		public Main_ParseAsync(){
		}
			@Override
			protected String doInBackground(String... params) {
				 try{
					 pref = getSharedPreferences(context.getString(R.string.txt_main_activity36), Activity.MODE_PRIVATE);
					 category_which = pref.getInt("category_which", category_which);
//					 Log.i("dsu", "category_which : " + category_which);
					 if(category_which == 0){
						 data = Crypto.decrypt(Utils.data, context.getString(R.string.txt_str0));
						 Log.i("dsu", "data : " + data);
					 }else if(category_which == 1){
						 data = Crypto.decrypt(Utils.data, context.getString(R.string.txt_str1));
					 }else if(category_which == 2){
						 data = Crypto.decrypt(Utils.data, context.getString(R.string.txt_str2));
					 }else if(category_which == 3){
						 data = Crypto.decrypt(Utils.data, context.getString(R.string.txt_str3));
					 }else if(category_which == 4){
						 data = Crypto.decrypt(Utils.data, context.getString(R.string.txt_str4));
					 }else if(category_which == 5){
						 data = Crypto.decrypt(Utils.data, context.getString(R.string.txt_str5));
					 }else if(category_which == 6){
						 data = Crypto.decrypt(Utils.data, context.getString(R.string.txt_str6));
					 }else if(category_which == 7){
						 data = Crypto.decrypt(Utils.data, context.getString(R.string.txt_str7));
					 }
			         HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(data).openConnection();
			         HttpURLConnection.setFollowRedirects(true);
			         localHttpURLConnection.setConnectTimeout(15000);
			         localHttpURLConnection.setReadTimeout(15000);
			         localHttpURLConnection.setRequestMethod("GET");
			         localHttpURLConnection.connect();
			         InputStream inputStream = new URL(data).openStream(); //open Stream�� ����Ͽ� InputStream�� �����մϴ�.
			         XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
			         XmlPullParser xpp = factory.newPullParser();
			         xpp.setInput(inputStream, "EUC-KR"); //euc-kr�� �� �����մϴ�. utf-8�� �ϴϱ� ����������
			         int eventType = xpp.getEventType();
			         while (eventType != XmlPullParser.END_DOCUMENT) {
				        	if (eventType == XmlPullParser.START_DOCUMENT) {
				        	}else if (eventType == XmlPullParser.END_DOCUMENT) {
				        	}else if (eventType == XmlPullParser.START_TAG){
				        		sTag = xpp.getName();
				        		if(sTag.equals("Album")){
				        			main_data = new Main_Data();
				        			Response = xpp.getAttributeValue(null, "id") + "";
				            	}else if(sTag.equals("num")){
				            		num = xpp.nextText()+"";
				            	}else if(sTag.equals("subject")){
				            		subject = xpp.nextText()+"";
				            	}else if(sTag.equals("thumb")){
				            		thumb = xpp.nextText()+"";
				            	}else if(sTag.equals("portal")){
				            		portal = xpp.nextText()+"";
				            	}
				        	} else if (eventType == XmlPullParser.END_TAG){
				            	sTag = xpp.getName();
				            	if(sTag.equals("Album")){
				            		main_data.id = Response;
				            		main_data.num = num;
				            		main_data.subject = subject;
				            		main_data.thumb = thumb;
				            		main_data.portal = portal;
				            		if(searchKeyword != null && "".equals(searchKeyword.trim()) == false){
				            			KoreanTextMatcher matcher1 = new KoreanTextMatcher(searchKeyword.toLowerCase());
				            			KoreanTextMatcher matcher2 = new KoreanTextMatcher(searchKeyword.toUpperCase());
				            			match1 = matcher1.match(main_data.subject.toLowerCase());
				            			match2 = matcher2.match(main_data.subject.toUpperCase());
				            			if (match1.success()) {
				            				list.add(main_data);
				            			}else if (match2.success()) {
				            				list.add(main_data);
				            			}
				            		}else{
				            			list.add(main_data);
				            		}
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
						for(int i=0;; i++){
							if(i >= list.size()){
//							while (i > list.size()-1){
								main_adapter = new MainAdapter(context, menuItems, listview_main);
								listview_main.setAdapter(main_adapter);
								listview_main.setFocusable(true);
								listview_main.setSelected(true);
//								Log.e("dsu", "list_count : " + listview_main.getCount());
								if(listview_main.getCount() == 0){
									layout_nodata.setVisibility(View.VISIBLE);
								}else{
									layout_nodata.setVisibility(View.GONE);	
								}
								return;
							}
							menuItems.add(list.get(i));
						}
					}else{
						layout_nodata.setVisibility(View.VISIBLE);
						Retry_AlertShow(context.getString(R.string.txt_main_activity3));
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
//					context.getString(R.string.txt_channel_title8)
			};
			settings = getSharedPreferences(context.getString(R.string.txt_main_activity36), MODE_PRIVATE);
			edit = settings.edit();
			pref = getSharedPreferences(context.getString(R.string.txt_main_activity36), Activity.MODE_PRIVATE);
			category_which = pref.getInt("category_which", category_which);
			
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
						
						edit_searcher.setText("");
						list = new ArrayList<Main_Data>();
						list.clear();
						bt_category.setText(channel_title[0]);
						edit.putInt("category_which", 0);
					}else if(which == 1){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						edit_searcher.setText("");
						list = new ArrayList<Main_Data>();
						list.clear();
						bt_category.setText(channel_title[1]);
						edit.putInt("category_which", 1);
					}else if(which == 2){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						edit_searcher.setText("");
						list = new ArrayList<Main_Data>();
						list.clear();
						bt_category.setText(channel_title[2]);
						edit.putInt("category_which", 2);
					}else if(which == 3){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						edit_searcher.setText("");
						list = new ArrayList<Main_Data>();
						list.clear();
						bt_category.setText(channel_title[3]);
						edit.putInt("category_which", 3);
					}else if(which == 4){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						edit_searcher.setText("");
						list = new ArrayList<Main_Data>();
						list.clear();
						bt_category.setText(channel_title[4]);
						edit.putInt("category_which", 4);
					}else if(which == 5){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						edit_searcher.setText("");
						list = new ArrayList<Main_Data>();
						list.clear();
						bt_category.setText(channel_title[5]);
						edit.putInt("category_which", 5);
					}else if(which == 6){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						edit_searcher.setText("");
						list = new ArrayList<Main_Data>();
						list.clear();
						bt_category.setText(channel_title[6]);
						edit.putInt("category_which", 6);
					}else if(which == 7){
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						edit_searcher.setText("");
						list = new ArrayList<Main_Data>();
						list.clear();
						bt_category.setText(channel_title[7]);
						edit.putInt("category_which", 7);
					}else if(which == 8){
						Intent intent = new Intent(context, FavoriteActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
					edit.commit();
					dialog.dismiss();
					displaylist();
				}
			}).show();
			
		}else if(view == bt_search_result){
			pref = getSharedPreferences(context.getString(R.string.txt_main_activity36), Activity.MODE_PRIVATE);
			category_which = pref.getInt("category_which", category_which);
			if(category_which != 0){
				alertDialog = new AlertDialog.Builder(this)
			    .setTitle(context.getString(R.string.txt_alert_search_ment1))
				.setIcon(R.drawable.bt_search_on)
				.setMessage(context.getString(R.string.txt_alert_search_ment2))
				.setPositiveButton(context.getString(R.string.txt_alert_search_button_yes), new DialogInterface.OnClickListener() {				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						current_position = 0;
						start_index = 1;
						loadingMore = true;
						exeFlag = false;
						
						settings = getSharedPreferences(context.getString(R.string.txt_main_activity36), MODE_PRIVATE);
						edit = settings.edit();
						edit.putInt("category_which", 0);
						edit.commit();
						
						bt_category.setText(context.getString(R.string.txt_channel_title0));
						list = new ArrayList<Main_Data>();
						list.clear();
//						edit_searcher.setText("");
						
						
						displaylist();
						
						InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
			    		inputMethodManager.hideSoftInputFromWindow(edit_searcher.getWindowToken(), 0);
					}
				}).show();
			}
			else{
				String search_text = edit_searcher.getText().toString();
				if ((search_text != null) && (search_text.length() > 0)){
					InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
		    		inputMethodManager.hideSoftInputFromWindow(edit_searcher.getWindowToken(), 0);
					
		    		list = new ArrayList<Main_Data>();
					list.clear();
					displaylist();
				}else{
					Toast.makeText(context, context.getString(R.string.txt_search_empty), Toast.LENGTH_SHORT).show();
				}
			}
		}else if(view == bt_home){
			current_position = 0;
			start_index = 1;
			loadingMore = true;
			exeFlag = false;
			
			settings = getSharedPreferences(context.getString(R.string.txt_main_activity36), MODE_PRIVATE);
			edit = settings.edit();
			edit.putInt("category_which", 0);
			edit.commit();
			bt_category.setText(context.getString(R.string.txt_channel_title0));
			edit_searcher.setText("");
			list = new ArrayList<Main_Data>();
			list.clear();
			
			displaylist();
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
		}else if(view == bt_bug_send){
			Intent intent = new Intent(this, annie.kiz.view.technotown.favorite.MainActivity.class);
			intent.putExtra("num", "");
			intent.putExtra("subject", "");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		Main_Data main_data = (Main_Data)main_adapter.getItem(position);
		String id = main_data.id;
		String num = main_data.num;
		String subject = main_data.subject;
		String thumb = main_data.thumb;
		String portal = main_data.portal;
		
		Intent intent = new Intent(context, SubActivity.class);
		intent.putExtra("num", num);
		intent.putExtra("subject", subject);
		intent.putExtra("thumb", thumb);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
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
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setInverseBackgroundForced(true);
		builder.setNeutralButton(context.getString(R.string.txt_main_activity14), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				 current_position = 0;
            	 loadingMore = true;
            	 exeFlag = false;
            	 main_parseAsync = new Main_ParseAsync();
            	 main_parseAsync.execute();
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if(keyCode == KeyEvent.KEYCODE_BACK){
			 if(!flag){
				 Toast.makeText(context, context.getString(R.string.txt_main_activity6) , Toast.LENGTH_LONG).show();
				 flag = true;
				 handler.sendEmptyMessageDelayed(0, 2000);
				 return false;
			 }else{
				 handler.postDelayed(new Runnable() {
					 @Override
					 public void run() {
						 PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, true);
						 finish();
					 }
				 },0);
			 }
			 return false;	 
		 }
		return super.onKeyDown(keyCode, event);
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
	//** CustomPopup �̺�Ʈ�� *************
	@Override
	public void onCloseCustomPopup(String arg0) {
	}

	@Override
	public void onHasNoCustomPopup() {
	}

	@Override
	public void onShowCustomPopup(String arg0) {
	}

	@Override
	public void onStartedCustomPopup() {
	}

	@Override
	public void onWillCloseCustomPopup(String arg0) {
	}

	@Override
	public void onWillShowCustomPopup(String arg0) {
	}

	@Override
	public void onInterstitialAdClosed(InterstitialAd arg0) {
		interstialAd = null;
	}

	@Override
	public void onInterstitialAdFailedToReceive(int arg0, String arg1,
			InterstitialAd arg2) {
		
		interstialAd = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setCancelable(false);
		builder.setTitle(context.getString(R.string.app_name));
		builder.setMessage(context.getString(R.string.txt_finish_ment));
		builder.setInverseBackgroundForced(true);
		builder.setNeutralButton(context.getString(R.string.txt_finish_yes), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				finish();
			}
		});
		builder.setNegativeButton(context.getString(R.string.txt_finish_no), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
             	 dialog.dismiss();
			}
		});
		AlertDialog myAlertDialog = builder.create();
		if(retry_alert) myAlertDialog.show();
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
		finish();
	}
}
