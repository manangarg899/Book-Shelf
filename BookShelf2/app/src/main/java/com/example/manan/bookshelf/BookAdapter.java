package com.example.manan.bookshelf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Manan on 15-02-2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout, parent, false);
        }

        Book currentBook = getItem(position);

        TextView author = (TextView) convertView.findViewById(R.id.author_name);
        author.setText(currentBook.getAuthor());

        TextView title = (TextView) convertView.findViewById(R.id.book_title);
        title.setText(currentBook.getTitle());

        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.image_view);

        Picasso.with(getContext()).load(currentBook.getImageUrl()).into(thumbnail);


        return convertView;
    }
}
