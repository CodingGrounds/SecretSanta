package com.bacon.secretsanta;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

//
public class MainActivity extends AppCompatActivity {

    /* A constant to identify permission request */
    private final int PERMISSION_REQUEST_SEND_SMS = 123;
    /* Database helper used to access data */
    private DatabaseHelper myDatabase;
    /* Text fields in the User Interface */
    private EditText editName, editPhone, editEmail;
    /* Button in the User Interface */
    private Button buttonAddPlayer, buttonShowPlayers, buttonUpdatePlayer, buttonSecretSanta, buttonDeleteAll;
    /* Radio buttons in the User Interface */
    private RadioButton radioPhone, radioEmail;
    /* Contact information */
    private String contactInformation = "", method = "ERROR";
    private TextView outputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabase = new DatabaseHelper(this);

        editName = (EditText)findViewById(R.id.name_editText);
        editPhone = (EditText)findViewById(R.id.phone_editText);
        editEmail = (EditText)findViewById(R.id.email_editText);

        buttonAddPlayer = (Button)findViewById(R.id.addPlayer_button);
        buttonShowPlayers = (Button)findViewById(R.id.showPlayers_button);
        buttonUpdatePlayer = (Button)findViewById(R.id.updatePlayer_button);
        buttonSecretSanta = (Button)findViewById(R.id.secretSanta_button);
        buttonDeleteAll = (Button)findViewById(R.id.delete_button);

        radioPhone = (RadioButton)findViewById(R.id.phone_radioButton);
        radioEmail = (RadioButton)findViewById(R.id.email_radioButton);

        outputText = (TextView)findViewById(R.id.output_textView);

        addPlayer();
        showPlayers();
        updatePlayer();
        clearEntries();
        secretSanta();
        getRadioSelection();
    }

    private void sendSms(String phoneNumber, String message){
        SmsManager smsManager = SmsManager.getDefault();

        try{
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        }
        catch(Exception e){
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Takes an Android permission and checks if it is allowed in the current application.
     * If the permission is not allowed this will ask the user to provide the permission
     * @param permission The permission in 'Manifest.permission.Desired_Permission' form
     * @param text The text to be displayed to the user about the permission
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission(final String permission, String text){
        // Checks to see if the app has the desired permission.
        int hasDesiredPermission = checkSelfPermission(permission);
        if(hasDesiredPermission != PackageManager.PERMISSION_GRANTED){
            // Does the permission require an explanation?
            if(shouldShowRequestPermissionRationale(permission)) {
                // Tells the user what the permission does.
                showPopupMessage(text,
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                requestPermissions(new String[] {Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SEND_SMS);
                        }
                });
            } else if (!shouldShowRequestPermissionRationale(permission)) {
                // Change this to pass an intent taking the user to settings.
                Toast.makeText(MainActivity.this, "This feature does not have the correct permissions and is disabled. Visit settings to change permission settings.", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Creates a popup in the current view prompting the user to confirm the message
     * @param message The message to be displayed
     */
    private void showPopupMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    /**
     * Records the state of the radio buttons. If the button is selected its value is true.
     */
    private void getRadioSelection(){
        radioPhone.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        checkPermission(Manifest.permission.SEND_SMS, "This app needs permission to send sms messages");
                    method = "SMS";
                }
            }
        );
        radioEmail.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    method = "EMAIL";
                }
            }
        );
    }

    /**
     * Turns the user specified information into a person object to insert into the database
     */
    private void addPlayer(){
        buttonAddPlayer.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(method.equals("SMS"))
                        contactInformation = editPhone.getText().toString();
                    else if(method.equals("EMAIL"))
                        contactInformation = editEmail.getText().toString();
                    String name = editName.getText().toString();

                    boolean isInserted = myDatabase.insertRecord(new Person(name, method, contactInformation));

                    if(isInserted)
                        Toast.makeText(MainActivity.this, R.string.database_add_success, Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, R.string.database_add_failure, Toast.LENGTH_LONG).show();

                    editName.getText().clear();
                    editPhone.getText().clear();
                    editEmail.getText().clear();
                }
            }
        );
    }

    /**
     * Shows all players in the text view
     */
    private void showPlayers(){
        buttonShowPlayers.setOnClickListener(
             new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Person[] persons = myDatabase.getAllRecords();
                    String result = "";
                    for (Person person : persons) {
                        result += person + "\n";
                    }
                    outputText.setText(result);
                }
            }
        );
    }

    /**
     * Updates a players information with the new user specified fields
     */
    private void updatePlayer(){
        buttonUpdatePlayer.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View view){

                    if(method.equals("SMS"))
                        contactInformation = editPhone.getText().toString();
                    else if(method.equals("EMAIL"))
                        contactInformation = editEmail.getText().toString();
                    String name = editName.getText().toString();

                    boolean isInserted = myDatabase.updateRecord(new Person(name, method, contactInformation));

                    if(isInserted)
                        Toast.makeText(MainActivity.this, R.string.database_update_success, Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, R.string.database_update_failure, Toast.LENGTH_LONG).show();
                }
            }
        );
    }

    /**
     * Deletes all entries in the database
     */
    private void clearEntries(){
        buttonDeleteAll.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    myDatabase.deleteAll();
                    Toast.makeText(MainActivity.this, R.string.database_delete, Toast.LENGTH_LONG).show();
                }
            }
        );
    }

    /**
     * Calls the secret santa class to randomly assign players giftees
     */
    private void secretSanta(){
        buttonSecretSanta.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Person[] persons = SecretSanta.assignPlayers(myDatabase.getAllRecords());
                    for (Person person : persons) {
                        myDatabase.updateRecord(person);
                        if(person.getContactMethod().equals("SMS"))
                            sendSms(person.getContactInformation(), "Your secret santa giftee is " + person.getGiftee());
                        Toast.makeText(MainActivity.this, "" + person, Toast.LENGTH_LONG).show();
                    }

                }
            }
        );
    }
}
