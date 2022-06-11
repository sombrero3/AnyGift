package com.example.anygift.feed;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardTransaction;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.SellerRatings;
import com.example.anygift.Retrofit.Store;
import com.example.anygift.Retrofit.User;
import com.example.anygift.model.Model;
import com.example.anygift.model.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CardsDetailsFragment extends Fragment {
    View view;
    private TextView name, value, expTv, buyAt, typeTv, popUpTypeTv, popUpExpTv, popupValueTv, popUpPriceTv, emailTv, savingTv, askedPriceTv, storesTv,numLikeTv,numUnlikeTv,coinsTv,popUpSavingTv;
    private Button btnCloseStore, editBtn, deleteBtn, buyBtn, popUpSaveBtn, popUpCancel, popUpStoreBtn;
    NavigationView navigationView;
    private ImageView userImage, giftCardImage, popUpCcardImage,verifiedIv;
    Card card;
    String cardId, userId;
    ProgressBar pb;
    Dialog tryDialog;
    //gps
    FusedLocationProviderClient client;
    String latAndLong;
    String[] listFromJSON=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        Testing testing = new Testing();
        //getActivity().setTitle("AnyGift - CardsDetails");
        // card_details_stores_tv
        view = inflater.inflate(R.layout.fragment_cards_details, container, false);
        cardId = CardsDetailsFragmentArgs.fromBundle(getArguments()).getGiftCardId();

        pb = view.findViewById(R.id.details_pb);
        pb.setVisibility(View.VISIBLE);

        userImage = view.findViewById(R.id.card_details_user_pic_iv);
        name = view.findViewById(R.id.card_details_username_tv);
        value = view.findViewById(R.id.details_giftvalue_tv);
        buyAt = view.findViewById(R.id.details_buyatval_tv);
        storesTv = view.findViewById(R.id.card_details_stores_tv);
        deleteBtn = view.findViewById(R.id.details_delete_btn);
        editBtn = view.findViewById(R.id.details_edit_btn2);
        giftCardImage = view.findViewById(R.id.details_giftpic_iv);
        buyBtn = view.findViewById(R.id.card_details_buy_btn);
        emailTv = view.findViewById(R.id.card_details_email_tv);
        savingTv = view.findViewById(R.id.details_saving_tv);
        typeTv = view.findViewById(R.id.card_details_type_tv);
        expTv = view.findViewById(R.id.card_details_exp_tv);
        askedPriceTv = view.findViewById(R.id.details_asked_price_tv);
        numLikeTv = view.findViewById(R.id.details_num_like_tv);
        numUnlikeTv = view.findViewById(R.id.details_num_unlike_tv);
        verifiedIv = view.findViewById(R.id.details_verified_iv);
        Model.instance.getCardRetrofit(cardId, new Model.cardReturnListener() {
            @Override
            public void onComplete(Card c, String message) {
                card = new Card();
                card = c;
                for(CardType ct:Model.instance.cardTypes){
                    if(c.getCardType().equals(ct.getId())){
                        List<String> tempList=new ArrayList<>();
                        for(int i=0;i<ct.getStores().size();i++){
                            tempList.addAll(ct.getStores().get(i).getLocations());
                        }
                        listFromJSON = new String[tempList.size()];
                        listFromJSON = tempList.toArray(listFromJSON);
                    }
                }

                Model.instance.getUserRetrofit(card.getOwner(), new Model.userReturnListener() {
                    @Override
                    public void onComplete(User user, String message) {
                        userId = user.getId();
                        value.setText(Double.toString(card.getValue()));
                        name.setText(user.getFirstName() + " " + user.getLastName());
                        emailTv.setText(user.getEmail());
                        expTv.setText(Utils.ConvertLongToDate(card.getExpirationDate()));
                        askedPriceTv.setText(Double.toString(card.getPrice()));
                        buyAt.setText(Double.toString(card.getCalculatedPrice()));
                        savingTv.setText(card.getPrecentageSaved() + "%");
                        for (CardType ct : Model.instance.cardTypes) {
                            if (ct.getId().equals(card.getCardType())) {
                                typeTv.setText(ct.getName());
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    List<String> ls = new ArrayList<>();
                                    for(Store st: ct.getStores()){
                                        ls.add(st.getName());
                                    }
                                    storesTv.setText(String.join(", ",ls));
                                }
                            }
                        }
                        if(user.getVerified()){
                            verifiedIv.setVisibility(View.VISIBLE);
                        }

                        Model.instance.getSellerRatings(card.getOwner(), new Model.sellerRatingsListener() {
                            @Override
                            public void onComplete(SellerRatings sr) {
                                numLikeTv.setText(""+sr.getGood());
                                numUnlikeTv.setText(""+sr.getBad());

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
            }
        });
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        btnCloseStore=view.findViewById(R.id.btnCloseStore);
        btnCloseStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //find closet store
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();



                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

                }

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
                    createNewBottomDialog();
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

    public void createNewBottomDialog() {
        tryDialog = new Dialog(getActivity());
        tryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tryDialog.setContentView(R.layout.buy_card_popup);

        popUpCcardImage = tryDialog.findViewById(R.id.buy_card_popup_icon_iv);
        popUpPriceTv = tryDialog.findViewById(R.id.buy_card_popup_price_tv);
        popUpSaveBtn = tryDialog.findViewById(R.id.buy_card_popup_buy_btn);
        popUpSavingTv = tryDialog.findViewById(R.id.buy_card_popup_saving_tv);
        popUpCancel = tryDialog.findViewById(R.id.buy_card_popup_cancel_btn);
        popupValueTv = tryDialog.findViewById(R.id.buy_card_popup_value_tv);
        popUpExpTv = tryDialog.findViewById(R.id.buy_card_popup_exp_tv);
        popUpTypeTv = tryDialog.findViewById(R.id.buy_card_popup_card_type_tv);
        popUpStoreBtn = tryDialog.findViewById(R.id.buy_card_popup_coins_btn);

        setCardImage(popUpCcardImage);
        popUpPriceTv.setText(Double.toString(card.getCalculatedPrice()));
        popupValueTv.setText(Double.toString(card.getValue()));
        popUpSavingTv.setText(card.getPrecentageSaved());
        popUpExpTv.setText(Utils.ConvertLongToDate(card.getExpirationDate()));

        for (CardType ct : Model.instance.cardTypes) {
            if (ct.getId().equals(card.getCardType())) {
                popUpTypeTv.setText(ct.getName());
            }
        }

        //go get more coins
        popUpStoreBtn.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("cardId", cardId);
            tryDialog.dismiss();
            Navigation.findNavController(view).navigate(R.id.action_global_shopFragment, args);
        });

        //buy
        popUpSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Model.instance.getSignedUser().getCoins() < card.getCalculatedPrice()) {
                    Toast.makeText(getContext(), "You do not have enough coins!", Toast.LENGTH_SHORT).show();
                    popUpStoreBtn.setVisibility(View.VISIBLE);
                } else {
                    pb.setVisibility(View.VISIBLE);
                    String buyer = Model.instance.getSignedUser().getId();
                    String buyerEmail = Model.instance.getSignedUser().getEmail();
                    String seller = card.getOwner();
                    Model.instance.getUserRetrofit(seller, new Model.userReturnListener() {
                        @Override
                        public void onComplete(User user, String message) {
                            String sellerEmail = user.getEmail();
                            double cardPrice = card.getCalculatedPrice();
                            double cardValue = card.getValue();
                            String cardType = card.getCardType();
                            // sellerId, sellerEmail,buyerId,buyerEmail,CardId,CardTypeId,cardPrice,cardValue
                            transactCard(seller, sellerEmail, buyer, buyerEmail, card.getId(), cardType, cardPrice, cardValue);

                        }
                    });
                }
            }
        });

        popUpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryDialog.dismiss();
            }
        });
        tryDialog.show();
        tryDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tryDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        tryDialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    // sellerId, sellerEmail,buyerId,buyerEmail,CardId,CardTypeId,cardPrice,cardValue
    public void transactCard(String fromID, String fromEmail, String toID, String toEmail,
                             String cardID, String cardTypeId, Double cardPrice, Double cardValue) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("owner", toID);
        HashMap<String, Object> updateMap = Card.mapToUpdateCard(cardID, map); //update card owner.
        Model.instance.updateCardRetrofit(cardID, updateMap, new Model.cardReturnListener() {
            @Override
            public void onComplete(Card card, String message) {
                transferCoins(fromID, fromEmail, toID, toEmail, cardID, cardTypeId, cardPrice, cardValue);
            }
        });

    }

    public void transferCoins(String fromID, String fromEmail, String toID, String toEmail,
                              String cardID, String cardTypeId, Double cardPrice, Double cardValue) {
        Model.instance.addCoinsToUser(toID, -cardPrice, new Model.userReturnListener() {
            @Override
            public void onComplete(User user, String message) {
                System.out.println(user);
                Model.instance.addCoinsToUser(fromID, cardPrice, new Model.userReturnListener() {
                    @Override
                    public void onComplete(User user, String message) {
                        HashMap<String, Object> map = com.example.anygift.Retrofit.CoinTransaction.mapToAddCoinTransaction(toID, fromID, cardPrice);
                        Model.instance.addCoinTransaction(map, new Model.coinTransactionListener() {
                            @Override
                            public void onComplete(String message) {
                                System.out.println(message);
                                addCardTransaction(fromID, fromEmail, toID, toEmail,
                                        cardID, cardTypeId, cardPrice, cardValue);
                            }
                        });

                    }
                });


            }
        });

    }

    public void addCardTransaction(String fromID, String fromEmail, String toID, String toEmail,
                                   String cardID, String cardTypeId, Double cardPrice, Double cardValue) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("seller", fromID);
        map.put("sellerEmail", fromEmail);
        map.put("buyer", toID);
        map.put("buyerEmail", toEmail);
        map.put("card", cardID);
        map.put("boughtFor", cardPrice);
        map.put("cardValue", cardValue);
        map.put("cardType", cardTypeId);
        Model.instance.addCardTransaction(map, new Model.booleanReturnListener() {
            @Override
            public void onComplete(Boolean cardTransaction, String message) {

                Toast.makeText(getContext(), "Enjoy your New GiftCard!", Toast.LENGTH_SHORT).show();
                // Model.instance.setCurrentUser(user);


//                Model.instance.addCoinsToUser(Model.instance.getSignedUser().getId(), (-1) * (card.getCalculatedPrice()), new Model.userReturnListener() {
//                    @Override
//                    public void onComplete(User user, String message) {
//                        Model.instance.getSignedUser().setCoins(user.getCoins());
                        double dub = card.getCalculatedPrice();
                        int price = (int)dub;
                        Model.instance.getSignedUser().setCoins(Model.instance.getSignedUser().getCoins()-price);
                        navigationView = getActivity().findViewById(R.id.Navigation_view);
                        View header= (View)navigationView.getHeaderView(0);
                        coinsTv = header.findViewById(R.id.header_coins_tv);
                        coinsTv.setText(Model.instance.getSignedUser().getCoins().toString());
                        pb.setVisibility(View.INVISIBLE);
                        tryDialog.dismiss();
                        Navigation.findNavController(view).navigate(R.id.action_global_myCardsFragment);
//                    }
//                });
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = (Location) task.getResult();
                    if (location != null) {
                        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if (addresses.size() > 0) {

                                latAndLong = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                                latAndLong="31.879926029240508, 34.73570520900597";
                                int index= Utils.MinDistance(listFromJSON,latAndLong);
                                Uri gmmIntentUri = Uri.parse("geo:"+listFromJSON[index]);
                                String labelLocation = "Closet Store: ";
                                String address = "http://maps.google.com/maps?q="+listFromJSON[index] +"("+ labelLocation + ")&iwloc=A&hl=es";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
                                startActivity(intent);
                                /*  Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null)
                                 {
                                    startActivity(mapIntent);
                                }

                               */
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }
            });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }
}