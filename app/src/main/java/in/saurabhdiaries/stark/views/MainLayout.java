package in.saurabhdiaries.stark.views;

import android.content.Context;
import android.graphics.Color;
import android.sax.StartElementListener;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import in.saurabhdiaries.stark.Settings;

/**
 * Created by Saurabh on 28/03/2015.
 */
public class MainLayout extends LinearLayout
{
    Context context;
    public MainLayout(Context context)
    {
        super(context);
        this.context = context;
    }

    FrameLayout mainView;
    LinearLayout navigation;
    TriggerView triggerView;
    public void build()
    {
        navigation = new LinearLayout(context);
        navigation.setId(Settings.id++);
        navigation.setOrientation(LinearLayout.HORIZONTAL);
        navigation.setBackgroundColor(Color.parseColor("#A0A0A0A0"));
        navigation.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Button triggers = new Button(context);
        triggers.setText("Triggers");
        triggers.setId(Settings.id++);
        navigation.addView(triggers);

        Button events = new Button(context);
        events.setText("Events");
        events.setId(Settings.id++);
        navigation.addView(events);

        Button timer = new Button(context);
        timer.setText("Timer");
        timer.setId(Settings.id++);
        navigation.addView(timer);

        this.addView(navigation);

        mainView = new FrameLayout(context);
        mainView.setId(Settings.id++);

        triggerView = new TriggerView(context);
        triggerView.build();
        triggerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mainView.addView(triggerView);

        this.addView(mainView);
        this.setId(Settings.id++);
    }
}
