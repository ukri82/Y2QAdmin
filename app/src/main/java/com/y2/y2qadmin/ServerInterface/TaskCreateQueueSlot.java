package com.y2.y2qadmin.ServerInterface;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.y2.y2qadmin.misc.VolleySingleton;
import com.y2.y2qadmin.model.QueueSlot;
import com.y2.y2qadmin.model.QueueSlotResultParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by u on 10.06.2015.
 */
public class TaskCreateQueueSlot extends AsyncTask<Void, Void, QueueSlot>
{
    public interface QueueSlotsCreateListener
    {
        public void onQueueSlotCreated(QueueSlot queueSlot);
    }


    private QueueSlotsCreateListener myListener;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String mQueueId;

    public TaskCreateQueueSlot(QueueSlotsCreateListener aListener_in, String qId)
    {
        mQueueId = qId;
        this.myListener = aListener_in;
        volleySingleton = VolleySingleton.getInstance(null);
        requestQueue = volleySingleton.getRequestQueue();


    }


    @Override
    protected QueueSlot doInBackground(Void... params)
    {
        Endpoints.waitForIP();
        JSONObject response = Requestor.request(requestQueue, Endpoints.getRequestUrlCreateQueueSlot(mQueueId));

        QueueSlot slot = null;
        try
        {
            slot = QueueSlotResultParser.parseOne(response.getJSONArray("QueueSlotData").getJSONObject(0));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return slot;
    }

    @Override
    protected void onPostExecute(QueueSlot queueSlot)
    {
        if (myListener != null)
        {
            myListener.onQueueSlotCreated(queueSlot);
        }
    }
}