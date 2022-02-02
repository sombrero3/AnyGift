package com.example.anygift;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddCardFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    ImageView giftCardInTitle;
    TextView title;
    TextInputEditText cardValue;
    TextInputEditText cardExpDay,cardExpMonth,cardExpYear;
    TextInputEditText cardAskingValue;
    TextInputEditText cardNumber;
    ImageButton uploadPicButton;
    Button addCardButton;
    Bitmap giftCardBitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_card, container, false);

        //giftCardInTitle = view.findViewById(R.id.add_card_giftcard_iv);
        //title = view.findViewById(R.id.add_header_tv);
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
    private void openCam () {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA){
            if (resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                giftCardBitmap = (Bitmap) extras.get("data");
            }
        }
    }

    private void upload(){
        addCardButton.setEnabled(false);
        uploadPicButton.setEnabled(false);
        String CardValue = cardValue.getText().toString();
        String CardAskingValue = cardAskingValue.getText().toString();
        String CardNumber = cardNumber.getText().toString();

        String day = cardExpDay.getText().toString();
        String mo = cardExpMonth.getText().toString();
        String year = cardExpYear.getText().toString();

         int dayI=Integer.parseInt(day);
        int moI=Integer.parseInt(mo);
        if(dayI<1||dayI>31||(moI==2&&dayI>29)){
            cardExpDay.setError("Wrong day");
            cardExpMonth.setError("Wrong month");
            return;
        }
        String date=day+"/"+mo+"/"+year;
        Log.d("TAG",CardValue);
        Log.d("TAG",CardAskingValue);
        Log.d("TAG",CardNumber);
        Log.d("TAG",date);

        GiftCard newGiftCard = new GiftCard(); //todo maybe change double to string
       //
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user= auth.getCurrentUser();
        String emailUser=user.getEmail().toString();

     /*   if (giftCardBitmap == null){
            Model.instance.addGiftCard(newGiftCard,()->{
//                Navigation.findNavController(nameEt).navigateUp(); //todo where do we go?
            });
        }else{
            Model.instance.saveImage(giftCardBitmap, newGiftCard.getId() + ".jpg", url -> {
                newGiftCard.setGiftCardImageUrl(url);
                Model.instance.addGiftCard(newGiftCard,()->{
//                    Navigation.findNavController(nameEt).navigateUp();
                });
            });
        }
*/
    }
}
