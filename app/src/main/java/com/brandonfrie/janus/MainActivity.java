package com.brandonfrie.janus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String GLOBAL_PREFERENCES = "janus_preferences";
    private static final String PREVENT_CONSEC_SEL = "prevent_consecutive_selections";

    int lastSelectionIndex = -1;
    List<String> names = new ArrayList<>();
    boolean preventConsecutiveSelections;

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadSettings();
        configureExecuteButton();
        configureSwitch();

        // TODO: remove debug method call
        loadMockData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intentEditNames = new Intent(this, ManageNamesActivity.class);
            startActivity(intentEditNames);
        } else if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int calculateWinnerIndex() {
        Random r = new Random();
        int idx = r.nextInt(names.size());
        while(preventConsecutiveSelections && (idx==lastSelectionIndex)) {
            idx = r.nextInt(names.size());
        }
        lastSelectionIndex = idx;
        return idx;
    }

    public void rollDiceAndDisplayWinner() {
        int idx = calculateWinnerIndex();
        TextView winnerTxt = findViewById(R.id.textView);
        winnerTxt.setText(names.get(idx));
    }

    private void loadMockData() {
        names.add("Brandon");
        names.add("John");
        names.add("Iliana");
        names.add("Angela");
    }

    private void loadSettings() {
        settings = getSharedPreferences(GLOBAL_PREFERENCES, MODE_PRIVATE);
        preventConsecutiveSelections = settings.getBoolean(PREVENT_CONSEC_SEL, true);
        lastSelectionIndex = -1;
    }

    private void configureSwitch() {
        Switch sw = findViewById(R.id.main_switch_prevent_repeat_selections);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                preventConsecutiveSelections = isChecked;
            }
        });
    }

    private void configureExecuteButton() {
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDiceAndDisplayWinner();
            }
        });
    }

}