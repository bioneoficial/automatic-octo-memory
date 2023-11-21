package com.amazon.android.tv.tenfoot.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.amazon.android.tv.tenfoot.R;

public class LoginActivity extends Activity {

    private View loadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        loadingView = findViewById(R.id.loading_view);
        showLoading((false));
        // Start with the loading indicator visible if you're loading something initially
//        showLoading(true);

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> {
            // Start the loading indicator before transitioning to the next activity
//            showLoading(true);

            // Your login logic here
            // ...

            // If login is successful, transition to the next activity
            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
            startActivity(intent);

            // Optionally, if you don't want to come back to LoginActivity on pressing back button
            finish();
        });
        // Rest of your code...
    }

    private void showLoading(boolean show) {
        loadingView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}