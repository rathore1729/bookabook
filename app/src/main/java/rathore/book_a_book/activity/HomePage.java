package rathore.book_a_book.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import rathore.book_a_book.R;
import rathore.book_a_book.adapter.CategoryAdapter;
import rathore.book_a_book.pojos.BookDeal;
import rathore.book_a_book.pojos.Links;
import rathore.book_a_book.pojos.MyViewPager;
import rathore.book_a_book.pojos.UserPojo;

import static rathore.book_a_book.R.id.deal;

public class HomePage extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    View coordi;
    NavigationView navView;
    TextView userName,userMail;
    CircleImageView userDP;
    MyViewPager myPager;
    ArrayList<BookDeal> arraylist;
    CategoryAdapter myPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        fetchValues("");
        myPagerAdapter = new CategoryAdapter(this,R.layout.listitemdeal,arraylist);
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
        coordi = findViewById(R.id.coordi);
        navView = (NavigationView) findViewById(R.id.nav_view);
        View head = navView.getHeaderView(0);
        userDP = (CircleImageView) head.findViewById(R.id.userDP);
        userDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,EditProfile.class));
            }
        });
        userName = (TextView) head.findViewById(R.id.userName);
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,EditProfile.class));
            }
        });
        userMail = (TextView) head.findViewById(R.id.userEmail);
        userMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,EditProfile.class));
            }
        });
        showUser();
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
        SharedPreferences sharedUser = getSharedPreferences("PREF_NAME",MODE_PRIVATE);
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(sharedUser.getString("userPhone","none"));;
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserPojo user = dataSnapshot.getValue(UserPojo.class);
                userName.setText(user.getNAME());
                userMail.setText(user.getEMAIL());
                if(user.getDP().equals("none"))
                    userDP.setImageResource(R.drawable.user);
                else
                    Glide.with(HomePage.this).load(Uri.parse(user.getDP())).crossFade().error(R.drawable.user).into(userDP);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomePage.this, "Network Error!!!", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        final SearchView search = (SearchView) menuItem.getActionView();
        /*ImageView img = search.findViewById(android.support.v7.appcompat.R.id.search_button);
        img.setImageResource(R.drawable.user);*/
        final EditText edit = search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        edit.setHint("Search Books");
        edit.setHintTextColor(Color.parseColor("#FF949494"));
        edit.setTextColor(Color.parseColor("#FF484848"));
        edit.setBackgroundResource(R.drawable.searchbar);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.isEmpty()||query.equals(""))
                    search.setIconified(true);
                else{
                    search.setIconified(true);
                    Intent intent = new Intent(HomePage.this,ProductListingActivity.class);
                    query = query.trim();
                    if(query.contains(" "))
                        query = query.replace(" ","+");
                    if(query.contains("\n"))
                        query = query.replace(" ","+");
                    String link = Links.SEARCH + query + Links.FILTER;
                    intent.putExtra("link",link);
                    intent.putExtra("title","Search Results");
                    startActivity(intent);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()) {
                    //Toast.makeText(HomePage.this, "no", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        //edit.setBackgroundColor(Color.parseColor("#f8f9fa")); To set Background simply to a color
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit) {
            startActivity(new Intent(HomePage.this,EditProfile.class));
        } else if (id == R.id.nav_order) {
            startActivity(new Intent(HomePage.this,MyOrderActivity.class));
        } else if (id == R.id.nav_cart) {
            startActivity(new Intent(HomePage.this,MyCartActivity.class));
        } else if (id == R.id.nav_settings) {
            Snackbar.make(coordi,"Settings are disable for now.",Snackbar.LENGTH_LONG).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Please share our APP", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_contact) {
            startActivity(new Intent(HomePage.this,ContactUsActivity.class));
        } else if (id == R.id.nav_logout) {
            AlertDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.warning);
            builder.setTitle("Sure to Logout?");
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}
            });
            builder.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences.Editor editor = getSharedPreferences("PREF_NAME",MODE_PRIVATE).edit();
                    editor.putString("userPhone","none");
                    editor.commit();
                    startActivity(new Intent(HomePage.this,LoginPage.class));
                    finish();
                }
            });
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
            deal.setAuthor("By " + authStr.substring(0,authStr.length()-1));
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
            myPagerAdapter = new CategoryAdapter(this,R.layout.listitemdeal,arraylist);
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