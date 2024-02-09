package com.application.netflix.Mainscreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.application.netflix.Adapters.MainRecyclerAdapter;
import com.application.netflix.Model.AllCategory;
import com.application.netflix.R;
import com.application.netflix.Retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainScreen extends AppCompatActivity {
    TextView movietext, tvseriestext;
    BottomNavigationView bottomNavigationView;
    MainRecyclerAdapter mainRecyclerAdapter;
    RecyclerView MainRecycler;
    List<AllCategory> allCategoryList;
    public void onBackPressed(){
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        movietext=findViewById(R.id.movietooltext);
        tvseriestext=findViewById(R.id.tvseriestooltext);
        MainRecycler=findViewById(R.id.mainRecyclerView);
        movietext.setOnClickListener(view -> {
            Intent i =new Intent(MainScreen.this,Movies.class);
            startActivity(i);
        });
        tvseriestext.setOnClickListener(view -> {
            Intent i =new Intent(MainScreen.this,Series.class);
            startActivity(i);
        });
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        Menu menu =bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeicon) {
                } else if (itemId == R.id.serachicon) {
                    Intent i = new Intent(MainScreen.this, Search.class);
                    startActivity(i);
                } else if (itemId == R.id.settingsicon) {
                    Intent j = new Intent(MainScreen.this, Settings.class);
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
    public void setMainRecycler(List<AllCategory> allCategoryList){
        MainRecycler=findViewById(R.id.mainRecyclerView);
        RecyclerView.LayoutManager layoutManager  = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        MainRecycler.setLayoutManager(layoutManager);
        mainRecyclerAdapter = new MainRecyclerAdapter(this,allCategoryList);
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
}