package com.miniproj.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.miniproj.R;
import com.miniproj.activity.Utils.PreferenceManager;
import com.miniproj.activity.interfaces.Preference_Keys;
import com.miniproj.activity.realm.Register;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText name,pass,mob,country,loc;
    Button signup;
    private PreferenceManager mPrefernce_Manager= PreferenceManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.uname);
        pass = findViewById(R.id.pass);
        mob = findViewById(R.id.mob);
        country = findViewById(R.id.country);
        loc = findViewById(R.id.loc);
        signup = findViewById(R.id.signup_btn);
        signup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.signup_btn:
                System.out.println("signup Successful");

                if (!name.getText().toString().isEmpty() && !mob.getText().toString().isEmpty()) {
                    mPrefernce_Manager.setString(Preference_Keys.LOginKeys.displayName,name.getText().toString());
                    mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.LoginStatus,true);
                    Intent intent = new Intent(getApplicationContext(),dashboardActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Field Empty", Toast.LENGTH_SHORT).show();
                }

                break;



        }
    }
}