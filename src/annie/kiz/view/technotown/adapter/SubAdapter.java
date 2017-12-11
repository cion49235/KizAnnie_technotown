package annie.kiz.view.technotown.adapter;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import annie.kiz.view.technotown.R;
import annie.kiz.view.technotown.activity.SubActivity;
import annie.kiz.view.technotown.data.Sub_Data;
import annie.kiz.view.technotown.util.ImageLoader;
import annie.kiz.view.technotown.util.RoundedTransform;


public class SubAdapter extends BaseAdapter{
	public Context context;
	public ImageLoader imgLoader;
	public int _id = -1;
	public String id = "empty";
	public ArrayList<Sub_Data> list;
	public ListView listview_main;
	public SubAdapter(Context context, ArrayList<Sub_Data> list, ListView listview_main) {
		this.imgLoader = new ImageLoader(context.getApplicationContext());
		this.context = context;
		this.list = list;
		this.listview_main = listview_main;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		try{
			if(view == null){	
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				view = layoutInflater.inflate(R.layout.sub_activity_listrow, parent, false); 
			}
			ImageView img_imageurl = (ImageView)view.findViewById(R.id.img_imageurl);
			img_imageurl.setFocusable(false);
			String image_url = list.get(position).thumb;
			Picasso.with(context)
		    .load(image_url)
		    .placeholder(R.drawable.no_image)
		    .transform(new RoundedTransform())
		    .error(R.drawable.no_image)
		    .into(img_imageurl);
			
			Button bt_bug_send = (Button)view.findViewById(R.id.bt_bug_send);
			bt_bug_send.setFocusable(false);
			bt_bug_send.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(context, annie.kiz.view.technotown.favorite.MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("num", SubActivity.num);
					intent.putExtra("subject", SubActivity.subject + ":"+list.get(position).subject);
					context.startActivity(intent);
				}
			});

			TextView txt_subject = (TextView)view.findViewById(R.id.txt_subject);
			txt_subject.setText(list.get(position).subject);
			
			if(listview_main.isItemChecked(position)){
				view.setBackgroundColor(Color.parseColor("#e3e3e3"));
				txt_subject.setTextColor(Color.parseColor("#000000"));
			}else{
				view.setBackgroundColor(Color.parseColor("#00000000"));
				txt_subject.setTextColor(Color.parseColor("#000000"));
			}
		}catch (Exception e) {
		}
		return view;
	}
}