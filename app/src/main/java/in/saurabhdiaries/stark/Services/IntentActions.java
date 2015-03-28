package in.saurabhdiaries.stark.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;

import in.saurabhdiaries.stark.Settings;

/**
 * Created by Saurabh on 28/03/2015.
 */
public class IntentActions
{
    public static void onWifiConnected(final Context context)
    {
        String url = "http://" + Settings.get("ip") + ":8000/api/v1/event/?devicename=" + Settings.get("devicename") + "&auth_token=" + Settings.get("auth_token");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(context, "Lights Turned On", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d("Volley Error", "Error: " + error.getMessage());
            }
        })
        {

            @Override
            public byte[] getBody() throws AuthFailureError
            {
                try
                {
                    String data = "{\"event\":\"WIFI_CONNECTED\"}";
                    System.out.println(data);
                    return data.getBytes(getParamsEncoding());
                } catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };
        Settings.requestQueue.add(stringRequest);
    }

    public enum CHARGE_EVENTS{
        CHARGER_DISCONNECTED,
        BATTERY_FULL
    }

    static String event;
    public static void onChargeEvent(CHARGE_EVENTS chargeEvent)
    {
        switch (chargeEvent)
        {
            case CHARGER_DISCONNECTED:
            {
                event = "CHARGER_DISCONNECTED";
            }
            break;
            case BATTERY_FULL:
            {
                event = "BATTERY_FULL";
            }
            break;
        }

        String url = "http://" + Settings.get("ip") + ":8000/api/v1/event/?devicename=" + Settings.get("devicename") + "&auth_token=" + Settings.get("auth_token");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d("Volley Error", "Error: " + error.getMessage());
            }
        })
        {

            @Override
            public byte[] getBody() throws AuthFailureError
            {
                try
                {
                    String data = "{\"event\":\""+event+"\"}";
                    System.out.println(data);
                    return data.getBytes(getParamsEncoding());
                } catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };
        Settings.requestQueue.add(stringRequest);
    }
}
