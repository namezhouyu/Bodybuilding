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
    edtPart.setText(Application.getInstance().getGroupPartTime() + "");
    edtRest.setText(Application.getInstance().getGroupRestTime() + "");
    edtCount.setText(Application.getInstance().getGroupCount() + "");
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
          Application.getInstance().setGroupPartTime(partTime);
          Application.getInstance().setGroupRestTime(restTime);
          Application.getInstance().setGroupCount(countNo);
          for (int i = 0; i < countNo; i++) {
            Runnable runnable = new Runnable() {
              @Override public void run() {
                try {
                  System.out.println("组开始");
                  SoundsHelper.get().play1();
                  for (int j = 0; j < partTime; j++) {
                    Thread.sleep(1000);
                    second++;
                    handler.sendEmptyMessage(0);
                  }
                  System.out.println("组结束，开始休息");
                  SoundsHelper.get().play2();
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
    executorService.shutdownNow();
    executorService = null;
    handler.removeCallbacksAndMessages(null);
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
      txtSecond.setText(second + "s");
    }
  };
}
