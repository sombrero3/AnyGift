package com.example.anygift.feed;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anygift.R;
import com.example.anygift.model.Model;
import com.example.anygift.model.User;
import com.squareup.picasso.Picasso;


public class EditProfileFragment extends Fragment {

    TextView emailTv;
    View view;
    UserViewModel userViewModel;
    EditText firstNameEt, lastNameEt, phoneEt;
    Button saveBtn;
    User temp;
    ImageButton cameraBtn;
    ImageView profileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //getActivity().setTitle("AnyGift - EditProfile");
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        saveBtn = view.findViewById(R.id.editProfile_saveBtn);

        firstNameEt = view.findViewById(R.id.editProfileF_firstName);
        lastNameEt = view.findViewById(R.id.editProfileF_LastName);
        phoneEt = view.findViewById(R.id.editProfileF_phone);
        profileImage = view.findViewById(R.id.editProfileF_image);
        profileImage.setTag("");
        emailTv = view.findViewById(R.id.editProfile_email_tv);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        temp = new User();
        temp.fromMap(Model.instance.getSignedUser().mapToCreateOriginalUser());
        firstNameEt.setText((temp != null) ? temp.getFirstName() : "null");
        lastNameEt.setText((temp != null) ? temp.getLastName() : "null");
        emailTv.setText((temp != null) ? temp.getEmail() : "null");
        phoneEt.setText((temp != null) ? temp.getPhone() : "null");
        if (temp.getImageUrl() != null && !temp.getImageUrl().isEmpty()) {
            Picasso.get().load(temp.getImageUrl()).into(profileImage);
            profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            profileImage.setClipToOutline(true);
        }


        cameraBtn = view.findViewById(R.id.editProfile_imageButton);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editImage();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation(firstNameEt.getText().toString().trim(),lastNameEt.getText().toString().trim(),phoneEt.getText().toString().trim())) {
                    if (temp.getFirstName().compareTo(firstNameEt.getText().toString()) != 0) {
                        temp.setFirstName(firstNameEt.getText().toString());
                    }
                    if (temp.getLastName().compareTo(lastNameEt.getText().toString()) != 0) {
                        temp.setLastName(lastNameEt.getText().toString());
                    }
                    if (temp.getPhone().compareTo(phoneEt.getText().toString()) != 0) {
                        temp.setPhone(phoneEt.getText().toString());
                    }
                    updateImage();
                }
            }
        });
        return view;
    }

    private boolean validation(String firstName, String lastName,String phone) {
        if(firstName.isEmpty()){
            firstNameEt.setError("First name is required");
            firstNameEt.requestFocus();
            return false;
        }
        if(lastName.isEmpty()){
            lastNameEt.setError("Last name is required");
            lastNameEt.requestFocus();
            return false;
        }
        if(phone.isEmpty()){
            phoneEt.setError("Email address is required");
            phoneEt.requestFocus();
            return false;
        }
        return true;
    }

    private void editImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        profileImage.setImageBitmap(selectedImage);
                        profileImage.setTag("img");
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                profileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                profileImage.setTag("img");
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void updateImage() {
        BitmapDrawable drawable = (BitmapDrawable) profileImage.getDrawable();
        Log.d("BITAG", drawable.toString());
        Bitmap bitmap = drawable.getBitmap();
        Model.instance.uploadUserImage(bitmap, temp.getId(), new Model.UploadUserImageListener() {
            @Override
            public void onComplete(String url) {
                if (url == null) {
                    displayFailedError();
                } else {
//                    temp.setImageUrl(url);
//                    Model.instance.addUser(temp, () -> {
//                        Model.instance.setCurrentUser(null) -> Navigation.findNavController(view).navigate(R.id.action_global_myCardsFragment));
//                    });
                }
            }
        });


    }

    private void displayFailedError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Operation Failed");
        builder.setMessage("Saving image failed, please try again later...");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

}