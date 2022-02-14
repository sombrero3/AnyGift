package com.example.anygift;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
import java.util.Timer;
import java.util.TimerTask;


public class FeedFragment extends Fragment {
    ImageView cardIv;
    View view;
    FeedViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    LiveData<List<GiftCard>> liveData;
    public FeedFragment() {
        // Required empty public constructor
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_feed, container, false);
         getActivity().setTitle("AnyGift - Feed");
        swipeRefresh = view.findViewById(R.id.giftCardlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                reloadData();
            }
        });
        adapter = new MyAdapter();
        RecyclerView list = view.findViewById(R.id.cards_list_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        FeedFragment.MyAdapter adapter = new FeedFragment.MyAdapter();
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new FeedFragment.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                double val = viewModel.getList().getValue().get(position).getValue();
                Log.d("TAG","Gift card in vlaue of: " + val);
                 Navigation.findNavController(v).navigate(FeedFragmentDirections.actionFeedFragmentToCardsDetailsFragment(position));

            }
        });
        setHasOptionsMenu(true);
        viewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<GiftCard>>() {
            @Override
            public void onChanged(List<GiftCard> giftCards) {
                adapter.notifyDataSetChanged();
            }
        });
        reloadData();
        return view;

    }

    void reloadData() {
        Model.instance.refreshGiftCardsList(new Model.GetAllGiftCardListener() {
            @Override
            public void onComplete() {
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public OnItemClickListener listener;
        TextView cardValue,cardValTag;
        ImageView cardImage;
        int position;
        private LiveData<List<GiftCard>> giftCardList = Model.instance.getAll();

            public MyViewHolder(View itemView) {
            super(itemView);
            cardValue = itemView.findViewById(R.id.cards_list_row_et_value);
            cardImage = itemView.findViewById(R.id.cards_list_row_iv);
            cardValTag=itemView.findViewById(R.id.listRow_tv_value);
                itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick( v,position);
                }
            });
        }
            public void bindView (int position){
                if (!viewModel.getList().getValue().get(position).getDeleted()) {
                    cardValue.setText(String.valueOf(viewModel.getList().getValue().get(position).getValue()));
                    // Picasso.get().load(viewModel.getList().getValue().get(position).getImageUrl()).into(cardImage);
                    this.position = position;
                } else {
                    cardValue.setVisibility(View.GONE);
                    cardImage.setVisibility(View.GONE);
                    cardValTag.setVisibility(View.GONE);
                }
            }
        }

    interface OnItemClickListener{

         void onItemClick(View v, int position);
    }

    class MyAdapter extends RecyclerView.Adapter<FeedFragment.MyViewHolder>{
        FeedFragment.OnItemClickListener listener;
        private LiveData<List<GiftCard>> giftCardList = Model.instance.getAll();
        public void setOnItemClickListener(FeedFragment.OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public FeedFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.cards_list_row,parent,false);
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
            if(viewModel.getList().getValue()==null)
                return 0;
            return viewModel.getList().getValue().size();
        }
    }
}