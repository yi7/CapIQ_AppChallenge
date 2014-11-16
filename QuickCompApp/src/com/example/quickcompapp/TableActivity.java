package com.example.quickcompapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class TableActivity extends Activity {
	private static final String TAG = "TableActivity";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);
		
		//Intent i = getIntent();
		//GDSSDKResponse[] sdkResponse = (GDSSDKResponse[])i.getSerializableExtra("sdkResponse");
		//Log.d( TAG, sdkResponse[0].getRows()[0].getRow()[0] + "");
		
		String returned = getIntent().getStringExtra("json");
		GDSSDKResponse[] sdkResponse;
		
		try{
			//Gson gson = new Gson();
			//Response response = gson.fromJson( returned, Response.class );
			//sdkResponse = response.getGDSSDKResponse();
			//Log.d( TAG, sdkResponse[0].getRows()[0].getRow()[0] + "");
			
			ParseData pd = new ParseData( returned );
			pd.createGson();
			sdkResponse = pd.getSdkResponse();
			
			createTableRow( "Companies", "MarketCap", "LastPrice", "TotalRevenue", "BasicEPS", "NetMargin" );
			
			String tempMarketCap, tempLastPrice, tempTotalRev, tempBasicEPS, tempNetMargin;
			for( Rows r : sdkResponse[0].getRows() ) {
				int cutIndex = r.getRow()[0].indexOf(':');
				GDSSDKResponse[] tempResponse1=null, tempResponse2=null, tempResponse3=null, tempResponse4=null, tempResponse5=null;
				
				try {
					tempMarketCap = new LoadData("GDSHE", r.getRow()[0].substring(cutIndex+1), "IQ_MARKETCAP").execute().get();
					tempLastPrice = new LoadData("GDSHE", r.getRow()[0].substring(cutIndex+1), "IQ_LASTSALEPRICE").execute().get();
					tempTotalRev = new LoadData("GDSHE", r.getRow()[0].substring(cutIndex+1), "IQ_TOTAL_REV").execute().get();
					tempBasicEPS = new LoadData("GDSHE", r.getRow()[0].substring(cutIndex+1), "IQ_BASIC_EPS_INCL").execute().get();
					tempNetMargin = new LoadData("GDSHE", r.getRow()[0].substring(cutIndex+1), "IQ_NI_MARGIN").execute().get();
					
					ParseData tempMCData = new ParseData( tempMarketCap );
					ParseData tempLPData = new ParseData( tempLastPrice );
					ParseData tempTRData = new ParseData( tempTotalRev );
					ParseData tempBEPSData = new ParseData( tempBasicEPS );
					ParseData tempNMData = new ParseData( tempNetMargin );
					
					tempMCData.createGson();
					tempLPData.createGson();
					tempTRData.createGson();
					tempBEPSData.createGson();
					tempNMData.createGson();
					
					tempResponse1 = tempMCData.getSdkResponse();
					tempResponse2 = tempLPData.getSdkResponse();
					tempResponse3 = tempTRData.getSdkResponse();
					tempResponse4 = tempBEPSData.getSdkResponse();
					tempResponse5 = tempNMData.getSdkResponse();
				} catch ( Exception e ) {
					// TODO Auto-generated catch block
					Log.d( TAG, "error with try in for loop" );
					e.printStackTrace();
				}
				
				
				//Log.d( TAG, r.getRow()[0].substring(cutIndex+1) + ":" + tempResponse[0].getRows()[0].getRow()[0] );
				createTableRow( r.getRow()[0].substring(cutIndex+1),
						tempResponse1[0].getRows()[0].getRow()[0] ,
						tempResponse2[0].getRows()[0].getRow()[0] ,
						tempResponse3[0].getRows()[0].getRow()[0] ,
						tempResponse4[0].getRows()[0].getRow()[0] ,
						tempResponse5[0].getRows()[0].getRow()[0] );
			}
		} catch( Exception e ) {
			Log.d(TAG, "error with something");
		}
		
	}
	
	public void createTableRow( String company, String marketCap, String lastPrice, String totalRev, String basicEPS, String netMargin ) {
		
		String[] dataList = {marketCap, lastPrice, totalRev, basicEPS, netMargin}; String temp;
		for( int i = 0; i < dataList.length; i++ ) {
			if( dataList[i].equals("Data Unavailable")) {
				dataList[i] = "N/A";
			}
			else if( dataList[i].indexOf('.') > 0 ) {
				temp = dataList[i].substring( 0, dataList[i].length() - 4 );
				dataList[i] = temp;
			}
		}
		
		TableLayout tl = (TableLayout)findViewById(R.id.main_table);
		TableRow tr = new TableRow(this);
		LayoutParams lp = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tr.setLayoutParams(lp);
		
		TextView companies = new TextView(this);
		companies.setLayoutParams(lp);
		companies.setText( company );
		tr.addView(companies);
		
		for( int i = 0; i < dataList.length; i++ ) {
			TextView space = new TextView(this);
			space.setLayoutParams(lp);
			space.setText( "   " );
			tr.addView(space);
			
			TextView data = new TextView(this);
			data.setLayoutParams(lp);
			data.setText( dataList[i] );
			tr.addView(data);
		}
		
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
}
