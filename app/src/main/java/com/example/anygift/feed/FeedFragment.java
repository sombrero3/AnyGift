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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.adapters.CardsListAdapter;
import com.example.anygift.model.Model;
import com.example.anygift.view_holders.CardsListViewHolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class FeedFragment extends Fragment {
    FeedViewModel viewModel;
    CardsListAdapter  dreamCardAdapter, shufersalAdapter;
    MyAdapter mostRecAdapter;
    SwipeRefreshLayout swipeRefresh;
    TextView nameTv;
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
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshGiftCardsList());

        nameTv = view.findViewById(R.id.cards_list_user_name_tv);
        nameTv.setText("Hello " + Model.instance.getSignedUser().getFirstName() +" and welcome to the gift card trading platform. Find every gift card buy or trade with your own cards.");
        searchFab = view.findViewById(R.id.feed_search_fab);

        setRvs();

        setHasOptionsMenu(true);
        viewModel.getList().observe(getViewLifecycleOwner(), list1 -> refresh());
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

    private void setRvs() {
        //--Most Recommended RV------//
        mostRecList = v.findViewById(R.id.cards_list_rv);
        mostRecList.setHasFixedSize(true);
        RecyclerView.LayoutManager mostRecLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        mostRecList.setLayoutManager(mostRecLayout);
        mostRecAdapter = new MyAdapter();
        mostRecList.setAdapter(mostRecAdapter);

        mostRecAdapter.setOnItemClickListener(new FeedFragment.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                double val = viewModel.getList().getValue().get(position).getValue();
                String id = viewModel.getList().getValue().get(position).getId();
                Log.d("TAG", "Gift card in value of: " + val);
                Navigation.findNavController(v).navigate(FeedFragmentDirections.actionFeedFragmentToCardsDetailsFragment(id));
            }
        });
        setDynamicRvs();



    }

    private void setDynamicRvs() {
        viewModel.refreshMap(new Model.VoidListener() {
            @Override
            public void onComplete() {
                //--dreamCard RV--//
                dreamCardList = v.findViewById(R.id.feed_dream_cards_rv);
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
                shufersalList = v.findViewById(R.id.feed_shufersal_cards_rv);
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

    class MyViewHolder extends RecyclerView.ViewHolder {
        public OnItemClickListener listener;
        TextView cardValue, cardValTag;
        ImageView cardImage;
        int position;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardValue = itemView.findViewById(R.id.cards_list_row_amount_in_card_tv);
            cardImage = itemView.findViewById(R.id.cards_list_row_iv);
            cardValTag = itemView.findViewById(R.id.listRow_tv_value);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(v, position);
                }
            });
        }

        public void bindView(int position) {
            if (!Objects.requireNonNull(viewModel.getList().getValue()).get(position).getDeleted()) {
                cardValue.setText(String.valueOf(viewModel.getList().getValue().get(position).getValue()));
                Picasso.get().load(viewModel.getList().getValue().get(position).getGiftCardImageUrl()).into(cardImage);
                this.position = position;
            } else {
                cardValue.setVisibility(View.GONE);
                cardImage.setVisibility(View.GONE);
                cardValTag.setVisibility(View.GONE);
            }
        }
    }

    interface OnItemClickListener {

        void onItemClick(View v, int position);
    }

    class MyAdapter extends RecyclerView.Adapter<FeedFragment.MyViewHolder> {
        OnItemClickListener listener;

        public void setOnItemClickListener(FeedFragment.OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public FeedFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.cards_list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            holder.listener = listener;

            /* use progress bar
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    pb.setVisibility(View.INVISIBLE);
                }
            }, 2500);*/

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull FeedFragment.MyViewHolder holder, int position) {

            ((MyViewHolder) holder).bindView(position);

        }

        @Override
        public int getItemCount() {
            if (viewModel.getList().getValue() == null)
                return 0;
            return viewModel.getList().getValue().size();
        }
    }
}