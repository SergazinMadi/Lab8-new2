package serg.madi.lab8_new2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private Intent serviceIntent;
    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceIntent = new Intent(this, MyService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions();
        }
    }

    private void requestPermissions() {
        String[] permissions = {
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
        };

        if (!hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean hasPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void startService(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Требуется разрешение", Toast.LENGTH_LONG).show();
            requestPermissions();
            return;
        }
        try {
            ContextCompat.startForegroundService(this, serviceIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка запуска сервиса", Toast.LENGTH_LONG).show();
        }
    }

    public void nextActivity(View view) {
        startActivity(new Intent(this, MainActivity2.class));
    }

    public void stopService(View view) {
        try {
            stopService(serviceIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка остановки сервиса", Toast.LENGTH_LONG).show();
        }
    }
}
