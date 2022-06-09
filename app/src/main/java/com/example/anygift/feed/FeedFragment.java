package com.example.anygift.feed;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.Category;
import com.example.anygift.adapters.CardsListAdapter;
import com.example.anygift.model.Model;
import com.example.anygift.model.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FeedFragment extends Fragment {
    FeedViewModel viewModel;
    CardsListAdapter mostRecAdapter;
    SwipeRefreshLayout swipeRefresh;
    TextView dateTv, spinnerTypeTitleTv,mostRecTv,spinnerCategoryTitleTv;
    EditText maxPriceEt;
    ImageView walletIv,transIv,shopIv;
    FloatingActionButton addFab;
    RecyclerView mostRecList;
    int year,month,day;
    Button searchBtn,verificationBtn;
    View v;
    DatePickerDialog.OnDateSetListener dateListener;
    List<Card> mosetRecCl;
    Spinner spinnerCardType,spinnerCategory;
    List<String> cardTypes,categories;
    Switch filterSw;
    String cardTypeId="Any",categoryId ="Any";
    ProgressBar pb;
    Animation topAnim,bottomAnim,rightAnim;

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
        v=view;
        pb=view.findViewById(R.id.feed_progressBar);
        swipeRefresh = view.findViewById(R.id.giftCardlist_swiperefresh);
        mostRecList = view.findViewById(R.id.cards_list_rv);
        dateTv = view.findViewById(R.id.feed_date_tv);
        maxPriceEt = view.findViewById(R.id.feed_max_price_et);
        spinnerCardType = view.findViewById(R.id.feed_card_type_spinner);
        searchBtn = view.findViewById(R.id.feed_search_btn);
        verificationBtn = view.findViewById(R.id.feed_verification_btn);
        filterSw = view.findViewById(R.id.feed_filter_switch);
        spinnerTypeTitleTv = view.findViewById(R.id.feed_spinner_title_tv);
        addFab = view.findViewById(R.id.feed_search_fab);
        mostRecTv = view.findViewById(R.id.feed_most_rec_tv);
        spinnerCategoryTitleTv = view.findViewById(R.id.feed_card_category_title_tv);
        spinnerCategory = view.findViewById(R.id.feed_card_category_spinner);
        transIv = view.findViewById(R.id.feed_transactions_iv);
        shopIv = view.findViewById(R.id.feed_shop_iv);
        walletIv = view.findViewById(R.id.feed_wallet_iv);


        mosetRecCl = new ArrayList<>();

        setCardTypeSpinner();
        setCategoriesSpinner();

        mostRecList.setHasFixedSize(true);
        mostRecList.setLayoutManager(new LinearLayoutManager(getContext()));
        mostRecAdapter = new CardsListAdapter(mosetRecCl);
        mostRecList.setAdapter(mostRecAdapter);

        mostRecAdapter.setOnItemClickListener(new com.example.anygift.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                double val = mosetRecCl.get(position).getValue();
                String id = mosetRecCl.get(position).getId();
                Log.d("TAG", "Gift card in value of: " + val);
                Navigation.findNavController(v).navigate(FeedFragmentDirections.actionFeedFragmentToCardsDetailsFragment(id));
            }
        });

        setMostRecRv();
        setHasOptionsMenu(true);


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refreshMostRecRv();
                search();
            }
        });
