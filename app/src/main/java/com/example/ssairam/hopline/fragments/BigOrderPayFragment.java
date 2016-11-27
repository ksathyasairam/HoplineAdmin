package com.example.ssairam.hopline.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ssairam.hopline.DataStore;
import com.example.ssairam.hopline.PrinterHelper;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.ServerHelper;
import com.example.ssairam.hopline.Util;
import com.example.ssairam.hopline.adapters.BigOrderPayAdapter;
import com.example.ssairam.hopline.vo.OrderVo;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BigOrderPayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BigOrderPayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BigOrderPayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private BigOrderPayAdapter adapter;

    public BigOrderPayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BigOrderPayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BigOrderPayFragment newInstance(String param1, String param2) {
        BigOrderPayFragment fragment = new BigOrderPayFragment();
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

        new LoadBigOrderPayData(getActivity()).execute("");

        return layout;
    }

    private void initUi(List<OrderVo> orders) {
        adapter = createOrderReadyAdatper(orders);

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(5,1);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private BigOrderPayAdapter createOrderReadyAdatper(List<OrderVo> orders) {
        return new BigOrderPayAdapter(this.getActivity().getApplicationContext(), orders, new View.OnClickListener() {
            @Override
            public void onClick(View v) { //print bill
                int position = (Integer) v.getTag();
                OrderVo order = adapter.getData().get(position);

                if (PrinterHelper.get().canConnectToPrinter()){
                    markOrderPreparingAndPaid(order);
                } else {
                    Toast.makeText(getActivity(), "Printer connection FAILED! MAKE SURE BLUETOOTH IS TURNED ON AND CONNECTED TO PRINTER", Toast.LENGTH_LONG).show();
                }

            }


        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) { //remove order
                int position = (Integer) v.getTag();
                OrderVo order = adapter.getData().get(position);
                new RemoveOrder(order.getIdorder()).execute("");
            }
        });
    }


    private void markOrderPreparingAndPaid(OrderVo order) {
        new MarkOrderPreparingAndPaid(order).execute("");
    }


    private class MarkOrderPreparingAndPaid extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        OrderVo order;

        MarkOrderPreparingAndPaid(OrderVo order) {
            this.order = order;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean success = ServerHelper.markOrderPreparingAndPaidAndUdpateDate(order.getIdorder());

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
               removeOrderFromUi(order.getIdorder());
               Util.printBill(order,getActivity());
               Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
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


    private class RemoveOrder extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        Integer orderId;

        RemoveOrder(Integer orderId) {
            this.orderId = orderId;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean success = ServerHelper.markOrderCancel(orderId,"Unattended Big order pay.");
            return success;
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {
                removeOrderFromUi(orderId);
                Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
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

    private void removeOrderFromUi(Integer orderId) {
        OrderVo deletedOrder = new OrderVo();
        deletedOrder.setIdorder(orderId);
        adapter.getData().remove(deletedOrder);
        adapter.notifyDataSetChanged();
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
        if (context instanceof IncomingOrderFragment.OnFragmentInteractionListener) {
            mListener = (BigOrderPayFragment.OnFragmentInteractionListener) context;
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


    public class LoadBigOrderPayData extends AsyncTask<String, Void, Boolean> {
        public final Activity activity;
        ProgressDialog dialog;
        List<OrderVo> orders;

        public LoadBigOrderPayData(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                orders = ServerHelper.retrieveBigOrderPayOrders();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {
                initUi(orders);
            } else {
                Toast.makeText(activity, "Error loading data!!", Toast.LENGTH_LONG).show();
            }

            if (dialog != null)
                dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            dialog = Util.createProgressDialog(activity);
            dialog.show();
        }
    }


}
