package rathore.book_a_book.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import rathore.book_a_book.R;
import rathore.book_a_book.database.MyOpenHelper;
import rathore.book_a_book.pojos.Links;
import rathore.book_a_book.tables.UserDataTable;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText resetPass,resetPassConf;
    Button resetYes,resetNo;
    String resetpassPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Intent intent = this.getIntent();
        resetpassPhone = intent.getStringExtra("resetpass");
        Toast.makeText(this, "Resetting Password for " + resetpassPhone, Toast.LENGTH_SHORT).show();

        init();
        methodListeners();
    }

    private void init() {
        resetPass = (EditText) findViewById(R.id.resetPass);
        resetPassConf = (EditText) findViewById(R.id.resetPassConf);
        resetYes = (Button) findViewById(R.id.resetbtnyes);
        resetNo = (Button) findViewById(R.id.resetbtnno);
    }

    private void methodListeners() {
        resetPassConf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String confPass = resetPassConf.getText().toString();
                if(!confPass.equals(""))
                    if(!confPass.equals(resetPass.getText().toString()))
                        resetPassConf.setError("PASSWORD DOES NOT MATCH !!!");
            }
        });

        resetYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Links.hideKeyPad(ForgotPasswordActivity.this);
                validate();
            }
        });
        resetNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Links.hideKeyPad(ForgotPasswordActivity.this);
                finish();
            }
        });
    }
    
    private void validate(){
        String userpass = resetPass.getText().toString();
        String userconf = resetPassConf.getText().toString();
        int flag = 0;
        if(userpass.equals("")){
            resetPass.setError("PLEASE ENTER PASSWORD.");
            flag=1;
        }
        else if(!Pattern.matches(".{6,}",userpass) ){
            resetPass.setError("PASSWORD MUST BE AT LEAST 6 CHARACTORS LONG !!!");
            flag=1;
        }else if( !Pattern.matches(".*[a-z]+.*",userpass) || !Pattern.matches(".*[A-Z]+.*",userpass) || !Pattern.matches(".*[0-9]+.*",userpass) || !Pattern.matches(".*[^\\w\\s].*+",userpass) ) {
            resetPass.setError("Password must contain one lowercase,one uppercase,one digit and one special character.");
            flag = 1;
        }

        if(userconf.equals("")){
            resetPassConf.setError("PLEASE ENTER PASSWORD AGAIN");
            flag=1;
        }
        else if(!userpass.equals(userconf)){
            resetPassConf.setError("PASSWORD DOES'T MATCH !!!");
            flag=1;
        }

        if(flag!=0)
            Toast.makeText(this, "PLEASE ENTER PASSWORD CORRECTLY", Toast.LENGTH_SHORT).show();
        else{
            FirebaseDatabase.getInstance().getReference("users").child(resetpassPhone).child("password").setValue(userconf+"");
            Toast.makeText(this, "PASSWORD RESET SUCCESSFULLY.", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },2000);
        }
    }
}
