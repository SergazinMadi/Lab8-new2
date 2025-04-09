package serg.madi.lab8_new2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Random;

public class RandomCharacterService extends Service {

    private static final String TAG = "RandomCharacterService";
    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final int MIN = 0;
    private static final int MAX = ALPHABET.length;

    private boolean isRandomGeneratorOn;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Service started...");
        Log.i(TAG, "In onStartCommand - Thread ID: " + Thread.currentThread().getId());

        isRandomGeneratorOn = true;

        new Thread(() -> startRandomGenerator()).start();

        return START_STICKY;
    }

    private void startRandomGenerator() {
        Random random = new Random();

        while (isRandomGeneratorOn) {
            try {
                Thread.sleep(1000);

                if (isRandomGeneratorOn) {
                    int randomIdx = random.nextInt(MAX - MIN) + MIN;
                    char randomCharacter = ALPHABET[randomIdx];

                    Log.i(TAG, "Thread ID: " + Thread.currentThread().getId() +
                            ", Random Character: " + randomCharacter);

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("my.custom.action.tag.lab6");
                    broadcastIntent.putExtra("randomCharacter", randomCharacter);
                    sendBroadcast(broadcastIntent);
                }
            } catch (InterruptedException e) {
                Log.i(TAG, "Thread Interrupted.");
            }
        }
    }

    private void stopRandomGenerator() {
        isRandomGeneratorOn = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomGenerator();
        Toast.makeText(getApplicationContext(), "Service Stopped", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Service Destroyed...");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
