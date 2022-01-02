package com.program.endtermexam;

import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.LinkedList;

public class CurrentUser {
    private FirebaseDatabase rootNode;
    private DatabaseReference databaseReference_user;

    public DatabaseReference getDatabaseReference_user() {
        return databaseReference_user;
    }

    private LinkedList<String> userDataList;
    private String userID;
    private String userPassword;

    public CurrentUser(String userID, String userPassword) {
        this.userID = userID;
        this.userPassword = userPassword;
    }

    public void InitializeUserData(){
        rootNode = FirebaseDatabase.getInstance("https://endterm-exam-default-rtdb.firebaseio.com/");
        databaseReference_user = rootNode.getReference("Users");
//        userDataList = new LinkedList<>();
//        Query query_userIDs = databaseReference_user.child()
    }

    public String getUserID() {
        return userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
