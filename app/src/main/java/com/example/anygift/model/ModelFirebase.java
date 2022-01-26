package com.example.anygift.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ModelFirebase {
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    public ModelFirebase(){
        FirebaseFirestoreSettings set=new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build();
        db.setFirestoreSettings(set);
    }


    //GiftCard

    interface GetAllGiftCardListener{
        void onComplete(List<GiftCard> list);
    }

    public void getAllProducts(Long lastUpdated, final GetAllGiftCardListener listener) {
        Timestamp ts = new Timestamp(lastUpdated, 0);
        db.collection("giftCards").whereGreaterThanOrEqualTo("lastUpdated", ts).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<GiftCard> data = new LinkedList<GiftCard>();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        GiftCard gf = new GiftCard();
                        gf.fromMap(doc.getData());
                        data.add(gf);
                        Log.d("TAG", "st: " + gf.getId());
                    }
                }
                listener.onComplete(data);
            }
        });
    }

    public void addGiftCard(GiftCard giftCard, final Model.AddGiftCardListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("giftCards").document(giftCard.getId())
                .set(giftCard.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG","giftCard added successfully");
                listener.onComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","fail adding giftCard");
                listener.onComplete();
            }
        });
    }
    public void updateGiftCard(GiftCard product, Model.AddGiftCardListener listener) {
        addGiftCard(product,listener);
    }


    public void getGiftCard(String id, final Model.GetGiftCardListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("giftCard").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                GiftCard giftCard = null;
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null) {
                        giftCard = new GiftCard();
                        giftCard.fromMap(task.getResult().getData());
                    }
                }
                listener.onComplete(giftCard);
            }
        });
    }

    public void delete(String id) {
        FirebaseFirestore.getInstance().collection("giftCards").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
//        .addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                listener.onComplete();
//            }
//        });
    }
/*
    public void uploadImage(Bitmap imageBmp, String name, final Model.UploadImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child("images").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        listener.onComplete(downloadUrl.toString());
                    }
                });
            }
        });
    }

 */

//User

    public interface GetAllUsersListener{ void onComplete(List<User> list);}

    public void getAllUsers(final GetAllUsersListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(task -> {
            List<User> data = new ArrayList<>();
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult()) {
                    User user = new User();
                    user.fromMap(doc.getData());
                    data.add(user);
                }
            }
            listener.onComplete(data);
        });
    }

    public void addUser(User user, final Model.AddUserListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getId())
                .set(user.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG","user added successfully");
                listener.onComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","fail adding student");
                listener.onComplete();
            }
        });
    }



}
