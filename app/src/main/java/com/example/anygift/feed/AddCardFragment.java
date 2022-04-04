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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.anygift.R;
import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;
import com.example.anygift.model.User;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.List;

public class AddCardFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;//
    TextInputEditText cardValue, cardExpDay, cardExpMonth, cardExpYear, cardAskingValue, cardNumber;
    ImageButton uploadPicButton;
    Button addCardButton;
    ImageView giftCardImage;
    View view;
    ProgressBar pb;
    String latAndLong;
    UserViewModel userViewModel;
    AutoCompleteTextView autoCompleteTextView;
    TextInputLayout textInputLayout;
    ArrayAdapter<String> optionsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("AnyGift - AddCard");
        view = inflater.inflate(R.layout.fragment_add_card, container, false);
        cardNumber = view.findViewById(R.id.add_card_number);
        cardValue = view.findViewById(R.id.add_card_value);
        cardExpDay = view.findViewById(R.id.add_card_exp_day);
        cardExpMonth = view.findViewById(R.id.add_card_exp_mo);
        cardExpYear = view.findViewById(R.id.add_card_exp_year);
        cardAskingValue = view.findViewById(R.id.add_card_exp_value);
        uploadPicButton = view.findViewById(R.id.add_camera_bt);
        addCardButton = view.findViewById(R.id.add_upload_bt);
        giftCardImage = view.findViewById(R.id.add_giftCardImage);

//        String []items = {"op 1","op 2","op 3","op 4"};
//        textInputLayout = view.findViewById(R.id.add_card_til);
//        autoCompleteTextView = view.findViewById(R.id.add_card_otions_actv);
//        optionsAdapter = new ArrayAdapter<String>(requireContext(),R.layout.dropdown_item,items);
//        autoCompleteTextView.setAdapter(optionsAdapter);
//        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //adapterView.getItemAtPosition(i);
//            }
//        });
        

        giftCardImage.setTag("");
        pb = view.findViewById(R.id.add_pb);
        pb.setVisibility(View.INVISIBLE);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUser(new UserViewModel.GetUserListener() {
            @Override
            public void onComplete(User user) {
                latAndLong = user.getLatAndLong();
            }
        });
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        uploadPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editImage();
            }
        });

        return view;
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
                        giftCardImage.setImageBitmap(selectedImage);
                        giftCardImage.setTag("img");
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
                                giftCardImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                giftCardImage.setTag("img");
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    void popMsg(String Msg) {
        Snackbar mySnackbar = Snackbar.make(view, Msg, BaseTransientBottomBar.LENGTH_LONG);
        mySnackbar.show();

    }

    void enableButtons() {
        addCardButton.setEnabled(true);
        uploadPicButton.setEnabled(true);
    }

    void disableButtons() {
        addCardButton.setEnabled(false);
        uploadPicButton.setEnabled(false);

    }

    static boolean isLegalDate(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        return sdf.parse(s, new ParsePosition(0)) != null;
    }


    void logging(String CardValue, String CardAskingValue, String CardNumber) {
        Log.d("TAG", CardValue);
        Log.d("TAG", CardAskingValue);
        Log.d("TAG", CardNumber);
    }

    private void upload() {
        pb.setVisibility(View.VISIBLE);
        boolean error = false;
        String errorMsg = "";
        disableButtons();
        String CardValue = cardValue.getText().toString();
        String CardAskingValue = cardAskingValue.getText().toString();
        String CardNumber = cardNumber.getText().toString();
        String day = cardExpDay.getText().toString();
        String mo = cardExpMonth.getText().toString();
        String year = cardExpYear.getText().toString();
        String dateFormatted = year + "-" + mo + "-" + day;
        if (!isLegalDate(dateFormatted)) {
            error = true;
            errorMsg += "date: " + dateFormatted + " is Invalid!\n";
        }
        if (error) {
            popMsg(errorMsg);
            enableButtons();
            return;
        }

        String date = day + "/" + mo + "/" + year;
        logging(CardValue, CardAskingValue, CardNumber);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String emailUser = user.getEmail().toString();

        GiftCard newGiftCard = new GiftCard(CardNumber, Double.parseDouble(CardValue), date, Double.parseDouble(CardAskingValue), emailUser, latAndLong); //todo maybe change double to string
        BitmapDrawable drawable = (BitmapDrawable) giftCardImage.getDrawable();
        Log.d("BITAG", drawable.toString());
        Bitmap bitmap = drawable.getBitmap();
        Model.instance.uploadImage(bitmap, newGiftCard.getCardName(), new Model.UploadImageListener() {
            @Override
            public void onComplete(String url) {
                if (url == null) {
                    displayFailedError();
                } else {
                    newGiftCard.setGiftCardImageUrl(url);
                    Model.instance.addGiftCard(newGiftCard, () -> {
                        Navigation.findNavController(view).navigate(R.id.action_global_feedFragment);
                        popMsg("GiftCard Added! :)");
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
