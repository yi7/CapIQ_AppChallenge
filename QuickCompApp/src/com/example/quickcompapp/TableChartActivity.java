package com.example.quickcompapp;

import com.example.quickcompapp.Access.LoadData;
import com.example.quickcompapp.Access.ParseData;
import com.example.quickcompapp.Response.GDSSDKResponse;
import com.example.quickcompapp.Response.Rows;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class TableChartActivity extends Activity {
	private static final String TAG = "TableActivity";
	private String[] overviewM = {"IQ_MARKETCAP","IQ_CLOSEPRICE","IQ_TOTAL_REV","IQ_BASIC_EPS_INCL","IQ_NI_MARGIN"};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tablechart);
		
		String returned = getIntent().getStringExtra("json");
		GDSSDKResponse[] sdkResponse=null;
		
		try{
			ParseData pd = new ParseData( returned );
			pd.createGson();
			sdkResponse = pd.getSdkResponse();
			
			String[] heading = {"Ticker", "Market Cap ($MM)", "Close Price ($MM)", "Total Revenue ($MM)", "Basic EPS ($)", "Net Margin (%)"};
			createTableRow( heading );
			
		} catch( Exception e ) {
			Log.d(TAG, "error with something");
		}
		
		for( Rows r : sdkResponse[0].getRows() ) {
			int cutIndex = r.getRow()[0].indexOf(':');
			String[] tableRow = new String[6];
			GDSSDKResponse[] tempResponse=null;
			try {
				int i = 0;
				tableRow[i++] = r.getRow()[0].substring(cutIndex+1);
				for( String mnemonic: overviewM ) {
					String tempJson = new LoadData("GDSHE", r.getRow()[0].substring(cutIndex+1), mnemonic, "STARTRANK:'1'").execute().get();
					ParseData tempGson = new ParseData( tempJson );
					tempGson.createGson();
					tempResponse = tempGson.getSdkResponse();
					tableRow[i++] = tempResponse[0].getRows()[0].getRow()[0];
				}
				
			} catch ( Exception e ) {
				// TODO Auto-generated catch block
				Log.d( TAG, "error with try in for loop" );
				e.printStackTrace();
			}
			
			createTableRow( tableRow );
		}
	}
	
	public void createTableRow( String[] rowData ) {
		for( int i = 0; i < rowData.length; i++ ) {
			String temp;
			if( rowData[i].equals("Data Unavailable")) {
				rowData[i] = "-----";
			}
			else if( rowData[i].indexOf('.') > 0 ) {
				temp = rowData[i].substring( 0, rowData[i].length() - 4 );
				rowData[i] = temp;
			}
		}
		
		TableLayout tl = (TableLayout)findViewById(R.id.main_table);
		TableRow tr = new TableRow(this);
		LayoutParams lp = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tr.setLayoutParams(lp);
		
		for( int i = 0; i < rowData.length; i++ ) {
			
			TextView data = new TextView(this);
			data.setLayoutParams(lp);
			data.setTextColor(Color.WHITE);
			data.setText( rowData[i] );
			tr.addView(data);
			
			TextView space = new TextView(this);
			space.setLayoutParams(lp);
			space.setText( "    " );
			tr.addView(space);
		}
		
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
}
