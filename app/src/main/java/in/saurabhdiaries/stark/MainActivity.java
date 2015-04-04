package in.saurabhdiaries.stark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;

import in.saurabhdiaries.stark.Services.IntentListenerService;
import in.saurabhdiaries.stark.Views.LoginView;
import in.saurabhdiaries.stark.Views.MainLayout;
import in.saurabhdiaries.stark.Views.SettingsView;


public class MainActivity extends Activity
{
    Context context;
    LinearLayout triggerList;
    //String ip = "10.0.2.2";
    //String ip = "172.24.0.10";

    MainLayout mainLayout;
    IntentListenerService service;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Settings.load(this);

        if(Settings.get("ip")==null||Settings.get("username")==null||Settings.get("devicename")==null)
        {
            LoginView loginView = new LoginView(this);
            loginView.build();
            setContentView(loginView);
        }
        else
        {
            if(service==null)
            {
                startService(new Intent(this, IntentListenerService.class));
                System.out.println("Starting Service");
            }
            else
            {
                System.out.println("Service Already Started");
            }

            mainLayout = new MainLayout(this);
            mainLayout.build();
            this.setContentView(mainLayout);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == Settings.SPEECH_ID && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard

            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            for(String sentence: matches)
            {
                System.out.println(sentence);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            mainLayout.set(MainLayout.CATEGORY.SETTINGS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
