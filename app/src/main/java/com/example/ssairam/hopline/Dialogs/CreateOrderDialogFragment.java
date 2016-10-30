package com.example.ssairam.hopline.Dialogs;



import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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

public class CreateOrderDialogFragment extends DialogFragment {
        OrderVo orderVo;
        NewOrderFragment.PrintBillListner printBillListener;

        public void setData(OrderVo orderVo, NewOrderFragment.PrintBillListner printBillListner) {
            this.orderVo = orderVo;
            this.printBillListener = printBillListner;
        }



        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            LayoutInflater inflater = getActivity().getLayoutInflater();

            final View layout = inflater.inflate(R.layout.create_order_dialog,null);

            ListView listView = (ListView) layout.findViewById(R.id.create_order_list);
            listView.setAdapter(new CreateOrder_OrderProductAdaptor(getActivity(),orderVo.getOrderProducts()));


            final AlertDialog d = new AlertDialog.Builder(getActivity())
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
                                Toast.makeText(getActivity(),"Invalid name",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (phoneInput == null || phoneInput.length() != 10){
                                Toast.makeText(getActivity(),"Invalid phone number",Toast.LENGTH_SHORT).show();
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