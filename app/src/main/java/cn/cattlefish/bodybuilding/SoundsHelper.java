package cn.cattlefish.bodybuilding;


/*
* Copyright (C) 2016-2017 Yaomaitong Inc.All Rights Reserved.
* FileName：SoundsHelper
* @Description：简要描述本文件的内容
* History：
* v1.0 danggui 2017/6/28 Create
*/

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;

public class SoundsHelper {
  private static SoundsHelper soundsHelper;
  private SoundPool soundpool = null;
  private int soundid;

  public static SoundsHelper get() {
    if (null == soundsHelper) {
      soundsHelper = new SoundsHelper();
      soundsHelper.setSoundpool(new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100));
    }
    return soundsHelper;
  }

  private void setSoundpool(SoundPool soundpool) {
    this.soundpool = soundpool;
  }

  public void play1() {
    soundid = soundpool.load(
        android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/开始.ogg", 1);
    playSound();
  }

  public void play2() {
    soundid = soundpool.load(
        android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/休息.ogg", 1);
    playSound();
  }

  private void playSound() {
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        soundpool.play(soundid, 1, 1, 0, 0, 1);
      }
    }, 500);
  }
}
