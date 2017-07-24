package rathore.book_a_book.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import rathore.book_a_book.R;
import rathore.book_a_book.adapter.CategoryAdapter;
import rathore.book_a_book.database.MyOpenHelper;
import rathore.book_a_book.pojos.BookDeal;
import rathore.book_a_book.pojos.Links;
import rathore.book_a_book.tables.UserDataTable;

import static rathore.book_a_book.R.id.deal;

public class HomePage extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    TextView userName,userMail,userDP;
    MyViewPager myPager;
    ArrayList<BookDeal> arraylist;
    CategoryAdapter myPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        fetchValues("");
        myPagerAdapter = new CategoryAdapter(this,R.layout.dealitem,arraylist);
        myPager.setAdapter(myPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This is gonna be edited", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void init(){
        userDP = (TextView) findViewById(R.id.userDP);
        userName = (TextView) findViewById(R.id.userName);
        userMail = (TextView) findViewById(R.id.userEmail);
        myPager = (MyViewPager) findViewById(deal);
        arraylist = new ArrayList<>();



        StringRequest request = new StringRequest(Request.Method.GET, Links.ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fetchValues(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomePage.this, "Error in web request : " + error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue q = Volley.newRequestQueue(this);
        q.add(request);
        Timer timer = new Timer();
        timer.schedule(new MyTimeTask(),3000,3000);
    }

    private void showUser(){
        Cursor cursor = UserDataTable.select(new MyOpenHelper(this).getReadableDatabase(),UserDataTable.EMAIL + " = '" + getIntent().getStringExtra("userMail") + "'");
        cursor.moveToLast();
        userName.setText(cursor.getString(0));
        userMail.setText(getIntent().getStringExtra("userMail"));
        userDP.setText((cursor.getString(0).charAt(0)+"").toUpperCase());
        GradientDrawable drawable = new GradientDrawable();
        drawable = (GradientDrawable) userDP.getBackground();
        drawable.setColor(cursor.getInt(5));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fetchValues(String response) {
        if(response.equals("")){
            try {
                BookDeal deal = new BookDeal();
                deal.setName("Deals of the Day");         //Title
                deal.setAuthor("- Book A Book");    //Authors
                deal.setDesc(("Great books with Great discounts are being brought for you. Please wait. It is about books,and it is about great deals."));  //description
                double mrp = Math.random() * 200 + 220;                     //MRP
                int disc = (int) (Math.random() * 20 + 30);                 //Discount
                deal.setDisc(disc + "% OFF");
                deal.setMrp((mrp + "").substring(0,6));
                deal.setPrice((mrp * (100 - disc) / 100 + "").substring(0,6));               //Price
                deal.setImgURI(("http://books.google.com/books/content?id=vH8uAAAAYAAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api"));
                arraylist.add(deal);
            } catch (Exception e) {
                Toast.makeText(this, "Error in loop : " + e, Toast.LENGTH_SHORT).show();
            }
        }else{
            JSONObject data = null;
            try {
                data = (JSONObject) new JSONParser().parse(response);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            JSONArray array = (JSONArray) data.get("items");

            int[] index = new int[40];                      // Random deal of the day
            for(int i = 0;i<40;i++)
                index[i]=i;
            int tmp,random;
            for(int i=0;i<40;i++){
                random = (int) (Math.random()*39);
                tmp = index[random];
                index[random] = index[i];
                index[i] = tmp;
            }

            for(int i=0;i<5;i++)
            try{
            BookDeal deal = new BookDeal();
            JSONObject obj = (JSONObject) array.get(index[i]);
            JSONObject innerObj = (JSONObject) obj.get("volumeInfo");

            deal.setSelfLink((String) obj.get("selfLink"));         //SelfLink
            deal.setName((String) innerObj.get("title"));         //Title
            JSONArray auth = (JSONArray) innerObj.get("authors");    //Authors
            String authStr="";
            for (int j=0;j<auth.size();j++)
                authStr = auth.get(j) + ",";
            deal.setAuthor("- " + authStr.substring(0,authStr.length()-1));
            deal.setDesc((innerObj.get("description")+"12345678901234567890123456789012345678901234567890").substring(0,80)+"  ...and more");  //description

            double mrp = Math.random() * 200 + 220;                     //MRP
            int disc = (int) (Math.random() * 20 + 30);                 //Discount
            deal.setDisc(disc + "% OFF");
            deal.setMrp((mrp + "").substring(0,6));
            deal.setPrice((mrp * (100 - disc) / 100 + "").substring(0,6));               //Price
            JSONObject desc = (JSONObject) obj.get("volumeInfo");
            JSONObject img = (JSONObject) desc.get("imageLinks");   //Image
            deal.setImgURI((img.get("smallThumbnail")+""));
            arraylist.add(i,deal);
            myPagerAdapter = new CategoryAdapter(this,R.layout.dealitem,arraylist);
            myPager.setAdapter(myPagerAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error in hii : " + e, Toast.LENGTH_SHORT).show();
        }
        }
    }

    class MyTimeTask extends TimerTask{

        @Override
        public void run() {
            myPager.post(new Runnable() {
                public void run() {
                    if (myPager.getCurrentItem() < 4)
                        myPager.setCurrentItem(myPager.getCurrentItem() + 1, true);
                    else
                        myPager.setCurrentItem(0, true);
                }
            });
        }
    }
    
    public void showAll(View view){
        Intent intent = new Intent(HomePage.this,ProductListingActivity.class);
        intent.putExtra("link",Links.ALL);
        intent.putExtra("title","ALL BOOKS");
        startActivity(intent);
    }

    public void showNovel(View view){
        Intent intent = new Intent(HomePage.this,ProductListingActivity.class);
        intent.putExtra("link",Links.NOVEL);
        intent.putExtra("title","NOVELS");
        startActivity(intent);
    }

    public void showMagazine(View view){
        Intent intent = new Intent(HomePage.this,ProductListingActivity.class);
        intent.putExtra("link",Links.MAGAZINE);
        intent.putExtra("title","MAGAZINES");
        startActivity(intent);
    }

    public void showEducation(View view){
        Intent intent = new Intent(HomePage.this,ProductListingActivity.class);
        intent.putExtra("link",Links.EDUCATION);
        intent.putExtra("title","EDUCATION");
        startActivity(intent);
    }

    public void showBusiness(View view){
        Intent intent = new Intent(HomePage.this,ProductListingActivity.class);
        intent.putExtra("link",Links.BUSINESS);
        intent.putExtra("title","BUSINESS");
        startActivity(intent);
    }

    public void showFiction(View view){
        Intent intent = new Intent(HomePage.this,ProductListingActivity.class);
        intent.putExtra("link",Links.FICTION);
        intent.putExtra("title","FICTION");
        startActivity(intent);
    }

    public void showBiography(View view){
        Intent intent = new Intent(HomePage.this,ProductListingActivity.class);
        intent.putExtra("link",Links.BIOGRAPHY);
        intent.putExtra("title","BIOGRAPHIES");
        startActivity(intent);
    }

    public void showInspiration(View view){
        Intent intent = new Intent(HomePage.this,ProductListingActivity.class);
        intent.putExtra("link",Links.INSPIRATION);
        intent.putExtra("title","INSPIRATION");
        startActivity(intent);
    }

    public void showReligious(View view){
        Intent intent = new Intent(HomePage.this,ProductListingActivity.class);
        intent.putExtra("link",Links.RELIGIOUS);
        intent.putExtra("title","RELIGION");
        startActivity(intent);
    }
}

class MyViewPager extends ViewPager{

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        // Unspecified means that the ViewPager is in a ScrollView WRAP_CONTENT.
        // At Most means that the ViewPager is not in a ScrollView WRAP_CONTENT.
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            // super has to be called in the beginning so the child views can be initialized.
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) height = h;
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        // super has to be called again so the new specs are treated as exact measurements
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
