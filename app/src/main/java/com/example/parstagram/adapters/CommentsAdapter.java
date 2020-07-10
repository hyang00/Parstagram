package com.example.parstagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parstagram.R;
import com.example.parstagram.models.Comment;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private Context context;
    private List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        comments.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Comment> list) {
        comments.addAll(list);
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(int index, List<Comment> list) {
        comments.addAll(index, list);
        notifyItemInserted(index);
        //notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivCommentProfilePic;
        private TextView tvCommentDescription;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCommentProfilePic = itemView.findViewById(R.id.ivCommentProfilePic);
            tvCommentDescription = itemView.findViewById(R.id.tvCommentDescription);

        }

        public void bind(Comment comment) {
            Log.i("hi", "" + comment.getParseUser("user").getUsername());
            String descriptionString = "<b>" + comment.getParseUser("user").getUsername() + "</b> " + comment.getDescription();
            //Log.i("hi", descriptionString);
            //tvCommentDescription.setText("" + Html.fromHtml(descriptionString));
            tvCommentDescription.setText(Html.fromHtml(descriptionString));

            //tvCommentDescription.setText(comment.getDescription());
            ParseFile image = comment.getUser().getParseFile("profileImage");
            if(image != null){
                Glide.with(context).load(image.getUrl()).transform(new CircleCrop()).into(ivCommentProfilePic);
            }
        }
    }
}
