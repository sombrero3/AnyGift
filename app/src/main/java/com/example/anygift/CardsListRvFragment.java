package com.example.anygift;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.LinkedList;
import java.util.List;



import com.example.anygift.model.GiftCard;
import com.example.anygift.model.Model;


public class CardsListRvFragment extends Fragment {
    List<GiftCard> cards;
    EditText cardEt;
    ImageView cardIv;

@Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards_list_rv, container, false);
       cards= Model.instance.getCards();

        RecyclerView list = view.findViewById(R.id.cards_list_rv);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        MyAdapter adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String cardName = cards.get(position).getCardName();
                Log.d("TAG","user's row clicked: " + cardName);
                //Navigation.findNavController(v).navigate(StudentListRvFragmentDirections.actionStudentListRvFragmentToStudentDetailsFragment(stId));

            }
        });


        return view;

    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView cardName;
        ImageView cardImage;
        CheckBox cb;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            cardName = itemView.findViewById(R.id.cards_list_row_et);
            cardImage = itemView.findViewById(R.id.cards_list_row_iv);
            cb = itemView.findViewById(R.id.cards_list_row_cb);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cards.get(getAdapterPosition()).setFlag(cb.isChecked());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(v,pos);
                }
            });

        }
    }
    interface OnItemClickListener{
        void onItemClick(View v,int position);
    }
    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        OnItemClickListener listener;
        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.cards_list_row,parent,false);
            MyViewHolder holder = new MyViewHolder(view,listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            GiftCard card = cards.get(position);
            holder.cardName.setText(card.getCardName());
            holder.cb.setChecked(card.isFlag());


        }

        @Override
        public int getItemCount() {
            return cards.size();
        }
    }
}