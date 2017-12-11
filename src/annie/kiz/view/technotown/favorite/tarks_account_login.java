//This is source code of favorite. Copyrightⓒ. Tarks. All Rights Reserved.
package annie.kiz.view.technotown.favorite;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import annie.kiz.view.technotown.R;
import annie.kiz.view.technotown.favorite.connect.AsyncHttpTask;
import annie.kiz.view.technotown.favorite.global.Global;
import annie.kiz.view.technotown.favorite.global.Globalvariable;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class tarks_account_login extends SherlockActivity {
	Button bt;
	Button bt2;
	String myId, myPWord, myTitle, mySubject;
	String myResult = null;
	EditText edit1, edit2;
	String s1, s2;
//	boolean okbutton = true;
	boolean show_alert = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Can use progress
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.favorite_tarks_account);
		setSupportProgressBarIndeterminateVisibility(false);
		show_alert = true;
		// define edittext
		edit1 = (EditText) findViewById(R.id.editText1);
		edit2 = (EditText) findViewById(R.id.editText2);

		bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri
						.parse("https://tarks.net/index.php?mid=main&act=dispMemberFindAccount");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		bt2 = (Button) findViewById(R.id.button2);
		bt2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri
						.parse("https://tarks.net/index.php?mid=main&act=dispMemberSignUpForm");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		show_alert = false;
	}
	
	public void ConnectionError(){
		if(show_alert) Global.ConnectionError(this);
	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			//Stop progressbar
			setSupportProgressBarIndeterminateVisibility(false);
			
			if (msg.what == -1) {
				ConnectionError();
				
			}

			if (msg.what == 1) {
				myResult = msg.obj.toString();
				if (myResult.matches("")) {
					// Error Login
					AlertDialog.Builder builder1 = new AlertDialog.Builder(
							tarks_account_login.this);
					builder1.setMessage(getString(R.string.error_login))
							.setPositiveButton(getString(R.string.yes), null)
							.setTitle(getString(R.string.error));
					builder1.show();
				} else {
					// Save auth key to temp

					// Intent 생성
					Intent intent = new Intent();
					// 생성한 Intent에 데이터 입력
					intent.putExtra("id", edit1.getText().toString());
					intent.putExtra("auth_code", myResult);
					// 결과값 설정(결과 코드, 인텐트)
					tarks_account_login.this.setResult(RESULT_OK, intent);
					// 본 Activity 종료
					finish();
				}

			}

		}
	};

	public void TarksAccountLogin() throws NoSuchAlgorithmException {
		// Set Progress
		setSupportProgressBarIndeterminateVisibility(true);

		// import EditText string

		String s1 = edit1.getText().toString();
		String s2 = edit2.getText().toString();

		// md5 password value
		String src = s2;
		String enc = Global.getMD5Hash(src);
		
	//	Log.i("password", enc);

		ArrayList<String> Paramname = new ArrayList<String>();
		Paramname.add("authcode");
		Paramname.add("id");
		Paramname.add("password");

		ArrayList<String> Paramvalue = new ArrayList<String>();
		Paramvalue.add("642979");
		Paramvalue.add(s1);
		Paramvalue.add(enc);

		new AsyncHttpTask(this, getString(R.string.server_path)
				+ "member/tarks_account_check.php", mHandler, Paramname,
				Paramvalue, null, 1,0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// 메뉴 버튼 구현부분
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.accept, menu);
		return true;

	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.yes:
			// Check okbutton
			if (Globalvariable.okbutton == true) {
				Globalvariable.okbutton = false;
				edit1 = (EditText) findViewById(R.id.editText1);
				s1 = edit1.getText().toString();

				// no err
				try {
					// import EditText

					// edit2 = (EditText) findViewById(R.id.editText2);
					// String s2 = edit2.getText().toString();

					if (s1.matches("")) {
						// Show type id noti
						Global.Infoalert(this,
								getString(R.string.notification),
								getString(R.string.type_id),
								getString(R.string.yes));
					} else {
						// TODO Auto-generated method stub
						TarksAccountLogin();
					}
				} catch (Exception e) {
					// Log.i("ERROR", "App has been error");
					// System.out.println();
					// Not Connected To Internet
					ConnectionError();

				}
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
