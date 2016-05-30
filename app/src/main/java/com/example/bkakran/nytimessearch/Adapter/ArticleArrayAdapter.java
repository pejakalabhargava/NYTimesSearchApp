package com.example.bkakran.nytimessearch.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bkakran.nytimessearch.R;
import com.example.bkakran.nytimessearch.activity.ArticleActivity;
import com.example.bkakran.nytimessearch.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by bkakran on 5/29/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, R.layout.item_article_result, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Article article = this.getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_article_result, parent, false);
        }
        // Lookup view for data population
        TextView tvTitme = (TextView) convertView.findViewById(R.id.tvTitle);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        imageView.setImageResource(0);
        // Populate the data into the template view using the data object
        tvTitme.setText(article.getHeadLine());
        if (!TextUtils.isEmpty(article.getThumbNail())) {
            //Picasso.with(getContext()).load(article.getThumbNail()).placeholder(R.drawable.user_placeholder)
              //      .error(R.drawable.user_placeholder_error).transform(new RoundedCornersTransformation(10, 10)).into(imageView);
            Glide.with(getContext())
                    .load(article.getThumbNail())
                    .into(imageView);
        } else {
            Picasso.with(getContext()).load(R.drawable.user_placeholder_error).placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error).transform(new RoundedCornersTransformation(10, 10)).into(imageView);

        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent articleIntent = new Intent(getContext(), ArticleActivity.class);
                articleIntent.putExtra("article",article);
                getContext().startActivity(articleIntent);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

}
