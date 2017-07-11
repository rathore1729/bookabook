package rathore.book_a_book.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.xml.sax.Parser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Pattern;

import rathore.book_a_book.R;

public class AdminAddProduct extends AppCompatActivity {
    EditText name,ori_price,disc_price,desc;
    TextView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);
        init();
    }

    private void init() {
        name = (EditText) findViewById(R.id.product);
        ori_price = (EditText) findViewById(R.id.ori_price);
        disc_price = (EditText) findViewById(R.id.disc_price);
        desc = (EditText) findViewById(R.id.desc);
        image = (TextView) findViewById(R.id.image_uri);
    }

    public void selectImage(View view){
        if(checkGalleryPermission()){
            Intent i = new Intent();
            i.setAction(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i,0);
        }
        else { // request is asked for acitivity so ActivityCompat
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
            selectImage(view);
        }
    }

    private boolean checkGalleryPermission(){ //checking is done on existing context so contextCompat
        return ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0)
            if(resultCode==RESULT_OK){
                Uri uri = data.getData();
                image.setText(uri.toString());
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void checkData(View view){
        if(name.getText().toString().trim().equals(""))
            name.setError("This Field is empty!!!");
        else if(!Pattern.matches("[0-9]+",ori_price.getText().toString().trim()))
            ori_price.setError("This Field is invalid!!!");
        else if(!Pattern.matches("[0-9]+",disc_price.getText().toString().trim()))
            disc_price.setError("This Field is invalid!!!");
        else if(desc.getText().toString().trim().equals(""))
            desc.setError("This Field is empty!!!");
        else if(image.getText().toString().trim().equals(""))
            image.setError("Please select Image!!!");
        else{
            try{
                String filePath = this.getFilesDir().getPath() + "/books.json";
                FileReader file = new FileReader(filePath);
                JSONObject objRead = (JSONObject)new JSONParser().parse(file);
                JSONArray arrayRead = (JSONArray) objRead.get("1");
                int size = arrayRead.size();
                Toast.makeText(this, "data : " + objRead.toString(), Toast.LENGTH_SHORT).show();

                JSONObject objWrite = new JSONObject();
                JSONObject arrObj = new JSONObject();
                JSONArray arrayWrite = new JSONArray();
                FileWriter fWrite = new FileWriter(filePath);

                arrObj.put("name",name.getText().toString().trim());
                arrObj.put("ori_price",ori_price.getText().toString().trim());
                arrObj.put("disc_price",disc_price.getText().toString().trim());
                arrObj.put("desc",desc.getText().toString().trim());
                arrObj.put("image_uri",image.getText().toString().trim());

                arrayRead.add(size,arrObj);
                objWrite.put("1",arrayRead);

                fWrite.write(objWrite.toString());
                fWrite.flush();

            }
            catch(Exception ex){
                Toast.makeText(this, "Error in writing : "+ex, Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(this,ProductReview.class));
        }
    }
}