//        swipeRefresh.setOnRefreshListener(() -> Model.instance.refreshGiftCardsList());
//        viewModel.getList().observe(getViewLifecycleOwner(), list1 -> refresh());
//        swipeRefresh.setRefreshing(Model.instance.getListLoadingState().getValue() == Model.GiftListLoadingState.loading);
//        Model.instance.getListLoadingState().observe(getViewLifecycleOwner(), ListLoadingState -> {
//            if (ListLoadingState == Model.GiftListLoadingState.loading) {
//                swipeRefresh.setRefreshing(true);
//            } else {
//                swipeRefresh.setRefreshing(false);
//            }
//
//        });

        searchBtn.setOnClickListener(v -> search());

        addFab.setOnClickListener(v-> Navigation.findNavController(v).navigate(R.id.action_global_addCardFragment));

        Calendar calendar = Calendar.getInstance();
        year=0;
        month=0;
        day=0;
        dateTv.setOnClickListener(view1 -> {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateListener, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });
        dateListener = (datePicker, y, m, d) -> {
            m = m+1;
            year = y;
            month = m;
            day = d;
            dateTv.setText(day+"/"+month+"/"+year);
        };

        //animations
        rightAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.right_anim);
        filterSw.setAnimation(rightAnim);
        bottomAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.bottom_anim);
        mostRecTv.setAnimation(bottomAnim);

        if(Model.instance.getSignedUser().getVerified()){
            verificationBtn.setVisibility(View.GONE);
        }else{
            verificationBtn.setAnimation(bottomAnim);
            verificationBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_global_verificationFragment));
        }

        filterSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    showSearch();
                } else {
                    hideSearch();
                }
            }
        });

        Bundle args = new Bundle();
        args.putString("cardId","");
        walletIv.setOnClickListener(v->Navigation.findNavController(v).navigate(R.id.action_global_myCardsFragment));
        shopIv.setOnClickListener(v->Navigation.findNavController(v).navigate(R.id.action_global_shopFragment,args));
        transIv.setOnClickListener(v->Navigation.findNavController(v).navigate(R.id.action_global_transactionsFragment));

        return view;

    }

    private void hideSearch(){
        spinnerCardType.setVisibility(View.GONE);
        dateTv.setVisibility(View.GONE);
        searchBtn.setVisibility(View.GONE);
        maxPriceEt.setVisibility(View.GONE);
        spinnerTypeTitleTv.setVisibility(View.GONE);
        spinnerCategory.setVisibility(View.GONE);
        spinnerCategoryTitleTv.setVisibility(View.GONE);
    }
    private void showSearch(){
        spinnerCardType.setVisibility(View.VISIBLE);
        dateTv.setVisibility(View.VISIBLE);
        maxPriceEt.setVisibility(View.VISIBLE);
        searchBtn.setVisibility(View.VISIBLE);
        spinnerTypeTitleTv.setVisibility(View.VISIBLE);
        spinnerCategory.setVisibility(View.VISIBLE);
        spinnerCategoryTitleTv.setVisibility(View.VISIBLE);
    }

    private void search() {
        pb.setVisibility(View.VISIBLE);
        mostRecTv.setText("Search result:");
        Model.instance.searchCards(day, month, year, maxPriceEt.getText().toString(), cardTypeId,categoryId, new Model.cardsListener() {
            @Override
            public void onComplete(List<Card> cards) {
                mosetRecCl.clear();
                mosetRecCl.addAll(cards);
                mostRecAdapter.notifyDataSetChanged();
                pb.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }
        });
    }
    private void setCategoriesSpinner() {
        List<Category> cats = Model.instance.categories;
        categories = new ArrayList<>();

        for (Category ct : cats) {
            categories.add(ct.getName());
        }
        categories.add("Any");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i<cats.size()) {
                    categoryId = cats.get(i).getId();
                }else{
                    categoryId = "Any";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                categoryId = "Any";
            }
        });
        spinnerCategory.setSelection(cats.size());
    }

    private void setCardTypeSpinner() {
        List<CardType> cts = Model.instance.cardTypes;
        cardTypes = new ArrayList<>();

        for (CardType ct : cts) {
            cardTypes.add(ct.getName());
        }
        cardTypes.add("Any");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, cardTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCardType.setAdapter(adapter);
        spinnerCardType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i<cts.size()) {
                    cardTypeId = cts.get(i).getId();
                }else{
                    cardTypeId = "Any";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cardTypeId = "Any";
            }
        });
        spinnerCardType.setSelection(cts.size());
    }

    private void setMostRecRv() {
        Model.instance.getAllFeedCardsForSale(new Model.cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                cards.sort((c1, c2) -> {
                    double c1value = Double.parseDouble(c1.getPrecentageSaved().replace("%", ""));
                    double c2value = Double.parseDouble(c2.getPrecentageSaved().replace("%", ""));
                    return Double.compare(c1value, c2value);
                });

                mosetRecCl.clear();
                Calendar calendar = Calendar.getInstance();
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH);
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                Long now = Utils.convertDateToLong(Integer.toString(d), Integer.toString(m), Integer.toString(y));
                String userId =  Model.instance.getSignedUser().getId();

                for (Card c:cards) {
                    if(c.getExpirationDate()>now && !c.getOwner().equals(userId)){
                        mosetRecCl.add(c);
                    }
                }
                mostRecAdapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
                pb.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setDynamicRvs() {
        //viewModel.refreshMap(new Model.VoidListener() {
        //    @Override
        //    public void onComplete() {
//                setMostRecRv();
//                //--dreamCard RV--//
//
//                dreamCardList.setHasFixedSize(true);
//                RecyclerView.LayoutManager dreamCardLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
//                dreamCardList.setLayoutManager(dreamCardLayout);
//                dreamCardAdapter = new CardsListAdapter(viewModel.getDreamCardsList());
//                dreamCardList.setAdapter(dreamCardAdapter);
//
//                dreamCardAdapter.setOnItemClickListener(new com.example.anygift.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View v, int position) {
//                        double val = viewModel.getDreamCardsList().get(position).getValue();
//                        String id = viewModel.getDreamCardsList().get(position).getId();
//                        Log.d("TAG", "Gift card in value of: " + val);
//                        Navigation.findNavController(v).navigate(FeedFragmentDirections.actionFeedFragmentToCardsDetailsFragment(id));
//                    }
//                });
//
//                //---Shufersal RV---//
//
//                shufersalList.setHasFixedSize(true);
//                RecyclerView.LayoutManager shufersalLayout = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
//                shufersalList.setLayoutManager(shufersalLayout);
//                shufersalAdapter = new CardsListAdapter(viewModel.getShufersalList());
//                shufersalList.setAdapter(shufersalAdapter);
//
//                shufersalAdapter.setOnItemClickListener(new com.example.anygift.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View v, int position) {
//                        double val = viewModel.getShufersalList().get(position).getValue();
//                        String id = viewModel.getShufersalList().get(position).getId();
//                        Log.d("TAG", "Gift card in value of: " + val);
//                        Navigation.findNavController(v).navigate(FeedFragmentDirections.actionFeedFragmentToCardsDetailsFragment(id));
//                    }
//                });
        //    }
        //
    }

    private void refresh() {
        mostRecAdapter.notifyDataSetChanged();
    }

}