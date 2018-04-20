package com.example.dasser.popular.movies.stage2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dasser.popular.movies.stage2.R;
import com.example.dasser.popular.movies.stage2.utils.Utils;

import java.util.Arrays;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;

import static com.example.dasser.popular.movies.stage2.Constants.STRING_SEPARATOR;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{

    private List<String> authors, contents;
    private Context context;


    public ReviewsAdapter(Context context, String author, String content) {
        if (!author.trim().isEmpty())
            authors = Arrays.asList(author.split(STRING_SEPARATOR));
        contents = Arrays.asList(content.split(STRING_SEPARATOR));
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        ExpandableTextView content;

        ViewHolder(View itemView) {
            super(itemView);
            author =  itemView.findViewById(R.id.author);
            content =  itemView.findViewById(R.id.content);
        }
    }

    @NonNull
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.reviews_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewsAdapter.ViewHolder holder, final int position) {
        holder.author.setText(Utils.getAuthorFormat(context, authors.get(position)));
        final ExpandableTextView content = holder.content;
        content.setText(contents.get(position));
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content.isExpanded())
                    holder.content.collapse();
                else
                    holder.content.expand();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (authors == null)
            return 0;
        return authors.size();
    }

}
