package com.example.ssairam.hopline.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.Util;
import com.example.ssairam.hopline.vo.OrderProductAddonVo;
import com.example.ssairam.hopline.vo.OrderProductVo;

import java.util.List;


public class CreateOrder_OrderProductAdaptor extends BaseAdapter {
    List<OrderProductVo> orderProductVoList;
    LayoutInflater mInflater;
    private Context mContext;


    public CreateOrder_OrderProductAdaptor(Context mContext, List<OrderProductVo> orderProducts)
    {
        this.orderProductVoList = orderProducts;
        this.mContext = mContext;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return orderProductVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderProductVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.create_order_product_item, null);//set layout for displaying items

        ViewHolder holder = new ViewHolder(convertView);

        OrderProductVo orderProduct = orderProductVoList.get(position);
        holder.itemName.setText(orderProduct.getProduct().getName());
        holder.itemQuantity.setText(orderProduct.getCount()+"");
        holder.itemPrice.setText("Rs " + Util.calculatePrice(orderProduct));
        if (orderProduct.getOrderProductAddons() != null && !orderProduct.getOrderProductAddons().isEmpty()){
            holder.addonsString.setText(Util.getAddonString(orderProduct.getOrderProductAddons()));
            holder.addonsString.setVisibility(View.VISIBLE);
        } else {
            holder.addonsString.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemQuantity;
        TextView addonsString;
        TextView itemPrice;
        public ViewHolder(View itemView) {
            super(itemView);
            itemName= (TextView) itemView.findViewById(R.id.create_order_item_name);
            itemQuantity= (TextView) itemView.findViewById(R.id.create_order_item_qauntity);
            addonsString= (TextView) itemView.findViewById(R.id.create_order_item_Addon);
            itemPrice = (TextView) itemView.findViewById(R.id.create_order_item_price);
        }
    }
}
