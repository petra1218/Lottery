package com.petra.lottery;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

  //TODO:号码对比，
  //    自动跳转到下一项
  //    自动补0
  //    删除或是把号码置为无效
  //    自选号码布局调整
  //    号码推荐

  public static final String LOTTERY_DB = "lottery.db";
  public static final String TABLE_MY_RECORD = "my_record";
  Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
    }
  };
  private MyBalloonBean lastOfficialBean;
  private List<MyBalloonBean> myRecords = new ArrayList<>();
  private RecyclerView myRecordList;
  Runnable netRunnable = new Runnable() {
    @Override public void run() {
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
          int expect = jsonObject.getInt("expect");
          String opencode = jsonObject.getString("opencode");
          String time = jsonObject.getString("opentime");

          String[] nums = opencode.split("+");
          //MyBalloonBean bean = new MyBalloonBean(nums[0].split(","), nums[1], 0, MyBalloonBean.RecordType.OFFICIAL, expect, time);
          //if (null == lastOfficialBean || lastOfficialBean.getExpect() < bean.getExpect()) {
          //  lastOfficialBean = bean;
          //}
        }
      } catch (JSONException e) {

      }

      handler.sendEmptyMessage(0);
    }
  };
  private View view;
  private SQLiteDatabase db;
  private SqliteHelper sqliteHelper;
  private RecyclerView lotteryNum;

  public MainActivityFragment() {
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_main, container, false);

    lotteryNum = (RecyclerView) view.findViewById(R.id.lottery_num);
    lotteryNum.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, true));

    initOfficialRecord();
    initMyRecord();
    return view;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  private void initMyRecord() {
    if (null == sqliteHelper) {
      sqliteHelper = new SqliteHelper(getActivity(), LOTTERY_DB, null, 1);
      db = sqliteHelper.getReadableDatabase();
      if (!sqliteHelper.isTableExist(TABLE_MY_RECORD)) {
        try {
          db.execSQL("create table ["
              + TABLE_MY_RECORD
              + "] ( id integer primary key autoincrement, red_nums varchar(100), blue_num varchar(10), is_used integer)");
        } catch (SQLiteException e) {

        }
      }
    }

    try {
      Cursor cursor = db.rawQuery("select * from " + TABLE_MY_RECORD, null);

      if (!cursor.moveToFirst()) {
        return;
      }

      while (!cursor.isAfterLast()) {
        String redNums = cursor.getString(cursor.getColumnIndex("red_nums"));
        String blueNum = cursor.getString(cursor.getColumnIndex("blue_num"));
        int isUsed = cursor.getInt(cursor.getColumnIndex("is_used"));
        //MyBalloonBean bean = new MyBalloonBean(redNums.split(","), blueNum, isUsed, MyBalloonBean.RecordType.MY, 0, "");
        cursor.moveToNext();
      }
    } catch (SQLiteException e) {

    }
    MyBalloonListAdapter adapter = new MyBalloonListAdapter(getContext(), myRecords);
    myRecordList.setAdapter(adapter);
  }

  public void initOfficialRecord() {

    new Thread(netRunnable).start();
  }
}
