package com.petra.lottery;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.my_record_list_item_time)
    TextView myRecordListItemTime;
    @BindView(R.id.official_red_1)
    TextView officialRed1;
    @BindView(R.id.official_red_2)
    TextView officialRed2;
    @BindView(R.id.official_red_3)
    TextView officialRed3;
    @BindView(R.id.official_red_4)
    TextView officialRed4;
    @BindView(R.id.official_red_5)
    TextView officialRed5;
    @BindView(R.id.official_red_6)
    TextView officialRed6;
    @BindView(R.id.official_blue)
    TextView officialBlue;
    @BindView(R.id.my_balloon_list)
    RecyclerView myBalloonList;

    public static final String OFFICIALE_BALLOON = "OfficialeBalloon";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();
            MyBalloonBean bean = (MyBalloonBean) bundle.get(OFFICIALE_BALLOON);
            myRecordListItemTime.setText("时间：" + bean.getTime() + "    期数：" + bean.getExpect());
            officialRed1.setText(bean.getRedNums()[0]);
            officialRed2.setText(bean.getRedNums()[1]);
            officialRed3.setText(bean.getRedNums()[2]);
            officialRed4.setText(bean.getRedNums()[3]);
            officialRed5.setText(bean.getRedNums()[4]);
            officialRed6.setText(bean.getRedNums()[5]);
            officialBlue.setText(bean.getBlueNum());

            adapter.refreshMatch(bean);
        }
    };
    Runnable netRunnable = new Runnable() {
        @Override
        public void run() {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            try {
                URL url = new URL("http://f.apiplus.cn/ssq-10.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                int len = 0;
                while ((len = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String strRecordJson = new String(outputStream.toByteArray());

            try {
                JSONObject responseJson = new JSONObject(strRecordJson);
                JSONArray jsonArray = new JSONArray(responseJson.getString("data"));

                for (int i = jsonArray.length() - 1; i >= 0; i--) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    MyBalloonBean bean = parseBalloonBean(jsonObject);
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(OFFICIALE_BALLOON, bean);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            } catch (JSONException e) {

            }
        }

        @NonNull
        private MyBalloonBean parseBalloonBean(JSONObject jsonObject) throws JSONException {
            int expect = jsonObject.getInt("expect");
            String opencode = jsonObject.getString("opencode");
            String time = jsonObject.getString("opentime");

            int index_splite = opencode.indexOf("+");
            String reds = opencode.substring(0, index_splite);
            String blue = opencode.substring(index_splite + 1);
            return new MyBalloonBean(reds.split(","), blue, time, "" + expect);
        }
    };

    RouseService service;

    ServiceConnection connectioned = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private MyBalloonListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        new Thread(netRunnable).start();

        //final MainActivityFragment fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, RouseService.class);
                //if (!isWorked("com.petra.lottery.RouseService")) {
                //  startService(intent);
                //  bindService(intent, connectioned, Context.BIND_AUTO_CREATE);
                //}
                //
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                new Thread(netRunnable).start();

                //MainActivity.this.finish();
            }
        });

        initMyBalloon();
        SQLiteDatabase db = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory() + "/my.db", null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.execSQL("PRAGMA key = 'secretkey'");
    }

    private void initMyBalloon() {
        List<MyBalloonBean> myBalloons = new ArrayList<>();
        MyBalloonBean balloon1 = new MyBalloonBean(new String[]{"03", "07", "09", "11", "15", "27"}, "07", "", "");
        myBalloons.add(balloon1);
        MyBalloonBean balloon2 = new MyBalloonBean(new String[]{"01", "06", "07", "18", "22", "27"}, "08", "", "");
        myBalloons.add(balloon2);
        MyBalloonBean balloon3 = new MyBalloonBean(new String[]{"01", "12", "15", "23", "29", "31"}, "07", "", "");
        myBalloons.add(balloon3);
        MyBalloonBean balloon4 = new MyBalloonBean(new String[]{"05", "07", "09", "11", "27", "30"}, "08", "", "");
        myBalloons.add(balloon4);
        MyBalloonBean balloon5 = new MyBalloonBean(new String[]{"04", "09", "10", "11", "17", "20"}, "16", "", "");
        myBalloons.add(balloon5);

        adapter = new MyBalloonListAdapter(this, myBalloons);

        myBalloonList.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, true));
        myBalloonList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isWorked(String className) {
        ActivityManager myManager = (ActivityManager) this.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService =
                (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(className)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
