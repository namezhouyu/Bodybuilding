package cn.cattlefish.bodybuilding;


/*
* Copyright (C) 2016-2017 Yaomaitong Inc.All Rights Reserved.
* FileName：BApplication
* @Description：简要描述本文件的内容
* History：
* v1.0 danggui 2017/7/3 Create
*/

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

public class BApplication extends android.app.Application {
  private static final String GROUP_PART_TIME_KEY = "group_part_time_key";//一组的时间
  private static final String GROUP_REST_TIME_KEY = "group_rest_time_key";//组间休息时间
  private static final String GROUP_COUNT_KEY = "group_count_key";//组数
  private SharedPreferences sharedPreferences;
  private static BApplication instance;
  public static final String APP_ID = "52641772a1";

  @Override public void onCreate() {
    super.onCreate();
    instance = (BApplication) getApplicationContext();
    sharedPreferences = getSharedPreferences("body_building", Context.MODE_PRIVATE);
    Beta.autoInit = true;
    Beta.largeIconId = R.mipmap.ic_launcher;
    Beta.smallIconId = R.mipmap.ic_launcher;
    Beta.storageDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    Beta.showInterruptedStrategy = false;
    Bugly.init(getApplicationContext(), APP_ID, true);
  }

  public static BApplication getInstance() {
    return instance;
  }

  public void setGroupPartTime(int groupTime) {
    sharedPreferences.edit().putInt(GROUP_PART_TIME_KEY, groupTime).apply();
  }

  public int getGroupPartTime() {
    return sharedPreferences.getInt(GROUP_PART_TIME_KEY, 30);
  }

  public void setGroupRestTime(int groupTime) {
    sharedPreferences.edit().putInt(GROUP_REST_TIME_KEY, groupTime).apply();
  }

  public int getGroupRestTime() {
    return sharedPreferences.getInt(GROUP_REST_TIME_KEY, 5);
  }

  public void setGroupCount(int count) {
    sharedPreferences.edit().putInt(GROUP_COUNT_KEY, count).apply();
  }

  public int getGroupCount() {
    return sharedPreferences.getInt(GROUP_COUNT_KEY, 4);
  }
}
