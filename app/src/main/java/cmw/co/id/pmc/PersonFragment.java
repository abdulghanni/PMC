package cmw.co.id.pmc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cmw.co.id.pmc.Server;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final String TAG_ID = "id";
    public static final String TAG_USERNAME = "username";
    String id, username;
    TextView tvProjectsTotal;
    private OnFragmentInteractionListener mListener;
    ProgressDialog pd;

    public PersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    Button person_logout;
    TextView txt_id, txt_username, tvTasksTotal;
    SharedPreferences sharedpreferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_person, container, false);
        //Inflate the layout for this fragment
        txt_username = view.findViewById(R.id.user_profile_name);
        tvProjectsTotal = view.findViewById(R.id.tvProjectsTotal);
        tvTasksTotal = view.findViewById(R.id.tvTasksTotal);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        id = sharedPreferences.getString(TAG_ID, id);
        username = sharedPreferences.getString(TAG_USERNAME, username);

        txt_username.setText(username+' '+id);
        Log.e("Profile!", id);


//        Log.e("Profile2!", username);
        Button person_logout = (Button)view.findViewById(R.id.person_logout);
        person_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.putBoolean(Login.session_status, false);
            editor.commit();
            Intent logout = new Intent(getActivity(), Login.class);
            startActivity(logout);
            Log.d(TAG_ID, sharedPreferences.getString("TAG_ID", ""));
            Log.d(TAG_USERNAME, sharedPreferences.getString("TAG_USERNAME", ""));
            }
        });
        new getTotalProjects().execute(Server.URL+"project/total");
        new getTotalTasks().execute(Server.URL+"task/total");
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Log.e("Profile!", "Profile");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private class getTotalProjects extends AsyncTask<String, String, String> {

//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            pd = new ProgressDialog(getActivity());
//            pd.setMessage("Please wait");
//            pd.setCancelable(false);
//            pd.show();
//        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            if (pd.isShowing()){
//                pd.dismiss();
//            }
//            Log.e("result", result);
            tvProjectsTotal.setText(result);
        }
    }

    private class getTotalTasks extends AsyncTask<String, String, String> {

//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            pd = new ProgressDialog(getActivity());
//            pd.setMessage("Please wait");
//            pd.setCancelable(false);
//            pd.show();
//        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            if (pd.isShowing()){
//                pd.dismiss();
//            }
//            Log.e("result", result);
            tvTasksTotal.setText(result);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
