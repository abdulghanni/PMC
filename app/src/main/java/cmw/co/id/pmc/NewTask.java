package cmw.co.id.pmc;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmw.co.id.pmc.adapter.ProjectSpinnerAdapter;
import cmw.co.id.pmc.app.AppController;
import cmw.co.id.pmc.data.ProjectSpinnerData;

public class NewTask extends AppCompatActivity{
    Spinner projectSpinner;
    ProgressDialog pDialog;
    ProjectSpinnerAdapter adapter;
    List<ProjectSpinnerData> listProject = new ArrayList<ProjectSpinnerData>();
    Intent intent;

    int success;
    ConnectivityManager conMgr;

    private TextView mDisplayDate, mDisplayDate2;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;

    Button mAssigned, btnSave;
    TextView tvProjectSelected, tvAssigned, tvPlanStartDate, tvPlanEndDate, actionBarText;
    EditText txt_name, txt_description;
    String[] listAssigned;
    boolean[] checkedAssigned;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    public static final String url = Server.URL + "project/getProjectList";
    public static final String urlAddProject = Server.URL + "task/add";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_NO_EMP = "no_emp";
    private static final String TAG_NAME = "name";
    public static final String TAG = NewTask.class.getSimpleName();
    public static final String TAG_USERNAME = "username";
    String username;

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        actionBarText = findViewById(R.id.action_bar_text);
        actionBarText.setText("PMC - Add New Task");

        mDisplayDate = (TextView) findViewById(R.id.tvDisplayStartDate);
        mDisplayDate2 = (TextView) findViewById(R.id.tvDisplayEndDate);
        projectSpinner = (Spinner) findViewById(R.id.projectSpinner);
        tvProjectSelected = (TextView) findViewById(R.id.tvProjectSelected);

        projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                tvProjectSelected.setText(listProject.get(position).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        adapter = new ProjectSpinnerAdapter(NewTask.this, listProject);
        projectSpinner.setAdapter(adapter);
        callData();

        //datepicker
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        NewTask.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        mDisplayDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        NewTask.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener2,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = day + "/" + month + "/" + year;
                mDisplayDate2.setText(date);
            }
        };

        //select Assigned

        mAssigned = (Button) findViewById(R.id.btnAssigned);
        tvAssigned = (TextView) findViewById(R.id.tvAssigned);

        listAssigned = getResources().getStringArray(R.array.shopping_item);
        checkedAssigned = new boolean[listAssigned.length];

        mAssigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(NewTask.this);
                mBuilder.setTitle("User List");
                mBuilder.setMultiChoiceItems(listAssigned, checkedAssigned, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//                        if (isChecked) {
//                            if (!mUserItems.contains(position)) {
//                                mUserItems.add(position);
//                            }
//                        } else if (mUserItems.contains(position)) {
//                            mUserItems.remove(position);
//                        }
                        if(isChecked){
                            mUserItems.add(position);
                        }else{
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listAssigned[mUserItems.get(i)];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        tvAssigned.setText(item);
                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedAssigned.length; i++) {
                            checkedAssigned[i] = false;
                            mUserItems.clear();
                            tvAssigned.setText("");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        //add task
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        btnSave = (Button) findViewById(R.id.btnSaveTask);
        txt_name = (EditText) findViewById(R.id.etTaskName);
        txt_description = (EditText) findViewById(R.id.etTaskDescription);
        tvPlanStartDate = (TextView) findViewById(R.id.tvDisplayStartDate);
        tvPlanEndDate = (TextView) findViewById(R.id.tvDisplayEndDate);
        tvAssigned = (TextView) findViewById(R.id.tvAssigned);

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String name = txt_name.getText().toString();
                String description = txt_description.getText().toString();
                String plan_start_date = tvPlanStartDate.getText().toString();
                String plan_end_date = tvPlanEndDate.getText().toString();
                String assigned = tvAssigned.getText().toString();
                String project_id = tvProjectSelected.getText().toString();
                SharedPreferences sharedPreferences = NewTask.this.getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
                username = sharedPreferences.getString(TAG_USERNAME, username);

                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    saveProject(name, description, plan_start_date, plan_end_date, assigned, project_id, username);
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveProject(final String name, final String description, final String tvPlanStartDate, final String plan_end_date, final String assigned, final String project_id, final String username) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Saving ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, urlAddProject, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Save Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Successfully Register!", jObj.toString());

                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        intent = new Intent(NewTask.this, MainActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("description", description);
                params.put("plan_start_date", tvPlanStartDate);
                params.put("plan_end_date", plan_end_date);
                params.put("project_id", project_id);
                params.put("assigned", assigned);
                params.put("created_by", username);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void callData() {
        listProject.clear();

        pDialog = new ProgressDialog(NewTask.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        showDialog();

        // Creating volley request obj
        JsonArrayRequest jArr = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);

                                ProjectSpinnerData item = new ProjectSpinnerData();

                                item.setName(obj.getString(TAG_NAME));
                                Log.e(TAG, TAG_NAME);
                                item.setName(obj.getString(TAG_NAME));

                                listProject.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

//                         notifying list adapter about data changes
//                         so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();

                        hideDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(NewTask.this, error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
