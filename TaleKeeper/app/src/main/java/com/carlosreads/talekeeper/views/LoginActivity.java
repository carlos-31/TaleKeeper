package com.carlosreads.talekeeper.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        context = this;

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
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
                            getString(R.string.validation_missing_info), Toast.LENGTH_SHORT).show();
                else
                    viewModel.login(email, password);
            }
        });

        viewModel.getResutlMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                if (message == R.string.login_success) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("navigateTo", "profile");
                    startActivity(intent);
                    finish();
                }
            }
        });

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