package com.brandonfrie.janus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ManageNamesActivity extends AppCompatActivity {
    public static final String NAMES_LIST_PREF = "names_list";

    // Data Elements
    private SharedPreferences settings;
    private ArrayList<String> names;
    private Gson gson;

    // UI Elements
    EditText nameInputText;
    ListView namesListView;
    ArrayAdapter<String> listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_names);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setup the view
        initializeComponents();

        Log.d(this.getLocalClassName(), names.toString());
    }

    public void readListJson() {
        String json = settings.getString(NAMES_LIST_PREF, "[]");
        names = gson.fromJson(json, new TypeToken<ArrayList<String>>(){}.getType());
        listViewAdapter.notifyDataSetChanged();
    }

    public void saveListJson() {
        String json = gson.toJson(names);
        SharedPreferences.Editor settingsEditor = settings.edit();
        settingsEditor.putString(NAMES_LIST_PREF, json);
        settingsEditor.apply();
    }

    private void initializeComponents() {
        // setup data objects
        settings = getSharedPreferences(MainActivity.GLOBAL_PREFERENCES, MODE_PRIVATE);
        names = new ArrayList<>();
        gson = new Gson();

        // setup widget references
        nameInputText = findViewById(R.id.manage_names_name_input);
        namesListView = findViewById(R.id.manage_names_list_view);
        listViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);

        // handle specific configuration items
        configureListView();
        configureAddButton();
        configureSaveButton();
        configureCancelButton();

        // load the data
        readListJson();
    }

    private void configureAddButton() {
        Button btn = findViewById(R.id.manage_names_add_name_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInputText.getText().toString();
                if(name.length() > 0) {
                    names.add(name);
                    listViewAdapter.notifyDataSetChanged();
                    nameInputText.setText("");
                }
            }
        });
    }

    private void configureListView() {
        namesListView.setAdapter(listViewAdapter);
        namesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("User clicked ", names.get(position));
            }
        });
    }

    private void configureSaveButton() {
        Button btn = findViewById(R.id.manage_names_save_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveListJson();
                finish();
            }
        });
    }

    private void configureCancelButton() {
        Button btn = findViewById(R.id.manage_names_cancel_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}