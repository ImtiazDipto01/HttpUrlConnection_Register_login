package com.nextdot.login_registration_praactice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by sakib on 6/18/2017.
 */

public class LoginActivity extends AppCompatActivity {

    Button login, reg_here ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.login);
        reg_here = (Button) findViewById(R.id.register_here) ;

        reg_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class) ;
                startActivity(intent);
            }
        });
    }
}
