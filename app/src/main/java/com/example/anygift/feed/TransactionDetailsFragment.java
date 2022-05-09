package com.example.anygift.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anygift.R;
import com.example.anygift.model.Model;


public class TransactionDetailsFragment extends Fragment {
    TextView typeTv,numTv,expTv,dealDateTv,sellerTv,buyerTv,costTv,valueAtDealDateTv,reviewTv;
    String tranId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_details, container, false);
        tranId = TransactionDetailsFragmentArgs.fromBundle(getArguments()).getTranId();

        typeTv = view.findViewById(R.id.trans_details_type_tv);
        numTv = view.findViewById(R.id.trans_details_number_tv);
        expTv = view.findViewById(R.id.trans_details_exp_date_tv);
        dealDateTv = view.findViewById(R.id.trans_details_tran_date_tv);
        sellerTv = view.findViewById(R.id.trans_details_seller_tv);
        buyerTv = view.findViewById(R.id.trans_details_buyer_tv);
        costTv = view.findViewById(R.id.trans_details_cost_tv);
        valueAtDealDateTv = view.findViewById(R.id.trans_details_value_deal_day_tv);
        reviewTv = view.findViewById(R.id.trans_details_review_tv);

        setUI();

        return view;
    }

    private void setUI() {
        //getTranById
        //getCardById
    }
}