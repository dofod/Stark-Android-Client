package in.saurabhdiaries.stark.Views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

import in.saurabhdiaries.stark.Settings;

/**
 * Created by Saurabh on 28/03/2015.
 */
public class TimerView extends LinearLayout
{
    Context context;

    public TimerView(Context context)
    {
        super(context);
        this.context = context;
    }

    LinearLayout.LayoutParams params;
    EditText hour;
    EditText minute;
    EditText second;
    EditText event;
    public void build()
    {
        this.setOrientation(VERTICAL);

        hour = new EditText(context);
        hour.setId(Settings.id++);
        hour.setHint("Hours (1-24)");
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);
        this.addView(hour, params);

        minute = new EditText(context);
        minute.setId(Settings.id++);
        minute.setHint("Minutes (1-60");
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(minute, params);

        second = new EditText(context);
        second.setId(Settings.id++);
        second.setHint("Second (1-60)");
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(second, params);

        event = new EditText(context);
        event.setId(Settings.id++);
        event.setHint("Event Name");
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(event, params);

        final Button setTimer = new Button(context);
        setTimer.setId(Settings.id++);
        setTimer.setText("Set Timer");
        setTimer.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setTimer(
                        context,
                        event.getText().toString(),
                        hour.getText().toString(),
                        minute.getText().toString(),
                        second.getText().toString()
                );
            }
        });
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);
        this.addView(setTimer, params);

        this.setId(Settings.id++);
    }

    public static void setTimer(final Context context, final String eventName, final String h, final String m, final String s)
    {
        String url = "http://" + Settings.get("ip") + ":8000/api/v1/timer/?devicename=" + Settings.get("devicename") + "&auth_token=" + Settings.get("auth_token");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(context, "Timer Added Successfully", Toast.LENGTH_SHORT).show();
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
                    String data = "{\"event\":\""+eventName+"\", \"hour\":\""+(h.equals("")?"0":h)+"\", \"minute\":\""+(m.equals("")?"0":m)+"\", \"second\":\""+(s.equals("")?"0":s)+"\"}";
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
