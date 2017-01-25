package com.y2.y2qadmin.ServerInterface;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.y2.y2qadmin.misc.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by u on 10.06.2015.
 */
public class TaskGetServerIPs extends AsyncTask<Void, Void, ArrayList<String>>
{
    public interface TaskGetServerIPsQueryListener
    {
        public void onServerIPsAvailable(ArrayList<String> aPlayList_in);
    }


    private TaskGetServerIPsQueryListener myListener;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    String myUserId;
    String myStaticServerIp;

    public TaskGetServerIPs(TaskGetServerIPsQueryListener aListener_in, String aUserId_in, String aStaticIp_in)
    {
        myUserId = aUserId_in;
        myStaticServerIp = aStaticIp_in;
        this.myListener = aListener_in;
        volleySingleton = VolleySingleton.getInstance(null);
        requestQueue = volleySingleton.getRequestQueue();
    }


    @Override
    protected ArrayList<String> doInBackground(Void... params)
    {
        JSONObject response = Requestor.request(requestQueue, Endpoints.getServerIPs(myUserId, myStaticServerIp));
        return parse(response);
    }

    @Override
    protected void onPostExecute(ArrayList<String> aList_in)
    {
        if (myListener != null)
        {
            myListener.onServerIPsAvailable(aList_in);
        }
    }

    public ArrayList<String> parse(JSONObject response)
    {
        ArrayList<String> listServers = new ArrayList<>();
        if (response != null && response.length() > 0)
        {
            try
            {
                JSONArray arrayItems = response.getJSONArray("ServerData");
                for (int i = 0; i < arrayItems.length(); i++)
                {
                    listServers.add(arrayItems.getString(i));
                }

            }
            catch (JSONException e)
            {

            }
        }
        return listServers;
    }

}