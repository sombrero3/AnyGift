package com.example.anygift.feed;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.Category;
import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;
import com.example.anygift.model.User;
import com.example.anygift.model.Utils;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class CardsDetailsFragment extends Fragment {
    View view;
    UserViewModel userViewModel;
    private TextView name, value,buyAt,popUpTypeTv,popUpExpTv,popupValueTv, popUpPriceTv;
    private Button mapBtn, editBtn,deleteBtn,buyBtn,popUpSaveBtn,popUpCancel;
    private ImageView userImage,giftCardImage, popUpCcardImage;
    Card card;
    User user;
    CardType cardType;
    List<Category> categoryList;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog dialog;
    String imageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        Testing testing = new Testing();

        view = inflater.inflate(R.layout.fragment_cards_details, container, false);
        //getActivity().setTitle("AnyGift - CardsDetails");
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        String cardId = CardsDetailsFragmentArgs.fromBundle(getArguments()).getGiftCardId();
        userImage = view.findViewById(R.id.details_picture_iv);

        name = view.findViewById(R.id.card_details_username_tv);
        value = view.findViewById(R.id.details_giftvalue_tv);
        mapBtn = view.findViewById(R.id.cardDetails_mapBtn);
        buyAt = view.findViewById(R.id.details_buyatval_tv);
        deleteBtn = view.findViewById(R.id.details_delete_btn);
        editBtn = view.findViewById(R.id.details_edit_btn2);
        giftCardImage = view.findViewById(R.id.details_giftpic_iv);
        buyBtn = view.findViewById(R.id.card_details_buy_btn);
        ////////////////////////////////////////
        Model.instance.getAllCards(new Model.cardsReturnListener() {
            @Override
            public void onComplete(List<Card> cards, String message) {
                card = cards.get(0);
                setCardImage(giftCardImage);
                String userId = card.getOwner();
                Model.instance.getUserRetrofit(userId, new Model.userReturnListener() {
                    @Override
                    public void onComplete(com.example.anygift.Retrofit.User user, String message) {
                        if (user.getProfilePicture() != null  && !user.getProfilePicture().isEmpty()) {
                            Picasso.get().load(user.getProfilePicture()).into(userImage);
                            userImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            userImage.setClipToOutline(true);
                        }
                        String val = Double.toString(card.getValue());
                        value.setText(val);
                        name.setText(user.getEmail());
                        String price = Double.toString(card.getPrice());
                        buyAt.setText(price);
                        editBtn.setEnabled(true);
                        if (user.getEmail().equals(Model.instance.getSignedUser().getEmail())) {
                            deleteBtn.setVisibility(View.VISIBLE);
                            editBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        ///////////////////////////////////////


        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewBuyCardDialog();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteBtn.setEnabled(false);
                card.setIsDeleted(true);
                Model.instance.updateCardRetrofit(card.getId(), card.toMap(), new Model.cardReturnListener() {
                    @Override
                    public void onComplete(Card card, String message) {
                        Navigation.findNavController(view).navigateUp();
                        Snackbar mySnackbar = Snackbar.make(view, "GiftCard Deleted!", BaseTransientBottomBar.LENGTH_LONG);
                        mySnackbar.show();
                    }
                });

            }
        });

//        mapBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String[] cordinate = card.getLatAndLong().split(",");
//                String uri = "http://maps.google.com/maps?q=loc:" + cordinate[0] + "," + cordinate[1];
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                intent.setPackage("com.google.android.apps.maps");
//                startActivity(intent);
//            }
//        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(CardsDetailsFragmentDirections.actionCardsDetailsFragmentToEditCardsDetailsFragment(card.getId()));
            }
        });


        return view;
    }

    private void setCardImage(ImageView image) {
        imageUrl = null;
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).into(image);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setClipToOutline(true);
        }
        //Picasso.get().load(card.getCardType()).into(giftCardImage);
    }

    public void createNewBuyCardDialog(){
        alertDialogBuilder = new AlertDialog.Builder(getContext());
        final View buyCardpopUpView = getLayoutInflater().inflate(R.layout.buy_card_popup,null);
        popUpCcardImage = buyCardpopUpView.findViewById(R.id.buy_card_popup_icon_iv);
        popUpPriceTv = buyCardpopUpView.findViewById(R.id.buy_card_popup_price_tv);
        popUpSaveBtn = buyCardpopUpView.findViewById(R.id.buy_card_popup_buy_btn);
        popUpCancel = buyCardpopUpView.findViewById(R.id.buy_card_popup_cancel_btn);
        popupValueTv =     buyCardpopUpView.findViewById(R.id.buy_card_popup_value_tv);
        popUpExpTv = buyCardpopUpView.findViewById(R.id.buy_card_popup_exp_tv);

        //Picasso.get().load(giftCard.getGiftCardImageUrl()).into(popUpCcardImage);
        setCardImage(popUpCcardImage);
        popUpPriceTv.setText(Double.toString(card.getPrice()));
        popupValueTv.setText(Double.toString(card.getValue()));
        popUpExpTv.setText(card.getExpirationDate().toString());

        alertDialogBuilder.setView(buyCardpopUpView);
        dialog = alertDialogBuilder.create();
        dialog.show();

        popUpSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        popUpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
}