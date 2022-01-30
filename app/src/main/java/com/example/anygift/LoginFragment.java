package com.example.anygift;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    public FirebaseAuth mAuth= FirebaseAuth.getInstance();

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

        String email_usr=email.getText().toString();
        String password_usr= password.getText().toString();

        signIn_btn.setOnClickListener(v -> {
         FirebaseUser user = mAuth.getCurrentUser();
            /*if (user!=null) {
                if (user.getEmail().equals(email_usr))
                Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToUserProfileFragment());
            }
            else
                Log.d("TAG", "wrong details");*/
            Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToUserProfileFragment());
        });

        signUp_btn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment());
        });

        forgotP_btn.setOnClickListener(v -> {forgotPassword();});
        return view;
    }


    public void forgotPassword(){
       // FireBaseAuth auth= FireBaseAuth.get
        Log.d("TAG","forgot password");
    }
}

/*

nee to implement with firebase that the user is in the users database.
 */