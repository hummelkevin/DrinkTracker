package com.example.drinktracker;

import java.util.Calendar;

import com.example.drinktracker.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;


public class TemplateActivity extends Activity {
	
	public String drinkArray[][] ={ {"Name","alc","Size","Cost","$/Drink","Drinks"}}; 
	final Context context = this;
	private static final int DIALOG_ALERT = 10;
	int currentRow = 0;
	double totalCostOverall=0;
	double totalDrinksOverall=0;
	double mLtooz = 0.033814;
	double scaler = 1;

	private boolean isEmpty(EditText etText) {
	    if (etText.getText().toString().trim().length() > 0) {
	        return false;  //not empty
	    } else {
	        return true; // empty
	    }
	};
	
	private void addRow(String name,double units,double costperDrink,double totalDrinks)
	{
		//Add to Table
		TableLayout itemtable = (TableLayout) findViewById(R.id.entries_table_layout);
		TableRow row= new TableRow(this);
		TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        
		TextView nameText = new TextView(this);
		TextView unitsText = new TextView(this);
		TextView costText = new TextView(this);
		TextView drinkText = new TextView(this);
		
		nameText.setTextColor(getResources().getColor(R.color.black));
		unitsText.setTextColor(getResources().getColor(R.color.black));
		costText.setTextColor(getResources().getColor(R.color.black));
		drinkText.setTextColor(getResources().getColor(R.color.black));
		
		int padd = getResources().getDimensionPixelSize(R.dimen.entry_row_padding);
		nameText.setPadding(padd,padd,padd,padd);
		unitsText.setPadding(padd,padd,padd,padd);
		costText.setPadding(padd,padd,padd,padd);
		drinkText.setPadding(padd,padd,padd+padd,padd);
//		
//		
//		TableLayout.LayoutParams lp2 = new TableLayout.LayoutParams(drinkText.getLayoutParams());
//		lp.setMargins(0, 0, padd, 0);;
//		drinkText.setLayoutParams(lp2);
//		
//		
		nameText.setGravity(Gravity.LEFT);
		unitsText.setGravity(Gravity.RIGHT);
		costText.setGravity(Gravity.RIGHT);
		drinkText.setGravity(Gravity.RIGHT);
		
		nameText.setText(name);
		unitsText.setText(String.format("%.2f",units));
		costText.setText(String.format("%.2f", costperDrink));
		drinkText.setText(String.format("%.2f", totalDrinks));
		
		row.addView(nameText);
		row.addView(unitsText);
		row.addView(costText);
		row.addView(drinkText);
		itemtable.addView(row);
		
		currentRow++;
		
		
	};
	
	private void updateCounters(double costs,double drinks)
	{
		totalCostOverall += costs;
		totalDrinksOverall += drinks;
		TextView costView = (TextView) findViewById(R.id.cost_total_stats_textView);
		costView.setText("$"+String.format("%.2f",totalCostOverall));
		
		TextView drinkView = (TextView) findViewById(R.id.drink_total_stats_textView);
		drinkView.setText(String.format("%.2f",totalDrinksOverall));
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.activity_template); 
	    
	    final Button newEntry = (Button) findViewById(R.id.entry_button);
	    final TextView templateView = (TextView) findViewById(R.id.date_textView);
	    
	    Calendar c = Calendar.getInstance();
	    int Day = c.get(Calendar.DATE);
	    int Year = c.get(Calendar.YEAR);
	    int Month = c.get(Calendar.MONTH)+1;
	    
	    templateView.setText(String.valueOf(Month)+"/"+String.valueOf(Day)+"/"+String.valueOf(Year));
	    
	    
	    
