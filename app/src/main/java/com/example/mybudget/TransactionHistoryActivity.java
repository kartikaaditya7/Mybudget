package com.example.mybudget;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class TransactionHistoryActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> transactionList;
    private ArrayAdapter<String> adapter;
    private DatabaseReference incomeRef, expenseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        listView = findViewById(R.id.listView);
        transactionList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactionList);
        listView.setAdapter(adapter);

        incomeRef = FirebaseDatabase.getInstance().getReference("Income");
        expenseRef = FirebaseDatabase.getInstance().getReference("Expense");

        // Fetch Income Data
        incomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                transactionList.clear();  // Clear previous list data
                for (DataSnapshot data : snapshot.getChildren()) {
                    String source = data.child("source").getValue(String.class);
                    String amount = data.child("amount").getValue(String.class);
                    transactionList.add("Income: " + source + " - ₹" + amount);
                }
                adapter.notifyDataSetChanged();  // Notify adapter of the change
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(TransactionHistoryActivity.this, "Failed to load income data", Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch Expense Data
        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String source = data.child("source").getValue(String.class);
                    String amount = data.child("amount").getValue(String.class);
                    transactionList.add("Expense: " + source + " - ₹" + amount);
                }
                adapter.notifyDataSetChanged();  // Notify adapter of the change
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(TransactionHistoryActivity.this, "Failed to load expense data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
