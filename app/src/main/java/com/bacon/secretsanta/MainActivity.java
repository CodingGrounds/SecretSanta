package com.bacon.secretsanta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    /* Database helper used to access data */
    protected static DatabaseHelper myDatabase;
    /* Button in the User Interface */
    private Button buttonAddPlayers, buttonEditPlayers, buttonShowPlayers, buttonSecretSanta;
    /* Intent to start utility activities */
    private Intent intentAddPlayers, intentEditPlayers, intentShowPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabase = new DatabaseHelper(this);

        buttonAddPlayers = (Button)findViewById(R.id.addPlayers_button);
        buttonEditPlayers = (Button)findViewById(R.id.editPlayers_button);
        buttonShowPlayers = (Button)findViewById(R.id.showPlayers_button);
        buttonSecretSanta = (Button)findViewById(R.id.secretSanta_button);

        addPlayers();
        editPlayers();
        showPlayers();
        secretSanta();
    }

    /**
     * Stars add player activity
     */
    private void addPlayers(){
        buttonAddPlayers.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                intentAddPlayers = new Intent(MainActivity.this, AddPlayer.class);
                startActivity(intentAddPlayers);
            }
        });
    }

    /**
     * Starts edit player activity
     */
    private void editPlayers(){
        buttonEditPlayers.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                intentEditPlayers = new Intent(MainActivity.this, EditPlayer.class);
                startActivity(intentEditPlayers);
            }
        });
    }

    /**
     * Starts show players activity
     */
    private void showPlayers(){
        buttonShowPlayers.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                intentShowPlayers = new Intent(MainActivity.this, ViewPlayers.class);
                startActivity(intentShowPlayers);
            }
        });
    }

    /**
     * Calls the secret santa class to randomly assign players giftees
     */
    private void secretSanta(){
        buttonSecretSanta.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    ExternalContact async = new ExternalContact(MainActivity.this);
                    async.execute(SecretSanta.assignPlayers(myDatabase.getAllRecords()));
                }
            }
        );
    }
}
