package com.example.ssairam.hopline.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.Toast;

import com.example.ssairam.hopline.DataStore;
import com.example.ssairam.hopline.Dialogs.CreateOrderDialogFragment;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.ServerHelper;
import com.example.ssairam.hopline.Util;
import com.example.ssairam.hopline.adapters.CartAdapter;
import com.example.ssairam.hopline.adapters.MenuCategoryAdapter;
import com.example.ssairam.hopline.adapters.MenuItemAdapter;
import com.example.ssairam.hopline.vo.OrderProductVo;
import com.example.ssairam.hopline.vo.OrderVo;
import com.example.ssairam.hopline.vo.ProductVo;
import com.example.ssairam.hopline.vo.ShopVo;

public class NewOrderFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    //Views
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
        View layout = inflater.inflate(R.layout.fragment_new_order, container, false);

        ((Button) layout.findViewById(R.id.create_order_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrderOnClick();
            }
        });

        categoryListView = (ListView) layout.findViewById(R.id.list_view_menu_category);
        categoryAdapter = new MenuCategoryAdapter(getActivity().getApplicationContext(), DataStore.getMenuCategories());
        categoryListView.setAdapter(categoryAdapter);
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemAdapter.setCategory(categoryAdapter.getCategories().get(position));
            }
        });


        itemRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view_menu_items);
        RecyclerView.LayoutManager menuItemLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
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

                cartAdapter.getOrderProducts().remove(position);
                cartAdapter.notifyDataSetChanged();
            }
        });
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
                        cartAdapter.getOrderProducts().remove((int)cartIndex);
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

                Toast.makeText(getActivity().getApplicationContext(), "customizeButton", Toast.LENGTH_LONG).show();

            }

        });
    }


    private OrderProductVo toOrderProductVo(ProductVo productVo) {

        OrderProductVo orderProductVo = new OrderProductVo();
        orderProductVo.setProduct(productVo);
        orderProductVo.setCount(productVo.getQuantity());

        return orderProductVo;
    }

    public void createOrderOnClick() {
        if (cartAdapter.getOrder().getOrderProducts().isEmpty()) return;

        OrderVo order = cartAdapter.getOrder();

        ShopVo shop = new ShopVo();
        shop.setIdshop(1);
        order.setShop(shop);



        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        CreateOrderDialogFragment newFragment = new CreateOrderDialogFragment();
        newFragment.setData(order, new PrintBillListner() {
            @Override
            public void OnPrintBill(OrderVo order) {
                    new CreateWalkInOrder(order).execute("");
            }
        });
        newFragment.show(fragmentManager, "createOrderDialog");

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
                orderFromServer = ServerHelper.createWalkInOrder(order);
                return  true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {
                Util.printBill(orderFromServer);
                DataStore.getPreparingOrders().add(orderFromServer);
                Toast.makeText(getActivity(), "Success!!", Toast.LENGTH_SHORT).show();
            } else {
                createCompletOfflineOrder(order);
            }

            if (dialog != null)
                dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            dialog = Util.showProgressDialog(getActivity());


        }
    }

    private void createCompletOfflineOrder(OrderVo order) {
        Toast.makeText(getActivity(), "Error communicating with server!!", Toast.LENGTH_SHORT).show();
    }

    public interface PrintBillListner{
        public void OnPrintBill(OrderVo order);
    }



}
