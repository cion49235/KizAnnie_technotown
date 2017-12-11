package annie.kiz.view.technotown.adapter;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import annie.kiz.view.technotown.R;
import annie.kiz.view.technotown.activity.MainActivity;
import annie.kiz.view.technotown.data.Main_Data;
import annie.kiz.view.technotown.util.ImageLoader;
import annie.kiz.view.technotown.util.RoundedTransform;


public class MainAdapter extends BaseAdapter{
	public Context context;
	public ImageLoader imgLoader;
	public String num = "empty";
	public Cursor cursor;
	public Button bt_favorite;
	public ArrayList<Main_Data> list;
	public GridView listview_main;
	public MainAdapter(Context context, ArrayList<Main_Data> list, GridView listview_main) {
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
				view = layoutInflater.inflate(R.layout.main_activity_listrow, parent, false); 
			}
			ImageView img_imageurl = (ImageView)view.findViewById(R.id.img_imageurl);
			img_imageurl.setFocusable(false);
			String image_url = list.get(position).thumb;
			
			Picasso.with(context)
            .load(image_url)
            .placeholder(R.drawable.fanroom_list_thumbnail_001)
            .error(R.drawable.fanroom_list_thumbnail_001)
            .into(img_imageurl);

			TextView txt_subject = (TextView)view.findViewById(R.id.txt_subject);
//			txt_subject.setText(list.get(position).subject);
			setTextViewColorPartial(txt_subject, list.get(position).subject, MainActivity.searchKeyword, Color.RED);
			
			bt_favorite = (Button)view.findViewById(R.id.bt_favorite);
			bt_favorite.setFocusable(false);
			cursor = MainActivity.favorite_mydb.getReadableDatabase().rawQuery(
					"select * from favorite_list where num = '"+list.get(position).num+"'", null);
			if(null != cursor && cursor.moveToFirst()){
				num = cursor.getString(cursor.getColumnIndex("num"));
			}else{
				num = "empty";
				
			}
			if(num.equals("empty")){
				bt_favorite.setText(context.getString(R.string.txt_main_activity38));
				bt_favorite.setBackgroundResource(R.drawable.bg_favorite_normal);
			}else{
				bt_favorite.setText(context.getString(R.string.txt_main_activity37));
				bt_favorite.setBackgroundResource(R.drawable.bg_favorite_pressed);	
			}
			
			bt_favorite.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					cursor = MainActivity.favorite_mydb.getReadableDatabase().rawQuery(
							"select * from favorite_list where num = '"+list.get(position).num+"'", null);
					if(null != cursor && cursor.moveToFirst()){
						num = cursor.getString(cursor.getColumnIndex("num"));
					}else{
						num = "empty";
					}
					if(num.equals("empty")){
						bt_favorite.setText(context.getString(R.string.txt_main_activity37));
						bt_favorite.setBackgroundResource(R.drawable.bg_favorite_pressed);
						ContentValues cv = new ContentValues();
						cv.put("id", list.get(position).id);
	    				cv.put("num", list.get(position).num);
	    				cv.put("subject", list.get(position).subject);
	    				cv.put("thumb", list.get(position).thumb);
	    				cv.put("portal", list.get(position).portal);
						MainActivity.favorite_mydb.getWritableDatabase().insert("favorite_list", null, cv);
						MainActivity.main_adapter.notifyDataSetChanged();
						Toast.makeText(context, context.getString(R.string.txt_main_activity11), Toast.LENGTH_SHORT).show();
					}else{
						bt_favorite.setText(context.getString(R.string.txt_main_activity38));
						bt_favorite.setBackgroundResource(R.drawable.bg_favorite_normal);
						MainActivity.favorite_mydb.getWritableDatabase().delete("favorite_list", "num" + "=" +num, null);
						MainActivity.main_adapter.notifyDataSetChanged();
						Toast.makeText(context, context.getString(R.string.txt_main_activity12), Toast.LENGTH_SHORT).show();
					}
				}
			});
		}catch (Exception e) {
		}finally{
			MainActivity.favorite_mydb.close();
			if(cursor != null){
				cursor.close();	
			}
		}
		return view;
	}
	
	public void setTextViewColorPartial(TextView view, String fulltext, String subtext, int color) {
		try{
			view.setText(fulltext, TextView.BufferType.SPANNABLE);
			Spannable str = (Spannable) view.getText();
			int i = fulltext.indexOf(subtext);
			str.setSpan(new ForegroundColorSpan(color), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}catch (IndexOutOfBoundsException e) {
		}
	}
}