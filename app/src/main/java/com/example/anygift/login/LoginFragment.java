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
import com.example.anygift.Retrofit.User;
import com.example.anygift.feed.MainActivity;
import com.example.anygift.model.Model;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
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

import javax.annotation.Nullable;

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
ProfileTracker profileTracker;
    String userEmail,firstName,lastName;
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
        facebook_btn.setOnClickListener(v -> facebook_btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                if(Profile.getCurrentProfile() == null) {


                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            GraphRequest request = GraphRequest.newMeRequest(
                                    accessToken,
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                                            try {
                                                if (AccessToken.getCurrentAccessToken() == null) {

                                                    return; // already logged out
                                                }
                                                 userEmail = jsonObject.getString("email");
                                                 /* firstName = jsonObject.getString("first_name");
                                                lastName = jsonObject.getString("last_name");*/

                                                Long id = jsonObject.getLong("id");
                                                //oldProfile.getPictureUri()
                                                String token = loginResult.getAccessToken().getToken();
                                                String facebookToken = token;
                                                System.out.println(facebookToken);
                                                //check if user already register
                                                Model.instance.checkIfEmailExists(userEmail, new Model.booleanReturnListener() {
                                                    @Override
                                                    public void onComplete(Boolean result, String message) {
                                                        //go to feed
                                                        if(result){
                                                            email.setText(userEmail);
                                                        Toast.makeText(getContext(),"Please enter your password",Toast.LENGTH_LONG).show();
                                                            //goToFeedActivity(user);
                                                        }
                                                        else{
                                                            // register new user
                                                          //  String[] details= {userEmail,firstName,lastName};
                                                            Bundle bundle = new Bundle();
                                                            bundle.putString("userEmail", userEmail);
                                                            Navigation.findNavController(v).navigate(R.id.signUpFragment,bundle);
                                                             }
                                                    }
                                                });
                                            /*    Model.instance.(username2, new Modelauth.getUserByUserNameInSignIn() {
                                                    @Override
                                                    public void onComplete(User profile) {
                                                        if (profile != null) {
                                                            isCreated=false;
                                                            Modelauth.instance2.Login(username2, "facebook", new Modelauth.loginListener() {
                                                                @Override
                                                                public void onComplete(int code) {
                                                                    toFeedActivity();
                                                                    Toast.makeText(getActivity(), "Welcome to Collab Me!", Toast.LENGTH_LONG).show();
                                                                }
                                                            });

                                                            return;
                                                        }


                                                        else {
//                                                            Navigation.findNavController(v).navigate(LoginFragmentDirections.actionGlobalSocialmedia(username2, "facebook", false,
//                                                                    true, email, "age", gender, null, null, null,null));
                                                            // handleSighUp();
                                                            Navigation.findNavController(v).navigate(LoginFragmentDirections.actionGlobalSignupFragment2(username2,"facebook",email));
                                                        }
                                                    }
                                                });
                */
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email");
                            request.setParameters(parameters);
                            request.executeAsync();

                        }
                    };
                    profileTracker.startTracking();
                }

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
            }
        }));


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
}

