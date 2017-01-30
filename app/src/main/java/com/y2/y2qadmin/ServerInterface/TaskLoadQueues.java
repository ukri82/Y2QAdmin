package com.y2.y2qadmin.ServerInterface;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.y2.y2qadmin.misc.VolleySingleton;
import com.y2.y2qadmin.model.Queue;
import com.y2.y2qadmin.model.QueueResultParser;
import com.y2.y2qadmin.model.QueueSlot;
import com.y2.y2qadmin.model.QueueSlotResultParser;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by u on 10.06.2015.
 */
public class TaskLoadQueues extends AsyncTask<Void, Void, ArrayList<Queue>>
{
    public interface QueuesLoadedListener
    {
        public void onQueuesLoaded(ArrayList<Queue> listQueues);
    }


    private QueuesLoadedListener myListener;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private int myStart = 0;
    private int myCount = 10;
    private String mOrgId;

    public TaskLoadQueues(QueuesLoadedListener aListener_in, int aStart_in, int limit, String orgId)
    {
        mOrgId = orgId;
        myStart = aStart_in;
        myCount = limit;
        this.myListener = aListener_in;
        volleySingleton = VolleySingleton.getInstance(null);
        requestQueue = volleySingleton.getRequestQueue();
    }


    @Override
    protected ArrayList<Queue> doInBackground(Void... params)
    {
        Endpoints.waitForIP();
        JSONObject response = Requestor.request(requestQueue, Endpoints.getRequestUrlNextQueueChunk(myStart, myCount, mOrgId));
        return QueueResultParser.parse(response);
    }

    @Override
    protected void onPostExecute(ArrayList<Queue> listQueueSlots)
    {
        if (myListener != null)
        {
            myListener.onQueuesLoaded(listQueueSlots);
        }
    }
}