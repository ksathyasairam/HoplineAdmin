package com.example.ssairam.hopline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Created by root on 23/10/16.
 */


public class InitialiseDataFromServer extends AsyncTask<String, Void, Boolean> {
    public final Activity activity;
    ProgressDialog dialog;

    public InitialiseDataFromServer(Activity activity){
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        try {
            DataStore.loadEverythingFromServer(activity.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        dialog = Util.createProgressDialog(activity);
        dialog.show();
    }
}
