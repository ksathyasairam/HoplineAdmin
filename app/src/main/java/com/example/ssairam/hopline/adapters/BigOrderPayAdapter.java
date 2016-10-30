package com.example.ssairam.hopline.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ssairam.hopline.CustomLinearLayoutManager;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.vo.OrderVo;

import java.util.List;


public class BigOrderPayAdapter extends RecyclerView.Adapter<BigOrderPayAdapter.ViewHolder> {
    private final View.OnClickListener removeOrderListener;
    private final View.OnClickListener printBillListener;
    private Context mContext;
    private List<OrderVo> orderVoList;

    public BigOrderPayAdapter(Context mContext, List<OrderVo> orderVoList, View.OnClickListener printBillListener, View.OnClickListener removeOrderListener) {
        this.mContext = mContext;
        this.orderVoList = orderVoList;
        this.printBillListener = printBillListener;
        this.removeOrderListener = removeOrderListener;
    }

    public void updateData( List<OrderVo> orderVoList) {
        this.orderVoList = orderVoList;
        notifyDataSetChanged();
    }

    public List<OrderVo> getData() {
        return orderVoList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Button buttonPrintBill, buttonRemoveOrder;
        public TextView customerOrderNo;
        public RecyclerView productList;
        public ViewHolder(View itemView) {
            super(itemView);
            buttonPrintBill =(Button)itemView.findViewById(R.id.button_print_bill);
            buttonRemoveOrder =(Button)itemView.findViewById(R.id.button_remove);
            customerOrderNo = (TextView) itemView.findViewById(R.id.customer_order_number);

            productList = (RecyclerView) itemView.findViewById(R.id.bo_order_product_list);
            RecyclerView.LayoutManager layoutManager= new CustomLinearLayoutManager(itemView.getContext().getApplicationContext());
            productList.setLayoutManager(layoutManager);


        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.big_order_pay_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OrderVo order = orderVoList.get(position);
        holder.customerOrderNo.setText("#" + order.getCustomerOrderId());

        holder.productList.setAdapter(new OrderProductItemAdaptor(order.getOrderProducts()));


        holder.buttonPrintBill.setTag(position);
        holder.buttonPrintBill.setOnClickListener(printBillListener);

        holder.buttonRemoveOrder.setTag(position);
        holder.buttonRemoveOrder.setOnClickListener(removeOrderListener);
    }


    @Override
    public int getItemCount() {
        return orderVoList.size();
    }
}
