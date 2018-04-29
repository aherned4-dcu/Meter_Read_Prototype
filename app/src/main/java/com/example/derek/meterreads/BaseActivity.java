package com.example.derek.meterreads;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * The BaseActivity class contains methods that are common to all the other java classes in this project
 *
 *
 * @link {@link BaseActivity}
 *
 * @author – Derek Aherne
 * @version – 25/04/2018
 * @param – describe method parameters
 * @return – describe method return values
 * @throws – describe exceptions throw
 */
public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName(); //Log Tag
    /**
     *
     * The onRequestPermissionsResult method requests permission to use a phone app
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     * @param – requestCode An int that if equates 9 if permission granted
     * @param – permissions
     * @param – grantResults An int array that stores permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                if(permissionGranted){
                    phoneCall();
                }else {
                    Toast.makeText(this, R.string.Permission_declined, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    /**
     *
     * A method that allows a user to make a phone call via an implicit intent
     * @author – Derek Aherne
     * @version – 25/04/2018
     */
    public void phoneCall(){
        /* Citation: Method contains code adapted from
        * URL: https://stackoverflow.com/questions/4275678/how-to-make-a-phone-call-using-intent-in-android
        * Permission: MIT Licence Retrieved on:18th December 2017  */

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:+1800000888"));
            this.startActivity(callIntent);
        }else{
            Toast.makeText(this,R.string.Permission_declined, Toast.LENGTH_SHORT).show();
        }
    }



    /**
     *
     * A method to ensure application has correct permission to make a phone call
     *
     *
     * @link {@link BaseActivity}
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     */

    public void handlePhoneCall(){
        /* Citation: Class contains code adapted from
         * URL: https://stackoverflow.com/questions/4275678/how-to-make-a-phone-call-using-intent-in-android
         * Permission: MIT Licence Retrieved on:15th April 2018  */
        //
            if (Build.VERSION.SDK_INT < 23) {
                phoneCall();
            }else {

                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                    phoneCall();
                }else {
                    final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                    //Asking request Permissions
                    ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 9);
                }
            }

    }

    /**
     *
     * A method that uses an explicit intent to start Home.java
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     */
    public void goHome(){
        finish();
        Intent homeIntent = new Intent(this,Home.class);
        startActivity(homeIntent);
    }

    /**
     *
     * A method that uses an explicit intent to start Ocr,java
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     */
    public void goOCR(){
        finish();
        Intent ocrIntent = new Intent(this,Ocr.class);
        startActivity(ocrIntent);
    }

    /**
     *
     * A method that returns the sysdate in the format "yyyy-MM-dd"
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     * @return - a date of type String
     */
    public String getDate(){
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    }

    /**
     *
     * A method that hides the action bar
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     *
     */
    public void hideAction(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    /**
     *
     * A method uses implicit intent to open a browser at given URL
     *
     * @author – Derek Aherne
     * @version – 25/04/2018
     * @param - String site A URL String value
     *
     *
     */
    public void goToBrowser(String site){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(site));
        startActivity(intent);
    }
}
