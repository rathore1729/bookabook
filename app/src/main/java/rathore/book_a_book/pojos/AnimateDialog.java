package rathore.book_a_book.pojos;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;

import rathore.book_a_book.R;

/**
 * Created by Rathore on 01-08-2017.
 */

public class AnimateDialog extends Dialog {

    public AnimateDialog(@NonNull Context context) {
        super(context);
    }

    public AnimateDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected AnimateDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }
}
