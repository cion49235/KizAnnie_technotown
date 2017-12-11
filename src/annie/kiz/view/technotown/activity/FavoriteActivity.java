package annie.kiz.view.technotown.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import annie.kiz.view.technotown.R;
import annie.kiz.view.technotown.data.Favorite_DBopenHelper;
import annie.kiz.view.technotown.data.Favorite_Data;
import annie.kiz.view.technotown.util.ImageLoader;
import annie.kiz.view.technotown.util.RoundedTransform;

import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdViewListener;
import com.admixer.InterstitialAd;
import com.admixer.InterstitialAdListener;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

public class FavoriteActivity extends Activity implements OnItemClickListener, OnClickListener, AdViewListener, InterstitialAdListener{
	public Favorite_DBopenHelper favorite_mydb;
	public SQLiteDatabase mdb;
	public Cursor cursor;
	public Context context;
	public ConnectivityManager connectivityManger;
	public NetworkInfo mobile;
	public NetworkInfo wifi;
	public static GridView listview_favorite;
	public FavoriteAdapter<Favorite_Data> adapter;
	public static LinearLayout layout_listview_favorite, layout_nodata;
	public int SDK_INT = android.os.Build.VERSION.SDK_INT;
	public static RelativeLayout ad_layout;
	public static TextView txt_favorite_title;
	public static LinearLayout action_layout;
	public static Button bt_home, bt_plus_coin;
	private AdView adView;
	public SharedPreferences settings,pref;
	public Editor edit;
	public static AlertDialog alertDialog;
	public static com.admixer.InterstitialAd interstialAd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_activity);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		context = this;
		
		pref = getSharedPreferences(context.getString(R.string.txt_main_activity36), Activity.MODE_PRIVATE);
		MainActivity.coin = pref.getInt("coin", MainActivity.coin);
		
		txt_favorite_title = (TextView)findViewById(R.id.txt_favorite_title);
		txt_favorite_title.setText(context.getString(R.string.txt_favorite_activity4));
		
