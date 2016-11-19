package com.example.ssairam.hopline.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssairam.hopline.IncommingOrderBackgroudRefresh;
import com.example.ssairam.hopline.DataStore;
import com.example.ssairam.hopline.InitialiseDataFromServer;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.ServerHelper;
import com.example.ssairam.hopline.Util;
import com.example.ssairam.hopline.adapters.IncomingOrdersAdapter;
import com.example.ssairam.hopline.vo.OrderVo;

import java.util.ArrayList;
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
    private IncomingOrdersAdapter adapter;
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
        View layout = inflater.inflate(R.layout.fragment_incoming_order, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);


        if (!DataStore.isDataInilitised()) {
            new InitialiseDataFromServer(this.getActivity()) {
                @Override
                protected void onPostExecute(Boolean success) {
                    if (success) {
                        initUi();
                    } else {
                        Toast.makeText(activity, "Error communicating with server!", Toast.LENGTH_LONG).show();
                    }

                    super.onPostExecute(success);
                }
            }.execute("");
        } else {
            initUi();
        }

        return layout;
    }

    private void initUi() {
        List<OrderVo> orderVoList = DataStore.getIncomingOrders();
        View.OnClickListener callListner, cancelListner, confirmListner;
        callListner = new CallOnclickListner();
        cancelListner = new CancelOnClickListner();
        confirmListner = new ConfirmOnClickListner();
        adapter = new IncomingOrdersAdapter(this.getActivity().getApplicationContext(), orderVoList, callListner, cancelListner, confirmListner);

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(4,1);
        recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
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


    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
                mMessageReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver, new IntentFilter("newIncomingOrder"));
        updateUi();

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("ManAct", "New orders!!!!!!!!!");
            updateUi();
        }
    };

    private void updateUi() {
        if (adapter != null) adapter.updateData(DataStore.getIncomingOrders());
    }

    private class CallOnclickListner implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            int position = (Integer) v.getTag();
            OrderVo order = adapter.getOrders().get(position);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            CallDialogFragment newFragment = new CallDialogFragment();
            newFragment.setOrderVo(order);
            newFragment.setView(v);
            newFragment.setListners(new CancelOnClickListner(),new ConfirmOnClickListner());
            newFragment.show(fragmentManager, "dialog");

        }
    }

    private class ConfirmOnClickListner implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            int position = (Integer) v.getTag();
            OrderVo order = adapter.getOrders().get(position);
            new MarkOrderPreparing(order.getIdorder()).execute("");


        }
    }

    private class CancelOnClickListner implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            int position = (Integer) v.getTag();
            OrderVo order = adapter.getOrders().get(position);

            new CancelOrder(order.getIdorder(),"reason for cancel").execute("");


        }
    }



    public static class CallDialogFragment extends DialogFragment {
        OrderVo orderVo;
        View view;
        private View.OnClickListener cancelListner,confirmListner;

        public void setOrderVo(OrderVo orderVo) {
            this.orderVo = orderVo;
        }

        public void setView(View view){
            this.view = view;
        }

        public void setListners(View.OnClickListener cancelListner, View.OnClickListener confirmListner){
            this.cancelListner = cancelListner;
            this.confirmListner = confirmListner;
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            final View layout = inflater.inflate(R.layout.call_dialog,null);
            ((TextView) layout.findViewById(R.id.phone_numer)).setText(orderVo.getUser().getPhone());
            ((TextView) layout.findViewById(R.id.user_name)).setText(orderVo.getUser().getName());
            ((TextView) layout.findViewById(R.id.total_price)).setText(orderVo.getTotalPrice() + "");
            ((TextView) layout.findViewById(R.id.total_quantity)).setText(orderVo.getTotalItemCount() + "");

//            String[] values = new String[] { "Android List View",
//                    "Adapter implementation",
//                    "Simple List View In Android",
//                    "Create List View Android",
//                    "Android Example",
//                    "List View Source Code",
//                    "List View Array Adapter",
//                    "Android Example List View"
//            };
//            ListView listView=(ListView)layout.findViewById(R.id.dialog_listview);
//            listView.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.order_product_item,R.id.order_item_name,values ));

            builder.setView(layout)
                    .setPositiveButton("Confirm Order", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            confirmListner.onClick(view);
                        }
                    })
                    .setNegativeButton("Cancel Order", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            cancelListner.onClick(view);
                        }
                    }).setNeutralButton("Hide Dialog", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            return builder.create();
        }

    }



    private class CancelOrder extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        Integer orderId;
        String cancelReason;

        CancelOrder(Integer orderId, String cancelReason) {
            this.orderId = orderId;
            this.cancelReason = cancelReason;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return ServerHelper.markOrderCancel(orderId, cancelReason) ;
        }

        @Override
        protected void onPostExecute(Boolean success) {


            if (success) {
                Util.synchronizedRemoveFromIncommingOrders(orderId);
                updateUi();

                Toast.makeText(getActivity(), "Order cancelled!!", Toast.LENGTH_SHORT).show();
                //TODO : update conromation order ui.
            } else {
                Toast.makeText(getActivity(), "Error communicating with server!", Toast.LENGTH_LONG).show();
            }


            if (dialog != null)
                dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            dialog = Util.showProgressDialog(getActivity());


        }
    }



    private class MarkOrderPreparing extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        Integer orderId;

        MarkOrderPreparing(Integer orderId) {
            this.orderId = orderId;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean success = ServerHelper.markOrderPreparing(orderId);

            if (success) {

                try {
                    DataStore.setPreparingOrders(ServerHelper.retrievePreparingOrders());
                } catch (Exception e) {
                    e.printStackTrace();
                    DataStore.setPreparingOrders(null);
                    return  false;
                }
            }

            return success;

        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {
                Util.synchronizedRemoveFromIncommingOrders(orderId);
                updateUi();
                Toast.makeText(getActivity(), "Order Moved to preparing!!", Toast.LENGTH_SHORT).show();
                //TODO : update conromation order ui.
            } else {
                Toast.makeText(getActivity(), "Error communicating with server!!", Toast.LENGTH_SHORT).show();
            }

            if (dialog != null)
                dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            dialog = Util.showProgressDialog(getActivity());


        }
    }








}

