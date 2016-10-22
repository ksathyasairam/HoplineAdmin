package com.example.ssairam.hopline.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ssairam.hopline.DataStore;
import com.example.ssairam.hopline.InitialiseDataFromServer;
import com.example.ssairam.hopline.R;
import com.example.ssairam.hopline.adapters.OrdersAdapter;
import com.example.ssairam.hopline.vo.OrderVo;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderReadyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderReadyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderReadyFragment extends Fragment {
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private IncomingOrderFragment.OnFragmentInteractionListener mListener;

    public OrderReadyFragment() {
        // Required  public constructor
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
        List<OrderVo> orderVoList = DataStore.getReadyOrders();
        adapter = new OrdersAdapter(this.getActivity().getApplicationContext(), orderVoList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
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
        if (context instanceof IncomingOrderFragment.OnFragmentInteractionListener) {
            mListener = (IncomingOrderFragment.OnFragmentInteractionListener) context;
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

}
