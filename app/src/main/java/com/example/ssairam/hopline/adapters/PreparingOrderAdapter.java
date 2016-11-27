package com.example.ssairam.hopline.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ssairam.hopline.CustomLinearLayoutManager;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.vo.OrderVo;

import java.util.List;


public class PreparingOrderAdapter extends RecyclerView.Adapter<PreparingOrderAdapter.ViewHolder> {
    private final View.OnClickListener orderReadyListner;
    private Context mContext;
    private List<OrderVo> orderVoList;

    public PreparingOrderAdapter(Context mContext, List<OrderVo> orderVoList, View.OnClickListener orderReadyListner) {
        this.mContext = mContext;
        this.orderVoList = orderVoList;
        this.orderReadyListner = orderReadyListner;
    }

    public void updateData( List<OrderVo> orderVoList) {
        this.orderVoList = orderVoList;
        notifyDataSetChanged();
    }

    public List<OrderVo> getData() {
        return orderVoList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView customerOrderNo,customerName,customerPhone;
        public Button buttonOrderReady;
        public RecyclerView productList;
        public ViewHolder(View itemView) {
            super(itemView);
            customerOrderNo = (TextView) itemView.findViewById(R.id.customer_order_number);
            customerName = (TextView) itemView.findViewById(R.id.customer_name);
            customerPhone = (TextView) itemView.findViewById(R.id.customer_phone);
            buttonOrderReady=(Button)itemView.findViewById(R.id.button_order_ready);

            productList = (RecyclerView) itemView.findViewById(R.id.preparing_order_product_list);
            RecyclerView.LayoutManager layoutManager= new CustomLinearLayoutManager(itemView.getContext().getApplicationContext());
            productList.setLayoutManager(layoutManager);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.preparing_order_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OrderVo order = orderVoList.get(position);
        holder.customerOrderNo.setText("#" + order.getCustomerOrderId());
        holder.customerPhone.setText(order.getUser().getPhone());
        holder.customerName.setText(order.getUser().getName());
        holder.productList.setAdapter(new OrderProductItemAdaptor(order.getOrderProducts()));

        holder.buttonOrderReady.setTag(position);
        holder.buttonOrderReady.setOnClickListener(orderReadyListner);


    }


    @Override
    public int getItemCount() {
        return orderVoList.size();
    }
}
