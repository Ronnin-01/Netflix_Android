package com.application.netflix.Mainscreens;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import com.application.netflix.Adapters.SearchRecyclerAdapter;
import com.application.netflix.Model.AllCategory;
import com.application.netflix.R;
import com.application.netflix.Retrofit.RetrofitClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Search extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    SearchRecyclerAdapter mainRecyclerAdapter;
    RecyclerView MainRecycler;
    List<AllCategory> allCategoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);
            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        Objects.requireNonNull(getSupportActionBar()).show();
        Menu menu =bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeicon) {
                    Intent k = new Intent(Search.this, MainScreen.class);
                    startActivity(k);
                } else if (itemId == R.id.serachicon) {

                } else if (itemId == R.id.settingsicon) {
                    Intent j = new Intent(Search.this, Settings.class);
                    startActivity(j);
                }
                return false;
            }
        });
        ConnectivityManager connectivityManager =(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo==null || networkInfo.isConnected() || networkInfo.isAvailable()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Please turn on your internet connection to contine.");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    recreate();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
        }else{
            allCategoryList = new ArrayList<>();
            getAllMovieData();
        }
    }
    public void setMainRecycler(List<AllCategory>allCategoryList){
        MainRecycler=findViewById(R.id.searchRecyclerView);
        RecyclerView.LayoutManager layoutManager  = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        MainRecycler.setLayoutManager(layoutManager);
        mainRecyclerAdapter = new SearchRecyclerAdapter(this,allCategoryList);
        MainRecycler.setAdapter(mainRecyclerAdapter);
    }
    private void getAllMovieData() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(RetrofitClient.getRetrofitClient().getAllCategoryMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<AllCategory>>(){

                    @Override
                    public void onNext(List<AllCategory> allCategoryList) {
                        setMainRecycler(allCategoryList);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu,menu);
        MenuItem searchItem= menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                mainRecyclerAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
}