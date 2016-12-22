package com.example.ssairam.hopline.activity_ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.ssairam.hopline.DataStore;
import com.example.ssairam.hopline.IncommingOrderBackgroudRefresh;
import com.example.ssairam.hopline.InitialiseDataFromServer;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.fragments.BigOrderPayFragment;
import com.example.ssairam.hopline.fragments.IncomingOrderFragment;
import com.example.ssairam.hopline.fragments.NewOrderFragment;
import com.example.ssairam.hopline.fragments.OrderReadyFragment;
import com.example.ssairam.hopline.fragments.PreparingOrderFragment;


public class MainActivity extends BaseActivity implements IncomingOrderFragment.OnFragmentInteractionListener
        , AHBottomNavigation.OnTabSelectedListener, NewOrderFragment.OnFragmentInteractionListener, BigOrderPayFragment.OnFragmentInteractionListener,
        OrderReadyFragment.OnFragmentInteractionListener, PreparingOrderFragment.OnFragmentInteractionListener {
    AHBottomNavigation bottomNavigation;
    Menu actionBarMenu;
    NewOrderFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
        }

//        if (PrinterHelperBackup.get().isBluetoothOn()) {
//            new PrinterConnectorBackup(this).execute("");
//        } else {
//            Toast.makeText(this, "Printer connection FAILED! MAKE SURE BLUETOOTH IS TURNED ON AND CONNECTED TO PRINTER", Toast.LENGTH_LONG).show();
//        }

        if (!DataStore.isDataInilitised()) {
            new InitialiseDataFromServer(this) {


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
        initBottomNavigationBar();

        if (findViewById(R.id.fragment_container) != null) {
            IncomingOrderFragment Fragment = new IncomingOrderFragment();
            Fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, Fragment).commit();

        }

    }

    private void initBottomNavigationBar() {
        if (bottomNavigation != null) return;

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem tab_incomingorder = new AHBottomNavigationItem(getResources().getString(R.string.tab_incomingorder), R.drawable.ic_incoming);
        AHBottomNavigationItem tab_pendingorder = new AHBottomNavigationItem(getResources().getString(R.string.tab_pendingorder), R.drawable.ic_pendingorder);
        AHBottomNavigationItem tab_billing = new AHBottomNavigationItem(getResources().getString(R.string.tab_billing), R.drawable.ic_billing);
//        AHBottomNavigationItem tab_neworder = new AHBottomNavigationItem(getResources().getString(R.string.tab_neworder), R.drawable.ic_neworder);
        AHBottomNavigationItem tab_defaulter = new AHBottomNavigationItem(getResources().getString(R.string.tab_big_order_pay), R.drawable.ic_defaulter);

        bottomNavigation.addItem(tab_incomingorder);
        bottomNavigation.addItem(tab_pendingorder);
        bottomNavigation.addItem(tab_billing);
//        bottomNavigation.addItem(tab_neworder);
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
//                    restPriceAndQty();
                    IncomingOrderFragment fragment = new IncomingOrderFragment();
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                }
                break;
            case 1:
                if (!wasSelected) {

//                    restPriceAndQty();
                    PreparingOrderFragment fragment = new PreparingOrderFragment();
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

                }
                break;
            case 2:
                if (!wasSelected) {
//                    restPriceAndQty();
                    OrderReadyFragment fragment = new OrderReadyFragment();
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

                }
                break;
//            case 3:
//                if (!wasSelected) {
//                    restPriceAndQty();
//                     fragment = new NewOrderFragment();
//                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
//
//                }
//                break;
            case 3:
                if (!wasSelected) {
//                    restPriceAndQty();
                    BigOrderPayFragment fragment = new BigOrderPayFragment();
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

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
        actionBarMenu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
//            case R.id.offline_orders:
//                startActivity(new Intent(this, OfflineOrder.class));
//                return true;

            case R.id.inventory:
                startActivity(new Intent(this, Inventory.class));
                return true;

            case R.id.order_history:
                startActivity(new Intent(this, OrderHistoryActivity.class));
                return true;
            case R.id.feed_back :
                startActivity(new Intent(this, FeedbackForm.class));
                return true;
//            case R.id.action_clear_cart:
//                if(fragment!=null){
//
//                    fragment.clearAll();
//                }
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void setNotification (String text){
        if(text==null){
            if (bottomNavigation!=null){
                bottomNavigation.setNotification("!", 0);

            }
        }
        else
        { if(bottomNavigation!=null){
            bottomNavigation.setNotification(text, 0);

        }

        }
    }
    public void removeNotification (){
        if (bottomNavigation!=null){
            bottomNavigation.setNotification("", 0);

        }

    }
//
//    public void showPopup(){
//        View menuItemView = findViewById(R.id.action_clear_cart);
//
//        PopupMenu popup =new PopupMenu(this, menuItemView) {
//            @Override
//            public void show() {
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//                super.show();
//            }};
//
//        popup.get
//        MenuInflater inflate = popup.getMenuInflater();
//        inflate.inflate(R.menu.menu, popup.getMenu());
//        popup.show();
//
//    }
//
//
//
//    public void updateMenuPrice(String title) {
////        MenuItem totalPrice=actionBarMenu.findItem(R.id.action_total_price);
////        totalPrice.setTitle(title);
//
//    }
//    public void updateMenuQty(String title) {
////        MenuItem totalQty = actionBarMenu.findItem(R.id.action_total_qty);
////        totalQty.setTitle(title);
//
//
//    }
//    public void  restPriceAndQty (){
////        MenuItem totalPrice=actionBarMenu.findItem(R.id.action_total_price);
////        totalPrice.setTitle("Total : ");
////        MenuItem totalQty = actionBarMenu.findItem(R.id.action_total_qty);
////        totalQty.setTitle("Qty : ");
//    }





    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        initBottomNavigationBar();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("newIncomingOrder"));
        super.onResume();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setNotification(null);
        }
    };






}
