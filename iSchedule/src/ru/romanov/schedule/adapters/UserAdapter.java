package ru.romanov.schedule.adapters;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserAdapter extends SQLiteOpenHelper{
	public static final String DB_NAME = "schedule_db";
	private static final int DB_VERSION = 1;
	
	public static final String TABLE_NAME = "T_USER";
	public static final String NAME = "name";
	public static final String LOGIN = "login";
	public static final String EMAIL = "email";
	public static final String PHONE = "phone";
	private static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( _id integer primary key autoincrement, "
		      + LOGIN + " TEXT, " + NAME + " TEXT, " + EMAIL + " TEXT, " + PHONE + " TEXT)";
	
	//private static final String QUERY = "select * from " + TABLE_NAME + " where 1=1";
	
	public UserAdapter(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public Map<String, String> getUserAsMap() {
		Map<String, String> user = new HashMap<String, String>();
		String[] columnToTake = { NAME, LOGIN, EMAIL, PHONE };
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(true, TABLE_NAME, columnToTake, null, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			String[] names = cursor.getColumnNames();
			for (int i = 0; i < names.length; ++i) {
				user.put(names[i], cursor.getString(i));
			}
		}
		db.close();
		return user;
	}
	
	public void saveUser(String name, String login, String email, String phone) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from " + TABLE_NAME + " where 1=1");
        ContentValues cv = new ContentValues();
        cv.put(UserAdapter.LOGIN, login);
        cv.put(UserAdapter.NAME, name);
        cv.put(UserAdapter.EMAIL, email);
        cv.put(UserAdapter.PHONE, phone);
        long count = db.insert(UserAdapter.TABLE_NAME, null, cv);
        db.close();
	}
}
