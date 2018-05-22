package com.example.baptiste.smartcity.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.baptiste.smartcity.R;
import com.example.baptiste.smartcity.activities.NewsDetailsActivity;
import com.example.baptiste.smartcity.adapters.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;



public class NewsFragment extends Fragment {

    String API_KEY;
    ListView listNews;
    ProgressBar loader;
    View v;

    ArrayList<HashMap<String, String>> dataList = new ArrayList<>();

    public static final String KEY_AUTHOR = "author";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_URL = "url";
    public static final String KEY_URLTOIMAGE = "urlToImage";
    public static final String KEY_PUBLISHEDAT = "publishedAt";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.news_fragment_main, container, false);
        API_KEY = v.getContext().getResources().getString(R.string.news_key);

        listNews = (ListView) v.findViewById(R.id.listNews);
        loader = (ProgressBar) v.findViewById(R.id.loader);
        listNews.setEmptyView(loader);



        if(isNetworkAvailable(v.getContext())){
            DownloadNews newsTask = new DownloadNews();
            newsTask.execute();
        }
        else{
            Toast.makeText(v.getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
        return v;

    }


    class DownloadNews extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected String doInBackground(String... args) {
            String urlParameters = "";
            return excuteGet("https://newsapi.org/v2/top-headlines?category=general&country=fr&pageSize=99&apiKey="+API_KEY, urlParameters);
        }
        @Override
        protected void onPostExecute(String xml) {
            Log.e("test",xml);

            if(xml.length()>10){ //xml is not empty even if there is no article, 10 is enougth
                String status = xml.substring(xml.indexOf(":")+1,xml.indexOf(","));
                if(!status.equals("\"error\"")) {
                    try {
                        JSONObject jsonResponse = new JSONObject(xml);
                        JSONArray jsonArray = jsonResponse.optJSONArray("articles");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(KEY_AUTHOR, jsonObject.optString(KEY_AUTHOR).toString());
                            map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE).toString());
                            map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION).toString());
                            map.put(KEY_URL, jsonObject.optString(KEY_URL).toString());
                            map.put(KEY_URLTOIMAGE, jsonObject.optString(KEY_URLTOIMAGE).toString());
                            map.put(KEY_PUBLISHEDAT, jsonObject.optString(KEY_PUBLISHEDAT).toString());
                            dataList.add(map);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getView().getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                    }

                    ListNewsAdapter adapter = new ListNewsAdapter(NewsFragment.this, dataList);
                    listNews.setAdapter(adapter);

                    listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Intent i = new Intent(NewsFragment.this.getContext(), NewsDetailsActivity.class);
                            i.putExtra("url", dataList.get(+position).get(KEY_URL));
                            startActivity(i);
                        }
                    });
                }
                else{
                    Toast.makeText(getView().getContext(), v.getContext().getResources().getString(R.string.error_news), Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getView().getContext(), v.getContext().getResources().getString(R.string.no_news), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean isNetworkAvailable(Context context)
    {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }



    public static String excuteGet(String targetURL, String urlParameters)
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            //connection.setRequestMethod("POST");
            //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("content-type", "application/json;  charset=utf-8");


            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(false);



            InputStream is;

            int status = connection.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK)
                is = connection.getErrorStream();
            else
                is = connection.getInputStream();



            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {


            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }

}
