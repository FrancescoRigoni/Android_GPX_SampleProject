package com.codebutchery.androidgpxsampleproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codebutchery.androidgpx.data.GPXDocument;
import com.codebutchery.androidgpx.data.GPXRoute;
import com.codebutchery.androidgpx.data.GPXRoutePoint;
import com.codebutchery.androidgpx.data.GPXSegment;
import com.codebutchery.androidgpx.data.GPXTrack;
import com.codebutchery.androidgpx.data.GPXTrackPoint;
import com.codebutchery.androidgpx.data.GPXWayPoint;
import com.codebutchery.androidgpx.xml.GpxParser;
import com.codebutchery.androidgpx.xml.GpxParserHandler;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends Activity implements OnItemClickListener,
                GpxParser.GpxParserListener, GpxParserHandler.GpxParserProgressListener {
	 
	private ListView mListView = null;
	private static ArrayList<String> mFiles = null;

    static {
		mFiles = new ArrayList<String>();
        mFiles.add("boise_routes.gpx");
		mFiles.add("bogus_basin.gpx");
		mFiles.add("boise_front.gpx");
		mFiles.add("clementine_loop.gpx");
		mFiles.add("fells_loop.gpx");
		mFiles.add("sample_file.gpx");
		mFiles.add("colle_dei_morti.gpx");
		mFiles.add("s_nna_di_vinadio.gpx");
		mFiles.add("val_fontanalba_valle_della_miniera.gpx");
	}
	
	private ProgressDialog mProgressDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_activity);
		mListView = (ListView) findViewById(R.id.lvListView);
		mListView.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				return mFiles.size();
			}

			@Override
			public Object getItem(int arg0) {
				return mFiles.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public View getView(int arg0, View recycled, ViewGroup vg) {
				
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = recycled;
				if (v == null) v = inflater.inflate(R.layout.lv_item_title_only, vg, false);
				
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				tvTitle.setText((String) getItem(arg0));
				
				return v;
			}
			
		});
		
		mListView.setOnItemClickListener(this);
		
		
		/*
		// Code to test date time parsing
		String first = "2012-10-13T17:18:33Z"; 
					  //2012-10-13T08:44:31Z
		
		 
		Log.e("---", "Parsing " + first); 
		Date date = GPXBasePoint.parseTimestampIntoDate(first);
		 

		if (date != null) Log.e("---", "Output: " + GPXBasePoint.getTimeStampAsString(date));
		*/
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		
		String fileName = (String) mListView.getAdapter().getItem(pos);
		try {
        	InputStream input = getAssets().open(fileName);
        	// The GpxParser automatically closes the InputStream so we do not have to bother about it
        	new GpxParser(input, this, this).parse();
        	
		} catch (IOException e) {
			Toast.makeText(this, "IOExeption opening file", Toast.LENGTH_SHORT).show();
		}
		
	}

    @Override
    public void onGpxNewRouteParsed(int count, GPXRoute track) {
        mProgressDialog.setMessage("Finished parsing route " + track.getName());
    }

    @Override
    public void onGpxNewRoutePointParsed(int count, GPXRoutePoint routePoint) {

    }

	@Override
	public void onGpxNewTrackParsed(int count, GPXTrack track) {
		mProgressDialog.setMessage("Finished parsing track " + track.getName());
	}

	@Override
	public void onGpxNewSegmentParsed(int count, GPXSegment segment) {
		mProgressDialog.setMessage("Parsing track segment " + count);
	}
 
	@Override
	public void onGpxNewTrackPointParsed(int count, GPXTrackPoint trackPoint) {
		
	}

	@Override
	public void onGpxNewWayPointParsed(int count, GPXWayPoint wayPoint) {

	}

	@Override
	public void onGpxParseStarted() {
		mProgressDialog = ProgressDialog.show(this, "Parsing GPX", "Started");
	}

	@Override
	public void onGpxParseCompleted(GPXDocument document) {
		mProgressDialog.dismiss();
		
		GpxFileActivity.mDocument = document;
		
		Intent intent = new Intent(this, GpxFileActivity.class);
		startActivity(intent);
		
	}

	@Override
	public void onGpxParseError(String type, String message, int lineNumber, int columnNumber) {
		mProgressDialog.dismiss();
		
		new AlertDialog.Builder(this)
	    .setTitle("Error")
	    .setMessage("An error occurred: " + message)
	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            dialog.cancel();
	        }
	     })
	     .show();
	}

}
