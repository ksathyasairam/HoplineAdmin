package com.example.ssairam.hopline.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.fragments.NewOrderFragment;
import com.example.ssairam.hopline.vo.CategoryVo;

import java.util.List;

/**
 * Created by ssairam on 10/22/2016.
 */

public class MenuCategoryAdapter  extends RecyclerView.Adapter<MenuCategoryAdapter.ViewHolder>{
    private Context mContext;
    List<CategoryVo> categories;
    NewOrderFragment.CategoryItemClickListener itemClickListener;
    public MenuCategoryAdapter(Context mContext, List<CategoryVo> categories, NewOrderFragment.CategoryItemClickListener categoryItemClickListener) {
        this.mContext = mContext;
        this.categories = categories;
        this.itemClickListener=categoryItemClickListener;
        //added
        
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_category_item, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String categoryName=categories.get(position).getName();

        holder.categoryName.setText(categoryName);
        holder.layout.setTag(position);
        holder.layout.setOnClickListener(itemClickListener);
    }

    @Override
    public int getItemCount() {
        return categories.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryName;
        public LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            categoryName= (TextView) itemView.findViewById(R.id.category_name);
            layout=(LinearLayout) itemView;

        }
    }
}
