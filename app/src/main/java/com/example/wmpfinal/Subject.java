package com.example.wmpfinal;

public class Subject {
    private String id;
    private String name;
    private int credits;

    public Subject() {
        // Default constructor required for calls to DataSnapshot.getValue(Subject.class)
    }

    public Subject(String id, String name, int credits) {
        this.id = id;
        this.name = name;
        this.credits = credits;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }
}
