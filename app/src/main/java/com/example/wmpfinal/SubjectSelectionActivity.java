package com.example.wmpfinal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubjectSelectionActivity extends AppCompatActivity {

    private ListView subjectListView;
    private Button confirmButton;
    private List<Subject> subjectList;
    private SubjectAdapter adapter;
    private DatabaseReference database;
    private FirebaseAuth auth;

    private int totalCredits = 0;
    private final int MAX_CREDITS = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_selection);

        subjectListView = findViewById(R.id.subjectListView);
        confirmButton = findViewById(R.id.confirmButton);

        subjectList = new ArrayList<>();
        adapter = new SubjectAdapter(this, subjectList);
        subjectListView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        // Load available subjects
        database.child("Subjects").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectList.clear();
                for (DataSnapshot subjectSnap : snapshot.getChildren()) {
                    Subject subject = subjectSnap.getValue(Subject.class);
                    if (subject != null) {
                        subjectList.add(subject);
                    }
                }
                // Log to check if the data is being loaded
                Log.d("SubjectSelection", "Subjects loaded: " + subjectList.size());

                if (subjectList.isEmpty()) {
                    Toast.makeText(SubjectSelectionActivity.this, "No subjects available", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SubjectSelectionActivity.this, "Failed to load subjects", Toast.LENGTH_SHORT).show();
            }
        });

        // Confirm selected subjects
        confirmButton.setOnClickListener(view -> {
            List<Subject> selectedSubjects = adapter.getSelectedSubjects();
            totalCredits = 0;
            for (Subject subject : selectedSubjects) {
                totalCredits += subject.getCredits();
            }

            if (totalCredits > MAX_CREDITS) {
                Toast.makeText(this, "Total credits exceed " + MAX_CREDITS, Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = auth.getCurrentUser().getUid();
            HashMap<String, Subject> selectedMap = new HashMap<>();
            for (Subject subject : selectedSubjects) {
                selectedMap.put(subject.getId(), subject);
            }

            database.child("Students").child(userId).child("enrolledSubjects").setValue(selectedMap)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Enrollment successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to enroll: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
