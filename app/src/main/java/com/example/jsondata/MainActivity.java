package com.example.jsondata;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button  = findViewById(R.id.button);
        TextView textView = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final OkHttpClient client = new OkHttpClient();
                String url;
                url = "https://www.reddit.com/.json";
                final Request req = new Request.Builder().url(url).build();
                Thread t = new Thread() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void run() {
                        try {
                            Response response = client.newCall(req).execute();
                            String text = response.body().string();
                            Log.d("response", text);
                            JSONObject object = (JSONObject) new JSONTokener(text).nextValue();

                            JSONArray listings = object.getJSONObject("data").getJSONArray("children");
//                            ArrayList<String> titles = new ArrayList<>(listings.length());
//
//                            for (int i =0; i<listings.length(); i++) {
//                                JSONObject item = listings.getJSONObject(i);
//                                titles.add(item.getJSONObject("data").getString("title"));
//                            }
//
//                            // We can't update UI on a different thread, so we need to send
//                            // the processing back to the UI thread via runOnUiThread method
                            runOnUiThread(()->{
                               // String result = titles.stream().reduce("", (a, b) -> a += "\n" + b);
                                ((TextView)findViewById(R.id.textView)).setText(text);
                            });

                        } catch (IOException | JSONException e) {
                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            });
                        }
                    }};
                t.start();

            }
        });
}
}
