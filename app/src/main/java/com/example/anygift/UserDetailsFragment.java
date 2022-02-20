package com.example.anygift;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anygift.model.User;
import com.squareup.picasso.Picasso;


public class UserDetailsFragment extends Fragment {

   View view;
    TextView name, phone, email;
    Button MapBtn;
    String latAndLong=new String();
    ImageView profileImage;
    UserViewModel userViewModel;

    public UserDetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_details, container, false);
        getActivity().setTitle("AnyGift - Other Profile");
        name = view.findViewById(R.id.UserprofileF_name);
        phone = view.findViewById(R.id.UserprofileF_phone);
        email = view.findViewById(R.id.UserprofileF_mail);
        MapBtn=view.findViewById(R.id.UserprofileF_mapBtn);;
        profileImage=view.findViewById(R.id.UserprofileF_imageView);
        String userId = UserDetailsFragmentArgs.fromBundle(getArguments()).getUserId();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserById(userId, new UserViewModel.GetUserListener() {
            @Override
            public void onComplete(User user) {
                name.setText(user.getName());
                phone.setText(user.getPhone());
                email.setText(user.getEmail());
                if (user.getImageUrl() != null) {
                    Picasso.get().load(user.getImageUrl()).into(profileImage);
                    profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    profileImage.setClipToOutline(true);
                }
                latAndLong=user.getLatAndLong();
            }
        });
        MapBtn.setOnClickListener((v)->{
            String[] cordinate=latAndLong.split(",");
            String uri = "http://maps.google.com/maps?q=loc:" + cordinate[0] + "," + cordinate[1];
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });
        MapBtn = view.findViewById(R.id.UserprofileF_mapBtn);
        ;
        profileImage = view.findViewById(R.id.UserprofileF_imageView);
        String userId = UserDetailsFragmentArgs.fromBundle(getArguments()).getUserId();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserById(userId, new UserViewModel.GetUserListener() {
            @Override
            public void onComplete(User user) {
                name.setText(user.getName());
                phone.setText(user.getPhone());
                email.setText(user.getEmail());
                if (user.getImageUrl() != null) {
                    Picasso.get().load(user.getImageUrl()).into(profileImage);
                    profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    profileImage.setClipToOutline(true);
                }
                latAndLong = user.getLatAndLong();
            }
        });
        MapBtn.setOnClickListener((v) -> {
            String[] cordinate = latAndLong.split(",");
            String uri = "http://maps.google.com/maps?q=loc:" + cordinate[0] + "," + cordinate[1];
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });
        return view;
    }
}