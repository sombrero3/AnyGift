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
import com.example.anygift.Retrofit.UploadImageResult;
import com.example.anygift.Retrofit.User;
import com.example.anygift.model.Model;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;


public class EditProfileFragment extends Fragment {

    TextView emailTv;
    View view;
    UserViewModel userViewModel;
    EditText firstNameEt, lastNameEt, phoneEt;
    Button saveBtn;
    com.example.anygift.Retrofit.User temp;
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

        temp = Model.instance.getSignedUser();
        firstNameEt.setText((temp != null) ? temp.getFirstName() : "null");
        lastNameEt.setText((temp != null) ? temp.getLastName() : "null");
        emailTv.setText((temp != null) ? temp.getEmail() : "null");
        phoneEt.setText((temp != null) ? temp.getPhone() : "null");

        if (temp.getProfilePicture() != null && !temp.getProfilePicture().isEmpty()) {
            showProfilePic();
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
                if (validation(firstNameEt.getText().toString().trim(), lastNameEt.getText().toString().trim(), phoneEt.getText().toString().trim())) {
                    if (temp.getFirstName().compareTo(firstNameEt.getText().toString()) != 0) {
                        temp.setFirstName(firstNameEt.getText().toString());
                    }
                    if (temp.getLastName().compareTo(lastNameEt.getText().toString()) != 0) {
                        temp.setLastName(lastNameEt.getText().toString());
                    }
                    if (temp.getPhone().compareTo(phoneEt.getText().toString()) != 0) {
                        temp.setPhone(phoneEt.getText().toString());
                    }
                    try {
                        updateImage();
                    } catch (Exception exception) {

                    }
                }
            }
        });
        return view;
    }


    public void showProfilePic() {
        Model.instance.downloadImage(temp.getProfilePicture().replace("/image/", ""),
                new Model.byteArrayReturnListener() {
                    @Override
                    public void onComplete(Bitmap bitmap) {
                        if (bitmap == null) {
                            return;
                        }
                        profileImage.setImageBitmap(bitmap);
                        profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        profileImage.setClipToOutline(true);
                    }
                });

    }

    private boolean validation(String firstName, String lastName, String phone) {
        if (firstName.isEmpty()) {
            firstNameEt.setError("First name is required");
            firstNameEt.requestFocus();
            return false;
        }
        if (lastName.isEmpty()) {
            lastNameEt.setError("Last name is required");
            lastNameEt.requestFocus();
            return false;
        }
        if (phone.isEmpty()) {
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

    public static InputStream bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }


    private void updateImage() throws IOException {
        BitmapDrawable drawable = (BitmapDrawable) profileImage.getDrawable();
        Log.d("BITAG", drawable.toString());
        Bitmap bitmap = drawable.getBitmap();
        InputStream is = bitmap2InputStream(bitmap);

        Model.instance.uploadImage(getBytes(is), new Model.uploadImageListener() {
            @Override
            public void onComplete(UploadImageResult uploadImageResult) {
                System.out.println(uploadImageResult);
                String profilePicFile = uploadImageResult.getPath().replace("/images/", "");
                temp.setProfilePicture(profilePicFile);
                if (uploadImageResult == null)
                    displayFailedError();
                else {
                    HashMap<String, Object> newmap = new HashMap<String, Object>() {{
                        put("firstName", temp.getFirstName());
                        put("lastName", temp.getLastName());
                        put("phone", temp.getPhone());
                        put("profilePicture", temp.getProfilePicture());
                    }};

                    com.example.anygift.Retrofit.User.mapToUpdateUser(temp.getId(), newmap);
                    Model.instance.updateUser(newmap, new Model.userLoginListener() {
                        @Override
                        public void onComplete(User user, String message) {
                            if (user != null) {
                                Snackbar mySnackbar = Snackbar.make(view, "User updated successfully :)", BaseTransientBottomBar.LENGTH_LONG);
                                mySnackbar.show();
                                Navigation.findNavController(view).navigate(R.id.action_global_myCardsFragment);
                            } else {
                                Snackbar mySnackbar = Snackbar.make(view, "User update Failed :(", BaseTransientBottomBar.LENGTH_LONG);
                                mySnackbar.show();

                            }
                        }
                    });
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