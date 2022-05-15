package com.example.anygift.feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardTransaction;
import com.example.anygift.Retrofit.Income;
import com.example.anygift.Retrofit.Outcome;
import com.example.anygift.Retrofit.SellerRatings;
import com.example.anygift.Retrofit.User;
import com.example.anygift.adapters.CardsListAdapter;
import com.example.anygift.model.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

public class MyWalletFragment extends Fragment {
    View view;
    ProgressBar pb;
    MyWalletViewModel viewModel;
    CardsListAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    TextView userName, userEmail, userPhone, coins, userAddress, numOfSold, numOfBought, soldInCoins, boughtInCoins,numLikeTv,numUnlikeTv;
    ImageView userImage, editIv, addCardIv,verifiedIv;
    FloatingActionButton searchFab;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(MyWalletViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_cards, container, false);
        //getActivity().setTitle("AnyGift - MyCards");
        swipeRefresh = view.findViewById(R.id.my_cards_swiperefresh);

        pb = view.findViewById(R.id.my_cards_progressbar);
        pb.setVisibility(View.VISIBLE);

//        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshGiftCardsList());
        RecyclerView cardsList = view.findViewById(R.id.MyCards_list_rv);
        cardsList.setHasFixedSize(true);
        RecyclerView.LayoutManager horizontalLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        cardsList.setLayoutManager(horizontalLayout);

        viewModel.getListWithListener(new Model.CardsListListener() {
            @Override
            public void onComplete(List<Card> cards) {
                adapter = new CardsListAdapter(cards);
                cardsList.setAdapter(adapter);
                pb.setVisibility(View.GONE);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        double val = viewModel.getList().get(position).getValue();
                        String id = viewModel.getList().get(position).getId();
                        Log.d("TAG", "Gift card in value of: " + val);
                        Navigation.findNavController(v).navigate(MyWalletFragmentDirections.actionMyCardsFragmentToCardsDetailsFragment(id));
                    }
                });
            }
        });

        userName = view.findViewById(R.id.my_cards_user_name_tv);
        userImage = view.findViewById(R.id.my_cards_avater_iv);
        userEmail = view.findViewById(R.id.my_cards_email_tv);
        userPhone = view.findViewById(R.id.my_cards_phone_tv);
        coins = view.findViewById(R.id.my_cards_coins_tv);
        userAddress = view.findViewById(R.id.my_cards_address_tv);
        numOfSold = view.findViewById(R.id.my_cards_sold_counter_tv);
        numOfBought = view.findViewById(R.id.my_cards_bought_counter_tv);
        soldInCoins = view.findViewById(R.id.my_cards_sold_in_coins_tv);
        boughtInCoins = view.findViewById(R.id.my_cards_bought_in_coins_tv);
        editIv = view.findViewById(R.id.my_cards_edit_iv);
        addCardIv = view.findViewById(R.id.my_cards_add_card_iv);
        searchFab = view.findViewById(R.id.my_cards_search_fab);
        numLikeTv = view.findViewById(R.id.my_cards_num_like_tv);
        numUnlikeTv = view.findViewById(R.id.my_cards_num_un_like_tv);
        verifiedIv = view.findViewById(R.id.my_cards_verified_iv);


        editIv.setOnClickListener(v -> Navigation.findNavController(v).navigate(MyWalletFragmentDirections.actionMyCardsFragmentToEditProfileFragment()));
        addCardIv.setOnClickListener(v -> Navigation.findNavController(v).navigate(MyWalletFragmentDirections.actionGlobalAddCardFragment()));

        setUserUI();
        setHasOptionsMenu(true);

        searchFab.setOnClickListener((v)-> Navigation.findNavController(v).navigate(R.id.action_global_searchGiftCardFragment));

        swipeRefresh.setRefreshing(Model.instance.getListLoadingState().getValue() == Model.GiftListLoadingState.loading);
        Model.instance.getListLoadingState().observe(getViewLifecycleOwner(), ListLoadingState -> {
            if (ListLoadingState == Model.GiftListLoadingState.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }

        });
        return view;

    }

    private void setUserUI() {
        com.example.anygift.Retrofit.User user = Model.instance.getSignedUser();
        System.out.println(user);

        userName.setText(user.getFirstName() + " " + user.getLastName());
        userEmail.setText(user.getEmail());
        userPhone.setText(user.getPhone());
        userAddress.setText(user.getAddress());
        coins.setText(user.getCoins().toString());
        if(user.getVerified()){
            verifiedIv.setVisibility(View.VISIBLE);
        }

        Model.instance.getCardsTransactionsRetrofit(new Model.cardsTransactionsReturnListener() {
            @Override
            public void onComplete(List<CardTransaction> cardTransaction, String message) {
                int like=0, unlike=0,numOfSoldCards=0,numOfBoughtCards=0;
                double coinsFromSales=0,coinsSpent=0;
                for (CardTransaction ct:cardTransaction) {
                    if(ct.getSatisfied()!=null && ct.getSeller().equals(user.getId())){
                        if(ct.getSatisfied()){
                            like++;
                        }else{
                            unlike++;
                        }
                    }

                    if(ct.getSeller().equals(user.getId())){
                        numOfSoldCards++;
                        coinsFromSales+=ct.getBoughtFor();
                    }else{
                        numOfBoughtCards++;
                        coinsSpent+=ct.getBoughtFor();
                    }
                }
                numLikeTv.setText(""+like);
                numUnlikeTv.setText(""+unlike);
                numOfSold.setText(""+numOfSoldCards);
                numOfBought.setText(""+numOfBoughtCards);
                soldInCoins.setText(""+coinsFromSales);
                boughtInCoins.setText(""+coinsSpent);

                if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
                    Model.instance.downloadImage(user.getProfilePicture().replace("/image/", ""),
                            new Model.byteArrayReturnListener() {
                                @Override
                                public void onComplete(Bitmap bitmap) {
                                    if (bitmap == null) {
                                        return;
                                    }
                                    userImage.setImageBitmap(bitmap);
                                    userImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    userImage.setClipToOutline(true);

                                    Model.instance.modelRetrofit.refreshToken(message -> {
                                        getIncomeStats();
                                        getOutComeStats();
                                    });
                                }
                            });
                }

            }
        });


    }

    private void getIncomeStats() {
        Model.instance.getUserIncome(new Model.incomeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(Income income) {
                if (income != null) {
                    numOfSold.setText(income.getTransactions().toString());
                    soldInCoins.setText(income.getIncome().toString());
                }
            }
        });
    }

    private void getOutComeStats() {
        Model.instance.getUserOutCome(new Model.outComeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(Outcome outcome) {
                if (outcome != null) {
                    numOfBought.setText(outcome.getTransactions().toString());
                    boughtInCoins.setText(outcome.getoutcome().toString());
                    Model.instance.getSellerRatings(Model.instance.getSignedUser().getId(), new Model.sellerRatingsListener() {
                        @Override
                        public void onComplete(SellerRatings sr) {
                            System.out.println(sr);
                        }
                    });
                }
            }
        });
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
    }


