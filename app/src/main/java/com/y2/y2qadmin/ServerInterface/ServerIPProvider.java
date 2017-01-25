package com.y2.y2qadmin.ServerInterface;


import com.y2.y2qadmin.model.DeviceIdentity;

import java.util.ArrayList;

/**
 * Created by u on 28.10.2015.
 */
public class ServerIPProvider implements TaskGetServerIPs.TaskGetServerIPsQueryListener
{
    private static ServerIPProvider myInstance;

    public ServerIPProvider()
    {
        myInstance = this;
    }

    public static void create()
    {
            new TaskGetServerIPs(myInstance, DeviceIdentity.get(), "sevencenter6105.cloudapp.net").execute();
    }

    @Override
    public void onServerIPsAvailable(ArrayList<String> anIPList)
    {
        Endpoints.setIP(anIPList.get(0));
    }
}
