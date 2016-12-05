package com.example.ssairam.hopline.activity_ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ssairam.hopline.DataStore;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.ServerHelper;
import com.example.ssairam.hopline.Util;
import com.example.ssairam.hopline.adapters.InventoryAdapter;
import com.example.ssairam.hopline.vo.CategoryVo;
import com.example.ssairam.hopline.vo.OrderVo;
import com.example.ssairam.hopline.vo.ProductVo;
import com.example.ssairam.hopline.vo.Stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Inventory extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
ListView inventoryList;
    List<ProductVo> data;
    InventoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        inventoryList= (ListView) findViewById(R.id.inventory_list);
        getInventory();
        adapter= new InventoryAdapter(data,this.getApplicationContext(),this);
        inventoryList.setAdapter(adapter);



        }

    private void getInventory() {
        List<ProductVo> temp = new ArrayList<>();

        for (CategoryVo i :DataStore.getMenuCategories(this.getApplicationContext()))
        {
            temp.addAll( i.getProducts());
        }
        data=temp;
        Collections.sort(data, new LexicographicComparator());

    }

    class LexicographicComparator implements Comparator<ProductVo> {
        @Override
        public int compare(ProductVo a, ProductVo b) {
            return a.getName().compareToIgnoreCase(b.getName());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            Stock stock=new Stock();
            stock.setProductId(adapter.getMenu().get((Integer) buttonView.getTag()).getProductId());
            stock.setStockYN("Y");
            new InventoryChange(stock,this).execute("");
        }
        if(!isChecked){
            Stock stock=new Stock();
            stock.setProductId(adapter.getMenu().get((Integer) buttonView.getTag()).getProductId());
            stock.setStockYN("N");
            new InventoryChange(stock,this).execute("");


        }
    }



    private class InventoryChange extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        Stock stock;
        Activity activity;
        InventoryChange(Stock stock,Activity activity) {
            this.stock = stock;
            this.activity=activity;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            boolean success = ServerHelper.updateStock(stock);
            if(success){
                try {
                    DataStore.setMenuCategories(ServerHelper.retrieveMenu(),getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }



            return success;

        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(adapter!=null) {
                getInventory();
                adapter.setMenu(data);
                adapter.notifyDataSetChanged();
            }
            if (success) {

                Toast.makeText(activity, "Success!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Error communicating with server!!", Toast.LENGTH_SHORT).show();
            }

            if (dialog != null)
                dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            dialog = Util.showProgressDialog(activity);


        }
    }
}
