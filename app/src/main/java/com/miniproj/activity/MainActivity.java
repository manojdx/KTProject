package com.miniproj.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.miniproj.R;
import com.miniproj.activity.Utils.PreferenceManager;
import com.miniproj.activity.Utils.Preference_Details;
import com.miniproj.activity.interfaces.Preference_Keys;
import com.miniproj.activity.realm.Register;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    TextInputEditText uname,pass;
    AppCompatButton loginBtn;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageButton gmail;
    TextView signup;
    private Realm realm;
    Boolean status = false;
    private PreferenceManager mPrefernce_Manager= PreferenceManager.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        gmail = findViewById(R.id.gmail);
        signup = findViewById(R.id.signup);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);
        uname = findViewById(R.id.uname);
        pass = findViewById(R.id.pass);
        gmail.setOnClickListener(this);
        if (Preference_Details.getLoginStatus() == true) {
            navigateToSecondActivity();
        }
        signup.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.login_btn:
                if (uname.getText().toString().trim().equals("manoj")&&pass.getText().toString().equals("test123")) {
                    System.out.println("Login Successful");
                    mPrefernce_Manager.setString(Preference_Keys.LOginKeys.displayName,uname.getText().toString());
                    mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.LoginStatus,true);
                    Intent signup = new Intent(getApplicationContext(),dashboardActivity.class);
                    startActivity(signup);
                }

                break;
            case R.id.gmail:
                signIn();
                break;
            case R.id.signup:
                signup();
                break;
        }
    }

    private void signup() {
        Intent signUpScreen = new Intent(getApplicationContext(),SignupActivity.class);
        startActivity(signUpScreen);
    }

    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                registerData(task);
             //   navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void registerData(Task<GoogleSignInAccount> task) {


        // on below line we are getting id for the course which we are storing.
       // Number id = realm.where(RealmDb.class).max("id");
        RealmResults<Register> regList= realm.where(Register.class).findAll();
        for(Register reg: regList) {
            if (reg.getSid().equals(task.getResult().getId())) {
                status = true;
                setSessionData(reg);
                break;
            } else {
                status = false;
            }
        }

        if(status){

            navigateToSecondActivity();
        } else {
            addDataToDatabase(task);
        }



        if (task.getResult().getEmail()!=null) {

        }
        if (task.getResult().getDisplayName()!=null) {

        }
        if (task.getResult().getId()!=null) {

        }

    }

    private void setSessionData(Register modal) {
        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.SID,modal.getSid());
        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.displayName,modal.getName());
        mPrefernce_Manager.setString(Preference_Keys.LOginKeys.EmailID, modal.getEmail());
        mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.LoginStatus,true);

    }

    void navigateToSecondActivity(){

        finish();
        Intent intent = new Intent(MainActivity.this,dashboardActivity.class);
        startActivity(intent);
    }

    private void addDataToDatabase(Task<GoogleSignInAccount> task) {

        // on below line we are creating
        // a variable for our modal class.
        Register modal = new Register();

        // on below line we are getting id for the course which we are storing.
        Number id = realm.where(Register.class).max("id");

        // on below line we are
        // creating a variable for our id.
        long nextId;

        // validating if id is null or not.
        if (id == null) {
            // if id is null
            // we are passing it as 1.
            nextId = 1;
        } else {
            // if id is not null then
            // we are incrementing it by 1
            nextId = id.intValue() + 1;
        }
        // on below line we are setting the
        // data entered by user in our modal class.
        modal.setId(nextId);
        modal.setSid(task.getResult().getId());
        modal.setEmail(task.getResult().getEmail());
        modal.setName(task.getResult().getDisplayName());

        // on below line we are calling a method to execute a transaction.
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // inside on execute method we are calling a method
                // to copy to real m database from our modal class.
                realm.copyToRealm(modal);
                setSessionData(modal);
                navigateToSecondActivity();

            }
        });
    }

}