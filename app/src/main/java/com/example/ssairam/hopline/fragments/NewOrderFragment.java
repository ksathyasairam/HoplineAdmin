package com.example.ssairam.hopline.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssairam.hopline.DataStore;
import com.example.ssairam.hopline.Dialogs.CreateOrderDialog;
import com.example.ssairam.hopline.MainPrefs;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.ServerHelper;
import com.example.ssairam.hopline.Util;
import com.example.ssairam.hopline.activity_ui.MainActivity;
import com.example.ssairam.hopline.adapters.CartAdapter;
import com.example.ssairam.hopline.adapters.CustomizeAddOnAdapter;
import com.example.ssairam.hopline.adapters.MenuCategoryAdapter;
import com.example.ssairam.hopline.adapters.MenuItemAdapter;
import com.example.ssairam.hopline.vo.AddOnVo;
import com.example.ssairam.hopline.vo.OrderProductAddonVo;
import com.example.ssairam.hopline.vo.OrderProductVo;
import com.example.ssairam.hopline.vo.OrderVo;
import com.example.ssairam.hopline.vo.ProductVo;
import com.example.ssairam.hopline.vo.ShopVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewOrderFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    //Views
    private View layout;
    private ListView categoryListView;
    private RecyclerView itemRecyclerView;
    private ListView cartListView;

    MenuCategoryAdapter categoryAdapter;
    MenuItemAdapter itemAdapter;
    CartAdapter cartAdapter;

    public NewOrderFragment() {
        // Required empty public constructor
    }

    public static NewOrderFragment newInstance(String param1, String param2) {
        NewOrderFragment fragment = new NewOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_new_order, container, false);

        ((Button) layout.findViewById(R.id.create_order_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrderOnClick();
            }
        });

        categoryListView = (ListView) layout.findViewById(R.id.list_view_menu_category);
        categoryAdapter = new MenuCategoryAdapter(getActivity().getApplicationContext(), DataStore.getMenuCategories(getActivity().getApplicationContext()));
        categoryListView.setAdapter(categoryAdapter);
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemAdapter.setCategory(categoryAdapter.getCategories().get(position));
            }
        });


        itemRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view_menu_items);
        RecyclerView.LayoutManager menuItemLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 4);
        itemRecyclerView.setLayoutManager(menuItemLayoutManager);
        itemAdapter = createMenuItemAdaptor();
        itemRecyclerView.setAdapter(itemAdapter);


        cartListView = (ListView) layout.findViewById(R.id.list_view_cart);
        cartAdapter = new CartAdapter(getActivity().getApplicationContext(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();

                OrderProductVo orderProductVo = cartAdapter.getOrderProducts().get(position);

                if(!cartAdapter.isCustomizedOrder(orderProductVo)){
                    itemAdapter.resetProductCountById(orderProductVo.getProduct().getProductId());
                }

                cartAdapter.removeProduct(position);
                cartAdapter.notifyDataSetChanged();
            }
        }, new TotalItemCountPriceChangeListener());
        cartListView.setAdapter(cartAdapter);


        return layout;
    }

    private MenuItemAdapter createMenuItemAdaptor() {
        return new MenuItemAdapter(getActivity().getApplicationContext(), categoryAdapter.getCategories().get(0), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //increase quantity


                int position = (Integer) v.getTag();
                ProductVo productVo = itemAdapter.getCategory().getProducts().get(position);

                int quantity = productVo.getQuantity();
                productVo.setQuantity(quantity + 1);
                itemAdapter.notifyDataSetChanged();




                Integer cartIndex = cartAdapter.getNotCustomizedOrderIndex(productVo.getProductId());

                if (cartIndex == null) {
                    cartAdapter.addProduct(toOrderProductVo(productVo));
                } else {
                    cartAdapter.getOrderProducts().get(cartIndex).setCount(productVo.getQuantity());
                }

                cartAdapter.notifyDataSetChanged();

            }

        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //decrease quantity

                int position = (Integer) v.getTag();
                ProductVo productVo = itemAdapter.getCategory().getProducts().get(position);

                int quantity = productVo.getQuantity();
                if ((quantity-1) < 0) return;

                productVo.setQuantity(quantity - 1);
                itemAdapter.notifyDataSetChanged();



                Integer cartIndex = cartAdapter.getNotCustomizedOrderIndex(productVo.getProductId());

                if (cartIndex != null){
                    if (productVo.getQuantity() == 0) {
                        cartAdapter.removeProduct(cartIndex);
                    } else {
                        cartAdapter.getOrderProducts().get(cartIndex).setCount(productVo.getQuantity());
                    }
                }

                cartAdapter.notifyDataSetChanged();

            }
        }, new View.OnClickListener() {

            @Override
            public void onClick(View v) {                //customizeButton button
                int position = (Integer) v.getTag();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                CustomizeDialog dialog = new CustomizeDialog();
                OrderVo order=cartAdapter.getOrder();
                ProductVo productVo=itemAdapter.getCategory().getProducts().get(position);
                dialog.setOrderVo(order,productVo);
                dialog.setListners(new OkOnClickListner());
                Util.showDialogImmersive(getActivity(),dialog.createDialog());

//                Toast.makeText(getActivity().getApplicationContext(), "customizeButton", Toast.LENGTH_LONG).show();

            }

        });
    }

    private class OkOnClickListner implements  View.OnClickListener{

        OrderVo order;
        ProductVo productVo;
        List<AddOnVo> addOnVos;

        public List<AddOnVo> getAddOnVos() {
            return addOnVos;
        }

        public void setAddOnVos(List<AddOnVo> addOnVos) {
            this.addOnVos = addOnVos;
        }

        public ProductVo getProductVo() {
            return productVo;
        }

        public void setProductVo(ProductVo productVo) {
            this.productVo = productVo;
        }

        public OrderVo getOrder() {
            return order;
        }

        public void setOrder(OrderVo order) {
            this.order = order;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Ok customized", Toast.LENGTH_SHORT).show();
            productVo.setQuantity(productVo.getQuantity() + 1);
            order.getOrderProducts().add(toOrderProductVoWithAddons(productVo, addOnVos));
            itemAdapter.notifyDataSetChanged();
            cartAdapter.notifyDataSetChanged();
        }
    }


    public  class CustomizeDialog {

        OrderVo orderVo;
        List<AddOnVo> addOnVos;
        ProductVo productVo;
        public void setOrderVo(OrderVo orderVo,ProductVo productVo) {
            this.orderVo = orderVo;
            this.productVo=productVo;
        }

        View view;
        private OkOnClickListner okListener;


        public void setView(View view){
            this.view = view;
        }
        public void setListners(OkOnClickListner okListener){
            this.okListener = okListener;

        }
        private List<AddOnVo> getaddOns(ProductVo productVo) {
            List<AddOnVo> addOnVos= new ArrayList<>();
            List<AddOnVo> addons=productVo.getAddOns();
            for (AddOnVo addon : addons){
                if(addon.isSelected()){
                    addOnVos.add(addon);
                }
            }

            return addOnVos;
        }

        public AlertDialog createDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(NewOrderFragment.this.getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            final View layout = inflater.inflate(R.layout.customization_dialog,null);
            ((TextView) layout.findViewById(R.id.product_name)).setText(productVo.getName());
            ListView listView = (ListView) layout.findViewById(R.id.list_view);
            listView.setAdapter(new CustomizeAddOnAdapter(getActivity().getApplicationContext(),productVo));

            builder.setView(layout)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            addOnVos=getaddOns(productVo);
                            okListener.setProductVo(productVo);
                            okListener.setAddOnVos(addOnVos);
                            okListener.setOrder(orderVo);
                            okListener.onClick(view);
                        }


                    })
            ;
            return builder.create();
        }

    }



   public OrderProductVo toOrderProductVo(ProductVo productVo) {

        OrderProductVo orderProductVo = new OrderProductVo();
        orderProductVo.setProduct(productVo);
        orderProductVo.setCount(productVo.getQuantity());

        return orderProductVo;
    }

    public OrderProductVo toOrderProductVoWithAddons(ProductVo productVo, List<AddOnVo> addons) {

        OrderProductVo orderProductVo = toOrderProductVo(productVo);
        orderProductVo.setCount(1);

        if (addons != null && !addons.isEmpty()) {
            orderProductVo.setOrderProductAddons(new ArrayList<OrderProductAddonVo>());
            for (AddOnVo addon : addons){
                OrderProductAddonVo orderProductAddonVo = new OrderProductAddonVo();
                orderProductAddonVo.setAddOn(addon);
                orderProductVo.getOrderProductAddons().add(orderProductAddonVo);
            }
        }

        return orderProductVo;
    }

    public void createOrderOnClick() {
        if (cartAdapter.getOrder().getOrderProducts() == null || cartAdapter.getOrder().getOrderProducts().isEmpty()) return;

        OrderVo order = cartAdapter.getOrder();

        ShopVo shop = new ShopVo();
        shop.setIdshop(1);
        order.setShop(shop);



        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        CreateOrderDialog dialog = new CreateOrderDialog();
        dialog.setData(order, new PrintBillListner() {
            @Override
            public void OnPrintBill(OrderVo order) {

//                if (PrinterHelperBackup.get().isBluetoothOn()){
                    new CreateWalkInOrder(order).execute("");
//                } else {
//                    Toast.makeText(getActivity(), "Printer connection FAILED! MAKE SURE BLUETOOTH IS TURNED ON AND CONNECTED TO PRINTER", Toast.LENGTH_LONG).show();
//                }


            }
        },getActivity());
        Util.showDialogImmersive(getActivity(),dialog.creatDialog());
    }


    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




    private class CreateWalkInOrder extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        OrderVo order;
        OrderVo orderFromServer;

        CreateWalkInOrder(OrderVo order) {
            this.order = order;
            dialog = null;
            orderFromServer = null;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                orderFromServer = ServerHelper.createWalkInOrder(order,getActivity());
                return  true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {

//                if (Util.printBill(orderFromServer,getActivity())) {
                    Toast.makeText(getActivity(), R.string.orderSuccess, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), R.string.printFailedOrderSuccess, Toast.LENGTH_SHORT).show();
//                }

                DataStore.getPreparingOrders().add(orderFromServer);
            } else {
                createCompleteOfflineOrder(order);

//                if (Util.printBill(order,getActivity())) {
                    Toast.makeText(getActivity(), R.string.orderSuccess, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), R.string.printFailedOrderSuccess, Toast.LENGTH_SHORT).show();
