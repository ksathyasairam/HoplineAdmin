package com.example.ssairam.hopline.activity_ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.ssairam.hopline.DataStore;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.ServerHelper;
import com.example.ssairam.hopline.Util;
import com.example.ssairam.hopline.adapters.OrderHistoryAdapter;
import com.example.ssairam.hopline.vo.OrderVo;

import java.util.List;

public class OrderHistoryActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        new LoadOrderHistory(this).execute("");

    }

    private void initUi(List<OrderVo> orders) {
        adapter = new OrderHistoryAdapter(this.getApplicationContext(), orders, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                OrderVo order = adapter.getData().get(position);
                new PrintBill(OrderHistoryActivity.this,order).execute("");
            }
        });

        RecyclerView.LayoutManager layoutManager=new StaggeredGridLayoutManager(4,1);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }



    private void updateUi() {
        if(adapter != null) adapter.updateData(DataStore.getCompleteOfflineOrders(getApplicationContext()));
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }


    public class LoadOrderHistory extends AsyncTask<String, Void, Boolean> {
        public final Context context;
        ProgressDialog dialog;
        List<OrderVo> orders;

        public LoadOrderHistory(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                orders = ServerHelper.retrieveOrderHistory();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {
                initUi(orders);
            } else {
                Toast.makeText(context, "Error loading data!!", Toast.LENGTH_LONG).show();
            }

            if (dialog != null)
                dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            dialog = Util.showProgressDialog(OrderHistoryActivity.this);
        }
    }

    public class PrintBill extends AsyncTask<String, Void, Boolean> {
        public final Activity activity;
        ProgressDialog dialog;
        OrderVo order;

        public PrintBill(Activity activity, OrderVo order) {
            this.order = order;
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(String... params) {
//            return Util.printBill(order,getParent());
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
//            if (success) {
//                Toast.makeText(activity,"Print Success!", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(activity, "Print Failed! Make sure device is connected to Printer.", Toast.LENGTH_SHORT).show();
//            }
            if (dialog != null)
                dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            dialog = Util.showProgressDialog(OrderHistoryActivity.this);
        }
    }
}
