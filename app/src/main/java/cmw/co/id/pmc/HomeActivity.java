package cmw.co.id.pmc;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;
import static java.security.AccessController.getContext;

public class HomeActivity  extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "RecyclerViewExample";
    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter adapter;
    private ProgressBar progressBar;

    private SearchView mSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        String url = "http://krishscs.esy.es/RecycleViewExample/RecycleViewExample.php";
        mSearchView = (SearchView) findViewById(R.id.search_view);
        setupSearchView();
        new DownloadTask().execute(url);
    }

    public class DownloadTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                adapter = new MyRecyclerViewAdapter(HomeActivity.this, feedsList);
                mRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(HomeActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            feedsList = new ArrayList<FeedItem>();
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("result");
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                FeedItem item = new FeedItem();
                item.setTitle(post.optString("name"));
                item.setThumbnail(post.optString("image"));
                feedsList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void setupSearchView() {
        // mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
//        adapter.setFilter(feedsList);
        // mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search here....");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchview_in_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        int id = item.getItemId();
        if (id == R.id.search) {
            // Handle the camera action
            final SearchView searchView = (SearchView)  MenuItemCompat.getActionView(item);
            searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        }
        return true;
    }

    public boolean onQueryTextChange(String newText) {
        final List<FeedItem> filteredModelList = filter(feedsList, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    private List<FeedItem> filter(List<FeedItem> models, String query) {
        query = query.toLowerCase();final List<FeedItem> filteredModelList = new ArrayList<>();
        for (FeedItem model : models) {
            final String name = model.getTitle().toLowerCase();
            if (name.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
