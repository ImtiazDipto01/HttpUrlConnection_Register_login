package com.nextdot.login_registration_praactice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by sakib on 6/18/2017.
 */

public class LoginActivity extends AppCompatActivity {

    Button login, reg_here ;
    String login_user_email, login_user_password ;
    EditText login_email, login_password ;
    AlertDialog.Builder fill_info, login_report ;

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


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(login_email.getText().toString().equals("") || login_password.getText().toString().equals("")){

                    fill_missing();
                    AlertDialog alertDialog = fill_info.create() ;
                    alertDialog.show();
                }

                else{

                    login_user_email = login_email.getText().toString() ;
                    login_user_password = login_password.getText().toString() ;

                    ApiTaskLogin apiTaskLogin = new ApiTaskLogin(LoginActivity.this) ;
                    apiTaskLogin.execute(login_user_email, login_user_password) ;
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

    public class ApiTaskLogin extends AsyncTask<String, Void, String>{

        String login_url = "http://programmerimtiaz.000webhostapp.com/login.php" ;
        Context context ;
        ProgressDialog progressDialog ;

        public ApiTaskLogin(Context context){
            this.context = context ;
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            login_report = new AlertDialog.Builder(context);
            progressDialog = new ProgressDialog(context) ;
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Loading Please Wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(login_url) ;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection() ;
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream() ;
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8")) ;

                String login_email = params[0] ;
                String login_password = params[1] ;

                String data = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(login_email, "UTF-8")+"&"+
                        URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(login_password, "UTF-8") ;

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream() ;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)) ;
                StringBuilder stringBuilder = new StringBuilder() ;
                String line = "" ;

                while((line = bufferedReader.readLine()) != null){

                    stringBuilder.append(line + "\n") ;
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim() ;
            }
            catch (MalformedURLException e) {
                Log.d("MalformedURLException :", String.valueOf(e)) ;
            }
            catch (IOException e) {
                Log.d("IOException :", String.valueOf(e)) ;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                progressDialog.dismiss();
                JSONObject jsonObject = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1)) ;
                JSONArray main_array = jsonObject.getJSONArray("server_response") ;

                Log.d("SIZEEEEEEEEE :", String.valueOf(main_array.length())) ;

                JSONObject main_obj = main_array.getJSONObject(0) ;
                String code = main_obj.getString("code") ;
                String message = main_obj.getString("message") ;

                Log.d("+++ code +++:", code) ;
                Log.d("+++ message +++:", message) ;

                if(code.equals("login_true")){
                    show_dailouge("Login Success", message, code);
                }
                else if(code.equals("login_flase")){
                    show_dailouge("Login Failed", message, code);
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void show_dailouge(String title, String message, String code){
        //login_report = new AlertDialog.Builder(LoginActivity.this);
        login_report.setTitle(title) ;
        if(code.equals("login_true") || code.equals("login_flase")){
            login_report.setMessage(message) ;
            login_report.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        AlertDialog alertDialog = login_report.create() ;
        alertDialog.show();
    }

}
