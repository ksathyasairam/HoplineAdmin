package com.example.ssairam.hopline.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.adapters.CartAdapter;
import com.example.ssairam.hopline.adapters.MenuCategoryAdapter;
import com.example.ssairam.hopline.adapters.MenuItemAdapter;
import com.example.ssairam.hopline.vo.CategoryVo;
import com.example.ssairam.hopline.vo.OrderProductVo;
import com.example.ssairam.hopline.vo.ProductVo;
import com.example.ssairam.hopline.vo.ShopVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

        categoryListView = (ListView) layout.findViewById(R.id.list_view_menu_category);
        categoryAdapter = new MenuCategoryAdapter(getActivity().getApplicationContext(), getCat());
        categoryListView.setAdapter(categoryAdapter);
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemAdapter.setCategory(categoryAdapter.getCategories().get(position));
            }
        });


        itemRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view_menu_items);
        RecyclerView.LayoutManager menuItemLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        itemRecyclerView.setLayoutManager(menuItemLayoutManager);
        itemAdapter = createMenuItemAdaptor();
        itemRecyclerView.setAdapter(itemAdapter);


        cartListView =(ListView) layout.findViewById(R.id.list_view_cart);
        cartAdapter=new CartAdapter(getActivity().getApplicationContext());
        cartListView.setAdapter(cartAdapter);


        return layout;
    }

    private MenuItemAdapter createMenuItemAdaptor() {
        return new MenuItemAdapter(getActivity().getApplicationContext(), categoryAdapter.getCategories().get(0), new View.OnClickListener() {

            @Override
            public void onClick(View v) {                   //add Button
                int position = (Integer) v.getTag();

                cartAdapter.addProduct(toOrderProductVo(itemAdapter.getCategory().getProducts().get(position)));

                Toast.makeText(getActivity().getApplicationContext(),"add",Toast.LENGTH_LONG).show();

            }

        }, new View.OnClickListener() {

            @Override
            public void onClick(View v) {                //customizeButton button
                int position = (Integer) v.getTag();

                Toast.makeText(getActivity().getApplicationContext(),"customizeButton",Toast.LENGTH_LONG).show();

            }

        });
    }

    public class MenuItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Toast.makeText(getActivity().getApplicationContext(), "add/customizeButton", Toast.LENGTH_SHORT).show();


        }
    }


    private OrderProductVo toOrderProductVo(ProductVo productVo) {

        OrderProductVo orderProductVo = new OrderProductVo();
        orderProductVo.setProduct(productVo);
        orderProductVo.setCount(productVo.getQuantity());

        return orderProductVo;
    }

    public void onCreateOrder(View view){
        if(cartAdapter.getOrder().getOrderProducts().isEmpty()) return;

        ShopVo shop = new ShopVo();
        shop.setIdshop(1);
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


    private List<CategoryVo> getCat() {

        String menuJsonString = "[{\"idcategory\":18,\"shop\":{\"idshop\":1,\"shopName\":\"Bistro 37\",\"phone\":\"9560558201\",\"activeYn\":\"Y\"},\"name\":\"Sandwiches\",\"subCategoryName\":\"-\",\"sortId\":1,\"imgUrl\":\"images/sandwich vector.png\",\"products\":[{\"productId\":9,\"name\":\"Wow\",\"shortDesc\":\"Oriental Paneer Sandwich\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":6,\"name\":\"Asian Ratatouille\",\"shortDesc\":\"English Vegetable Sandwich\",\"longDesc\":\"-\",\"price\":100.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":15,\"name\":\"Dreamy Chicken\",\"shortDesc\":\"Chicken In White Sauce  And Cheese\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":11,\"name\":\"Plain Josh\",\"shortDesc\":\"Chicken In Thousand Island Dressing Sandwich\",\"longDesc\":\"-\",\"price\":120.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":2,\"name\":\"Plain Jane\",\"shortDesc\":\"Classic Vegetable And Mayo Sandwich\",\"longDesc\":\"A simple yet classic non-Grilled Vegetable Sa\",\"price\":80.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[{\"idaddOn\":2,\"name\":\"ketchup\",\"price\":10.0,\"stockYn\":\"Y\",\"selected\":false},{\"idaddOn\":1,\"name\":\"extra cheese\",\"price\":40.0,\"stockYn\":\"Y\",\"selected\":false}],\"isExpanded\":false,\"quantity\":1},{\"productId\":16,\"name\":\"Indiana Jones \",\"shortDesc\":\"Indian Spiced Chicken Sandwich\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":3,\"name\":\"Bombay Veg Sandwich\",\"shortDesc\":\"Green Chutney Sandwich\",\"longDesc\":\"-\",\"price\":100.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[{\"idaddOn\":2,\"name\":\"ketchup\",\"price\":10.0,\"stockYn\":\"Y\",\"selected\":false},{\"idaddOn\":1,\"name\":\"extra cheese\",\"price\":40.0,\"stockYn\":\"Y\",\"selected\":false}],\"isExpanded\":false,\"quantity\":1},{\"productId\":8,\"name\":\"Burner\",\"shortDesc\":\"Spicy Paneer Sandwich\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":10,\"name\":\"Pk\",\"shortDesc\":\"Paneer Ka Sandwich\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":17,\"name\":\"The Loaded Ming\",\"shortDesc\":\"Oriental Chicken Sandwich\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":12,\"name\":\"The Dc Special\",\"shortDesc\":\"Grilled Chicken With Saut\u00E9ed Vegetables Sandw\",\"longDesc\":\"-\",\"price\":140.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":7,\"name\":\"Cheesy Corn Sandwich\",\"shortDesc\":\"-\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":13,\"name\":\"Green Inferno\",\"shortDesc\":\"Hot And Spicy Chicken Sandwich\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":18,\"name\":\"B37 Club Sandwich\",\"shortDesc\":\"-\",\"longDesc\":\"-\",\"price\":140.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":14,\"name\":\"Aztec World\",\"shortDesc\":\"Chicken And Salsa Sandwich\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":5,\"name\":\"Mr Spicy Soyabean\",\"shortDesc\":\"Healthy Soya Chaap Sandwich\",\"longDesc\":\"-\",\"price\":100.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":4,\"name\":\"Popeye\u2019S Sandwich\",\"shortDesc\":\"Healthy Spinach Corn Sandwich\",\"longDesc\":\"-\",\"price\":100.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1}]},{\"idcategory\":19,\"shop\":{\"idshop\":1,\"shopName\":\"Bistro 37\",\"phone\":\"9560558201\",\"activeYn\":\"Y\"},\"name\":\"Burgers\",\"subCategoryName\":\"-\",\"sortId\":2,\"imgUrl\":\"images/burger.png\",\"products\":[{\"productId\":20,\"name\":\"Cottage Jack\",\"shortDesc\":\"Paneer Patty Flavoured With Exotic Veggies\",\"longDesc\":\"-\",\"price\":90.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":30,\"name\":\"Old School\",\"shortDesc\":\"Lamb Burger\",\"longDesc\":\"-\",\"price\":100.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":29,\"name\":\"Atomic\",\"shortDesc\":\"Spicy Grilled Chicken Patty Burger\",\"longDesc\":\"-\",\"price\":120.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":24,\"name\":\"Road Runner \",\"shortDesc\":\"Omelette In Maska Bun\",\"longDesc\":\"-\",\"price\":80.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":25,\"name\":\"Gobbler\",\"shortDesc\":\"Chicken Burger\",\"longDesc\":\"-\",\"price\":90.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":21,\"name\":\"Thin Lizzy\",\"shortDesc\":\"Double Patty Veg Burger\",\"longDesc\":\"-\",\"price\":120.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":27,\"name\":\"Tropical Torpedo\",\"shortDesc\":\"Thai Of Chicken Burger\",\"longDesc\":\"-\",\"price\":120.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":19,\"name\":\"B37 Veg Burger\",\"shortDesc\":\"Home Made Vegetable Patty Burger\",\"longDesc\":\"-\",\"price\":80.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":32,\"name\":\"Roman\",\"shortDesc\":\"Spicy Grilled Lamb Patty Burger\",\"longDesc\":\"-\",\"price\":140.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":22,\"name\":\"Unnatural\",\"shortDesc\":\"Spicy Patty Veg Burger\",\"longDesc\":\"-\",\"price\":90.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":28,\"name\":\"John Snow\",\"shortDesc\":\"Grilled Chicken Patty Burger\",\"longDesc\":\"-\",\"price\":120.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":23,\"name\":\"Sweet Chilly O Mine\",\"shortDesc\":\"With Nothing Sweet Double Spicy Patty Veg Bur\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":26,\"name\":\"New School\",\"shortDesc\":\"Double Patty Chicken Burger\",\"longDesc\":\"-\",\"price\":150.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":31,\"name\":\"The Bogart\",\"shortDesc\":\"Double Lamb Patty Burger\",\"longDesc\":\"-\",\"price\":160.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1}]},{\"idcategory\":20,\"shop\":{\"idshop\":1,\"shopName\":\"Bistro 37\",\"phone\":\"9560558201\",\"activeYn\":\"Y\"},\"name\":\"Munchies\",\"subCategoryName\":\"-\",\"sortId\":3,\"imgUrl\":\"images/munchies.png\",\"products\":[{\"productId\":36,\"name\":\"Cheesy Baked Fries\",\"shortDesc\":\"A Perfect Mouth Stirrer Start Munching\",\"longDesc\":\"-\",\"price\":100.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":34,\"name\":\"Rhcp Fries\",\"shortDesc\":\"Red Hot Chilly Pepper Fries\",\"longDesc\":\"-\",\"price\":80.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":37,\"name\":\"Cheesy Baked Fries With Chicken\",\"shortDesc\":\"Do We Need To Explain More\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":38,\"name\":\"French Toast\",\"shortDesc\":\"Savoury Indian Style 8 Pcs\",\"longDesc\":\"-\",\"price\":100.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":35,\"name\":\"Desi Tadka Fries\",\"shortDesc\":\"Spiced Fries Bhelpuri Style\",\"longDesc\":\"-\",\"price\":90.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":33,\"name\":\"Classic Salted Fries\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":70.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1}]},{\"idcategory\":21,\"shop\":{\"idshop\":1,\"shopName\":\"Bistro 37\",\"phone\":\"9560558201\",\"activeYn\":\"Y\"},\"name\":\"Street Chineese \",\"subCategoryName\":\"Dimsums\",\"sortId\":4,\"imgUrl\":\"images/streetchinese.png\",\"products\":[{\"productId\":51,\"name\":\"Mushroom And Corn Dimsums\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":70.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":52,\"name\":\"Cheese And Corn Dimsums\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":90.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":50,\"name\":\"Veg Dimsums\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":70.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":53,\"name\":\"Ginger Chicken\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":120.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1}]},{\"idcategory\":22,\"shop\":{\"idshop\":1,\"shopName\":\"Bistro 37\",\"phone\":\"9560558201\",\"activeYn\":\"Y\"},\"name\":\"Street Chineese \",\"subCategoryName\":\"Street Chineese Appitizers\",\"sortId\":5,\"imgUrl\":\"images/streetchinese.png\",\"products\":[{\"productId\":42,\"name\":\"Stir Fried Assorted Vegetables Salt And Peppe\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":44,\"name\":\"Cantonese Salt And Pepper Chicken\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":170.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":41,\"name\":\"Golden Fried Baby Corn\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":120.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":43,\"name\":\"Spicy Sichuan Paneer\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":150.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":39,\"name\":\"Crispy Corn Kernels With Pepper And Salt\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":120.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":45,\"name\":\"Wok Fried Singh-Hoi Chicken\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":170.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":40,\"name\":\"Crispy Chilli Potato Fries\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":120.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1}]},{\"idcategory\":23,\"shop\":{\"idshop\":1,\"shopName\":\"Bistro 37\",\"phone\":\"9560558201\",\"activeYn\":\"Y\"},\"name\":\"Street Chineese \",\"subCategoryName\":\"Street Chineese Noodles\",\"sortId\":6,\"imgUrl\":\"images/streetchinese.png\",\"products\":[{\"productId\":48,\"name\":\"Veg Haka Noodles\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":100.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":49,\"name\":\"Non Veg Hakka Noodles\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":150.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":47,\"name\":\"Non Veg Sichuan Pepper And Elephant Garlic No\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":150.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":46,\"name\":\"Veg Sichuan Pepper And Elephant Garlic Noodle\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":100.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1}]},{\"idcategory\":24,\"shop\":{\"idshop\":1,\"shopName\":\"Bistro 37\",\"phone\":\"9560558201\",\"activeYn\":\"Y\"},\"name\":\"Salad\",\"subCategoryName\":\"Salads for the soul\",\"sortId\":7,\"imgUrl\":\"images/salad.png\",\"products\":[{\"productId\":54,\"name\":\"Tabuli Salad\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":120.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":56,\"name\":\"Drilled Veg Salad\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":150.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":55,\"name\":\"Green Garden Salad\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":120.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":57,\"name\":\"Muscles Max\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":150.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1}]},{\"idcategory\":25,\"shop\":{\"idshop\":1,\"shopName\":\"Bistro 37\",\"phone\":\"9560558201\",\"activeYn\":\"Y\"},\"name\":\"Salad\",\"subCategoryName\":\"Rich Mayo Salads\",\"sortId\":8,\"imgUrl\":\"images/salad.png\",\"products\":[{\"productId\":60,\"name\":\"Caribbean Salad\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":120.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":58,\"name\":\"B37 Salad\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":120.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":61,\"name\":\"Pasta Salad\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":120.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":59,\"name\":\"Tossed Salad\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":120.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1}]},{\"idcategory\":26,\"shop\":{\"idshop\":1,\"shopName\":\"Bistro 37\",\"phone\":\"9560558201\",\"activeYn\":\"Y\"},\"name\":\"Pasta\",\"subCategoryName\":\"-\",\"sortId\":9,\"products\":[{\"productId\":75,\"name\":\"Chicken B37 Thai Red Curry Pasta\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":180.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":77,\"name\":\"Chicken B37 Thai Green Curry Pasta\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":180.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":71,\"name\":\"Chicken Pastain Two Sauces\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":170.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":73,\"name\":\"Chicken Indo-Chini Pasta\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":170.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":65,\"name\":\"Chicken Bechamel And Cheese Sause Pasta\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":170.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":74,\"name\":\"Veg B37 Thai Red Curry Pasta\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":150.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":68,\"name\":\"Veg Penne Al Pesto\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":150.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":72,\"name\":\"Veg Indo-Chini Pasta\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":140.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":66,\"name\":\"Veg Aglio \\u0027E\\u0027 Olio\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":140.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":67,\"name\":\"Chicken Aglio \\u0027E\\u0027 Olio\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":170.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":76,\"name\":\"Veg B37 Thai Green Curry Pasta\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":150.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":69,\"name\":\"Chicken Penne Al Pesto\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":180.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":70,\"name\":\"Veg Pasta In Two Sauces\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":140.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":64,\"name\":\"Veg Bechamel And Cheese Sause Pasta\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":140.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":63,\"name\":\"Chicken Pasta In Arrabiata Sause\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":170.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":62,\"name\":\"Veg Pasta In Arrabiata Sause\",\"shortDesc\":\"\",\"longDesc\":\"-\",\"price\":140.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1}]},{\"idcategory\":27,\"shop\":{\"idshop\":1,\"shopName\":\"Bistro 37\",\"phone\":\"9560558201\",\"activeYn\":\"Y\"},\"name\":\"Hoagie\",\"subCategoryName\":\"-\",\"sortId\":10,\"imgUrl\":\"images/hoagieee.png\",\"products\":[{\"productId\":79,\"name\":\"The Mario\",\"shortDesc\":\"Go Down The Memory Lane With This Hoagie.. Al\",\"longDesc\":\"-\",\"price\":100.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":80,\"name\":\"Sloppy Paneer Joe\",\"shortDesc\":\"Indian Sloppy Joe Made With Paneer .. Bhurji \",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":81,\"name\":\"Lucy Paneer\",\"shortDesc\":\"Where West Meets The East! Paneer In White Sa\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":86,\"name\":\"Protien Buff\",\"shortDesc\":\"Boiled Egg White Hoagie\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":82,\"name\":\"Juicy Chicken \",\"shortDesc\":\"White Sauce And Chicken\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":85,\"name\":\"Chicken Salami\",\"shortDesc\":\"Sliced Cold Cuts Of Chicken Delightful\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":83,\"name\":\"Chilli Mayo And Chicken \",\"shortDesc\":\"Homemade Chilli Mayo And Chicken\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":78,\"name\":\"Original Brisbane\",\"shortDesc\":\"A Healthy Hoagie A Veggie Hoagie\",\"longDesc\":\"-\",\"price\":100.000,\"vegYn\":\"Y\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1},{\"productId\":84,\"name\":\"Shards Of Pesto\",\"shortDesc\":\"Chicken In Frest Pesto\",\"longDesc\":\"-\",\"price\":130.000,\"vegYn\":\"N\",\"stockYn\":\"Y\",\"addOns\":[],\"isExpanded\":false,\"quantity\":1}]}]";

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();


        Type listType = new TypeToken<ArrayList<CategoryVo>>() {
        }.getType();

        List<CategoryVo> categoryVos = gson.fromJson(menuJsonString, listType);

        return categoryVos;
    }


}
