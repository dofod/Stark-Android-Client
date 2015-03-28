package in.saurabhdiaries.stark.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

import in.saurabhdiaries.stark.Settings;

/**
 * Created by Saurabh on 28/03/2015.
 */
public class IntentListenerService extends Service
{
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        this.registerReceiver(this.batteryMonitor, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        this.registerReceiver(this.wifiMonitor, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        return super.onStartCommand(intent, flags, startId);
    }

    private final BroadcastReceiver batteryMonitor = new BroadcastReceiver()
    {
        @Override
        public void onReceive(final Context context, final Intent intent)
        {
            final int currLevel = intent.getIntExtra(
                    BatteryManager.EXTRA_LEVEL, -1);
            final int maxLevel = intent.getIntExtra(
                    BatteryManager.EXTRA_SCALE, -1);
            final int percentage = (int) Math.round((currLevel * 100.0) / maxLevel);
            if (percentage == 100)
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        IntentActions.onChargeEvent(IntentActions.CHARGE_EVENTS.BATTERY_FULL);
                    }
                }).start();
            }
        }
    };

    private final BroadcastReceiver wifiMonitor = new BroadcastReceiver()
    {
        @Override
        public void onReceive(final Context context, Intent intent)
        {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager.getConnectionInfo().getSSID() != null&&Settings.get("home_wifi")!=null)
            {
                if (Settings.get("home_wifi").equals(wifiManager.getConnectionInfo().getSSID()))
                {
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            IntentActions.onWifiConnected(context);
                        }
                    }).start();
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onDestroy()
    {
        Log.d("", "Service.onDestroy()...");
        super.onDestroy();
    }


}
