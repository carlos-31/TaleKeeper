package com.carlosreads.talekeeper.views;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.databinding.ActivityLoginBinding;
import com.carlosreads.talekeeper.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(home);
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString().trim();

                //checks info is filled out before calling the function
                if (email.isEmpty() || password.isEmpty())
                    Toast.makeText(LoginActivity.this,
                            "Please fill out your information", Toast.LENGTH_SHORT).show();
                else
                    viewModel.login(email,password);
            }
        });

        viewModel.getLoginStatus().observe(this, status -> {
            if (status != null){
                if (status) {
                    //if login successful, sends the user home
                    Intent home = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(home);
                    finish();
                }
                else
                    Toast.makeText(this, "Error while loging in", Toast.LENGTH_SHORT).show();
            }});

        binding.signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signup);
                finish();
            }
        });

    }
}