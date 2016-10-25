package com.example.ssairam.hopline.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.fragments.NewOrderFragment;
import com.example.ssairam.hopline.vo.CategoryVo;
import com.example.ssairam.hopline.vo.ProductVo;

import java.util.List;

/**
 * Created by ssairam on 10/23/2016.
 */
public class MenuItemAdapter extends RecyclerView.Adapter <MenuItemAdapter.ViewHolder> {

    private CategoryVo products;
    private Context mContext;
    private NewOrderFragment.MenuItemClickListener itemClickListener;

    public CategoryVo getData() {
        return products;
    }

    public void setData(CategoryVo products) {
        this.products = products;
        for(ProductVo i:products.getProducts())
        {     i.setQuantity(1);
        }
        notifyDataSetChanged();
    }

    public MenuItemAdapter(Context mContext, CategoryVo products) {
        this.mContext = mContext;
        this.products = products;
    }

    public MenuItemAdapter(Context mContext, CategoryVo products, NewOrderFragment.MenuItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.products = products;
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView quantity;
        public TextView price;
        public Button addItem;
        public Button customize;
        public ImageButton add_button;
        public ImageButton delete_button;


        public ViewHolder(View itemView) {
            super(itemView);
            itemName= (TextView) itemView.findViewById(R.id.item_name);
            quantity= (TextView) itemView.findViewById(R.id.quantity);
            price= (TextView) itemView.findViewById(R.id.price);
            addItem= (Button) itemView.findViewById(R.id.additem_button);
            customize=(Button) itemView.findViewById(R.id.customize_button);
            add_button=(ImageButton)itemView.findViewById(R.id.add_button);

            delete_button=(ImageButton)itemView.findViewById(R.id.delete_button);

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
        ProductVo productVo=products.getProducts().get(position);

        holder.itemName.setText(productVo.getName());
        holder.price.setText(String.valueOf(productVo.getPrice()));
        holder.quantity.setText(String.valueOf(productVo.getQuantity()));
        holder.add_button.setTag(position);
        holder.add_button.setOnClickListener(new AddQuantityListener());

        holder.delete_button.setTag(position);
        holder.delete_button.setOnClickListener(new DeleteQuantityListener());

        holder.addItem.setOnClickListener(itemClickListener);
        holder.customize.setOnClickListener(itemClickListener);


    }

    @Override
    public int getItemCount() {
        return products.getProducts().size();
    }

    private class AddQuantityListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            int quantity=products.getProducts().get((Integer) v.getTag()).getQuantity();
            products.getProducts().get((Integer) v.getTag()).setQuantity(quantity++);
            notifyDataSetChanged();


//dadxsdx;

        }
    }



    private class DeleteQuantityListener implements  View.OnClickListener{

        @Override
        public void onClick(View v) {
            int quantity=products.getProducts().get((Integer) v.getTag()).getQuantity();
            if(quantity!=0) {
                products.getProducts().get((Integer) v.getTag()).setQuantity(quantity++);
                notifyDataSetChanged();
            }
        }
    }
}
