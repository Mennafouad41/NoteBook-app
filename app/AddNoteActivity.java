package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.TimeZone;

import static com.example.app.APP.CHANNEL_ID;

import static com.example.app.HomeActivity.count;

public class AddNoteActivity extends AppCompatActivity {
    EditText titleEditText;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    CalendarView calendar;
    TimePicker timePicker;
    Button doneButton;
    private NotificationManagerCompat notificationManager;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tasks");
    int Year,Month,Day,Hour,Minute = 0;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_note);
       notificationManager=NotificationManagerCompat.from(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        titleEditText = findViewById(R.id.task_title_edit_text);
        calendar = findViewById(R.id.day_calender);
        timePicker = findViewById(R.id.time_Picker);
        doneButton = findViewById(R.id.done_button);



        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Year = year;
                Month = month;
                Day = dayOfMonth;
            }
        });

        timePicker.setIs24HourView(true);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Hour = hourOfDay;
                Minute = minute;
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = titleEditText.getText().toString();
                Calendar calendarr = Calendar.getInstance(TimeZone.getDefault());
                int currentYear = calendarr.get(Calendar.YEAR);
                int currentMonth = calendarr.get(Calendar.MONTH) ;
                int currentDay = calendarr.get(Calendar.DAY_OF_MONTH);
                int currentHour = calendarr.get(Calendar.HOUR);
                int currentMinute = calendarr.get(Calendar.MINUTE);



                if (Title.equals(""))
                    Toast.makeText(AddNoteActivity.this, "title required", Toast.LENGTH_SHORT).show();
                else if(Year==0 && Month==0 && Day==0)
                    Toast.makeText(AddNoteActivity.this, "select date", Toast.LENGTH_SHORT).show();
                else if(Hour==0 && Minute==0)
                    Toast.makeText(AddNoteActivity.this, "select Time", Toast.LENGTH_SHORT).show();
                else if(Year < currentYear)
                    Toast.makeText(AddNoteActivity.this, "it is too old date please set date in year " + currentYear , Toast.LENGTH_SHORT).show();
                else if(Year == currentYear && Month < currentMonth)
                    Toast.makeText(AddNoteActivity.this, "it is old month .. please set new month  " + currentYear , Toast.LENGTH_SHORT).show();

                else if(Year == currentYear && Month == currentMonth && Day < currentDay)
                    Toast.makeText(AddNoteActivity.this, "it is old day .. please set new day  " + currentYear , Toast.LENGTH_SHORT).show();
                else if(Year == currentYear && Month == currentMonth && Day == currentDay && Hour < currentHour)
                    Toast.makeText(AddNoteActivity.this, "it is old hour .. please set new hour  " + currentYear , Toast.LENGTH_SHORT).show();

                else if(Year == currentYear && Month == currentMonth && Day == currentDay && Hour == currentHour && Minute < currentMinute)
                    Toast.makeText(AddNoteActivity.this, "it is old minute .. please set new minute  " + currentYear , Toast.LENGTH_SHORT).show();

                else{
                    String email=firebaseUser.getEmail();
                    //String email = getIntent().getStringExtra("email");
                    String title = titleEditText.getText().toString();
                    TaskModel taskModel = new TaskModel();
                    taskModel.setMail(email);
                    taskModel.setTitle(title);
                    taskModel.setYear(Year); taskModel.setMonth(Month); taskModel.setDay(Day);
                    taskModel.setHour(Hour); taskModel.setMinute(Minute); taskModel.setFlag(false);

                   // String due_date = " date is " + Day + "/" + calendarr.get(Calendar.MONTH) + "/" + Year ;
                    String due_date = "  Focus new note is created ";
                    Notification notification = new NotificationCompat.Builder(AddNoteActivity.this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                            .setContentTitle(title)
                            .setContentText(due_date)
                            .setPriority(NotificationCompat.PRIORITY_LOW)
                            .build();

                    SystemClock.sleep(20);
                    notificationManager.notify(count,notification);


                    ref.push().setValue(taskModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                                String time = "";

                                if(Hour< 10)
                                    time += "0" + Hour;
                                else if(Hour >= 10)
                                    time += Hour;
                                time += ":";

                                if(Minute < 10)
                                    time += "0" + Minute;
                                else if(Minute >= 10)
                                    time += Minute;
                                titleEditText.setText("");

                                startActivity(new Intent(AddNoteActivity.this,HomeActivity.class));
                            }
                        }
                    });



                }


            }
        });


    }
}