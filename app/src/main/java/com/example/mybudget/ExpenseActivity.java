package com.example.mybudget;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExpenseActivity extends AppCompatActivity {

    private EditText expenseSourceEditText, expenseAmountEditText;
    private Button saveExpenseButton;
    private DatabaseReference expenseRef, budgetRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        expenseSourceEditText = findViewById(R.id.expenseSourceEditText);
        expenseAmountEditText = findViewById(R.id.expenseAmountEditText);
        saveExpenseButton = findViewById(R.id.saveExpenseButton);

        // Initialize Firebase Database references
        expenseRef = FirebaseDatabase.getInstance().getReference("Expense");
        budgetRef = FirebaseDatabase.getInstance().getReference("Budget");

        saveExpenseButton.setOnClickListener(view -> saveExpense());
    }

    private void saveExpense() {
        String expenseSource = expenseSourceEditText.getText().toString().trim();
        String expenseAmount = expenseAmountEditText.getText().toString().trim();

        if (TextUtils.isEmpty(expenseSource) || TextUtils.isEmpty(expenseAmount)) {
            Toast.makeText(this, "Please enter both expense source and amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the expense exceeds the set budget
        checkBudgetExceeded(Double.parseDouble(expenseAmount));

        // Save the expense to Firebase
        String expenseId = expenseRef.push().getKey();
        Expense expense = new Expense(expenseSource, expenseAmount);

        expenseRef.child(expenseId).setValue(expense).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ExpenseActivity.this, "Expense saved successfully", Toast.LENGTH_SHORT).show();
                expenseSourceEditText.setText("");
                expenseAmountEditText.setText("");
            } else {
                Toast.makeText(ExpenseActivity.this, "Failed to save expense", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkBudgetExceeded(double newExpense) {
        budgetRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String budget = snapshot.getValue(String.class);
                    double budgetAmount = Double.parseDouble(budget);

                    // Calculate total expenses
                    expenseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            double totalExpense = 0;
                            for (DataSnapshot data : snapshot.getChildren()) {
                                String amount = data.child("amount").getValue(String.class);
                                totalExpense += Double.parseDouble(amount);
                            }

                            if (totalExpense + newExpense > budgetAmount) {
                                Toast.makeText(ExpenseActivity.this, "Warning: You have exceeded your monthly budget!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Handle error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }
}
