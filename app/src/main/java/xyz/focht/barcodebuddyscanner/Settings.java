package xyz.focht.barcodebuddyscanner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.preference.PreferenceManager;
import android.widget.EditText;

public class Settings extends AppCompatActivity{
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        final EditText url = findViewById(R.id.apiURL);
        final EditText key = findViewById(R.id.apiKey);
        final String apiURL = preferences.getString("apiURL","");
        final String apiKey = preferences.getString("apiKey", "");
        url.setText(apiURL);
        key.setText(apiKey);
    }
    public void savePreferences(View view){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final EditText url = findViewById(R.id.apiURL);
        final EditText key = findViewById(R.id.apiKey);
        final String apiURL = url.getText().toString();
        final String apiKey = key.getText().toString();
        preferences.edit().putString("apiURL", apiURL).apply();
        preferences.edit().putString("apiKey", apiKey).apply();
        finish();
    }
}
