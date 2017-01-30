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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.y2.y2qadmin.ServerInterface.TaskLoadQueueSlots;
import com.y2.y2qadmin.model.Queue;
import com.y2.y2qadmin.model.QueueSlot;
import com.y2.y2qadmin.model.Utils;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class QueueSlotListActivityFragment extends Fragment implements TaskLoadQueueSlots.QueueSlotsLoadedListener, QueueSlotListAdapter.QueueSlotClickListener
{

    private RecyclerView mTokenListView;
    private QueueSlotListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    Queue mQueue;

    public QueueSlotListActivityFragment()
    {
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState)
    {
        super.onViewCreated(v, savedInstanceState);
    }

    public void initialize(Queue queue)
    {
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAdapter.create(mQueue.mId);

            }
        });

        mQueue = queue;
        initQueueSlotList();

        initQueueHeader();
    }

    void initQueueHeader()
    {
        TextView textView = (TextView)getView().findViewById(R.id.queue_name);
        textView.setText(mQueue.mQName);
        textView = (TextView)getView().findViewById(R.id.queue_address);
        textView.setText(mQueue.mAddress);

        ImageView imgView = (ImageView)getView().findViewById(R.id.queue_picture);
        Utils.setImage(mAdapter.getImageLoader(), mQueue.mPhotoURL, imgView, R.drawable.ic_person_black_48dp);
    }
    void initQueueSlotList()
    {
        mTokenListView = (RecyclerView) getView().findViewById(R.id.queue_slot_list_view);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mTokenListView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new QueueSlotListAdapter(getActivity(), this, mTokenListView);

        mTokenListView.setAdapter(mAdapter);

        final TaskLoadQueueSlots.QueueSlotsLoadedListener listener = this;
        mTokenListView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                new TaskLoadQueueSlots(listener, mAdapter.getItemCount(), 10, mQueue.mId).execute();
            }


            public void onHide()
            {

            }
            public void onShow()
            {

            }
        });
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



    @Override
    public void onClick(QueueSlot slot)
    {
        this.getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        Intent intent = new Intent(this.getActivity(), QueueSlotActivity.class);
        intent.putExtra(QueueSlot.QUEUE_SLOT_OBJ, slot);
        intent.putExtra(Queue.QUEUE_OBJ, mQueue);
        startActivity(intent);
    }

    public void refresh()
    {
        if(mAdapter != null)
        {
            mAdapter.clear();
            new TaskLoadQueueSlots(this, 0, 10, mQueue.mId).execute();
        }
    }
}
