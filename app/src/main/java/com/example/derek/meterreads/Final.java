package com.example.derek.meterreads;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
/**
 *
 * The Final creates the functionality of the final activity screen.
 *
 *
 * @link {@link Final}
 *
 * @author – Derek Aherne
 * @version – 25/04/2018
 */
public class Final extends BaseActivity {
    TextView textViewWeb;
    public static final String TAG = Final.class.getSimpleName(); //Log Tag
    String sse="http://www.sseairtricity.com";
    String fb="http://www.facebook.com/sseairtricity";
    String twit="https://twitter.com/sseairtricity";
    String gp="http://www.sseairtricity.com";
    /**
     *
     * The onCreate method hides the action bar and sets the content to activity_final
     *
     *
     * @version – 25/04/2018
     * @param – Bundle savedInstanceState
     * @author – Derek Aherne
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAction();
        setContentView(R.layout.activity_final);
        textViewWeb = findViewById(R.id.link);
    }
    /**
     *
     * The goToSSE calls goToBrowser() using company URL
     *
     * @author – Derek Aherne
     * @param = View view
     * @version – 25/04/2018
     *
     */
    public void goToSSE (View view) {
        goToBrowser(sse);
    }
    /**
     *
     * The goToTwit calls goToBrowser() using companies Twitter URL
     *
     * @author – Derek Aherne
     * @param = View view
     * @version – 25/04/2018
     *
     */
    public void goToTwit (View view) {
        goToBrowser(twit);
    }
    /**
     *
     * The goToFB calls goToBrowser() using companies FaceBook URL
     *
     * @author – Derek Aherne
     * @param = View view
     * @version – 25/04/2018
     *
     */
    public void goToFB (View view) {
        goToBrowser(fb);
    }
    /**
     *
     * The goToGoogle calls goToBrowser() using companies Google Plus URL
     *
     * @author – Derek Aherne
     * @param = View view
     * @version – 25/04/2018
     *
     */
    public void goToGoogle (View view) {
        goToBrowser(gp);
    }
}
