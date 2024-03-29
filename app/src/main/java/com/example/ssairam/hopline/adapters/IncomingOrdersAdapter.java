package com.example.ssairam.hopline.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ssairam.hopline.CustomLinearLayoutManager;
import com.example.ssairam.hopline.OrderStates;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.vo.OrderVo;

import java.util.List;


public class IncomingOrdersAdapter extends RecyclerView.Adapter<IncomingOrdersAdapter.ViewHolder> {
    private Context mContext;
    private List<OrderVo> orderVoList;
    View.OnClickListener callListner, cancelListner, confirmListner,increaseTimeListner,decreaseTimeListner;


    public IncomingOrdersAdapter(Context mContext, List<OrderVo> orderVoList, View.OnClickListener callListner, View.OnClickListener cancelListner, View.OnClickListener confirmListner,View.OnClickListener increaseTimeListner,View.OnClickListener decreaseTimeListner) {
        this.mContext = mContext;
        this.orderVoList = orderVoList;
        this.callListner = callListner;
        this.cancelListner = cancelListner;
        this.confirmListner = confirmListner;
        this.increaseTimeListner=increaseTimeListner;
        this.decreaseTimeListner=decreaseTimeListner;



    }

    public void updateData( List<OrderVo> orderVoList) {
        this.orderVoList = orderVoList;
        notifyDataSetChanged();
    }

    public List<OrderVo> getOrders(){
        return orderVoList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, count, customerOrderNo,customerName,customerPhone,paid;
        public Button confirm;
        public Button call;
        public Button cancel;
        public RecyclerView productList;

        public ImageButton increaseTime;
        public ImageButton decreaseTime;

        public TextView time;
        public ViewHolder(View itemView) {
            super(itemView);

            confirm=(Button)itemView.findViewById(R.id.confirm);
            call=(Button)itemView.findViewById(R.id.call);
            cancel=(Button)itemView.findViewById(R.id.cancel);
            customerOrderNo = (TextView) itemView.findViewById(R.id.customer_order_number);
            customerName = (TextView) itemView.findViewById(R.id.customer_name);
            customerPhone = (TextView) itemView.findViewById(R.id.customer_phone);
            increaseTime=(ImageButton) itemView.findViewById(R.id.increase_time_button) ;
            decreaseTime=(ImageButton)itemView.findViewById(R.id.decrease_time_button);
            time=(TextView) itemView.findViewById(R.id.time_display);
            paid=(TextView) itemView.findViewById(R.id.paidYN);


            productList = (RecyclerView) itemView.findViewById(R.id.incoming_order_product_list);
            RecyclerView.LayoutManager layoutManager= new CustomLinearLayoutManager(itemView.getContext().getApplicationContext());
            productList.setLayoutManager(layoutManager);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.incoming_order_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OrderVo order = orderVoList.get(position);
        holder.customerOrderNo.setText("Order No : #" + order.getCustomerOrderId());
        holder.customerPhone.setText(order.getUser().getPhone());
        holder.customerName.setText(order.getUser().getName());
        holder.customerPhone.setTag(position);
        holder.customerPhone.setOnClickListener(new dialListener());

        if ("Y".equals(order.getPaidYn())) {
            holder.paid.setVisibility(View.VISIBLE);
        } else {
            holder.paid.setVisibility(View.GONE);
        }


        if (OrderStates.BIG_ORDER_CALL.equals(order.getOrderState()) || OrderStates.DEFAULTER_CALL.equals(order.getOrderState())) {
            holder.confirm.setVisibility(View.GONE);
            holder.cancel.setVisibility(View.GONE);
            holder.call.setVisibility(View.VISIBLE);
            holder.call.setTag(position);
            holder.call.setOnClickListener(callListner);
        }else {
            holder.confirm.setVisibility(View.VISIBLE);
            holder.cancel.setVisibility(View.VISIBLE);
            holder.call.setVisibility(View.GONE);
            holder.confirm.setTag(position);
            holder.cancel.setTag(position);
            holder.confirm.setOnClickListener(confirmListner);
            holder.cancel.setOnClickListener(cancelListner);
        }
        if(order.getOrderCompleteTime()!=null){
            holder.time.setText(String.valueOf(order.getOrderCompleteTime()));

        }
        else {
            order.setOrderCompleteTime(0);
            holder.time.setText(String.valueOf(order.getOrderCompleteTime()));

        }

        holder.increaseTime.setTag(position);
        holder.increaseTime.setOnClickListener(increaseTimeListner);
        holder.decreaseTime.setTag(position);
        holder.decreaseTime.setOnClickListener(decreaseTimeListner);

        holder.productList.setAdapter(new OrderProductItemAdaptor(order.getOrderProducts()));

    }

    private class dialListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            dialPhoneNumber(String.valueOf(orderVoList.get((Integer) v.getTag()).getUser().getPhone()));

        }
    }


    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return orderVoList.size();
    }
}

