package com.example.ssairam.hopline.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ssairam.hopline.OrderStates;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.vo.OrderVo;

import java.util.List;


public class IncomingOrdersAdapter extends RecyclerView.Adapter<IncomingOrdersAdapter.ViewHolder> {
    private Context mContext;
    private List<OrderVo> orderVoList;
    View.OnClickListener callListner, cancelListner, confirmListner;


    public IncomingOrdersAdapter(Context mContext, List<OrderVo> orderVoList, View.OnClickListener callListner, View.OnClickListener cancelListner, View.OnClickListener confirmListner) {
        this.mContext = mContext;
        this.orderVoList = orderVoList;
        this.callListner = callListner;
        this.cancelListner = cancelListner;
        this.confirmListner = confirmListner;
    }

    public void updateData( List<OrderVo> orderVoList) {
        this.orderVoList = orderVoList;
        notifyDataSetChanged();
    }

    public List<OrderVo> getOrders(){
        return orderVoList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, count;
        public ImageView thumbnail, overflow;
        public Button confirm;
        public Button call;
        public Button cancel;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            count = (TextView) itemView.findViewById(R.id.count);
            confirm=(Button)itemView.findViewById(R.id.confirm);
            call=(Button)itemView.findViewById(R.id.call);
            cancel=(Button)itemView.findViewById(R.id.cancel);

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
        holder.title.setText(order.getCustomerOrderId()+"");
        holder.count.setText(order.getCancelReason());

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

    }

    @Override
    public int getItemCount() {
        return orderVoList.size();
    }
}
