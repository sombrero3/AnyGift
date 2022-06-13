package com.example.anygift.feed;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.Category;
import com.example.anygift.adapters.CardsListAdapter;
import com.example.anygift.model.Model;
import com.example.anygift.model.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class SearchGiftCardFragment extends Fragment {
    TextView dateTv,spinnerTypeTitleTv,spinnerCategoryTitleTv;

    DatePickerDialog.OnDateSetListener dateListener;
    EditText maxPriceEt;
    Button searchBtn;
    int year,month,day;
    CardsListAdapter adapter;
    List<Card> searchResult;
    Spinner spinnerCardType,spinnerCategories;
    List<String> cardTypes,categories;
    String cardTypeId="Any",categoryId="Any";
    ProgressBar pb;
    Switch filterSw;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_gift_card, container, false);

        maxPriceEt = view.findViewById(R.id.search_max_price_et);
        pb = view.findViewById(R.id.search_progressBar);
        pb.setVisibility(View.VISIBLE);
        spinnerCardType = (Spinner) view.findViewById(R.id.search_card_type_spinner);
        spinnerCategories = view.findViewById(R.id.search_category_spinner);
        spinnerTypeTitleTv = view.findViewById(R.id.search_spinner_type_title_tv);
        spinnerCategoryTitleTv = view.findViewById(R.id.search_spinner_category_title_tv);
        filterSw = view.findViewById(R.id.search_switch);

        RecyclerView cardsList = view.findViewById(R.id.search_result_rv);
        cardsList.setHasFixedSize(true);
        // RecyclerView.LayoutManager horizontalLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        // horizontalLayout
        //
        cardsList.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResult = new ArrayList<>();
        adapter = new CardsListAdapter(searchResult);
        cardsList.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                double val = searchResult.get(position).getValue();
                String id = searchResult.get(position).getId();
                Log.d("TAG", "Gift card in value of: " + val);
                Bundle map = new Bundle();
                map.putString("giftCardId",id);
                Navigation.findNavController(v).navigate(R.id.action_global_cardsDetailsFragment,map);
            }
        });

        Model.instance.getAllFeedCardsForSale(new Model.cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                searchResult.clear();
                searchResult.addAll(cards);
                adapter.notifyDataSetChanged();
                setCardtypesSpinner();
                setCategoriesSpinner();
            }
        });

        searchBtn =view.findViewById(R.id.search_card_search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        dateTv = view.findViewById(R.id.search_card_date_tv);
        Calendar calendar = Calendar.getInstance();
        year=0;
        month=0;
        day=0;
        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                m = m+1;
                year = y;
                month = m;
                day = d;
                dateTv.setText(day+"/"+month+"/"+year);
            }
        };

        filterSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    filterSw.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.coins)));
                    filterSw.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
                    filterSw.setTextColor(getResources().getColor(R.color.coins));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        filterSw.setOutlineAmbientShadowColor(getResources().getColor(R.color.pink));
                    }

                    filterSw.setText("Hide Filter");
                    showSearch();
                } else {
                    filterSw.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_dark)));
                    filterSw.setTrackTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_dark)));
                    filterSw.setTextColor(getResources().getColor(R.color.blue_dark));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        filterSw.setOutlineAmbientShadowColor(getResources().getColor(R.color.pink));
                    }
                    filterSw.setText("Show Filter");
                    hideSearch();
                }
            }
        });
        return view;
    }
    private void hideSearch(){
        spinnerCardType.setVisibility(View.GONE);
        dateTv.setVisibility(View.GONE);
        searchBtn.setVisibility(View.GONE);
        maxPriceEt.setVisibility(View.GONE);
        spinnerTypeTitleTv.setVisibility(View.GONE);
        spinnerCategories.setVisibility(View.GONE);
        spinnerCategoryTitleTv.setVisibility(View.GONE);
    }
    private void showSearch(){
        spinnerCardType.setVisibility(View.VISIBLE);
        dateTv.setVisibility(View.VISIBLE);
        maxPriceEt.setVisibility(View.VISIBLE);
        searchBtn.setVisibility(View.VISIBLE);
        spinnerTypeTitleTv.setVisibility(View.VISIBLE);
        spinnerCategories.setVisibility(View.VISIBLE);
        spinnerCategoryTitleTv.setVisibility(View.VISIBLE);
    }


    private void search() {
        pb.setVisibility(View.VISIBLE);
        Model.instance.searchCards(day, month, year, maxPriceEt.getText().toString(), cardTypeId,categoryId, new Model.cardsListener() {
            @Override
            public void onComplete(List<Card> cards) {
                searchResult.clear();
                searchResult.addAll(cards);
                adapter.notifyDataSetChanged();
                pb.setVisibility(View.GONE);
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
        spinnerCategories.setAdapter(adapter);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        spinnerCategories.setSelection(cats.size());
        pb.setVisibility(View.INVISIBLE);
    }
    private void setCardtypesSpinner() {
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
        pb.setVisibility(View.INVISIBLE);
    }

}