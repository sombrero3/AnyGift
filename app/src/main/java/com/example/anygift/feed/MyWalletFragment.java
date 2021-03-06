package com.example.anygift.feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardTransaction;
import com.example.anygift.Retrofit.Category;
import com.example.anygift.Retrofit.Income;
import com.example.anygift.Retrofit.Outcome;
import com.example.anygift.Retrofit.SellerRatings;
import com.example.anygift.adapters.CardsListAdapter;
import com.example.anygift.model.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MyWalletFragment extends Fragment {
    View view;
    ProgressBar pb;
    MyWalletViewModel viewModel;
    CardsListAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    TextView userName, userEmail, userPhone, userAddress, numOfSold, numOfBought, soldInCoins, boughtInCoins,numLikeTv,numUnlikeTv,coinsTv;
    ImageView userImage,  addCardIv,verifiedIv;
    Button editBtn,transactionsBtn;

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
        userAddress = view.findViewById(R.id.my_cards_address_tv);
        numOfSold = view.findViewById(R.id.my_cards_sold_counter_tv);
        numOfBought = view.findViewById(R.id.my_cards_bought_counter_tv);
        soldInCoins = view.findViewById(R.id.my_cards_sold_in_coins_tv);
        boughtInCoins = view.findViewById(R.id.my_cards_bought_in_coins_tv);
        editBtn = view.findViewById(R.id.my_cards_edit_btn);
        addCardIv = view.findViewById(R.id.my_cards_add_card_iv);
        numLikeTv = view.findViewById(R.id.my_cards_num_like_tv);
        numUnlikeTv = view.findViewById(R.id.my_cards_num_un_like_tv);
        verifiedIv = view.findViewById(R.id.my_cards_verified_iv);
        transactionsBtn = view.findViewById(R.id.my_cards_transactions_btn);
        coinsTv = view.findViewById(R.id.my_cards_coins_tv);

//        Model.instance.getAllCategories(new Model.categoriesReturnListener() {
//            @Override
//            public void onComplete(List<Category> cat) {
//                System.out.println(cat);
//            }
//        });

        editBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(MyWalletFragmentDirections.actionMyCardsFragmentToEditProfileFragment()));
        addCardIv.setOnClickListener(v -> Navigation.findNavController(v).navigate(MyWalletFragmentDirections.actionGlobalAddCardFragment()));

        setUserUI();
        setHasOptionsMenu(true);

//        swipeRefresh.setRefreshing(Model.instance.getListLoadingState().getValue() == Model.GiftListLoadingState.loading);
//        Model.instance.getListLoadingState().observe(getViewLifecycleOwner(), ListLoadingState -> {
//            if (ListLoadingState == Model.GiftListLoadingState.loading) {
//                swipeRefresh.setRefreshing(true);
//            } else {
//                swipeRefresh.setRefreshing(false);
//            }
//
//        });
        swipeRefresh.setOnRefreshListener(() -> {
            viewModel.setList(() -> {
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            });
        });

        transactionsBtn.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_global_transactionsFragment));
        return view;

    }

    private void setUserUI() {
        com.example.anygift.Retrofit.User user = Model.instance.getSignedUser();
        System.out.println(user);

        userName.setText(user.getFirstName() + " " + user.getLastName());
        userEmail.setText(user.getEmail());
        userPhone.setText(user.getPhone());
        userAddress.setText(user.getAddress());
        if(user.getVerified()){
            verifiedIv.setVisibility(View.VISIBLE);
        }

        Model.instance.getCardsTransactionsRetrofit(new Model.cardsTransactionsReturnListener() {
            @Override
            public void onComplete(List<CardTransaction> cardTransaction, String message) {
                int like=0, unlike=0,numOfSoldCards=0,numOfBoughtCards=0;
                double coinsFromSales=0,coinsSpent=0;
                if(cardTransaction!=null) {
                    for (CardTransaction ct : cardTransaction) {
                        if (ct.getSatisfied() != null && ct.getSeller().equals(user.getId())) {
                            if (ct.getSatisfied()) {
                                like++;
                            } else {
                                unlike++;
                            }
                        }

                        if (ct.getSeller().equals(user.getId())) {
                            numOfSoldCards++;
                            coinsFromSales += ct.getBoughtFor();
                        } else {
                            numOfBoughtCards++;
                            coinsSpent += ct.getBoughtFor();
                        }
                    }
                }
                numLikeTv.setText(""+like);
                numUnlikeTv.setText(""+unlike);
                numOfSold.setText(""+numOfSoldCards);
                numOfBought.setText(""+numOfBoughtCards);
                soldInCoins.setText(""+coinsFromSales);
                boughtInCoins.setText(""+coinsSpent);
                coinsTv.setText(Model.instance.getSignedUser().getCoins().toString());

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
}