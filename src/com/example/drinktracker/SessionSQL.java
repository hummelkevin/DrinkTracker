package com.example.drinktracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SessionSQL extends SQLiteOpenHelper{


	// Database Version
    private static final int DATABASE_VERSION = 4;
    // Database Name
    private static final String DATABASE_NAME = "userDATA.sqlite3";
    //Path
    private static final String DATABASE_PATH = "/data/data/drinktracker/databases";
    
    private static Context context;
    private static SQLiteDatabase USER_DATA2 = null;
       
    private static String SESSION_TABLE ="SESSION";
    private static String SESSION_ID ="id_";
    private static String SESSION_NAME ="NAME";
    private static String SESSION_DATE ="DATE";
    private static String SESSION_DRINKS ="DRINKS";
    
    private static String ENTRY_TABLE ="ENTRY";
    private static String ENTRY_ID ="id_";
    private static String ENTRY_SESSION_ID ="SESSIONID";
    private static String ENTRY_BEER_NAME ="BEERNAME";
    private static String ENTRY_ALCOHOL ="ALCOHOL";
    private static String ENTRY_SIZE ="SIZE";
    private static String ENTRY_DRINK_COUNT ="COUNT";
    private static String ENTRY_COST ="COST";
 
	public SessionSQL(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		String create_session_table = "CREATE TABLE "
                + SESSION_TABLE + "(" +
                SESSION_ID + " INTEGER PRIMARY KEY," + 
                SESSION_NAME + " TEXT," + 
                SESSION_DATE + " TEXT," + 
                SESSION_DRINKS + " DOUBLE" +
    			")";
    	db.execSQL(create_session_table);
    	//Second Table - Entries (_id, SessionID, BeerName, Alochol, Size, #ofDrinks, Cost)
    	String create_entry_table = "CREATE TABLE "
                + ENTRY_TABLE + "(" +
                ENTRY_ID + " INTEGER PRIMARY KEY," + 
                ENTRY_SESSION_ID + " INTERGER," + 
                ENTRY_BEER_NAME + " TEXT," + 
                ENTRY_ALCOHOL + " DOUBLE," +
                ENTRY_SIZE + " DOUBLE," +
                ENTRY_DRINK_COUNT + " DOUBLE," +
                ENTRY_COST + " DOUBLE" +
    			")";
    	db.execSQL(create_entry_table);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// Drop older table if existed
		
		db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + ENTRY_TABLE);
 
        // Create tables again
        onCreate(db);
	}
    

    
    public  int  addSession(String Name, String Date, String Drinks)
    {
    	SQLiteDatabase USER_DATA = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(SESSION_NAME,Name);
    	values.put(SESSION_DATE,Date);
    	values.put(SESSION_DRINKS,Drinks);
    	
    	long id = USER_DATA.insert(SESSION_TABLE, null, values);
    	USER_DATA.close();
    	//return Session ID;
    	return (int)id;
    }    
    public  void updateSession(String Name, String Date, int SessionID)
    {
    	SQLiteDatabase USER_DATA = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(SESSION_NAME,Name);
    	values.put(SESSION_DATE,Date);
    	
    	long id = USER_DATA.update(SESSION_TABLE, values, SESSION_ID+ "=" + Integer.toString(SessionID), null);
    }
    
    public void updateDrinkCount(double Drinks, int SessionID)
    {
    	SQLiteDatabase USER_DATA = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(SESSION_DRINKS,Drinks);
    	
    	long id = USER_DATA.update(SESSION_TABLE, values, SESSION_ID+ "=" + Integer.toString(SessionID), null);
    }
    public  void deleteSession(int SessionID)
    {
    	//Delete from SessionOver Table
    	
    	//Delete All Entries
    }
    
    public  int  addEntry(String BeerName, String alcoholPercentage,String Size, String Cost, String DrinkCount, int SessionID)
    {
    	SQLiteDatabase USER_DATA = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(ENTRY_SESSION_ID,SessionID);
    	values.put(ENTRY_BEER_NAME,BeerName);
    	values.put(ENTRY_ALCOHOL,Double.valueOf(alcoholPercentage));
    	values.put(ENTRY_SIZE,Double.valueOf(Size));
    	values.put(ENTRY_DRINK_COUNT,Double.valueOf(DrinkCount));
    	values.put(ENTRY_COST,Double.valueOf(Cost));
    	
    	
    	long id = USER_DATA.insert(ENTRY_TABLE, null, values);
    	USER_DATA.close();
    	//return Entry ID;
    	return (int)id;
    	
    }    
    public  void deleteEntry(int entryId)
    {
    	
    }    
    public  void updateEntry(String BeerName, String alcholPercentage, String Cost, String Drink, String DrinkTotal, String SessionID, int entryId)
    {
    	
    }
    
    
    public  String[] retrieveSession(int SessionID)
    {
    	String[] stringValues = new String [4];
    	SQLiteDatabase USER_DATA = this.getWritableDatabase();
    	Cursor c = USER_DATA.query(SESSION_TABLE, null,SESSION_ID + " = "+ String.valueOf(SessionID) , null, null, null,null,null);
    	if (c.moveToFirst()){
    		stringValues[0] = c.getString(c.getColumnIndex(SESSION_NAME));
        	stringValues[1] = c.getString(c.getColumnIndex(SESSION_DATE));
        	stringValues[2] = String.format("%.2f", c.getDouble(c.getColumnIndex(SESSION_DRINKS)));
        	stringValues[3] = c.getString(c.getColumnIndex(SESSION_ID));
        	
    	}

    	USER_DATA.close();
    	return stringValues;
    }    
    public  String[] retrieveEntry(int entryID,int SessionID)
    {
    	String[] stringValues = new String [7];
    	SQLiteDatabase USER_DATA = this.getWritableDatabase();
    	Cursor c = USER_DATA.query(ENTRY_TABLE, null,ENTRY_SESSION_ID + " = "+ String.valueOf(SessionID) , null, null, null,null,null);
    	if(c.moveToPosition(entryID)){
    		stringValues[0] = c.getString(c.getColumnIndex(ENTRY_ID));
        	stringValues[1] = c.getString(c.getColumnIndex(ENTRY_SESSION_ID));
        	stringValues[2] = c.getString(c.getColumnIndex(ENTRY_BEER_NAME));
        	stringValues[3] = c.getString(c.getColumnIndex(ENTRY_ALCOHOL));
        	stringValues[4] = c.getString(c.getColumnIndex(ENTRY_SIZE));
        	stringValues[5] = c.getString(c.getColumnIndex(ENTRY_DRINK_COUNT));
        	stringValues[6] = c.getString(c.getColumnIndex(ENTRY_COST));
    	}
        	
    	

    	USER_DATA.close();
    	return stringValues;
    }

  
    
 
	public int sessionCount(){
		
		SQLiteDatabase USER_DATA = this.getWritableDatabase();
		int count =0;
		
		Cursor c  = USER_DATA.rawQuery("Select * from "+ SESSION_TABLE, null);
		count = c.getCount();
		USER_DATA.close();
		return count;
	}	
	public int entryCount(int SessionID){
		SQLiteDatabase USER_DATA = this.getWritableDatabase();
		int count =0;
		
		Cursor c  = USER_DATA.query(ENTRY_TABLE, null,ENTRY_SESSION_ID + " = "+ String.valueOf(SessionID), null, null, null,null ,null);
		count = c.getCount();
		USER_DATA.close();
		return count;
		
	}
	
	






}
