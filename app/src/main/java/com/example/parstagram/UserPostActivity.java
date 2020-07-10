package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.parstagram.adapters.UserPostsAdapter;
import com.example.parstagram.fragments.UserPostFragment;
import com.example.parstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class UserPostActivity extends AppCompatActivity {

    public static final String TAG = "UserPost Fragment";

    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);
        Fragment fragment = UserPostFragment.newInstance(ParseUser.getCurrentUser(), false);
        fragmentManager.beginTransaction().replace(R.id.rlUserPost, fragment).commit();
    }
}