package cn.cattlefish.bodybuilding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
  TextView txtSecond;
  ExecutorService executorService;
  Stack<Runnable> stacks = new Stack<Runnable>();
  int second = 0;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    txtSecond = (TextView) findViewById(R.id.txt_second);
    final EditText edtPart = (EditText) findViewById(R.id.edit_part);
    final EditText edtRest = (EditText) findViewById(R.id.edit_rest);
    final EditText edtCount = (EditText) findViewById(R.id.edit_count);
    edtPart.setText(BApplication.getInstance().getGroupPartTime() + "");
    edtRest.setText(BApplication.getInstance().getGroupRestTime() + "");
    edtCount.setText(BApplication.getInstance().getGroupCount() + "");
    findViewById(R.id.btn_terminal).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        stopService();
      }
    });
    findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        String part = edtPart.getText().toString();
        String rest = edtRest.getText().toString();
        String count = edtCount.getText().toString();
        if (!TextUtils.isEmpty(part) && !TextUtils.isEmpty(rest) && !TextUtils.isEmpty(count)) {
          final int partTime = Integer.valueOf(part);
          final int restTime = Integer.valueOf(rest);
          final int countNo = Integer.valueOf(count);
          BApplication.getInstance().setGroupPartTime(partTime);
          BApplication.getInstance().setGroupRestTime(restTime);
          BApplication.getInstance().setGroupCount(countNo);
          for (int i = 0; i < countNo; i++) {
            Runnable runnable = new Runnable() {
              @Override public void run() {
                try {
                  System.out.println("组开始");
                  Message message = new Message();
                  message.what = Play.START.getCode();
                  handler.sendMessage(message);

                  for (int j = 0; j < partTime; j++) {
                    Thread.sleep(1000);
                    second++;

                    handler.sendEmptyMessage(0);
                  }
                  System.out.println("组结束，开始休息");

                  message = new Message();
                  message.what = Play.END.getCode();
                  handler.sendMessage(message);
                  for (int j = 0; j < restTime; j++) {
                    Thread.sleep(1000);
                    second++;
                    handler.sendEmptyMessage(0);
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            };
            stacks.push(runnable);
          }
          executorService = Executors.newSingleThreadExecutor();
          while (!stacks.isEmpty()) {
            executorService.execute(stacks.pop());
          }
        } else {
          toastShow("请填写全部参数");
        }
      }
    });
  }

  private void toastShow(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  }

  private void stopService() {
    if (null != executorService) {
      executorService.shutdownNow();
      executorService = null;
    }
    if (null != handler) {
      handler.removeCallbacksAndMessages(null);
      handler = null;
    }
    second = 0;
    txtSecond.setText("0s");
    stacks.clear();
  }

  @Override protected void onDestroy() {
    stopService();
    super.onDestroy();
  }

  Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      int code = msg.what;
      if (0 == code) {
        txtSecond.setText(second + "s");
      } else if (Play.START.getCode() == code) {
        SoundsHelper.get(MainActivity.this).play1();
      } else if (Play.END.getCode() == code) {
        SoundsHelper.get(MainActivity.this).play2();
      }
    }
  };

  enum Play {
    START(1), END(2);
    int code;

    Play(int code) {
      this.code = code;
    }

    public int getCode() {
      return code;
    }
  }
}
