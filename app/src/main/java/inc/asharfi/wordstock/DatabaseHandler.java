package inc.asharfi.wordstock;

import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
 
    /* All Static variables */
    /* Database Version */
    private static final int DATABASE_VERSION = 1;
 
    /* Database Name */
    private static final String DATABASE_NAME = "wordsManager";
 
    /* Words table name */
    private static final String TABLE_WORDS = "words";
 
    /* Words Table Columns names */
    private static final String KEY_WORD = "word";
    private static final String KEY_MEANINGS = "meanings";
    private static final String KEY_ANTONYMS = "antonyms";
    private static final String KEY_SYNONYMS = "synonyms";
    private static final String KEY_EXAMPLES = "examples";
    private static final String KEY_ETYMOLOGY = "etymology";
    private static final String KEY_SENTENCES = "sentences";
    private static final String KEY_TYPES = "types";
    private static final String KEY_LISTS = "lists";
    private static final String KEY_SHORT_DEFINITIONS = "short_definitions";
    private static final String KEY_MNEMONICS = "mnemonics";
    private static final String KEY_STATUS = "status";
    private static final String KEY_FAVORITES = "favorites";
    private Context mContext = null;
    private SQLiteDatabase dataBase = null;
 
    public DatabaseHandler(Context context) {
        /*super(context, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        	    + File.separator + DATABASE_NAME, null, DATABASE_VERSION);*/
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        dataBase = this.getWritableDatabase();
    }
 
    /* Creating Tables */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_WORDS + "("
                + KEY_WORD + " TEXT PRIMARY KEY,"
        		+ KEY_MEANINGS + " BLOB,"
        		+ KEY_ANTONYMS + " TEXT,"
        		+ KEY_SYNONYMS + " TEXT,"
        		+ KEY_EXAMPLES + " BLOB,"
        		+ KEY_ETYMOLOGY + " BLOB,"
                + KEY_SENTENCES + " BLOB,"
                + KEY_TYPES + " BLOB,"
                + KEY_LISTS + " BLOB,"
                + KEY_SHORT_DEFINITIONS + " BLOB,"
                + KEY_MNEMONICS + " BLOB,"
                + KEY_STATUS + " BLOB,"
                + KEY_FAVORITES + " TEXT"
        		+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    /* Upgrading database */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* Drop older table if existed */
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
 
        /* Create tables again */
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    /* Adding new word */
    void addWord(Word word) {
 
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, word.getWord());
        values.put(KEY_MEANINGS, word.getMeanings());
        values.put(KEY_ANTONYMS, word.getAntonyms());
        values.put(KEY_SYNONYMS, word.getSynonyms());
        values.put(KEY_EXAMPLES, word.getExamples());
        values.put(KEY_ETYMOLOGY, word.getEtymology());
        values.put(KEY_SENTENCES, word.getSentences());
        values.put(KEY_TYPES, word.getTypes());
        values.put(KEY_LISTS, word.getLists());
        values.put(KEY_SHORT_DEFINITIONS, word.getShortDefinitions());
        values.put(KEY_MNEMONICS, word.getMnemonics());
        values.put(KEY_STATUS, word.getStatus());
        values.put(KEY_FAVORITES, word.getFavorite());
 
        /* Inserting Row */
        dataBase.insert(TABLE_WORDS, null, values);
    }
 
    /* Getting single word */
    Word getSingleByString(String word) {
        word = word.replace(mContext.getString(R.string.latin_e), mContext.getString(R.string.glyph));
 
        Cursor cursor = dataBase.query(TABLE_WORDS, new String[] { KEY_WORD, KEY_MEANINGS,
        													 KEY_ANTONYMS, KEY_SYNONYMS,
        													 KEY_EXAMPLES, KEY_ETYMOLOGY,
        													 KEY_SENTENCES, KEY_TYPES,
        													 KEY_LISTS, KEY_SHORT_DEFINITIONS,
        													 KEY_MNEMONICS, KEY_STATUS, KEY_FAVORITES },
        						KEY_WORD + "=?", new String[] { word }, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
			Word mWord = new Word(  cursor.getString(0), cursor.getString(1), cursor.getString(2),
									cursor.getString(3), cursor.getString(4), cursor.getString(5),
									cursor.getString(6), cursor.getString(7), cursor.getString(8),
									cursor.getString(9), cursor.getString(10), cursor.getString(11),
									cursor.getString(12), mContext);
			
			cursor.close();
            mWord.setFlag("flag");

			/* return word */
			return mWord;
        } else {
        	return null;
        }
    }

    /* Getting single word */
    Word getSingleByIndex(int index) {
        String selectQuery = "SELECT  * FROM " + TABLE_WORDS + " LIMIT 1 OFFSET " + index;
        Cursor cursor = dataBase.rawQuery(selectQuery, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
			Word mWord = new Word(  cursor.getString(0), cursor.getString(1), cursor.getString(2),
									cursor.getString(3), cursor.getString(4), cursor.getString(5),
									cursor.getString(6), cursor.getString(7), cursor.getString(8),
									cursor.getString(9), cursor.getString(10), cursor.getString(11),
									cursor.getString(12), mContext);
			
			cursor.close();
			/* return word */
			return mWord;
        } else {
        	return null;
        }
    }
     
    /* Getting complete database */
    public List<Word> getAll() {
        List<Word> wordList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_WORDS;
        Cursor cursor = dataBase.rawQuery(selectQuery, null);
 
        /* looping through all rows and adding to list  */
        if (cursor.moveToFirst()) {
            do {
            	Word mWord = new Word(  cursor.getString(0), cursor.getString(1), cursor.getString(2),
										cursor.getString(3), cursor.getString(4), cursor.getString(5),
										cursor.getString(6), cursor.getString(7), cursor.getString(8),
										cursor.getString(9), cursor.getString(10), cursor.getString(11),
										cursor.getString(12), mContext);
            	mWord.setFlag("flag");
                wordList.add(mWord);
            } while (cursor.moveToNext());
        }

        cursor.close();

        /* return word list */
        return wordList;
    }

    /* Getting All Words */
    public List<String> getAllWords(int sectionNumber) {
        List<String> wordList = new ArrayList<String>();
        String selectQuery = null;
    	switch(sectionNumber) {
    	case 3:
    		selectQuery = "SELECT  * FROM " + TABLE_WORDS
		    				+ " WHERE " + KEY_SENTENCES + " IS NOT 'nil'";
    		break;
    	case 4:
    		selectQuery = "SELECT  * FROM " + TABLE_WORDS
		    				+ " WHERE " + KEY_SENTENCES + " IS NOT 'nil'"
							+ " AND " + KEY_SYNONYMS + " IS NOT 'nil'";
    		break;
    	default:
        	selectQuery = "SELECT  * FROM " + TABLE_WORDS;
    	}

        Cursor cursor = dataBase.rawQuery(selectQuery, null);
 
        /* looping through all rows and adding to list */
        if (cursor.moveToFirst()) {
            do {
            	String mWord = cursor.getString(0);
                mWord = mWord.replace(mContext.getString(R.string.glyph), mContext.getString(R.string.latin_e));
                wordList.add(mWord);
            } while (cursor.moveToNext());
        }

        cursor.close();

        /* return word list */
        return wordList;
    }

    /* Getting All Words corresponding to an Alphabet */
    public List<String> getAlphaWords(char mChar, int sectionNumber) {
    	return getAlphaWords((CharSequence) (mChar + ""), sectionNumber, false);
    }

    /* Getting All Words corresponding to a character sequence */
    public List<String> getAlphaWords(CharSequence mChar, int sectionNumber, boolean limit) {
        List<String> wordList = new ArrayList<>();
        String selectQuery;
    	switch(sectionNumber) {
    	case 3:
    		selectQuery = "SELECT * FROM " + TABLE_WORDS
    						+ " WHERE " + KEY_WORD + " LIKE '" + mChar + "%'"
    						+ " AND " + KEY_SENTENCES + " IS NOT 'nil'";
    		break;
    	case 4:
    		selectQuery = "SELECT * FROM " + TABLE_WORDS
    						+ " WHERE " + KEY_WORD + " LIKE '" + mChar + "%'"
    						+ " AND " + KEY_SENTENCES + " IS NOT 'nil'"
    						+ " AND " + KEY_SYNONYMS + " IS NOT 'nil'";
    		break;
    	default:
        	selectQuery = "SELECT * FROM " + TABLE_WORDS + " WHERE " + KEY_WORD + " LIKE '" + mChar + "%'";
    	}
    	
    	if (limit)
    		selectQuery += "LIMIT 0,5";

        Cursor cursor = dataBase.rawQuery(selectQuery, null);

        /* looping through all rows and adding to list */
        if (cursor.moveToFirst()) {
            do {
            	String mWord = cursor.getString(0);
                mWord = mWord.replace(mContext.getString(R.string.glyph), mContext.getString(R.string.latin_e));
                wordList.add(mWord);
            } while (cursor.moveToNext());
        }

        cursor.close();

        /* return word list */
        return wordList;
    }

    /* Getting All Words corresponding to a status */
    /*public List<String> getStatusWords(String status, int sectionNumber) {
        List<String> wordList = new ArrayList<String>();
        String selectQuery = null;
    	switch(sectionNumber) {
    	case 3:
    		selectQuery = "SELECT * FROM " + TABLE_WORDS
    						+ " WHERE " + KEY_STATUS + " LIKE '" + status + "'"
    						+ " AND " + KEY_SENTENCES + " IS NOT 'nil'";
    		break;
    	case 4:
    		selectQuery = "SELECT * FROM " + TABLE_WORDS
    						+ " WHERE " + KEY_STATUS + " LIKE '" + status + "'"
    						+ " AND " + KEY_SENTENCES + " IS NOT 'nil'"
    						+ " AND " + KEY_SYNONYMS + " IS NOT 'nil'";
    		break;
    	default:
        	selectQuery = "SELECT * FROM " + TABLE_WORDS + " WHERE " + KEY_STATUS + " LIKE '" + status + "'";
    	}

        Cursor cursor = dataBase.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	String mWord = cursor.getString(0);
                wordList.add(mWord);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return word list
        return wordList;
    }
    */

    /* Getting All Words corresponding to a list tag */
    public List<String> getListsWords(String listTag, int sectionNumber) {
        List<String> wordList = new ArrayList<>();
        String selectQuery;
    	switch(sectionNumber) {
    	case 3:
    		selectQuery = "SELECT * FROM " + TABLE_WORDS
    						+ " WHERE " + KEY_LISTS + " LIKE '%" + listTag + "%'"
    						+ " AND " + KEY_SENTENCES + " IS NOT 'nil'";
    		break;
    	case 4:
    		selectQuery = "SELECT * FROM " + TABLE_WORDS
    						+ " WHERE " + KEY_LISTS + " LIKE '%" + listTag + "%'"
    						+ " AND " + KEY_SENTENCES + " IS NOT 'nil'"
    						+ " AND " + KEY_SYNONYMS + " IS NOT 'nil'";
    		break;
    	default:
        	selectQuery = "SELECT * FROM " + TABLE_WORDS + " WHERE " + KEY_LISTS + " LIKE '%" + listTag + "%'";
    	}

        Cursor cursor = dataBase.rawQuery(selectQuery, null);
 
        /* looping through all rows and adding to list */
        if (cursor.moveToFirst()) {
            do {
            	String mWord = cursor.getString(0);
                mWord = mWord.replace(mContext.getString(R.string.glyph), mContext.getString(R.string.latin_e));
                wordList.add(mWord);
            } while (cursor.moveToNext());
        }

        cursor.close();

        /* return word list */
        return wordList;
    }

    /* Getting Favorite Words */
    public List<String> getFavoriteWords(int sectionNumber) {
        List<String> wordList = new ArrayList<>();
        String selectQuery;
    	switch(sectionNumber) {
    	case 3:
    		selectQuery = "SELECT * FROM " + TABLE_WORDS
    						+ " WHERE " + KEY_FAVORITES + " LIKE 'true'"
    						+ " AND " + KEY_SENTENCES + " IS NOT 'nil'";
    		break;
    	case 4:
    		selectQuery = "SELECT * FROM " + TABLE_WORDS
    						+ " WHERE " + KEY_FAVORITES + " LIKE 'true'"
    						+ " AND " + KEY_SENTENCES + " IS NOT 'nil'"
    						+ " AND " + KEY_SYNONYMS + " IS NOT 'nil'";
    		break;
    	default:
        	selectQuery = "SELECT * FROM " + TABLE_WORDS + " WHERE " + KEY_FAVORITES + " LIKE 'true'";
    	}

        Cursor cursor = dataBase.rawQuery(selectQuery, null);
 
        /* looping through all rows and adding to list */
        if (cursor.moveToFirst()) {
            do {
            	String mWord = cursor.getString(0);
                mWord = mWord.replace(mContext.getString(R.string.glyph), mContext.getString(R.string.latin_e));
                wordList.add(mWord);
            } while (cursor.moveToNext());
        }

        cursor.close();

        /* return word list */
        return wordList;
    }
 
    /* Updating single word */
    public int updateWord(Word word) {
 
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, word.getWord().replace(mContext.getString(R.string.latin_e), mContext.getString(R.string.glyph)));
        values.put(KEY_MEANINGS, word.getMeanings());
        values.put(KEY_ANTONYMS, word.getAntonyms());
        values.put(KEY_SYNONYMS, word.getSynonyms());
        values.put(KEY_EXAMPLES, word.getExamples());
        values.put(KEY_ETYMOLOGY, word.getEtymology());
        values.put(KEY_SENTENCES, word.getSentences());
        values.put(KEY_TYPES, word.getTypes());
        values.put(KEY_LISTS, word.getLists());
        values.put(KEY_SHORT_DEFINITIONS, word.getShortDefinitions());
        values.put(KEY_MNEMONICS, word.getMnemonics());
        values.put(KEY_STATUS, word.getStatus());
        values.put(KEY_FAVORITES, word.getFavorite());
 
        /* updating row */
        int pos = dataBase.update(TABLE_WORDS, values, KEY_WORD + " = ?",
                new String[] { word.getWord() });

        return pos;
    }
 
    /* Deleting single word */
    public void deleteWord(Word word) {
        dataBase.delete(TABLE_WORDS, KEY_WORD + " = ?",
                new String[] { word.getWord().replace(mContext.getString(R.string.latin_e), mContext.getString(R.string.glyph)) });
    }

    /* Getting words Count */
    public int getAllWordsCount(int sectionNumber) {
    	int count;
    	String countQuery = null;
    	switch(sectionNumber) {
    	case 3:
    		countQuery = "SELECT  * FROM " + TABLE_WORDS
		    				+ " WHERE " + KEY_SENTENCES + " IS NOT 'nil'";
    		break;
    	case 4:
    		countQuery = "SELECT  * FROM " + TABLE_WORDS
		    				+ " AND " + KEY_SENTENCES + " IS NOT 'nil'"
							+ " AND " + KEY_SYNONYMS + " IS NOT 'nil'";
    		break;
    	default:
        	countQuery = "SELECT  * FROM " + TABLE_WORDS;
    	}

        Cursor cursor = dataBase.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        return count;
    }

    /* Getting Words Count corresponding to an Alphabet */
    public int getAlphaWordsCount(char mChar, int sectionNumber) {
    	int count;
    	String countQuery = null;
    	switch(sectionNumber) {
    	case 3:
    		countQuery = "SELECT * FROM " + TABLE_WORDS
    						+ " WHERE " + KEY_WORD + " LIKE '" + mChar + "%'"
    						+ " AND " + KEY_SENTENCES + " IS NOT 'nil'";
    		break;
    	case 4:
    		countQuery = "SELECT * FROM " + TABLE_WORDS
    						+ " WHERE " + KEY_WORD + " LIKE '" + mChar + "%'"
    						+ " AND " + KEY_SENTENCES + " IS NOT 'nil'"
    						+ " AND " + KEY_SYNONYMS + " IS NOT 'nil'";
    		break;
    	default:
        	countQuery = "SELECT * FROM " + TABLE_WORDS + " WHERE " + KEY_WORD + " LIKE '" + mChar + "%'";
    	}

        Cursor cursor = dataBase.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        return count;
    }

    /* Getting Words Count corresponding to a Status */
    /*public int getStatusWordsCount(String status, int sectionNumber) {
    	int count;
    	String countQuery = null;
    	switch(sectionNumber) {
    	case 3:
    		countQuery = "SELECT * FROM " + TABLE_WORDS
    						+ " WHERE " + KEY_STATUS + " LIKE '" + status + "'"
    						+ " AND " + KEY_SENTENCES + " IS NOT 'nil'";
    		break;
    	case 4:
    		countQuery = "SELECT * FROM " + TABLE_WORDS
    						+ " WHERE " + KEY_STATUS + " LIKE '" + status + "'"
    						+ " AND " + KEY_SENTENCES + " IS NOT 'nil'"
    						+ " AND " + KEY_SYNONYMS + " IS NOT 'nil'";
    		break;
    	default:
        	countQuery = "SELECT * FROM " + TABLE_WORDS + " WHERE " + KEY_STATUS + " LIKE '" + status + "'";
    	}

        Cursor cursor = dataBase.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        return count;
    }*/

    /* Getting Words Count corresponding to a list tag */
    public int getListsWordsCount(String listTag, int sectionNumber) {
    	int count;
    	String countQuery;
    	switch(sectionNumber) {
    	case 3:
    		countQuery = "SELECT * FROM " + TABLE_WORDS
    						+ " WHERE instr(" + KEY_LISTS + ", '" + listTag + "')"
    						+ " AND " + KEY_SENTENCES + " IS NOT 'nil'";
    		break;
    	case 4:
    		countQuery = "SELECT * FROM " + TABLE_WORDS
    						+ " WHERE instr(" + KEY_LISTS + ", '" + listTag + "')"
    						+ " AND " + KEY_SENTENCES + " IS NOT 'nil'"
    						+ " AND " + KEY_SYNONYMS + " IS NOT 'nil'";
    		break;
    	default:
        	countQuery = "SELECT * FROM " + TABLE_WORDS + " WHERE instr(" + KEY_LISTS + ", '" + listTag + "')";
    	}

        Cursor cursor = dataBase.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        return count;
    }

    /* Getting Favorite Words Count */
    public int getFavoriteWordsCount(int sectionNumber) {
    	int count;
    	String countQuery;
    	switch(sectionNumber) {
    	case 3:
    		countQuery = "SELECT * FROM " + TABLE_WORDS
							+ " WHERE " + KEY_FAVORITES + " LIKE 'true'"
							+ " AND " + KEY_SENTENCES + " IS NOT 'nil'";
    		break;
    	case 4:
    		countQuery = "SELECT * FROM " + TABLE_WORDS
    						+ " WHERE " + KEY_FAVORITES + " LIKE 'true'"
    						+ " AND " + KEY_SENTENCES + " IS NOT 'nil'"
    						+ " AND " + KEY_SYNONYMS + " IS NOT 'nil'";
    		break;
    	default:
    		countQuery = "SELECT * FROM " + TABLE_WORDS + " WHERE " + KEY_FAVORITES + " LIKE 'true'";
    	}

        Cursor cursor = dataBase.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        return count;
    }

    public String getDBPath() {
    	if (dataBase != null)
    	    return dataBase.getPath();
        else
            return null;
    }

    public void cleanUp() {
        dataBase.close();
    }
}