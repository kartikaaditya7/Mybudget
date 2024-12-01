package com.example.mybudget;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IncomeActivity extends AppCompatActivity {

    private EditText incomeSourceEditText, incomeAmountEditText;
    private Button saveIncomeButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        incomeSourceEditText = findViewById(R.id.incomeSourceEditText);
        incomeAmountEditText = findViewById(R.id.incomeAmountEditText);
        saveIncomeButton = findViewById(R.id.saveIncomeButton);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Income");

        saveIncomeButton.setOnClickListener(view -> saveIncome());
    }

    private void saveIncome() {
        String incomeSource = incomeSourceEditText.getText().toString().trim();
        String incomeAmount = incomeAmountEditText.getText().toString().trim();

        if (TextUtils.isEmpty(incomeSource) || TextUtils.isEmpty(incomeAmount)) {
            Toast.makeText(this, "Please enter both income source and amount", Toast.LENGTH_SHORT).show();
            return;
        }

        String incomeId = databaseReference.push().getKey();
        Income income = new Income(incomeSource, incomeAmount);

        databaseReference.child(incomeId).setValue(income).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(IncomeActivity.this, "Income saved successfully", Toast.LENGTH_SHORT).show();
                incomeSourceEditText.setText("");
                incomeAmountEditText.setText("");
            } else {
                Toast.makeText(IncomeActivity.this, "Failed to save income", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
