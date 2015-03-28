package in.saurabhdiaries.stark;

/**
 * Created by Saurabh on 26/03/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLCache extends SQLiteOpenHelper
{
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "cache";

    // Table name
    private String TABLE = "";

    // Contacts Table Columns names
    private static final String KEY_KEY = "key";
    private static final String KEY_VALUE = "value";

    public SQLCache(Context context, String CacheType)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.TABLE = CacheType;
    }

    class KeyNotFoundException extends Exception
    {
        public String key;
        public KeyNotFoundException(String message, String key)
        {
            super(message);
            this.key = key;
        }
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String QUERY= "CREATE TABLE " + TABLE + "("
                + KEY_KEY + " TEXT PRIMARY KEY," + KEY_VALUE + " TEXT)";
        db.execSQL(QUERY);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void set(String key, String value)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_KEY, key);
        values.put(KEY_VALUE, value);

        // Inserting Row
        try
        {
            db.insertOrThrow(TABLE, null, values);
        }
        catch (SQLiteConstraintException e)
        {
            db.delete(TABLE, KEY_KEY + " = ?", new String[] { key });
            db.insert(TABLE, null, values);
        }
        catch (SQLiteException e)
        {
            onCreate(db);
        }
        db.close(); // Closing database connection
    }

    // Getting single contact
    public String get(String key) throws KeyNotFoundException
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        try
        {
            cursor = db.query(TABLE, new String[]{KEY_KEY,
                            KEY_VALUE}, KEY_KEY + "=?",
                    new String[]{key}, null, null, null, null
            );
        }
        catch (SQLiteException e)
        {
            throw new KeyNotFoundException("Key Not Found", key);
        }

        if (cursor.moveToFirst())
        {
            return cursor.getString(1);
        }

        throw new KeyNotFoundException("Key Not Found", key);
    }

    public void delete(String key)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, KEY_KEY + " = ?",
                new String[] { key });
        db.close();
    }

    public int count()
    {
        String countQuery = "SELECT  * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

}