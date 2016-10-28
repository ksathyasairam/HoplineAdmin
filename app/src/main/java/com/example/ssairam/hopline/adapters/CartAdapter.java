package com.example.ssairam.hopline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.vo.OrderProductVo;
import com.example.ssairam.hopline.vo.OrderVo;
import com.example.ssairam.hopline.vo.ProductVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssairam on 10/23/2016.
 */
public class CartAdapter extends BaseAdapter {


    private final View.OnClickListener removeCartListner;
    private Context mContext;
    private OrderVo order;
    private LayoutInflater mInflater;


    public CartAdapter(Context mContext, View.OnClickListener removeCartListner) {
        this.mContext = mContext;
        this.removeCartListner = removeCartListner;
        order = new OrderVo();
        order.setOrderProducts(new ArrayList<OrderProductVo>());
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addProduct(OrderProductVo orderProductVo) {
        order.getOrderProducts().add(orderProductVo);
    }

    public OrderVo getOrder() {
        return order;
    }

    public List<OrderProductVo> getOrderProducts(){
        return order.getOrderProducts();
    }


    public Integer getNotCustomizedOrderIndex(int productId){

        for (int i=0 ; i < order.getOrderProducts().size() ; ++i){
            OrderProductVo orderProductVo = order.getOrderProducts().get(i);
            if (orderProductVo.getProduct().getProductId().equals(productId) && !isCustomizedOrder(orderProductVo))
                return i;
        }

        return  null;
    }

    public boolean isCustomizedOrder(OrderProductVo orderProductVo) {
        return orderProductVo.getOrderProductAddons() != null && orderProductVo.getOrderProductAddons().size() > 0;
    }



    @Override
    public int getCount() {
        return order.getOrderProducts().size();
    }

    @Override
    public Object getItem(int position) {
        return order.getOrderProducts().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.cart_product_item, null);//set layout for displaying items
        ViewHolder holder = new ViewHolder(convertView);

        holder.cartItemName.setText(order.getOrderProducts().get(position).getProduct().getName());
        holder.cartItemQuantity.setText(order.getOrderProducts().get(position).getCount()+ "");


        holder.cartItemDelete.setTag(position);

        holder.cartItemDelete.setOnClickListener(removeCartListner);
//        holder.cartItemDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = (Integer) v.getTag();
//                order.getOrderProducts().remove(position);
//                notifyDataSetChanged();
//
//            }
//        });

//        holder.cartItemQuantity.setText((order.getOrderProducts().get(position).getProduct().getQuantity()));
//        holder.cartItemAddon.setText((order.getOrderProducts().get(position).getOrderProductAddons().get(0).getAddOn().getName()));

        return convertView;
    }


//
//
//
    public class ViewHolder {
        public TextView cartItemName,cartItemQuantity,cartItemAddon;
        public ImageButton cartItemDelete;

        public ViewHolder(View itemView) {
            cartItemName=(TextView) itemView.findViewById(R.id.cart_item_name);
            cartItemQuantity=(TextView)itemView.findViewById(R.id.cart_item_quantity);
            cartItemAddon=(TextView)itemView.findViewById(R.id.cart_item_addon);
            cartItemDelete=(ImageButton)itemView.findViewById(R.id.cart_item_delete_button);
        }
    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.cart_product_item, parent, false);
//
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//
//        if(order.getOrderProducts()!=null){
//
//        holder.cartItemName.setText(order.getOrderProducts().get(position).getProduct().getName());
//        holder.cartItemQuantity.setText((order.getOrderProducts().get(position).getProduct().getQuantity()));
//        holder.cartItemAddon.setText((order.getOrderProducts().get(position).getOrderProductAddons().get(0).getAddOn().getName()));
//
//
//        }
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }


}
