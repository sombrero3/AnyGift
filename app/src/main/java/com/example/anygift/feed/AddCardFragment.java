package com.example.anygift.feed;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.model.Model;
import com.example.anygift.model.Utils;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddCardFragment extends Fragment {
    TextInputEditText cardValue, cardAskingValue, cardNumber;
    Button addCardButton;
    ImageView giftCardImage;
    View view;
    ProgressBar pb;
    String latAndLong, cardType;
//    UserViewModel userViewModel;
    Spinner spinnerCardType;
    TextView dateTv;
    CheckBox forSaleCb;
    DatePickerDialog.OnDateSetListener dateListener;
    List<String> cardTypes;
    int year, month, day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //getActivity().setTitle("AnyGift - AddCard");
        view = inflater.inflate(R.layout.fragment_add_card, container, false);
        cardNumber = view.findViewById(R.id.add_card_number);
        cardValue = view.findViewById(R.id.add_card_value);
        cardAskingValue = view.findViewById(R.id.add_card_asking_price);
        addCardButton = view.findViewById(R.id.add_upload_bt);
        giftCardImage = view.findViewById(R.id.add_giftCardImage);
        dateTv = view.findViewById(R.id.add_card_date_tv);
        forSaleCb = view.findViewById(R.id.add_card_for_sale_cb);
        pb = view.findViewById(R.id.add_pb);

        pb.setVisibility(View.VISIBLE);
        giftCardImage.setTag("");

        setCardtypesSpinner();
        setDateSelector();


//        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        return view;
    }

    private void setDateSelector() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                m += 1;
                year = y;
                month = m;
                day = d;
                dateTv.setText(day + "/" + month + "/" + year);
            }
        };
    }

    private void setCardtypesSpinner() {
        List<CardType> cts = Model.instance.cardTypes;
        cardTypes = new ArrayList<>();
        for (CardType ct : cts) {
            cardTypes.add(ct.getName());
        }

        spinnerCardType = (Spinner) view.findViewById(R.id.option);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, cardTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCardType.setAdapter(adapter);
        spinnerCardType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                giftCardImage.setImageBitmap(cts.get(i).getPicture());
                cardType = cts.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cardType = cts.get(0).getId();
            }
        });
        pb.setVisibility(View.INVISIBLE);
    }


    void popMsg(String Msg) {
        Snackbar mySnackbar = Snackbar.make(view, Msg, BaseTransientBottomBar.LENGTH_LONG);
        mySnackbar.show();

    }

    void enableButtons() {
        addCardButton.setEnabled(true);
    }

    void disableButtons() {
        addCardButton.setEnabled(false);
    }

    static boolean isLegalDate(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        return sdf.parse(s, new ParsePosition(0)) != null;
    }


    void logging(String CardValue, String CardAskingValue, String CardNumber) {
        Log.d("TAG", CardValue);
        Log.d("TAG", CardAskingValue);
        Log.d("TAG", CardNumber);
    }

    private void upload() {
        pb.setVisibility(View.VISIBLE);
        boolean error = false;
        String errorMsg = "";
        disableButtons();

        if (error) {
            popMsg(errorMsg);
            enableButtons();
            return;
        }

        Log.d("TAG", "date = " + day + "/" + month + "/" + year);
        Log.d("TAG", "card type id = " + cardType);

        HashMap<String, Object> map = Card.mapToAddCard(Double.parseDouble(cardAskingValue.getText().toString()),
                Double.parseDouble(cardValue.getText().toString()), cardNumber.getText().toString(), cardType,
                Model.instance.modelRetrofit.getUserId(), forSaleCb.isChecked(),
                Utils.convertDateToLong(Integer.toString(day), Integer.toString(month), Integer.toString(year)));

        //"62532859427c06ccbf55d31e" <--> this is card type id

        Model.instance.addCardRetrofit(map, new Model.cardReturnListener() {

            @Override
            public void onComplete(Card card, String message) {
                if (card != null) {
                    popMsg("GiftCard successfully uploaded");
                    Navigation.findNavController(dateTv).navigate(AddCardFragmentDirections.actionGlobalMyCardsFragment());
                } else {
                    popMsg("GiftCard uploading Failed!");
                }
                enableButtons();
                pb.setVisibility(View.INVISIBLE);
            }
        });

    }
}
