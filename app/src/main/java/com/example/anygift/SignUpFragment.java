package com.example.anygift;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.room.PrimaryKey;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.anygift.model.Model;
import com.example.anygift.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpFragment extends Fragment {

    User user;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_sign_up, container, false);
        firstName=view.findViewById(R.id.SignUp_firstName_input);
        lastName=view.findViewById(R.id.SignUp_lastName_input);
        phone=view.findViewById(R.id.SignUp_phone_input);
        email=view.findViewById(R.id.SignUp_email_input);
        password=view.findViewById(R.id.SignUp_password_input);
        signIn_btn=view.findViewById(R.id.SignUp_signIn_btn);
        continue_btn=view.findViewById(R.id.SignUp_continue_btn);
        address=view.findViewById(R.id.SignUp_address_input);
        terms=view.findViewById(R.id.SignUp_check_box);
        signIn_btn.setTypeface(Typeface.SANS_SERIF);
        continue_btn.setTypeface(Typeface.SANS_SERIF);

        continue_btn.setOnClickListener(v -> {
            save();
        });

        signIn_btn.setOnClickListener(v ->{
            Navigation.findNavController(v).navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment());
        } );
        return view;
    }

    public void save(){
        continue_btn.setEnabled(false);
        signIn_btn.setEnabled(false);
        String Fname=firstName.getText().toString();
        String Lname=lastName.getText().toString();
        String phone_usr=phone.getText().toString();
        String email_usr=email.getText().toString();
        String address_usr=address.getText().toString();
        String password_usr=password.getText().toString();
        boolean flag = terms.isChecked();
        Log.d("TAG","saved name:" + Fname + " id:" + Lname + " flag:" + flag);
        user = new User(Fname,Lname,phone_usr,email_usr,address_usr,password_usr);
        Model.instance.addUser(user,() -> {
             Navigation.findNavController(view).navigate(SignUpFragmentDirections.actionSignUpFragmentToUserProfileFragment());
        });
        Log.d("TAG", "successful");
    }
}