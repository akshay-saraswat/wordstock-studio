package inc.asharfi.wordstock;

import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class TestDatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "testsManager";
 
    // Words table name
    private static final String TABLE_TESTS = "tests";
 
    // Words Table Columns names
    private static final String KEY_DATE = "date";
    private static final String KEY_SPAN = "span";
    private static final String KEY_TYPE = "test_type";
    private static final String KEY_TOTAL_COUNT = "total_count";
    private static final String KEY_ATTEMPT_COUNT = "attempt_count";
    private static final String KEY_CORRECT_COUNT = "correct_count";
    private static final String KEY_CORRECT_WORDS = "correct_words";
    private static final String KEY_INCORRECT_WORDS = "incorrect_words";
 
    public TestDatabaseHandler(Context context) {
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TESTS + "("
                + KEY_DATE + " BLOB PRIMARY KEY,"
        		+ KEY_SPAN + " BLOB,"
        		+ KEY_TYPE + " TEXT,"
        		+ KEY_TOTAL_COUNT + " TEXT,"
        		+ KEY_ATTEMPT_COUNT + " TEXT,"
        		+ KEY_CORRECT_COUNT + " TEXT,"
        		+ KEY_CORRECT_WORDS + " TEXT,"
        		+ KEY_INCORRECT_WORDS + " TEXT"
        		+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTS);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new test
    void addTest(Test test) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, test.getDate());
        values.put(KEY_SPAN, test.getSpan());
        values.put(KEY_TYPE, test.getType());
        values.put(KEY_TOTAL_COUNT, test.getTotalCount());
        values.put(KEY_ATTEMPT_COUNT, test.getAttemptCount());
        values.put(KEY_CORRECT_COUNT, test.getCorrectCount());
        values.put(KEY_CORRECT_WORDS, test.getCorrectWords());
        values.put(KEY_INCORRECT_WORDS, test.getIncorrectWords());
 
        // Inserting Row
        db.insert(TABLE_TESTS, null, values);
        while (db.inTransaction());
        db.close(); // Closing database connection
    }
 
    // Getting single test
    Test getSingleByString(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_TESTS, null,
        						KEY_DATE + "=?", new String[] { date }, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
			Test mTest = new Test(  cursor.getString(0), cursor.getString(1), cursor.getString(2),
									Integer.parseInt(cursor.getString(3)),
									Integer.parseInt(cursor.getString(4)),
									Integer.parseInt(cursor.getString(5)),
									cursor.getString(6), cursor.getString(7));
			
			cursor.close();
			db.close();
			
			// return word
			return mTest;
        } else {
        	return null;
        }
    }

    // Getting single test
    Test getSingleByIndex(int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_TESTS + " LIMIT 1 OFFSET " + index;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Test mTest = new Test(  cursor.getString(0), cursor.getString(1), cursor.getString(2),
									Integer.parseInt(cursor.getString(3)),
									Integer.parseInt(cursor.getString(4)),
									Integer.parseInt(cursor.getString(5)),
									cursor.getString(6), cursor.getString(7));
			
			cursor.close();
			db.close();
			
			// return word
			return mTest;
        } else {
        	return null;
        }
    }
     
    // Getting complete database
    public List<Test> getAll() {
        List<Test> testList = new ArrayList<Test>();
        String selectQuery = "SELECT  * FROM " + TABLE_TESTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Test mTest = new Test(  cursor.getString(0), cursor.getString(1), cursor.getString(2),
										Integer.parseInt(cursor.getString(3)),
										Integer.parseInt(cursor.getString(4)),
										Integer.parseInt(cursor.getString(5)),
										cursor.getString(6), cursor.getString(7));

            	testList.add(mTest);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return word list
        return testList;
    }

    // Getting tests Count
    public int getAllTestsCount() {
    	int count;
        String countQuery = "SELECT  * FROM " + TABLE_TESTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    public String getDBPath() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	return db.getPath();
    }
}
