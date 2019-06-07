package com.example.hadastourgeman.maps123;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    DatabaseReference mRootRef;
    DatabaseReference dbRecRef;
    public StringBuilder message;

    TextView des1;
    TextView des2;
    SeekBar sk1;
    SeekBar sk2;
    final static int RQS_TIME = 1;
    int[] values;
    int mone;


    TextView tvAlarmPrompt;
    TextView tv;
    int rngl, rngr, head;
    byte elev;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        des1 = (TextView) findViewById(R.id.des1);
        des2 = (TextView) findViewById(R.id.des2);

        sk1 = (SeekBar) findViewById(R.id.sk1);
        sk2 = (SeekBar) findViewById(R.id.sk2);
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();


        mRootRef = FirebaseDatabase.getInstance().getReference();
        dbRecRef = mRootRef.child("dataRec");
        DateFormat df = new SimpleDateFormat("yy/dd/MM HH:mm:ss");
        Date dateobj = new Date();
        String date = "" + df.format(dateobj);
        tv.setText(""+date);



        rngl = 0;
        rngr = 200;
        head = 0;
        elev = -90;

        message = new StringBuilder();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReciver, new IntentFilter("incomingMessage"));


    }

    // check the information from bluetooth
    public static boolean check(String s) {
        if (s.length() != 8)
            return false;
        for (int i = 0; i < 8; i++) {
            char c = s.charAt(i);
            if (!((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f')))
                return false;
        }
        return true;
    }

    //build array from the bluetooth information
    public int[] buildArray(String s) {
        int[] arr = new int[4];
        for (int i = 0; i < 8; i += 2) {
            String num = "" + s.charAt(i) + s.charAt(i + 1);
            arr[i / 2] = Integer.parseInt(num, 16);
        }
        return arr;
    }






    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();
            calSet.set(Calendar.SECOND, calSet.get(Calendar.SECOND) + 2);
            if (calSet.compareTo(calNow) <= 0) {
                calSet.add(Calendar.DATE, 1);
            }
            setAlarm(calSet);
        }
    };

    private void setAlarm(Calendar targetCal) {
        tvAlarmPrompt.setText("Time is ");
        tvAlarmPrompt.append(String.valueOf(targetCal.getTime()));
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, RQS_TIME, intent, 0);
        AlarmManager alarmManager = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);
    }

    //cancel alarm
    private void cancelAlarm() {
        tvAlarmPrompt.setText("Cancel!");
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, RQS_TIME, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    //start alarm
    private void start() {
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();
        calSet.set(Calendar.SECOND, calSet.get(Calendar.SECOND) + 2);
        setAlarm(calSet);

    }


    //record for 5 sec information (from bluetooth connection)
    public void rec(View view) {
        DateFormat df = new SimpleDateFormat("yy_dd_MM HH_mm_ss");
        Date dateobj = new Date();
        mone = 1;
        final DatabaseReference dbDatarecRef = dbRecRef.child("" + df.format(dateobj));
        new CountDownTimer(5000, 100) {

            public void onTick(long millisUntilFinished) {
                dbDatarecRef.child("" + mone).setValue("" + message);
                mone++;
            }

            public void onFinish() {
                Toast.makeText(MainActivity.this, "done", Toast.LENGTH_LONG).show();
            }
        }.start();


    }


    public void toblue(View view) {
        Intent n = new Intent(this, BluetoothAlpha.class);
        startActivity(n);
    }


    // recive information from bluetooth
    BroadcastReceiver mReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String Text = intent.getStringExtra("theMessage");


            message.setLength(0);
            message.append(Text + "");


            tv.setText("" + Text);

            if (check(Text)) {
                values = buildArray(Text);
                des1.setText("" + values[0]);
                des2.setText("" + values[1]);
                sk1.setProgress(values[2]);
                sk2.setProgress(values[3]);
            }
        }
    };



    //optionmenu
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

