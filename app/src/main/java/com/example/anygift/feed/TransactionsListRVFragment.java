package com.example.anygift.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.Retrofit.CardTransaction;
import com.example.anygift.Retrofit.CoinTransaction;
import com.example.anygift.adapters.TransactionsAdapter;
import com.example.anygift.model.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TransactionsListRVFragment extends Fragment {
    RecyclerView rv;
    TransactionsAdapter adapter;
    List<CardTransaction> tranList;
    ProgressBar pb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_transactions_list_rv, container, false);
        pb = view.findViewById(R.id.tran_list_pb);
        pb.setVisibility(View.VISIBLE);

        //createLocalList();

        rv = view.findViewById(R.id.transactions_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        Model.instance.getCardsTransactionsRetrofit(new Model.cardsTransactionsReturnListener() {
            @Override
            public void onComplete(List<CardTransaction> cardTransaction, String message) {
                tranList = new ArrayList<>();
                tranList.addAll(cardTransaction);
                adapter = new TransactionsAdapter(tranList);
                rv.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Navigation.findNavController(v).navigate(TransactionsListRVFragmentDirections.actionTransactionsFragmentToTransactionDetailsFragment(tranList.get(position).getId()));
                    }
                });
                pb.setVisibility(View.GONE);
            }
        });


        return view;
    }

    private void createLocalList() {
        tranList = new ArrayList<>();
        for(int i=0;i<10;i++){
            CardTransaction ct = new CardTransaction();
            ct.setDate("0/0/00"+i);
            ct.setBoughtFor(i*100.0);

            ct.setCardType(Model.instance.cardTypes.get(0).getId());
            tranList.add(ct);

            Random x = new Random();
            if(x.nextInt()%2==0){
                ct.setBuyer(Model.instance.getSignedUser().getId());
                ct.setSeller("");
                ct.setSellerEmail("Mister "+i);
            }else {
                ct.setSeller(Model.instance.getSignedUser().getId());
                ct.setBuyer("");
                ct.setBuyerEmail("Mister "+i);
            }
        }
    }
}