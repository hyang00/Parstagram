package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.parstagram.models.Comment;
import com.example.parstagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

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
    private Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvDate = findViewById(R.id.tvDate);
        ivUserProfilePic = findViewById(R.id.ivUserProfilePic);
        ivSend = findViewById(R.id.ivSend);
        etComment = findViewById(R.id.etComment);
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

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
    }

    private void postComment(String description) {
        Comment comment= new Comment();
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
            }
        });
    }
}