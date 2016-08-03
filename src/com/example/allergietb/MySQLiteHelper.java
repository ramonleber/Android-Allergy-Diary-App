package com.example.allergietb;

import java.util.Calendar;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Allergie-Beschwerdebuch.db";
 // Database Table Name
    static final String TABLE_NAME = "disorders";//+getSystemTime();
    
    //public static final String TABLE_COMMENTS = "comments";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EYES_GRADE = "eyes_grade";
    public static final String COLUMN_NOSE_GRADE = "nose_grade";
    public static final String COLUMN_MOUTH_GRADE = "mouth_grade";
    public static final String COLUMN_BRONCHIA_GRADE = "bronchia_grade";
    public static final String COLUMN_HANDS_GRADE = "hands_grade";
    public static final String COLUMN_SKIN_GRADE = "skin_grade";
    public static final String COLUMN_MED_NAME = "med_name";
    public static final String COLUMN_MED_QUANTITY = "med_quantity";
    public static final String COLUMN_MED_FORM = "med_form";
    public static final String COLUMN_MED_NOTES = "med_notes";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_MINUTE = "minute";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    
    // Database creation sql statement
    private static  final String DATABASE_CREATE = "create table "
        + TABLE_NAME + "(" + COLUMN_ID
        + " integer primary key autoincrement, "
        + COLUMN_EYES_GRADE + " integer"
        + COLUMN_NOSE_GRADE + " integer"
        + COLUMN_MOUTH_GRADE + " integer"
        + COLUMN_BRONCHIA_GRADE + " integer"
        + COLUMN_HANDS_GRADE + " integer"
        + COLUMN_SKIN_GRADE + " integer"
        + COLUMN_MED_NAME + " text"
        + COLUMN_MED_QUANTITY + " text"
        + COLUMN_MED_FORM + " text"
        + COLUMN_MED_NOTES + " text"
        + COLUMN_YEAR + " integer"
        + COLUMN_MONTH + " integer"
        + COLUMN_DAY + " integer"
        + COLUMN_HOUR + " integer"
        + COLUMN_MINUTE + " integer"
        + COLUMN_LONGITUDE + " text"
        + COLUMN_LATITUDE + " text);";
    
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**Called when database does not exist*/
	@Override
	public void onCreate(SQLiteDatabase db) {
        // SQL statement to create table
		
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	  }
	
    /**
     * CRUD operations
     */
    
	public void addAll(int eyes_grade, int nose_grade, int mouth_grade, int bronchia_grade, int hands_grade, int skin_grade, String name, String quantity, String form, String notes, int year, int month, int day, int hour, int minute){
		
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        //values.put(name, value);
       
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_NAME, null);
        
        //Symptoms
        if(cursor.getColumnIndex(COLUMN_EYES_GRADE) == -1)
        	db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_EYES_GRADE + " integer");
        values.put(MySQLiteHelper.COLUMN_EYES_GRADE, eyes_grade);
        
        if(cursor.getColumnIndex(MySQLiteHelper.COLUMN_NOSE_GRADE) == -1)
        	db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NOSE_GRADE + " integer");
        values.put(MySQLiteHelper.COLUMN_NOSE_GRADE, nose_grade);
        
        if(cursor.getColumnIndex(MySQLiteHelper.COLUMN_MOUTH_GRADE) == -1)
        	db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_MOUTH_GRADE + " integer");
        values.put(MySQLiteHelper.COLUMN_MOUTH_GRADE, mouth_grade);
        
        if(cursor.getColumnIndex(MySQLiteHelper.COLUMN_BRONCHIA_GRADE) == -1)
        	db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_BRONCHIA_GRADE + " integer");
        values.put(MySQLiteHelper.COLUMN_BRONCHIA_GRADE, bronchia_grade);
        
        if(cursor.getColumnIndex(MySQLiteHelper.COLUMN_HANDS_GRADE) == -1)
        	db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_HANDS_GRADE + " integer");
        values.put(MySQLiteHelper.COLUMN_HANDS_GRADE, hands_grade);
        
        if(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SKIN_GRADE) == -1)
        	db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_SKIN_GRADE + " integer");
        values.put(MySQLiteHelper.COLUMN_SKIN_GRADE, skin_grade);
        
        //Medicine
        if(cursor.getColumnIndex(COLUMN_MED_NAME) == -1)
        	db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_MED_NAME + " text");
        values.put(MySQLiteHelper.COLUMN_MED_NAME, name);
        
        if(cursor.getColumnIndex(COLUMN_MED_QUANTITY) == -1)
        	db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_MED_QUANTITY + " text");
        values.put(MySQLiteHelper.COLUMN_MED_QUANTITY, quantity);
        
        if(cursor.getColumnIndex(COLUMN_MED_FORM) == -1)
        	db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_MED_FORM + " text");
        values.put(MySQLiteHelper.COLUMN_MED_FORM, form);
        
        if(cursor.getColumnIndex(COLUMN_MED_NOTES) == -1)
        	db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_MED_NOTES + " text");
        values.put(MySQLiteHelper.COLUMN_MED_NOTES, notes);
        
        //Date
        if(cursor.getColumnIndex(COLUMN_YEAR) == -1)
        	db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_YEAR + " integer");
        values.put(MySQLiteHelper.COLUMN_YEAR, year);
        
        if(cursor.getColumnIndex(COLUMN_MONTH) == -1)
        	db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_MONTH + " integer");
        values.put(MySQLiteHelper.COLUMN_MONTH, month);
        
        if(cursor.getColumnIndex(COLUMN_DAY) == -1)
        	db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_DAY + " integer");
        values.put(MySQLiteHelper.COLUMN_DAY, day);
        
        if(cursor.getColumnIndex(COLUMN_HOUR) == -1)
        	db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_HOUR + " integer");
        values.put(MySQLiteHelper.COLUMN_HOUR, hour);
        
        if(cursor.getColumnIndex(COLUMN_MINUTE) == -1)
        	db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_MINUTE + " integer");
        values.put(MySQLiteHelper.COLUMN_MINUTE, minute);
        
        // 3. insert() if exists, else update()
        db.insert(TABLE_NAME, 	// table
        		null, 			//nullColumnHack
                values); 		// key/value -> keys = column names/ values = column values
      
        // 4. close
        db.close(); 
    }


}
