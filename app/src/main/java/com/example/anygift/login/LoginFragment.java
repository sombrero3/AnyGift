package com.example.anygift.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anygift.R;
import com.example.anygift.feed.MainActivity;
import com.example.anygift.model.Model;
import com.example.anygift.model.ModelFirebase;
import com.example.anygift.model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LoginFragment extends Fragment {

    View view;
    Snackbar mySnackbar;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressBar pb;

    Button signIn_btn, signUp_btn, forgotP_btn;
    LoginButton facebook_btn;
    TextInputEditText email, password;
    String email_user, password_user;
CallbackManager callbackManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        //getActivity().setTitle("AnyGift - Login");
        view = inflater.inflate(R.layout.fragment_login, container, false);
        signIn_btn = view.findViewById(R.id.Login_signIn_btn);
        signUp_btn = view.findViewById(R.id.Login_btn_signUp);
        forgotP_btn = view.findViewById(R.id.Login_btn_forgotp);
        email = view.findViewById(R.id.Login_email_input);
        password = view.findViewById(R.id.Login_password_input);
        pb = view.findViewById(R.id.login_prob);
        pb.setVisibility(View.INVISIBLE);
        signIn_btn.setTypeface(Typeface.SANS_SERIF);
        signUp_btn.setTypeface(Typeface.SANS_SERIF);
        forgotP_btn.setTypeface(Typeface.SANS_SERIF);

        signIn_btn.setOnClickListener(v -> {
//            login();
            checkUser();
        });

        signUp_btn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment());
        });

        //TODO implement forgot password
        forgotP_btn.setOnClickListener(v -> {
            mySnackbar = Snackbar.make(view, "in development", BaseTransientBottomBar.LENGTH_LONG);
            mySnackbar.show();
        });
//Facebook
        callbackManager = CallbackManager.Factory.create();
        facebook_btn=view.findViewById(R.id.signin_facebook_btn);
        //facebook_btn.registerCallback(callbackManager,this);
        facebook_btn.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        facebook_btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>(){
            @Override
            public void onSuccess(LoginResult loginResult){

                // Calling method to access User Data After successfully login.
                GraphLoginRequest(loginResult.getAccessToken());
            }

            @Override
            public void onCancel(){

                Toast.makeText(getContext(),"Login Canceled",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception){

                Toast.makeText(getContext(),"Login Failed",Toast.LENGTH_SHORT).show();
            }

        });
        return view;
    }
    public void setButtons(Boolean b){
        signIn_btn.setEnabled(b);
        signUp_btn.setEnabled(b);
        forgotP_btn.setEnabled(b);
    }

    public void login() {
        setButtons(false);
        HashMap<String, Object> map = com.example.anygift.Retrofit.User.mapToLogin(email.getText().toString(), password.getText().toString());
        Model.instance.login(map, (user, message) -> {
            pb.setVisibility(View.INVISIBLE);
            if (user == null) {
                setButtons(true);
                mySnackbar = Snackbar.make(view, message, BaseTransientBottomBar.LENGTH_LONG);
                mySnackbar.show();
                } else {
                    mySnackbar = Snackbar.make(view, "Login successful :)", BaseTransientBottomBar.LENGTH_LONG);
                    mySnackbar.show();
                    Model.instance.setCardTypes(new Model.VoidListener() {
                        @Override
                        public void onComplete() {
                            goToFeedActivity(user);
                        }
                    });

                }
        });
    }

    public void checkUser() {
        email_user = email.getText().toString();
        password_user = password.getText().toString();
        if (TextUtils.isEmpty(email_user) && email_user.matches(emailPattern)) {
            email.setError("please enter correct email");
            return;
        }
        if (TextUtils.isEmpty(password_user)) {
            password.setError("please enter correct  password");
            return;
        }
        //connect via http request
        pb.setVisibility(View.VISIBLE);

        login();

    }

    public void goToFeedActivity(com.example.anygift.Retrofit.User user) {
        Model.instance.setCurrentUser(user);
        //progressBar.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

//    public void forgotPassword() {
//        email_user = email.getText().toString();
//        mAuth.sendPasswordResetEmail(email_user).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    mySnackbar = Snackbar.make(view, "Email sent, check your mailbox :)", BaseTransientBottomBar.LENGTH_LONG);
//                    mySnackbar.show();
//
//                } else
//                    Log.d("TAG", "failed");
//            }
//        });
//    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    protected void GraphLoginRequest(AccessToken accessToken){
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                    }

                });

        Bundle bundle = new Bundle();
        bundle.putString(
                "fields",
                "id,name,link,email,gender,last_name,first_name,locale,timezone,updated_time,verified"
        );
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
        Navigation.findNavController(view).navigate(R.id.signUpFragment, bundle);

    }
}

