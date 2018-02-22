package cmw.co.id.pmc;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn_logout;
    private TextView mTextMessage, actionBarText;
    SharedPreferences sharedpreferences;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public static final String PUSH_NOTIFICATION = "pushNotification";
    private String deviceToken, id;
    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    actionBarText.setText("PMC - ACTIVITY");
                    transaction.replace(R.id.content, new HomeFragment()).commit();
                    return true;
//                    Intent intent1 = new Intent(MainActivity.this, HomeActivity.class);
//                    startActivity(intent1);
//                    break;

                case R.id.navigation_tasks:
                    actionBarText.setText("PMC - TASK");
                    transaction.replace(R.id.content, new TaskFragment()).commit();
                    return true;
                case R.id.navigation_projects:
                    actionBarText.setText("PMC - PROJECT");
                    transaction.replace(R.id.content, new ProjectFragment()).commit();
                    return true;
                case R.id.navigation_profile:
                    actionBarText.setText("PMC - PROFILE");
                    transaction.replace(R.id.content, new PersonFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setTitle("PMC - News");


//        getActionBar().setIcon(R.drawable.my_icon);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        actionBarText = findViewById(R.id.action_bar_text);
        actionBarText.setText("PMC - Project");

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        deviceToken = SharedPrefManager.getInstance(MainActivity.this).getDeviceToken();

        if(deviceToken != null){
            displayDeviceRegId();
        }else{
            Toast.makeText(getApplicationContext(), "Push notification: " + deviceToken, Toast.LENGTH_LONG).show();
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equalsIgnoreCase(PUSH_NOTIFICATION)){
                    displayDeviceRegId();
                }

            }
        };

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, new ProjectFragment()).commit();
    }

    private void displayDeviceRegId() {
        deviceToken = SharedPrefManager.getInstance(MainActivity.this).getDeviceToken();
        SharedPreferences sharedPreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedPreferences.getString(TAG_ID, id);
        Log.e("FCM Device token",deviceToken);
        Toast.makeText(getApplicationContext(), "Push notification: " + deviceToken, Toast.LENGTH_LONG).show();
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String ServerURL = Server.URL+"login/register_token/"+id;
                String NameHolder = deviceToken ;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                Toast.makeText(MainActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(deviceToken);
//        txtDeviceToken.setText(deviceToken);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


}
