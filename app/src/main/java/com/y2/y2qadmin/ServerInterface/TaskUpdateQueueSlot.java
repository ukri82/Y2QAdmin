package com.y2.y2qadmin.ServerInterface;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.y2.y2qadmin.misc.VolleySingleton;
import com.y2.y2qadmin.model.QueueSlot;
import com.y2.y2qadmin.model.QueueSlotResultParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by u on 10.06.2015.
 */
public class TaskUpdateQueueSlot extends AsyncTask<Void, Void, QueueSlot>
{
    public interface QueueSlotsUpdateListener
    {
        public void onQueueSlotUpdated(QueueSlot queueSlot);
    }

    public enum UpdateStatus
    {
        Start,
        Pause,
        Resume,
        Stop,
        Set
    }


    private QueueSlotsUpdateListener myListener;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String mQueueSlotId;
    int mTokenNumber;
    UpdateStatus mUpdateStatus;

    public TaskUpdateQueueSlot(QueueSlotsUpdateListener aListener_in, String qSlotId, UpdateStatus updateStatus, int tokenNumber)
    {
        mQueueSlotId = qSlotId;
        mTokenNumber = tokenNumber;
        mUpdateStatus = updateStatus;
        this.myListener = aListener_in;
        volleySingleton = VolleySingleton.getInstance(null);
        requestQueue = volleySingleton.getRequestQueue();


    }


    @Override
    protected QueueSlot doInBackground(Void... params)
    {
        Endpoints.waitForIP();

        String status = "START";
        if(mUpdateStatus == UpdateStatus.Pause)
            status = "PAUSE";
        if(mUpdateStatus == UpdateStatus.Resume)
            status = "RESUME";
        if(mUpdateStatus == UpdateStatus.Stop)
            status = "STOP";
        if(mUpdateStatus == UpdateStatus.Set)
            status = "SET";

        JSONObject response = Requestor.request(requestQueue, Endpoints.getRequestUrlUpdateQueueSlot(mQueueSlotId, status, mTokenNumber));

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
            myListener.onQueueSlotUpdated(queueSlot);
        }
    }
}