//    class MyViewHolder extends RecyclerView.ViewHolder {
//        public MyWalletFragment.OnItemClickListener listener;
//        TextView cardValue, cardValTag;
//        ImageView cardImage;
//        int position;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            cardValue = itemView.findViewById(R.id.my_card_row_amount_in_card_tv);
//            cardImage = itemView.findViewById(R.id.my_card_row_iv);
//            cardValTag = itemView.findViewById(R.id.my_card_row_price_now_tv);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    listener.onItemClick(v, position);
//                }
//            });
//        }
//
//        public void bindView(int position) {
//            String gfEmail = viewModel.getList().getValue().get(position).getOwnerEmail();
//            String userEmail = Model.instance.getSignedUser().getEmail();
//            if (!viewModel.getList().getValue().get(position).getDeleted() && gfEmail.equals(userEmail)) {
//                cardValue.setText(String.valueOf(viewModel.getList().getValue().get(position).getValue()));
//                Picasso.get().load(viewModel.getList().getValue().get(position).getGiftCardImageUrl()).into(cardImage);
//                this.position = position;
//            } else {
//                cardValue.setVisibility(View.GONE);
//                cardImage.setVisibility(View.GONE);
//                cardValTag.setVisibility(View.GONE);
//            }
//        }
//    }

//    interface OnItemClickListener {
//
//        void onItemClick(View v, int position);
//    }
//
//    class MyAdapter extends RecyclerView.Adapter<MyWalletFragment.MyViewHolder> {
//        MyWalletFragment.OnItemClickListener listener;
//
//        public void setOnItemClickListener(MyWalletFragment.OnItemClickListener listener) {
//            this.listener = listener;
//        }
//
//        @NonNull
//        @Override
//        public MyWalletFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = getLayoutInflater().inflate(R.layout.my_card_row, parent, false);
//            MyWalletFragment.MyViewHolder holder = new MyWalletFragment.MyViewHolder(view);
//            holder.listener = listener;
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MyWalletFragment.MyViewHolder holder, int position) {
//            holder.bindView(position);
//        }
//
//        @Override
//        public int getItemCount() {
//            if (viewModel.getList().getValue() == null)
//                return 0;
//            return viewModel.getList().getValue().size();
//        }
//    }
}