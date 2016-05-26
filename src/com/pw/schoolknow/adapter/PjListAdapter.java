package com.pw.schoolknow.adapter;

import java.util.List;
import java.util.Map;

import com.pw.schoolknow.R;
import com.pw.schoolknow.utils.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PjListAdapter extends BaseAdapter {
	
	private Context context;
	private List<Map<String,Object>> list;
	
	public PjListAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
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
	          .inflate(R.layout.pj_list_item, parent, false);
	    }
		 TextView subject=ViewHolder.get(convertView, R.id.pj_subject);
		 TextView name=ViewHolder.get(convertView, R.id.pj_teacher);
		 TextView mark=ViewHolder.get(convertView, R.id.pj_mark);
		 TextView demand=ViewHolder.get(convertView, R.id.pj_demand);
		 TextView info=ViewHolder.get(convertView, R.id.pj_credit);
		 TextView tid=ViewHolder.get(convertView, R.id.pj_id);
		 
		 
		 Map<String,Object> map=list.get(position);
		 subject.setText(map.get("course").toString());
		 name.setText(map.get("name").toString());
		 demand.setText(map.get("demand").toString());
		 info.setText(map.get("info").toString());
		 tid.setText(map.get("tid").toString());
		 
		 String markValue=map.get("mark").toString();
		 if(markValue.trim().length()<4||markValue.equals("null")){
			 mark.setText("");
		 }else{
			 mark.setText("ÄúÒÑ¾­ÆÀ·Ö:"+map.get("mark"));
		 }
		 
		return convertView;
	}

}
