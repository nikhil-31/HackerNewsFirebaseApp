package in.co.nikhil.hackernewsfirebaseapp.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.co.nikhil.hackernewsfirebaseapp.R;


public class FirebaseMessaging extends FirebaseMessagingService {
  private static final String LOG_TAG = FirebaseMessaging.class.getSimpleName();

  public FirebaseMessaging() {
  }

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {

    if (remoteMessage.getNotification() != null) {
      String body = remoteMessage.getNotification().getBody();
      String title = remoteMessage.getNotification().getTitle();

      int currentApiVersion = Build.VERSION.SDK_INT;
      if (currentApiVersion >= Build.VERSION_CODES.O) {
        sendNotificationChannel(title, body);
      } else {
        sendNotificationOld(title, body);
      }
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  private void sendNotificationChannel(String title, String body) {

    String name = "Story Updates";
    int notificationId = 1;
    NotificationChannel updateChannel = new NotificationChannel(name
        , "Update channel"
        , NotificationManager.IMPORTANCE_HIGH);

    updateChannel.setLightColor(Color.GREEN);

    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    if (notificationManager != null) {
      notificationManager.createNotificationChannel(updateChannel);
      Notification notification = new Notification.Builder(getApplicationContext())
          .setContentTitle(title)
          .setContentText(body)
          .setSmallIcon(R.drawable.ic_launcher_foreground)
          .setChannelId(name)
          .build();

      notificationManager.notify(notificationId, notification);
    }

  }

  private void sendNotificationOld(String title, String body) {
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(body)
        .setStyle(new NotificationCompat.BigTextStyle().bigText(body));

    NotificationManager notificationManager = (NotificationManager) getApplicationContext()
        .getSystemService(Context.NOTIFICATION_SERVICE);
    if (notificationManager != null) {
      notificationManager.notify(1, mBuilder.build());
    }
  }
}

