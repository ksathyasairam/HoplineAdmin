package com.example.ssairam.hopline.Dialogs;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.adapters.CreateOrder_OrderProductAdaptor;
import com.example.ssairam.hopline.fragments.NewOrderFragment;
import com.example.ssairam.hopline.vo.OrderVo;
import com.example.ssairam.hopline.vo.UserVo;

public class CreateOrderDialog {
        OrderVo orderVo;
        NewOrderFragment.PrintBillListner printBillListener;
        Activity activity;

        public void setData(OrderVo orderVo, NewOrderFragment.PrintBillListner printBillListner, Activity activity) {
            this.orderVo = orderVo;
            this.printBillListener = printBillListner;
            this.activity = activity;
        }



        public AlertDialog creatDialog() {

            LayoutInflater inflater = activity.getLayoutInflater();

            final View layout = inflater.inflate(R.layout.create_order_dialog,null);

            ListView listView = (ListView) layout.findViewById(R.id.create_order_list);
            listView.setAdapter(new CreateOrder_OrderProductAdaptor(activity,orderVo.getOrderProducts()));


            final AlertDialog d = new AlertDialog.Builder(activity)
                    .setView(layout)
                    .setPositiveButton("PRINT BILL", null) //Set to null. We override the onclick
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();

            d.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {

                    Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            String nameInput = ((EditText) layout.findViewById(R.id.editTextName)).getText().toString();
                            String phoneInput = ((EditText) layout.findViewById(R.id.editTextPhone)).getText().toString();

                            if (nameInput == null || nameInput.length() < 3) {
                                Toast.makeText(activity,"Invalid name",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (phoneInput == null || phoneInput.length() != 10){
                                Toast.makeText(activity,"Invalid phone number",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            UserVo user = new UserVo();
                            user.setName(nameInput);
                            user.setPhone(phoneInput);
                            orderVo.setUser(user);
                            printBillListener.OnPrintBill(orderVo);
                            d.dismiss();
                        }
                    });


                }
            });


            return d;

        }

    }