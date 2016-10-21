package com.bacon.secretsanta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

public class EditPlayer extends AppCompatActivity {

    /* Button in the User Interface */
    private Button buttonUpdatePlayer, buttonDeleteAll;
    /* Home button */
    ImageButton buttonHome;
    /* Text fields in the User Interface */
    private EditText editName, editPhone, editEmail;
    /* Radio buttons in the User Interface */
    private RadioButton radioPhone, radioEmail;
    /* Intent from main activity containing the database helper */
    private Intent home;
    /* Database accessor */
    private DatabaseHelper myDatabase;
    /* Contact information */
    private String contactInformation, method = "ERROR";
    /* Permissions object */
    private Permissions permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);

        myDatabase = new DatabaseHelper(this);

        editName = (EditText)findViewById(R.id.name_editText);
        editPhone = (EditText)findViewById(R.id.phone_editText);
        editEmail = (EditText)findViewById(R.id.email_editText);

        radioPhone = (RadioButton)findViewById(R.id.phone_radioButton);
        radioEmail = (RadioButton)findViewById(R.id.email_radioButton);

        //buttonHome = (ImageButton)findViewById(R.id.home_imageButton);
        buttonUpdatePlayer = (Button)findViewById(R.id.updatePlayer_button);
        buttonDeleteAll = (Button)findViewById(R.id.deletePlayer_button);

        updatePlayer();
        clearEntries();
        //home();
    }

    private void home(){
        buttonHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                home = new Intent(EditPlayer.this, MainActivity.class);
                startActivity(home);
            }
        });
    }

    /**
     * Updates a players information with the new user specified fields
     */
    private void updatePlayer(){
        buttonUpdatePlayer.setOnClickListener(
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
                            boolean success = myDatabase.updateRecord(new Person(name, method, contactInformation));
                            Toast.makeText(EditPlayer.this, R.string.database_update_success, Toast.LENGTH_SHORT).show();
                            Toast.makeText(EditPlayer.this, String.valueOf(success), Toast.LENGTH_SHORT).show();
                        }
                        catch(Exception e){
                            Toast.makeText(EditPlayer.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    /**
     * Deletes selected record
     */
    private void clearEntries(){
        buttonDeleteAll.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        try{
                            boolean success = myDatabase.deleteRecord(editName.getText().toString());
                            Toast.makeText(EditPlayer.this, R.string.database_deleteIndividual, Toast.LENGTH_SHORT).show();
                            Toast.makeText(EditPlayer.this, String.valueOf(success), Toast.LENGTH_SHORT).show();
                        }
                        catch(Exception e){
                            Toast.makeText(EditPlayer.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}
