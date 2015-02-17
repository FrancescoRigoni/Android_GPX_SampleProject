package com.codebutchery.androidgpxsampleproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.codebutchery.androidgpx.data.GPXDocument;
import com.codebutchery.androidgpx.print.GPXFilePrinter;


public class GpxFileActivity extends Activity implements OnItemClickListener, GPXFilePrinter.GPXFilePrinterListener {
	 
	private ListView mListView = null;

	public static GPXDocument mDocument = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.gpx_file_activity);
		mListView = (ListView) findViewById(R.id.lvListView);
		mListView.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				return 4;
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				return 0;
			}

			@Override
			public View getView(int arg0, View recycled, ViewGroup vg) {
				
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = recycled;
				if (v == null) v = inflater.inflate(R.layout.lv_item_title_subtitle, vg, false);
				
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				TextView tvSubtitle = (TextView) v.findViewById(R.id.tvSubtitle);
				
				if (arg0 == 0) {
					tvTitle.setText("Waypoints");
					tvSubtitle.setText(mDocument.getWayPoints().size() > 0 ? "" + mDocument.getWayPoints().size() + " waypoints" : "No waypoints");
				}
				else if (arg0 == 1) {
					tvTitle.setText("Tracks");
					tvSubtitle.setText(mDocument.getTracks().size() > 0 ? "" + mDocument.getTracks().size() + " tracks" : "No tracks");
				}
				else if (arg0 == 2) {
					tvTitle.setText("Routes");
                    tvSubtitle.setText(mDocument.getRoutes().size() > 0 ? "" + mDocument.getRoutes().size() + " routes" : "No routes");
				}
                else if (arg0 == 3) {
                    tvTitle.setText("Print");
                    tvSubtitle.setText("Print to file and send it via e-mail");
                }
				
				return v;
			}
			
		});
		
		mListView.setOnItemClickListener(this);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		
		if (pos == 0) {
			
			WayPointsActivity.mPoints = mDocument.getWayPoints();
			
			Intent intent = new Intent(this, WayPointsActivity.class);
			startActivity(intent);
			
		}
		else if (pos == 1) {
			
			TracksActivity.mTracks = mDocument.getTracks();
			
			Intent intent = new Intent(this, TracksActivity.class);
			startActivity(intent);
			
		}
		else if (pos == 2) {

            RoutesActivity.mRoutes = mDocument.getRoutes();

            Intent intent = new Intent(this, RoutesActivity.class);
            startActivity(intent);
			
		}
        else if (pos == 3) {

            new GPXFilePrinter(mDocument, "/mnt/sdcard/output.gpx", this).print();

        }
		
	}
	
	private ProgressDialog mProgressDialog = null;

	@Override
	public void onGPXPrintStarted() {
		
		mProgressDialog = ProgressDialog.show(this, "Printing GPX to file", "Started");
		
	}

	@Override
	public void onGPXPrintCompleted() {
		
		mProgressDialog.dismiss();
		
		new AlertDialog.Builder(this)
	    .setTitle("Done")
	    .setMessage("File was printed, press ok to send it via email")
	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            dialog.cancel();
	            
	            Intent emailIntent = new Intent(Intent.ACTION_SEND);

	            emailIntent.putExtra(Intent.EXTRA_EMAIL, "");
	            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "GPX file");
	            emailIntent.setType("plain/text");
	            emailIntent.putExtra(Intent.EXTRA_TEXT, "Here's your file");
	            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + "/mnt/sdcard/output.gpx"));

	            startActivity(emailIntent);
	             
	            
	        }
	     })
	     .show();
		
	}

	@Override
	public void onGPXPrintError(String message) {
		
		mProgressDialog.dismiss();
		
		new AlertDialog.Builder(this)
	    .setTitle("Error")
	    .setMessage("An error occurred while printing: " + message)
	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            dialog.cancel();
	        }
	     })
	     .show();
		
	}


}
