package com.program.endtermexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupMenu extends AppCompatActivity {
    private Intent intent_login, intent_dashboard;
    private TextInputLayout textInputLayout_email, textInputLayout_fname, textInputLayout_mname,
            textInputLayout_lname, textInputLayout_pass, textInputLayout_prog, textInputLayout_loc;
    private RadioGroup radioGroup_type;
    private RadioButton radioButton_selectedType;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private RelativeLayout relativeLayout_modal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_menu);
        InitializeIntents();
        InitializeValues();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        ExtendedLayoutAccess.CheckConnection(this);
        ExtendedLayoutAccess.CheckConnectivity(this);
    }

    @Override
    protected void onPause() {
        if (ExtendedLayoutAccess.monitoringConnectivity) {
            final ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(ExtendedLayoutAccess.connectivityCallback);
            ExtendedLayoutAccess.monitoringConnectivity = false;
        }
        super.onPause();
    }

    private void InitializeIntents(){
        intent_login = new Intent(SignupMenu.this, LoginMenu.class);
        intent_dashboard = new Intent(SignupMenu.this, Dashboard.class);
    }
    private void InitializeValues(){
        textInputLayout_email = findViewById(R.id.textInputLayout_Email);
        textInputLayout_fname = findViewById(R.id.textInputLayout_Firstname);
        textInputLayout_mname = findViewById(R.id.textInputLayout_Middlename);
        textInputLayout_lname = findViewById(R.id.textInputLayout_Lastname);
        textInputLayout_pass = findViewById(R.id.textInputLayout_Password);
        textInputLayout_prog = findViewById(R.id.textInputLayout_Program);
        textInputLayout_loc = findViewById(R.id.textInputLayout_Location);

        radioGroup_type = findViewById(R.id.radioGroup_Type);

        relativeLayout_modal = findViewById(R.id.modal_message);

        ExtendedLayoutAccess.InitializeModal(relativeLayout_modal);

        rootNode = FirebaseDatabase.getInstance("https://endterm-exam-default-rtdb.firebaseio.com/");
        reference = rootNode.getReference("Users");
    }

    public void OnLogin(View view) {
        startActivity(intent_login);
        finish();
    }
    private boolean IsEmailValid(CharSequence email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void OnSignup(View view) {

        int selectedType = radioGroup_type.getCheckedRadioButtonId();
        radioButton_selectedType = radioGroup_type.findViewById(selectedType);

        String email = textInputLayout_email.getEditText().getText().toString().trim();
        String fname = textInputLayout_fname.getEditText().getText().toString().trim();
        String mname = textInputLayout_mname.getEditText().getText().toString().trim();
        String lname = textInputLayout_lname.getEditText().getText().toString().trim();
        String pass = textInputLayout_pass.getEditText().getText().toString();
        String program = textInputLayout_prog.getEditText().getText().toString().trim();
        String location = textInputLayout_loc.getEditText().getText().toString().trim();
        String type = radioButton_selectedType.getText().toString();

        UserHelper userHelper = new UserHelper(email, fname, mname, lname, pass, program, location, type);

        if (email.isEmpty() || fname.isEmpty() || mname.isEmpty() || lname.isEmpty() ||
                program.isEmpty() || location.isEmpty() || type.isEmpty()){
            if (email.isEmpty()){
                textInputLayout_email.requestFocus();
                textInputLayout_email.setError("Email is Empty");
            }else
                textInputLayout_email.setError(null);
            if (fname.isEmpty()){
                textInputLayout_fname.requestFocus();
                textInputLayout_fname.setError("First Name is Empty");
            }else
                textInputLayout_fname.setError(null);
            if(mname.isEmpty()){
                textInputLayout_mname.requestFocus();
                textInputLayout_mname.setError("Middle Name is Empty");
            }else
                textInputLayout_mname.setError(null);
            if(lname.isEmpty()){
                textInputLayout_lname.requestFocus();
                textInputLayout_lname.setError("Last Name is Empty");
            }else
                textInputLayout_lname.setError(null);
            if(pass.isEmpty()){
                textInputLayout_pass.requestFocus();
                textInputLayout_pass.setError("Password is Empty");
            }else
                textInputLayout_pass.setError(null);
            if(program.isEmpty()){
                textInputLayout_prog.requestFocus();
                textInputLayout_prog.setError("Program is Empty");
            }else
                textInputLayout_prog.setError(null);
            if(location.isEmpty()){
                textInputLayout_loc.requestFocus();
                textInputLayout_loc.setError("Location/Address is Empty");
            }else
                textInputLayout_loc.setError(null);
            Toast.makeText(this, "Please Fill up the forms to proceed", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.length() <= 6 || pass.isEmpty()){
            textInputLayout_pass.setError("Password length must be greater than 6");
            textInputLayout_pass.requestFocus();
            return;
        }else
            textInputLayout_pass.setError(null);
        if(!IsEmailValid(email)){
            textInputLayout_email.setError("Password length must be greater than 6");
            textInputLayout_email.requestFocus();
            return;
        }
        else
            textInputLayout_email.setError(null);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()){
                                reference.child("User_".concat(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                        .setValue(userHelper).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                FirebaseUser current_user = firebaseAuth.getCurrentUser();
                                                if (current_user.isEmailVerified()){
                                                    Toast.makeText(SignupMenu.this, "User Has been registered!", Toast.LENGTH_SHORT).show();
                                                    ExtendedLayoutAccess.HideModal();
                                                    startActivity(intent_dashboard);
                                                    finish();
                                                }else{
                                                    current_user.sendEmailVerification();
                                                    ExtendedLayoutAccess.ShowModal("Email Verification", "Please check your email to verify account. This method will help us confirm if your email is legitimate.");
                                                }
                                            }else
                                                Toast.makeText(SignupMenu.this, "Invalid Authentication! User has not been registered", Toast.LENGTH_SHORT).show();
                                        });
                        }
                        });
    }
}
