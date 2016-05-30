package com.example.bkakran.nytimessearch.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.bkakran.nytimessearch.Adapter.ArticleArrayAdapter;
import com.example.bkakran.nytimessearch.Fragment.SettingsDialogFragment;
import com.example.bkakran.nytimessearch.R;
import com.example.bkakran.nytimessearch.model.Article;
import com.example.bkakran.nytimessearch.model.Request;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    String searchText="";

    public static final int GETREQUESTACTIVITY = 50;

    private Request request = new Request();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpViews();
    }

    private void setUpViews() {
        gvResults = (GridView) findViewById(R.id.gvResults);
        articles = new ArrayList();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        /*
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent articleIntent = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = adapter.getItem(position);
                articleIntent.putExtra("article",article);
                startActivity(articleIntent);
            }
        });*/

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                getData(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                if(!TextUtils.isEmpty(query)){
                    searchText = query;
                } else {
                    searchText = "";
                }
                adapter.clear();
                getData(0);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            launchSettingsActivity();
        } else
        if (id == R.id.miRequest) {
            //launchSettingsFragmentActivity();
            launchSettingsActivity();
        }
        //navigateToURL(null);

        return super.onOptionsItemSelected(item);
    }

    private void launchSettingsActivity() {
        Intent i = new Intent(SearchActivity.this, RequestActivity.class);
        i.putExtra("request", request);
        startActivityForResult(i, GETREQUESTACTIVITY);
    }

    private void launchSettingsFragmentActivity() {
        FragmentManager fm = getSupportFragmentManager();
        SettingsDialogFragment settingsDialogFragment = SettingsDialogFragment.newInstance("Some Title");
        settingsDialogFragment.show(fm, "fragment_edit_name");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (GETREQUESTACTIVITY == requestCode && resultCode == RESULT_OK) {
            request = (Request) data.getParcelableExtra("request");
            Toast.makeText(this, "Filers Applied", Toast.LENGTH_SHORT).show();
            adapter.clear();
            getData(0);
        }
    }

    public void onArticleSearch(View view) {

        //Toast.makeText(this, "Searching for" + query, Toast.LENGTH_LONG).show();
        getData(0);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    private void getData(int pageNo) {
        if(!isNetworkAvailable() || !isOnline()) {
            Toast.makeText(getApplicationContext(), "Internet Service is not available" , Toast.LENGTH_LONG).show();
            return;
        }
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = constructParams(pageNo);
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        JSONArray articleSearchResults = null;
                        try {
                            articleSearchResults = response.getJSONObject("response").getJSONArray("docs");
                            ArrayList<Article> articleResults = Article.fromJSonArray(articleSearchResults);
                            if(!articleResults.isEmpty()) {
                                adapter.addAll(articleResults);
                            } else {
                                Toast.makeText(getApplicationContext(), "No results to show", Toast.LENGTH_SHORT).show();
                            }
                            //adapter.notifyDataSetChanged();
                            Log.d("DEBUG", articles.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private RequestParams constructParams(int pageNo) {
        RequestParams params = new RequestParams();
        params.put("api-key", "65eabf8ee85845fd8fe031cc6eafed82");
        params.put("page", pageNo);
        params.put("q", searchText);
        if (!TextUtils.isEmpty(request.getBeginDate())) {
            try {
                params.put("begin_date", getFormattedDate(request.getBeginDate()));
            } catch (ParseException e) {
                Log.e("ERROR", e.toString());
            }
        }
        if (!TextUtils.isEmpty(request.getSortOrder())) {
            params.put("sort", request.getSortOrder().toLowerCase());
        }
        //news_desk:science OR news_desk:arts OR news_desk:fashion
        List<String> filters = new ArrayList<>();
        if (request.getNewsDesk() != null && request.getNewsDesk().size() == 3) {
            if (request.getNewsDesk().get(0)) filters.add("news_desk:arts");
            if (request.getNewsDesk().get(1)) filters.add("news_desk:fashion");
            if (request.getNewsDesk().get(2)) filters.add("news_desk:sports");

        }
        if (filters.size() < 1) return params;
        StringBuilder sb = new StringBuilder();
        for (String filter : filters) {
            sb.append(filter);
            sb.append(" OR ");
        }
        sb.append(filters.get(0));
        params.put("fq",sb.toString());
        return params;

    }

    private String getFormattedDate(String beginDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(beginDate);
        SimpleDateFormat targetsdf = new SimpleDateFormat("yyyyMMdd");
        return targetsdf.format(date);
    }
}
