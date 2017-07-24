package rathore.book_a_book.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
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

public class SignupPage extends AppCompatActivity {
    ScrollView signUp;
    EditText signName,signEmail,signNum,signPass,signConfPass;
    TextView registered;
    CheckBox signTerms;
    Button signBtnYES,signBtnNO;
    String ques; int ques_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        init();
        methodListeners();
    }

    private void methodListeners() {
        signName.addTextChangedListener(myTextName);
        signNum.addTextChangedListener(myTextNum);
        signEmail.addTextChangedListener(myTextEmail);
        signConfPass.addTextChangedListener(myTextConfPass);

        signBtnYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyPad();
                if(isNetworkAvailable())
                    validation();
                else
                    Snackbar.make(view,"No Internet connection available. Please check your network settings.",Snackbar.LENGTH_LONG).show();
                }
        });
        
        signBtnNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyPad();
                Intent intent = new Intent(SignupPage.this,LoginPage.class);
                startActivity(intent);
                finish();
            }
        });
        
        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupPage.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        signUp = (ScrollView) findViewById(R.id.signup);
        signName = (EditText) findViewById(R.id.signname);
        signEmail = (EditText) findViewById(R.id.signemail);
        signNum = (EditText) findViewById(R.id.signnum);
        signPass = (EditText) findViewById(R.id.signpass);
        signConfPass = (EditText) findViewById(R.id.signpasscon);
        registered = (TextView) findViewById(R.id.registered);
        signTerms = (CheckBox) findViewById(R.id.signterms);
        signBtnYES = (Button) findViewById(R.id.signbtnyes);
        signBtnNO = (Button) findViewById(R.id.signbtnno);
    }
    
    TextWatcher myTextName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            String name = signName.getText().toString();
            if(!name.equals(""))
                if(!Pattern.matches("[A-Za-z ]+",name))
                    signName.setError("INVALID CHARACTERS IN NAME !!!");
        }
    };
    
    TextWatcher myTextNum = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            String num = signNum.getText().toString();
            if(!num.equals(""))
                if(!Pattern.matches("[0-9]+",num))
                    signNum.setError("INVALID CHARACTERS IN PHONE NUMBER !!!");
        }
    };
    
    TextWatcher myTextEmail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            String email = signEmail.getText().toString();
            if(!email.equals(""))
                if(email.contains("@"))
                    if(!Pattern.matches("[\\w.]+[@][\\w.]*",email))
                        signEmail.setError("INVALID CHARACTERS IN EMAIL ADDRESS !!!");
                    else
                        if(!Pattern.matches("[\\w.@]+",email))
                            signEmail.setError("INVALID CHARACTERS IN EMAIL ADDRESS !!!");
        }
    };
    
    TextWatcher myTextConfPass = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            String confPass = signConfPass.getText().toString();
            if(!confPass.equals(""))
                if(!confPass.equals(signPass.getText().toString()))
                    signConfPass.setError("PASSWORD DOES NOT MATCH !!!");
        }
    };
    
    private void validation() {
        int flag = 0;
        String username = signName.getText().toString().trim();
        String usermail = signEmail.getText().toString().trim();
        String usercont_num = signNum.getText().toString();
        String userpass = signPass.getText().toString();
        String userconf = signConfPass.getText().toString();
        Boolean terms = signTerms.isChecked();

        if (username.equals("")) {
            signName.setError("PLEASE ENTER YOUR NAME");
            showError();
        } else if (!Pattern.matches("[A-Za-z ]+", username)) {
            signName.setError("INVALID CHARACTERS IN NAME !!!");
            showError();
        } else if (usercont_num.equals("")) {
            signNum.setError("PLEASE ENTER MOBILE NUMBER");
            showError();
        } else if (!Pattern.matches("[789]{1}[\\d]{9}", usercont_num)) {
            signNum.setError("MOBILE NUMBER INVALID OR TOO SHORT !!!");
            showError();
        } else if (usermail.equals("")) {
            signEmail.setError("PLEASE ENTER EMAIL ADDRESS");
            showError();
        } else if (!Pattern.matches("[\\w.]+@[a-zA-Z0-9_]+[.][a-z]+", usermail)) {
            signEmail.setError("INVALID EMAIL ADDRESS !!!");
            showError();
        } else if (userpass.equals("")) {
            signPass.setError("PLEASE ENTER PASSWORD");
            showError();
        } else if (!Pattern.matches(".{6,}", userpass)) {
            signPass.setError("PASSWORD MUST BE AT LEAST 6 CHARACTERS LONG !!!");
            showError();
        } else if (!Pattern.matches(".*[a-z]+.*", userpass) || !Pattern.matches(".*[A-Z]+.*", userpass) || !Pattern.matches(".*[0-9]+.*", userpass) || !Pattern.matches(".*[^\\w\\s].*+", userpass)) {
            signPass.setError("Password must contain one digit,one lowercase,one uppercase and one special character");
            showError();
        } else if (userconf.equals("")) {
            signConfPass.setError("PLEASE ENTER PASSWORD AGAIN");
            showError();
        } else if (!userpass.equals(userconf)) {
            signConfPass.setError("PASSWORD DOES'T MATCH !!!");
            showError();
        } else if(!terms) {
            Toast.makeText(this, "PLEASE ACCEPT TERMS AND CONDITIONS", Toast.LENGTH_SHORT).show();
        } else
            checkExisting(username, usermail, userpass, usercont_num);
    }

    private void showError() {
        Toast.makeText(this, "PLEASE ENTER ALL FIELDS CORRECTLY", Toast.LENGTH_SHORT).show();
    }

    private void setSecurity(final UserPojo user){
        final Dialog dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_security, null);
        final Spinner spinner = (Spinner) view.findViewById(R.id.sec_spinner);
        final EditText sec_ans = (EditText) view.findViewById(R.id.sec_ans);
        Button sec_submit = view.findViewById(R.id.sec_submit);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                TextView tv = (TextView) view;
                ques_pos = position;
                sec_ans.setText("");
                if(ques_pos==2)
                    sec_ans.setInputType(InputType.TYPE_CLASS_PHONE);
                else
                    sec_ans.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                ques = tv.getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        sec_ans.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(!sec_ans.getText().toString().equals(""))
                    if(spinner.getSelectedItemPosition()==0) {
                        Toast.makeText(SignupPage.this, "SELECT SECURITY QUESTION FIRST", Toast.LENGTH_SHORT).show();
                        sec_ans.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                }
            }
        });

        sec_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyPad();
                if(!isNetworkAvailable())
                    Snackbar.make(view,"No Internet connection available. Please check your network settings.",Snackbar.LENGTH_LONG).show();
                else {
                    if (ques_pos != 0) {
                        if (sec_ans.getText().toString().trim().equals(""))
                            Toast.makeText(SignupPage.this, "Please Enter answer to the Security Question", Toast.LENGTH_SHORT).show();
                        else {
                            user.setSEC_QUES(ques);
                            user.setANSWER(sec_ans.getText().toString().trim().toUpperCase());
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference userRef = firebaseDatabase.getReference("users");
                            userRef.child(user.getPHONE()).setValue(user);
                            registerUser(user);
                            dialog.cancel();
                        }
                    } else
                        Toast.makeText(SignupPage.this, "Please Select a Security Question", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void registerUser(UserPojo user){
        ContentValues cv = new ContentValues();
        cv.put(UserDataTable.NAME,user.getNAME());
        cv.put(UserDataTable.EMAIL,user.getEMAIL());
        cv.put(UserDataTable.PASSWORD,user.getPASSWORD());
        cv.put(UserDataTable.PHONE,user.getPHONE());
        cv.put(UserDataTable.BACKCOLOR,user.getBACKCOLOR());
        cv.put(UserDataTable.SEC_QUES,user.getSEC_QUES());
        cv.put(UserDataTable.ANSWER,user.getANSWER());
        cv.put(UserDataTable.VERIFICATION,"false");
        if(UserDataTable.insert(new MyOpenHelper(SignupPage.this).getWritableDatabase(),cv)>0.0)
            Toast.makeText(this, "SIGNED UP SUCCESSFULLY.\nPLEASE LOGIN NOW.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Signup Operation Failed!!!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SignupPage.this,LoginPage.class));
        finish();
    }

    private void hideKeyPad(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    private void checkExisting(final String username, final String usermail, final String userpass, final String usercont_num){
        Links.showDialog(this,"Fetching information... ");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Links.cancelDialog();
                boolean phoneExists = false, emailExists = false, success = true;
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    UserPojo user = snap.getValue(UserPojo.class);
                    if(usercont_num.equals(user.getPHONE()))
                        phoneExists = true;
                    if(usermail.equals(user.getEMAIL()))
                        emailExists = true;
                }

                if(phoneExists) {
                    signNum.setError("AN ACCOUNT ALREADY EXISTS WITH THE SAME PHONE NUMBER");
                    Toast.makeText(SignupPage.this, "AN ACCOUNT ALREADY EXISTS WITH THE SAME PHONE NUMBER", Toast.LENGTH_SHORT).show();
                    success = false;
                }

                if(emailExists) {
                    signEmail.setError("AN ACCOUNT ALREADY EXISTS WITH THE SAME EMAIL ADDRESS");
                    Toast.makeText(SignupPage.this, "AN ACCOUNT ALREADY EXISTS WITH THE SAME EMAIL ADDRESS", Toast.LENGTH_SHORT).show();
                    success = false;
                }

                if(success) {
                    Toast.makeText(SignupPage.this, "JUST ONE MORE STEP", Toast.LENGTH_SHORT).show();
                    UserPojo user = new UserPojo();
                    user.setNAME(username);
                    user.setEMAIL(usermail);
                    user.setPASSWORD(userpass);
                    user.setPHONE(usercont_num);
                    user.setBACKCOLOR();
                    setSecurity(user);
                } else{
                    final boolean finalEmailExists = emailExists;
                    final boolean finalPhoneExists = phoneExists;
                    Snackbar.make(signUp,"USER ALREADY REGISTERED!!!",Snackbar.LENGTH_LONG).setAction("RESET", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(finalEmailExists) {
                                signEmail.setText("");
                                signEmail.setError(null);
                            }
                            if(finalPhoneExists){
                                signNum.setText("");
                                signNum.setError(null);
                            }
                        }
                    }).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Links.cancelDialog();
                Toast.makeText(SignupPage.this, "Network error!!!", Toast.LENGTH_SHORT).show();
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
}
