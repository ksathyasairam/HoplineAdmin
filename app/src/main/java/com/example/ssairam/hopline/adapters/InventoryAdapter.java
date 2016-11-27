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

import java.util.List;

/**
 * Created by ssairam on 11/20/2016.
 */

public class InventoryAdapter extends BaseAdapter  {
    List<ProductVo> menu;
    LayoutInflater mInflater;
    CompoundButton.OnCheckedChangeListener onCheckChangeListener;
    Context context;


    public InventoryAdapter(List<ProductVo> menu, Context context, CompoundButton.OnCheckedChangeListener onCheckChangeListener) {
        this.menu = menu;
        this.context = context;
        this.onCheckChangeListener = onCheckChangeListener;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return menu.size();
    }

    @Override
    public Object getItem(int position) {
        return menu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.inventory_row_item, null);//set layout for displaying items

        ((TextView)convertView.findViewById(R.id.inventory_item_name)).setText(menu.get(position).getName());
        CheckBox flag=(CheckBox) convertView.findViewById((R.id.checkBox));
        flag.setChecked(("Y".equals(menu.get(position).getStockYn())?true:false));
        Stock stock = new Stock();
        stock.setProductId(menu.get(position).getProductId());

        flag.setTag(position);
        flag.setOnCheckedChangeListener(onCheckChangeListener);
        return convertView;

    }

    public List<ProductVo> getMenu() {
        return menu;
    }

    public void setMenu(List<ProductVo> menu) {
        this.menu = menu;
    }
}
