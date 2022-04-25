package com.example.anygift.feed;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.User;
import com.example.anygift.model.Model;
import com.example.anygift.model.Utils;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CardsDetailsFragment extends Fragment {
    View view;
    //    UserViewModel userViewModel;
    private TextView name, value, expTv, buyAt, typeTv, popUpTypeTv, popUpExpTv, popupValueTv, popUpPriceTv, emailTv, savingTv;
    private Button mapBtn, editBtn, deleteBtn, buyBtn, popUpSaveBtn, popUpCancel;
    private ImageView userImage, giftCardImage, popUpCcardImage;
    Card card;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog dialog;
    String imageUrl, cardId, userId;
    ProgressBar pb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        Testing testing = new Testing();
        //getActivity().setTitle("AnyGift - CardsDetails");
        view = inflater.inflate(R.layout.fragment_cards_details, container, false);
        cardId = CardsDetailsFragmentArgs.fromBundle(getArguments()).getGiftCardId();

        pb = view.findViewById(R.id.details_pb);
        pb.setVisibility(View.VISIBLE);

        userImage = view.findViewById(R.id.card_details_user_pic_iv);
        name = view.findViewById(R.id.card_details_username_tv);
        value = view.findViewById(R.id.details_giftvalue_tv);
        mapBtn = view.findViewById(R.id.cardDetails_mapBtn);
        buyAt = view.findViewById(R.id.details_buyatval_tv);
        deleteBtn = view.findViewById(R.id.details_delete_btn);
        editBtn = view.findViewById(R.id.details_edit_btn2);
        giftCardImage = view.findViewById(R.id.details_giftpic_iv);
        buyBtn = view.findViewById(R.id.card_details_buy_btn);
        emailTv = view.findViewById(R.id.card_details_email_tv);
        savingTv = view.findViewById(R.id.details_saving_tv);
        typeTv = view.findViewById(R.id.card_details_type_tv);
        expTv = view.findViewById(R.id.card_details_exp_tv);
        Model.instance.getCardRetrofit(cardId, new Model.cardReturnListener() {
            @Override
            public void onComplete(Card c, String message) {
                card = c;
                Model.instance.getUserRetrofit(card.getOwner(), new Model.userReturnListener() {
                    @Override
                    public void onComplete(com.example.anygift.Retrofit.User user, String message) {
                        userId = user.getId();
                        value.setText(Double.toString(card.getValue()));
                        name.setText(user.getFirstName() + " " + user.getLastName());
                        emailTv.setText(user.getEmail());
                        expTv.setText(Utils.ConvertLongToDate(card.getExpirationDate()));
                        buyAt.setText(Double.toString(card.getPrice()));
                        savingTv.setText(card.getPrecentageSaved() + "%");
                        for (CardType ct : Model.instance.cardTypes) {
                            if (ct.getId().equals(card.getCardType())) {
                                typeTv.setText(ct.getName());
                            }
                        }
                        Model.instance.downloadImage(user.getProfilePicture().replace("/image/", ""),
                                new Model.byteArrayReturnListener() {
                                    @Override
                                    public void onComplete(Bitmap bitmap) {
                                        if (bitmap != null) {
                                            userImage.setImageBitmap(bitmap);
                                            userImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                            userImage.setClipToOutline(true);
                                        }
                                        setUI();
                                    }
                                });

                    }
                });
            }
        });


        return view;
    }

    private void setUI() {
        if (userId.equals(Model.instance.getSignedUser().getId())) {
            deleteBtn.setEnabled(true);
            deleteBtn.setVisibility(View.VISIBLE);
            editBtn.setEnabled(true);
            editBtn.setVisibility(View.VISIBLE);

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Navigation.findNavController(view).navigate(CardsDetailsFragmentDirections.actionCardsDetailsFragmentToEditCardsDetailsFragment(card.getId()));
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
        } else {
            buyBtn.setEnabled(true);
            buyBtn.setVisibility(View.VISIBLE);

            buyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createNewBuyCardDialog();
                }
            });
        }
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

        setCardImage(giftCardImage);
        pb.setVisibility(View.GONE);
    }

    private void setCardImage(ImageView image) {
        for (CardType ct : Model.instance.cardTypes) {
            if (ct.getId().equals(card.getCardType())) {
                image.setImageBitmap(ct.getPicture());
            }
        }
    }

    public void createNewBuyCardDialog() {
        alertDialogBuilder = new AlertDialog.Builder(getContext());
        final View buyCardpopUpView = getLayoutInflater().inflate(R.layout.buy_card_popup, null);
        popUpCcardImage = buyCardpopUpView.findViewById(R.id.buy_card_popup_icon_iv);
        popUpPriceTv = buyCardpopUpView.findViewById(R.id.buy_card_popup_price_tv);
        popUpSaveBtn = buyCardpopUpView.findViewById(R.id.buy_card_popup_buy_btn);
        popUpCancel = buyCardpopUpView.findViewById(R.id.buy_card_popup_cancel_btn);
        popupValueTv = buyCardpopUpView.findViewById(R.id.buy_card_popup_value_tv);
        popUpExpTv = buyCardpopUpView.findViewById(R.id.buy_card_popup_exp_tv);
        popUpTypeTv = buyCardpopUpView.findViewById(R.id.buy_card_popup_card_type_tv);

        //Picasso.get().load(giftCard.getGiftCardImageUrl()).into(popUpCcardImage);
        setCardImage(popUpCcardImage);
        popUpPriceTv.setText(Double.toString(card.getPrice()));
        popupValueTv.setText(Double.toString(card.getValue()));
        popUpExpTv.setText(Utils.ConvertLongToDate(card.getExpirationDate()));

        for (CardType ct : Model.instance.cardTypes) {
            if (ct.getId().equals(card.getCardType())) {
                popUpTypeTv.setText(ct.getName());
            }
        }
        alertDialogBuilder.setView(buyCardpopUpView);
        dialog = alertDialogBuilder.create();
        dialog.show();

        //buy
        popUpSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Model.instance.getSignedUser().getCoins() < card.getCalculatedPrice()) {
                    Toast.makeText(getContext(), "You do not have enough coins!", Toast.LENGTH_SHORT).show();
                } else {
                    String buyer = Model.instance.getSignedUser().getId();
                    String seller = card.getOwner();
                    double coins = card.getCalculatedPrice();
                    transacteCard(seller, buyer, card.getId(), coins);
                }
            }
        });


        popUpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void transacteCard(String fromID, String toID, String cardID, Double coins) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("owner", toID);
        HashMap<String, Object> updateMap = Card.mapToUpdateCard(cardID, map); //update card owner.
        Model.instance.updateCardRetrofit(cardID, updateMap, new Model.cardReturnListener() {
            @Override
            public void onComplete(Card card, String message) {
                transferCoins(fromID, toID, coins);
            }
        });

    }

    public void transferCoins(String sellerID, String buyerID, Double coins) {
        Model.instance.addCoinsToUser(buyerID, -coins, new Model.userReturnListener() {
            @Override
            public void onComplete(User user, String message) {
                System.out.println(user);
                Model.instance.addCoinsToUser(sellerID, coins, new Model.userReturnListener() {
                    @Override
                    public void onComplete(User user, String message) {
                        System.out.println(user);
                        Toast.makeText(getContext(), "Enjoy your New GiftCard!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(FeedFragmentDirections.actionGlobalMyCardsFragment());
                    }
                });


            }
        });

    }

}