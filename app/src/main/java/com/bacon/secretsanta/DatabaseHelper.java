package com.bacon.secretsanta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jason on 2016-09-19.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

	/* Database version */
	private static final int DATABASE_VERSION = 1;
    /* Database name */
	private static final String DATABASE_NAME = "SantaStorage.db";
    /* Table name */
	private static final String TABLE_NAME = "people_table";

    /* Table column names */
    private static final String KEY_ID = "ID";
    private static final String KEY_NAME = "Name";
    private static final String KEY_METHOD = "ContactMethod";
    private static final String KEY_INFORMATION = "ContactInformation";
	private static final String KEY_GIFTEE = "Giftee";

    /* Data types */
    private static final String PRIMARY = " INTEGER PRIMARY KEY,";
    private static final String TEXT_TYPE = " TEXT,";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
		String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + "INTEGER PRIMARY KEY," + 
								KEY_NAME + " TEXT," + KEY_METHOD + " TEXT," +
								KEY_INFORMATION + " TEXT," + KEY_GIFTEE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int one, int two){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Breaks down a person object and inserts it into the database
     * @param person The person object
     * @return Whether or not the insertion succeeded
     */
    public boolean insertRecord(Person person){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, person.getName());
        values.put(KEY_METHOD, person.getContactMethod());
        values.put(KEY_INFORMATION, person.getContactInformation());
		values.put(KEY_GIFTEE, person.getGiftee());
        long result = db.insert(TABLE_NAME, null, values);
		db.close();
        return result != -1;
    }

    /**
     * Returns all of the persons in the database in an array
     * @return All the persons in the database
     */
    public Person[] getAllRecords(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        int count = cursor.getCount();
		db.close();
        Person[] personList = new Person[count];
		if(cursor.moveToFirst()) {
            do {
                Person person = new Person();
                person.setName(cursor.getString(1));
                person.setContactMethod(cursor.getString(2));
                person.setContactInformation(cursor.getString(3));
                person.setGiftee(cursor.getString(4));
                personList[personList.length - count] = person;
                count--;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return personList;
    }

    /**
     * Updates the entry of the specified person
     * @param person The person object
     * @return whether or not the update operation succeeded
     */
	public boolean updateRecord(Person person){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, person.getName());
        values.put(KEY_METHOD, person.getContactMethod());
        values.put(KEY_INFORMATION, person.getContactInformation());
		values.put(KEY_GIFTEE, person.getGiftee());
		long result = db.update(TABLE_NAME, values, KEY_NAME + "= ?", new String[] {person.getName()});
		return result != -1;
	}

    /**
     * Deletes the record of the specified person
     * @param person The person object
     */
	public void deleteRecord(Person person){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, KEY_NAME + "= ?", new String[] {person.getName()});
		db.close();
	}

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
