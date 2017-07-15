package rathore.book_a_book.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import rathore.book_a_book.R;
import rathore.book_a_book.database.MyOpenHelper;
import rathore.book_a_book.pojos.UserPojo;
import rathore.book_a_book.tables.UserDataTable;

public class SignupPage extends AppCompatActivity {
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
                validation();
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

        if(username.equals("")) {
            signName.setError("PLEASE ENTER YOUR NAME");
            flag = 1;
        }else if(!Pattern.matches("[A-Za-z ]+",username)){
            signName.setError("INVALID CHARACTERS IN NAME !!!");
            flag=1;
        }else if(usercont_num.equals("")){
            signNum.setError("PLEASE ENTER MOBILE NUMBER");
            flag=1;
        }else if(!Pattern.matches("[789]{1}[\\d]{9}",usercont_num)){
            signNum.setError("MOBILE NUMBER INVALID OR TOO SHORT !!!");
            flag=1;
        }else if(usermail.equals("")){
            signEmail.setError("PLEASE ENTER EMAIL ADDRESS");
            flag=1;
        }else if(!Pattern.matches("[\\w.]+@[a-zA-Z0-9_]+[.][a-z]+",usermail)){
            signEmail.setError("INVALID EMAIL ADDRESS !!!");
            flag=1;
        }else if(checkExisting(usermail)){
            signEmail.setError("AN ACCOUNT ALREADY EXISTS WITH THE SAME EMAIL ADDRESS");
            flag=1;
        }else if(userpass.equals("")){
            signPass.setError("PLEASE ENTER PASSWORD");
            flag=1;
        }else if(!Pattern.matches(".{6,}",userpass) ){
            signPass.setError("PASSWORD MUST BE AT LEAST 6 CHARACTERS LONG !!!");
            flag=1;
        }else if( !Pattern.matches(".*[a-z]+.*",userpass) || !Pattern.matches(".*[A-Z]+.*",userpass) || !Pattern.matches(".*[0-9]+.*",userpass) || !Pattern.matches(".*[^\\w\\s].*+",userpass) ) {
            signPass.setError("Password must contain one digit,one lowercase,one uppercase and one special character");
            flag = 1;
        }else if(userconf.equals("")){
            signConfPass.setError("PLEASE ENTER PASSWORD AGAIN");
            flag=1;
        }else if(!userpass.equals(userconf)){
            signConfPass.setError("PASSWORD DOES'T MATCH !!!");
            flag=1;
        }

        if(flag!=0)
            Toast.makeText(this, "PLEASE ENTER ALL FIELDS CORRECTLY", Toast.LENGTH_SHORT).show();
        else if(!terms)
            Toast.makeText(this, "PLEASE ACCEPT TERMS AND CONDITIONS", Toast.LENGTH_SHORT).show();
            else {
            Toast.makeText(this, "JUST ONE MORE STEP", Toast.LENGTH_SHORT).show();
            UserPojo user = new UserPojo();
            user.setNAME(username);
            user.setEMAIL(usermail);
            user.setPASSWORD(userpass);
            user.setPHONE(usercont_num);
            user.setBACKCOLOR();
            setSecurity(user);
        }
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
                if(ques_pos!=0){
                    if(sec_ans.getText().toString().trim().equals(""))
                        Toast.makeText(SignupPage.this, "Please Enter answer to the Security Question", Toast.LENGTH_SHORT).show();
                    else{
                        user.setSEC_QUES(ques);
                        user.setANSWER(sec_ans.getText().toString().trim().toUpperCase());
                        registerUser(user);
                        dialog.cancel();
                    }
                }
                else
                    Toast.makeText(SignupPage.this, "Please Select a Security Question", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "SIGNED UP SUCCESSFULLY...\nPLEASE LOGIN NOW.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Signup Operation Failed!!!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SignupPage.this,LoginPage.class));
        finish();
    }

    private void hideKeyPad(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    private boolean checkExisting(String usermail){
        Cursor cursor = UserDataTable.askEmail(new MyOpenHelper(this).getReadableDatabase(),null);
        if(cursor!=null)
            while (cursor.moveToNext()){
                if(cursor.getString(0).equals(usermail))
                    return true;
            }
        return false;
    }
}
