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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anygift.R;
import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;
import com.squareup.picasso.Picasso;

import java.util.List;


public class EditCardDetailsFragment extends Fragment {
    View view;
    EditText  buyAt;
    TextView nametv,valuetv;
    Button save, mapBtn, editBtn,deleteBtn;
    ImageView giftCardImage,uploadPicbtn;
    MyWalletViewModel viewModel;
    GiftCard giftCard = null;
    /*
    List<Category> categoryList;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_card_details, container, false);
        //getActivity().setTitle("AnyGift - EditCardDetails");
        String giftCardId = EditCardDetailsFragmentArgs.fromBundle(getArguments()).getGiftCardId();
        viewModel = new ViewModelProvider(this).get(MyWalletViewModel.class);
        List<GiftCard> list = viewModel.getList().getValue();

        for (GiftCard gc : list) {
            if (gc.getId().equals(giftCardId))
                giftCard = gc;
        }
        nametv = view.findViewById(R.id.card_details_username_tv);
        valuetv = view.findViewById(R.id.edit_card_card_value);
        buyAt = view.findViewById(R.id.edit_card_card_asking_price);
        uploadPicbtn = view.findViewById(R.id.edit_card_camera_bt);
        save = view.findViewById(R.id.edit_card_upload_bt);
        giftCardImage = view.findViewById(R.id.edit_card_giftCardImage);

        nametv.setText(String.valueOf("Belong to: " + giftCard.getOwnerEmail()));
        valuetv.setText(String.valueOf(giftCard.getValue()));
        buyAt.setText(String.valueOf(giftCard.getWantedPrice()));
        Picasso.get().load(giftCard.getGiftCardImageUrl()).into(giftCardImage);

        uploadPicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editImage();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
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

    private void update() {
        BitmapDrawable drawable = (BitmapDrawable) giftCardImage.getDrawable();
        Log.d("BITAG", drawable.toString());
        Bitmap bitmap = drawable.getBitmap();
        Model.instance.uploadImage(bitmap, giftCard.getId(), new Model.UploadImageListener() {
            @Override
            public void onComplete(String url) {
                if (url == null) {
                    displayFailedError();
                } else {
                    giftCard.setGiftCardImageUrl(url);
                    giftCard.setValue(Double.parseDouble(valuetv.getText().toString()));
                    giftCard.setWantedPrice(Double.parseDouble(buyAt.getText().toString()));
                    Model.instance.addGiftCard(giftCard, () -> {
                        Navigation.findNavController(view).navigate(R.id.action_global_feedFragment);
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