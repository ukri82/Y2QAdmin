package com.y2.y2qadmin;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;
import com.y2.y2qadmin.ServerInterface.TaskLoadQueueSlots;
import com.y2.y2qadmin.model.QueueSlot;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class QueueSlotListActivityFragment extends Fragment implements TaskLoadQueueSlots.QueueSlotsLoadedListener, QueueSlotListAdapter.QueueSlotClickListener
{

    private RecyclerView mTokenListView;
    private QueueSlotListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    public QueueSlotListActivityFragment()
    {
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState)
    {
        super.onViewCreated(v, savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAdapter.create();

            }
        });

        mTokenListView = (RecyclerView) getView().findViewById(R.id.queue_slot_list_view);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mTokenListView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new QueueSlotListAdapter(getActivity(), this);

        mTokenListView.setAdapter(mAdapter);

        final TaskLoadQueueSlots.QueueSlotsLoadedListener listener = this;
        mTokenListView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                new TaskLoadQueueSlots(listener, mAdapter.getItemCount(), 10, "1").execute();
            }


            public void onHide()
            {

            }
            public void onShow()
            {

            }
        });

        new TaskLoadQueueSlots(this, 0, 10, "1").execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_queue_slot, container, false);
    }

    @Override
    public void onQueueSlotsLoaded(ArrayList<QueueSlot> listQueueSlots)
    {
        mAdapter.appendTokenSlotList(listQueueSlots);
    }

    public static String QUEUE_SLOT_OBJ = "QueueSlot";

    @Override
    public void onClick(QueueSlot slot)
    {
        Intent intent = new Intent(this.getActivity(), QueueSlotActivity.class);
        intent.putExtra(QUEUE_SLOT_OBJ, slot);
        startActivity(intent);
    }
}
