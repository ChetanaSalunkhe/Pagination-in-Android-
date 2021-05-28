package com.chetana.paginationassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chetana.paginationassignment.Adapter.RecyclerviewAdapter;
import com.chetana.paginationassignment.Class.EndlessRecyclerViewScrollListener_;
import com.chetana.paginationassignment.Class.NetworkUtil;
import com.chetana.paginationassignment.Class.RetrofitClient;
import com.chetana.paginationassignment.Class.User;
import com.chetana.paginationassignment.Interface.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Context parent;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<User.Data> _User;

    Api apiInterface;
    boolean isScrolling = false;

    int pageNumber=0;

    int currentUser,scrolledUser,totalUser;
    LinearLayoutManager mLayoutManager;
    RecyclerviewAdapter recyclerviewAdapter;
    EndlessRecyclerViewScrollListener_ scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        if(NetworkUtil.isNetworkAvailable(parent)){
            getUsers();

            getEndlessScroll();
        }else {
            showDialog();
        }

        setListener();
    }

    public void init(){
        parent = MainActivity.this;

        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progress_circular);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);

        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        _User = new ArrayList<User.Data>();
        apiInterface = RetrofitClient.getClient().create(Api.class);

        swipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);

    }

    public void setListener(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                recyclerviewAdapter.notifyDataSetChanged();
                _User.clear();
                getUsers();
            }
        });
    }

    public void getEndlessScroll(){
        scrollListener = new EndlessRecyclerViewScrollListener_(mLayoutManager){
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                currentUser = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                pageNumber = pageNumber + 1;
                fetchData();
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    public void getUsers(){
        //_User.clear();

        Call<User> call = apiInterface.getUsers("2");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User myUser  = response.body();
                Integer text = myUser.page;
                Integer total = myUser.total;
                Integer totalPages = myUser.total_pages;
                List<User.Data> DataList = myUser.data;

                for (int i=0; i<DataList.size();i++) {
                    User.Data user_data = new User.Data(DataList.get(i).getId(),DataList.get(i).getFirst_name(),DataList.get(i).getLast_name(),
                            DataList.get(i).getEmail(),DataList.get(i).getAvatar());

                    _User.add(user_data);
                }

                recyclerviewAdapter = new RecyclerviewAdapter(parent,_User);
                recyclerView.setAdapter(recyclerviewAdapter);
                recyclerviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    void showProgressView() {
        progressBar.setVisibility(View.VISIBLE);
    }

    void hideProgressView() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void fetchData(){
        showProgressView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getUsers();
                hideProgressView();
                recyclerviewAdapter.notifyDataSetChanged();
            }
        }, 5000);
    }

    public void showDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(parent);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.alert_dialog, null);
        dialogBuilder.setView(myView);
        dialogBuilder.show();
        dialogBuilder.setCancelable(true);
    }
}