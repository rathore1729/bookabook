package rathore.book_a_book.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import rathore.book_a_book.R;
import rathore.book_a_book.pojos.BookDeal;

/**
 * Created by Rathore on 14-Jul-17.
 */

public class CategoryAdapter extends PagerAdapter {
    Context context;
    int layout;
    LayoutInflater inflater;
    ArrayList<BookDeal> arrayList;

    public CategoryAdapter(Context ctx, int layout, ArrayList<BookDeal> data) {
        this.layout = layout;
        this.context = ctx;
        this.arrayList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object instantiateItem(View collection, final int position) {
        View view = inflater.inflate(layout,null);
        TextView name = view.findViewById(R.id.bookName);
        TextView desc = view.findViewById(R.id.bookDesc);
        TextView author = view.findViewById(R.id.bookAuthor);
        TextView price = view.findViewById(R.id.bookPrice);
        TextView mrp = view.findViewById(R.id.bookMRP);
        TextView disc = view.findViewById(R.id.bookDisc);
        ImageView img = view.findViewById(R.id.bookImage);

        BookDeal deal = arrayList.get(position);
        name.setText(deal.getName());
        desc.setText(deal.getDesc());
        author.setText(deal.getAuthor());
        price.append(deal.getPrice());
        mrp.append(deal.getMrp());
        disc.setText(deal.getDisc());
        Glide.with(context).load(deal.getImgURI()).diskCacheStrategy(DiskCacheStrategy.ALL).into(img);

        ((ViewPager)collection).addView(view);
        return view;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public void startUpdate(View arg0) {
    }

    @Override
    public void finishUpdate(View arg0) {
    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }
}
