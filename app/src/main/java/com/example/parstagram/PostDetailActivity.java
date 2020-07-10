package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parstagram.adapters.CommentsAdapter;
import com.example.parstagram.adapters.PostsAdapter;
import com.example.parstagram.models.Comment;
import com.example.parstagram.models.Like;
import com.example.parstagram.models.Post;
import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    public static final String TAG = "PostDetailActivity";

    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvDescription;
    private ImageView ivProfilePic;
    private TextView tvDate;
    private ImageView ivUserProfilePic;
    private ImageView ivSend;
    private EditText etComment;
    private ImageView ivLike;
    private TextView tvLikeCount;
    private RecyclerView rvComments;
    protected CommentsAdapter adapter;
    protected List<Comment> allComments;
    private Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvDate = findViewById(R.id.tvDate);
        ivUserProfilePic = findViewById(R.id.ivUserProfilePic);
        ivSend = findViewById(R.id.ivSend);
        etComment = findViewById(R.id.etComment);
        rvComments = findViewById(R.id.rvComments);
        ivLike = findViewById(R.id.ivLike);
        tvLikeCount = findViewById(R.id.tvLikeCount);
        getIsLiked();
        getLikeCount();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        allComments = new ArrayList<>();
        adapter = new CommentsAdapter(this, allComments);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(linearLayoutManager);
        //tvLikeCount.setText(Integer.toString(getLikeCount()));


        tvUsername.setText(post.getUser().getUsername());
        String descriptionString = "<b>" + post.getUser().getUsername() + "</b> " + post.getDescription();
        tvDescription.setText(Html.fromHtml(descriptionString));
        //tvDescription.setText(post.getDescription());
        tvDate.setText(post.getCreatedAt().toString());
        ParseFile image = post.getImage();
        if(image != null){
            Glide.with(PostDetailActivity.this).load(image.getUrl()).into(ivImage);
        }
        ParseFile imageProfile = post.getUser().getParseFile("profileImage");
        if(imageProfile != null){
            Glide.with(PostDetailActivity.this).load(imageProfile.getUrl()).transform(new CircleCrop()).into(ivProfilePic);
        }
        ParseFile imageUserProfile = ParseUser.getCurrentUser().getParseFile("profileImage");
        if(imageUserProfile != null){
            Glide.with(PostDetailActivity.this).load(imageUserProfile.getUrl()).transform(new CircleCrop()).into(ivUserProfilePic);
        }
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "send clicked");
                String description = etComment.getText().toString();
                postComment(description);
            }
        });
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ivLike.isSelected()){
                    removeLike();
                }else{
                    addLike();
                }
            }
        });


        queryComments();
    }

    private void removeLike() {
        // Specify which class to query
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.whereEqualTo(Like.KEY_POST, post);
        query.whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());
        Log.i(TAG, "currUser: " + ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> objects, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while unliking", e);
                    Toast.makeText(PostDetailActivity.this, "Error while unliking", Toast.LENGTH_SHORT).show();
                }
                objects.get(0).deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null){
                            Log.e(TAG, "Error while unliking", e);
                            Toast.makeText(PostDetailActivity.this, "Error while unliking", Toast.LENGTH_SHORT).show();
                        }
                        ivLike.setSelected(false);
                        getLikeCount();
                    }
                });

            }
        });
    }
    private void addLike(){
        Like like = new Like();
        like.setPost(post);
        like.setUser(ParseUser.getCurrentUser());
        like.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while liking", e);
                    Toast.makeText(PostDetailActivity.this, "Error while liking", Toast.LENGTH_SHORT).show();
                }
                ivLike.setSelected(true);
                getLikeCount();
            }
        });

    }

    private void getIsLiked(){
        // Specify which class to query
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.whereEqualTo(Like.KEY_POST, post);
        query.whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());
        Log.i(TAG, "currUser: " + ParseUser.getCurrentUser().getUsername());
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue w/ getting is Liked", e);
                }
                Log.i(TAG, "isLiked: " + count);
                if (count == 1){
                    ivLike.setSelected(true);
                } else{
                    ivLike.setSelected(false);
                }
            }
        });

    }

    private void getLikeCount(){
        // Specify which class to query
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.whereEqualTo(Like.KEY_POST, post);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue w/ getting likeCount", e);
                }
                Log.i(TAG, "Count: " + count);
                tvLikeCount.setText(Integer.toString(count));
            }
        });
    }
    private void queryComments() {
        // Specify which class to query
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        // also want the user info associated w/ the post
        query.include(Comment.KEY_USER);
        query.include(Comment.KEY_POST);
        query.include(Comment.KEY_DESCRIPTION);
        query.whereEqualTo(Comment.KEY_POST, post);
        query.setLimit(20);
        query.addDescendingOrder(Comment.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue w/ getting comments", e);
                }
                for (Comment comment : comments) {
                    Log.i(TAG, "Comment: " + comment.getDescription());
                }
                adapter.clear();
                adapter.addAll(comments);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void postComment(String description) {
        final Comment comment= new Comment();
        comment.setDescription(description);
        comment.setUser(ParseUser.getCurrentUser());
        comment.setPost(post);
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while posting", e);
                    Toast.makeText(PostDetailActivity.this, "Error while posting comment", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Posting comment");
                // clear out editText and imageView after posting
                etComment.setText("");
                // closes keyboard when send clicked
                etComment.setEnabled(false);
                etComment.setEnabled(true);
                allComments.add(0, comment);
                adapter.notifyItemChanged(0);
            }
        });

    }
}