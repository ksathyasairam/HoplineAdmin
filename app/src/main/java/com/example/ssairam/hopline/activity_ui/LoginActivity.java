package com.example.ssairam.hopline.activity_ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ssairam.hopline.MainPrefs;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.ServerHelper;
import com.example.ssairam.hopline.Util;
import com.example.ssairam.hopline.vo.ShopVo;

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


    }


    public void loginButton(View v) {
        String username = ((EditText) findViewById(R.id.username)).getEditableText()
                .toString().trim();

        String password = ((EditText) findViewById(R.id.password)).getEditableText()
                .toString().trim();

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) return;

        ShopVo shopVo = new ShopVo();
        shopVo.setUsername(username);
        shopVo.setPassword(password);
        new Login(shopVo, this).execute("");



    }


    private class Login extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        ShopVo shopVo;
        Context context;

        Login(ShopVo shopVo, Context context) {
            this.shopVo = shopVo;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                shopVo =  ServerHelper.login(shopVo);
            } catch (Exception e) {
                e.printStackTrace();
                shopVo = null;
            }

            return null;

        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (dialog != null)
                dialog.dismiss();

            if (shopVo == null) {
                Toast.makeText(context, "Login failed!",Toast.LENGTH_LONG).show();
                return;
            }

            if (!shopVo.getSuccess()){
                Toast.makeText(context, shopVo.getMsg(),Toast.LENGTH_LONG).show();
                return;
            }

            MainPrefs.saveShopId(shopVo.getIdshop(),context.getApplicationContext());
            startActivity(new Intent(context,MainActivity.class));
            finish();

        }

        @Override
        protected void onPreExecute() {
            dialog = Util.showProgressDialog(LoginActivity.this);
        }
    }


}
