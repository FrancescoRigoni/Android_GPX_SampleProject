package com.codebutchery.androidgpxsampleproject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.codebutchery.androidgpx.data.GPXBasePoint;
import com.codebutchery.androidgpx.data.GPXTrackPoint;

import java.util.ArrayList;
import java.util.List;

public class TrackPointsActivity extends Activity {
	 
	private ListView mListView = null;

	public static List<GPXTrackPoint> mPoints = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.list_activity);
		
		mListView = findViewById(R.id.lvListView);
		mListView.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				return mPoints.size();
			}

			@Override
			public Object getItem(int arg0) {
				return mPoints.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public View getView(int arg0, View recycled, ViewGroup vg) {
				
				final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = recycled;
				if (v == null) v = inflater.inflate(R.layout.lv_item_point, vg, false);
				
				GPXBasePoint t = (GPXBasePoint) getItem(arg0);
				
				TextView tvName = (TextView) v.findViewById(R.id.tvName);
				TextView tvLatLon = (TextView) v.findViewById(R.id.tvLatLon);
				TextView tvTimestamp = (TextView) v.findViewById(R.id.tvTimestamp);
				TextView tvDescription = (TextView) v.findViewById(R.id.tvDescription);
				TextView tvType = (TextView) v.findViewById(R.id.tvType);
				TextView tvHdopVdop = (TextView) v.findViewById(R.id.tvHdopVdop);
				TextView tvEle = (TextView) v.findViewById(R.id.tvEle);

				tvName.setText("Name " + t.getName());
				tvLatLon.setText("Lat: " + t.getLatitude() + " Lon: " + t.getLongitude());
				if (t.getTimeStamp() != null) tvTimestamp.setText("Timestamp: " + GPXBasePoint.getTimeStampAsString(t.getTimeStamp()));
				else tvTimestamp.setText("Timestamp: NA");
				tvDescription.setText("Description: " + t.getDescription());
				tvType.setText("Type: " + t.getType());
				tvHdopVdop.setText("HDOP: " + t.getHDop() + " VDOP: " + t.getVDop());
				tvEle.setText("Elevation " + t.getElevation() + " m");
				
				return v;
			}
		});
	}
}
