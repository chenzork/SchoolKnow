package com.pw.schoolknow.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.pw.schoolknow.R;
import com.pw.schoolknow.bean.FilmItemBean;
import com.pw.schoolknow.helper.BitmapHelper;
import com.pw.schoolknow.utils.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



/**
 * 花椒影院列表适配器类
 * @author wei8888go
 *
 */
public class FilmListAdapter extends BaseAdapter {
	
	private Context context;
	private List<FilmItemBean> list;
	private BitmapUtils bitmapUtils;
	
	
	public FilmListAdapter(Context context, List<FilmItemBean> list) {
		super();
		this.context = context;
		this.list = list;
		bitmapUtils=BitmapHelper.getBitmapUtils(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
	        convertView = LayoutInflater.from(context)
	          .inflate(R.layout.film_list_lv_item, parent, false);
	    }
		
		FilmItemBean item=list.get(position);
		
		ImageView img = ViewHolder.get(convertView, R.id.film_list_item_icon);
		TextView title = ViewHolder.get(convertView, R.id.film_list_item_title);
		TextView time = ViewHolder.get(convertView, R.id.film_list_item_time);
		
		bitmapUtils.display(img, item.getImgUrl());
		title.setText(item.getTitle());
		time.setText(item.getUploadTime());
		return convertView;
	}

}
