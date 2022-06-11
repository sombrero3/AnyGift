package com.example.anygift.feed;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddCardFragment extends Fragment {
    TextInputEditText cardValue, cardAskingValue, cardNumber ;
    Button addCardButton;
    ImageView giftCardImage;
    View view,titleLine;
    ProgressBar pb;
    String latAndLong, cardType;
//    UserViewModel userViewModel;
    Spinner spinnerCardType;
    TextView dateTv,titleTop,titleBottom;
    CheckBox forSaleCb;
    DatePickerDialog.OnDateSetListener dateListener;
    List<String> cardTypes;
    int year, month, day;
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
        //getActivity().setTitle("AnyGift - AddCard");
        //userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        view = inflater.inflate(R.layout.fragment_add_card, container, false);
        cardNumber = view.findViewById(R.id.add_card_number);
        cardValue = view.findViewById(R.id.add_card_value);
        cardAskingValue = view.findViewById(R.id.add_card_asking_price);
        addCardButton = view.findViewById(R.id.add_upload_bt);
        giftCardImage = view.findViewById(R.id.add_giftCardImage);
        dateTv = view.findViewById(R.id.add_card_date_tv);
        forSaleCb = view.findViewById(R.id.add_card_for_sale_cb);
        titleLine = view.findViewById(R.id.add_card_title_line);
        titleTop = view.findViewById(R.id.add_card_title_top);
        titleBottom = view.findViewById(R.id.add_card_title_bottom);
        pb = view.findViewById(R.id.add_pb);

        pb.setVisibility(View.VISIBLE);
        giftCardImage.setTag("");



        setCardtypesSpinner();
        setDateSelector();

        categories = new ArrayList<>();
        categories.addAll(Model.instance.categories);
        setCategoriesDialog();

        addCardButton.setOnClickListener(v -> upload());

        rightAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.right_anim);
        titleLine.setAnimation(rightAnim);
        titleTop.setAnimation(rightAnim);
        titleBottom.setAnimation(rightAnim);

        pb.setVisibility(View.INVISIBLE);
        return view;
    }
    private void setCategoriesDialog() {
        categoriesTv = view.findViewById(R.id.add_card_categories_tv);
        categoriesBackgroundTv = view.findViewById(R.id.add_card_publisher_background);
        publisherNameEt = view.findViewById(R.id.add_card_publisher_name);
        addCategoriesBtn = view.findViewById(R.id.add_card_categories_btn);
        nameContainerTIl = view.findViewById(R.id.add_card_publisher_name_container_textinputlayout);
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
                            result += ", " + categories.get(i).getName();
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

    private void setDateSelector() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateListener, year, month, day);
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

    private void setCardtypesSpinner() {
        List<CardType> cts = Model.instance.cardTypes;
        cardTypes = new ArrayList<>();
        for (CardType ct : cts) {
            cardTypes.add(ct.getName());
        }
        cardTypes.add("Other");
        spinnerCardType = (Spinner) view.findViewById(R.id.option);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, cardTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCardType.setAdapter(adapter);
        otherFlag =false;
        spinnerCardType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i<cts.size()) {
                    if(otherFlag){
                        CategoryMenuGone();
                        otherFlag = false;
                    }
                    giftCardImage.setImageBitmap(cts.get(i).getPicture());
                    cardType = cts.get(i).getId();
                }else{
                    giftCardImage.setImageResource(R.drawable.gift_card_logo_card);
                    otherFlag = true;
                    CategoryMenuVisible();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                giftCardImage.setImageResource(R.drawable.gift_card_logo_card);


            }
        });
        spinnerCardType.setSelection(cts.size());

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

    void popMsg(String Msg) {
        Snackbar mySnackbar = Snackbar.make(view, Msg, BaseTransientBottomBar.LENGTH_LONG);
        mySnackbar.show();

    }

    void enableButtons() {
        addCardButton.setEnabled(true);
    }

    void disableButtons() {
        addCardButton.setEnabled(false);
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

        if (error) {
            popMsg(errorMsg);
            enableButtons();
            return;
        }

        Log.d("TAG", "date = " + day + "/" + month + "/" + year);
        Log.d("TAG", "card type id = " + cardType);

        HashMap<String, Object> map = Card.mapToAddCard(Double.parseDouble(cardAskingValue.getText().toString()),
                Double.parseDouble(cardValue.getText().toString()), cardNumber.getText().toString(), cardType,
                Model.instance.modelRetrofit.getUserId(), forSaleCb.isChecked(),
                Utils.convertDateToLong(Integer.toString(day), Integer.toString(month), Integer.toString(year)));

        Model.instance.addCardRetrofit(map, new Model.cardReturnListener() {
            @Override
            public void onComplete(Card card, String message) {
                if (card != null) {
                    popMsg("GiftCard successfully uploaded");
                    Navigation.findNavController(dateTv).navigate(AddCardFragmentDirections.actionGlobalMyCardsFragment());
                } else {
                    popMsg("GiftCard uploading Failed!");
                }
                enableButtons();
                pb.setVisibility(View.INVISIBLE);
            }
        });

    }
}
