package com.example.wmpfinal;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentSummaryActivity extends AppCompatActivity {

    private ListView enrolledSubjectsListView;
    private TextView totalCreditsTextView;
    private List<Subject> enrolledSubjects;
    private SubjectAdapter adapter;
    private DatabaseReference database;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment_summary);

        enrolledSubjectsListView = findViewById(R.id.enrolledSubjectsListView);
        totalCreditsTextView = findViewById(R.id.totalCreditsTextView);

        enrolledSubjects = new ArrayList<>();
        adapter = new SubjectAdapter(this, enrolledSubjects);
        enrolledSubjectsListView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        loadEnrolledSubjects();
    }

    private void loadEnrolledSubjects() {
        String userId = auth.getCurrentUser().getUid();

        database.child("Students").child(userId).child("enrolledSubjects").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                enrolledSubjects.clear();
                int totalCredits = 0;

                for (DataSnapshot subjectSnap : snapshot.getChildren()) {
                    Subject subject = subjectSnap.getValue(Subject.class);
                    if (subject != null) {
                        enrolledSubjects.add(subject);
                        totalCredits += subject.getCredits();
                    }
                }

                adapter.notifyDataSetChanged();
                totalCreditsTextView.setText("Total Credits: " + totalCredits);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EnrollmentSummaryActivity.this, "Failed to load enrollment summary", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
