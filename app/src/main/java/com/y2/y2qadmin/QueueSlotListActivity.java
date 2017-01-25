package com.y2.y2qadmin;

import android.os.Bundle;
import android.app.Activity;

import com.y2.y2qadmin.ServerInterface.ServerIPProvider;
import com.y2.y2qadmin.misc.VolleySingleton;
import com.y2.y2qadmin.model.DeviceIdentity;

public class QueueSlotListActivity extends Activity
{

    ServerIPProvider myServerIPProvider = new ServerIPProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        VolleySingleton.getInstance(this);  //  Just initialize the singleton
        DeviceIdentity.initialize(this);
        myServerIPProvider.create();

        setContentView(R.layout.activity_queue_slot_list);
    }

}
