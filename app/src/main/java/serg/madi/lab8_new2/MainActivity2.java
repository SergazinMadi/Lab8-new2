package serg.madi.lab8_new2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity2 extends AppCompatActivity {

    private EditText randomCharacterEditText;
    private BroadcastReceiver broadcastReceiver;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Инициализация элементов интерфейса
        randomCharacterEditText = findViewById(R.id.editText_randomCharacter);

        // Создаём BroadcastReceiver для получения данных
        broadcastReceiver = new MyBroadcastReceiver();

        // Подготавливаем Intent для запуска сервиса
        serviceIntent = new Intent(getApplicationContext(), RandomCharacterService.class);
    }

    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.button_start) {
            startService(serviceIntent);
        } else if (id == R.id.button_end) {
            stopService(serviceIntent);
            randomCharacterEditText.setText("");
        }
    }


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("my.custom.action.tag.lab6");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(broadcastReceiver, intentFilter);
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        // Отменяем регистрацию приёмника
        unregisterReceiver(broadcastReceiver);
    }

    // Внутренний класс-приемник широковещательных сообщений
    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                char data = intent.getCharExtra("randomCharacter", '?');
                Log.i("MyBroadcastReceiver", "Received character: " + data);

                runOnUiThread(() -> {
                    randomCharacterEditText.setText(String.valueOf(data));
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
