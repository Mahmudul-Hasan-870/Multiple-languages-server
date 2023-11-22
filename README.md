# Multiple-languages-server

## MainActivity

```sh
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
```
## SettingsActivity

```sh
public class SettingsActivity extends AppCompatActivity {

    private RadioGroup languageRadioGroup;
    private RadioButton banglaRadioButton;
    private RadioButton hindiRadioButton;
    private RadioButton arabicRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        languageRadioGroup = findViewById(R.id.languageRadioGroup);
        banglaRadioButton = findViewById(R.id.banglaRadioButton);
        hindiRadioButton = findViewById(R.id.hindiRadioButton);
        arabicRadioButton = findViewById(R.id.arabicRadioButton);

        // Load the saved language preference
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String selectedLanguage = preferences.getString("selectedLanguage", "Bangla");
        if (selectedLanguage.equals("Bangla")) {
            banglaRadioButton.setChecked(true);
        } else if (selectedLanguage.equals("Hindi")) {
            hindiRadioButton.setChecked(true);
        } else if (selectedLanguage.equals("Arabic")) {
            arabicRadioButton.setChecked(true);
        }

        // Listen for radio button changes and save the preference
        languageRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String selected;
            if (banglaRadioButton.isChecked()) {
                selected = "Bangla";
            } else if (hindiRadioButton.isChecked()) {
                selected = "Hindi";
            } else if (arabicRadioButton.isChecked()) {
                selected = "Arabic";
            } else {
                // Handle the case when neither radio button is checked, possibly set a default language.
                selected = "DefaultLanguage"; // Replace with your default language.
            }

            // Save the selected language preference
            preferences.edit().putString("selectedLanguage", selected).apply();
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            finish();
        });
    }
}

```
## activity_main

```sh
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    tools:context=".MainActivity">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="500dp" />


    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Settings"
        android:layout_marginTop="30dp"
        android:layout_centerInParent="true"
        android:layout_below="@id/recyclerView"
        android:id="@+id/setting"/>

</RelativeLayout>
```
## activity_settings

```sh
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    tools:context=".SettingsActivity">


    <RadioGroup
        android:id="@+id/languageRadioGroup"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content">

        <RadioButton
            android:id="@+id/banglaRadioButton"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Bangla" />

        <RadioButton
            android:id="@+id/hindiRadioButton"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Hindi" />

        <RadioButton
            android:id="@+id/arabicRadioButton"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Arabic" />
    </RadioGroup>

</LinearLayout>
```
## items.xml
```sh
<!-- item_sentence.xml -->
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:id="@+id/englishTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="18sp"
        android:textColor="#000" />

    <TextView
        android:id="@+id/banglaTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="16sp"
        android:textColor="#666" />

</LinearLayout>
```
## Thank you
