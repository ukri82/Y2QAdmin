package com.y2.y2qadmin.ServerInterface;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.y2.y2qadmin.misc.VolleySingleton;
import com.y2.y2qadmin.model.QueueSlot;
import com.y2.y2qadmin.model.QueueSlotResultParser;
import com.y2.y2qadmin.model.QueueSlot;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by u on 10.06.2015.
 */
public class TaskLoadQueueSlots extends AsyncTask<Void, Void, ArrayList<QueueSlot>>
{
    public interface QueueSlotsLoadedListener
    {
        public void onQueueSlotsLoaded(ArrayList<QueueSlot> listQueueSlots);
    }


    private QueueSlotsLoadedListener myListener;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private int myStart = 0;
    private int myCount = 10;
    private String mQueueId;

    public TaskLoadQueueSlots(QueueSlotsLoadedListener aListener_in, int aStart_in, int limit, String qId)
    {
        mQueueId = qId;
        myStart = aStart_in;
        myCount = limit;
        this.myListener = aListener_in;
        volleySingleton = VolleySingleton.getInstance(null);
        requestQueue = volleySingleton.getRequestQueue();


    }


    @Override
    protected ArrayList<QueueSlot> doInBackground(Void... params)
    {
        Endpoints.waitForIP();
        JSONObject response = Requestor.request(requestQueue, Endpoints.getRequestUrlNextQueueSlotChunk(myStart, myCount, mQueueId));
        return QueueSlotResultParser.parse(response);
    }

    @Override
    protected void onPostExecute(ArrayList<QueueSlot> listQueueSlots)
    {
        if (myListener != null)
        {
            myListener.onQueueSlotsLoaded(listQueueSlots);
        }
    }
}