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
import java.util.Map;

public class CurrentUser {
    private FirebaseDatabase rootNode;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference_user;
    private DatabaseReference databaseReference_quiz;
    private SharedPreferences session_user;
    private SharedPreferences session_quiz;
    private SharedPreferences session_quizKeys;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor editor_quiz;
    private SharedPreferences.Editor editor_quizKeys;

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

    public CurrentUser(){}

    public void InitializeUserData(boolean useDataKeys){
        firebaseAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance("https://endterm-exam-default-rtdb.firebaseio.com/");
        databaseReference_user = rootNode.getReference("Users");
        if (useDataKeys)
            userDataKeys = new ArrayList<>();
    }

    public void InitializeQuizData(){
        firebaseAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance("https://endterm-exam-default-rtdb.firebaseio.com/");
        databaseReference_quiz = rootNode.getReference("Quizzes");
    }

    public void InitializeUserID(){
        this.setUserID("User_".concat(firebaseAuth.getCurrentUser().getUid()));
    }
    public boolean canUpdate;
    public boolean canUpdateQuiz;


    public String getUserID() {
        return userID;
    }

    public ArrayList<String> getUserDataKeys() {
        return userDataKeys;
    }

    public void setUserDataKeys(ArrayList<String> userDataKeys) {
        this.userDataKeys = userDataKeys;
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

    public HashMap<String, String> userHashmap = new HashMap<>();

    public void SetUserData(Activity activity){
        databaseReference_user.child(getUserID()).addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int childCount = (int) dataSnapshot.getChildrenCount();
                    userDataKeys.clear();
                    int x = 0;
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        userDataKeys.add(snapshot.getKey());
                        editor.putString(snapshot.getKey(), snapshot.getValue().toString());
                        x += 1;
                    }
                    setUserDataKeys(userDataKeys);
                    if(x >= childCount) {
                        canUpdate = true;
                        editor.commit();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(activity.getApplicationContext(), "Opss! Something went wrong!", Toast.LENGTH_SHORT).show();
                }
        });
    }



    public void SetSpecificQuizData(Activity activity, String quizID){
        DatabaseReference reference;

//        if (quizID == null)
//            reference = databaseReference_quiz.child(getUserID());
//        else
//            reference = databaseReference_quiz.child(quizID);

        reference = databaseReference_quiz.child(quizID);

        reference.addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int childCount = (int) dataSnapshot.getChildrenCount();
                    int x = 0;
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        editor_quiz.putString(snapshot.getKey(), snapshot.getValue().toString());
                        x += 1;
                    }
                    if(x >= childCount) {
                        canUpdateQuiz = true;
                        editor_quiz.commit();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(activity.getApplicationContext(), "Opss! Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    public void SetAllQuizData(Activity activity){
        DatabaseReference reference = databaseReference_quiz;

        reference.addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int childCount = (int) dataSnapshot.getChildrenCount();
                    int x = 0;
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        editor_quizKeys.putString("key_"+key, key);
                        editor_quiz.putString(key, snapshot.getValue().toString());
//                        Log.d("Quiz Data", session_quiz.getString(key, null));
                        x += 1;
                    }
                    Log.d("Quiz Data All", session_quiz.toString());
                    Log.d("Quiz DataKeys", editor_quizKeys.toString());

                    if(x >= childCount) {
                        canUpdateQuiz = true;
                        editor_quizKeys.commit();
                        editor_quiz.commit();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(activity.getApplicationContext(), "Opss! Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        );
    }


    public HashMap<String, String> GetAllQuizData(){
        Map <String, ?> sessions = session_quiz.getAll();
        HashMap<String, String> quizHashMap = new HashMap<>();

        for (Map.Entry <String, ?> entry: sessions.entrySet())
            quizHashMap.put(entry.getKey(), entry.getValue().toString());


        Log.d("Quiz Data All", quizHashMap.toString());
        Log.d("Quiz DataKeys", Long.toString(quizHashMap.size()));
        return quizHashMap;
    }


    public HashMap<String ,String > GetUserSession(){
        HashMap<String, String> userDataHashMap = new HashMap<>();
        for(String keys : userDataKeys )
            userDataHashMap.put(keys, session_user.getString(keys,null));
        return userDataHashMap;
    }

    public HashMap<String ,String > GetUserIndividualSession(){
        HashMap<String, String> userDataHashMap = new HashMap<>();

        userDataHashMap.put("academicProgram", session_user.getString("academicProgram",null));
        userDataHashMap.put("email", session_user.getString("email",null));
        userDataHashMap.put("firstName", session_user.getString("firstName",null));
        userDataHashMap.put("lastName", session_user.getString("lastName",null));
        userDataHashMap.put("location", session_user.getString("location",null));
        userDataHashMap.put("middleName", session_user.getString("middleName",null));
        userDataHashMap.put("password", session_user.getString("password",null));
        userDataHashMap.put("type", session_user.getString("type",null));

        return userDataHashMap;
    }

    public void LogoutSession(){
        editor.clear();
        editor_quizKeys.clear();
        editor_quiz.clear();

        editor.commit();
        editor_quizKeys.commit();
        editor_quiz.commit();
    }
    public void InitializePreferences(Context context){
        session_user = context.getSharedPreferences("UserLoginSession", Context.MODE_PRIVATE);
        editor = session_user.edit();
    }
    public void InitializeQuizPreferences(Context context){
        session_quiz = context.getSharedPreferences("QuizSessions", Context.MODE_PRIVATE);
        session_quizKeys = context.getSharedPreferences("QuizKeysSessions", Context.MODE_PRIVATE);
        editor_quiz = session_quiz.edit();
        editor_quizKeys = session_quizKeys.edit();
    }
}
