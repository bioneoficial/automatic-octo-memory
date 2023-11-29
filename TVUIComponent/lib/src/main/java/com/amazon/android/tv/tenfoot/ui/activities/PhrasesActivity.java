package com.amazon.android.tv.tenfoot.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.android.tv.tenfoot.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class PhrasesActivity extends Activity {
    private TextView tvPhrase;
    private TextView tvAuthor;
    private ImageView companyLogoImageView;
    private ImageView logoImageView;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phrases_activity);

        tvPhrase = (TextView) findViewById(R.id.tvPhrase);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        companyLogoImageView = (ImageView) findViewById(R.id.companyLogoImageView);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        companyLogoImageView.getLayoutParams().width = width / 2;


        handler.postDelayed(() -> {
            fetchPhrases();
            companyLogoImageView.setVisibility(View.GONE);
            tvPhrase.setVisibility(View.VISIBLE);
            tvAuthor.setVisibility(View.VISIBLE);
            logoImageView.setVisibility(View.VISIBLE);
        }, 2000);
    }

    private void fetchPhrases() {
        String url = "https://app.vivaemaquarius.com.br/api/phrases/all";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        JSONArray phrasesArray = response.getJSONArray("frases");
                        if (phrasesArray.length() > 0) {
                            int index = new Random().nextInt(phrasesArray.length());
                            JSONObject phraseObject = phrasesArray.getJSONObject(index);

                            tvPhrase.setText(phraseObject.getString("frase"));
                            tvAuthor.setText(phraseObject.getString("autor"));

                            handler.postDelayed(() -> {
                                Intent intent = new Intent(PhrasesActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }, 3000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        redirectToLogin();
                    }
                }, error -> {
                    Log.e("PhrasesActivity", "Error fetching phrases: " + error.getMessage());
                    redirectToLogin();
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void redirectToLogin() {
        Toast.makeText(this, "Error loading phrases. Redirecting...", Toast.LENGTH_SHORT).show();
        handler.postDelayed(() -> {
            Intent intent = new Intent(PhrasesActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}