package com.codebutchery.androidgpxsampleproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.codebutchery.androidgpx.data.GPXTrack;

import java.util.ArrayList;

public class TracksActivity extends Activity implements OnItemClickListener {
	 
	private ListView mListView = null;

	public static ArrayList<GPXTrack> mTracks = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.list_activity);
		
		mListView = (ListView) findViewById(R.id.lvListView);
		mListView.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				return mTracks.size();
			}

			@Override
			public Object getItem(int arg0) {
				return mTracks.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public View getView(int arg0, View recycled, ViewGroup vg) {
				
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = recycled;
				if (v == null) v = inflater.inflate(R.layout.lv_item_track, vg, false);
				
				GPXTrack t = (GPXTrack) getItem(arg0);
				
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				TextView tvType = (TextView) v.findViewById(R.id.tvType);
				TextView tvUserDesc = (TextView) v.findViewById(R.id.tvUserDesc);
				TextView tvGpsComment = (TextView) v.findViewById(R.id.tvGpsComment);
				TextView tvSegmentsCount = (TextView) v.findViewById(R.id.tvSegmentsCount);

				tvTitle.setText(t.getName());
				tvType.setText("Type: " + t.getType());
				tvUserDesc.setText("User description: " + t.getUserDescription());
				tvGpsComment.setText("Gps comment: " + t.getGpsComment());
				tvSegmentsCount.setText("Segments: " + t.getSegments().size());
				
				return v;
			}
			
		});
		
		mListView.setOnItemClickListener(this);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		
		GPXTrack t = (GPXTrack) mListView.getAdapter().getItem(pos);
		
		SegmentsActivity.mSegments = t.getSegments();
		
		Intent intent = new Intent(this, SegmentsActivity.class);
		startActivity(intent);
		
	}


}
