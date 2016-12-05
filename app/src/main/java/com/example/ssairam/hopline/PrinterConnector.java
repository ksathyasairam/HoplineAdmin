package com.example.ssairam.hopline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class PrinterConnector extends AsyncTask<String, Void, Boolean> {
        public final Activity activity;
        ProgressDialog dialog;

        public PrinterConnector(Activity activity){
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = PrinterHelper.get().connectToPrinter();
            return result;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (dialog != null)
                dialog.dismiss();

            if (success)
                Toast.makeText(activity, "Printer Connected!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(activity, "Unable to connect to printer!, Please contact Hopline.", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            dialog = Util.showProgressDialog(activity);
        }
    }
