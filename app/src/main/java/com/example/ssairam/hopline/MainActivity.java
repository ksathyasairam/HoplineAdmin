package com.example.ssairam.hopline;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.ssairam.hopline.fragments.BigOrderPayFragment;
import com.example.ssairam.hopline.fragments.IncomingOrderFragment;
import com.example.ssairam.hopline.fragments.NewOrderFragment;
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
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, Fragment).commit();
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                }
                break;
            case 1:
                if (!wasSelected) {
                    PreparingOrderFragment fragment = new PreparingOrderFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();


                }
                break;
            case 2:
                if (!wasSelected) {
                    OrderReadyFragment fragment = new OrderReadyFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                }
                break;
            case 3:
                if (!wasSelected) {
                    NewOrderFragment fragment = new NewOrderFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                }
                break;
            case 4:
                if (!wasSelected) {
                    BigOrderPayFragment fragment = new BigOrderPayFragment();
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
}
