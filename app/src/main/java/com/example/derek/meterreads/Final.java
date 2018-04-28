package com.example.derek.meterreads;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Final extends BaseActivity {
    TextView textViewWeb;
    String sse="http://www.sseairtricity.com";
    String fb="http://www.facebook.com/sseairtricity";
    String twit="https://twitter.com/sseairtricity";
    String gp="http://www.sseairtricity.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAction();
        setContentView(R.layout.activity_final);
        textViewWeb = findViewById(R.id.link);
    }

    public void goToSSE (View view) {
        goToBrowser(sse);
    }

    public void goToTwit (View view) {
        goToBrowser(twit);
    }

    public void goToFB (View view) {
        goToBrowser(fb);
    }

    public void goToGoogle (View view) {
        goToBrowser(gp);
    }
}
