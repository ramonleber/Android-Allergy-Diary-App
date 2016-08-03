package com.example.allergietb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Address;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVWriter;

public class MainActivity extends ActionBarActivity {
 	
	boolean dateCalled = false;
	
	int itemId = 2131034239;
	int eyes_grade = 0; 
	int nose_grade = 0; 
	int mouth_grade =0; 
	int bronchia_grade = 0;
	int hands_grade = 0; 
	int skin_grade = 0;
	
	int year = 0; 
	int month = 0; 
	int day = 0; 
	int hour = 0;
	int minute = 0;
	
	String medicine_name_str = "";
	String medicine_quantity_str = "";
	String medicine_form_str = ""; 
	String medicine_notes_str = "";
	
	Calendar calendar = Calendar.getInstance();
	
	/**LocationListener declarations*/
	private LocationManager locationManager=null;  
	private LocationListener locationListener=null;
	 //boolean is_called_by_position = false;
	private MySQLiteHelper db;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /**Database declarations*/
        db = new MySQLiteHelper(this);
        db.getWritableDatabase();
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	 locationManager = (LocationManager)   
    			  getSystemService(Context.LOCATION_SERVICE);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.layout.menu, menu);
        //MenuInflater menuInflater = getMenuInflater();
        //menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /** Handle action bar item clicks here. The action bar will
         automatically handle clicks on the Home/Up button, so long
         as you specify a parent activity in AndroidManifest.xml.
        */
    	
    	if(itemId == R.id.menu_symptoms){
    		saveSymptoms();
    	}
    	
    	else if(itemId == R.id.menu_medicine){
    		saveMedicine();
    	}
    	
    	else if(itemId == R.id.menu_date){
    		saveDate();
    	}
    	
    	else if(itemId == R.id.menu_position){
    		savePosition();
    	}
    	
    	switch (item.getItemId())
        {
        case R.id.menu_symptoms:
            // Single menu item is selected do something
            // Ex: launching new activity/screen or show alert message
            Toast.makeText(MainActivity.this, "Beschwerden is Selected", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.fragment_main);
            setSymptomsValue();
            itemId = R.id.menu_symptoms;
            return true;
 
        case R.id.menu_medicine:
            Toast.makeText(MainActivity.this, "Medikamente is Selected", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.fragment_medicine);
            setMedicineValue();
            itemId = R.id.menu_medicine;
            return true;
            
        case R.id.menu_date:
            Toast.makeText(MainActivity.this, "Datum / Uhrzeit is Selected", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.fragment_date_time);
            itemId = R.id.menu_date;
            return true;
 
        case R.id.menu_position:
            Toast.makeText(MainActivity.this, "Position is Selected", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.fragment_position);
            itemId = R.id.menu_position;
        	getLocation();
            return true;
 
        case R.id.menu_export:
            Toast.makeText(MainActivity.this, "Datei-Export is Selected", Toast.LENGTH_SHORT).show();
            export();
            return true;
            
        case R.id.menu_help:
            Toast.makeText(MainActivity.this, "Hilfe is Selected", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.fragment_help);
            return true;
            
        case R.id.menu_save:
            Toast.makeText(MainActivity.this, "Speichern is Selected", Toast.LENGTH_SHORT).show();
            
            if(!dateCalled){
            	year = calendar.get(Calendar.YEAR);
            	month = calendar.get(Calendar.MONTH) + 1;
            	day = calendar.get(Calendar.DAY_OF_MONTH);
            	hour = calendar.get(Calendar.HOUR_OF_DAY);
            	minute = calendar.get(Calendar.MINUTE);
            }
            
            saveAll();
            return true;
            
        default:
            return super.onOptionsItemSelected(item);
        }
    } 
    
    private void setMedicineValue() {
    	EditText medicine_name = (EditText)findViewById(R.id.medicine_name_editText);
    	medicine_name.setText(medicine_name_str);
    	
    	EditText medicine_quantity = (EditText)findViewById(R.id.medicine_quantity_editText);
    	medicine_quantity.setText(medicine_quantity_str);
    	
    	EditText medicine_form = (EditText)findViewById(R.id.medicine_form_editText);
    	medicine_form.setText(medicine_form_str);
    	
    	EditText medicine_notes = (EditText)findViewById(R.id.medicine_notes_editText);
    	medicine_notes.setText(medicine_notes_str);
		
	}


	private void setSymptomsValue() {
    	RadioGroup rg_eyes = (RadioGroup)findViewById(R.id.main_eyes_radioGroup);
    	RadioGroup rg_nose = (RadioGroup)findViewById(R.id.main_nose_radioGroup);
    	RadioGroup rg_mouth = (RadioGroup)findViewById(R.id.main_mouth_radioGroup);
    	RadioGroup rg_bronchia = (RadioGroup)findViewById(R.id.main_bronchia_radioGroup);
    	RadioGroup rg_hands = (RadioGroup)findViewById(R.id.main_hands_radioGroup);
    	RadioGroup rg_skin = (RadioGroup)findViewById(R.id.main_skin_radioGroup);
    	
    	if(eyes_grade == 1)rg_eyes.check(R.id.main_eyes_radio1);
    	else if(eyes_grade == 2)rg_eyes.check(R.id.main_eyes_radio2);
    	else if(eyes_grade == 3)rg_eyes.check(R.id.main_eyes_radio3);
    	else if(eyes_grade == 4)rg_eyes.check(R.id.main_eyes_radio4);
    	else if(eyes_grade == 5)rg_eyes.check(R.id.main_eyes_radio5);
    	
    	if(nose_grade == 1)rg_nose.check(R.id.main_nose_radio1);
    	else if(nose_grade == 2)rg_nose.check(R.id.main_nose_radio2);
    	else if(nose_grade == 3)rg_nose.check(R.id.main_nose_radio3);
    	else if(nose_grade == 4)rg_nose.check(R.id.main_nose_radio4);
    	else if(nose_grade == 5)rg_nose.check(R.id.main_nose_radio5);
    	
    	if(mouth_grade == 1)rg_mouth.check(R.id.main_mouth_radio1);
    	else if(mouth_grade == 2)rg_mouth.check(R.id.main_mouth_radio2);
    	else if(mouth_grade == 3)rg_mouth.check(R.id.main_mouth_radio3);
    	else if(mouth_grade == 4)rg_mouth.check(R.id.main_mouth_radio4);
    	else if(mouth_grade == 5)rg_mouth.check(R.id.main_mouth_radio5);
    	
    	if(bronchia_grade == 1)rg_bronchia.check(R.id.main_bronchia_radio1);
    	else if(bronchia_grade == 2)rg_bronchia.check(R.id.main_bronchia_radio2);
    	else if(bronchia_grade == 3)rg_bronchia.check(R.id.main_bronchia_radio3);
    	else if(bronchia_grade == 4)rg_bronchia.check(R.id.main_bronchia_radio4);
    	else if(bronchia_grade == 5)rg_bronchia.check(R.id.main_bronchia_radio5);
    	
    	if(hands_grade == 1)rg_hands.check(R.id.main_hands_radio1);
    	else if(hands_grade == 2)rg_hands.check(R.id.main_hands_radio2);
    	else if(hands_grade == 3)rg_hands.check(R.id.main_hands_radio3);
    	else if(hands_grade == 4)rg_hands.check(R.id.main_hands_radio4);
    	else if(hands_grade == 5)rg_hands.check(R.id.main_hands_radio5);
    	
    	if(skin_grade == 1)rg_skin.check(R.id.main_skin_radio1);
    	else if(skin_grade == 2)rg_skin.check(R.id.main_skin_radio2);
    	else if(skin_grade == 3)rg_skin.check(R.id.main_skin_radio3);
    	else if(skin_grade == 4)rg_skin.check(R.id.main_skin_radio4);
    	else if(skin_grade == 5)rg_skin.check(R.id.main_skin_radio5);
	}


	private void saveAll() {
    	db.addAll(eyes_grade, nose_grade, mouth_grade, bronchia_grade, hands_grade, skin_grade, medicine_name_str, medicine_quantity_str, medicine_form_str, medicine_notes_str, year, month, day, hour, minute);
    	
	}


    public void saveSymptoms() {
    	
    	RadioGroup rg_eyes = (RadioGroup)findViewById(R.id.main_eyes_radioGroup);
    	RadioGroup rg_nose = (RadioGroup)findViewById(R.id.main_nose_radioGroup);
    	RadioGroup rg_mouth = (RadioGroup)findViewById(R.id.main_mouth_radioGroup);
    	RadioGroup rg_bronchia = (RadioGroup)findViewById(R.id.main_bronchia_radioGroup);
    	RadioGroup rg_hands = (RadioGroup)findViewById(R.id.main_hands_radioGroup);
    	RadioGroup rg_skin = (RadioGroup)findViewById(R.id.main_skin_radioGroup);
    	
    	if(rg_eyes.getCheckedRadioButtonId() == R.id.main_eyes_radio1)eyes_grade = 1;
    	else if(rg_eyes.getCheckedRadioButtonId() == R.id.main_eyes_radio2)eyes_grade = 2;
    	else if(rg_eyes.getCheckedRadioButtonId() == R.id.main_eyes_radio3)eyes_grade = 3;
    	else if(rg_eyes.getCheckedRadioButtonId() == R.id.main_eyes_radio4)eyes_grade = 4;
    	else if(rg_eyes.getCheckedRadioButtonId() == R.id.main_eyes_radio5)eyes_grade = 5;
    	
    	if(rg_nose.getCheckedRadioButtonId() == R.id.main_nose_radio1)nose_grade = 1;
    	else if(rg_nose.getCheckedRadioButtonId() == R.id.main_nose_radio2)nose_grade = 2;
    	else if(rg_nose.getCheckedRadioButtonId() == R.id.main_nose_radio3)nose_grade = 3;
    	else if(rg_nose.getCheckedRadioButtonId() == R.id.main_nose_radio4)nose_grade = 4;
    	else if(rg_nose.getCheckedRadioButtonId() == R.id.main_nose_radio5)nose_grade = 5;
    	
    	if(rg_mouth.getCheckedRadioButtonId() == R.id.main_mouth_radio1)mouth_grade = 1;
    	else if(rg_mouth.getCheckedRadioButtonId() == R.id.main_mouth_radio2)mouth_grade = 2;
    	else if(rg_mouth.getCheckedRadioButtonId() == R.id.main_mouth_radio3)mouth_grade = 3;
    	else if(rg_mouth.getCheckedRadioButtonId() == R.id.main_mouth_radio4)mouth_grade = 4;
    	else if(rg_mouth.getCheckedRadioButtonId() == R.id.main_mouth_radio5)mouth_grade = 5;
    	
    	if(rg_bronchia.getCheckedRadioButtonId() == R.id.main_bronchia_radio1)bronchia_grade = 1;
    	else if(rg_bronchia.getCheckedRadioButtonId() == R.id.main_bronchia_radio2)bronchia_grade = 2;
    	else if(rg_bronchia.getCheckedRadioButtonId() == R.id.main_bronchia_radio3)bronchia_grade = 3;
    	else if(rg_bronchia.getCheckedRadioButtonId() == R.id.main_bronchia_radio4)bronchia_grade = 4;
    	else if(rg_bronchia.getCheckedRadioButtonId() == R.id.main_bronchia_radio5)bronchia_grade = 5;
    	
    	if(rg_hands.getCheckedRadioButtonId() == R.id.main_hands_radio1)hands_grade = 1;
    	else if(rg_hands.getCheckedRadioButtonId() == R.id.main_hands_radio2)hands_grade = 2;
    	else if(rg_hands.getCheckedRadioButtonId() == R.id.main_hands_radio3)hands_grade = 3;
    	else if(rg_hands.getCheckedRadioButtonId() == R.id.main_hands_radio4)hands_grade = 4;
    	else if(rg_hands.getCheckedRadioButtonId() == R.id.main_hands_radio5)hands_grade = 5;
    	
    	if(rg_skin.getCheckedRadioButtonId() == R.id.main_skin_radio1)skin_grade = 1;
    	else if(rg_skin.getCheckedRadioButtonId() == R.id.main_skin_radio2)skin_grade = 2;
    	else if(rg_skin.getCheckedRadioButtonId() == R.id.main_skin_radio3)skin_grade = 3;
    	else if(rg_skin.getCheckedRadioButtonId() == R.id.main_skin_radio4)skin_grade = 4;
    	else if(rg_skin.getCheckedRadioButtonId() == R.id.main_skin_radio5)skin_grade = 5;
    }

    public void saveMedicine() {
    	EditText medicine_name = (EditText)findViewById(R.id.medicine_name_editText);
    	medicine_name_str = medicine_name.getText().toString();
    	
    	EditText medicine_quantity = (EditText)findViewById(R.id.medicine_quantity_editText);
    	medicine_quantity_str = medicine_quantity.getText().toString();
    	
    	EditText medicine_form = (EditText)findViewById(R.id.medicine_form_editText);
    	medicine_form_str = medicine_form.getText().toString();
    	
    	EditText medicine_notes = (EditText)findViewById(R.id.medicine_notes_editText);
    	medicine_notes_str = medicine_notes.getText().toString();
    }
    
    public void saveDate() {
    	
    	DatePicker datePicker;
    	TimePicker timePicker;
    	
        datePicker = (DatePicker) findViewById(R.id.datePicker1);
        timePicker = (TimePicker) findViewById(R.id.timePicker1);
        
        year = datePicker.getYear();
        month = datePicker.getMonth()+1;
        day = datePicker.getDayOfMonth();
        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();
        
        dateCalled = true;
    }
    
    
    public void savePosition() {

    	
    }
    
    public void getLocation(){
    
    	   locationListener = new MyLocationListener();  
    	   locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10,locationListener);
    	   
    	   
    }
    
    
    public void export(){
    	
        /**File-Export declarations*/
        //File dbFile=getDatabasePath("/data/data/com-example.allergietb/databases/Allergie-Beschwerdebuch.db");
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        
        if (!exportDir.exists()) 
        {
            exportDir.mkdirs();
        }
        
        File file = new File(exportDir, "Allergie-Beschwerdebuch.csv");
        try 
        {
            file.createNewFile();
            //String csv = "/storage/sdcard0/csvname.csv";
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            //String bla = "text here";
            //csvWrite.writeNext(bla);
            SQLiteDatabase dbsql = db.getReadableDatabase();
            Cursor curCSV = dbsql.rawQuery("SELECT * FROM " +MySQLiteHelper.TABLE_NAME, null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
               //Which column you want to export
                String arrStr[] ={	curCSV.getString(0),
                					curCSV.getString(1), 
			    					curCSV.getString(2), 
			    					curCSV.getString(3), 
			    					curCSV.getString(4),
			    					curCSV.getString(5), 
			    					curCSV.getString(6), 
			    					curCSV.getString(7), 
			    					curCSV.getString(8), 
			    					curCSV.getString(9),
			    					curCSV.getString(10), 
			    					curCSV.getString(11),
			    					curCSV.getString(12), 
			    					curCSV.getString(13),
			    					curCSV.getString(14), 
			    					curCSV.getString(15),
			    					curCSV.getString(16)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }
    
    /*----------Listener class to get coordinates ------------- */  
    private class MyLocationListener implements LocationListener {  
        @Override  
        public void onLocationChanged(Location loc) {  
           
        TextView longitude_textView, latitude_textView;
        	     
        String longitude = "" +loc.getLongitude();      
        String latitude = "" +loc.getLatitude();
         
        longitude_textView = (TextView)findViewById(R.id.position_longitude_TextView);
        longitude_textView.setText(longitude);
        latitude_textView = (TextView)findViewById(R.id.position_latitude_TextView);
        latitude_textView.setText(latitude); 
        }
           
		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
    }
    
    
    
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}

