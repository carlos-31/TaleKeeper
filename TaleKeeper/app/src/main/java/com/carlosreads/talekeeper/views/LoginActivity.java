package com.carlosreads.talekeeper.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    }
}