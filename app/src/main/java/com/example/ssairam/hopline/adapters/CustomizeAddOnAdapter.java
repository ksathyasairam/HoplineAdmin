package com.example.ssairam.hopline.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.activity_ui.Inventory;
import com.example.ssairam.hopline.vo.ProductVo;
import com.example.ssairam.hopline.vo.Stock;

/**
 * Created by ssairam on 12/3/2016.
 */

public class CustomizeAddOnAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    ProductVo productVo;
    Context context;
    LayoutInflater mInflater;

    public CustomizeAddOnAdapter(Context context, ProductVo productVo) {
        this.productVo=productVo;
        this.context=context;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return productVo.getAddOns().size();
    }

    @Override
    public Object getItem(int position) {
        return productVo.getAddOns().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.customize_add_on_row, null);//set layout for displaying items

        ((TextView)convertView.findViewById(R.id.addOn_item_name)).setText(productVo.getAddOns().get(position).getName());
        CheckBox flag=(CheckBox) convertView.findViewById((R.id.checkBoxAddOn));


        flag.setChecked(productVo.getAddOns().get(position).isSelected());
//        Stock stock = new Stock();
//        stock.setProductId(menu.get(position).getProductId());
//
        flag.setTag(position);
        flag.setOnCheckedChangeListener(this);
        return convertView;
    }




    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked){
            productVo.getAddOns().get((Integer) buttonView.getTag()).setSelected(isChecked);
        }
        if(!isChecked){
            productVo.getAddOns().get((Integer) buttonView.getTag()).setSelected(isChecked);

        }

    }
}
