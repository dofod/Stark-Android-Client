package in.saurabhdiaries.stark.Views;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.saurabhdiaries.stark.Settings;
/**
 * Created by Saurabh on 28/03/2015.
 */
public class SettingsView extends LinearLayout
{
    Context context;
    public SettingsView(Context context)
    {
        super(context);
        this.context = context;
    }

    public void build()
    {
        this.setId(Settings.id++);
        this.setOrientation(VERTICAL);

        Button wifi = new Button(context);
        wifi.setId(Settings.id++);
        wifi.setText("Set Wifi as Home");
        wifi.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                String currentSSID = wifiManager.getConnectionInfo().getSSID();
                Settings.set("home_wifi", currentSSID);
                Toast.makeText(context, currentSSID+" has been set as Home Wifi", Toast.LENGTH_SHORT).show();
            }
        });
        this.addView(wifi);

        Button alarm = new Button(context);
        alarm.setId(Settings.id++);
        alarm.setText("Scan Alarms");
        alarm.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                AlarmManager.AlarmClockInfo clock = alarm.getNextAlarmClock();
                if(clock!=null)
                {
                    Date date = new Date(clock.getTriggerTime());
                    DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
                    String dateFormatted = formatter.format(date);
                    TimerView.setTimer(context, "MORNING_ALARM", dateFormatted.substring(0, 2), dateFormatted.substring(3, 5),"0");
                }
                /*
                AlarmDatabase alarmDatabase = new AlarmDatabase(context.getContentResolver());
                AlarmDatabase.Record alarmRecord = alarmDatabase.getNearestEnabledAlarm();
                TimerView.setTimer(context, "MORNING_ALARM", Integer.toString(alarmRecord.hour), Integer.toString(alarmRecord.minute),"0");*/
            }
        });
        this.addView(alarm);

        Button talk = new Button(context);
        talk.setId(Settings.id++);
        talk.setText("Talk");
        talk.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
                ((Activity)context).startActivityForResult(intent, Settings.SPEECH_ID);
            }
        });
        this.addView(talk);

    }

    public static void recognizeSpeech(final String sentence)
    {
        String url = "http://" + Settings.get("ip") + ":8000/api/v1/event/?devicename=" + Settings.get("devicename") + "&auth_token=" + Settings.get("auth_token");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST,
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
                    String data = "{\"speech\":\"" + sentence + "\"}";
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
        Settings.requestQueue.add(jsonRequest);
    }
}
