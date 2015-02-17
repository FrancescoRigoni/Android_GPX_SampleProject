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

import com.codebutchery.androidgpx.data.GPXSegment;

import java.util.ArrayList;

public class SegmentsActivity extends Activity implements OnItemClickListener {
	 
	private ListView mListView = null;

	public static ArrayList<GPXSegment> mSegments = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.list_activity);
		
		mListView = (ListView) findViewById(R.id.lvListView);
		mListView.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				return mSegments.size();
			}

			@Override
			public Object getItem(int arg0) {
				return mSegments.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public View getView(int arg0, View recycled, ViewGroup vg) {
				
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = recycled;
				if (v == null) v = inflater.inflate(R.layout.lv_item_title_subtitle, vg, false);
				
				GPXSegment s = (GPXSegment) getItem(arg0);
				
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				TextView tvSubtitle = (TextView) v.findViewById(R.id.tvSubtitle);

				tvTitle.setText("Segment " + arg0);
				tvSubtitle.setText("Points: " + s.getTrackPoints().size());
				
				return v;
			}
			
		});
		
		mListView.setOnItemClickListener(this);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		
		GPXSegment s = (GPXSegment) mListView.getAdapter().getItem(pos);
		
		TrackPointsActivity.mPoints = s.getTrackPoints();
		
		Intent intent = new Intent(this, TrackPointsActivity.class);
		startActivity(intent);
		
	}


}