	    newEntry.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        		//Open Dialog Here
        		// custom dialog
    			final Dialog dialog = new Dialog(context);
    			dialog.setContentView(R.layout.custom);
    			dialog.setTitle("New Entry");
    			final double oneDrink = 0.6;
    			
    			
    			final EditText itemNameEditText = (EditText) dialog.findViewById(R.id.item_name_entry);
    			final EditText sizeEditText = (EditText) dialog.findViewById(R.id.size_entry);
    			final EditText alcoholEditText = (EditText) dialog.findViewById(R.id.alcohol_entry);
    			final EditText numberEditText = (EditText) dialog.findViewById(R.id.number_entry);
    			final EditText costEditText = (EditText) dialog.findViewById(R.id.cost_entry);
    			final Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);    			
    			final ImageButton shotButton = (ImageButton) dialog.findViewById(R.id.shots_button);
    			final ImageButton beerButton = (ImageButton) dialog.findViewById(R.id.beer_button);
    			final ImageButton wineButton = (ImageButton) dialog.findViewById(R.id.wine_button);
    			final Button unitButton = (Button) dialog.findViewById(R.id.units_button);
    			final Button dialogCloseButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);
    			
    			shotButton.setOnClickListener(new OnClickListener(){
    				public void onClick(View v){
    					sizeEditText.setText("1.5");
    					unitButton.setText("oz");
    					scaler = 1;
    				}
    			});
    			
    			wineButton.setOnClickListener(new OnClickListener(){
    				public void onClick(View v){
    					sizeEditText.setText("5");
    					unitButton.setText("oz");
    					scaler = 1;
    				}
    			});
    			beerButton.setOnClickListener(new OnClickListener(){
    				public void onClick(View v){
    					sizeEditText.setText("12");
    					unitButton.setText("oz");
    					scaler = 1;
    				}
    			});
    			unitButton.setOnClickListener(new OnClickListener(){
    				public void onClick(View v){
    					if(unitButton.getText().toString()=="oz"){
    						scaler=mLtooz;
    						unitButton.setText("mL");
    					}else{
    						scaler=1;
    						unitButton.setText("oz");
    					}
    				}
    			});
    			
    			// if button is clicked, close the custom dialog
    			dialogButton.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					boolean goodtoGO = true;
    					double alcohol = 0;
    					double units = 0;
    					double cost = 0;
    					double size = 0;
    					String name = null;
    					
    					double drinkTotal;
    					double costperDrink;
    					
    					
    					if(isEmpty(itemNameEditText))
    					{
    						Toast.makeText(getApplicationContext(), "Enter item name",
    								   Toast.LENGTH_LONG).show();
    						goodtoGO = false;
    					}else{name = itemNameEditText.getText().toString().trim();}
    						
    					if(isEmpty(sizeEditText))
    					{
    						Toast.makeText(getApplicationContext(), "Enter item size",
    								   Toast.LENGTH_LONG).show();
    						goodtoGO = false;
    					}else{size = Double.parseDouble(sizeEditText.getText().toString().trim());}
    					
    					if(isEmpty(alcoholEditText))
    					{
    						Toast.makeText(getApplicationContext(), "Enter alcohol content",
    								   Toast.LENGTH_LONG).show();
    						goodtoGO = false;
    					}else{alcohol = Double.parseDouble(alcoholEditText.getText().toString().trim());}
    					    					
    					if(isEmpty(numberEditText))
    					{
    						Toast.makeText(getApplicationContext(), "Enter number of units",
    								   Toast.LENGTH_LONG).show();
    						goodtoGO = false;
    					}else{units = Double.parseDouble(numberEditText.getText().toString().trim());}
    					
    					if(isEmpty(costEditText))
    					{
    						Toast.makeText(getApplicationContext(), "Enter item cost",
    								   Toast.LENGTH_LONG).show();
    						goodtoGO = false;
    					}else{cost = Double.parseDouble(costEditText.getText().toString().trim());}
    					
    					
    					
    					if(goodtoGO)
    					{
    						//Calculate Needed Values
    						drinkTotal = (alcohol/100)*size*units*scaler/oneDrink;
    						costperDrink = cost/drinkTotal;
    						
    						addRow(name,units,costperDrink,drinkTotal);
    						updateCounters(cost,drinkTotal);
    						dialog.dismiss();
    						
    					}
    				}
    			});
     
    			dialogCloseButton.setOnClickListener(new OnClickListener(){
    				public void onClick(View v){
    					dialog.dismiss();
    				}
    			});
    			
     
    			dialog.show();
        	}        	
        });

	
	}
	
	
	
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		

	}

}
