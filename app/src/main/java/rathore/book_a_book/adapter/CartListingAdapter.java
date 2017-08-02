package rathore.book_a_book.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import rathore.book_a_book.R;
import rathore.book_a_book.activity.HomePage;
import rathore.book_a_book.activity.MyCartActivity;
import rathore.book_a_book.pojos.OrderPojo;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rathore on 29-07-2017.
 */

public class CartListingAdapter extends ArrayAdapter {
    Context context;
    int layout;
    static ArrayList<OrderPojo> arrayList;
    LayoutInflater inflater;
    public CartListingAdapter(@NonNull final Context context, @LayoutRes int resource, @NonNull ArrayList<OrderPojo> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        layout = resource;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(arrayList.isEmpty()){
            MyCartActivity.showConShop();
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view = inflater.inflate(layout,null);
        ImageButton delete,add,remove;
        ImageView image;
        final TextView name,author,price,quan;
        name = view.findViewById(R.id.cartName);
        author = view.findViewById(R.id.cartAuthor);
        price = view.findViewById(R.id.cartTotal);
        quan = view.findViewById(R.id.cartQuan);
        image = view.findViewById(R.id.cartImage);
        delete = view.findViewById(R.id.cartDelete);
        add = view.findViewById(R.id.cartAdd);
        remove = view.findViewById(R.id.cartRemove);
        
        final OrderPojo order = arrayList.get(position);
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("cart");
        DatabaseReference cartRef = data.child(context.getSharedPreferences("PREF_NAME",MODE_PRIVATE).getString("userPhone","none"));
        final DatabaseReference orderRef = cartRef.child(order.getId());
        final int[] srcQuantity = {order.getQuantity()};
        final int srcPrice = order.getPrice();

        name.setText(order.getName());
        author.setText(order.getAuthor());
        price.setText("₹ " + srcPrice*srcQuantity[0]);
        quan.setText(srcQuantity[0]+"");
        Glide.with(context)
                .load(Uri.parse(order.getUri()))
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(srcQuantity[0] <5) {
                    srcQuantity[0]++;
                    orderRef.child("quantity").setValue(srcQuantity[0]);
                    order.setQuantity(srcQuantity[0]);
                    arrayList.remove(position);
                    arrayList.add(position,order);
                    notifyDataSetChanged();
                    price.setText("₹ " + (srcPrice* srcQuantity[0]));
                }
                else
                    Toast.makeText(context, "Maximum quantity in one order is 5", Toast.LENGTH_SHORT).show();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (srcQuantity[0] > 1) {
                    srcQuantity[0]--;
                    orderRef.child("quantity").setValue(srcQuantity[0]);
                    order.setQuantity(srcQuantity[0]);
                    arrayList.remove(position);
                    arrayList.add(position,order);
                    notifyDataSetChanged();
                    price.setText("₹ " + (srcPrice* srcQuantity[0]));
                }
                else
                    Toast.makeText(context, "Minimum quantity is 1", Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialogCofirm;
                AlertDialog.Builder build = new AlertDialog.Builder(context);
                build.setIcon(R.drawable.warning);
                build.setTitle("Sure to discard this item from cart???");
                build.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                build.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        arrayList.remove(position);
                        if(arrayList.isEmpty()){
                            MyCartActivity.showConShop();
                        }
                        orderRef.setValue(null);
                        notifyDataSetChanged();
                    }
                });
                dialogCofirm = build.create();
                dialogCofirm.show();
                dialogCofirm.setCancelable(false);
            }
        });

        return view;
    }

    public static int getCounting(){
        return arrayList.size();
    }
}