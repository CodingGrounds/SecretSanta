package com.bacon.secretsanta;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddPlayer extends AppCompatActivity {

    /* Text fields in the User Interface */
    private EditText editName, editPhone, editEmail;
    /* Button in the User Interface */
    private Button buttonAddPlayer;
    /* Home button */
    ImageButton buttonHome;
    /* Radio buttons in the User Interface */
    private RadioButton radioPhone, radioEmail;
    /* Database accessor */
    private DatabaseHelper myDatabase;
    /* Intent from main activity containing the database helper */
    private Intent home;
    /* Contact information */
    private String contactInformation = "", method = "ERROR";
    /* Permissions object */
    private Permissions permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        myDatabase = new DatabaseHelper(this);//(DatabaseHelper)main.getSerializableExtra(MainActivity.EXTRA_MESSAGE);

        //buttonHome = (ImageButton)findViewById(R.id.home_imageButton);
        buttonAddPlayer = (Button)findViewById(R.id.addPlayer_button);

        editName = (EditText)findViewById(R.id.name_editText);
        editPhone = (EditText)findViewById(R.id.phone_editText);
        editEmail = (EditText)findViewById(R.id.email_editText);

        radioPhone = (RadioButton)findViewById(R.id.phone_radioButton);
        radioEmail = (RadioButton)findViewById(R.id.email_radioButton);

        addPlayer();
        getRadioSelection();
        //home();
    }

    private void home(){
        buttonHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                home = new Intent(AddPlayer.this, MainActivity.class);
                startActivity(home);
            }
        });
    }

    /**
     * Records the state of the radio buttons. If the button is selected its value is true.
     */
    private void getRadioSelection(){
        radioPhone.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        permissions = new Permissions(AddPlayer.this);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            permissions.checkPermission(Manifest.permission.SEND_SMS, "This app needs permission to send sms messages");
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
                        switch (method) {
                            case "SMS":
                                contactInformation = editPhone.getText().toString();
                                break;
                            case "EMAIL":
                                contactInformation = editEmail.getText().toString();
                                break;
                            default:
                                contactInformation = "ERROR";
                                break;
                        }
                        String name = editName.getText().toString();

                        try{
                            boolean success = myDatabase.insertRecord(new Person(name, method, contactInformation));
                            Toast.makeText(AddPlayer.this, R.string.database_add_success, Toast.LENGTH_SHORT).show();
                            Toast.makeText(AddPlayer.this, String.valueOf(success), Toast.LENGTH_SHORT).show();
                        }
                        catch(Exception e){
                            Toast.makeText(AddPlayer.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        finally{
                            editName.getText().clear();
                            editPhone.getText().clear();
                            editEmail.getText().clear();
                        }
                    }
                }
        );
    }
}