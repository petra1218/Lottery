package com.petra.lottery;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by petra on 1/11/2017.
 */

public class RouseService extends Service {

  public Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      Toast.makeText(RouseService.this, "toast in handleMessage.", Toast.LENGTH_SHORT).show();
      super.handleMessage(msg);
      Intent i = new Intent(RouseService.this, MainActivity.class);
      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      RouseService.this.startActivity(i);
    }
  };

  @Override public IBinder onBind(Intent intent) {
    EventBus.getDefault().register(this);
    registAlarmReceive();
    return null;
  }

  @Override public boolean onUnbind(Intent intent) {
    return super.onUnbind(intent);
  }

  @Override public void onRebind(Intent intent) {
    super.onRebind(intent);
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  private void registAlarmReceive() {
    long triggerTime = System.currentTimeMillis() + 1000 * 2;
    AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

    Intent intent = new Intent(this, AlarmReceive.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

    alarm.set(AlarmManager.RTC, triggerTime, pendingIntent);
    Toast.makeText(this, "toast in service.", Toast.LENGTH_SHORT).show();
  }

  @Subscribe public void onEventAsync(RouseEvent event) {
    Toast.makeText(this, "toast in onEventAsync.", Toast.LENGTH_SHORT).show();
    handler.sendEmptyMessage(0);
  }
}
