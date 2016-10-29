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


public class OrderReadyAdatper extends RecyclerView.Adapter<OrderReadyAdatper.ViewHolder> {
    private final View.OnClickListener unpickedListener;
    private final View.OnClickListener finalizedListener;
    private Context mContext;
    private List<OrderVo> orderVoList;

    public OrderReadyAdatper(Context mContext, List<OrderVo> orderVoList, View.OnClickListener finalizedListener, View.OnClickListener unpickedListener) {
        this.mContext = mContext;
        this.orderVoList = orderVoList;
        this.finalizedListener = finalizedListener;
        this.unpickedListener = unpickedListener;
    }

    public void updateData( List<OrderVo> orderVoList) {
        this.orderVoList = orderVoList;
        notifyDataSetChanged();
    }

    public List<OrderVo> getData() {
        return orderVoList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, count;
        public ImageView thumbnail, overflow;
        public Button buttonFinalized,buttonUnpicked;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            count = (TextView) itemView.findViewById(R.id.count);
            buttonFinalized=(Button)itemView.findViewById(R.id.button_finalized);
            buttonUnpicked=(Button)itemView.findViewById(R.id.button_unpicked);


        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_ready_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OrderVo order = orderVoList.get(position);
        holder.title.setText(order.getCustomerOrderId()+"");
        holder.count.setText(order.getCancelReason());


        if("Y".equals(order.getPaidYn())) {
            holder.buttonFinalized.setText("ORDER COMPLETE");
        } else {
            holder.buttonFinalized.setText("PRINT BILL    ");
        }











        holder.buttonFinalized.setTag(position);
        holder.buttonFinalized.setOnClickListener(finalizedListener);

        holder.buttonUnpicked.setTag(position);
        holder.buttonUnpicked.setOnClickListener(unpickedListener);
    }


    @Override
    public int getItemCount() {
        return orderVoList.size();
    }
}