//		adView = new AdView(this);
//	    adView.setAdUnitId(context.getString(R.string.banner_ad_unit_id));
//	    adView.setAdSize(AdSize.BANNER);
//	    
//	    RelativeLayout layout = (RelativeLayout)findViewById(R.id.ad_layout);
//	    layout.addView(adView);
//	    AdRequest adRequest = new AdRequest.Builder().build();
//	    adView.loadAd(adRequest);
		
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "0pv7we6d");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/6752137777");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/7873647755");
		
		addBannerView();
		
		layout_listview_favorite = (LinearLayout)findViewById(R.id.layout_listview_favorite);
		layout_nodata = (LinearLayout)findViewById(R.id.layout_nodata);
		action_layout = (LinearLayout)findViewById(R.id.action_layout);
		bt_plus_coin = (Button)findViewById(R.id.bt_plus_coin);
		bt_home = (Button)findViewById(R.id.bt_home);
		bt_home.setOnClickListener(this);
		bt_plus_coin.setOnClickListener(this);
		displayList();
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
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(favorite_mydb != null){
			favorite_mydb.close();
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
//		Log.i("dsu", "onRestart");
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
	
	public void displayList(){
		List<Favorite_Data>contactsList = getContactsList();
		adapter = new FavoriteAdapter<Favorite_Data>(
    			context, R.layout.favorite_activity_listrow, contactsList);
		listview_favorite = (GridView)findViewById(R.id.listview_favorite);
		listview_favorite.setAdapter(adapter);
		if (SDK_INT >= Build.VERSION_CODES.HONEYCOMB){ 
			listview_favorite.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    	}
		listview_favorite.setOnItemClickListener(this);
		listview_favorite.setFastScrollEnabled(false);
		listview_favorite.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		if(listview_favorite.getCount() == 0){
			layout_nodata.setVisibility(View.VISIBLE);
			layout_listview_favorite.setVisibility(View.GONE);
		}else{
			layout_nodata.setVisibility(View.GONE);
			layout_listview_favorite.setVisibility(View.VISIBLE);
		}
	}
	
	public List<Favorite_Data> getContactsList() {
		List<Favorite_Data>contactsList = new ArrayList<Favorite_Data>();
		try{
			favorite_mydb = new Favorite_DBopenHelper(context);
			mdb = favorite_mydb.getWritableDatabase();
	        cursor = mdb.rawQuery("select * from favorite_list order by _id desc", null);
	        while (cursor.moveToNext()){
				addContact(contactsList,cursor.getInt(0), cursor.getString(1), cursor.getString(2), 
						cursor.getString(3),cursor.getString(4), cursor.getString(5));
	        }
		}catch (Exception e) {
		}finally{
			if(favorite_mydb != null){
				favorite_mydb.close();
			}
		}
		return contactsList;
	}
	
	public void addContact(List<Favorite_Data> contactsList, int _id, String id,String num,String subject,String thumb,String portal){
		contactsList.add(new Favorite_Data(_id, id, num, subject, thumb, portal));
	}
	
	@Override
	public void onClick(View view) {
		if(view == bt_home){
			onBackPressed();
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
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		Favorite_Data favorite_data = (Favorite_Data)adapter.getItem(position);
		String id = favorite_data.getId();
		String num = favorite_data.getNum();
		String subject =favorite_data.getSubject();
		String thumb = favorite_data.getThumb();
		String portal = favorite_data.getPortal();
		
		Intent intent = new Intent(context, SubActivity.class);
		intent.putExtra("num", num);
		intent.putExtra("subject", subject);
		intent.putExtra("thumb", thumb);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}
	
	public class FavoriteAdapter<T extends Favorite_Data>extends ArrayAdapter<T>{
		public List<T> contactsList;
		ImageLoader imgLoader = new ImageLoader(getApplicationContext());
		public Button bt_favorite_delete;
		public String num = "empty";
		public FavoriteAdapter(Context context, int textViewResourceId, List<T> items) {
			super(context, textViewResourceId, items);
			contactsList = items;
		}
		@Override
		public View getView(final int position, View view, ViewGroup parent) {
			try{
				if(view == null){
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					view = vi.inflate(R.layout.favorite_activity_listrow, null);
				}
				final T contacts = contactsList.get(position);
				TextView txt_favorite_subject = (TextView)view.findViewById(R.id.txt_favorite_subject);
				txt_favorite_subject.setText(contacts.getSubject());
				
				ImageView img_favorite_imageurl = (ImageView)view.findViewById(R.id.img_favorite_imageurl);
				img_favorite_imageurl.setFocusable(false);
				String image_url = contacts.getThumb();
				
				Picasso.with(context)
	            .load(image_url)
	            .placeholder(R.drawable.fanroom_list_thumbnail_001)
	            .error(R.drawable.fanroom_list_thumbnail_001)
	            .into(img_favorite_imageurl);
				
				bt_favorite_delete = (Button)view.findViewById(R.id.bt_favorite_delete);
				bt_favorite_delete.setFocusable(false);
				bt_favorite_delete.setSelected(false);
				bt_favorite_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						favorite_mydb.getWritableDatabase().delete("favorite_list", "_id" + "=" +contacts.get_id(), null);
						displayList();
						Toast.makeText(context, context.getString(R.string.txt_favorite_activity1), Toast.LENGTH_SHORT).show();
					}
				});
			}catch (Exception e) {
			}finally{
				if(favorite_mydb != null){
					favorite_mydb.close();
				}
			}
			return view;
		}
	}

	public void AlertShow(String msg) {
        AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
                 this);
         alert_internet_status.setTitle(context.getString(R.string.txt_favorite_activity2));
         alert_internet_status.setCancelable(false);
         alert_internet_status.setMessage(msg);
         alert_internet_status.setPositiveButton(context.getString(R.string.txt_favorite_activity3),
                 new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss(); // �ݱ�
                         finish();
                     }
                 });
         alert_internet_status.show();
     }
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

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