package com.petra.lottery;

/**
 * Created by petra on 7/31/2016.
 */
public class MyRecordBean {
    private String mum1;
    private String mum2;
    private String mum3;
    private String mum4;
    private String mum5;
    private String mum6;
    private String mum7;
    private int status;
    private RecordType type;
    private int expect;
    private String time;

    public MyRecordBean(String mum1, String mum2, String mum3, String mum4, String mum5, String mum6, String mum7, int status, RecordType type, int expect, String time) {
        this.mum1 = mum1;
        this.mum2 = mum2;
        this.mum3 = mum3;
        this.mum4 = mum4;
        this.mum5 = mum5;
        this.mum6 = mum6;
        this.mum7 = mum7;
        this.status = status;
        this.type = type;
        this.expect = expect;
        this.time = time;
    }

    @Override
    public String toString() {
        return "MyRecordBean{" +
                "mum1='" + mum1 + '\'' +
                ", mum2='" + mum2 + '\'' +
                ", mum3='" + mum3 + '\'' +
                ", mum4='" + mum4 + '\'' +
                ", mum5='" + mum5 + '\'' +
                ", mum6='" + mum6 + '\'' +
                ", mum7='" + mum7 + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", expect=" + expect +
                ", time='" + time + '\'' +
                '}';
    }

    public String getMum7() {
        return mum7;
    }

    public void setMum7(String mum7) {
        this.mum7 = mum7;
    }

    public RecordType getType() {
        return type;
    }

    public void setType(RecordType type) {
        this.type = type;
    }

    public int getExpect() {
        return expect;
    }

    public void setExpect(int expect) {
        this.expect = expect;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMum1() {
        return mum1;
    }

    public void setMum1(String mum1) {
        this.mum1 = mum1;
    }

    public String getMum2() {
        return mum2;
    }

    public void setMum2(String mum2) {
        this.mum2 = mum2;
    }

    public String getMum3() {
        return mum3;
    }

    public void setMum3(String mum3) {
        this.mum3 = mum3;
    }

    public String getMum4() {
        return mum4;
    }

    public void setMum4(String mum4) {
        this.mum4 = mum4;
    }

    public String getMum5() {
        return mum5;
    }

    public void setMum5(String mum5) {
        this.mum5 = mum5;
    }

    public String getMum6() {
        return mum6;
    }

    public void setMum6(String mum6) {
        this.mum6 = mum6;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    enum RecordType {
        MY,
        OFFICIAL
    }
}
