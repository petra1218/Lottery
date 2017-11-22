package com.petra.lottery;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petra on 7/31/2016.
 */
public class MyBalloonListAdapter extends RecyclerView.Adapter<MyBalloonListAdapter.MyBalloonHolder> {
    List<MyBalloonBean> dates = new ArrayList<>();
    Context context;
    private MyBalloonBean officialeBalloon;

    public MyBalloonListAdapter(Context context, List<MyBalloonBean> datas) {
        this.context = context;
        this.dates = datas;
    }

    @Override
    public MyBalloonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_record_list_item, parent, false);
        return new MyBalloonHolder(view);
    }

    @Override
    public void onBindViewHolder(MyBalloonHolder holder, int position) {
        MyBalloonBean bean = dates.get(position);

        holder.officialRed1.setText(bean.getRedNums()[0]);
        holder.officialRed2.setText(bean.getRedNums()[1]);
        holder.officialRed3.setText(bean.getRedNums()[2]);
        holder.officialRed4.setText(bean.getRedNums()[3]);
        holder.officialRed5.setText(bean.getRedNums()[4]);
        holder.officialRed6.setText(bean.getRedNums()[5]);
        holder.officialBlue.setText(bean.getBlueNum());

        if (null != officialeBalloon) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (bean.getBlueNum().equals(officialeBalloon.getBlueNum())) {
                    holder.officialBlue.setBackground(context.getDrawable(R.drawable.balloon_shape_blue_matched));
                } else {
                    holder.officialBlue.setBackground(context.getDrawable(R.drawable.balloon_shape_blue));
                }
                holder.officialRed1.setBackground(context.getDrawable(R.drawable.balloon_shape_red));
                holder.officialRed2.setBackground(context.getDrawable(R.drawable.balloon_shape_red));
                holder.officialRed3.setBackground(context.getDrawable(R.drawable.balloon_shape_red));
                holder.officialRed4.setBackground(context.getDrawable(R.drawable.balloon_shape_red));
                holder.officialRed5.setBackground(context.getDrawable(R.drawable.balloon_shape_red));
                holder.officialRed6.setBackground(context.getDrawable(R.drawable.balloon_shape_red));
                for (String balloon : officialeBalloon.getRedNums()) {
                    if (balloon.equals(bean.getRedNums()[0])) {
                        holder.officialRed1.setBackground(context.getDrawable(R.drawable.balloon_shape_red_matched));
                    } else if (balloon.equals(bean.getRedNums()[1])) {
                        holder.officialRed2.setBackground(context.getDrawable(R.drawable.balloon_shape_red_matched));
                    } else if (balloon.equals(bean.getRedNums()[2])) {
                        holder.officialRed3.setBackground(context.getDrawable(R.drawable.balloon_shape_red_matched));
                    } else if (balloon.equals(bean.getRedNums()[3])) {
                        holder.officialRed4.setBackground(context.getDrawable(R.drawable.balloon_shape_red_matched));
                    } else if (balloon.equals(bean.getRedNums()[4])) {
                        holder.officialRed5.setBackground(context.getDrawable(R.drawable.balloon_shape_red_matched));
                    } else if (balloon.equals(bean.getRedNums()[5])) {
                        holder.officialRed6.setBackground(context.getDrawable(R.drawable.balloon_shape_red_matched));
                    }
                }
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

    static class MyBalloonHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.my_record_list_layout)
        View myRecordListLayout;

        MyBalloonHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            myRecordListLayout.setBackground(null);
        }
    }

    public void refreshMatch(MyBalloonBean bean) {
        this.officialeBalloon = bean;
        notifyDataSetChanged();
    }
}
