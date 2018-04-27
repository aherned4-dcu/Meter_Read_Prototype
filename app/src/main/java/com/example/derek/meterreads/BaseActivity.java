package com.example.derek.meterreads;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
//https://stackoverflow.com/questions/4275678/how-to-make-a-phone-call-using-intent-in-android
public class BaseActivity extends AppCompatActivity {
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                if(permissionGranted){
                    phoneCall();
                }else {
                    Toast.makeText(this, "You didn't assign permission.", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    public void phoneCall(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:1800000888"));
            this.startActivity(callIntent);
        }else{
            Toast.makeText(this, "You didn't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    public void handlePhoneCall(){

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
}
