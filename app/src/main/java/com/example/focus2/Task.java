package com.example.focus2;

//Task class defines the properties of an user's task
public class Task {
    private String name, date, alarm, notes;
    private boolean repeat;

    public Task (String name, String date, String alarm, boolean repeat, String notes) {
        this.name = name;
        this.date = date;
        this.alarm = alarm;
        this.repeat = repeat;
        this.notes = notes;
    }

    //Accessor methods
    public String getName() {
        return this.name;
    }

    public String getDate() {
        return this.date;
    }

    public String getAlarm() {
        return this.alarm;
    }

    public boolean getRepeat() {
        return this.repeat;
    }

    public String getNotes() {
        return this.notes;
    }

    //Mutator methods
    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public String toString() {
        String result;

        result = String.format("Task: %s\nDue Date: %s\nAlarm: %s\nRepeat: %b\nNotes: %s",
                this.name, this.date, this.alarm, this.repeat, this.notes);

        return result;
    }
}
