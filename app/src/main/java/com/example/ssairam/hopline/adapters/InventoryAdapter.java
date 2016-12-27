package com.example.ssairam.hopline.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssairam.hopline.DataStore;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.ServerHelper;
import com.example.ssairam.hopline.Util;
import com.example.ssairam.hopline.activity_ui.Inventory;
import com.example.ssairam.hopline.vo.OrderVo;
import com.example.ssairam.hopline.vo.ProductVo;
import com.example.ssairam.hopline.vo.Stock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssairam on 11/20/2016.
 */

public class InventoryAdapter extends BaseAdapter implements Filterable{
    List<ProductVo> menu;
    LayoutInflater mInflater;
    CompoundButton.OnCheckedChangeListener onCheckChangeListener;
    Context context;
    FriendFilter friendFilter=null;
    List<ProductVo> filteredList;


    public InventoryAdapter(List<ProductVo> menu, Context context, CompoundButton.OnCheckedChangeListener onCheckChangeListener) {
        this.menu = menu;
        this.filteredList = menu;
        this.context = context;
        this.onCheckChangeListener = onCheckChangeListener;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.inventory_row_item, null);//set layout for displaying items

        ((TextView)convertView.findViewById(R.id.inventory_item_name)).setText(filteredList.get(position).getName());
        CheckBox flag=(CheckBox) convertView.findViewById((R.id.checkBox));
        flag.setChecked(("Y".equals(filteredList.get(position).getStockYn())?true:false));
        Stock stock = new Stock();
        stock.setProductId(filteredList.get(position).getProductId());

        flag.setTag(position);
        flag.setOnCheckedChangeListener(onCheckChangeListener);
        return convertView;

    }

    public List<ProductVo> getMenu() {
        return filteredList;
    }

    public void setMenu(List<ProductVo> menu) {
        this.menu = menu;
        getFilter().filter(lastSearch);
    }

    @Override
    public Filter getFilter() {
        if (friendFilter == null) {
            friendFilter = new FriendFilter();
        }
        return friendFilter;
    }

    private String lastSearch = null;
    /**
     * Custom filter for friend list
     * Filter content in friend list according to the search text
     */
    private class FriendFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            lastSearch = String.valueOf(constraint);
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<ProductVo> tempList = new ArrayList<ProductVo>();

                // search content in friend list
                for (ProductVo productVo : menu) {
                    if (productVo.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(productVo);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = menu.size();
                filterResults.values = menu;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<ProductVo>) results.values;
            notifyDataSetChanged();
        }
    }
}
