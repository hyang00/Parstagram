package com.example.parstagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
//import androidx.appcompat.widget.Toolbar;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parstagram.EndlessRecyclerViewScrollListener;
import com.example.parstagram.LoginActivity;
import com.example.parstagram.models.Post;
import com.example.parstagram.R;
import com.example.parstagram.adapters.UserPostsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPostFragment extends Fragment {

    public static final String TAG = "UserPost Fragment";

    private RecyclerView rvPosts;
    private UserPostsAdapter adapter;
    private List<Post> allPosts;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    private TextView tvName;
    private TextView tvBio;
    private ImageView ivProfile;
    private Toolbar toolbar;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "user";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ParseUser user;

    public UserPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment UserPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserPostFragment newInstance(ParseUser user) {
        UserPostFragment fragment = new UserPostFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_PARAM1);
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        tvName = view.findViewById(R.id.tvName);
        tvBio = view.findViewById(R.id.tvBio);
        ivProfile = view.findViewById(R.id.ivProfile);
        tvName.setText(user.getUsername());
        ParseFile imageProfile = user.getParseFile("profileImage");
        if(imageProfile != null){
            Glide.with(getContext()).load(imageProfile.getUrl()).transform(new CircleCrop()).into(ivProfile);
        }
        toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        toolbar.inflateMenu(R.menu.menu_user_profile);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.miLogout:
                        ParseUser.logOut();
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        getContext().startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //((AppCompatActivity) getActivity()).setActionBar(toolbar);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 3 );
        // create the data source
        allPosts = new ArrayList<>();
        // create the adapter
        adapter = new UserPostsAdapter(getContext(), allPosts);

//        // Lookup the swipe container view
//        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
//        // Setup refresh listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Your code to refresh the list here.
//                // Make sure you call swipeContainer.setRefreshing(false)
//                // once the network request has completed successfully.
//                //Todo: check if this is correct
//                queryPosts();
//            }
//        });
//        // Configure the refreshing colors
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);

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

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.
//    }

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