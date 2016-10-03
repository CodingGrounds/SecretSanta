package com.bacon.secretsanta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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

    /**
     * Records the state of the radio buttons. If the button is selected its value is true.
     */
    public void getRadioSelection(){
        radioPhone.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View view){
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
    public void addPlayer(){
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
    public void showPlayers(){
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
    public void updatePlayer(){
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
    public void clearEntries(){
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
    public void secretSanta(){
        buttonSecretSanta.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Person[] persons = SecretSanta.assignPlayers(myDatabase.getAllRecords());
                    for (Person person : persons) {
                        myDatabase.updateRecord(person);
                        Toast.makeText(MainActivity.this, "" + person, Toast.LENGTH_LONG).show();
                    }

                }
            }
        );
    }
}
