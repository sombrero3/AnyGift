package com.example.anygift;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class UserProfileFragment extends Fragment {

    View view;
    Button editbtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_user_profile, container, false);
        editbtn=view.findViewById(R.id.profileF_editInfoBtn);
        editbtn.setOnClickListener((v)->{
            Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_editProfileFragment);
        });
        return view;
    }
}