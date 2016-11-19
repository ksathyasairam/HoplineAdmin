package com.example.ssairam.hopline;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.ssairam.hopline.fragments.BigOrderPayFragment;
import com.example.ssairam.hopline.fragments.IncomingOrderFragment;
import com.example.ssairam.hopline.fragments.NewOrderFragment;
import com.example.ssairam.hopline.fragments.OfflineOrderFragment;
import com.example.ssairam.hopline.fragments.OrderReadyFragment;
import com.example.ssairam.hopline.fragments.PreparingOrderFragment;


public class MainActivity extends AppCompatActivity implements IncomingOrderFragment.OnFragmentInteractionListener
        , AHBottomNavigation.OnTabSelectedListener, NewOrderFragment.OnFragmentInteractionListener, BigOrderPayFragment.OnFragmentInteractionListener,
        OrderReadyFragment.OnFragmentInteractionListener, PreparingOrderFragment.OnFragmentInteractionListener {
    AHBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
        }

        if (!DataStore.isDataInilitised()) {
            new InitialiseDataFromServer(this){
                @Override
                protected void onPostExecute(Boolean success) {
                    if (success) {
                        initUi();
                    } else {
                        Toast.makeText(activity, "Error communicating with server!", Toast.LENGTH_LONG).show();
                        initUi();
                    }

                    Intent intent = new Intent(activity, IncommingOrderBackgroudRefresh.class);
                    startService(intent);

                    super.onPostExecute(success);
                }
            }.execute("");
        } else {
            initUi();
        }

    }

    private void initUi() {
        intializenavigation();

        if (findViewById(R.id.fragment_container) != null) {
            IncomingOrderFragment Fragment = new IncomingOrderFragment();
            Fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, Fragment).addToBackStack(null).commit();

        }

    }

    private void intializenavigation() {

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem tab_incomingorder = new AHBottomNavigationItem(getResources().getString(R.string.tab_incomingorder), R.drawable.ic_incoming);
        AHBottomNavigationItem tab_pendingorder = new AHBottomNavigationItem(getResources().getString(R.string.tab_pendingorder), R.drawable.ic_pendingorder);
        AHBottomNavigationItem tab_billing = new AHBottomNavigationItem(getResources().getString(R.string.tab_billing), R.drawable.ic_billing);
        AHBottomNavigationItem tab_neworder = new AHBottomNavigationItem(getResources().getString(R.string.tab_neworder), R.drawable.ic_neworder);
        AHBottomNavigationItem tab_defaulter = new AHBottomNavigationItem(getResources().getString(R.string.tab_big_order_pay), R.drawable.ic_defaulter);

        bottomNavigation.addItem(tab_incomingorder);
        bottomNavigation.addItem(tab_pendingorder);
        bottomNavigation.addItem(tab_billing);
        bottomNavigation.addItem(tab_neworder);
        bottomNavigation.addItem(tab_defaulter);
        bottomNavigation.setOnTabSelectedListener(this);
        bottomNavigation.setBehaviorTranslationEnabled(false);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {

        switch (position) {

            case 0:
                if (!wasSelected) {
                    IncomingOrderFragment fragment = new IncomingOrderFragment();
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                }
                break;
            case 1:
                if (!wasSelected) {
                    PreparingOrderFragment fragment = new PreparingOrderFragment();
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();


                }
                break;
            case 2:
                if (!wasSelected) {
                    OrderReadyFragment fragment = new OrderReadyFragment();
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                }
                break;
            case 3:
                if (!wasSelected) {
                    NewOrderFragment fragment = new NewOrderFragment();
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                }
                break;
            case 4:
                if (!wasSelected) {
                    BigOrderPayFragment fragment = new BigOrderPayFragment();
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                }
                break;
            default:
                Toast.makeText(getApplicationContext(), "Error UI Fragment Loading",
                        Toast.LENGTH_LONG).show();
                break;


        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.offline_orders:
                OfflineOrderFragment fragment = new OfflineOrderFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            View decorView = getWindow().getDecorView();
//
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }
}
