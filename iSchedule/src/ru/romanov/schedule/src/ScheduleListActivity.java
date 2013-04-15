package ru.romanov.schedule.src;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import ru.romanov.schedule.R;
import ru.romanov.schedule.utils.MySubject;
import ru.romanov.schedule.utils.StringConstants;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ScheduleListActivity extends ListActivity {

	private ArrayList <MySubject> subjList;
	private HashMap<String, String> ruDays = new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity_layout);
		
		ruDays.put("Mon", "Пн");
		ruDays.put("Tue", "Вт");
		ruDays.put("Wed", "Ср");
		ruDays.put("Thu", "Чт");
		ruDays.put("Fri", "Пт");
		ruDays.put("Sat", "Сб");
		ruDays.put("Sun", "Вс");
	}

	@Override
	protected void onStart() {
		super.onStart();
		ListView listOfSchedule = (ListView) findViewById(android.R.id.list);
		ArrayList<HashMap<String, String>> currentWeek = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> cur_day = null;
		Calendar calend = Calendar.getInstance(Locale.FRANCE);
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		
		for (int i = 0; i < 7; ++i) {
			cur_day = new HashMap<String, String>();
			calend.set(Calendar.DAY_OF_WEEK, calend.getFirstDayOfWeek() + i);
			String disp_name = calend.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
			
			cur_day.put("date", sdf.format(calend.getTime()));
			cur_day.put("day", ruDays.get(disp_name));
			currentWeek.add(cur_day);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(this,
												currentWeek,
												R.layout.schedule_list_item, new String[] {
												"date", "day"}, 
												new int[] { 
												R.id.schedule_list_item_date, 
												R.id.schedule_list_item_weekday});
		listOfSchedule.setAdapter(adapter);
	}

	
	private void loadSchedule() throws JSONException, ParseException {
		SharedPreferences sherPref = getSharedPreferences(StringConstants.MY_SCHEDULE, MODE_PRIVATE);
		Map<String,String> myMap = (Map<String, String>) sherPref.getAll();
		this.subjList = new ArrayList<MySubject>(myMap.size());
		for (String key : myMap.keySet()) {
			this.subjList.add(new MySubject(key, new JSONObject(myMap.get(key))));
		}
		
	}


}
