package com.example.quickcompapp;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

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
	
	private final String TAG = "LineChartActivity";
	private LineChart mChart;
	private ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
	private int[] Colors = {Color.BLACK, Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE}; int colorIndex;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_linechart);
 
        mChart = (LineChart)findViewById(R.id.chart1);
        mChart.setUnit("$MM");
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
    		xVals.add( date.substring(0, date.length()-5) );
    		String data = rows[i].getRow()[0];
            yVals.add(new Entry(Float.parseFloat(data/*.substring(0, data.length()-9)*/), i));
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
    }
}
