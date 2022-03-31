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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anygift.R;
import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;
import com.example.anygift.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;


public class UserProfileFragment extends Fragment {

    View view;
    TextView name, phone, email, cardCounter, coinCounter;
    UserViewModel userViewModel;
    Button editBtn;
    Button MapBtn;
    ImageButton cardBtn, coinsBtn;
    String latAndLong = new String();
    ImageView profileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("AnyGift - Profile");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        editBtn = view.findViewById(R.id.profile_editInfo_btn);
        name = view.findViewById(R.id.profile_name_tv);
        phone = view.findViewById(R.id.profile_phone_tv);
        email = view.findViewById(R.id.profile_mail_tv);
        cardCounter = view.findViewById(R.id.profileF_cards);
        coinCounter = view.findViewById(R.id.profileF_coins);
        cardBtn = view.findViewById(R.id.profile_cards_ibtn);
        coinsBtn = view.findViewById(R.id.profile_coins_ibtn);
        MapBtn = view.findViewById(R.id.profile_map_btn);
        profileImage = view.findViewById(R.id.profileF_imageView);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUser(new UserViewModel.GetUserListener() {
            @Override
            public void onComplete(User user) {
                name.setText((user != null) ? user.getName() : "null");
                email.setText((user != null) ? user.getEmail() : "null");
                phone.setText((user != null) ? user.getPhone() : "null");
                if (user.getImageUrl() != null) {
                    Picasso.get().load(user.getImageUrl()).into(profileImage);
                    profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    profileImage.setClipToOutline(true);
                }
                latAndLong = user.getLatAndLong();
                int count = 0;
                List<GiftCard> giftCardList = Model.instance.getAll().getValue();
                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                for (GiftCard gc : giftCardList
                ) {
                    if (gc.getOwnerEmail().compareTo(userEmail) == 0 && !gc.getDeleted()) {
                        count++;
                    }
                }

                cardCounter.setText(String.valueOf(count));
                coinCounter.setText("0");
            }
        });

        editBtn.setOnClickListener((v) -> {
            Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_editProfileFragment);
        });
        cardBtn.setOnClickListener((v) -> {
            Navigation.findNavController(v).navigate(R.id.action_global_myCardsFragment);
        });
        coinsBtn.setOnClickListener((v) -> {
            Toast.makeText(getContext(), "Coming soon...", Toast.LENGTH_SHORT).show();
        });
        MapBtn.setOnClickListener((v) -> {
            String[] cordinate = latAndLong.split(",");
            String uri = "http://maps.google.com/maps?q=loc:" + cordinate[0] + "," + cordinate[1];
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });
        return view;
    }

}