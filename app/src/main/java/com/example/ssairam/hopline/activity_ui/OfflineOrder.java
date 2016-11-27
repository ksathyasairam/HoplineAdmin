package com.example.ssairam.hopline.activity_ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.ssairam.hopline.DataStore;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.adapters.OfflineOrderAdapter;
import com.example.ssairam.hopline.fragments.OfflineOrderFragment;
import com.example.ssairam.hopline.vo.OrderVo;

import java.util.List;

public class OfflineOrder extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OfflineOrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_order);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        initUi();
    }

    private void initUi() {
        List<OrderVo> orderVoList = DataStore.getCompleteOfflineOrders(getApplicationContext());
        adapter = new OfflineOrderAdapter(this.getApplicationContext(), orderVoList,new OfflineOrder.OfflineOrderListener());

        RecyclerView.LayoutManager layoutManager=new StaggeredGridLayoutManager(5,1);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private class OfflineOrderListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            DataStore.removeOfflineOrder(adapter.getData().get(position),getApplicationContext());
            adapter.updateData(DataStore.getCompleteOfflineOrders(getApplicationContext()));
        }
    }

    private void updateUi() {
        if(adapter != null) adapter.updateData(DataStore.getCompleteOfflineOrders(getApplicationContext()));
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }
}
