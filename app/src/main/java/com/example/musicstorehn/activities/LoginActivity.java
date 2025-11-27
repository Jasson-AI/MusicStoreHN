package com.example.musicstorehn.activities;

// File: app/src/main/java/com/uth/musicstorehn/activities/LoginActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.example.musicstorehn.R;
import com.example.musicstorehn.models.Response;
import com.example.musicstorehn.models.User;
import com.example.musicstorehn.network.RetrofitClient;
import com.example.musicstorehn.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister, tvForgotPassword;
    private ProgressBar progressBar;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);
        if (session.isLoggedIn()) {
            navigateToMain();
            return;
        }

        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());

        tvRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

        tvForgotPassword.setOnClickListener(v -> startActivity(new Intent(this, RecoverPasswordActivity.class)));
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email requerido");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Contraseña requerida");
            etPassword.requestFocus();
            return;
        }

        showLoading(true);

        RetrofitClient.getApiService()
                .login(email, password)
                .enqueue(new Callback<Response<User>>() {
                    @Override
                    public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> r) {
                        showLoading(false);

                        if (r.isSuccessful() && r.body() != null) {
                            Response<User> apiResponse = r.body();

                            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                                User user = apiResponse.getData();

                                session.saveLoginData(
                                        user.getId(),
                                        user.getName(),
                                        user.getEmail(),
                                        "token_placeholder", // replace with token from API if available
                                        user.getProfileImage()
                                );

                                getFCMToken();

                                Toast.makeText(LoginActivity.this,
                                        "Bienvenido " + user.getName(),
                                        Toast.LENGTH_SHORT).show();

                                navigateToMain();
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        apiResponse.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Error al iniciar sesión",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<User>> call, Throwable t) {
                        showLoading(false);
                        Toast.makeText(LoginActivity.this,
                                "Error de conexión: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();
                        sendTokenToServer(token);
                    }
                });
    }

    private void sendTokenToServer(String token) {
        RetrofitClient.getApiService()
                .updateFcmToken(session.getAuthToken(), token)
                .enqueue(new Callback<Response<String>>() {
                    @Override
                    public void onResponse(Call<Response<String>> call, retrofit2.Response<Response<String>> response) {
                        // Token actualizado
                    }

                    @Override
                    public void onFailure(Call<Response<String>> call, Throwable t) {
                        // Error al actualizar token
                    }
                });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
