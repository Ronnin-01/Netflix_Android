package com.application.netflix.Retrofit;

import static com.application.netflix.Retrofit.RetrofitClient.BASE_URL;

import io.reactivex.Observable;
import com.application.netflix.Model.AllCategory;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET(BASE_URL)
    Observable<List<AllCategory>> getAllCategoryMovies();
}
