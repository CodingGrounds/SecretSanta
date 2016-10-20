package com.bacon.secretsanta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ViewPlayers extends AppCompatActivity {

    /* Output text view */
    private TextView output;
    /* Home button */
    ImageButton buttonHome;
    /* Database accessor */
    private DatabaseHelper myDatabase;
    /* Intent from main activity containing the database helper */
    private Intent main, home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_players);

        main = getIntent();
        myDatabase = new DatabaseHelper(this);//(DatabaseHelper)main.getSerializableExtra(MainActivity.EXTRA_MESSAGE);

        buttonHome = (ImageButton)findViewById(R.id.home_imageButton);
        output = (TextView) findViewById(R.id.output_textView);

        showPlayers();
        home();
    }

    private void home(){
        buttonHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                home = new Intent(ViewPlayers.this, MainActivity.class);
                startActivity(home);
            }
        });
    }

    /**
     * Shows all players in the text view
     */
    private void showPlayers(){
        Person[] persons = myDatabase.getAllRecords();
        String result = "";
        for (Person person : persons) {
            result += person + "\n";
        }
        output.setText(result);
    }
}
