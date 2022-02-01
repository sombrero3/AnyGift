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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.anygift.model.Model;
import com.example.anygift.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpFragment extends Fragment {

    User user;
    View view;

    private FirebaseAuth mAuth= FirebaseAuth.getInstance();

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
    String Lname ;
    String phone_usr;
    String email_usr;
    String address_usr;
    String password_usr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        signIn_btn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment());
        });
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

        mAuth.createUserWithEmailAndPassword(email_usr,password_usr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                   updateProfile();
                }
            }
        });
        user = new User(Fname, Lname, phone_usr, email_usr, address_usr, password_usr);
        Model.instance.addUser(user, () -> {
            Navigation.findNavController(view).navigate(SignUpFragmentDirections.actionSignUpFragmentToUserProfileFragment());
        });
    }


    public void updateProfile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Fname)
                .build();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }
}