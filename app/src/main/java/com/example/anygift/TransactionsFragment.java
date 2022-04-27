package com.example.anygift;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anygift.Retrofit.CoinTransaction;
import com.example.anygift.adapters.TransactionsAdapter;

import java.util.List;

public class TransactionsFragment extends Fragment {
    RecyclerView rv;
    TransactionsAdapter adapter;
    List<CoinTransaction> tranList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_transactions, container, false);

        RecyclerView cardsList = view.findViewById(R.id.transactions_rv);
        cardsList.setHasFixedSize(true);
        cardsList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TransactionsAdapter(tranList);
        cardsList.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                double val = tranList.get(position).getAmount();
                String id = tranList.get(position).getFrom();
                Log.d("TAG", "transaction in value of: " + val);
                //Navigation.findNavController(v).navigate(MyWalletFragmentDirections.actionMyCardsFragmentToCardsDetailsFragment(id));
            }
        });


        return view;
    }
}