package com.y2.y2qadmin.ServerInterface;


/**
 * Created by Windows on 02-03-2015.
 */
public class Endpoints
{
    static String myServerIp;

    public static void setIP(String anIP_in)
    {
        myServerIp = anIP_in;
        synchronized(syncObject)
        {
            syncObject.notify();
        }
    }

    public static String getServerIPs(String aUserId_in, String aStaticIp_in)
    {
        String aQueryString = "http://" + aStaticIp_in + "/y2q/default/get_servers?";
        aQueryString += "UserId=" + aUserId_in;
        return aQueryString;
    }

    public static String getRequestUrlNextQueueSlotChunk(int aStart_in, int limit, String qId)
    {
        //get_token_slots
        String aQueryString = "http://" + myServerIp + "/y2q/default/get_queue_slots" + "?Start=" + aStart_in + "&Count=" + limit + "&QueueId=" + qId;

        return aQueryString;
    }

    public static String getRequestUrlCreateQueueSlot(String qId)
    {
        //get_token_slots
        String aQueryString = "http://" + myServerIp + "/y2q/default/create_queue_slot" + "?QueueId=" + qId;

        return aQueryString;
    }

    public static String getRequestUrlUpdateQueueSlot(String qSlotId, String updateStatus, int tokenNumber)
    {
        //get_token_slots
        String aQueryString = "http://" + myServerIp + "/y2q/default/update_queue_slot" + "?QueueSlotId=" + qSlotId + "&Status=" + updateStatus  + "&TokenNumber=" + tokenNumber;

        return aQueryString;
    }

    public static String getImageDownloadURL(String photoURL)
    {
        String imageURL = "http://" + myServerIp + "/y2q/default/download/" + photoURL;
        return imageURL;
    }

    public static void waitForIP()
    {
        if(myServerIp == null)
        {
            synchronized (syncObject)
            {
                try
                {
                    // Calling wait() will block this thread until another thread
                    // calls notify() on the object.
                    syncObject.wait();
                } catch (InterruptedException e)
                {
                    // Happens if someone interrupts your thread.
                }
            }
        }
    }

    private static Object syncObject = new Object();
}
