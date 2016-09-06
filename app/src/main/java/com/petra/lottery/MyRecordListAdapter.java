package com.petra.lottery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petra on 7/31/2016.
 */
public class MyRecordListAdapter extends RecyclerView.Adapter<MyRecordListAdapter.MyRecordHolder> {
    private final MyRecordBean officialData;
    List<MyRecordBean> dates = new ArrayList<MyRecordBean>();
    Context context;

    public MyRecordListAdapter(Context context, List<MyRecordBean> datas, MyRecordBean officialData) {
        this.context = context;
        this.dates = datas;
        this.officialData = officialData;
    }

    @Override
    public MyRecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_record_list_item, parent, false);
        return new MyRecordHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRecordHolder holder, int position) {
        MyRecordBean bean = dates.get(position);
        switch (bean.getType()) {
            case MY:
                holder.expect.setVisibility(View.GONE);
                holder.time.setVisibility(View.GONE);

                switch (bean.getStatus()) {
                    case 0:
                        holder.status.setImageResource(R.mipmap.ic_remove_circle_black_24dp);
                        break;
                    case 1:
                        holder.status.setImageResource(R.mipmap.ic_done_black_24dp);
                        break;
                    default:
                        break;
                }
                break;
            case OFFICIAL:
                holder.status.setVisibility(View.GONE);
                holder.expect.setText("" + bean.getExpect());
                String time = bean.getTime();
                time = time.substring(0, time.indexOf(" "));
                holder.time.setText(time);
                break;
        }
        holder.num1.setText(bean.getMum1());
        holder.num2.setText(bean.getMum2());
        holder.num3.setText(bean.getMum3());
        holder.num4.setText(bean.getMum4());
        holder.num5.setText(bean.getMum5());
        holder.num6.setText(bean.getMum6());
        holder.num7.setText(bean.getMum7());
        if (null != officialData) {
            if (bean.getMum1().equals(officialData.getMum1())) {
                holder.num1.setTextColor(0xFFFF0000);
            }
            if (bean.getMum2().equals(officialData.getMum2())) {
                holder.num2.setTextColor(0xFFFF0000);
            }
            if (bean.getMum3().equals(officialData.getMum3())) {
                holder.num3.setTextColor(0xFFFF0000);
            }
            if (bean.getMum4().equals(officialData.getMum4())) {
                holder.num4.setTextColor(0xFFFF0000);
            }
            if (bean.getMum5().equals(officialData.getMum5())) {
                holder.num5.setTextColor(0xFFFF0000);
            }
            if (bean.getMum6().equals(officialData.getMum6())) {
                holder.num6.setTextColor(0xFFFF0000);
            }
            if (bean.getMum7().equals(officialData.getMum7())) {
                holder.num7.setTextColor(0xFFFF0000);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dates == null ? 0 : dates.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class MyRecordHolder extends RecyclerView.ViewHolder {
        TextView expect;
        TextView num1;
        TextView num2;
        TextView num3;
        TextView num4;
        TextView num5;
        TextView num6;
        TextView num7;
        TextView time;
        ImageView status;

        public MyRecordHolder(View itemView) {
            super(itemView);

            expect = (TextView) itemView.findViewById(R.id.my_record_list_item_expect);

            num1 = (TextView) itemView.findViewById(R.id.my_record_list_item_num1);
            num2 = (TextView) itemView.findViewById(R.id.my_record_list_item_num2);
            num3 = (TextView) itemView.findViewById(R.id.my_record_list_item_num3);
            num4 = (TextView) itemView.findViewById(R.id.my_record_list_item_num4);
            num5 = (TextView) itemView.findViewById(R.id.my_record_list_item_num5);
            num6 = (TextView) itemView.findViewById(R.id.my_record_list_item_num6);
            num7 = (TextView) itemView.findViewById(R.id.my_record_list_item_num7);

            time = (TextView) itemView.findViewById(R.id.my_record_list_item_time);

            status = (ImageView) itemView.findViewById(R.id.my_record_list_item_status);
        }
    }
}
