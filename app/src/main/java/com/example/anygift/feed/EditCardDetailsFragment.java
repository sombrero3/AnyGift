package com.example.anygift.feed;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.anygift.R;
import com.example.anygift.Retrofit.Card;
import com.example.anygift.Retrofit.CardType;
import com.example.anygift.Retrofit.Category;
import com.example.anygift.model.Model;
import com.example.anygift.model.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EditCardDetailsFragment extends Fragment {
    View view,titleLine;
    EditText numberEt,priceEt;
    Spinner typeSp;
    String cardType, giftCardId;
    TextView  valueTv, dateTv, titleTop,titleBottom;
    Button save, mapBtn, deleteBtn;
    ImageView giftCardImage;
    Card giftCard = null;
    List<String> cardTypes;
    DatePickerDialog.OnDateSetListener dateListener;
    int year, month, day;
    ProgressBar pb;
    CheckBox forSaleCb;
    Animation rightAnim;
    List<Category> categories;

    TextInputEditText publisherNameEt;
    TextView categoriesTv,categoriesBackgroundTv;
    Button addCategoriesBtn;
    TextInputLayout nameContainerTIl;
    boolean otherFlag,itemsFlags[],itemsFlagsLastCondition[];
    CharSequence []  items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_card_details, container, false);
        //getActivity().setTitle("AnyGift - EditCardDetails");
        giftCardId = EditCardDetailsFragmentArgs.fromBundle(getArguments()).getGiftCardId();
        pb = view.findViewById(R.id.edit_card_pb);
        pb.setVisibility(View.VISIBLE);
        typeSp = view.findViewById(R.id.edit_card_type_spinner);
        numberEt = view.findViewById(R.id.edit_card_card_number);
        priceEt = view.findViewById(R.id.edit_card_card_asking_price);
        dateTv = view.findViewById(R.id.edit_card_exp_date_tv);
        valueTv = view.findViewById(R.id.edit_card_card_value);
        save = view.findViewById(R.id.edit_card_upload_bt);
        forSaleCb = view.findViewById(R.id.edit_card_for_sale_cb);
        giftCardImage = view.findViewById(R.id.edit_card_giftCardImage);
        titleLine = view.findViewById(R.id.edit_card_title_line);
        titleTop = view.findViewById(R.id.edit_card_title_top);
        titleBottom = view.findViewById(R.id.edit_card_title_bottom);
        categories = new ArrayList<>();
        categories.addAll(Model.instance.categories);

        rightAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.right_anim);
        titleLine.setAnimation(rightAnim);
        titleTop.setAnimation(rightAnim);
        titleBottom.setAnimation(rightAnim);

        setCardView();
        save.setOnClickListener(view -> saveChanges());
        return view;
    }

    private void setCardView() {
        Model.instance.getCardRetrofit(giftCardId, new Model.cardReturnListener() {
            @Override
            public void onComplete(Card card, String message) {
                giftCard = new Card();
                giftCard.setId(card.getId());
                giftCard.setCardType(card.getCardType());
                giftCard.setOwner(card.getOwner());
                //typeSp.setText(card.getCardType());
                numberEt.setText(card.getCardNumber());
                priceEt.setText(card.getPrice().toString());
                String date = Utils.ConvertLongToDate(Long.parseLong(card.getExpirationDate().toString().split(" ")[0]));
                dateTv.setText(date);
                year = Integer.valueOf(date.split("/")[2]);
                month = Integer.valueOf(date.split("/")[1]);
                day = Integer.valueOf(date.split("/")[0]);
                setDateSelector();
                valueTv.setText(card.getValue().toString());
                setCardTypesSpinner();
                forSaleCb.setSelected(card.getIsForSale());
                setCategoriesDialog();
            }
        });
    }

    private void setCategoriesDialog() {
        categoriesTv = view.findViewById(R.id.edit_card_categories_tv);
        categoriesBackgroundTv = view.findViewById(R.id.edit_card_publisher_background);
        publisherNameEt = view.findViewById(R.id.edit_card_publisher_name);
        addCategoriesBtn = view.findViewById(R.id.edit_card_categories_btn);
        nameContainerTIl = view.findViewById(R.id.edit_card_publisher_name_container_textinputlayout);
        List<String> AllCategories = new ArrayList<>();

        for(Category c : categories){
            AllCategories.add(c.getName());
        }
        items = AllCategories.toArray(new CharSequence[AllCategories.size()]);
        itemsFlags = new boolean[categories.size()];
        itemsFlagsLastCondition = new boolean[categories.size()];
        addCategoriesBtn.setOnClickListener(v->startCategoriesDialog());
    }

    private void startCategoriesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setIcon(R.drawable.categories_dialog_icon_1);
        builder.setTitle("Please Choose Categories");
        builder.setMultiChoiceItems(items, itemsFlags, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos, boolean isChecked) {
                //  itemsFlags[pos] = isChecked;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                String result = "";

                boolean psik = false;
                for(int i=0;i<categories.size();i++){
                    itemsFlagsLastCondition[i] = itemsFlags[i];
                    if(itemsFlags[i]) {
                        if (!psik) {
                            result += categories.get(i).getName();
                            psik = true;
                        } else {
                            result += ", " + categories.get(i).getName();;
                        }
                    }
                }

                if(result.isEmpty()) {
                    result = "Please choose categories";
                }
                categoriesTv.setText(result);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                for(int i=0;i<itemsFlags.length;i++){
                    itemsFlags[i] = itemsFlagsLastCondition[i];
                }
                dialogInterface.dismiss();
            }
        });

        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                for(int i=0;i<itemsFlags.length;i++){
                    itemsFlagsLastCondition[i] = false;
                    itemsFlags[i] = false;
                }
                categoriesTv.setText("Please choose categories");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
    private void CategoryMenuVisible(){
        categoriesTv.setVisibility(View.VISIBLE);
        addCategoriesBtn.setVisibility(View.VISIBLE);
        nameContainerTIl.setVisibility(View.VISIBLE);
        publisherNameEt.setVisibility(View.VISIBLE);
        categoriesBackgroundTv.setVisibility(View.VISIBLE);
    }
    private void CategoryMenuGone(){
        categoriesTv.setVisibility(View.GONE);
        addCategoriesBtn.setVisibility(View.GONE);
        nameContainerTIl.setVisibility(View.GONE);
        publisherNameEt.setVisibility(View.GONE);
        categoriesBackgroundTv.setVisibility(View.GONE);
    }
    private void setCardTypesSpinner() {
        List<CardType> cts = Model.instance.cardTypes;
        cardTypes = new ArrayList<>();
        int pos=0;
        for (int i=0;i<cts.size();i++) {
            cardTypes.add(cts.get(i).getName());
            if(cts.get(i).getId().equals(giftCard.getCardType())){
                pos=i;
            }
        }
        cardTypes.add("Other");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, cardTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSp.setAdapter(adapter);
        int finalPos = pos;
        otherFlag =false;
        typeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i<cts.size()) {
                    if(otherFlag){
                        CategoryMenuGone();
                        otherFlag = false;
                    }
                    if(cts.get(i).getPicture()!=null) {
                        giftCardImage.setImageBitmap(cts.get(i).getPicture());
                    }else{
                        giftCardImage.setImageResource(R.drawable.gift_card_logo_card);
                    }
                    cardType = cts.get(i).getId();
                }else{
                    giftCardImage.setImageResource(R.drawable.gift_card_logo_card);
                    otherFlag = true;
                    CategoryMenuVisible();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                giftCardImage.setImageBitmap(cts.get(finalPos).getPicture());
                cardType = cts.get(finalPos).getId();
            }
        });

        typeSp.setSelection(pos);
        pb.setVisibility(View.GONE);

    }


    private void saveChanges() {
        giftCard.setCardNumber(numberEt.getText().toString());
        giftCard.setPrice(Double.parseDouble(priceEt.getText().toString()));
        giftCard.setValue(Double.parseDouble(valueTv.getText().toString()));
        giftCard.setExpirationDate(Utils.convertDateToLong(Integer.toString(day),Integer.toString(month),Integer.toString(year)));
        giftCard.setIsForSale(forSaleCb.isChecked());
        giftCard.setCardType(Model.instance.cardTypes.get(typeSp.getSelectedItemPosition()).getId());

        Model.instance.updateCardRetrofit(giftCard.getId(), giftCard.toMap(), new Model.cardReturnListener() {
            @Override
            public void onComplete(Card card, String message) {
                Navigation.findNavController(view).navigateUp();
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

    private void setDateSelector() {
        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateListener, year, --month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                m += 1;
                year = y;
                month = m;
                day = d;
                dateTv.setText(day + "/" + month + "/" + year);
            }
        };
    }

}
