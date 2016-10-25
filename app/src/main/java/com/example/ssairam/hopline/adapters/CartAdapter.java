package com.example.ssairam.hopline.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.vo.OrderVo;

/**
 * Created by ssairam on 10/23/2016.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context mContext;
    private OrderVo order;

    public CartAdapter(Context mContext) {
        this.mContext = mContext;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cartItemName,cartItemQuantity,cartItemAddon;
        public ImageButton cartItemDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            cartItemName=(TextView) itemView.findViewById(R.id.cart_item_name);
            cartItemQuantity=(TextView)itemView.findViewById(R.id.cart_item_quantity);
            cartItemAddon=(TextView)itemView.findViewById(R.id.cart_item_addon);
            cartItemDelete=(ImageButton)itemView.findViewById(R.id.cart_item_delete_button);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_product_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(order.getOrderProducts()!=null){

        holder.cartItemName.setText(order.getOrderProducts().get(position).getProduct().getName());
        holder.cartItemQuantity.setText((order.getOrderProducts().get(position).getProduct().getQuantity()));
        holder.cartItemAddon.setText((order.getOrderProducts().get(position).getOrderProductAddons().get(0).getAddOn().getName()));


        }


    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
