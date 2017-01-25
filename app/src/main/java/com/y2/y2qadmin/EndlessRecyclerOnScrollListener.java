package com.y2.y2qadmin;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener
{
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager)
    {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading)
        {
            if (totalItemCount > previousTotal)
            {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold))
        {
            // End has been reached

            // Do something
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }

        onScrolledExtended(recyclerView, dx, dy);
    }

    private static final int HIDE_THRESHOLD = 200;
    private int scrolledDistance = 0;
    boolean controlsVisible = true;

    public void setControlsVisible(boolean aVal_in)
    {
        controlsVisible = aVal_in;
    }

    public void onScrolledExtended(RecyclerView recyclerView, int dx, int dy)
    {
        /*Log.d("", "scrolledDistance : " + scrolledDistance);
        if (scrolledDistance == 0  && scrolledDistance + dx >= 0)
        {
            Log.d("", "in down");
            onHide();
        }
        else if (scrolledDistance >= 0 && scrolledDistance - dx <= 0)
        {
            Log.d("", "in up");
            onShow();
            scrolledDistance = 0;
        }
        else
        {
            Log.d("", "in increase");
            scrolledDistance += dy;
        }*/
        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible)
        {
            onHide();
            controlsVisible = false;
            scrolledDistance = 0;
        }
        else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible)
        {
            onShow();
            controlsVisible = true;
            scrolledDistance = 0;
        }

        if((controlsVisible && dy>0) || (!controlsVisible && dy<0))
        {
            scrolledDistance += dy;
        }
    }

    public abstract void onHide();
    public abstract void onShow();


    public abstract void onLoadMore(int current_page);
}