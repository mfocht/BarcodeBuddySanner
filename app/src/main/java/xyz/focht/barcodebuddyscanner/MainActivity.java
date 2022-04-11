package xyz.focht.barcodebuddyscanner;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText barcode = findViewById(R.id.BarcodeInput);
        final TextView textView = findViewById(R.id.output);
        final Button submit = findViewById(R.id.submit);
        final Button purchase = findViewById(R.id.purchase);
        final Button consume = findViewById(R.id.consume);

        barcode.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submit.performClick();
                barcode.setText("");
                return true;
            }
            return false;
        });
        barcode.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    submit.performClick();
                    barcode.setText("");
                    return true;
                }
                return false;
            }
        });
        submit.setOnClickListener(v -> {
            String code = barcode.getText().toString();
            apiCall(code, textView, "");
        });


        //Consume Button
        consume.setOnClickListener(v -> {
            apiCall("BBUDDY-C", textView, "State set to Consume.");
        });


        //Purchase Button
        purchase.setOnClickListener(v -> {
            apiCall("BBUDDY-P", textView, "State set to Purchase.");
        });
    }
    public void apiCall(String code, TextView textView, String message){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String apiURL = preferences.getString("apiURL","error");
        final String apiKey = preferences.getString("apiKey", "error");
        if(apiURL.equals("error")){
            textView.setText("Please enter server URL in settings");
            return;
        }
        if(apiKey.equals("error")){
            textView.setText("Please enter API Key in settings");
            return;
        }
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = apiURL + "/api/action/scan?apikey=" + apiKey +"&add=" + code;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Display the first 500 characters of the response string.
                    if(message.equals("")){
                        String output = "Success: " + code;
                        textView.setText(output);
                    }else{
                        String output = "Success: " + message;
                        textView.setText(output);
                    }

                }, error -> textView.setText(error.toString()));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        // Code here executes on main thread after user presses button
    }
    public void preferences(View view){
        Intent intent = new Intent(MainActivity.this, Settings.class);
        startActivity(intent);
    }
}