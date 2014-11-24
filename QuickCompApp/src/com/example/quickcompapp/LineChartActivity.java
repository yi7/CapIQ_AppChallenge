package com.example.quickcompapp;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendForm;
import com.github.mikephil.charting.utils.LimitLine;
import com.github.mikephil.charting.utils.LimitLine.LimitLabelPosition;


public class LineChartActivity extends Activity {
	
	final Context context = this;
	
	private Button mBackButton;
	private Button mClearButton;
	private Button mAddButton;
	private Button mRemoveButton;
	
	private final String TAG = "LineChartActivity";
	private LineChart mChart;
	private ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
	private int[] Colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA}; int colorIndex;
	
	private ArrayList<String> tickerList = new ArrayList<String>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_linechart);
        
        mBackButton = (Button)findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Rows[] rows = new Rows[tickerList.size()];
				for( int i = 0; i < tickerList.size(); i++ ) {
					Rows tempRows = new Rows();
					String[] s = new String[1];
					s[0] = tickerList.get(i);
					tempRows.setRow(s);
					rows[i] = tempRows;
				}
				
				try{
					Intent i = new Intent(LineChartActivity.this, TableChartActivity.class);
					i.putExtra("rows", rows);
					startActivity( i );
				} catch( Exception e ) {
					e.printStackTrace();
				}
			}
		});
        
        mClearButton = (Button)findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tickerList.clear();
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
									tickerList.add(identifier);
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
											tickerList.remove(i);
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
 
        mChart = (LineChart)findViewById(R.id.chart1);
        mChart.setUnit("");
        mChart.setDrawUnitsInChart(true);
        mChart.setStartAtZero(false);
        mChart.setDrawYValues(false);
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");
        mChart.setHighlightEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.GRAY);
        mChart.setStartAtZero(false);
        
        Parcelable[] parcel = getIntent().getParcelableArrayExtra("rows");
        Rows[] rows = new Rows[parcel.length];
        rows = Arrays.copyOf(parcel, parcel.length, Rows[].class);
        String mnemonic = getIntent().getStringExtra("mnemonic");
		
		for( Rows r : rows ) {
			GDSSDKResponse[] tempResponse=null;
			tickerList.add(r.getRow()[0]);
			try{
				String tempReturned = new LoadData("GDSHE", r.getRow()[0], mnemonic, "STARTDATE:'10/01/2014',ENDDATE:'10/10/2014'").execute().get();
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
		
		Legend legend = mChart.getLegend();
		legend.setForm(LegendForm.LINE);
    }
    
    private void setData( Rows[] rows, String id ) {
    	
    	ArrayList<String> xVals = new ArrayList<String>();
    	ArrayList<Entry> yVals = new ArrayList<Entry>();
    	
    	for( int i = 0; i < rows.length; i++ ) {
    		String date = rows[i].getRow()[1];
    		xVals.add( date );
    		String data = rows[i].getRow()[0];
    		Log.d( "Test", data );
            yVals.add(new Entry(Float.parseFloat(data), i));
    	}
    	
    	LineDataSet set1 = new LineDataSet(yVals, id );
    	set1.enableDashedLine(10f, 5f, 0f);
    	int color = Colors[colorIndex++];
    	set1.setColor(color);
    	set1.setCircleColor(color);
    	set1.setLineWidth(1f);
    	set1.setCircleSize(4f);
    	set1.setFillAlpha(65);
    	set1.setFillColor(color);
    	
    	dataSets.add(set1);
    	
    	LineData data = new LineData(xVals, dataSets);
    	
    	mChart.setData(data);
    	mChart.invalidate();
    }
}
