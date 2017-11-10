package com.rperazzo.weatherapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rperazzo.weatherapp.R;
import com.rperazzo.weatherapp.managers.WeatherManager;
import com.rperazzo.weatherapp.managers.WeatherManager.WeatherService;
import com.rperazzo.weatherapp.managers.RealmManager;
import com.rperazzo.weatherapp.model.City;
import com.rperazzo.weatherapp.model.CityRealm;
import com.rperazzo.weatherapp.model.FindResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private ListView mList;
    private FindItemAdapter mAdapter;
    private ArrayList<City> cities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.editText);
        mTextView = (TextView) findViewById(R.id.textView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mList = (ListView) findViewById(R.id.list);

        mAdapter = new FindItemAdapter(this, cities);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                City item = (City) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("city", item);
                startActivity(intent);
            }
        });

        mEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchByName();
                }
                return false;
            }
        });

        searchByFavorites();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSearchClick(View view) {
        searchByName();
    }

    private void onStartLoading() {
        mList.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.GONE);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                     getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void onFinishLoading(FindResult result){

        mProgressBar.setVisibility(View.GONE);
        cities.clear();

        if (result.list.size() > 0) {
            cities.addAll(result.list);
            mList.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        } else {
            mTextView.setText("No results.");
        }
    }

    private void onFinishLoadingWithError() {
        mProgressBar.setVisibility(View.GONE);
        mList.setVisibility(View.GONE);
        mTextView.setText("Error");
    }

    public boolean isDeviceConnected() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void searchByFavorites() {
        if (!isDeviceConnected()) {
            Toast.makeText(this, "No connection!", Toast.LENGTH_LONG);
            return;
        }

        RealmResults<CityRealm> favorites = RealmManager.findAllCities();
        List<String> favoriteIDs = new LinkedList<>();
        for(CityRealm favorite : favorites) {
            favoriteIDs.add(String.valueOf(favorite.getId()));
        }

        if (favoriteIDs.size() == 0) {
            mTextView.setText("No results yet. Trying searching for a city.");
            return;
        }

        onStartLoading();

        WeatherService wService = WeatherManager.getService();
        final Call<FindResult> findByIdCall =
                wService.findById(TextUtils.join(",", favoriteIDs), WeatherManager.API_KEY);
        findByIdCall.enqueue(new Callback<FindResult>() {
            @Override
            public void onResponse(Call<FindResult> call, Response<FindResult> response) {
                onFinishLoading(response.body());
            }

            @Override
            public void onFailure(Call<FindResult> call, Throwable t) {
                onFinishLoadingWithError();
            }
        });
    }

    private void searchByName() {
        if (!isDeviceConnected()) {
            Toast.makeText(this, "No connection!", Toast.LENGTH_LONG);
            return;
        }

        String search = mEditText.getText().toString();
        if (TextUtils.isEmpty(search)) {
            return;
        }

        onStartLoading();

        WeatherService wService = WeatherManager.getService();
        final Call<FindResult> findCall = wService.find(search, WeatherManager.API_KEY);
        findCall.enqueue(new Callback<FindResult>() {
            @Override
            public void onResponse(Call<FindResult> call, Response<FindResult> response) {
                onFinishLoading(response.body());
            }

            @Override
            public void onFailure(Call<FindResult> call, Throwable t) {
                onFinishLoadingWithError();
            }
        });
    }
}
