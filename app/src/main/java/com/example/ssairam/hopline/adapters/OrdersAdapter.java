package com.example.ssairam.hopline.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.vo.OrderVo;


import java.util.List;

/**
 * Created by ssairam on 10/19/2016.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private Context mContext;
    private List<OrderVo> orderVoList;

    public OrdersAdapter(Context mContext, List<OrderVo> orderVoList) {
        this.mContext = mContext;
        this.orderVoList = orderVoList;
    }

    public void setData( List<OrderVo> orderVoList) {
        this.orderVoList = orderVoList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, count;
        public ImageView thumbnail, overflow;
        public Button confirm;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            count = (TextView) itemView.findViewById(R.id.count);
            confirm=(Button)itemView.findViewById(R.id.confirm);

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OrderVo order = orderVoList.get(position);
        holder.title.setText(order.getCustomerOrderId()+"");
        holder.count.setText(order.getCancelReason());
        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"Confirmed",Toast.LENGTH_LONG).show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return orderVoList.size();
    }
}
