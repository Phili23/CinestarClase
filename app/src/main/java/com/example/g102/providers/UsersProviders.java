package com.example.g102.providers;

import com.example.g102.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsersProviders {

    private CollectionReference mCollection;

    public UsersProviders() {
        mCollection=FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot> getUser(String id){
        return mCollection.document(id).get();//1.05 primera sesion 4ta semana

    }

    public Task<Void> create(User user){
        return mCollection.document(user.getId()).set(user);
    }

    public Task<Void> update(User user){
        Map<String,Object>map=new HashMap<>();
        map.put("username",user.getUsername());
        map.put("email",user.getEmail());
        map.put("password",user.getPassword());
        return mCollection.document(user.getId()).update(map);
    }
}
