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
import ru.romanov.schedule.adapters.SubjectAdapter;
import ru.romanov.schedule.adapters.SubjectsListAdapter;
import ru.romanov.schedule.utils.MySubject;
import ru.romanov.schedule.utils.StringConstants;
import ru.romanov.schedule.utils.Subject;
import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ScheduleListActivity extends Activity {

	private ArrayList<ArrayList<Subject>> subjList;
	private ArrayList<String> group;
	private HashMap<String, String> ruDays = new HashMap<String, String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity_layout);
		
		ExpandableListView scheduleList = (ExpandableListView) findViewById(R.id.scheduleExpandableList);
		
		loadData();
		
		SubjectsListAdapter subjectListAdapter = new SubjectsListAdapter(this, group, subjList);
		scheduleList.setAdapter(subjectListAdapter);
		
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
	}
	
	private void loadData() {
		group = new ArrayList<String>();
		subjList = new ArrayList<ArrayList<Subject>>();
		
		SubjectAdapter sa = new SubjectAdapter(this);
		
		Calendar calend = Calendar.getInstance(Locale.FRANCE);
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		
		for (int i = 0; i < 7; ++i) {
			calend.set(Calendar.DAY_OF_WEEK, calend.getFirstDayOfWeek() + i);
			String disp_name = calend.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
			group.add(sdf.format(calend.getTime()));
			
			subjList.add(sa.getAll());
		}
	}


}
