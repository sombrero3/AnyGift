package com.example.anygift.feed;

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
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.Category;
import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;
import com.example.anygift.model.User;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardsDetailsFragment extends Fragment {
    View view;
    FeedViewModel viewModel;
    UserViewModel userViewModel;
    private TextView name;
    private TextView value;
    private TextView buyAt;
    private Button deleteBtn;
    private Button mapBtn, editBtn;
    private ImageView userImage;
    private ImageView giftCardImage;
    GiftCard giftCard = null;
    List<Category> categoryList;

    public CardsDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Model.instance.getAllCategories(new Model.categoriesReturnListener() {
            @Override
            public void onComplete(List<Category> cts)
            {
                categoryList = new ArrayList<>(cts);
                System.out.println(categoryList);
            }
        });

        Model.instance.getUserRetrofit("6252d5c4075b499f9d50bd8f", new Model.userReturnListener() {
            @Override
            public void onComplete(com.example.anygift.Retrofit.User user)
            {
                System.out.println(user);
            }
        });
//        HashMap<String,Object> map = com.example.anygift.Retrofit.User.mapToAddUser("","",)
//        Model.instance.addUserRetrofit(map, new Model.userReturnListener() {
//            @Override
//            public void onComplete(com.example.anygift.Retrofit.User user)
//            {
//                System.out.println(user);
//            }
//        });



        Model.instance.getAllCardTypes(new Model.cardTypesReturnListener() {
            @Override
            public void onComplete(List<CardType> cts)
            {
                System.out.println(cts);
            }
        });


        view = inflater.inflate(R.layout.fragment_cards_details, container, false);
        //getActivity().setTitle("AnyGift - CardsDetails");
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        String giftCardId = CardsDetailsFragmentArgs.fromBundle(getArguments()).getGiftCardId();
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);

        List<GiftCard> list = viewModel.getList().getValue();

        for (GiftCard gc : list) {
            if (gc.getId().equals(giftCardId))
                giftCard = gc;
        }
        userImage = view.findViewById(R.id.details_picture_iv);
        userViewModel.getUserById(giftCard.getOwnerEmail(), new UserViewModel.GetUserListener() {
            @Override
            public void onComplete(User user) {
                if (user.getImageUrl() != null) {
                    Picasso.get().load(user.getImageUrl()).into(userImage);
                    userImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    userImage.setClipToOutline(true);
                }
            }
        });
        name = view.findViewById(R.id.card_details_username_tv);
        name.setText(giftCard.getOwnerEmail());
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(CardsDetailsFragmentDirections.actionCardsDetailsFragmentToUserDetailsFragment(giftCard.getOwnerEmail()));


            }
        });
        value = view.findViewById(R.id.details_giftvalue_tv);
        mapBtn = view.findViewById(R.id.cardDetails_mapBtn);
        String val = Double.toString(giftCard.getValue());
        value.setText(val);
        buyAt = view.findViewById(R.id.details_buyatval_tv);
        String price = Double.toString(giftCard.getWantedPrice());
        buyAt.setText(price);
        giftCardImage = view.findViewById(R.id.details_giftpic_iv);
        Picasso.get().load(giftCard.getGiftCardImageUrl()).into(giftCardImage);
        deleteBtn = view.findViewById(R.id.details_delete_btn);
        editBtn = view.findViewById(R.id.details_edit_btn2);
        editBtn.setEnabled(true);
        if (giftCard.getOwnerEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            deleteBtn.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.VISIBLE);
        }


        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteBtn.setEnabled(false);
                giftCard.setDeleted(true);
                Model.instance.modelFirebase.addGiftCard(giftCard, new Model.AddGiftCardListener() {
                    @Override
                    public void onComplete() {
                        Navigation.findNavController(view).navigateUp();
                        Snackbar mySnackbar = Snackbar.make(view, "GiftCard Deleted!", BaseTransientBottomBar.LENGTH_LONG);
                        mySnackbar.show();
                    }
                });

            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] cordinate = giftCard.getLatAndLong().split(",");
                String uri = "http://maps.google.com/maps?q=loc:" + cordinate[0] + "," + cordinate[1];
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(CardsDetailsFragmentDirections.actionCardsDetailsFragmentToEditCardsDetailsFragment(giftCard.getId()));
            }
        });
        return view;
    }
}