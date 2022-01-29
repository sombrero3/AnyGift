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
import com.google.firebase.auth.FirebaseAuth;

public class CardsDetailsFragment extends Fragment {
  View view;
  FeedViewModel viewModel;
  UserViewModel userViewModel;
  private TextView name;
  private TextView value;
  private TextView buyAt;
  private Button editBtn;
  private Button stores;
  private ImageView userImage;

    public CardsDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_cards_details, container, false);
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        userViewModel=new ViewModelProvider(this).get(UserViewModel.class);
        int giftCardId =CardsDetailsFragmentArgs.fromBundle(getArguments()).getGiftCardId();
        GiftCard giftCard = viewModel.getList().getValue().get(giftCardId);

        //toDo:checking
        if(viewModel.getList().getValue().get(giftCardId).getOwnerEmail().equals(FirebaseAuth.getInstance().getUid())) {
            editBtn.setVisibility(View.VISIBLE);
        }



        //toDo:checking
        name=view.findViewById(R.id.details_username_tv);
        name.setText(userViewModel.getUserName());
        value=view.findViewById(R.id.details_value_tv);
        String val=Double.toString(viewModel.getList().getValue().get(giftCardId).getValue());
        value.setText(val);
        buyAt=view.findViewById(R.id.details_buyatval_tv);
        String price=Double.toString(viewModel.getList().getValue().get(giftCardId).getWantedPrice());
        buyAt.setText(price);
        editBtn=view.findViewById(R.id.details_edit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardsDetailsFragmentDirections.ActionCardsDetailsFragmentToEditCardsDetailsFragment toEditDetails = CardsDetailsFragmentDirections.actionCardsDetailsFragmentToEditCardsDetailsFragment(giftCardId);
                Navigation.findNavController(v).navigate(toEditDetails);
            }
        });

        stores=view.findViewById(R.id.details_stores_btn);

        return view;
    }
}