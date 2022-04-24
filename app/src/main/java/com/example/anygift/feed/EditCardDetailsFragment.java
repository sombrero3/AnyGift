package com.example.anygift.feed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.model.Model;

import java.util.ArrayList;
import java.util.List;


public class EditCardDetailsFragment extends Fragment {
    View view;
    EditText numberEt,priceEt,expEt;
    Spinner typeSp;
    String cardType;
    TextView  valueTv;
    Button save, mapBtn, deleteBtn;
    ImageView giftCardImage;
    Card giftCard = null;
    List<String> cardTypes;
    ProgressBar pb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_card_details, container, false);
        //getActivity().setTitle("AnyGift - EditCardDetails");
        String giftCardId = EditCardDetailsFragmentArgs.fromBundle(getArguments()).getGiftCardId();
        pb = view.findViewById(R.id.edit_card_pb);
        pb.setVisibility(View.VISIBLE);
        typeSp = view.findViewById(R.id.edit_card_type_spinner);
        numberEt = view.findViewById(R.id.edit_card_card_number);
        priceEt = view.findViewById(R.id.edit_card_card_asking_price);
        expEt = view.findViewById(R.id.edit_card_card_date_et);
        valueTv = view.findViewById(R.id.edit_card_card_value);
        save = view.findViewById(R.id.edit_card_upload_bt);
        giftCardImage = view.findViewById(R.id.edit_card_giftCardImage);


        Model.instance.getCardRetrofit(giftCardId, new Model.cardReturnListener() {
            @Override
            public void onComplete(Card card, String message) {
                giftCard = card;
                //typeSp.setText(card.getCardType());
                numberEt.setText(card.getCardNumber());
                priceEt.setText(card.getPrice().toString());
                expEt.setText(card.getExpirationDate().toString().split(" ")[0]);
                valueTv.setText(card.getValue().toString());
                setCardTypesSpinner();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });
        return view;
    }
    private void setCardTypesSpinner() {
        List<CardType> cts = Model.instance.cardTypes;
        cardTypes = new ArrayList<>();
        int pos=0;
        for (int i=0;i<cts.size();i++) {
            cardTypes.add(cts.get(i).getName());
            if(cts.get(i).getId().equals(giftCard.getCardType())){
                pos=i;
            }

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, cardTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSp.setAdapter(adapter);
        int finalPos = pos;
        typeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                giftCardImage.setImageBitmap(cts.get(i).getPicture());
                cardType = cts.get(i).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                giftCardImage.setImageBitmap(cts.get(finalPos).getPicture());
                cardType = cts.get(finalPos).getId();
            }
        });

        typeSp.setSelection(finalPos);
        pb.setVisibility(View.GONE);

    }


    private void saveChanges() {
        giftCard.setCardNumber(numberEt.getText().toString());
        //giftCard.setCardType(typeSp.getText().toString());
        giftCard.setPrice(Double.parseDouble(priceEt.getText().toString()));
        //giftCard.setExpirationDate();
        giftCard.setValue(Double.parseDouble(valueTv.getText().toString()));

        Model.instance.addCardRetrofit(giftCard.toMap(), new Model.cardReturnListener() {
            @Override
            public void onComplete(Card card, String message) {
                Navigation.findNavController(valueTv).navigateUp();
            }
        });
    }

    private void displayFailedError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Operation Failed");
        builder.setMessage("Saving image failed, please try again later...");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

}
