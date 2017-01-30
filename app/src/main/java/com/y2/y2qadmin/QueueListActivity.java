package com.y2.y2qadmin;

import android.Manifest;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.y2.y2qadmin.ServerInterface.ServerIPProvider;
import com.y2.y2qadmin.ServerInterface.TaskLoadQueueSlots;
import com.y2.y2qadmin.ServerInterface.TaskLoadQueues;
import com.y2.y2qadmin.misc.PermissionChecker;
import com.y2.y2qadmin.misc.VolleySingleton;
import com.y2.y2qadmin.model.DeviceIdentity;
import com.y2.y2qadmin.model.Queue;
import com.y2.y2qadmin.model.QueueSlot;

import java.util.ArrayList;

public class QueueListActivity extends AppCompatActivity implements QueueListAdapter.QueueClickListener, TaskLoadQueues.QueuesLoadedListener
{

    private RecyclerView mTokenListView;
    private QueueListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    ServerIPProvider myServerIPProvider = new ServerIPProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        VolleySingleton.getInstance(this);  //  Just initialize the singleton
        DeviceIdentity.initialize(this);
        myServerIPProvider.create();

        setContentView(R.layout.activity_queue_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if(new PermissionChecker(this).checkPermission(Manifest.permission.INTERNET))
        {
            initActivity();
        }
    }

    void initActivity()
    {
        mTokenListView = (RecyclerView) findViewById(R.id.queue_list_view);
        mLayoutManager = new LinearLayoutManager(this);
        mTokenListView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new QueueListAdapter(this, this, mTokenListView);

        mTokenListView.setAdapter(mAdapter);

        final TaskLoadQueues.QueuesLoadedListener listener = this;
        mTokenListView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                new TaskLoadQueues(listener, mAdapter.getItemCount(), 10, "1").execute();
            }


            public void onHide()
            {

            }
            public void onShow()
            {

            }
        });
        new TaskLoadQueues(listener, mAdapter.getItemCount(), 10, "1").execute();
    }



    @Override
    public void onClick(Queue queue)
    {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        Intent intent = new Intent(this, QueueSlotListActivity.class);
        intent.putExtra(Queue.QUEUE_OBJ, queue);
        startActivity(intent);
    }

    @Override
    public void onQueuesLoaded(ArrayList<Queue> listQueues)
    {
        mAdapter.appendQueueList(listQueues);
    }
}
