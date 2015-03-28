package in.saurabhdiaries.stark.Views;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000);
    }

    Animation fadeIn;

    RelativeLayout.LayoutParams params;
    FrameLayout mainView;
    RelativeLayout navigation;
    TriggerView triggerView;
    EventView eventView;
    TimerView timerView;
    SettingsView settingsView;
    public void build()
    {
        this.setId(Settings.id++);
        this.setOrientation(VERTICAL);

        navigation = new RelativeLayout(context);
        navigation.setId(Settings.id++);
        navigation.setBackgroundColor(Color.parseColor("#FFFFFF"));
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);
        navigation.setLayoutParams(params);

        Button triggers = new Button(context);
        triggers.setId(Settings.id++);
        triggers.setText("Triggers");
        triggers.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                set(CATEGORY.TRIGGERS);
            }
        });
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        navigation.addView(triggers, params);

        Button events = new Button(context);
        events.setId(Settings.id++);
        events.setText("Events");
        events.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                set(CATEGORY.EVENTS);
            }
        });
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        navigation.addView(events, params);

        Button timer = new Button(context);
        timer.setId(Settings.id++);
        timer.setText("Timer");
        timer.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                set(CATEGORY.TIMER);
            }
        });
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        navigation.addView(timer, params);

        this.addView(navigation);

        mainView = new FrameLayout(context);
        mainView.setId(Settings.id++);

        set(CATEGORY.TRIGGERS);

        this.addView(mainView);
    }

    public enum CATEGORY{
        EVENTS,
        TRIGGERS,
        TIMER,
        SETTINGS
    }
    public void set(CATEGORY option)
    {
        switch (option)
        {
            case EVENTS:
            {
                if(eventView!=null)
                {
                    eventView.removeAllViews();
                }
                mainView.removeAllViews();
                eventView = new EventView(context);
                eventView.build();
                mainView.addView(eventView);
                mainView.startAnimation(fadeIn);
            }
            break;
            case TRIGGERS:
            {
                if(triggerView!=null)
                {
                    triggerView.removeAllViews();
                }
                mainView.removeAllViews();
                triggerView = new TriggerView(context);
                triggerView.build();
                mainView.addView(triggerView);
                mainView.startAnimation(fadeIn);
            }
            break;
            case TIMER:
            {
                if(timerView!=null)
                {
                    timerView.removeAllViews();
                }
                mainView.removeAllViews();
                timerView = new TimerView(context);
                timerView.build();
                mainView.addView(timerView);
                mainView.startAnimation(fadeIn);
            }
            break;
            case SETTINGS:
            {
                if(settingsView!=null)
                {
                    settingsView.removeAllViews();
                }
                mainView.removeAllViews();
                settingsView = new SettingsView(context);
                settingsView.build();
                mainView.addView(settingsView);
                mainView.startAnimation(fadeIn);
            }
            break;

        }
    }
}
