package com.example.focus2;

import java.util.ArrayList;

public class User {

    public String name, email;
    public ArrayList<Task> taskList;

    public User() {

    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.taskList = new ArrayList<Task>();
    }

    public ArrayList<Task> getList() {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> list) {
        this.taskList = list;
    }
}
