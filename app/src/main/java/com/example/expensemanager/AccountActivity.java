package com.example.expensemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {
    private Toolbar settingsToolbar;
    private TextView userEmail;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        logoutBtn = findViewById(R.id.logoutBtn);
        userEmail =findViewById(R.id.userEmail);

        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AccountActivity.this)
                        .setTitle("Expense Manager")
                        .setMessage("Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(AccountActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });

    }
}