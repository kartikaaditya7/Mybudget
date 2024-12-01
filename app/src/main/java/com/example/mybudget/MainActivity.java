package com.example.mybudget;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView balanceText;
    private Button incomeButton, expenseButton, setBudgetButton, resetButton, viewTransactionsButton;
    private float totalIncome = 0f;
    private float totalExpense = 0f;
    private DatabaseReference incomeRef, expenseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        balanceText = findViewById(R.id.balanceText);
        incomeButton = findViewById(R.id.incomeButton);
        expenseButton = findViewById(R.id.expenseButton);
        setBudgetButton = findViewById(R.id.setBudgetButton);
        resetButton = findViewById(R.id.resetButton);
        viewTransactionsButton = findViewById(R.id.viewTransactionsButton);

        // Initialize Firebase references
        incomeRef = FirebaseDatabase.getInstance().getReference("Income");
        expenseRef = FirebaseDatabase.getInstance().getReference("Expense");

        // Fetch the initial income and expense data from Firebase
        fetchIncomeExpenseData();

        // Handle income button click
        incomeButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, IncomeActivity.class);
            startActivity(intent);
        });

        // Handle expense button click
        expenseButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
            startActivity(intent);
        });

        // Handle budget set button click
        setBudgetButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SetBudgetActivity.class);
            startActivity(intent);
        });

        // Handle reset button click
        resetButton.setOnClickListener(view -> {
            resetIncomeExpenseData();
        });

        // Handle view transaction history button click
        viewTransactionsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TransactionHistoryActivity.class);
            startActivity(intent);
        });
    }

    // Fetch income and expense data from Firebase and update balance
    private void fetchIncomeExpenseData() {
        incomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                totalIncome = 0f;
                for (DataSnapshot data : snapshot.getChildren()) {
                    String amount = data.child("amount").getValue(String.class);
                    totalIncome += Float.parseFloat(amount);
                }
                updateBalance();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle errors (if any)
            }
        });

        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                totalExpense = 0f;
                for (DataSnapshot data : snapshot.getChildren()) {
                    String amount = data.child("amount").getValue(String.class);
                    totalExpense += Float.parseFloat(amount);
                }
                updateBalance();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle errors (if any)
            }
        });
    }

    // Update the balance text in MainActivity
    private void updateBalance() {
        balanceText.setText("Income: ₹" + totalIncome + " | Expense: ₹" + totalExpense);
    }

    // Reset income and expense data in Firebase and locally
    private void resetIncomeExpenseData() {
        // Reset data in Firebase
        incomeRef.setValue(null); // Removes all income data
        expenseRef.setValue(null); // Removes all expense data

        // Reset local variables
        totalIncome = 0f;
        totalExpense = 0f;

        // Update balance UI to reflect reset values
        updateBalance();
    }
}
