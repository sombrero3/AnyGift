package com.example.anygift.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardTransaction;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.model.Model;
import com.example.anygift.model.Utils;


public class TransactionDetailsFragment extends Fragment {
    TextView typeTv,numTv,expTv,dealDateTv,sellerTv,sellerTitleTv,buyerTitleTv,buyerTv,costTv,valueAtDealDateTv,reviewTv,noFeedbackTv;
    EditText reviewEt;
    String tranId;
    CardTransaction transaction;
    ImageView likeIv,unlikeIv,likeClickedIv,unlikeClickedIv;
    Card card;
    ProgressBar pb;
    Button addFeedbackBtn;
    boolean likeFlag=true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_details, container, false);
        tranId = TransactionDetailsFragmentArgs.fromBundle(getArguments()).getTranId();
        getUIReferences(view);
        getData();

        return view;
    }
    private void getData() {
        Model.instance.getCardsTransactionByTransactionIdRetrofit(tranId, new Model.cardsTransactionReturnListener() {
            @Override
            public void onComplete(CardTransaction cardTransaction, String message) {
                transaction = new CardTransaction();
                transaction = cardTransaction;
                Model.instance.getCardRetrofit(transaction.getCard(), new Model.cardReturnListener() {
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

    private void setUI() {
        //card details
        for(CardType ct:Model.instance.cardTypes){
            if(ct.getId().equals(transaction.getCardType())){
                typeTv.setText(ct.getName());
                break;
            }
        }
        numTv.setText(card.getCardNumber());
        expTv.setText(Utils.ConvertLongToDate(card.getExpirationDate()));

        //transaction details
        costTv.setText(transaction.getBoughtFor().toString());
        valueAtDealDateTv.setText(transaction.getCardValue().toString());
        dealDateTv.setText(transaction.getDate().split("T")[0]);
        unlikeClickedIv.setVisibility(View.GONE);
        //satisfied and review, seller and buyer titles
        if(transaction.getSeller().equals(Model.instance.getSignedUser().getId())){
            //I'm the seller
            buyerTv.setText(transaction.getBuyerEmail());
            sellerTv.setVisibility(View.GONE);
            sellerTitleTv.setVisibility(View.INVISIBLE);
            addFeedbackBtn.setEnabled(false);

            if(transaction.getSatisfied()==null){
                noFeedbackTv.setVisibility(View.VISIBLE);
            }else{
                reviewTv.setVisibility(View.VISIBLE);
                reviewTv.setText(transaction.getBuyerComment());
                if(transaction.getSatisfied()){
                    likeIv.setVisibility(View.VISIBLE);
                }else{
                    unlikeIv.setVisibility(View.VISIBLE);
                }
            }

        }else{
            //I'm the buyer
            sellerTv.setText(transaction.getSellerEmail());
            buyerTv.setVisibility(View.GONE);
            buyerTitleTv.setVisibility(View.GONE);

            if(transaction.getSatisfied()==null){
                likeIv.setVisibility(View.VISIBLE);
                likeClickedIv.setVisibility(View.VISIBLE);
                unlikeIv.setVisibility(View.VISIBLE);
                reviewEt.setVisibility(View.VISIBLE);
                addFeedbackBtn.setOnClickListener(view -> postFeedback());
                setLikeSystem();

            }else{
                reviewTv.setVisibility(View.VISIBLE);
                reviewTv.setText(transaction.getBuyerComment());
                if(transaction.getSatisfied()){
                    likeIv.setVisibility(View.VISIBLE);
                }else{
                    unlikeIv.setVisibility(View.VISIBLE);
                }
            }
        }
        pb.setVisibility(View.GONE);
    }

    private void setLikeSystem() {
        likeIv.setOnClickListener(v->{
            if(!likeFlag){
                likeClickedIv.setVisibility(View.VISIBLE);
                unlikeClickedIv.setVisibility(View.GONE);
                likeFlag=true;
            }
        });
        unlikeIv.setOnClickListener(v->{
            if(likeFlag){
                unlikeClickedIv.setVisibility(View.VISIBLE);
                likeClickedIv.setVisibility(View.GONE);
                likeFlag=false;
            }
        });
    }

    private void postFeedback() {
        //addFeedbackBtn
        if(reviewEt.getText().toString().isEmpty()){
            reviewEt.setError("Feedback is required");
            reviewEt.requestFocus();
        }else{
            //push transaction feedback here
            pb.setVisibility(View.VISIBLE);
            Model.instance.addReview(tranId, likeFlag, reviewEt.getText().toString(), new Model.cardTransactionReturnListener() {
                @Override
                public void onComplete(CardTransaction cardTransaction, String message) {
                    Navigation.findNavController(reviewEt).navigateUp();
                }
            });
        }
    }

    private void getUIReferences(View view) {
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
        reviewEt = view.findViewById(R.id.trans_details_review_et);
        noFeedbackTv = view.findViewById(R.id.trans_details_nofeedback_tv);
        likeIv = view.findViewById(R.id.trans_details_like_iv);
        unlikeIv = view.findViewById(R.id.trans_details_unlike_iv);
        likeClickedIv = view.findViewById(R.id.trans_details_vi_like_iv);
        unlikeClickedIv = view.findViewById(R.id.trans_details_vi_unlike_iv);
        addFeedbackBtn = view.findViewById(R.id.tran_details_add_feedback_btn);
        sellerTitleTv = view.findViewById(R.id.tran_details_seller_title_tv);
        buyerTitleTv = view.findViewById(R.id.tran_details_buyer_title_tv);
    }
}