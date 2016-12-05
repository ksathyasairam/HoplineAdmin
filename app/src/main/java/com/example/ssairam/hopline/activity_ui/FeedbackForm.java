package com.example.ssairam.hopline.activity_ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.ssairam.hopline.R;

public class FeedbackForm extends BaseActivity {
    public static final String OUR_EMAIL = "mail.hopline@gmail.com";
    String mailSubject = "Bugs/Feedback";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);


    }



        public void buttonNext(View v) {
            String msg1 = ((EditText) findViewById(R.id.support_edittext))
                    .getEditableText().toString().trim();
            String msg=msg1+"Rating"+((RatingBar)findViewById(R.id.ratingBar)).getRating();
            if (!msg.equals("")) {
                msg = msg + "\n\n\n" + "***Dont modify it***\n" + getUserDetails();
                sendMail(msg);

            }
        }

        private String getUserDetails() {
            String appver="";

            try {
                appver = "Version "
                        + getPackageManager().getPackageInfo(this.getPackageName(),0).versionName;
            } catch (PackageManager.NameNotFoundException e1) {
                e1.printStackTrace();
            }
            String str = "";



            return appver+str;
        }

        private void sendMail(String msg) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[] { OUR_EMAIL });
            i.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
            i.putExtra(Intent.EXTRA_TEXT, msg);
            try {
                startActivity(Intent.createChooser(i, "Select email client"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.",
                        Toast.LENGTH_SHORT).show();
            }
        }


}
