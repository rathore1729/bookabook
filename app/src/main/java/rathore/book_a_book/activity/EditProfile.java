package rathore.book_a_book.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import rathore.book_a_book.R;
import rathore.book_a_book.pojos.AnimateDialog;
import rathore.book_a_book.pojos.CircleImage;
import rathore.book_a_book.pojos.Links;
import rathore.book_a_book.pojos.UserPojo;

public class EditProfile extends AppCompatActivity {
    String loggeDIn;
    UserPojo user;
    Boolean picChanged,isDefaultImage;
    CircleImageView image;
    TextView email,phone,password,editChangePicture;
    EditText name;
    EditText address;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Your Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_profile);
        init();
        fetchValues();
    }

    @Override
    public void onBackPressed() {
        if(user.getNAME().equals(name.getText().toString())&&user.getADDRESS().equals(address.getText().toString())&&!picChanged){
            finish();
        } else{
            AlertDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.warning);
            builder.setTitle("Discard any changes made?");
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}
            });
            builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        if(user.getNAME().equals(name.getText().toString())&&user.getADDRESS().equals(address.getText().toString())&&!picChanged){
            finish();
        } else{
            AlertDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.warning);
            builder.setTitle("Discard any changes made?");
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}
            });
            builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialog = builder.create();
            dialog.show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.done){
            saveChanges();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        loggeDIn = getSharedPreferences("PREF_NAME",MODE_PRIVATE).getString("userPhone","none");
        picChanged  = false;
        name = (EditText) findViewById(R.id.editName);
        address = (EditText) findViewById(R.id.editAddress);
        password = (TextView) findViewById(R.id.editPass);
        phone = (TextView) findViewById(R.id.editPhone);
        email = (TextView) findViewById(R.id.editEmail);
        editChangePicture = (TextView) findViewById(R.id.editChangePicture);
        image = (CircleImageView) findViewById(R.id.editImage);
        image.setImageResource(R.drawable.user);
        isDefaultImage=true;
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp();
            }
        });
        editChangePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void fetchValues() {
        Links.showDialog(this,"Loading...");
        ref = FirebaseDatabase.getInstance().getReference("users").child(loggeDIn);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserPojo.class);
                name.setText(user.getNAME());
                email.setText(user.getEMAIL());
                phone.setText(user.getPHONE());
                password.setText(user.getPASSWORD());
                if (!user.getDP().equals("none"))
                    Glide.with(EditProfile.this).load(user.getDP()).placeholder(R.drawable.user).crossFade().dontAnimate().listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            isDefaultImage = true;
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            isDefaultImage = false;
                            return false;
                        }
                    }).into(image);
                if(!user.getADDRESS().equals("none"))
                    address.setText(user.getADDRESS());
                Links.cancelDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfile.this, "Network Error!!!", Toast.LENGTH_SHORT).show();
                Links.cancelDialog();
            }
        });
    }

    private void saveChanges() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.warning);
        builder.setTitle("Save Changes to your profile?");
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        builder.setNegativeButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(name.getText().toString().trim().equals(""))
                    Toast.makeText(EditProfile.this, "Name field can not be empty!!!", Toast.LENGTH_SHORT).show();
                else {
                    ref.child("name").setValue(name.getText().toString());

                    if (address.getText().toString().trim().equals(""))
                        ref.child("address").setValue("none");
                    else
                        ref.child("address").setValue(address.getText().toString());

                    if (isDefaultImage) {
                        ref.child("dp").setValue("none");
                    } else {
                        StorageReference storage = FirebaseStorage.getInstance().getReference().child(loggeDIn);
                        Toast.makeText(EditProfile.this, "Saving changes and Uploading Image", Toast.LENGTH_SHORT).show();
                        storage.putFile(Uri.parse(user.getDP())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                @SuppressWarnings("VisibleForTests") Uri uri = taskSnapshot.getDownloadUrl();
                                Toast.makeText(EditProfile.this, "Image Uploaded successfully", Toast.LENGTH_SHORT).show();
                                ref.child("dp").setValue(uri.toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProfile.this, "Upload Image operation Failed!!!", Toast.LENGTH_SHORT).show();
                                Log.d("12345", "onFailure: " + e);
                            }
                        });
                        finish();
                    }
                }
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public void changePassword(View view) {
        LoginPage loggin = new LoginPage();
        loggin.showSecDialog(EditProfile.this,true);
    }

    public void selectImage(){
        if(checkGalleryPermission()){
            Intent i = new Intent();
            i.setAction(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i,30);
        }
        else { // request is asked for acitivity so ActivityCompat
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
            selectImage();
        }
    }

    public void showPopUp(){
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        if(isDefaultImage){
            String[] option = {"Set New Image"};
            builder.setItems(option, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(i==0)
                        selectImage();
                }
            });
        } else {
            String[] option = {"View Image","Change Image","Delete Image"};
            builder.setItems(option, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i){
                        case 0 : showImageDialog();break;
                        case 1 : selectImage();break;
                        case 2 : picChanged=true;
                                 isDefaultImage=true;
                                 image.setImageResource(R.drawable.user);
                                 break;
                        default: selectImage();
                    }
                }
            });
        }
        dialog = builder.create();
        dialog.show();
    }

    public void showImageDialog(){
        AnimateDialog dialog = new AnimateDialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_image_popup,null);
        ImageView image = view.findViewById(R.id.popupImage);
        Glide.with(this).load(user.getDP()).crossFade().fitCenter().into(image);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
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
        if(requestCode==30)
            if(resultCode==RESULT_OK){
                picChanged = true;
                isDefaultImage=false;
                picChanged=true;
                Uri uri = data.getData();
                user.setDP(uri+"");
                Glide.with(this).load(uri).crossFade().error(R.drawable.user).into(image);
            }
    }

    public void errorPhone(View view){
        Toast.makeText(this, "You can't change your Phone Number!!!", Toast.LENGTH_SHORT).show();
    }

    public void errorEmail(View view){
        Toast.makeText(this, "You can't change your Email Address!!!", Toast.LENGTH_SHORT).show();
    }
}