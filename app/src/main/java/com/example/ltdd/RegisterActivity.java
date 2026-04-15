package com.example.ltdd;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ltdd.database.DatabaseUser;
import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPhone;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private DatabaseUser databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edtShopName);
        edtEmail = findViewById(R.id.edtRegisterEmail);
        edtPhone = findViewById(R.id.edtRegisterPhone);
        edtPassword = findViewById(R.id.edtRegisterPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        databaseUser = new DatabaseUser(this);

        ImageView btnBackRegister = findViewById(R.id.btnBackRegister);
        MaterialButton btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnBackRegister.setOnClickListener(v -> finish());
        btnCreateAccount.setOnClickListener(v -> validateRegisterForm());
    }

    private void validateRegisterForm() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            edtName.setError("Không được để trống tên");
            edtName.requestFocus();
            return;
        }

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

        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError("Không được để trống số điện thoại");
            edtPhone.requestFocus();
            return;
        }

        if (!phone.matches("^0\\d{9}$")) {
            edtPhone.setError("Số điện thoại phải bắt đầu bằng 0 và đủ 10 số");
            edtPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Không được để trống mật khẩu");
            edtPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            edtConfirmPassword.setError("Không được để trống mật khẩu nhập lại");
            edtConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            edtConfirmPassword.setError("Mật khẩu nhập lại không khớp");
            edtConfirmPassword.requestFocus();
            return;
        }

        if (databaseUser.isEmailExists(email)) {
            edtEmail.setError("Email đã tồn tại");
            edtEmail.requestFocus();
            return;
        }

        boolean result = databaseUser.insertUser(name, email, phone, password, "user");
        if (result) {
            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}