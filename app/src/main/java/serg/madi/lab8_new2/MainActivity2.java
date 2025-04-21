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

        randomCharacterEditText = findViewById(R.id.editText_randomCharacter);

        broadcastReceiver = new MyBroadcastReceiver();

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

    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                char data = intent.getCharExtra("randomCharacter", '?');
                Log.i("MyBroadcastReceiver", "Received character: " + data);

                runOnUiThread(() -> randomCharacterEditText.setText(data));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
