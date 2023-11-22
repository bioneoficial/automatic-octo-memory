package com.amazon.android.tv.tenfoot.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

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
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phrases_activity);

        tvPhrase = (TextView) findViewById(R.id.tvPhrase);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);

        fetchPhrases();
    }

    private void fetchPhrases() {
        String url = "https://app.vivaemaquarius.com.br/api/phrases/all";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    // Parse the JSON Response
                    try {
                        JSONArray phrasesArray = response.getJSONArray("frases");
                        if (phrasesArray.length() > 0) {
                            // Choose a random phrase
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
                    }
                }, error -> {
                    // TODO: Handle error
                    Log.e("PhrasesActivity", "Error fetching phrases: " + error.getMessage());
                });

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }
}