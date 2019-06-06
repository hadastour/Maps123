package com.example.hadastourgeman.maps123;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Credits extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);


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

    public void tomain(View view) {
        Intent n = new Intent(this, MainActivity.class);
        startActivity(n);
    }
}
