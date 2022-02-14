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
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class MyCardsFragment extends Fragment {


    ImageView cardIv;
    View view;
    FeedViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    LiveData<List<GiftCard>> liveData;
    public MyCardsFragment() {
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
        view = inflater.inflate(R.layout.fragment_my_cards, container, false);
        getActivity().setTitle("AnyGift - MyCards");
        swipeRefresh = view.findViewById(R.id.MyGiftCardlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                reloadData();
            }
        });
        adapter = new MyCardsFragment.MyAdapter();
        RecyclerView list = view.findViewById(R.id.MyCards_list_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        MyCardsFragment.MyAdapter adapter = new MyCardsFragment.MyAdapter();
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyCardsFragment .OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                double val = viewModel.getList().getValue().get(position).getValue();
                Log.d("TAG","Gift card in value of: " + val);
                Navigation.findNavController(v).navigate(MyCardsFragmentDirections.actionMyCardsFragmentToCardsDetailsFragment(position));

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
        public MyCardsFragment.OnItemClickListener listener;
        TextView cardValue,cardValTag;
        ImageView cardImage;
        int position;
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
            String gfEmail=viewModel.getList().getValue().get(position).getOwnerEmail();
            String userEmail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
            if (!viewModel.getList().getValue().get(position).getDeleted()&&(gfEmail.compareTo(userEmail)==0)) {
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

    class MyAdapter extends RecyclerView.Adapter<MyCardsFragment.MyViewHolder>{
        MyCardsFragment.OnItemClickListener listener;
       // private LiveData<List<GiftCard>> giftCardList = Model.instance.getAll();
        public void setOnItemClickListener(MyCardsFragment.OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyCardsFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.cards_list_row,parent,false);
            MyCardsFragment.MyViewHolder holder = new MyCardsFragment.MyViewHolder(view);
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
        public void onBindViewHolder(@NonNull MyCardsFragment.MyViewHolder holder, int position) {

            ((MyCardsFragment.MyViewHolder) holder).bindView(position);

        }

        @Override
        public int getItemCount() {
            if(viewModel.getList().getValue()==null)
                return 0;
            return viewModel.getList().getValue().size();
        }
    }
}