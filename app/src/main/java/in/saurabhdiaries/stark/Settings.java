package in.saurabhdiaries.stark;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Saurabh on 27/03/2015.
 */
public class Settings
{
    public static Context context;
    public static RequestQueue requestQueue;
    static SQLCache settings;

    public static int id=0;

    public static void load(Context context)
    {
        Settings.context = context;
        settings = new SQLCache(context, "settings");
        requestQueue = Volley.newRequestQueue(context);

    }

    public static void set(String key, String value)
    {
        settings.set(key, value);
    }

    public static String get(String key)
    {
        try
        {
            return settings.get(key);
        }
        catch (SQLCache.KeyNotFoundException e)
        {
            Log.d("ERROR", "Settings:SQL Key missing");
        }
        return null;
    }
}
