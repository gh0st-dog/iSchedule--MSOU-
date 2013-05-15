package ru.romanov.schedule.src;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import ru.romanov.schedule.R;
import ru.romanov.schedule.utils.StringConstants;


public class MenuSettingsActivity extends Activity implements OnClickListener {

	private String host;
	private String port;
	SharedPreferences mSharedPreferences;
	static EditText hostText;
	static EditText portText;
	Button saveButton;
	 
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_menu);
		mSharedPreferences = getSharedPreferences(
				StringConstants.SCHEDULE_SHARED_PREFERENCES, MODE_PRIVATE);
		this.host = mSharedPreferences.getString("host", "");
		this.port = mSharedPreferences.getString("port", "");
		hostText = (EditText) findViewById(R.id.hostText);
		portText = (EditText) findViewById(R.id.portText);
		saveButton = (Button) findViewById(R.id.saveSettingsBtn);
	}
	
	@Override
	protected void onStart() {
		hostText.setText(host);
		portText.setText(port);
		
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = mSharedPreferences.edit();
				editor.putString("host", hostText.getText().toString());
				editor.putString("port", portText.getText().toString());
				editor.commit();
				finish();
			}
		});
		
		super.onStart();
	}
}
