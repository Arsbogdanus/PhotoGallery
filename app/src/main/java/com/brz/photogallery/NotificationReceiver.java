package com.brz.photogallery;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context c, Intent i) {
        Log.i(TAG, "received result: " + getResultCode());

        if (getResultCode() != Activity.RESULT_OK) {
            // Активность переднего плана отменила рассылку
            return;
        }

        int requestCode = i.getIntExtra(PollService.REQUEST_CODE, 0);
        Notification notification = (Notification) i.getParcelableExtra(PollService.NOTIFICATION);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(c);

        // Проверяем, есть ли разрешение на отправку уведомлений
        if (notificationManager.areNotificationsEnabled()) {
            try {
                notificationManager.notify(requestCode, notification);
            } catch (SecurityException e) {
                // Обрабатываем исключение SecurityException
                Log.e(TAG, "Ошибка отправки уведомления: " + e.getMessage());
            }
        } else {
            // Логируем предупреждение, если уведомления не разрешены для приложения
            Log.w(TAG, "Уведомления не разрешены для приложения");
        }
    }
}

