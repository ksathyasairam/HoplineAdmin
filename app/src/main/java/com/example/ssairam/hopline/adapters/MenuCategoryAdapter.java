package com.example.ssairam.hopline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.vo.CategoryVo;

import java.util.List;

/**
 * Created by ssairam on 10/22/2016.
 */

public class MenuCategoryAdapter  extends BaseAdapter{


    private Context mContext;
    private List<CategoryVo> categories;
    LayoutInflater mInflater;

    public MenuCategoryAdapter(Context mContext, List<CategoryVo> categories) {
        this.mContext = mContext;
        this.categories = categories;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return categories.size();
    }


    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.menu_category_item, null);//set layout for displaying items

        ((TextView)convertView.findViewById(R.id.category_name)).setText(categories.get(position).getName() + " " +
                categories.get(0).getSubCategoryName());

        return convertView;
    }


    public List<CategoryVo> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryVo> categories) {
        this.categories = categories;
    }
}
