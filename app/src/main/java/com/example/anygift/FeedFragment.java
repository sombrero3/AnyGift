package com.example.anygift;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;

import java.util.List;


public class FeedFragment extends Fragment {
    List<GiftCard> cards;
    EditText cardEt;
    ImageView cardIv;
    View view;
    FeedViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_feed, container, false);
        swipeRefresh = view.findViewById(R.id.giftCardlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshGiftCardsList());
        cards= Model.instance.getAll().getValue();
        adapter = new MyAdapter();
        RecyclerView list = view.findViewById(R.id.cards_list_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        FeedFragment.MyAdapter adapter = new FeedFragment.MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new FeedFragment.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String cardName = cards.get(position).getCardName();
                Log.d("TAG","user's row clicked: " + cardName);
                // Navigation.findNavController(v).navigate(StudentListRvFragmentDirections.actionStudentListRvFragmentToStudentDetailsFragment(stId));

            }
        });
        setHasOptionsMenu(true);
        viewModel.getList().observe(getViewLifecycleOwner(), list1 -> refresh());
        /*swipeRefresh.setRefreshing(Model.instance.getStudentListLoadingState().getValue() == Model.StudentListLoadingState.loading);
        Model.instance.getStudentListLoadingState().observe(getViewLifecycleOwner(), studentListLoadingState -> {
            if (studentListLoadingState == Model.StudentListLoadingState.loading){
                swipeRefresh.setRefreshing(true);
            }else{
                swipeRefresh.setRefreshing(false);
            }

        });

         */
        return view;

    }
    private void refresh() {
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView cardValue;
        ImageView cardImage;


        public MyViewHolder(@NonNull View itemView, FeedFragment.OnItemClickListener listener) {
            super(itemView);
            cardValue = itemView.findViewById(R.id.cards_list_row_et_value);
            cardImage = itemView.findViewById(R.id.cards_list_row_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(v,pos);
                }
            });

        }
    }
    interface OnItemClickListener{
        void onItemClick(View v,int position);
    }
    class MyAdapter extends RecyclerView.Adapter<FeedFragment.MyViewHolder>{
        FeedFragment.OnItemClickListener listener;
        public void setOnItemClickListener(FeedFragment.OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public FeedFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.cards_list_row,parent,false);
            FeedFragment.MyViewHolder holder = new FeedFragment.MyViewHolder(view,listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull FeedFragment.MyViewHolder holder, int position) {
            GiftCard card = cards.get(position);
            holder.cardValue.setText(String.valueOf(card.getValue()));


        }

        @Override
        public int getItemCount() {
            if(cards==null)
                return 0;
            return cards.size();
        }
    }
}