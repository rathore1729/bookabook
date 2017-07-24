package rathore.book_a_book.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import rathore.book_a_book.R;
import rathore.book_a_book.pojos.BookDeal;
import rathore.book_a_book.pojos.BookDeal;

/**
 * Created by Rathore on 22-07-2017.
 */

public class ListingAdapter extends ArrayAdapter {
    Context context;
    int layout;
    ArrayList<BookDeal> arrayList;
    LayoutInflater inflater;
    public ListingAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<BookDeal> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        layout = resource;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(layout,null);
        TextView productName,productAuthor,productPrice,productMRP,productDisc;
        ImageView productImage;
        productName = view.findViewById(R.id.listingName);
        productAuthor = view.findViewById(R.id.listingAuthor);
        productPrice = view.findViewById(R.id.listingPrice);
        productMRP = view.findViewById(R.id.listingMRP);
        productDisc = view.findViewById(R.id.listingDisc);
        productImage = view.findViewById(R.id.listingImage);
        BookDeal product = arrayList.get(position);
        productName.setText(product.getName());
        productAuthor.setText(product.getAuthor());
        productPrice.append(product.getPrice());
        productMRP.append(product.getMrp());
        productDisc.setText(product.getDisc());
        Glide.with(context)
                .load(Uri.parse(product.getImgURI()))
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(productImage);
        return view;
    }
}
