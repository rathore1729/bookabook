package rathore.book_a_book.pojos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Rathore on 22-07-2017.
 */

public class Links {
    public static String SEARCH = "https://www.googleapis.com/books/v1/volumes?q=";
    public static String FILTER = "&langRestrict=en&maxResults=40";
    public static String ALL = "https://www.googleapis.com/books/v1/volumes?q=subject:adventure&langRestrict=en&maxResults=40";
    public static String MAGAZINE = "https://www.googleapis.com/books/v1/volumes?q=is+the+an+or+and&printType=magazines&langRestrict=en&maxResults=40";
    public static String NOVEL = "https://www.googleapis.com/books/v1/volumes?q=subject:novel&langRestrict=en&maxResults=40";
    public static String BUSINESS = "https://www.googleapis.com/books/v1/volumes?q=subject:business&langRestrict=en&maxResults=40";
    public static String FICTION = "https://www.googleapis.com/books/v1/volumes?q=subject:fiction&langRestrict=en&maxResults=40";
    public static String BIOGRAPHY = "https://www.googleapis.com/books/v1/volumes?q=subject:biography&langRestrict=en&maxResults=40";
    public static String INSPIRATION = "https://www.googleapis.com/books/v1/volumes?q=subject:inspiration&langRestrict=en&maxResults=40";
    public static String EDUCATION = "https://www.googleapis.com/books/v1/volumes?q=subject:education&langRestrict=en&maxResults=40";
    public static String RELIGIOUS = "https://www.googleapis.com/books/v1/volumes?q=subject:religion&langRestrict=en&maxResults=40";
    public static String NEWEST = "&orderBy=newest";
    public static String RELEVANCE = "&orderBy=relevance";
    public static ProgressDialog dialog;

    public static void showDialog(Context context,String message){
        dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void cancelDialog(){
        dialog.cancel();
    }

    public static void hideKeyPad(Activity context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),0);
    }
}
