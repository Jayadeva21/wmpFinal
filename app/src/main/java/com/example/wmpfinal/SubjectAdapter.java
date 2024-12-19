package com.example.wmpfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends ArrayAdapter<Subject> {
    private final List<Subject> subjectList;
    private final List<Subject> selectedSubjects;

    public SubjectAdapter(Context context, List<Subject> subjectList) {
        super(context, R.layout.subject_item, subjectList);
        this.subjectList = subjectList;
        this.selectedSubjects = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subject_item, parent, false);
        }

        Subject subject = subjectList.get(position);

        TextView subjectName = convertView.findViewById(R.id.subjectNameTextView);
        TextView subjectCredits = convertView.findViewById(R.id.subjectCreditsTextView);
        CheckBox subjectCheckBox = convertView.findViewById(R.id.subjectCheckBox);

        subjectName.setText(subject.getName());
        subjectCredits.setText("Credits: " + subject.getCredits());

        subjectCheckBox.setOnCheckedChangeListener(null);
        subjectCheckBox.setChecked(selectedSubjects.contains(subject));

        subjectCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedSubjects.add(subject);
            } else {
                selectedSubjects.remove(subject);
            }
        });

        return convertView;
    }

    public List<Subject> getSelectedSubjects() {
        return selectedSubjects;
    }
}
