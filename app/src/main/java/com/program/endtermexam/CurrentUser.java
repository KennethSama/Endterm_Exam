package com.program.endtermexam;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class CurrentUser {
    private FirebaseDatabase rootNode;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference_user;
    private SharedPreferences userSession;
    private SharedPreferences.Editor editor;

    public DatabaseReference getDatabaseReference_user() {
        return databaseReference_user;
    }

    private ArrayList<String> userDataKeys;
    private String userID;
    private String userPassword;

    public CurrentUser(String userID, String userPassword) {
        this.userID = userID;
        this.userPassword = userPassword;
    }

    public ArrayList<String> getUserDataKeys() {
        return userDataKeys;
    }

    public void setUserDataKeys(ArrayList<String> userDataKeys) {
        this.userDataKeys = userDataKeys;
    }

    public CurrentUser(){}

    public void InitializeUserData(){
        firebaseAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance("https://endterm-exam-default-rtdb.firebaseio.com/");
        databaseReference_user = rootNode.getReference("Users");
        userDataKeys = new ArrayList<>();
//        Query query_userIDs = databaseReference_user.child()
    }

    public String getUserID() {
        return userID;
    }

    public String getUserPassword() {
        return userPassword;
    }
    public void InitializeUserID(){
        this.setUserID("User_".concat(firebaseAuth.getCurrentUser().getUid()));
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean canUpdate;
    public HashMap<String, String> userHashmap = new HashMap<>();

    public void SetUserData(Activity activity){
        Log.d("UserDataID", getUserID());
        Log.d("UserDataID", databaseReference_user.toString());
        Log.d("UserDataID", databaseReference_user.child(getUserID()).toString());
        InitializePreferences(activity.getBaseContext());
        editor = userSession.edit();
        databaseReference_user.child(getUserID()).addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int childCount = (int) dataSnapshot.getChildrenCount();
//                    Log.d("UserData", Integer.toString(childCount));
                    userDataKeys.clear();
                    int x = 0;
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        userDataKeys.add(snapshot.getKey());
                        editor.putString(snapshot.getKey(), snapshot.getValue().toString());
                        x += 1;
                        Log.d("UserData", Integer.toString(x));
                    }
                    setUserDataKeys(userDataKeys);
                    if(x >= childCount) {
                        canUpdate = true;
                        editor.commit();
                        Log.d("UserData \"Array\"", userDataKeys.toString());
                        Log.d("UserData \"SPEditor\"", editor.toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(activity.getApplicationContext(), "Opss! Something went wrong!", Toast.LENGTH_SHORT).show();
                }
        });
    }
    public HashMap<String ,String > GetUserSession(){
        HashMap<String, String> userDataHashMap = new HashMap<>();
        for(String keys : userDataKeys )
            userDataHashMap.put(keys, userSession.getString(keys,null));
        return userDataHashMap;
    }
    public HashMap<String ,String > GetUserSession(ArrayList<String> dataKeys){
        HashMap<String, String> userDataHashMap = new HashMap<>();
        for(String keys : dataKeys )
            userDataHashMap.put(keys, userSession.getString(keys,null));
        return userDataHashMap;
    }
    public void LogoutSession(){
        editor.clear();
        editor.commit();
    }
    private void InitializePreferences(Context context){
        userSession = context.getSharedPreferences("UserLoginSession", Context.MODE_PRIVATE);
    }
}
