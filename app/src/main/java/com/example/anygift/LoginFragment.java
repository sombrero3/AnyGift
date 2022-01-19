package com.example.anygift;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;


public class LoginFragment extends Fragment {

    Button signIn_btn;
    Button signUp_btn;
    Button forgotP_btn;
    TextInputEditText email;
    TextInputEditText password;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        signIn_btn=view.findViewById(R.id.Login_signIn_btn);
        signUp_btn=view.findViewById(R.id.Login_btn_signUp);
        forgotP_btn=view.findViewById(R.id.Login_btn_forgotp);
        email=view.findViewById(R.id.Login_email_input);
        password=view.findViewById(R.id.Login_password_input);
        signIn_btn.setTypeface(Typeface.SANS_SERIF);
        signUp_btn.setTypeface(Typeface.SANS_SERIF);
        forgotP_btn.setTypeface(Typeface.SANS_SERIF);
        return view;
    }
}

/*

nee to implement with firebase that the user is in the users database.
 */