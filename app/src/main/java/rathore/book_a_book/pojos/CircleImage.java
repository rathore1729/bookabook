package rathore.book_a_book.pojos;

import android.content.Context;
import android.util.AttributeSet;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rathore on 31-07-2017.
 */

public class CircleImage extends CircleImageView {
    public CircleImage(Context context) {
        super(context);
    }

    public CircleImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = heightMeasureSpec;
        super.onMeasure(width, heightMeasureSpec);
    }
}
