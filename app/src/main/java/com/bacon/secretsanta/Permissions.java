package com.bacon.secretsanta;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Jason on 2016-10-19.
 */

public class Permissions {

    /* A constant to identify permission request */
    private final int PERMISSION_REQUEST_SEND_SMS = 123;
    private final int PERMISSION_REQUEST_INTERNET = 234;
    /* variable to keep track of what permission to request */
    private int PERMISSION_REQUEST;
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
        switch(permission){
            case Manifest.permission.INTERNET:
                PERMISSION_REQUEST = PERMISSION_REQUEST_INTERNET;
                break;
            case Manifest.permission.SEND_SMS:
                PERMISSION_REQUEST = PERMISSION_REQUEST_SEND_SMS;
                break;
            default:
                PERMISSION_REQUEST = 0;
                break;
        }
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
                                ActivityCompat.requestPermissions(activity, new String[] {permission}, PERMISSION_REQUEST);
                            }
                        });
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                // Change this to pass an intent taking the user to settings.
                Toast.makeText(activity, "This feature does not have the correct permissions and is disabled. Visit settings to change permission settings.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(activity, new String[] {permission}, PERMISSION_REQUEST);
            }
            ActivityCompat.requestPermissions(activity, new String[] {permission}, PERMISSION_REQUEST);
        }
    }
}
