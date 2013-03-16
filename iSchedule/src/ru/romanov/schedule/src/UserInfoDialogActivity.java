package ru.romanov.schedule.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.romanov.schedule.R;
import ru.romanov.schedule.adapters.UserAdapter;
import ru.romanov.schedule.utils.StringConstants;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class UserInfoDialogActivity extends Activity implements OnClickListener{

	
	private Map<String, String> user = new HashMap<String, String>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_dialog_layout);
		
		Context context = getApplicationContext();
		UserAdapter ua = new UserAdapter(context);
		user = ua.getUserAsMap(); 
		
	}
	@Override
	protected void onStart() {
		final ArrayList<String> attr_list = new ArrayList<String>();
		ListView user_attrs = (ListView) findViewById(R.id.info_dialog_list);
		for (String key : user.keySet()) {
			attr_list.add(key + ": " + user.get(key));
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, attr_list);
		user_attrs.setAdapter(adapter);
		super.onStart();
	}
	@Override
	public void onClick(View arg0) {
		finish();
		
	}
}
