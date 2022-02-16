package com.example.anygift;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.anygift.model.GiftCard;

import java.util.List;


public class EditCardDetailsFragment extends Fragment {
    View view;
    EditText value;
    EditText buyAt;
    Button save;
    ImageView giftCardImage;
    ImageButton uploadPic;
    UserViewModel userViewModel;
    MyCardsViewModel viewModel;
    GiftCard giftCard = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_edit_card_details, container, false);
        getActivity().setTitle("AnyGift - EditCardDetails");
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        String giftCardId = EditCardDetailsFragmentArgs.fromBundle(getArguments()).getGiftCardId();
        viewModel = new ViewModelProvider(this).get(MyCardsViewModel.class);
        List<GiftCard> list = viewModel.getList().getValue();

        for (GiftCard gc : list) {
            if (gc.getId().equals(giftCardId))
                giftCard = gc;
        }
        value = view.findViewById(R.id.editDetails_giftvalue_tv);
        buyAt = view.findViewById(R.id.editDetails_buyatval_tv);
        uploadPic = view.findViewById(R.id.take_pic_IB);
        save = view.findViewById(R.id.editDetails_save_btn);
        giftCardImage = view.findViewById(R.id.editDetails_giftpic_iv);

        return view;
    }
}