package com.example.derek.meterreads;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Final extends AppCompatActivity {
    TextView textViewWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_final);
        textViewWeb = findViewById(R.id.link);


    }

    public void openWeb(View v) {
        textViewWeb.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='www.sseairtricity.com'> sseairtricity.com </a>";
        textViewWeb.setText(Html.fromHtml(text));

    }
    /*@Override
    //https://stackoverflow.com/questions/17719634/how-to-exit-an-android-app-using-code
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

*/
    public void onPressed() {
        //moveTaskToBack(true);
        //android.os.Process.killProcess(android.os.Process.myPid());
       // System.exit(1);
        Toast.makeText(Final.this,"Nothing to see here",Toast.LENGTH_LONG).show();
    }

    public void goToSSE (View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://www.sseairtricity.com"));
        startActivity(intent);
    }

    public void goToTwit (View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://twitter.com/sseairtricity"));
        startActivity(intent);
    }

    public void goToFB (View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://www.facebook.com/sseairtricity"));
        startActivity(intent);
    }

    public void goToGoogle (View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://www.sseairtricity.com"));
        startActivity(intent);
    }
}
