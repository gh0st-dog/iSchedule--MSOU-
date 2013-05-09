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
import android.widget.SimpleAdapter;
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
		super.onStart();
		TextView name = (TextView) findViewById(R.id.nameInfo);
		TextView login = (TextView) findViewById(R.id.loginInfo);
		TextView phone = (TextView) findViewById(R.id.phoneInfo);
		TextView email = (TextView) findViewById(R.id.emailInfo);
		
		name.setText(user.get("name"));
		login.setText(user.get("login"));
		phone.setText(user.get("phone"));
		email.setText(user.get("email"));
	}
		
	
	@Override
	public void onClick(View arg0) {
		finish();
		
	}
}
