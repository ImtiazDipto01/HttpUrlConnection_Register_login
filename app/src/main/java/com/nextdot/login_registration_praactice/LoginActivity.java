package com.nextdot.login_registration_praactice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by sakib on 6/18/2017.
 */

public class LoginActivity extends AppCompatActivity {

    Button login, reg_here ;
    String login_user_email, login_user_password ;
    EditText login_email, login_password ;
    AlertDialog.Builder fill_info, login_msg ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.login);
        reg_here = (Button) findViewById(R.id.register_here) ;
        login_email = (EditText) findViewById(R.id.login_email) ;
        login_password = (EditText) findViewById(R.id.login_password) ;

        reg_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class) ;
                startActivity(intent);
            }
        });

        login_user_email = login_email.getText().toString() ;
        login_user_password = login_password.getText().toString() ;

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(login_email.getText().toString().equals("") || login_password.getText().toString().equals("")){

                    fill_missing();
                    AlertDialog alertDialog = fill_info.create() ;
                    alertDialog.show();
                }
            }
        });


    }
    private void fill_missing(){
        fill_info = new AlertDialog.Builder(LoginActivity.this);
        fill_info.setTitle("Field missing") ;
        fill_info.setMessage("Please fill all Field perfectly") ;
        fill_info.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

}