//                }

            }

            clearAll();

            if (dialog != null)
                dialog.dismiss();
        }



        @Override
        protected void onPreExecute() {
            dialog = Util.showProgressDialog(getActivity());


        }
    }
    public void clearAll() {
        if(itemAdapter!=null){
            itemAdapter.resetAllProductCount();
        }
        if(cartAdapter!=null) {
            cartAdapter.clearCart();
        }
    }


    //TOTO : Modify for multiple Vendor;
    private void createCompleteOfflineOrder(OrderVo order) {
        int offlineOrderId = MainPrefs.getNewOfflineOrderNumber(getActivity().getApplicationContext());
        order.setCustomerOrderId(offlineOrderId);
        order.setIdorder(offlineOrderId);
        order.setOrderCreator("bistro 37");
        order.setOrderState("COMPLETE_OFFLINE_ORDER");
        order.setPaidYn("Y");
        order.setOrderTime(new Date());
        DataStore.addOfflineOrder(order,getActivity().getApplicationContext());
        Toast.makeText(getActivity(), "Unable to connect to server, OFFLINE ORDER CREATED!!", Toast.LENGTH_SHORT).show();
    }

    public interface PrintBillListner{
        public void OnPrintBill(OrderVo order);
    }


    public class TotalItemCountPriceChangeListener {
        public void onChange(int totalItem, double totalPrice) {
            ((TextView) layout.findViewById(R.id.total_item)).setText(totalItem+"");
//            ( (MainActivity)getActivity()).updateMenuQty("Qty : "+totalItem);

                        ((TextView) layout.findViewById(R.id.total_price)).setText(totalPrice+"");
//            ( (MainActivity)getActivity()).updateMenuPrice("Total : "+totalPrice);
        }
    }

}
