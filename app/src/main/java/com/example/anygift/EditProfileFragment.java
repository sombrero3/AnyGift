package com.example.anygift;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.anygift.model.Model;
import com.example.anygift.model.User;


public class EditProfileFragment extends Fragment {

    View view;
    UserViewModel userViewModel;
    TextView name;
    EditText firstName,LastName,phone,password;
    TextView email;
    Button saveBtn;
    User temp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("AnyGift - EditProfile");
        view= inflater.inflate(R.layout.fragment_edit_profile, container, false);
        saveBtn = view.findViewById(R.id.editProfile_saveBtn);
        name = view.findViewById(R.id.editProfileF_name);
        firstName = view.findViewById(R.id.editProfileF_firstName);
        LastName = view.findViewById(R.id.editProfileF_LastName);
        phone = view.findViewById(R.id.editProfileF_phone);
        email = view.findViewById(R.id.editProfileF_email);
        password=view.findViewById(R.id.editProfileF_password);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUser(new UserViewModel.GetUserListener() {
            @Override
            public void onComplete(User user) {
                temp=user;
                name.setText((user!=null)?user.getName():"null");
                firstName.setText((user!=null)?user.getFirstName():"null");
                LastName.setText((user!=null)?user.getLastName():"null");
                 email.setText((user!=null)?user.getEmail():"null");
                phone.setText((user!=null)?user.getPhone():"null");
                password.setText((user!=null)?user.getPassword():"null");


            }

        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp.getFirstName().compareTo(firstName.getText().toString())!=0){
                    temp.setFirstName(firstName.getText().toString());
                }
                if(temp.getLastName().compareTo(LastName.getText().toString())!=0){
                    temp.setLastName(LastName.getText().toString());
                }
                if(temp.getPhone().compareTo(phone.getText().toString())!=0){
                    temp.setPhone(phone.getText().toString());
                }
                if(temp.getPassword().compareTo(password.getText().toString())!=0){
                    temp.setPassword(password.getText().toString());
                }
                Model.instance.updateUser(temp, () -> {
                    Navigation.findNavController(view).navigate(R.id.action_global_userProfileFragment);
                });
            }
        });
        return view;
    }

}