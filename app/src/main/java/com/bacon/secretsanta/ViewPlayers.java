package com.bacon.secretsanta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ViewPlayers extends AppCompatActivity {

    /* Output text view */
    private TextView output;
    /* Button in activity */
    private Button buttonDeleteAll;
    /* Home button */
    private ImageButton buttonHome;
    /* Database accessor */
    private DatabaseHelper myDatabase;
    /* Intent from main activity containing the database helper */
    private Intent home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_players);

        myDatabase = new DatabaseHelper(this);

        buttonDeleteAll = (Button)findViewById(R.id.deleteAll_button);
        buttonHome = (ImageButton)findViewById(R.id.home_imageButton);
        output = (TextView) findViewById(R.id.output_textView);

        showPlayers();
        deleteAll();
        home();
    }

    /**
     * Returns to the main activity
     */
    private void home(){
        buttonHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                home = new Intent(ViewPlayers.this, MainActivity.class);
                startActivity(home);
            }
        });
    }

    private void deleteAll(){
        buttonDeleteAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                myDatabase.deleteAll();
                showPlayers();
            }
        });
    }

    /**
     * Shows all players in the text view
     */
    private void showPlayers(){
        try{
            Person[] persons = myDatabase.getAllRecords();
            String result = "";
            for (Person person : persons) {
                result += person + "\n";
            }
            output.setText(result);
        }
        catch(Exception e) {
            output.setText(e.getMessage());
        }
    }
}
