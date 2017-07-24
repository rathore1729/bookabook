package rathore.book_a_book.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import rathore.book_a_book.R;
import rathore.book_a_book.database.MyOpenHelper;
import rathore.book_a_book.pojos.Links;
import rathore.book_a_book.pojos.UserPojo;
import rathore.book_a_book.tables.UserDataTable;

public class LoginPage extends AppCompatActivity {
    ScrollView loginPage;
    TextView new_user,forgot;
    EditText email,pass;
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        init();
        methodListeners();
        }

    private void init() {
        loginPage = (ScrollView) findViewById(R.id.loginPage);
        new_user = (TextView) findViewById(R.id.new_user);
        forgot = (TextView) findViewById(R.id.forgot);
        email = (EditText) findViewById(R.id.login_email);
        pass = (EditText) findViewById(R.id.login_pass);
        login = (Button) findViewById(R.id.loginbtn);
    }

    private void methodListeners() {
        email.addTextChangedListener(myTextEmail);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyPad();
                final String usermail = email.getText().toString().trim();
                final String userpass = pass.getText().toString().trim();

                if (usermail.equals("") || userpass.equals("")) {
                    Toast.makeText(LoginPage.this, "Fill Details.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isNetworkAvailable()) {
                    Links.showDialog(LoginPage.this, "Please wait... ");
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Links.cancelDialog();
                            boolean emailExists = false, success = false;
                            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                emailExists = false;
                                success = false;
                                UserPojo user = snap.getValue(UserPojo.class);
                                if (usermail.equals(user.getEMAIL())) {
                                    emailExists = true;
                                    if (userpass.equals(user.getPASSWORD())) {
                                        success = true;
                                        break;
                                    }
                                    break;
                                }
                            }

                            if (emailExists) {
                                if (success) {
                                    if (usermail.equals("admin@bookabook.com"))
                                        startActivity(new Intent(LoginPage.this, AdminAddProduct.class));
                                    else {
                                        Intent intent = new Intent(LoginPage.this, HomePage.class);
                                        intent.putExtra("userMail", usermail);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    pass.setError("Wrong Password!!!");
                                    Snackbar.make(loginPage, "PASSWORD DOESN'T MATCH!!!", Snackbar.LENGTH_LONG).setAction("RESET", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            pass.setError(null);
                                            pass.setText("");
                                        }
                                    }).show();
                                }
                            } else {
                                email.setError("Wrong Email!!!");
                                Snackbar.make(loginPage, "We can't recognize this email.\nPlease check entered Email.", Snackbar.LENGTH_LONG).setAction("RESET", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        email.setError(null);
                                        email.setText("");
                                    }
                                }).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Links.cancelDialog();
                            Toast.makeText(LoginPage.this, "Network error!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {

                    Cursor cursor = UserDataTable.verifyAccount(new MyOpenHelper(LoginPage.this).getReadableDatabase(), UserDataTable.EMAIL + " = '" + usermail + "'");
                    if (cursor.moveToNext())
                        if (usermail.equals(cursor.getString(0)) && userpass.equals(cursor.getString(1))) {
                            if (usermail.equals("admin@bookabook.com"))
                                startActivity(new Intent(LoginPage.this, AdminAddProduct.class));
                            else {
                                Intent intent = new Intent(LoginPage.this, HomePage.class);
                                intent.putExtra("userMail", usermail);
                                startActivity(intent);
                                finish();
                            }
                        } else
                            Toast.makeText(LoginPage.this, "Wrong Password!!!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(LoginPage.this,"We can't recognize this email\nPlease check entered Email.", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyPad();
                if(isNetworkAvailable()){
                    Intent intent = new Intent(LoginPage.this,SignupPage.class);
                    startActivity(intent);
                    finish();
                }else{
                    Snackbar.make(view,"No Internet connection available. Please check your network settings.",Snackbar.LENGTH_LONG).show();
                }
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyPad();
                final Dialog dialog = new Dialog(LoginPage.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View inflateView = inflater.inflate(R.layout.dialog_forgot_email,null);
                final EditText email = inflateView.findViewById(R.id.forgot_email);
                final TextView question = inflateView.findViewById(R.id.forgot_Ques);
                final EditText answer = inflateView.findViewById(R.id.forgot_answer);
                final LinearLayout emailBack = inflateView.findViewById(R.id.forgot_email_back);
                final LinearLayout questionBack = inflateView.findViewById(R.id.forgot_ques_back);
                final LinearLayout answerBack = inflateView.findViewById(R.id.forgot_ans_back);
                final Button submit = inflateView.findViewById(R.id.forgot_Ques_but);
                final Button change_password = inflateView.findViewById(R.id.forgot_changePass);
                final String[] setEmail = new String[1];
                final String[] setQues = new String[1];
                final String[] setAnswer = new String[1];
                TextWatcher myNewTextEmail = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String mail = email.getText().toString();
                        if(!mail.equals(""))
                            if(!Pattern.matches("[\\w.@]+",mail))
                                email.setError("INVALID CHARACTERS IN EMAIL ADDRESS !!!");
                    }
                };
                email.addTextChangedListener(myNewTextEmail);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideKeyPad();
                        String usermail = email.getText().toString().trim();
                        if(usermail.equals(""))
                            Toast.makeText(LoginPage.this,"Please enter Email",Toast.LENGTH_LONG).show();
                        else{
                            Cursor cursor = UserDataTable.retrievePassword(new MyOpenHelper(LoginPage.this).getReadableDatabase(),UserDataTable.EMAIL + " = '" + usermail + "'");
                            if(cursor.moveToNext()) {
                                setEmail[0] = cursor.getString(0);
                                setQues[0] = cursor.getString(1);
                                setAnswer[0] = cursor.getString(2);
                                emailBack.setVisibility(View.GONE);
                                submit.setVisibility(View.GONE);
                                question.setText(setQues[0]);
                                questionBack.setVisibility(View.VISIBLE);
                                answerBack.setVisibility(View.VISIBLE);
                                change_password.setVisibility(View.VISIBLE);
                            }
                            else
                                Toast.makeText(LoginPage.this, "We don't recognize this email.\nCheck entered email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                change_password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideKeyPad();
                        String enteredAnswer = answer.getText().toString().trim().toUpperCase();
                        if(enteredAnswer.equals(setAnswer[0])){
                            Intent intentForgot = new Intent(LoginPage.this, ForgotPasswordActivity.class);
                            intentForgot.putExtra("resetpassEmail",setEmail[0]);
                            dialog.cancel();
                            startActivity(intentForgot);
                            finish();
                        }
                        else
                            Toast.makeText(LoginPage.this, "ANSWER DOESN'T MATCH", Toast.LENGTH_SHORT).show();
                    }
                });
                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(inflateView);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
        }catch (Exception e){
            Toast.makeText(this, "Error : " + e, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    TextWatcher myTextEmail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            String mail = email.getText().toString();
            if(!mail.equals(""))
                if(!Pattern.matches("[\\w.@]+",mail))
                    email.setError("INVALID CHARACTERS IN EMAIL ADDRESS !!!");
        }
    };

    private void hideKeyPad(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
}
