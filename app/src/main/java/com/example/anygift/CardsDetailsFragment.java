package com.example.anygift;

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

import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardsDetailsFragment extends Fragment {
    View view;
    FeedViewModel viewModel;
    UserViewModel userViewModel;
    private TextView name;
    private TextView value;
    private TextView buyAt;
    private Button deleteBtn;
    private Button stores;
    private ImageView userImage;
    private ImageView giftCardImage;
    GiftCard giftCard = null;
    public CardsDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cards_details, container, false);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        String giftCardId = CardsDetailsFragmentArgs.fromBundle(getArguments()).getGiftCardId();
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        List<GiftCard> list=viewModel.getList().getValue();

        for(GiftCard gc:list){
            if(gc.getId().equals(giftCardId))
                giftCard=gc;
        }

        //toDo:checking
        name = view.findViewById(R.id.details_username_tv);
        name.setText(giftCard.getOwnerEmail());
        value = view.findViewById(R.id.details_giftvalue_tv);
        String val = Double.toString(giftCard.getValue());
        value.setText(val);
        buyAt = view.findViewById(R.id.details_buyatval_tv);
        String price = Double.toString(giftCard.getWantedPrice());
        buyAt.setText(price);
        giftCardImage=view.findViewById(R.id.details_giftpic_iv);
        Picasso.get().load(giftCard.getGiftCardImageUrl()).into(giftCardImage);
        deleteBtn = view.findViewById(R.id.details_delete_btn);
        if (giftCard.getOwnerEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            deleteBtn.setVisibility(View.VISIBLE);
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

//        stores = view.findViewById(R.id.details_stores_btn);

        return view;
    }
}