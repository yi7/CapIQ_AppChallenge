package com.example.quickcompapp;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.quickcompapp.Access.LoadData;
import com.example.quickcompapp.Access.ParseData;
import com.example.quickcompapp.Response.GDSSDKResponse;
import com.example.quickcompapp.Response.Rows;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.LargeValueFormatter;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendPosition;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;

public class BarChartActivityMultiDataset extends Activity implements OnChartValueSelectedListener {
	
	final Context context = this;
	
	private String mnemonic;
	
	private Button mBackButton;
	private Button mClearButton;
	private Button mAddButton;
	private Button mRemoveButton;
	
	private BarChart mChart;
	private ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
	private int[] Colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA}; int colorIndex;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_barchart);
        
        mBackButton = (Button)findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(BarChartActivityMultiDataset.this, MainActivity.class);
				startActivity( i );
			}
		});
        
        mClearButton = (Button)findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dataSets.clear();
				mChart.clear();
			}
		});
        
        mAddButton = (Button)findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater li = LayoutInflater.from(context);
				View promptsView = li.inflate(R.layout.prompts, null );
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				
				//set prompts.xml to alertdialog builder
				alertDialogBuilder.setView(promptsView);
				final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
				
				//set dialog message
				alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									String identifier = userInput.getText().toString();
									GDSSDKResponse[] sdkResponse=null;
									try {
										String returned = new LoadData("GDSHE", identifier, "IQ_MARKETCAP", "STARTDATE:'10/01/2014',ENDDATE:'10/10/2014'").execute().get();
										ParseData pd = new ParseData( returned );
										pd.createGson();
										sdkResponse = pd.getSdkResponse();
									} catch ( Exception e ) {
										e.printStackTrace();
									}
									
									if( colorIndex > Colors.length-1 ) {
										colorIndex = 0;
									}
									
									setData( sdkResponse[0].getRows(), sdkResponse[0].getIdentifier() );
								
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
					});
				
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				
			}
		});
        
        mRemoveButton = (Button)findViewById(R.id.remove_button);
        mRemoveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater li = LayoutInflater.from(context);
				View promptsView = li.inflate(R.layout.prompts, null );
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				
				//set prompts.xml to alertdialog builder
				alertDialogBuilder.setView(promptsView);
				final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
				
				//set dialog message
				alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									String identifier = userInput.getText().toString();
									Legend l = mChart.getLegend();
									String[] idList = l.getLegendLabels();
									int i;
									for( i = 0; i < idList.length; i++ ) {
										if( idList[i].equals(identifier)) {
											dataSets.remove(i);
											break;
										}
									}
									
									mChart.invalidate();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
					});
				
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				
			}
		});
        
        mChart = (BarChart)findViewById(R.id.chart1);
        mChart.setUnit("$MM");
        mChart.setHighlightEnabled(true);
        mChart.setHighlightIndicatorEnabled(true);
        //mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawYValues(false);
        mChart.setPinchZoom(false);
        mChart.setValueFormatter(new LargeValueFormatter());
        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawHorizontalGrid(false);
        
        String returned = getIntent().getStringExtra("json");
		GDSSDKResponse[] sdkResponse=null;
		try{
			ParseData pd = new ParseData( returned );
			pd.createGson();
			sdkResponse = pd.getSdkResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for( Rows r : sdkResponse[0].getRows() ) {
			int cutIndex = r.getRow()[0].indexOf(':');
			GDSSDKResponse[] tempResponse=null;
			
			try{
				String tempReturned = new LoadData("GDSHE", r.getRow()[0].substring(cutIndex+1), "IQ_MARKETCAP", "STARTDATE:'10/01/2014',ENDDATE:'10/10/2014'").execute().get();
				ParseData tempPd = new ParseData( tempReturned );
				tempPd.createGson();
				tempResponse = tempPd.getSdkResponse();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if( colorIndex > Colors.length-1 ) {
				colorIndex = 0;
			}
			
			setData( tempResponse[0].getRows(), tempResponse[0].getIdentifier() );
		}
		
		Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
		
		Legend l = mChart.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART_INSIDE);
		l.setTypeface(tf);
		
		XLabels xl = mChart.getXLabels();
		xl.setCenterXLabelText(true);
		xl.setTypeface(tf);
		
		YLabels yl = mChart.getYLabels();
		yl.setTypeface(tf);
		yl.setFormatter(new LargeValueFormatter());
		
		mChart.setValueTypeface(tf);
	}
	
	private void setData( Rows[] rows, String id ) {
		
		ArrayList<String> xVals = new ArrayList<String>();
		ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
		
		for( int i = 0; i < rows.length; i++ ) {
    		String date = rows[i].getRow()[1];
    		xVals.add( date.substring(0, date.length()-5) );
    		String data = rows[i].getRow()[0];
            yVals.add(new BarEntry(Float.parseFloat(data.substring(0, data.length()-9)), i));
    	}
		
		BarDataSet set1 = new BarDataSet( yVals, id );
		set1.setColor(Colors[colorIndex++]);
		dataSets.add(set1);
		
		BarData data = new BarData( xVals, dataSets );
		data.setGroupSpace(110f);
		
		mChart.setData(data);
		mChart.invalidate();
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub
		
	}
}
