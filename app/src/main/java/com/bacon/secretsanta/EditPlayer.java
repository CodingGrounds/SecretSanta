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
    private Intent main, home;
    /* Database accessor */
    private DatabaseHelper myDatabase;
    /* Contact information */
    private String contactInformation = "", method = "ERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);

        main = getIntent();
        myDatabase = new DatabaseHelper(this);//(DatabaseHelper)main.getSerializableExtra(MainActivity.EXTRA_MESSAGE);

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

                    if(method.equals("SMS"))
                        contactInformation = editPhone.getText().toString();
                    else if(method.equals("EMAIL"))
                        contactInformation = editEmail.getText().toString();
                    String name = editName.getText().toString();

                    boolean isInserted = myDatabase.updateRecord(new Person(name, method, contactInformation));

                    if(isInserted)
                        Toast.makeText(EditPlayer.this, R.string.database_update_success, Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(EditPlayer.this, R.string.database_update_failure, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(EditPlayer.this, R.string.database_delete, Toast.LENGTH_LONG).show();
                }
            }
        );
    }
}
