package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class UserPostActivity extends AppCompatActivity {

    public static final String TAG = "UserPost Fragment";

    private RecyclerView rvPosts;
    private UserPostsAdapter adapter;
    private List<Post> allPosts;
    private Post post;
    private ParseUser user;
    //protected SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        user = post.getParseUser(Post.KEY_USER);
        setContentView(R.layout.activity_user_post);

        rvPosts = findViewById(R.id.rvPosts);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        GridLayoutManager linearLayoutManager = new GridLayoutManager(UserPostActivity.this, 3 );
        // create the data source
        allPosts = new ArrayList<>();
        // create the adapter
        adapter = new UserPostsAdapter(UserPostActivity.this, allPosts);

        // set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPosts.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page, totalItemsCount);
            }
        };
        rvPosts.addOnScrollListener(scrollListener);
        queryPosts();
    }

    protected void loadNextDataFromApi(int page, final int totalItemsCount) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // also want the user info associated w/ the post
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        Log.i(TAG, "totalItemsCount: " + totalItemsCount);
        query.setSkip(totalItemsCount);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue w/ getting posts", e);
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription());
                }
                adapter.addAll(totalItemsCount, posts);
                adapter.notifyDataSetChanged();
            }
        });

    }

    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // also want the user info associated w/ the post
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue w/ getting posts", e);
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription());
                }
                adapter.clear();
                adapter.addAll(posts);
                adapter.notifyDataSetChanged();
                //swipeContainer.setRefreshing(false);
            }
        });

    }
}