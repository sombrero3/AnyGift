package com.example.anygift.feed;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.anygift.OnItemClickListener;
import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.adapters.CardsListAdapter;
import com.example.anygift.model.Model;
import com.example.anygift.model.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class SearchGiftCardFragment extends Fragment {
    TextView dateTv;
    DatePickerDialog.OnDateSetListener dateListener;
    EditText maxPriceEt;
    Button searchBtn;
    int year,month,day;
    CardsListAdapter adapter;
    List<Card> searchResult;
    Spinner spinnerCardType;
    List<String> cardTypes;
    String cardTypeId;
    ProgressBar pb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_gift_card, container, false);

        maxPriceEt = view.findViewById(R.id.search_max_price_et);
        pb = view.findViewById(R.id.search_progressBar);
        pb.setVisibility(View.VISIBLE);
        spinnerCardType = (Spinner) view.findViewById(R.id.search_card_type_spinner);
        RecyclerView cardsList = view.findViewById(R.id.search_result_rv);
        cardsList.setHasFixedSize(true);
        RecyclerView.LayoutManager horizontalLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        cardsList.setLayoutManager(horizontalLayout);
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
        Model.instance.getAllCards(new Model.cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                searchResult.clear();
                searchResult.addAll(cards);
                adapter.notifyDataSetChanged();
                setCardtypesSpinner();
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

        return view;
    }


    private void search() {
        pb.setVisibility(View.VISIBLE);
        Model.instance.searchCards(day, month, year, maxPriceEt.getText().toString(), cardTypeId, new Model.cardsListener() {
            @Override
            public void onComplete(List<Card> cards) {
                searchResult.clear();
                searchResult.addAll(cards);
                adapter.notifyDataSetChanged();
                pb.setVisibility(View.GONE);
            }
        });
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