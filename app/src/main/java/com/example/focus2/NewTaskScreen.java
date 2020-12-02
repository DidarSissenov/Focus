package com.example.focus2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TimePicker;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import static com.example.focus2.MainActivity.taskList;

public class NewTaskScreen extends AppCompatActivity {

   FirebaseUser user;
   FirebaseDatabase database;
   DatabaseReference myRef;

    final Calendar myCalendar = Calendar.getInstance();

    EditText taskName;
    EditText dueDate;
    EditText alarmEt;
    Switch repeat;
    boolean repeatVal=false;
    TextInputEditText notesEt;

    Button doneButton;

    TimePickerDialog timePickerDialog;
    DatePickerDialog.OnDateSetListener onDateSetListener;

    final static int ONE = 1;

    Task task;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_screen);

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(user.getUid()).child("tasks");

        taskName = findViewById(R.id.taskName);

        //Back to main screen button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backMainScreen(v);
            }
        });

        //Configuring the dueDate field
        dueDate = (EditText) findViewById(R.id.dueDate);
        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
                new DatePickerDialog(NewTaskScreen.this, onDateSetListener, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        alarmEt = findViewById(R.id.alarmTime);
        alarmEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePickerDialog(false);
            }
        });

        repeat = findViewById(R.id.repeatSwitch);

        //Need to be properly implemented
        if(repeat != null) {
            repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        repeatVal = true;
                }
            });
        }

        notesEt = findViewById(R.id.notes);

        doneButton = findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDone();
            }
        });
    }

    //To go back to main screen
    public void backMainScreen(View view)
    {
        Intent backMain = new Intent(this, MainActivity.class);
        startActivity(backMain);
    }

    private void openDatePickerDialog() {
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
    }

    //To show selected due date
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);

        dueDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(NewTaskScreen.this,
                onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), is24r);
        timePickerDialog.setTitle("Set Alarm Time");

        timePickerDialog.show();
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calSet = (Calendar) myCalendar.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);


            //To be fixed                                                               <<<<<<<<
            /*if (calSet.compareTo(myCalendar) <= 0) {
                // Today Set time passed, count to tomorrow
                calSet.add(Calendar.DATE, 1);
            }
            */
            setAlarm(calSet);
        }
    };

    private void setAlarm(Calendar targetCal) {

        String myFormat = "h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);

        alarmEt.setText(sdf.format(targetCal.getTime()));

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), ONE, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }


    private void onClickDone()
    {
        String name = taskName.getText().toString().trim();
        String date = dueDate.getText().toString().trim();
        String alarm = alarmEt.getText().toString().trim();
        String notes = notesEt.getText().toString().trim();


        if (name.isEmpty()) {
            taskName.setError("Task name required");
            taskName.requestFocus();
            return;
        }
        if (date.isEmpty()) {
            dueDate.setError("Due date required");
            dueDate.requestFocus();
            return;
        }

        task = new Task(name, date);
        if(!alarm.isEmpty())
            task.setAlarm(alarm);

        if(!notes.isEmpty())
            task.setNotes(notes);


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<Task>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Task>>() {};
                ArrayList<Task> taskList = snapshot.getValue(genericTypeIndicator);

                //Check if the list already exists, if not create it
                if(taskList == null)
                    taskList = new ArrayList<Task>();


                //Add new Task
                taskList.add(task);

                //set updated list of tasks
                database.getReference("users").child(user.getUid()).child("tasks").setValue(taskList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        };

        myRef.addValueEventListener(valueEventListener);

        startActivity(new Intent(NewTaskScreen.this, MainActivity.class));
        finish();
        return;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRef.removeEventListener((ChildEventListener) this);
    }
}