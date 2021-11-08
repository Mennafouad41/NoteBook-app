package com.example.app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import static com.example.app.APP.CHANNEL_ID;


public class HomeActivity extends AppCompatActivity {


    public static int count = 0;
    private NotificationManagerCompat notificationManager;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    View header;
    ArrayList<TaskModel> list=new ArrayList<>();

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       // DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Tasks");
        final DatabaseReference ref = reference.child("Tasks");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d : snapshot.getChildren()) {


                    String email = d.child("mail").getValue(String.class);
                    if (email.equals(firebaseUser.getEmail())) {
                        String title = d.child("title").getValue(String.class);
                        int year = d.child("year").getValue(Integer.class);
                        int month = d.child("month").getValue(Integer.class);
                        int day = d.child("day").getValue(Integer.class);
                        int hour = d.child("hour").getValue(Integer.class);
                        int minute = d.child("minute").getValue(Integer.class);
                        Boolean flag = d.child("flag").getValue(Boolean.class);



                        TaskModel taskModel = new TaskModel();
                        taskModel.setMail(email);
                        taskModel.setTitle(title);
                        taskModel.setYear(year);
                        taskModel.setMonth(month);
                        taskModel.setDay(day);
                        taskModel.setHour(hour);
                        taskModel.setMinute(minute);
                        taskModel.setFlag(flag);
                        list.add(taskModel);
                    }
                    initialRecyclerView();


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



}



    public void AddNote(View view)
    {
        Intent intent = new Intent(HomeActivity.this, AddNoteActivity.class);
       // intent.putExtra("email",firebaseUser.getEmail().toString());
        startActivity(intent);
        count++;

    }
    public void logout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        finish();
    }
    public void initialRecyclerView()
    {
        RecyclerView recyclerView = findViewById(R.id.notepad_recycleview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}