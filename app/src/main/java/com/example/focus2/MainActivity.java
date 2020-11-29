package com.example.focus2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static protected List<Task> taskList = new ArrayList<>();

    String[] titles = {"Do the dishes", "Walk the dog", "Read one chapter", "Buy bread"};
    String[] description = {"11/21/2020", "11/22/2020", "11/24/2020", "11/25/2020"};

    ListView taskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskListView = findViewById(R.id.listViewTasks);
        MyAdapter adapter = new MyAdapter(this, titles, description);
        taskListView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.newTaskButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewTask(view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        for (int i=0; i < taskList.size(); i++)
        {
            menu.add(taskList.get(i).getName());
        }

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Open new task screen Activity
    public void openNewTask (View view) {
        Intent openNewTaskScreen = new Intent(this, NewTaskScreen.class);
        startActivity(openNewTaskScreen);
    }
}