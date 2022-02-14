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

    public CardsDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cards_details, container, false);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        int giftCardId = CardsDetailsFragmentArgs.fromBundle(getArguments()).getGiftCardId();
        GiftCard giftCard = viewModel.getList().getValue().get(giftCardId);
        //toDo:checking
        name = view.findViewById(R.id.details_username_tv);
        name.setText(giftCard.getOwnerEmail());
        value = view.findViewById(R.id.details_giftvalue_tv);
        String val = Double.toString(viewModel.getList().getValue().get(giftCardId).getValue());
        value.setText(val);
        buyAt = view.findViewById(R.id.details_buyatval_tv);
        String price = Double.toString(viewModel.getList().getValue().get(giftCardId).getWantedPrice());
        buyAt.setText(price);
        deleteBtn = view.findViewById(R.id.details_delete_btn);
        if (giftCard.getOwnerEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            deleteBtn.setVisibility(View.VISIBLE);
        }


        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteBtn.setEnabled(false);
                GiftCard gc=viewModel.getList().getValue().get(giftCardId);
                gc.setDeleted(true);
                Model.instance.modelFirebase.addGiftCard(gc, new Model.AddGiftCardListener() {
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