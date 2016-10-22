package com.example.ssairam.hopline.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.ssairam.hopline.FetchOrderTo;

import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.adapters.OrdersAdapter;
import com.example.ssairam.hopline.vo.OrderVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IncomingOrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IncomingOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomingOrderFragment extends Fragment {
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private List<OrderVo> orderVoList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public IncomingOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncomingOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IncomingOrderFragment newInstance(String param1, String param2) {
        IncomingOrderFragment fragment = new IncomingOrderFragment();
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
        // Inflate the layout for this fragment
        View Layout=inflater.inflate(R.layout.fragment_incoming_order, container, false);

        recyclerView = (RecyclerView) Layout.findViewById(R.id.recycler_view);

        orderVoList = getOrders();
        adapter = new OrdersAdapter(this.getActivity().getApplicationContext(), orderVoList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getActivity().getApplicationContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return Layout ;




}

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private List<OrderVo> getOrders() {

        String s = "{\"orderStates\":[\"CANCELLED\",\"PREPARING\"],\"orders\":[{\"cancelReason\":\"Testing\",\"customerOrderId\":190,\"idorder\":190,\"orderCreator\":\"\",\"orderProducts\":[{\"count\":1,\"idorderProduct\":411,\"orderProductAddons\":[],\"product\":{\"addOns\":[],\"expanded\":false,\"longDesc\":\"-\",\"name\":\"Asian Ratatouille\",\"price\":100,\"productId\":6,\"quantity\":1,\"shortDesc\":\"English Vegetable Sandwich\",\"stockYn\":\"Y\",\"vegYn\":\"Y\"}}],\"orderState\":\"CANCELLED\",\"orderTime\":\"2016-10-14T18:31:49\",\"paidYn\":\"N\",\"shop\":{\"activeYn\":\"Y\",\"idshop\":1,\"imgUrl\":null,\"phone\":\"9560558201\",\"shopName\":\"Bistro 37\"},\"totalItemCount\":1,\"totalPrice\":100,\"user\":{\"iduser\":47,\"name\":\"akshansh\",\"phone\":\"9999966666\"}},{\"cancelReason\":null,\"customerOrderId\":220,\"idorder\":220,\"orderCreator\":\"\",\"orderProducts\":[{\"count\":1,\"idorderProduct\":471,\"orderProductAddons\":[],\"product\":{\"addOns\":[],\"expanded\":false,\"longDesc\":\"-\",\"name\":\"Wow\",\"price\":130,\"productId\":9,\"quantity\":1,\"shortDesc\":\"Oriental Paneer Sandwich\",\"stockYn\":\"Y\",\"vegYn\":\"Y\"}},{\"count\":1,\"idorderProduct\":472,\"orderProductAddons\":[],\"product\":{\"addOns\":[],\"expanded\":false,\"longDesc\":\"-\",\"name\":\"Asian Ratatouille\",\"price\":100,\"productId\":6,\"quantity\":1,\"shortDesc\":\"English Vegetable Sandwich\",\"stockYn\":\"Y\",\"vegYn\":\"Y\"}}],\"orderState\":\"PREPARING\",\"orderTime\":\"2016-10-17T02:02:13\",\"paidYn\":\"Y\",\"shop\":{\"activeYn\":\"Y\",\"idshop\":1,\"imgUrl\":null,\"phone\":\"9560558201\",\"shopName\":\"Bistro 37\"},\"totalItemCount\":2,\"totalPrice\":230,\"user\":{\"iduser\":51,\"name\":\"ayush arnav\",\"phone\":\"9958675060\"}},{\"cancelReason\":null,\"customerOrderId\":227,\"idorder\":227,\"orderCreator\":\"\",\"orderProducts\":[{\"count\":1,\"idorderProduct\":491,\"orderProductAddons\":[],\"product\":{\"addOns\":[],\"expanded\":false,\"longDesc\":\"-\",\"name\":\"Asian Ratatouille\",\"price\":100,\"productId\":6,\"quantity\":1,\"shortDesc\":\"English Vegetable Sandwich\",\"stockYn\":\"Y\",\"vegYn\":\"Y\"}},{\"count\":1,\"idorderProduct\":492,\"orderProductAddons\":[],\"product\":{\"addOns\":[],\"expanded\":false,\"longDesc\":\"-\",\"name\":\"Wow\",\"price\":130,\"productId\":9,\"quantity\":1,\"shortDesc\":\"Oriental Paneer Sandwich\",\"stockYn\":\"Y\",\"vegYn\":\"Y\"}},{\"count\":1,\"idorderProduct\":490,\"orderProductAddons\":[],\"product\":{\"addOns\":[],\"expanded\":false,\"longDesc\":\"-\",\"name\":\"Asian Ratatouille\",\"price\":100,\"productId\":6,\"quantity\":1,\"shortDesc\":\"English Vegetable Sandwich\",\"stockYn\":\"Y\",\"vegYn\":\"Y\"}}],\"orderState\":\"CANCELLED\",\"orderTime\":\"2016-10-17T14:04:35\",\"paidYn\":\"N\",\"shop\":{\"activeYn\":\"Y\",\"idshop\":1,\"imgUrl\":null,\"phone\":\"9560558201\",\"shopName\":\"Bistro 37\"},\"totalItemCount\":3,\"totalPrice\":330,\"user\":{\"iduser\":51,\"name\":\"ayush arnav\",\"phone\":\"9958675060\"}},{\"cancelReason\":null,\"customerOrderId\":228,\"idorder\":228,\"orderCreator\":\"\",\"orderProducts\":[{\"count\":1,\"idorderProduct\":494,\"orderProductAddons\":[],\"product\":{\"addOns\":[],\"expanded\":false,\"longDesc\":\"-\",\"name\":\"Asian Ratatouille\",\"price\":100,\"productId\":6,\"quantity\":1,\"shortDesc\":\"English Vegetable Sandwich\",\"stockYn\":\"Y\",\"vegYn\":\"Y\"}},{\"count\":1,\"idorderProduct\":493,\"orderProductAddons\":[],\"product\":{\"addOns\":[],\"expanded\":false,\"longDesc\":\"-\",\"name\":\"Wow\",\"price\":130,\"productId\":9,\"quantity\":1,\"shortDesc\":\"Oriental Paneer Sandwich\",\"stockYn\":\"Y\",\"vegYn\":\"Y\"}}],\"orderState\":\"CANCELLED\",\"orderTime\":\"2016-10-17T15:42:11\",\"paidYn\":\"N\",\"shop\":{\"activeYn\":\"Y\",\"idshop\":1,\"imgUrl\":null,\"phone\":\"9560558201\",\"shopName\":\"Bistro 37\"},\"totalItemCount\":2,\"totalPrice\":230,\"user\":{\"iduser\":51,\"name\":\"ayush arnav\",\"phone\":\"9958675060\"}},{\"cancelReason\":null,\"customerOrderId\":229,\"idorder\":229,\"orderCreator\":\"\",\"orderProducts\":[{\"count\":1,\"idorderProduct\":496,\"orderProductAddons\":[],\"product\":{\"addOns\":[],\"expanded\":false,\"longDesc\":\"-\",\"name\":\"Wow\",\"price\":130,\"productId\":9,\"quantity\":1,\"shortDesc\":\"Oriental Paneer Sandwich\",\"stockYn\":\"Y\",\"vegYn\":\"Y\"}},{\"count\":1,\"idorderProduct\":495,\"orderProductAddons\":[],\"product\":{\"addOns\":[],\"expanded\":false,\"longDesc\":\"-\",\"name\":\"Asian Ratatouille\",\"price\":100,\"productId\":6,\"quantity\":1,\"shortDesc\":\"English Vegetable Sandwich\",\"stockYn\":\"Y\",\"vegYn\":\"Y\"}}],\"orderState\":\"CANCELLED\",\"orderTime\":\"2016-10-17T13:19:05\",\"paidYn\":\"N\",\"shop\":{\"activeYn\":\"Y\",\"idshop\":1,\"imgUrl\":null,\"phone\":\"9560558201\",\"shopName\":\"Bistro 37\"},\"totalItemCount\":2,\"totalPrice\":230,\"user\":{\"iduser\":51,\"name\":\"ayush arnav\",\"phone\":\"9958675060\"}},{\"cancelReason\":null,\"customerOrderId\":235,\"idorder\":235,\"orderCreator\":\"\",\"orderProducts\":[{\"count\":1,\"idorderProduct\":503,\"orderProductAddons\":[],\"product\":{\"addOns\":[],\"expanded\":false,\"longDesc\":\"-\",\"name\":\"The Loaded Ming\",\"price\":130,\"productId\":17,\"quantity\":1,\"shortDesc\":\"Oriental Chicken Sandwich\",\"stockYn\":\"Y\",\"vegYn\":\"N\"}}],\"orderState\":\"PREPARING\",\"orderTime\":\"2016-10-17T15:34:11\",\"paidYn\":\"N\",\"shop\":{\"activeYn\":\"Y\",\"idshop\":1,\"imgUrl\":null,\"phone\":\"9560558201\",\"shopName\":\"Bistro 37\"},\"totalItemCount\":1,\"totalPrice\":130,\"user\":{\"iduser\":2,\"name\":\"Akshansh\",\"phone\":\"9560558203\"}},{\"cancelReason\":null,\"customerOrderId\":261,\"idorder\":261,\"orderCreator\":\"\",\"orderProducts\":[{\"count\":2,\"idorderProduct\":581,\"orderProductAddons\":[],\"product\":{\"addOns\":[],\"expanded\":false,\"longDesc\":\"-\",\"name\":\"Mr Spicy Soyabean\",\"price\":100,\"productId\":5,\"quantity\":1,\"shortDesc\":\"Healthy Soya Chaap Sandwich\",\"stockYn\":\"Y\",\"vegYn\":\"Y\"}}],\"orderState\":\"PREPARING\",\"orderTime\":\"2016-10-19T00:19:39\",\"paidYn\":\"Y\",\"shop\":{\"activeYn\":\"Y\",\"idshop\":1,\"imgUrl\":null,\"phone\":\"9560558201\",\"shopName\":\"Bistro 37\"},\"totalItemCount\":2,\"totalPrice\":200,\"user\":{\"iduser\":2,\"name\":\"Akshansh\",\"phone\":\"9560558203\"}},{\"cancelReason\":null,\"customerOrderId\":262,\"idorder\":262,\"orderCreator\":\"\",\"orderProducts\":[{\"count\":2,\"idorderProduct\":582,\"orderProductAddons\":[],\"product\":{\"addOns\":[],\"expanded\":false,\"longDesc\":\"-\",\"name\":\"Mr Spicy Soyabean\",\"price\":100,\"productId\":5,\"quantity\":1,\"shortDesc\":\"Healthy Soya Chaap Sandwich\",\"stockYn\":\"Y\",\"vegYn\":\"Y\"}}],\"orderState\":\"PREPARING\",\"orderTime\":\"2016-10-19T00:21:25\",\"paidYn\":\"Y\",\"shop\":{\"activeYn\":\"Y\",\"idshop\":1,\"imgUrl\":null,\"phone\":\"9560558201\",\"shopName\":\"Bistro 37\"},\"totalItemCount\":2,\"totalPrice\":200,\"user\":{\"iduser\":54,\"name\":\"akshansh a\",\"phone\":\"9560558213\"}},{\"cancelReason\":null,\"customerOrderId\":263,\"idorder\":263,\"orderCreator\":\"vendor\",\"orderProducts\":[{\"count\":2,\"idorderProduct\":583,\"orderProductAddons\":[],\"product\":{\"addOns\":[],\"expanded\":false,\"longDesc\":\"-\",\"name\":\"Mr Spicy Soyabean\",\"price\":100,\"productId\":5,\"quantity\":1,\"shortDesc\":\"Healthy Soya Chaap Sandwich\",\"stockYn\":\"Y\",\"vegYn\":\"Y\"}}],\"orderState\":\"PREPARING\",\"orderTime\":\"2016-10-19T00:33:18\",\"paidYn\":\"Y\",\"shop\":{\"activeYn\":\"Y\",\"idshop\":1,\"imgUrl\":null,\"phone\":\"9560558201\",\"shopName\":\"Bistro 37\"},\"totalItemCount\":2,\"totalPrice\":200,\"user\":{\"iduser\":54,\"name\":\"akshansh a\",\"phone\":\"9560558213\"}}],\"shopId\":1,\"success\":true}" ;

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();


        FetchOrderTo fetchOrderTo = gson.fromJson(s, FetchOrderTo.class);

        List<OrderVo> list= fetchOrderTo.getOrders();

        return list;
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}

