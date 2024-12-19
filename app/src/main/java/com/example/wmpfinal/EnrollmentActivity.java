package com.example.wmpfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class EnrollmentActivity extends AppCompatActivity {

    private Button viewSubjectsButton, enrollmentSummaryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        viewSubjectsButton = findViewById(R.id.viewSubjectsButton);
        enrollmentSummaryButton = findViewById(R.id.enrollmentSummaryButton);

        viewSubjectsButton.setOnClickListener(view -> {
            startActivity(new Intent(EnrollmentActivity.this, SubjectSelectionActivity.class));
        });

        enrollmentSummaryButton.setOnClickListener(view -> {
            startActivity(new Intent(EnrollmentActivity.this, EnrollmentSummaryActivity.class));
        });
    }
}
