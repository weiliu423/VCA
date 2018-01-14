package com.groupproject.cs3.vca;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.groupproject.cs3.vca.information.UserInformation;

public class signupActivity extends BaseActivity implements  View.OnClickListener{

        private EditText mName,mEmail,mPass, mPhone;
        private String userID;
        String name;
        String phone;
        String type;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReferenceFromUrl("https://vcafirebaseproject-40148.firebaseio.com");
        private FirebaseAuth mAuth;
        private Spinner spinner1;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signup);

            mName = findViewById(R.id.name);
            mEmail = findViewById(R.id.email);
            mPass = findViewById(R.id.password);
            mPhone = findViewById(R.id.phoneEditText);
            Button register = findViewById(R.id.registerBtn);
            Button cancel = findViewById(R.id.cancelBtn);
            findViewById(R.id.registerBtn).setOnClickListener(this);

            cancel.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v) {

                    Toast.makeText(signupActivity.this.getApplicationContext(), "Registration Cancelled", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(signupActivity.this.getApplicationContext(), MainActivity.class);
                    startActivity(mainIntent);
                }
            });
            addListenerOnSpinnerItemSelection();
            mAuth = FirebaseAuth.getInstance();
            buttonTapEffect.buttonEffect(register);
            buttonTapEffect.buttonEffect(cancel);

        }
    public void addListenerOnSpinnerItemSelection() {
        spinner1 = findViewById(R.id.spinner1);
    }



        private void createAccount(String email, String password) {
           if (!validateForm()) {
               return;
           }
           showProgressDialog();
            name = mName.getText().toString();
            phone = mPhone.getText().toString();
            type =  String.valueOf(spinner1.getSelectedItem());
            mAuth.createUserWithEmailAndPassword(email, password)
                   .addOnCompleteListener(signupActivity.this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               toastMessage("Create User: Success");
                               FirebaseUser user = mAuth.getCurrentUser();
                               updateUI(user);
                               userID = mAuth.getUid();
                               if (!name.equals("") && !phone.equals("")) {
                                   UserInformation userInformation = new UserInformation(name, phone, type, userID);
                                   myRef.child("users").child(userID).setValue(userInformation);
                                   toastMessage("New Information has been saved.");
                                   Intent myIntent = new Intent(signupActivity.this.getApplicationContext(), ModeActivity.class);
                                   signupActivity.this.startActivity(myIntent);
                                   startService(new Intent(signupActivity.this, AndroidLocationServices.class));
                                   mName.setText("");
                                   mEmail.setText("");
                                   mPass.setText("");
                                   mPhone.setText("");

                               } else {
                                   toastMessage("Fill out all the fields");
                               }
                           }else if (!isNetworkAvailable()) {
                               toastMessage("No Internet Access!!");

                           } else {
                               Toast.makeText(signupActivity.this, "Authentication failed",
                                       Toast.LENGTH_SHORT).show();

                           }
                           hideProgressDialog();
                       }
                   });
       }

    private boolean validateForm() {
        boolean valid = true;
         name = mName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mName.setError("Required.");
            valid = false;
        } else if(!name.matches("[a-zA-Z]+")) {
            mName.setError("Name Format Wrong");
            valid = false;
        }
        else
        {
            mName.setError(null);
        }

        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Required.");
            valid = false;
        } else if(!isValidEmail(email)) {
            mEmail.setError("Wrong Email Format.");
            valid = false;
        }
        else
        {
            mEmail.setError(null);
        }

        String password = mPass.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPass.setError("Required.");
            valid = false;
        }else if(password.length() < 6) {
            mPass.setError("Minimum 6 Password Length Required.");
            valid = false;
        }else
        {
            mPass.setError(null);
        }

        phone = mPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            mPhone.setError("Required.");
            valid = false;
        } else if(!phone.matches("[0-9]+")) {
            mPhone.setError("Please Enter Numbers");
            valid = false;
        }
        else
        {
            mPhone.setError(null);
        }


        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.registerBtn) {

           String email = mEmail.getText().toString();
           String password = mPass.getText().toString();

            createAccount(email,password);
        }
    }
    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            toastMessage("Email Not Right Format");
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
