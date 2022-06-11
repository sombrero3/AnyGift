package com.example.anygift.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.room.PrimaryKey;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.anygift.R;
import com.example.anygift.model.Model;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SignUpFragment extends Fragment {

    View view;

    @NonNull
    TextInputEditText firstName;
    @NonNull
    TextInputEditText lastName;
    @PrimaryKey
    @NonNull
    TextInputEditText email;
    @NonNull
    TextInputEditText phone;
    @NonNull
    TextInputEditText password;
    @NonNull
    TextInputEditText address;

    Button signIn_btn;
    Button continue_btn;
    CheckBox terms;

    String Fname;
    String Lname;
    String phone_usr;
    String email_usr;
    String address_usr;
    String password_usr;
    FusedLocationProviderClient client;
    String latAndLong;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //getActivity().setTitle("AnyGift - SignUp");
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        firstName = view.findViewById(R.id.SignUp_firstName_input);
        lastName = view.findViewById(R.id.SignUp_lastName_input);
        phone = view.findViewById(R.id.SignUp_phone_input);
        email = view.findViewById(R.id.SignUp_email_input);
        password = view.findViewById(R.id.SignUp_password_input);
        signIn_btn = view.findViewById(R.id.SignUp_signIn_btn);
        continue_btn = view.findViewById(R.id.SignUp_continue_btn);
        address = view.findViewById(R.id.SignUp_address_input);

        terms = view.findViewById(R.id.SignUp_check_box);
        signIn_btn.setTypeface(Typeface.SANS_SERIF);
        continue_btn.setTypeface(Typeface.SANS_SERIF);
        continue_btn.setOnClickListener(v -> {
            save();
        });
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        signIn_btn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment());
        });

        String userEmail=getArguments().getString("userEmail");
        if(userEmail!=null){
            email.setText(userEmail);
        }
        return view;
    }

    public void save() {
        continue_btn.setEnabled(false);
        signIn_btn.setEnabled(false);
        Fname = firstName.getText().toString();
        Lname = lastName.getText().toString();
        phone_usr = phone.getText().toString();
        email_usr = email.getText().toString();
        address_usr = address.getText().toString();
        password_usr = password.getText().toString();
        boolean flag = terms.isChecked();


        if (Fname == null || Lname == null || address_usr == null) {
            firstName.setError("You must enter your first name");
            lastName.setError("You must ennte your last name");
            address.setError(" You must enter your address");
            continue_btn.setEnabled(true);
            return;
        }

        if (password_usr.length() < 6) {
            password.setError("Password must be at least 6 characters");
            continue_btn.setEnabled(true);
            return;
        }

        if (flag == false) {
            terms.setError("You must agree our terms :)");
            continue_btn.setEnabled(true);
            return;
        }


        HashMap<String, Object> map = com.example.anygift.Retrofit.User.mapToAddUser(Fname, Lname,
                email_usr, password_usr, address_usr, "11", phone_usr, false);
        Model.instance.addUserRetrofit(map, new Model.userReturnListener() {
            @Override
            public void onComplete(com.example.anygift.Retrofit.User user, String message) {
                System.out.println(user);
                if (user == null) {
                    Snackbar mySnackbar = Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_LONG);
                    mySnackbar.show();
                    continue_btn.setEnabled(true);

                } else {
                    Snackbar mySnackbar = Snackbar.make(view, "signUp succeed, Nice to meet you " + Fname + " :)", BaseTransientBottomBar.LENGTH_LONG);
                    mySnackbar.show();
                    Navigation.findNavController(view).navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment());
                }
            }
        });

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

}