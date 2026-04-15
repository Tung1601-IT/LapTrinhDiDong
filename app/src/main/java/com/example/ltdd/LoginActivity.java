package com.example.ltdd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ltdd.database.DatabaseUser;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPassword;
    private MaterialButton btnLogin;
    private DatabaseUser databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        TextView txtCreateAccount = findViewById(R.id.txtCreateAccount);

        databaseUser = new DatabaseUser(this);
        databaseUser.insertDefaultAdmin();

        SharedPreferences preferences = getSharedPreferences("LTDD_PREFS", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        String role = preferences.getString("role", "");
        if (isLoggedIn) {
            navigateByRole(role);
            return;
        }

        txtCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Không được để trống email");
            edtEmail.requestFocus();
            return;
        }

        if (!email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
            edtEmail.setError("Email phải có @gmail.com");
            edtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Không được để trống mật khẩu");
            edtPassword.requestFocus();
            return;
        }

        String role = databaseUser.loginUser(email, password);
        if (role == null) {
            Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            return;
        }

        getSharedPreferences("LTDD_PREFS", MODE_PRIVATE)
                .edit()
                .putBoolean("isLoggedIn", true)
                .putString("role", role)
                .putString("email", email)
                .apply();

        navigateByRole(role);
    }

    private void navigateByRole(String role) {
        Intent intent;
        if ("admin".equals(role)) {
            intent = new Intent(this, HomeActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}