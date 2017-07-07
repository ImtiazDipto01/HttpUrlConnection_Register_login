package com.nextdot.login_registration_praactice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by sakib on 6/18/2017.
 */

public class RegistrationActivity extends AppCompatActivity {

    Button register, login_here ;
    EditText reg_name, reg_email, reg_pass ;
    AlertDialog.Builder fill_info, reg_report ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        register = (Button) findViewById(R.id.register);
        login_here = (Button) findViewById(R.id.login_here);

        reg_name = (EditText) findViewById(R.id.reg_name) ;
        reg_email = (EditText) findViewById(R.id.reg_email) ;
        reg_pass = (EditText) findViewById(R.id.reg_pass) ;

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reg_name.getText().toString().equals("") || reg_email.getText().toString().equals("") || reg_pass.getText().toString().equals("")){

                    fill_missing();
                    AlertDialog alertDialog = fill_info.create() ;
                    alertDialog.show();
                }
                else{
                    String name = reg_name.getText().toString() ;
                    String email = reg_email.getText().toString() ;
                    String pass = reg_pass.getText().toString() ;

                    ApiTaskRegistration APIcall = new ApiTaskRegistration(RegistrationActivity.this) ;
                    APIcall.execute(name, email, pass) ;
                }
            }
        });
    }

    private void fill_missing(){
        fill_info = new AlertDialog.Builder(RegistrationActivity.this);
        fill_info.setTitle("Field missing") ;
        fill_info.setMessage("Please fill all Field perfectly") ;
        fill_info.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public class ApiTaskRegistration extends AsyncTask<String, Void, String>{

        Context context ; // we didn't pass the activity inst prabesh r k ;
        String register_url = "http://programmerimtiaz.000webhostapp.com/register.php" ;
        ProgressDialog progressDialog ;

        public ApiTaskRegistration(Context context){
            this.context = context ;
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            reg_report = new AlertDialog.Builder(context) ;
            progressDialog = new ProgressDialog(context) ;
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Connecting to server...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(register_url) ;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection() ;
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream() ;
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8")) ;

                String name = params[0] ;
                String email = params[1] ;
                String password = params[2] ;

                String data = URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&"+
                        URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")+"&"+
                        URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8") ;

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream() ;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")) ;
                StringBuilder stringBuilder = new StringBuilder() ;
                String line = "" ;

                while((line = bufferedReader.readLine())!= null){

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
                Log.d("my JSON ::::::",json) ;

                JSONObject jsonObject = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1)) ;
                JSONArray mainarray = jsonObject.getJSONArray("server_response") ;

                Log.d("SIZEEEEEEEEE :", String.valueOf(mainarray.length())) ;

                JSONObject mainobj = mainarray.getJSONObject(0);
                String cd = mainobj.getString("code") ;
                String msg = mainobj.getString("message") ;

                Log.d("+++ code +++:", cd) ;
                Log.d("+++ message +++:", msg) ;

                if(cd.equals("reg_true")){
                    show_dailouge("Registration Success", msg, cd);
                }
                else if(cd.equals("reg_flase")){
                    show_dailouge("Registration Failed", msg, cd);
                }
            }
            catch (JSONException e) {
                Log.d("JSONException :", String.valueOf(e)) ;
            }

        }
    }
    public void show_dailouge(String title, String message, String code){
        //fill_info = new AlertDialog.Builder(RegistrationActivity.this);
        reg_report.setTitle(title) ;
        if(code.equals("reg_true") || code.equals("reg_false")){
            reg_report.setMessage(message) ;
            reg_report.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        AlertDialog alertDialog = reg_report.create() ;
        alertDialog.show();
    }
}
