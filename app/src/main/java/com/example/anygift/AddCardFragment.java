package com.example.anygift;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;
import com.example.anygift.model.ModelFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class AddCardFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    ImageView giftCardInTitle;
    TextView title;
    EditText cardName;
    EditText cardValue;
    EditText cardExpDate;
    EditText cardAskingValue;
    TextView uploadPicText;
    ImageButton uploadPicButton;
    Button addCardButton;
    Bitmap giftCardBitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_card, container, false);
        giftCardInTitle = view.findViewById(R.id.add_card_giftcard_iv);
        title = view.findViewById(R.id.add_header_tv);
        cardName = view.findViewById(R.id.add_card_name_et);
        cardValue = view.findViewById(R.id.add_amount_et);
        cardExpDate = view.findViewById(R.id.add_exp_date_et);
        cardAskingValue = view.findViewById(R.id.add_asking_et);
        uploadPicText = view.findViewById(R.id.add_take_picture_tv);
        uploadPicButton = view.findViewById(R.id.add_camera_bt);
        addCardButton = view.findViewById(R.id.add_upload_bt);
        uploadPicButton.setOnClickListener(new View.OnClickListener() {
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
        String CardValue = cardValue.toString();
        String CardAskingValue = cardAskingValue.toString();
        String CardName = cardName.toString();
        String CardExpDate = cardExpDate.toString();
        GiftCard newGiftCard = new GiftCard(); //todo maybe change double to string
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.getCurrentUser();
        if (giftCardBitmap == null){
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

    }
}
