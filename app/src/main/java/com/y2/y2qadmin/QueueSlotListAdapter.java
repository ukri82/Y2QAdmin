package com.y2.y2qadmin;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.y2.y2qadmin.ServerInterface.TaskCreateQueueSlot;
import com.y2.y2qadmin.misc.AnimationUtils;
import com.y2.y2qadmin.misc.VolleySingleton;
import com.y2.y2qadmin.model.QueueSlot;
import com.y2.y2qadmin.model.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by u on 05.12.2016.
 */

public class QueueSlotListAdapter extends RecyclerView.Adapter<QueueSlotListAdapter.ViewHolder> implements TaskCreateQueueSlot.QueueSlotsCreateListener
{
    public interface QueueSlotClickListener
    {
        public void onClick(QueueSlot slot);
    }

    QueueSlotClickListener mListener;

    private ArrayList<QueueSlot> mDataset = new ArrayList<>();


    private VolleySingleton myVolleySingleton;
    private ImageLoader myImageLoader;
    private int myPreviousPosition = 0;
    Activity mActivity;

    public QueueSlotListAdapter(Activity activity, QueueSlotClickListener listener)
    {
        mActivity = activity;
        myVolleySingleton = VolleySingleton.getInstance(null);
        myImageLoader = myVolleySingleton.getImageLoader();
        mListener = listener;
    }

    public void appendTokenSlotList(List<QueueSlot> data)
    {
        int aNumItems = mDataset.size();
        mDataset.addAll(data);

        notifyItemRangeInserted(aNumItems, data.size());
    }

    public void add(ArrayList<QueueSlot> dataset)
    {
        mDataset = dataset;
    }

    @Override
    public void onQueueSlotCreated(QueueSlot queueSlot)
    {
        mDataset.add(0, queueSlot);
        notifyItemRangeInserted(0, 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public View mParentView;
        public TextView mTextView;
        public TextView mStartTimeView;
        public TextView mEndTimeView;
        public ImageView mPictureView;
        public ViewHolder(View v)
        {
            super(v);
            mParentView = v;
            mTextView = (TextView)v.findViewById(R.id.title);
            mStartTimeView = (TextView)v.findViewById(R.id.start_time);
            mEndTimeView = (TextView)v.findViewById(R.id.end_time);
            mPictureView = (ImageView)v.findViewById(R.id.picture);
        }
    }



    @Override
    public QueueSlotListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_slot_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /*private void setOrgImage(final Organization organization, final ImageView imageView)
    {
        if (!organization.mPhotoURL.equals("N.A") && !organization.mPhotoURL.equals(""))
        {
            if (organization.mBitmap == null)
            {
                myImageLoader.get(organization.mPhotoURL, new ImageLoader.ImageListener()
                {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                    {
                        organization.mBitmap = response.getBitmap();
                        imageView.setImageBitmap(organization.mBitmap);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        imageView.setImageResource(R.drawable.organization);
                    }
                });
            }
            else
            {
                imageView.setImageBitmap(organization.mBitmap);
            }
        }
        else
        {
            imageView.setImageResource(R.drawable.organization);
        }
    }*/

    @Override
    public void onBindViewHolder(QueueSlotListAdapter.ViewHolder holder, final int position)
    {
        holder.mParentView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.onClick(mDataset.get(position));
            }
        });

        Utils.updateBgColor(mActivity, holder.mParentView, mDataset.get(position).mQState);

        holder.mTextView.setText(mDataset.get(position).mQName);
        holder.mStartTimeView.setText(mDataset.get(position).mStartTime + "");
        holder.mEndTimeView.setText(mDataset.get(position).mEndTime + "");

        //setOrgImage(mDataset.get(position), holder.mPictureView);

        if (position > myPreviousPosition)
        {
            if(Build.VERSION.SDK_INT >= 11)
            {
                AnimationUtils.animateSunblind(holder, true);
            }
        }
        else
        {
            if(Build.VERSION.SDK_INT >= 11)
            {
                AnimationUtils.animateSunblind(holder, false);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }

    public void create()
    {
        new TaskCreateQueueSlot(this, "1").execute();
    }
}
