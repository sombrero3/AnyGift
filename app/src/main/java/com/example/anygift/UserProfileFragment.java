package com.example.anygift;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.anygift.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserProfileFragment extends Fragment {

    View view;
    Button editbtn;
    TextView name, phone, email, cardsNumber;
    UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        editbtn = view.findViewById(R.id.profileF_editInfoBtn);
        name = view.findViewById(R.id.profileF_name);
        phone = view.findViewById(R.id.profileF_phone);
        email = view.findViewById(R.id.profileF_mail);
        cardsNumber = view.findViewById(R.id.profileF_cards);
      /*  FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser userA= auth.getCurrentUser();*/
       userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
       userViewModel.getUser(new UserViewModel.GetUserListener() {
           @Override
           public void onComplete(User user) {
               name.setText((user!=null)?user.getName():"null");
               email.setText((user!=null)?user.getEmail():"null");
               phone.setText((user!=null)?user.getPhone():"null");
           }
       });

        editbtn.setOnClickListener((v) -> {
            Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_editProfileFragment);
        });

        return view;
    }

}