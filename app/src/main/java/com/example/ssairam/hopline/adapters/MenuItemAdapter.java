package com.example.ssairam.hopline.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.vo.CategoryVo;
import com.example.ssairam.hopline.vo.ProductVo;

/**
 * Created by ssairam on 10/23/2016.
 */
public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {

    private final View.OnClickListener customizeButtonListner;
    private final View.OnClickListener addButtonListner;
    private CategoryVo category;
    private Context mContext;

    public CategoryVo getCategory() {
        return category;
    }

    public void setCategory(CategoryVo category) {
        this.category = category;
        for (ProductVo i : category.getProducts()) {
            i.setQuantity(1);
        }
        notifyDataSetChanged();
    }

    public MenuItemAdapter(Context mContext, CategoryVo products, View.OnClickListener addButtonListner, View.OnClickListener customizeButtonListner) {
        this.mContext = mContext;
        this.category = products;
        this.addButtonListner = addButtonListner;
        this.customizeButtonListner = customizeButtonListner;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView quantity;
        public TextView price;
        public Button addItemToCartButton;
        public Button customizeButton;
        public ImageButton increaseQuantityButton;
        public ImageButton decreaseQuantityButton;


        public ViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            price = (TextView) itemView.findViewById(R.id.price);
            addItemToCartButton = (Button) itemView.findViewById(R.id.add_item_cart_button);
            customizeButton = (Button) itemView.findViewById(R.id.customize_button);
            increaseQuantityButton = (ImageButton) itemView.findViewById(R.id.increase_quantity_button);
            decreaseQuantityButton = (ImageButton) itemView.findViewById(R.id.decrease_quantity_button);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_product_item, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ProductVo productVo = category.getProducts().get(position);

        holder.itemName.setText(productVo.getName());
        holder.price.setText(String.valueOf(productVo.getPrice()));
        holder.quantity.setText(String.valueOf(productVo.getQuantity()));

        holder.increaseQuantityButton.setTag(position);
        holder.increaseQuantityButton.setOnClickListener(new IncreaseQuantityListener());

        holder.decreaseQuantityButton.setTag(position);
        holder.decreaseQuantityButton.setOnClickListener(new DecreaseQuantityListener());

        holder.addItemToCartButton.setTag(position);
        holder.addItemToCartButton.setOnClickListener(addButtonListner);

        holder.customizeButton.setTag(position);
        holder.customizeButton.setOnClickListener(customizeButtonListner);
    }

    @Override
    public int getItemCount() {
        return category.getProducts().size();
    }

    private class IncreaseQuantityListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            int position = (Integer) v.getTag();

            int quantity = category.getProducts().get(position).getQuantity();

            category.getProducts().get(position).setQuantity(quantity + 1);
            notifyDataSetChanged();

        }
    }


    private class DecreaseQuantityListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            int position = (Integer) v.getTag();

            int quantity = category.getProducts().get(position).getQuantity();
            if (quantity == 1) return;

            category.getProducts().get(position).setQuantity(quantity - 1);
            notifyDataSetChanged();
        }
    }
}

