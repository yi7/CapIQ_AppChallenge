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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.example.quickcompapp.Access.LoadData;
import com.example.quickcompapp.Access.ParseData;
import com.example.quickcompapp.Response.GDSSDKResponse;
import com.example.quickcompapp.Response.Rows;

public class TableChartActivity extends Activity {
	
	final Context context = this;
	
	private Button mClearButton;
	private Button mAddButton;
	private ImageButton mRemoveButton;
	private Button mBarChartButton;
	private Button mLineChartButton;
	
	private static final String TAG = "TableActivity";

	protected static final GDSSDKResponse[] GDSSDKResponse = null;
	private String[] overview = {"IQ_MARKETCAP","IQ_CLOSEPRICE","IQ_TOTAL_REV","IQ_BASIC_EPS_INCL","IQ_NI_MARGIN"};
	private String[] heading = {"Ticker", "Market Cap ($MM)", "Close Price ($MM)", "Total Revenue ($MM)", "Basic EPS ($)", "Net Margin (%)" };
	
	private ArrayList<String> tickerList = new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tablechart);
		
        mClearButton = (Button)findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TableLayout table = (TableLayout) findViewById(R.id.main_table);       
				table.removeAllViews();
				tickerList.clear();
				createTableRow( heading, true );
				createChartingRow( heading.length, true );
		        createChartingRow( heading.length, false );
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
									String[] tableRow = new String[6];
									GDSSDKResponse[] tempResponse=null;
									try {
										int i = 0;
										tableRow[i++] = identifier;
										for( String mnemonic: overview ) {
											String tempJson = new LoadData("GDSHE", identifier, mnemonic, "STARTRANK:'1'").execute().get();
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
									tickerList.add(identifier);
									createTableRow( tableRow, false );
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
        
        createTableRow( heading, true );
        createChartingRow( heading.length, true );
        createChartingRow( heading.length, false );
		
        Parcelable[] parcel = getIntent().getParcelableArrayExtra("rows");
        Rows[] rows = new Rows[parcel.length];
        rows = Arrays.copyOf(parcel, parcel.length, Rows[].class);
		
		for( Rows r : rows ) {
			int cutIndex = r.getRow()[0].indexOf(':');
			String[] tableRow = new String[6];
			tickerList.add(r.getRow()[0].substring(cutIndex+1));
			GDSSDKResponse[] tempResponse=null;
			try {
				int i = 0;
				tableRow[i++] = r.getRow()[0].substring(cutIndex+1);
				for( String mnemonic: overview ) {
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
			
			createTableRow( tableRow, false );
		}
	}
	
	public void clickChart( String mnemonic, boolean isBar ) {
		Rows[] rows = new Rows[tickerList.size()];
		for( int i = 0; i < tickerList.size(); i++ ) {
			Rows tempRows = new Rows();
			String[] s = new String[1];
			s[0] = tickerList.get(i);
			tempRows.setRow(s);
			rows[i] = tempRows;
		}
		
		Intent i;
		if( isBar ) {
			i = new Intent(TableChartActivity.this, BarChartActivity.class);
		} else {
			i = new Intent(TableChartActivity.this, LineChartActivity.class);
		}
		
		i.putExtra("rows", rows);
		i.putExtra("mnemonic", mnemonic);
		startActivity( i );
	}
	
	public void createChartingRow( int n, boolean isBar ) {
		TableLayout tl = (TableLayout)findViewById(R.id.main_table);
		TableRow tr = new TableRow(this);
		LayoutParams lp = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tr.setLayoutParams(lp);
		
		TextView space1 = new TextView(this);
		space1.setLayoutParams(lp);
		space1.setText( "   " );
		tr.addView(space1);
		
		for( int i = 0; i < n; i++ ) {
			
			if( i > 0 ) {
				if( isBar ) {
					mBarChartButton = new Button(this);
					mBarChartButton.setText("Bar");
					mBarChartButton.setGravity(Gravity.CENTER);
					mBarChartButton.setId(i);
					mBarChartButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							switch( v.getId() ) {
								case 1: clickChart("IQ_MARKETCAP", true); break;
								case 2: clickChart("IQ_CLOSEPRICE", true); break;
								case 3: clickChart("IQ_TOTAL_REV", true); break;
								case 4: clickChart("IQ_BASIC_EPS_INCL", true); break;
								case 5: clickChart("IQ_NI_MARGIN", true); break;
							}
						}
				    });
					tr.addView(mBarChartButton);
				} else {
					mLineChartButton = new Button(this);
					mLineChartButton.setText("Line");
					mLineChartButton.setGravity(Gravity.CENTER);
					mLineChartButton.setId(i);
					mLineChartButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							switch( v.getId() ) {
								case 1: clickChart("IQ_MARKETCAP", false); break;
								case 2: clickChart("IQ_CLOSEPRICE", false); break;
								case 3: clickChart("IQ_TOTAL_REV", false); break;
								case 4: clickChart("IQ_BASIC_EPS_INCL", false); break;
								case 5: clickChart("IQ_NI_MARGIN", false); break;
							}
						}
				    });
					mLineChartButton.setGravity(Gravity.CENTER);
					tr.addView(mLineChartButton);
				}
			} else {
				TextView space = new TextView(this);
				space.setLayoutParams(lp);
				space.setText( "   " );
				tr.addView(space);
			}
			
			TextView space2 = new TextView(this);
			space2.setLayoutParams(lp);
			space2.setText( "   " );
			tr.addView(space2);
		}
		
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	public void createTableRow( String[] rowData, boolean isHeading ) {
		
		if( !isHeading ) {
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
		}
		
		TableLayout tl = (TableLayout)findViewById(R.id.main_table);
		TableRow tr = new TableRow(this);
		LayoutParams lp = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tr.setLayoutParams(lp);
		tr.setGravity(Gravity.CENTER_VERTICAL);
		
		if( isHeading ) {
			TextView space = new TextView(this);
			space.setLayoutParams(lp);
			space.setText( "" );
			tr.addView(space);
		} else {
			mRemoveButton = new ImageButton(this);
			mRemoveButton.setImageResource(R.drawable.remove_button);
			mRemoveButton.setBackgroundColor(Color.TRANSPARENT);
			mRemoveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					View row = (View) v.getParent();
		            ViewGroup container = ((ViewGroup)row.getParent());
		            
		            TableRow tr = (TableRow) v.getParent();
		            TextView tickerView = (TextView) tr.getChildAt(1);
		            
		            container.removeView(row);
		            container.invalidate();
		            
		            String ticker = tickerView.getText().toString();
		            
		            for( int i = 0; i < tickerList.size(); i++ ) {
		            	if( tickerList.get(i).equals(ticker) ) {
		            		tickerList.remove(i);
		            	}
		            }
				}
		    });
			
			tr.addView(mRemoveButton);
		}
		
		for( int i = 0; i < rowData.length; i++ ) {
			
			TextView data = new TextView(this);
			data.setLayoutParams(lp);
			data.setTextColor(Color.WHITE);
			data.setText( rowData[i] );
			data.setGravity(Gravity.CENTER);
			tr.addView(data);
			
			TextView space = new TextView(this);
			space.setLayoutParams(lp);
			space.setText( "   " );
			tr.addView(space);
		}
		
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
}
