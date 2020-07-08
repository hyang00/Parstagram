package com.example.parstagram;

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
import org.parceler.Parcels;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(int index, List<Post> list) {
        posts.addAll(index, list);
        notifyItemInserted(index);
        //notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvDate;
        private ImageView ivProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvDate = itemView.findViewById(R.id.tvDate);
            itemView.setOnClickListener(this);

        }

        public void bind(Post post) {
            // bind the post data to the view elements
            tvUsername.setText(post.getUser().getUsername());
            String descriptionString = "<b>" + post.getUser().getUsername() + "</b> " + post.getDescription();
            tvDescription.setText(Html.fromHtml(descriptionString));
            //Log.i("Posts Adapter", post.getDescription() + post.getCreatedAt());
            tvDate.setText(post.getCreatedAt().toString());
            ParseFile image = post.getImage();
            if(image != null){
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            ParseFile imageProfile = post.getUser().getParseFile("profileImage");
            if(imageProfile != null){
                Glide.with(context).load(imageProfile.getUrl()).transform(new CircleCrop()).into(ivProfilePic);
            }
        }

        @Override
        public void onClick(View view) {
            // gets item position
            Toast.makeText(context, "Clicked post", Toast.LENGTH_SHORT).show();
            Log.i("Post Adapter", "clicked post");
            int position = getAdapterPosition();
            //Log.i("myApp", "on click");
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Post post = posts.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, PostDetailActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                ///intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
