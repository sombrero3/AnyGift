package com.example.anygift.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anygift.R;


public class TransactionDetailsFragment extends Fragment {
    TextView typeTv,numTv,expTv,dealDateTv,sellerTv,buyerTv,costTv,valueAtDealDateTv,reviewTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_details, container, false);
    }
}