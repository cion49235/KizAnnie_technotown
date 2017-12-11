//This is source code of favorite. Copyright?? Tarks. All Rights Reserved.
package annie.kiz.view.technotown.favorite.page;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import annie.kiz.view.technotown.R;
import annie.kiz.view.technotown.favorite.connect.AsyncHttpTask;
import annie.kiz.view.technotown.favorite.connect.ImageDownloader;
import annie.kiz.view.technotown.favorite.fadingactionbar.extras.actionbarsherlock.FadingActionBarHelper;
import annie.kiz.view.technotown.favorite.global.Global;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.admixer.AdAdapter;
import com.admixer.AdInfo;
import com.admixer.AdMixerManager;
import com.admixer.AdView;
import com.admixer.AdViewListener;

public class ProfileActivity extends SherlockActivity implements AdViewListener {

	// Profile image local path
	String local_path;
	// User name
	String title;
	// Member srl
	String member_srl = "0";
	int write_status;
	int your_status;
	int me_status;
	// String profile_user_srl = "0";
	// Profile
	ImageView profile;

	// ListView
	ListView listView;
	// FadingActionbar
	FadingActionBarHelper helper;
	// Menu state
	boolean add_menu_state = false;
	// List
	ArrayList<List> m_orders = new ArrayList<List>();
	// Define ListAdapter
	ListAdapter m_adapter;
	// Menu
	private Menu optionsMenu;
	private String[] member;
	public static RelativeLayout ad_layout;
	boolean show_alert = false;
	public Handler handler = new Handler();
	String num;
	String subject;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Can use progress
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		show_alert = true;
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "0pv7we6d");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/6752137777");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/7873647755");
		// Get Intent
		Intent intent = getIntent();// ?�텐??받아?�고
		member_srl = intent.getStringExtra("member_srl");
        Log.i("member_srl", member_srl);
        try{
			num = getIntent().getStringExtra("num");
			subject = getIntent().getStringExtra("subject");
		}catch(NullPointerException e){
		}
		load();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		show_alert = false;
	}
	
	public void onConfigurationChanged(android.content.res.Configuration newConfig) {
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	    }else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	    }
	    super.onConfigurationChanged(newConfig);
	};
	
	public void addBannerView() {
    	AdInfo adInfo = new AdInfo("0pv7we6d");
    	adInfo.setTestMode(false);
        AdView adView = new AdView(this);
        adView.setAdInfo(adInfo, this);
        adView.setAdViewListener(this);
        ad_layout = (RelativeLayout)findViewById(R.id.ad_layout);
        if(ad_layout != null){
        	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ad_layout.addView(adView, params);	
        }
    }
	

	public void load() {
		setFadingActionBar();
		// local_path = getCacheDir().toString()
		// + "/member/";
		try {
			local_path = getCacheDir().toString() + "/member/";
		} catch (Exception e) {
		}
		profile = (ImageView) findViewById(R.id.image_header);
		try {
			profile.setImageDrawable(Drawable.createFromPath(local_path
					+ member_srl + ".jpg"));
		} catch (Exception e) {
		}
		getProfileInfo();
		setListAdapter();
		getDocList("0");
	}

	public void getProfileInfo() {
		// Start Progressbar
		setSupportProgressBarIndeterminateVisibility(true);

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("profile_user_srl");
		Paramname.add("member_info");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(String.valueOf(member_srl));
		Paramvalue
				.add("tarks_account//write_status//name_1//name_2//gender//birthday//join_day//profile_pic//profile_update//lang//country");

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "member/profile_info.php", mHandler, Paramname, Paramvalue,
				null, 1, 0);
	}

	public void setFadingActionBar() {
		helper = new FadingActionBarHelper()
				.actionBarBackground(R.drawable.ab_background)
				.headerLayout(R.layout.favorite_profile)
				.contentLayout(R.layout.favorite_activity_listview);
		setContentView(helper.createView(this));
		helper.initActionBar(this);
		helper.initContext(this);
		
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "0pv7we6d");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB, "ca-app-pub-4637651494513698/6752137777");
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMOB_FULL, "ca-app-pub-4637651494513698/7873647755");
		
		addBannerView();
	}

	public void setListAdapter() {
		// profile = (ImageView) findViewById(R.id.image_header);
		listView = (ListView) findViewById(android.R.id.list);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg0.getAdapter() instanceof HeaderViewListAdapter) {
					if (((HeaderViewListAdapter) arg0.getAdapter())
							.getWrappedAdapter() instanceof ListAdapter) {
						ListAdapter ca = (ListAdapter) ((HeaderViewListAdapter) arg0
								.getAdapter()).getWrappedAdapter();

						List ls = (List) ca.getItem(arg2 - 1);

						Intent intent = new Intent(ProfileActivity.this, document_read.class);
						intent.putExtra("doc_srl", String.valueOf(ls.getDocSrl()));
						
						startActivityForResult(intent, 1);
					}
				}
			}
		});
		// listView.setOnScrollListener(this);
		m_adapter = new ListAdapter(this, R.layout.favorite_profile_list, m_orders);

		// Set Profile
		profile.setImageDrawable(Drawable.createFromPath(local_path
				+ member_srl + ".jpg"));

		listView.setAdapter(m_adapter);

	}

	public void setList(int doc_srl, String user_srl, String name,
			String contents, int status) {

		// Get Profile
		// getMemberInfo(user_srl);

		List p1 = new List(user_srl, name, contents, 1, doc_srl, status);
		m_orders.add(p1);

		// ListView listview = (ListView) findViewById(R.id.listView1);

	}

	public void getDocList(String startdoc) {
		// Start Progressbar
		setSupportProgressBarIndeterminateVisibility(true);

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("kind");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("doc_user_srl");
		Paramname.add("start_doc");
		Paramname.add("doc_number");
		Paramname.add("doc_info");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("0");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(member_srl);
		Paramvalue.add(startdoc);
		Paramvalue.add("15");
		Paramvalue.add("srl//user_srl//name//content//status");

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "board/documents_app_read.php", mHandler, Paramname,
				Paramvalue, null, 3, 0);
	}

	public void MoreLoad(String number) {
		//Log.i("ListView", listView.getLastVisiblePosition() + "");
		//Log.i("ListViewCount", listView.getCount() + "");
		if (listView.getLastVisiblePosition() >= listView.getCount() - 1 && listView.getChildAt(0).getTop() != 0) {
			getDocList(number);
		}
	}

	public void getMemberInfo(String user_srl) {
		if (Global.getUpdatePossible(user_srl)) {
			//Log.i("Update", "Updateing");
			ArrayList<String> Paramname = new ArrayList<String>();
			Paramname.add("authcode");
			Paramname.add("user_srl");
			Paramname.add("user_srl_auth");
			Paramname.add("profile_user_srl");
			Paramname.add("member_info");

			ArrayList<String> Paramvalue = new ArrayList<String>();
			Paramvalue.add("642979");
			Paramvalue.add(Global.getSetting("user_srl",
					Global.getSetting("user_srl", "0")));
			Paramvalue.add(Global.getSetting("user_srl_auth",
					Global.getSetting("user_srl_auth", "null")));
			Paramvalue.add(String.valueOf(user_srl));
			Paramvalue.add("profile_pic//profile_update");

			new AsyncHttpTask(this, getString(R.string.server_path)
					+ "member/profile_info.php", mHandler, Paramname,
					Paramvalue, null, 4, Integer.parseInt(user_srl));

		}
	}

	public void addFavorite(String user_srl) {

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("category");
		Paramname.add("user_srl");
		Paramname.add("user_srl_auth");
		Paramname.add("value");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add("3");
		Paramvalue.add(Global.getSetting("user_srl",
				Global.getSetting("user_srl", "0")));
		Paramvalue.add(Global.getSetting("user_srl_auth",
				Global.getSetting("user_srl_auth", "null")));
		Paramvalue.add(user_srl);

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "favorite/favorite_app_add.php", mHandler, Paramname,
				Paramvalue, null, 6, Integer.parseInt(user_srl));

	}

	/**
	 * @return A list of Strings read from the specified resource
	 */
	private ArrayList<String> loadItems(int rawResourceId) {
		try {
			ArrayList<String> countries = new ArrayList<String>();
			InputStream inputStream = getResources().openRawResource(
					rawResourceId);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line;
			while ((line = reader.readLine()) != null) {
				countries.add(line);
			}
			reader.close();
			return countries;
		} catch (IOException e) {
			return null;
		}
	}

	public void ProfileImageDownload() {
		// Start Progressbar
		setSupportProgressBarIndeterminateVisibility(true);
		new ImageDownloader(this, getString(R.string.server_path)
				+ "files/profile/" + member_srl + ".jpg", mHandler, 2, 0);
	}

	public void ProfileUserImageDownload(String user_srl) {
		// Start Progressbar
		setSupportProgressBarIndeterminateVisibility(true);
		new ImageDownloader(this, getString(R.string.server_path)
				+ "files/profile/thumbnail/" + user_srl + ".jpg", mHandler, 5,
				Integer.parseInt(user_srl));
	}

	public void MemberInfoError() {
		Global.Infoalert(this, getString(R.string.error),
				getString(R.string.member_info_error_des),
				getString(R.string.yes));
	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			setSupportProgressBarIndeterminateVisibility(false);
			// IF Sucessfull no timeout

			if (msg.what == -1) {
				if(show_alert) Global.ConnectionError(ProfileActivity.this);
			}

			if (msg.what == 1) {

				try {
					String[] array = msg.obj.toString().split("/LINE/.");
					// Global.dumpArray(array);
					String tarks_account = array[0];
					write_status = Integer.parseInt(array[1]);
					String name_1 = array[2];
					String name_2 = array[3];
					String gender = array[4];
					String birthday = array[5];
					String join_day = array[6];
					String profile_pic = array[7];
					String profile_update = array[8];
					String lang = array[9];
					String country = array[10];
					your_status = Integer.parseInt(array[11]);
					me_status = Integer.parseInt(array[12]);

					title = Global.NameMaker(lang, name_1, name_2);

					getSupportActionBar().setTitle(title);

					if (Global.UpdateMemberFileCache(member_srl,
							profile_update, profile_pic)) {
						Global.SaveUserSetting(member_srl, profile_update,
								null, profile_pic);
						ProfileImageDownload();
						// Log.i("test", "Let s profile image download");

					}
					if (profile_pic.matches("N")) {
						File file = new File(local_path + member_srl + ".jpg");
						file.delete();
						profile.setImageDrawable(null);
					}

					if (me_status < 3) {
						add_menu_state = true;
					}
					invalidateOptionsMenu();
				} catch (Exception e) {
					MemberInfoError();

				}
			}

			if (msg.what == 2) {
				// Save File cache
				try {
					Global.SaveBitmapToFileCache((Bitmap) msg.obj, local_path,
							member_srl + ".jpg");

					// Set Profile
					profile.setImageDrawable(Drawable.createFromPath(local_path
							+ member_srl + ".jpg"));
					// Refresh();
				} catch (Exception e) {
				}
			}

			if (msg.what == 3) {
				try {
					//Log.i("Doc", msg.obj.toString());
					String[] doc = msg.obj.toString().split("/DOC/.");

					for (int i = 0; i < doc.length; i++) {
						String[] array = doc[i].split("/LINE/.");
						// Global.dumpArray(array);
						String srl = array[0];
						String user_srl = array[1];
						String name = array[2];
						String content = array[3];
						String status = array[4];

						//Log.i("user", user_srl);
						getMemberInfo(user_srl);
						setList(Integer.parseInt(srl), user_srl, name, content, Integer.parseInt(status));
						m_adapter.notifyDataSetChanged();
					}
				} catch (Exception e) {

				}
			}

			if (msg.what == 4) {

				try {

					String[] array = msg.obj.toString().split("/LINE/.");
					// Global.dumpArray(array);
					String profile_pic = array[0];
					String profile_update = array[1];

					String user_srl = String.valueOf(msg.arg1);
					Global.SaveUserSetting(user_srl, null,
							String.valueOf(Global.getCurrentTimeStamp()),
							profile_pic);

					if (profile_pic.matches("Y")) {
						// Global.SaveUserSetting(user_srl, profile_update);
						ProfileUserImageDownload(user_srl);
						// Log.i("test", "Let s profile image download");

					}
					if (profile_pic.matches("N")) {
						File file = new File(local_path + user_srl + ".jpg");
						file.delete();
						File file_thum = new File(local_path + "thumbnail/"
								+ user_srl + ".jpg");
						file_thum.delete();
					}
				} catch (Exception e) {
					// MemberInfoError();

				}
			}

			if (msg.what == 5) {
				// Save File cache
				try {
					//Log.i("Save", msg.arg1 + "");
					Global.SaveBitmapToFileCache((Bitmap) msg.obj, local_path
							+ "thumbnail/", msg.arg1 + ".jpg");

					m_adapter.notifyDataSetChanged();

					// Set Profile
					// profile.setImageDrawable(Drawable.createFromPath(local_path
					// + member_srl + ".jpg"));
					// Refresh();
				} catch (Exception e) {
				}
			}

			if (msg.what == 6) {
				String result = msg.obj.toString();
				if (result.matches("favorite_add_succeed")) {
					Global.toast(getString(R.string.added));
					add_menu_state = false;
					invalidateOptionsMenu();
				} else {
					if(show_alert) Global.ConnectionError(ProfileActivity.this);
				}
				// Log.i("Result","로그 ?�상 ?�동");
				//Log.i("Result", msg.obj.toString());

			}

		}
	};

	// public void Refresh() {
	//
	// setList();
	// }

	private class ListAdapter extends ArrayAdapter<List> {

		private ArrayList<List> items;

		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<List> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.favorite_profile_list, null);
			}
			final List p = items.get(position);
			if (p != null) {
				TextView tt = (TextView) v.findViewById(R.id.titre);
				TextView bt = (TextView) v.findViewById(R.id.description);
				ImageView image = (ImageView) v.findViewById(R.id.img);
				
				
				if (tt != null) {
					tt.setText(p.getTitle());
					
					//Status not public
					if(p.getStatus() > 1){
						tt.setTextColor(Color.GRAY);
					}
					
				}
				if (bt != null) {
					bt.setText(Global.getValue(p.getDes()));
				}
				if (image != null) {

					boolean state = Global.CheckFileState(local_path
							+ "thumbnail/" + p.getUserSrl() + ".jpg");

					if (state) {
						image.setImageDrawable(Drawable
								.createFromPath(local_path + "thumbnail/"
										+ p.getUserSrl() + ".jpg"));
					} else {
						image.setImageResource(R.drawable.person);
					}
					image.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
							intent.putExtra("member_srl", p.getUserSrl());
							startActivity(intent);
						}
					});
				}
			}
			return v;
		}
	}

	class List {
		private String user_srl;
		private String Title;
		private String Description;
		private int Tag;
		private int Doc_srl;
		private int status;

		public List(String _user_Srl, String _Title, String _Description,
				int _Tag, int _Doc_srl, int _status) {
			this.user_srl = _user_Srl;
			this.Title = _Title;
			this.Description = _Description;
			this.Tag = _Tag;
			this.Doc_srl = _Doc_srl;
			this.status = _status;
		}

		public String getUserSrl() {
			return user_srl;
		}

		public String getTitle() {
			return Title;
		}

		public String getDes() {
			return Description;
		}

		public int getTag() {
			return Tag;
		}

		public int getDocSrl() {
			return Doc_srl;
		}
		
		public int getStatus() {
			return status;
		}

	}

	// @Override
	// public void onScroll(AbsListView view, int firstVisibleItem,
	// int visibleItemCount, int totalItemCount)
	// {
	// helper.ScrollListener(activity);
	//
	// // ?�재 �?�� 처음??보이????��?��? 보여�?�� ??��?��? ?�한값이
	// // ?�체???�자???�일?��?�?�?�� ?�래�??�크�??�었?�고 �?��?�니??
	// int count = totalItemCount - visibleItemCount;
	//
	//
	//
	// if(firstVisibleItem >= count && totalItemCount != 0
	// && mLockListView == false)
	// {
	// Log.i("로그", "Loading next items");
	// addItems(totalItemCount);
	// }
	// }

	/**
	 * ?�의??방법?�로 ?��? ?�이?�을 추�??�니??
	 * 
	 * @param size
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		this.optionsMenu = menu;
		MenuItem item;
		menu.add(0, 0, 0, getString(R.string.add_favorite))
				.setIcon(R.drawable.add)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, 1, 0, getString(R.string.write)).setIcon(R.drawable.write)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, 2, 0, getString(R.string.information)).setShowAsAction(
				MenuItem.SHOW_AS_ACTION_NEVER);

		//No visible in favorite example
	//	menu.findItem(0).setVisible(add_menu_state);
		menu.findItem(0).setVisible(false);
		menu.findItem(1).setVisible(write_status <= your_status);
		// item = menu.add(0, 1, 0, R.string.Main_MenuAddBookmark);
		// item.setIcon(R.drawable.ic_menu_add_bookmark);

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			// setListAdapter();
			m_adapter.clear();
			load();
		}

	}

	// 빽백???�단?�션�?	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			// Alert
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ProfileActivity.this);
			builder.setMessage(getString(R.string.favorite_add_des)).setTitle(
					getString(R.string.add_favorite));
			builder.setPositiveButton(getString(R.string.yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							addFavorite(member_srl);
						}
					});
			builder.setNegativeButton(getString(R.string.no), null);
			builder.show();
			return true;
		case 1:
			Intent intent1 = new Intent(ProfileActivity.this, document_write.class);
			intent1.putExtra("page_srl", member_srl);
			intent1.putExtra("page_name", title);
			intent1.putExtra("num", num);
			intent1.putExtra("subject", subject);
			startActivityForResult(intent1, 1);
			return true;
		case 2:
			Intent intent2 = new Intent(ProfileActivity.this, ProfileInfo.class);
			intent2.putExtra("member_srl", member_srl);
			startActivityForResult(intent2, 1);
			return true;
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	//** BannerAd ?�벤?�들 *************
	@Override
	public void onClickedAd(String arg0, AdView arg1) {
	}

	@Override
	public void onFailedToReceiveAd(int arg0, String arg1, AdView arg2) {
		
	}

	@Override
	public void onReceivedAd(String arg0, AdView arg1) {
//		Log.i("dsu", "배너광고 : arg0 : " + arg0+"\n" + arg1) ;
	}
}
