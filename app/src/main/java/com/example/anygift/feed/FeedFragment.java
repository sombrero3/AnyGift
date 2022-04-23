package com.example.anygift.feed;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anygift.R;
import com.example.anygift.adapters.CardsListAdapter;
import com.example.anygift.model.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class FeedFragment extends Fragment {
    FeedViewModel viewModel;
    CardsListAdapter  dreamCardAdapter, shufersalAdapter,mostRecAdapter;
    SwipeRefreshLayout swipeRefresh;
    TextView nameTv,coinsTv;
    FloatingActionButton searchFab;
    RecyclerView dreamCardList,mostRecList,shufersalList;
    View v;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
       // getActivity().setTitle("AnyGift - Feed");
        v= view;
        swipeRefresh = view.findViewById(R.id.giftCardlist_swiperefresh);
//        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshGiftCardsList());
        dreamCardList = v.findViewById(R.id.feed_dream_cards_rv);
        mostRecList = v.findViewById(R.id.cards_list_rv);
        shufersalList = v.findViewById(R.id.feed_shufersal_cards_rv);
        nameTv = view.findViewById(R.id.cards_list_user_name_tv);
        coinsTv = view.findViewById(R.id.feed_coins_tv);
        nameTv.setText("Hello " + Model.instance.getSignedUser().getFirstName() +" and welcome to the gift card trading platform. Find every gift card buy or trade with your own cards.");


        coinsTv.setText(Model.instance.getSignedUser().getCoins().toString());
        searchFab = view.findViewById(R.id.feed_search_fab);
        setDynamicRvs();
        setHasOptionsMenu(true);
        //viewModel.getList().observe(getViewLifecycleOwner(), list1 -> refresh());
        swipeRefresh.setRefreshing(Model.instance.getListLoadingState().getValue() == Model.GiftListLoadingState.loading);
        Model.instance.getListLoadingState().observe(getViewLifecycleOwner(), ListLoadingState -> {
            if (ListLoadingState == Model.GiftListLoadingState.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }

        });

        searchFab.setOnClickListener((v)-> Navigation.findNavController(v).navigate(R.id.action_global_searchGiftCardFragment));


        return view;

    }

    private void setMostRecRv() {
        //--Most Recommended RV------//

        mostRecList.setHasFixedSize(true);
        RecyclerView.LayoutManager mostRecLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        mostRecList.setLayoutManager(mostRecLayout);
        mostRecAdapter = new CardsListAdapter(viewModel.getMostRecList());
        mostRecList.setAdapter(mostRecAdapter);

        mostRecAdapter.setOnItemClickListener(new com.example.anygift.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                double val = viewModel.getMostRecList().get(position).getValue();
                String id = viewModel.getMostRecList().get(position).getId();
                Log.d("TAG", "Gift card in value of: " + val);
                Navigation.findNavController(v).navigate(FeedFragmentDirections.actionFeedFragmentToCardsDetailsFragment(id));
            }
        });

    }

    private void setDynamicRvs() {
        viewModel.refreshMap(new Model.VoidListener() {
            @Override
            public void onComplete() {
                setMostRecRv();
                //--dreamCard RV--//

                dreamCardList.setHasFixedSize(true);
                RecyclerView.LayoutManager dreamCardLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                dreamCardList.setLayoutManager(dreamCardLayout);
                dreamCardAdapter = new CardsListAdapter(viewModel.getDreamCardsList());
                dreamCardList.setAdapter(dreamCardAdapter);

                dreamCardAdapter.setOnItemClickListener(new com.example.anygift.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        double val = viewModel.getDreamCardsList().get(position).getValue();
                        String id = viewModel.getDreamCardsList().get(position).getId();
                        Log.d("TAG", "Gift card in value of: " + val);
                        Navigation.findNavController(v).navigate(FeedFragmentDirections.actionFeedFragmentToCardsDetailsFragment(id));
                    }
                });

                //---Shufersal RV---//

                shufersalList.setHasFixedSize(true);
                RecyclerView.LayoutManager shufersalLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                shufersalList.setLayoutManager(shufersalLayout);
                shufersalAdapter = new CardsListAdapter(viewModel.getShufersalList());
                shufersalList.setAdapter(shufersalAdapter);

                shufersalAdapter.setOnItemClickListener(new com.example.anygift.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        double val = viewModel.getShufersalList().get(position).getValue();
                        String id = viewModel.getShufersalList().get(position).getId();
                        Log.d("TAG", "Gift card in value of: " + val);
                        Navigation.findNavController(v).navigate(FeedFragmentDirections.actionFeedFragmentToCardsDetailsFragment(id));
                    }
                });
            }
        });
    }

    private void refresh() {
        mostRecAdapter.notifyDataSetChanged();
    }

}