package com.bacon.secretsanta;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by Jason on 2016-10-19.
 */

public class Permissions {

    /* A constant to identify permission request */
    private final int PERMISSION_REQUEST_SEND_SMS = 123;
    /* The activity this class is used from */
    private Activity activity;

    public Permissions(Activity activity){
        this.activity = activity;
    }

    /**
     * Creates a popup in the current view prompting the user to confirm the message
     * @param message The message to be displayed
     */
    private void showPopupMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    /**
     * Takes an Android permission and checks if it is allowed in the current application.
     * If the permission is not allowed this will ask the user to provide the permission
     * @param permission The permission in 'Manifest.permission.Desired_Permission' form
     * @param text The text to be displayed to the user about the permission
     */
    @TargetApi(Build.VERSION_CODES.M)
    protected void checkPermission(final String permission, String text){
        // Checks to see if the app has the desired permission.
        int hasDesiredPermission = activity.checkSelfPermission(permission);
        if(hasDesiredPermission != PackageManager.PERMISSION_GRANTED){
            // Does the permission require an explanation?
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                // Tells the user what the permission does.
                showPopupMessage(text,
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SEND_SMS);
                            }
                        });
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                // Change this to pass an intent taking the user to settings.
                Toast.makeText(activity, "This feature does not have the correct permissions and is disabled. Visit settings to change permission settings.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SEND_SMS);
            }
        }
    }

    /**
     * Sends SMS message to a desired phone number
     * @param phoneNumber The phone number to sent the message to
     * @param message The SMS message to send
     */
    public static void sendSms(String phoneNumber, String message, Context context){
        SmsManager smsManager = SmsManager.getDefault();

        try{
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        }
        catch(Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
