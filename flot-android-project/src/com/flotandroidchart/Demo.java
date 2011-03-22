package com.flotandroidchart;

import java.util.Vector;

import com.flotandroidchart.Demo_List.ItemData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Demo extends Activity implements OnItemClickListener {
	GridView grid_main;
	Vector<ItemData> itemData = new Vector<ItemData>();
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_switch);

		grid_main = (GridView)findViewById(R.id.GridView01);
		
		itemData.add(new ItemData(android.R.drawable.ic_menu_manage, "Basic Example", this.getPackageName() + ".Demo1"));
		itemData.add(new ItemData(android.R.drawable.ic_menu_always_landscape_portrait, "Different graph types", this.getPackageName() + ".Demo2"));
		itemData.add(new ItemData(android.R.drawable.ic_menu_agenda, "Setting various options", this.getPackageName() + ".Demo3"));
		itemData.add(new ItemData(android.R.drawable.ic_menu_camera, "Interacting with datapoints", this.getPackageName() + ".Demo4"));
		itemData.add(new ItemData(android.R.drawable.ic_menu_call, "CrossHair Plugin", this.getPackageName() + ".Demo5"));
		grid_main.setAdapter(new ImageAdapter(this, itemData));
		grid_main.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
		if(arg2 < itemData.size()) {
			
			
			ItemData currentItemData = itemData.elementAt(arg2);
			
			Class<?> actClass = vv.class;
			try {
				actClass = Class.forName(currentItemData.getActivity());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Intent intent = new Intent(this, actClass);
			this.startActivity(intent);
		}
	}
	
	public class ItemData{
		
		private int image = R.drawable.icon;
		private String description = "Demo";
		private String activity = "";
		
		public ItemData(int image, String description, String activity) {
			this.image = image;
			this.description = description;
			this.activity = activity;
		}

		public int getImage() {
			return image;
		}
		
		public void setImage(int image) {
			this.image = image;
		}
		
		public String getDescription() {
			return description;
		}
		
		public void setDescription(String description) {
			this.description = description;
		}
		
		public String getActivity() {
			return activity;
		}
		
		public void setActivity(String activity) {
			this.activity = activity;
		}
	}
	
	public class ImageAdapter extends BaseAdapter{
		Context mContext;
		Vector<ItemData> mItemData;
		public static final int ACTIVITY_CREATE = 10;
		public ImageAdapter(Context c){
			mContext = c;
			mItemData = new Vector<ItemData>();
		}
		public ImageAdapter(Context c, Vector<ItemData> itemData) {
			mContext = c;
			mItemData = itemData;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mItemData.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v;
			if(convertView==null && position < mItemData.size()){
				ItemData currentItemData = mItemData.elementAt(position);
				LayoutInflater li = getLayoutInflater();
				v = li.inflate(R.layout.icon, null);
				TextView tv = (TextView)v.findViewById(R.id.icon_text);
				tv.setText(currentItemData.getDescription());
				ImageView iv = (ImageView)v.findViewById(R.id.icon_image);
				iv.setImageResource(currentItemData.getImage());

			}
			else
			{
				v = convertView;
			}
			return v;
		}
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

}
