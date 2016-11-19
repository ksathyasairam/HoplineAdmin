package com.example.ssairam.hopline.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ssairam.hopline.CustomLinearLayoutManager;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.vo.OrderVo;

import java.util.List;


public class OfflineOrderAdapter extends RecyclerView.Adapter<OfflineOrderAdapter.ViewHolder> {
    private final View.OnClickListener orderCompleteListener;
    private Context mContext;
    private List<OrderVo> orderVoList;

    public OfflineOrderAdapter(Context mContext, List<OrderVo> orderVoList, View.OnClickListener orderCompleteListener) {
        this.mContext = mContext;
        this.orderVoList = orderVoList;
        this.orderCompleteListener = orderCompleteListener;
    }

    public void updateData( List<OrderVo> orderVoList) {
        this.orderVoList = orderVoList;
        notifyDataSetChanged();
    }

    public List<OrderVo> getData() {
        return orderVoList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView customerOrderNo;
        public Button buttonOrderComplete;
        public RecyclerView productList;
        public ViewHolder(View itemView) {
            super(itemView);
            customerOrderNo = (TextView) itemView.findViewById(R.id.customer_order_number);
            buttonOrderComplete =(Button)itemView.findViewById(R.id.button_order_completed);

            productList = (RecyclerView) itemView.findViewById(R.id.offline_order_product_list);
            RecyclerView.LayoutManager layoutManager= new CustomLinearLayoutManager(itemView.getContext().getApplicationContext());
            productList.setLayoutManager(layoutManager);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offline_order_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OrderVo order = orderVoList.get(position);
        holder.customerOrderNo.setText("#" + order.getCustomerOrderId());

        holder.productList.setAdapter(new OrderProductItemAdaptor(order.getOrderProducts()));

        holder.buttonOrderComplete.setTag(position);
        holder.buttonOrderComplete.setOnClickListener(orderCompleteListener);


    }


    @Override
    public int getItemCount() {
        return orderVoList.size();
    }
}
