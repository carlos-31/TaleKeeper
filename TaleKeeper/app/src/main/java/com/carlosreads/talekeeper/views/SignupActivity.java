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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.carlosreads.talekeeper.R;
import com.carlosreads.talekeeper.databinding.ActivitySignupBinding;
import com.carlosreads.talekeeper.viewmodels.SignupViewModel;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private SignupViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

        viewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(home);
            }
        });

        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.name.getText().toString().trim();
                String email = binding.inputEmail.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                String password2 = binding.password2.getText().toString().trim();

                //checks all info is filled in before calling viewmodel
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty())
                    Toast.makeText(SignupActivity.this,
                            "Please fill out your information", Toast.LENGTH_SHORT).show();
                else
                    viewModel.validateAndRegister(name, email,password, password2);
            }
        });

        viewModel.getRegistrationStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isRegistered) {
                if (isRegistered != null) {
                    if (isRegistered) {
                        //if sign up succeeds, send user to login activity
                        Toast.makeText(SignupActivity.this,
                                "user registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this,
                                                    LoginActivity.class));
                    } else {
                        Toast.makeText(SignupActivity.this,
                                "something went wrong :(", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        viewModel.getValidationMessage().observe(this, message -> {
            if (message != null) {
                //gets the error message, and if not null displays it
                Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        binding.loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
    }
}
