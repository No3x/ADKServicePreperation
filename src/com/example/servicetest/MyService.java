package com.example.servicetest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
	private static String TAG = MyService.class.getSimpleName();
	// Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;
	private NotificationManager mNM;
	
	/**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
    	MyService getService() {
            return MyService.this;
        }
    }

    @Override
    public void onCreate() {
    	 Log.i(TAG, "onCreate()");
    	 mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
    	 //!!! Create new Thread!
    	 
    	 //Check for Accessory
    	 
        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
    	
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        
        CharSequence title = getText(R.string.local_service_label);
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.local_service_started);
       
        // Set the icon, scrolling text
        Notification notification = new NotificationCompat.Builder( this )
        .setContentTitle(title)
        .setContentText(text)
        .setNumber(42)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentIntent(contentIntent)
        .setLargeIcon( BitmapFactory.decodeResource( getResources(), R.drawable.ic_launcher) )
        .build();
        

        // Set the info for the views that show in the notification panel.
        //notification.setLatestEventInfo(this, getText(R.string.local_service_label), text, contentIntent);
        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }
}
