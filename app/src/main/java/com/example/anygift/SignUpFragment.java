package com.example.anygift;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpFragment extends Fragment {


    TextInputEditText firstName;
    TextInputEditText lastName;
    TextInputEditText email;
    TextInputEditText phone;
    TextInputEditText password;
    Button signIn_btn;
    Button continue_btn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);
        firstName=view.findViewById(R.id.SignUp_firstName_input);
        lastName=view.findViewById(R.id.SignUp_lastName_input);
        phone=view.findViewById(R.id.SignUp_phone_input);
        email=view.findViewById(R.id.SignUp_email_input);
        password=view.findViewById(R.id.Login_password_input);
        signIn_btn=view.findViewById(R.id.SignUp_signIn_btn);
        continue_btn=view.findViewById(R.id.SignUp_continue_btn);

        signIn_btn.setTypeface(Typeface.SANS_SERIF);
        continue_btn.setTypeface(Typeface.SANS_SERIF);

        return view;
    }
}