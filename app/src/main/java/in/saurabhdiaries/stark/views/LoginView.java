package in.saurabhdiaries.stark.Views;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import in.saurabhdiaries.stark.Settings;

/**
 * Created by Saurabh on 26/03/2015.
 */
public class LoginView extends LinearLayout
{
    Context context;
    public LoginView(Context context)
    {
        super(context);
        this.context = context;
    }

    LinearLayout.LayoutParams params;
    EditText ip;
    EditText username;
    EditText password;
    public void build()
    {
        this.setOrientation(VERTICAL);

        ip = new EditText(context);
        ip.setId(Settings.id++);
        ip.setHint("IP address");
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);
        this.addView(ip, params);

        username = new EditText(context);
        username.setId(Settings.id++);
        username.setHint("Username");
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(username, params);

        password = new EditText(context);
        password.setId(Settings.id++);
        password.setHint("Password");
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(password, params);

        Button login= new Button(context);
        login.setId(Settings.id++);
        login.setText("Login");
        login.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Settings.set("ip", ip.getText().toString());
                String url = "http://" + Settings.get("ip") + ":8000/api/v1/api-key/?username=" + username.getText().toString() + "&password=" + password.getText().toString();
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                        url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            for (int objIndex = 0; objIndex < response.getJSONArray("objects").length(); objIndex++)
                            {
                                Settings.set("api_key", response.getJSONArray("objects").getJSONObject(objIndex).getString("key"));
                                Settings.set("username", username.getText().toString());
                            }
                        } catch (JSONException e)
                        {
                            Log.d("Error", "Login: JSON Parse");
                        }
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        VolleyLog.d("Volley Error", "Error: " + error.getMessage());
                        System.out.println(error.networkResponse);
                    }
                });
                Settings.requestQueue.add(jsonRequest);
                getDeviceAuthToken();
            }
        });
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);
        this.addView(login, params);

        this.setId(Settings.id++);
    }

    public void getDeviceAuthToken()
    {
        String url = "http://" + Settings.get("ip") + ":8000/api/v1/device/?devicename=android_smartphone&key=password";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    for (int objIndex = 0; objIndex < response.getJSONArray("objects").length(); objIndex++)
                    {
                        Settings.set("auth_token", response.getJSONArray("objects").getJSONObject(objIndex).getString("auth_token"));
                        Settings.set("devicename", "android_smartphone");
                    }
                } catch (JSONException e)
                {
                    Log.d("Error", "Login: JSON Parse");
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d("Volley Error", "Error: " + error.getMessage());
                if(error.networkResponse.statusCode==401&&!registerFlag)
                {
                    registerDevice();
                }
            }
        });
        Settings.requestQueue.add(jsonRequest);
    }

    boolean registerFlag = false;
    public void registerDevice()
    {
        registerFlag = true;
        String url = "http://"+Settings.get("ip")+":8000/api/v1/add-device/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                getDeviceAuthToken();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d("Volley Error", "Error: " + error.getMessage());
                System.out.println(new String(error.networkResponse.data));
            }
        }){

            @Override
            public byte[] getBody() throws AuthFailureError
            {
                try
                {
                    String data="{\"name\":\"android_smartphone\",\"password\":\"password\"}";
                    return data.getBytes(getParamsEncoding());
                }
                catch (UnsupportedEncodingException e)
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
