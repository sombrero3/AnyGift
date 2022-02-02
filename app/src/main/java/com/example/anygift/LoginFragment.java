package com.example.anygift;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.anygift.model.Model;
import com.example.anygift.model.ModelFirebase;
import com.example.anygift.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth= FirebaseAuth.getInstance();

    View view;

    Button signIn_btn;
    Button signUp_btn;
    Button forgotP_btn;
    TextInputEditText email;
    TextInputEditText password;

    String email_user;
    String password_user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view= inflater.inflate(R.layout.fragment_login, container, false);
        signIn_btn=view.findViewById(R.id.Login_signIn_btn);
        signUp_btn=view.findViewById(R.id.Login_btn_signUp);
        forgotP_btn=view.findViewById(R.id.Login_btn_forgotp);
        email=view.findViewById(R.id.Login_email_input);
        password=view.findViewById(R.id.Login_password_input);

        signIn_btn.setTypeface(Typeface.SANS_SERIF);
        signUp_btn.setTypeface(Typeface.SANS_SERIF);
        forgotP_btn.setTypeface(Typeface.SANS_SERIF);

        signIn_btn.setOnClickListener(v -> {checkUser();});

        signUp_btn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment());
        });

        forgotP_btn.setOnClickListener(v -> {forgotPassword();});
        return view;
    }

    public void checkUser() {


        email_user=email.getText().toString();
        password_user=password.getText().toString();
        mAuth.signInWithEmailAndPassword(email_user,password_user).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    signIn_btn.setEnabled(false);
                    signUp_btn.setEnabled(false);
                    forgotP_btn.setEnabled(false);
                    //Toast.makeText(getApplicationContext(),"Login successful!!",Toast.LENGTH_LONG).show();
                    Log.d("TAG","login success");
                    Navigation.findNavController(view).navigate(LoginFragmentDirections.actionLoginFragmentToUserProfileFragment());
                }
                else
                    Log.d("TAG","failed login");
            }
        });
    }


    public void forgotPassword(){
        email_user=email.getText().toString();
        mAuth.sendPasswordResetEmail(email_user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                   Toast.makeText(getActivity().getBaseContext(), "check your email",Toast.LENGTH_LONG);
                }
                else
                    Log.d("TAG", "failed");
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }
}

