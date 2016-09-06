package com.petra.lottery;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener, TextWatcher {

  //TODO:号码对比，
  //    自动跳转到下一项
  //    自动补0
  //    删除或是把号码置为无效
  //    自选号码布局调整
  //    号码推荐

  public static final String LOTTERY_DB = "lottery.db";
  public static final String TABLE_MY_RECORD = "my_record";
  public static final String TABLE_NET_RECORD = "net_record";
  ArrayList<MyRecordBean> officialRecords = new ArrayList<>();
  ArrayList<MyRecordBean> myRecords = new ArrayList<>();
  private RecyclerView officialRecordList;
  Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);

      MyRecordListAdapter adapter = new MyRecordListAdapter(getContext(), officialRecords, null);
      officialRecordList.setAdapter(adapter);
      officialRecordList.scrollToPosition(0);
    }
  };
  private MyRecordBean lastOfficialBean;
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

        officialRecords.clear();
        for (int i = jsonArray.length() - 1; i >= 0; i--) {
          JSONObject jsonObject = jsonArray.getJSONObject(i);
          int expect = jsonObject.getInt("expect");
          String opencode = jsonObject.getString("opencode");
          String time = jsonObject.getString("opentime");

          String[] nums = opencode.split(",");
          String num6 = nums[5].substring(0, 2);
          String num7 = nums[5].substring(3, 5);
          MyRecordBean bean =
              new MyRecordBean(nums[0], nums[1], nums[2], nums[3], nums[4], num6, num7, 0, MyRecordBean.RecordType.OFFICIAL,
                  expect, time);
          if (null == lastOfficialBean || lastOfficialBean.getExpect() < bean.getExpect()) {
            lastOfficialBean = bean;
          }
          officialRecords.add(bean);
        }
      } catch (JSONException e) {

      }

      handler.sendEmptyMessage(0);
    }
  };
  private RecyclerView myRecordList;
  private View view;
  private EditText etNum1;
  private EditText etNum2;
  private EditText etNum3;
  private EditText etNum4;
  private EditText etNum5;
  private EditText etNum6;
  private EditText etNum7;
  private String num1;
  private String num2;
  private String num3;
  private String num4;
  private String num5;
  private String num6;
  private String num7;
  private Button btAdd;
  private SQLiteDatabase db;
  private SqliteHelper sqliteHelper;

  public MainActivityFragment() {
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_main, container, false);

    TextView tv = (TextView) view.findViewById(R.id.textView);
    officialRecordList = (RecyclerView) view.findViewById(R.id.record_list);
    myRecordList = (RecyclerView) view.findViewById(R.id.my_record_list);

    officialRecordList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, true));
    myRecordList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, true));

    officialRecordList.setItemAnimator(new DefaultItemAnimator());
    myRecordList.setItemAnimator(new DefaultItemAnimator());

    btAdd = (Button) view.findViewById(R.id.add_new_nums);
    btAdd.setOnClickListener(this);
    btAdd.setClickable(false);

    etNum1 = (EditText) view.findViewById(R.id.num_1);
    etNum1.addTextChangedListener(this);
    etNum2 = (EditText) view.findViewById(R.id.num_2);
    etNum2.addTextChangedListener(this);
    etNum3 = (EditText) view.findViewById(R.id.num_3);
    etNum3.addTextChangedListener(this);
    etNum4 = (EditText) view.findViewById(R.id.num_4);
    etNum4.addTextChangedListener(this);
    etNum5 = (EditText) view.findViewById(R.id.num_5);
    etNum5.addTextChangedListener(this);
    etNum6 = (EditText) view.findViewById(R.id.num_6);
    etNum6.addTextChangedListener(this);
    etNum7 = (EditText) view.findViewById(R.id.num_7);
    etNum7.addTextChangedListener(this);

    initOfficialRecord();
    initMyRecord();
    return view;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  private void initMyRecord() {
    if (null == sqliteHelper) {
      // db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + LOTTERY_DB, null);
      // db = getContext().openOrCreateDatabase(LOTTERY_DB, Context.MODE_PRIVATE, null);
      sqliteHelper = new SqliteHelper(getActivity(), LOTTERY_DB, null, 1);
      db = sqliteHelper.getReadableDatabase();
      if (!sqliteHelper.isTableExist(TABLE_MY_RECORD)) {
        try {
          db.execSQL("create table ["
              + TABLE_MY_RECORD
              + "] ( id integer primary key autoincrement, num1 varchar(20), num2 varchar(20),num3 varchar(20),num4 varchar(20),num5 varchar(20),num6 varchar(20),num7 varchar(20), is_used integer)");
        } catch (SQLiteException e) {

        }
      }
    }
    //        if (!sqliteHelper.isTableExist(TABLE_NET_RECORD)) {
    //            try {
    //                db.execSQL("create table [" + TABLE_NET_RECORD + "] ( id varch(20) primary key autoincrement, num1 varch(20), num1 varch(20),num1 varch(20),num1 varch(20),num1 varch(20),num1 varch(20), expect varch(20), time varchar(20))");
    //            } catch (SQLiteException e) {
    //
    //            }
    //        }

    try {
      Cursor cursor = db.rawQuery("select * from " + TABLE_MY_RECORD, null);

      if (!cursor.moveToFirst()) {
        return;
      }

      myRecords.clear();
      while (!cursor.isAfterLast()) {
        String num1 = cursor.getString(cursor.getColumnIndex("num1"));
        String num2 = cursor.getString(cursor.getColumnIndex("num2"));
        String num3 = cursor.getString(cursor.getColumnIndex("num3"));
        String num4 = cursor.getString(cursor.getColumnIndex("num4"));
        String num5 = cursor.getString(cursor.getColumnIndex("num5"));
        String num6 = cursor.getString(cursor.getColumnIndex("num6"));
        String num7 = cursor.getString(cursor.getColumnIndex("num7"));
        int isUsed = cursor.getInt(cursor.getColumnIndex("is_used"));
        MyRecordBean bean =
            new MyRecordBean(num1, num2, num3, num4, num5, num6, num7, isUsed, MyRecordBean.RecordType.MY, 0, "");
        myRecords.add(bean);
        cursor.moveToNext();
      }
    } catch (SQLiteException e) {

    }
    MyRecordListAdapter adapter = new MyRecordListAdapter(getContext(), myRecords, lastOfficialBean);
    myRecordList.setAdapter(adapter);
  }

  public void initOfficialRecord() {

    new Thread(netRunnable).start();
  }

  @Override public void onClick(View v) {
    ContentValues values = new ContentValues();
    values.put("num1", num1);
    values.put("num2", num2);
    values.put("num3", num3);
    values.put("num4", num4);
    values.put("num5", num5);
    values.put("num6", num6);
    values.put("num7", num7);
    values.put("is_used", 1);
    db.insert(TABLE_MY_RECORD, "", values);
    etNum1.setText("");
    etNum2.setText("");
    etNum3.setText("");
    etNum4.setText("");
    etNum5.setText("");
    etNum6.setText("");
    etNum7.setText("");

    initMyRecord();
  }

  @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override public void afterTextChanged(Editable s) {
    num1 = etNum1.getText().toString();
    num2 = etNum2.getText().toString();
    num3 = etNum3.getText().toString();
    num4 = etNum4.getText().toString();
    num5 = etNum5.getText().toString();
    num6 = etNum6.getText().toString();
    num7 = etNum7.getText().toString();


    if (!textIsEmpty(num1)
        && !textIsEmpty(num2)
        && !textIsEmpty(num3)
        && !textIsEmpty(num4)
        && !textIsEmpty(num5)
        && !textIsEmpty(num6)) {
      btAdd.setClickable(true);
    }
  }

  private boolean textIsEmpty(String str) {
    return null == str || "".equals(str);
  }
}
