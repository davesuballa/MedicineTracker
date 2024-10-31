package com.example.medicinetracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView picImageView;
    private TextView nameofpill, quantityofpill, time, texttimes, textoftime,date,textofdate;
    private static final int REQUEST_SETUP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDialog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button mondayButton = findViewById(R.id.Monday);
        Button tuesdayButton = findViewById(R.id.Tuesday);
        Button wednesdayButton = findViewById(R.id.Wednesday);
        Button settingButton = findViewById(R.id.setting);
        Button addButton = findViewById(R.id.add);

        updateButtonDates(mondayButton, tuesdayButton, wednesdayButton);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Setup.class);
                startActivityForResult(intent, REQUEST_SETUP);
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Setting.class);
                startActivity(intent);
            }
        });
    }

    private void updateButtonDates(Button... buttons) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d\nEEE", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        for (int i = 0; i < buttons.length; i++) {
            String dateText = dateFormat.format(calendar.getTime());
            buttons[i].setText(dateText);

            if (calendar.get(Calendar.DAY_OF_WEEK) == currentDayOfWeek) {
                buttons[i].setBackgroundResource(R.drawable.highlight_color);
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETUP && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                String selectedImageName = extras.getString("selectedImageName");
                int selectedImageId = extras.getInt("selectedImageId");
                String times = extras.getString("texttimes");
                String timesOfDay = extras.getString("textoftime");
                String timesOfDate = extras.getString("textofdate");
                String mgInput = extras.getString("inputmg");

                View newMedicationView = getLayoutInflater().inflate(R.layout.medication_item, null);

                ImageView picImageView = newMedicationView.findViewById(R.id.pic);
                TextView nameofpill = newMedicationView.findViewById(R.id.nameofpill);
                TextView quantityofpill = newMedicationView.findViewById(R.id.quantityofpill);
                TextView time = newMedicationView.findViewById(R.id.time);
                TextView date = newMedicationView.findViewById(R.id.date);

                picImageView.setImageResource(selectedImageId);
                nameofpill.setText(selectedImageName);
                quantityofpill.setText(times);
                time.setText(timesOfDay);
                date.setText(timesOfDate);

                LinearLayout bottomSection = findViewById(R.id.bottom_section);
                bottomSection.addView(newMedicationView);
            } else {
                Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
