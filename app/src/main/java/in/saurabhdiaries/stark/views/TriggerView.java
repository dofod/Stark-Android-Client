package in.saurabhdiaries.stark.Views;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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
public class TriggerView extends ScrollView
{
    Context context;
    public TriggerView(Context context)
    {
        super(context);
        this.context = context;
    }

    public LinearLayout triggerList;
    public void build()
    {
        triggerList = new LinearLayout(context);
        triggerList.setOrientation(LinearLayout.VERTICAL);

        String url = "http://"+Settings.get("ip")+":8000/api/v1/plugin/?username="+Settings.get("username")+"&api_key="+Settings.get("api_key");

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    for(int objIndex=0; objIndex<response.getJSONArray("objects").length(); objIndex++)
                    {
                        for(int triggerIndex=0; triggerIndex<response.getJSONArray("objects").getJSONObject(objIndex).getJSONArray("triggers").length(); triggerIndex++)
                        {
                            final String triggerName = response.getJSONArray("objects").getJSONObject(objIndex).getJSONArray("triggers").getString(triggerIndex);
                            Button trigger = new Button(context);
                            trigger.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    System.out.println(triggerName);
                                    String url = "http://"+Settings.get("ip")+":8000/api/v1/trigger/?username="+Settings.get("username")+"&api_key="+Settings.get("api_key");
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
                                    }){

                                        @Override
                                        public byte[] getBody() throws AuthFailureError
                                        {
                                            try
                                            {
                                                String data="{\"trigger\":\""+triggerName+"\"}";
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
                                    Settings.requestQueue.add(jsonRequest);
                                }
                            });
                            trigger.setText(response.getJSONArray("objects").getJSONObject(objIndex).getJSONArray("triggers").getString(triggerIndex));
                            triggerList.addView(trigger);
                        }
                    }
                }
                catch(JSONException e)
                {

                }
                System.out.println(response);
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

        this.addView(triggerList);
        this.setId(Settings.id++);
    }
}
