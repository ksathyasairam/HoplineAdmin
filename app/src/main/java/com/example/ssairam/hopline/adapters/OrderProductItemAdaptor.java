package com.example.ssairam.hopline.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.Util;
import com.example.ssairam.hopline.vo.OrderProductAddonVo;
import com.example.ssairam.hopline.vo.OrderProductVo;

import java.util.List;



public class OrderProductItemAdaptor extends RecyclerView.Adapter<OrderProductItemAdaptor.ViewHolder> {
    List<OrderProductVo> orderProductVoList;

    public OrderProductItemAdaptor(List<OrderProductVo> orderProducts)
    {
        this.orderProductVoList = orderProducts;
    }

    @Override
    public OrderProductItemAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_product_item, parent, false);

        return new  OrderProductItemAdaptor.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderProductItemAdaptor.ViewHolder holder, int position) {
        OrderProductVo orderProduct = orderProductVoList.get(position);
        holder.itemName.setText(orderProduct.getProduct().getName());
        holder.itemQuantity.setText(orderProduct.getCount()+"");
        if (orderProduct.getOrderProductAddons() != null && !orderProduct.getOrderProductAddons().isEmpty()){
            holder.addonsString.setText(Util.getAddonString(orderProduct.getOrderProductAddons()));
            holder.addonsString.setVisibility(View.VISIBLE);
        } else {
            holder.addonsString.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return orderProductVoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemQuantity;
        TextView addonsString;
        public ViewHolder(View itemView) {
            super(itemView);
            itemName= (TextView) itemView.findViewById(R.id.order_item_name);
            itemQuantity= (TextView) itemView.findViewById(R.id.order_item_qauntity);
            addonsString= (TextView) itemView.findViewById(R.id.order_item_Addon);
        }
    }
}
