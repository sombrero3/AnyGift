package com.example.anygift;

import static com.example.anygift.model.Utils.isInt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddCardFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    TextInputEditText cardValue;
    TextInputEditText cardExpDay, cardExpMonth, cardExpYear;
    TextInputEditText cardAskingValue;
    TextInputEditText cardNumber;
    ImageButton uploadPicButton;
    Button addCardButton;
    Bitmap giftCardBitmap;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_card, container, false);
        cardNumber = view.findViewById(R.id.add_card_number);
        cardValue = view.findViewById(R.id.add_card_value);
        cardExpDay = view.findViewById(R.id.add_card_exp_day);
        cardExpMonth = view.findViewById(R.id.add_card_exp_mo);
        cardExpYear = view.findViewById(R.id.add_card_exp_year);
        cardAskingValue = view.findViewById(R.id.add_card_exp_value);


        // uploadPicText = view.findViewById(R.id.add_take_picture_tv);
        uploadPicButton = view.findViewById(R.id.add_camera_bt);
        addCardButton = view.findViewById(R.id.add_upload_bt);
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        uploadPicButton.setOnClickListener(v -> {
            openCam();
        });

        return view;
    }

    private void openCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                giftCardBitmap = (Bitmap) extras.get("data");
            }
        }
    }

    void popMsg(String Msg) {
        Snackbar mySnackbar = Snackbar.make(view, Msg, BaseTransientBottomBar.LENGTH_LONG);
        mySnackbar.show();

    }

    void enableButtons() {
        addCardButton.setEnabled(true);
        uploadPicButton.setEnabled(true);
    }

    void disableButtons() {
        addCardButton.setEnabled(false);
        uploadPicButton.setEnabled(false);

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
        boolean error = false;
        String errorMsg = "";
        disableButtons();
        String CardValue = cardValue.getText().toString();
        String CardAskingValue = cardAskingValue.getText().toString();
        String CardNumber = cardNumber.getText().toString();
        String day = cardExpDay.getText().toString();
        String mo = cardExpMonth.getText().toString();
        String year = cardExpYear.getText().toString();
        String dateFormatted = year + "-" + mo + "-" + day;
        if (!isLegalDate(dateFormatted)){
            error=true;
            errorMsg += "date: " + dateFormatted + " is Invalid!\n";
        }
        if (giftCardBitmap == null) {
            error = true;
            errorMsg += "GiftCard Picture Is Missing!";
        }

        if (error) {
            popMsg(errorMsg);
            enableButtons();
            return;
        }

        // if reached here all is good
        String date = day + "/" + mo + "/" + year;
        logging(CardValue, CardAskingValue, CardNumber);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String emailUser = user.getEmail().toString();

        GiftCard newGiftCard = new GiftCard(CardNumber, Double.parseDouble(CardValue), date, Double.parseDouble(CardAskingValue), emailUser); //todo maybe change double to string

        Model.instance.saveImage(giftCardBitmap, newGiftCard.getId() + ".jpg", url -> {
            newGiftCard.setGiftCardImageUrl(url);
            Model.instance.addGiftCard(newGiftCard, () -> {
                Navigation.findNavController(view).navigate(R.id.action_global_feedFragment);
                popMsg("GiftCard Added! :)");
            });
        });

    }
}
