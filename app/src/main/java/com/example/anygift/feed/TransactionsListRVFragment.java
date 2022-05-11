package com.example.anygift.feed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    SwipeRefreshLayout swipeRefresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_transactions_list_rv, container, false);
        pb = view.findViewById(R.id.tran_list_pb);
        pb.setVisibility(View.VISIBLE);
        swipeRefresh = view.findViewById(R.id.tran_rv_swiperefresh);
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

        swipeRefresh.setOnRefreshListener(() -> setTranRv());
        return view;
    }
    private void setTranRv() {
        Model.instance.getCardsTransactionsRetrofit(new Model.cardsTransactionsReturnListener() {
            @Override
            public void onComplete(List<CardTransaction> cardTransaction, String message) {
                tranList.clear();
                tranList.addAll(cardTransaction);
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });
    }
}