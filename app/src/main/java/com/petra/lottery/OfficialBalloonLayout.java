package com.petra.lottery;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by petra on 2/4/2017.
 */

public class OfficialBalloonLayout extends RelativeLayout implements View.OnTouchListener, GestureDetector.OnGestureListener {
  private OfficialBalloonChangeListener officialBalloonChangedListener;

  public OfficialBalloonLayout(Context context) {
    super(context);
  }

  public OfficialBalloonLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public OfficialBalloonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override public boolean onDown(MotionEvent e) {
    return false;
  }

  @Override public void onShowPress(MotionEvent e) {

  }

  @Override public boolean onSingleTapUp(MotionEvent e) {
    return false;
  }

  @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    return false;
  }

  @Override public void onLongPress(MotionEvent e) {

  }

  @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    officialBalloonChangedListener.onOfficialBalloonChanged();
    return false;
  }

  @Override public boolean onTouch(View v, MotionEvent event) {
    return false;
  }

  public void setOfficialBalloonChangedListener(OfficialBalloonChangeListener listener) {
    this.officialBalloonChangedListener = listener;
  }
}
