package com.example.anygift.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.Retrofit.CardTransaction;
import com.example.anygift.Retrofit.CoinTransaction;
import com.example.anygift.adapters.TransactionsAdapter;
import com.example.anygift.model.Model;

import java.util.ArrayList;
import java.util.List;

public class TransactionsListRVFragment extends Fragment {
    RecyclerView rv;
    TransactionsAdapter adapter;
    List<CoinTransaction> tranList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_transactions_list_rv, container, false);


        tranList = new ArrayList<>();
        for(int i=0;i<10;i++){
            CoinTransaction ct = new CoinTransaction();
            ct.setDate("0/0/00"+i);
            ct.setAmount(i*100.0);
            ct.setFrom("Mister "+i);
            ct.setTo(Double.valueOf(i));
            tranList.add(ct);
        }



        rv = view.findViewById(R.id.transactions_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TransactionsAdapter(tranList);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                double val = tranList.get(position).getAmount();
                String id = tranList.get(position).getFrom();
                Log.d("TAG", "transaction in value of: " + val);
                //Navigation.findNavController(v).navigate(MyWalletFragmentDirections.actionMyCardsFragmentToCardsDetailsFragment(id));
            }
        });

        Model.instance.getCardsTransactionsRetrofit(new Model.cardsTransactionsReturnListener() {
            @Override
            public void onComplete(List<CardTransaction> cardTransaction, String message) {
                System.out.println("######################");
                System.out.println(cardTransaction);
                System.out.println("######################");
            }
        });

        return view;
    }
}