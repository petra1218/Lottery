package com.petra.lottery;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by petra on 7/31/2016.
 */
public class MyBalloonBean implements Serializable {
  private String[] redNums;
  private String blueNum;
  private String time;
  private String expect;

  public MyBalloonBean(String[] redNums, String blueNum, String time, String expect) {
    this.redNums = redNums;
    this.blueNum = blueNum;
    this.time = time;
    this.expect = expect;
  }

  @Override public String toString() {
    return "MyBalloonBean{" +
        "redNums=" + Arrays.toString(redNums) +
        ", blueNum='" + blueNum + '\'' +
        ", time='" + time + '\'' +
        '}';
  }

  public String[] getRedNums() {
    return redNums;
  }

  public void setRedNums(String[] redNums) {
    this.redNums = redNums;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getBlueNum() {
    return blueNum;
  }

  public void setBlueNum(String blueNum) {
    this.blueNum = blueNum;
  }

  public String getExpect() {
    return expect;
  }

  public void setExpect(String expect) {
    this.expect = expect;
  }
}
