package com.sarkartech.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Sentence> sentenceList = new ArrayList<>();
    private SentenceAdapter adapter;

    private static final String DEFAULT_URL = ""; // Replace with your default URL


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.setting);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });


        // Load the saved language preference
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String selectedLanguage = preferences.getString("selectedLanguage", "Bangla");


        String url;
        if (selectedLanguage.equals("Bangla")) {
            url = ""; // Replace with the actual Bangla URL
        } else if (selectedLanguage.equals("Hindi")) {
            url = ""; // Replace with the actual Hindi URL
        } else if (selectedLanguage.equals("Arabic")) {
            url = "";
        } else {
            // Use the default URL if neither Bangla nor Hindi is selected
            url = DEFAULT_URL;
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SentenceAdapter(sentenceList);
        recyclerView.setAdapter(adapter);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray sentencesArray = response.getJSONArray("");

                            for (int i = 0; i < sentencesArray.length(); i++) {
                                JSONObject sentenceJson = sentencesArray.getJSONObject(i);
                                String english = sentenceJson.getString("");
                                String bangla = sentenceJson.getString("");
                                Sentence sentence = new Sentence(english, bangla);
                                sentenceList.add(sentence);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private class Sentence {
        private String english;
        private String bangla;

        public Sentence(String english, String bangla) {
            this.english = english;
            this.bangla = bangla;
        }
    }

    private class SentenceAdapter extends RecyclerView.Adapter<SentenceAdapter.SentenceViewHolder> {
        private List<Sentence> sentenceList;

        public SentenceAdapter(List<Sentence> sentenceList) {
            this.sentenceList = sentenceList;
        }

        @Override
        public SentenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sentence, parent, false);
            return new SentenceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SentenceViewHolder holder, int position) {
            Sentence sentence = sentenceList.get(position);
            holder.englishTextView.setText(sentence.english);
            holder.banglaTextView.setText(sentence.bangla);
        }

        @Override
        public int getItemCount() {
            return sentenceList.size();
        }

        public class SentenceViewHolder extends RecyclerView.ViewHolder {
            public TextView englishTextView;
            public TextView banglaTextView;

            public SentenceViewHolder(View itemView) {
                super(itemView);
                englishTextView = itemView.findViewById(R.id.englishTextView);
                banglaTextView = itemView.findViewById(R.id.banglaTextView);
            }
        }
    }
}
