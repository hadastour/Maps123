package com.example.hadastourgeman.maps123;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AlphaSQ extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    ListView listView;
    DatabaseReference ref;
    String n=" ";
    List<String> dates=new ArrayList<>();
    List<String> values;
    int[] arr = new int[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alpha_sq);

        spinner = findViewById(R.id.spinner);
        listView = findViewById(R.id.listView);

        ref = FirebaseDatabase.getInstance().getReference("dataRec");

        dates = new ArrayList<>();
        values = new ArrayList<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String date = ds.getKey();
                    dates.add(date);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        dates.add("Choose date:");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dates);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ref.child(dates.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                values.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String value = ds.getValue(String.class);

                    for (int i = 0; i < 8; i += 2) {
                        String num = "" + value.charAt(i) + value.charAt(i + 1);
                        arr[i / 2] = Integer.parseInt(num, 16);
                        value= value +"  "+arr[i / 2];
                    }

                    values.add(value);
                }



                ArrayAdapter<String> listAdapter = new ArrayAdapter(AlphaSQ.this,
                        android.R.layout.simple_spinner_item, values);
                listView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String st = item.getTitle().toString();

        if (st.equals("MAIN")) {
            Intent p = new Intent(this, MainActivity.class);
            startActivity(p);
        }
        if (st.equals("BLUETOOTH")) {
            Intent p = new Intent(this, BluetoothAlpha.class);
            startActivity(p);
        }
        if (st.equals("RECORDS")) {
            Intent p = new Intent(this, AlphaSQ.class);
            startActivity(p);
        }
        if (st.equals("CREDITS")) {
            Intent p = new Intent(this, Credits.class);
            startActivity(p);
        }

        return true;
    }
}