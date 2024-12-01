package com.example.mybudget;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetBudgetActivity extends AppCompatActivity {

    private EditText budgetEditText;
    private Button saveBudgetButton;
    private DatabaseReference budgetRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);

        budgetEditText = findViewById(R.id.budgetEditText);
        saveBudgetButton = findViewById(R.id.saveBudgetButton);

        // Initialize Firebase Database reference for Budget
        budgetRef = FirebaseDatabase.getInstance().getReference("Budget");

        saveBudgetButton.setOnClickListener(view -> {
            String budget = budgetEditText.getText().toString().trim();
            if (TextUtils.isEmpty(budget)) {
                Toast.makeText(this, "Please enter a budget amount", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save the budget to Firebase
            budgetRef.setValue(budget).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Budget saved successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the SetBudgetActivity after saving the budget
                } else {
                    Toast.makeText(this, "Failed to save budget", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
