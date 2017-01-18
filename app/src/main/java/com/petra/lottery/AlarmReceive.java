package com.petra.lottery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by petra on 1/11/2017.
 */

public class AlarmReceive extends BroadcastReceiver {
  @Override public void onReceive(Context context, Intent intent) {

    Toast.makeText(context, "toast in AlarmReceive.", Toast.LENGTH_SHORT).show();
    EventBus.getDefault().post(new RouseEvent());
  }
}
