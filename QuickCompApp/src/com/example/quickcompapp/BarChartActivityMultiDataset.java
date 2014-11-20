package com.example.quickcompapp;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.quickcompapp.Access.LoadData;
import com.example.quickcompapp.Access.ParseData;
import com.example.quickcompapp.Response.GDSSDKResponse;
import com.example.quickcompapp.Response.Rows;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.LargeValueFormatter;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendPosition;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;

public class BarChartActivityMultiDataset extends Activity {
	
	private BarChart mChart;
	private ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
	private int[] Colors = {Color.BLACK, Color.RED, Color.GRAY, Color.GREEN, Color.BLUE}; int colorIndex;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_barchart);
        
        mChart = (BarChart)findViewById(R.id.chart1);
        mChart.setUnit("$MM");
        mChart.setDescription("");
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
			
			if( colorIndex > 3 ) {
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
}
