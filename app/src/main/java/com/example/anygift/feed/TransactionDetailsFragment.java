package com.example.anygift.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardTransaction;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.model.Model;
import com.example.anygift.model.Utils;


public class TransactionDetailsFragment extends Fragment {
    TextView typeTv,numTv,expTv,dealDateTv,sellerTv,buyerTv,costTv,valueAtDealDateTv,reviewTv;
    String tranId;
    CardTransaction transaction;
    Card card;
    ProgressBar pb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_details, container, false);
        tranId = TransactionDetailsFragmentArgs.fromBundle(getArguments()).getTranId();

        pb = view.findViewById(R.id.tran_details_pb);
        pb.setVisibility(View.VISIBLE);

        typeTv = view.findViewById(R.id.trans_details_type_tv);
        numTv = view.findViewById(R.id.trans_details_number_tv);
        expTv = view.findViewById(R.id.trans_details_exp_date_tv);
        dealDateTv = view.findViewById(R.id.trans_details_tran_date_tv);
        sellerTv = view.findViewById(R.id.trans_details_seller_tv);
        buyerTv = view.findViewById(R.id.trans_details_buyer_tv);
        costTv = view.findViewById(R.id.trans_details_cost_tv);
        valueAtDealDateTv = view.findViewById(R.id.trans_details_value_deal_day_tv);
        reviewTv = view.findViewById(R.id.trans_details_review_tv);

        getData();

        return view;
    }

    private void getData() {
        Model.instance.executor.execute(new Runnable() {
            @Override
            public void run() {
                Model.instance.getCardsTransactionByTransactionIdRetrofit(tranId, new Model.cardsTransactionReturnListener() {
                    @Override
                    public void onComplete(CardTransaction cardTransaction, String message) {
                        transaction = new CardTransaction();
                        transaction = cardTransaction;
                        Model.instance.getCardRetrofit(cardTransaction.getCard(), new Model.cardReturnListener() {
                            @Override
                            public void onComplete(Card c, String message) {
                                card=new Card();
                                card=c;
                                setUI();
                            }
                        });
                    }
                });
            }
        });

    }

    private void setUI() {
        for(CardType ct:Model.instance.cardTypes){
            if(ct.getId().equals(transaction.getCardType())){
                typeTv.setText(ct.getName());
                break;
            }
        }
        numTv.setText(card.getCardNumber());
        expTv.setText(Utils.ConvertLongToDate(card.getExpirationDate()));

        dealDateTv.setText(transaction.getDate());
        sellerTv.setText(transaction.getSellerEmail());
        buyerTv.setText(transaction.getBuyerEmail());
        costTv.setText(transaction.getBoughtFor().toString());
        valueAtDealDateTv.setText(transaction.getCardValue().toString());

        //satisfied and review

        pb.setVisibility(View.GONE);
    }
}