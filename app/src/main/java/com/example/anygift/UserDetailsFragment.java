package com.example.anygift;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class UserDetailsFragment extends Fragment {

   View view;
    TextView name, phone, email;
    Button MapBtn;
    String latAndLong=new String();
    ImageView profileImage;
    UserViewModel userViewModel;
    public UserDetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_user_details, container, false);
        getActivity().setTitle("AnyGift - Other Profile");
        name = view.findViewById(R.id.UserprofileF_name);
        phone = view.findViewById(R.id.UserprofileF_phone);
        email = view.findViewById(R.id.UserprofileF_mail);
        profileImage=view.findViewById(R.id.UserprofileF_imageView);

        return view;
    }
}