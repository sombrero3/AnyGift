package com.example.anygift;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anygift.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserProfileFragment extends Fragment {

    View view;
    TextView name, phone, email,cardCounter,coinCounter;
    UserViewModel userViewModel;
    Button editBtn;
    ImageButton cardBtn,coinsBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        editBtn = view.findViewById(R.id.profileF_editInfoBtn);
        name = view.findViewById(R.id.profileF_name);
        phone = view.findViewById(R.id.profileF_phone);
        email = view.findViewById(R.id.profileF_mail);
        cardCounter=view.findViewById(R.id.profileF_cards);
        coinCounter=view.findViewById(R.id.profileF_coins);
        cardBtn=view.findViewById(R.id.profileF_cardsBtn);
        coinsBtn=view.findViewById(R.id.profileF_coinsBtn);

       userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
       userViewModel.getUser(new UserViewModel.GetUserListener() {
           @Override
           public void onComplete(User user) {
               name.setText((user!=null)?user.getName():"null");
               email.setText((user!=null)?user.getEmail():"null");
               phone.setText((user!=null)?user.getPhone():"null");
              cardCounter.setText((user.getGiftCards()!=null)?String.valueOf(user.getGiftCards().size()):"0");
               coinCounter.setText("0");
           }
       });

        editBtn.setOnClickListener((v) -> {
            Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_editProfileFragment);
        });
        cardBtn.setOnClickListener((v) -> {
           // Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_editProfileFragment);
        });
        coinsBtn.setOnClickListener((v) -> {
            Toast.makeText(getContext(),"Coming soon...", Toast.LENGTH_SHORT).show();
        });
        return view;
    }

}