package com.programming.kantech.mygathering.view.ui;
/**
 * Created by patrick keogh on 2017-05-18.
 */


import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.pojo.Gathering_Pojo;
import com.programming.kantech.mygathering.provider.Contract_MyGathering;
import com.programming.kantech.mygathering.utils.Constants;
import com.programming.kantech.mygathering.utils.Utils_DateFormatting;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * {@link Adapter_Gatherings} exposes a list of gatherings
 * from a {@link android.database.Cursor} to a {@link android.support.v7.widget.RecyclerView}.
 */
public class Adapter_Gatherings extends RecyclerView.Adapter<Adapter_Gatherings.GatheringsAdapterViewHolder> {

    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;

    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our Adapter_Gatherings, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onClick method whenever
     * an item is clicked in the list.
     */
    final private GatheringsAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface GatheringsAdapterOnClickHandler {
        void onClick(long id);
    }

    private Cursor mCursor;

    /**
     * Creates an Adapter_Gatherings.
     *
     * @param context      Used to talk to the UI and app resources
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public Adapter_Gatherings(@NonNull Context context, GatheringsAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (like ours does) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new GatheringsAdapterViewHolder that holds the View for each list item
     */
    @Override
    public GatheringsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_gathering, viewGroup, false);

        view.setFocusable(true);

        return new GatheringsAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param viewHolder The ViewHolder which should be updated to represent the
     *                   contents of the item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(GatheringsAdapterViewHolder viewHolder, int position) {
        mCursor.moveToPosition(position);

        /* Read date from the cursor */
        Gathering_Pojo gathering = Contract_MyGathering.GatheringEntry.getGatheringFromCursor(mCursor);

        viewHolder.itemView.setTag(position);

        //Log.i(Constants.TAG, "Banner URL:" + gathering.getBanner_url());

        if (gathering.getBanner_url() == "null") {
            Picasso.with(mContext)
                    .load(R.drawable.blank)
                    .placeholder(R.drawable.blank)
                    .into(viewHolder.iv_gathering_banner);
        } else {
            Picasso.with(mContext)
                    .load(gathering.getBanner_url())
                    .placeholder(R.drawable.blank)
                    .into(viewHolder.iv_gathering_banner);
        }
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);


        try {
            Date date = utcFormat.parse(gathering.getStart_date());
            viewHolder.tv_gathering_date.setText(Utils_DateFormatting.getFormattedGatheringDate(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //DateFormat utcFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

        Date createdDate = Utils_DateFormatting.convertUtcToLocal(gathering.getCreatedAt_date());

        if(createdDate != null){
            viewHolder.tv_gathering_createdAt.setText(Utils_DateFormatting.getFormattedGatheringDate(createdDate));
        }



//        try {
//            String dateStr = gathering.getCreatedAt_date();
//            Log.i(Constants.TAG, "Created Date string:" + dateStr);
//
//            //SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a");
//
//            utcFormat2.setTimeZone(TimeZone.getTimeZone("UTC"));
//            Date date = utcFormat2.parse(dateStr);
//            utcFormat2.setTimeZone(TimeZone.getDefault());
//            String formattedDate = utcFormat2.format(date);
//
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


//        try {
//
//            Date date = format.parse(gathering.getCreatedAt_date());
//            viewHolder.tv_gathering_createdAt.setText(Utils_DateFormatting.getFormattedGatheringDate(date));
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        viewHolder.tv_gathering_name.setText(gathering.getName());


        //Location location = gathering.getLocation();
        viewHolder.tv_gathering_location.setText(gathering.getLocation_name());
        viewHolder.tv_gathering_city.setText(gathering.getLocation_city());
        viewHolder.tv_gathering_country.setText(gathering.getLocation_country());
        viewHolder.tv_gathering_type.setText(gathering.getType());
        viewHolder.tv_gathering_topic.setText(gathering.getTopic());
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    /**
     * Swaps the cursor used by the ForecastAdapter for its weather data. This method is called by
     * MainActivity after a load has finished, as well as when the Loader responsible for loading
     * the data is reset. When this method is called, we assume we have a completely new
     * set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newCursor the new cursor to use as ForecastAdapter's data source
     */
    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    /**
     * Get the object related to an item position.
     *
     * @param position the position of the cursor
     * @return Patient
     */
    public Gathering_Pojo getGathering(int position) {

        mCursor.moveToPosition(position);
        return Contract_MyGathering.GatheringEntry.getGatheringFromCursor(mCursor);
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class GatheringsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView tv_gathering_name;
        final TextView tv_gathering_date;
        final TextView tv_gathering_location;
        final TextView tv_gathering_city;
        final TextView tv_gathering_country;

        final TextView tv_gathering_topic;
        final TextView tv_gathering_type;

        final TextView tv_gathering_createdAt;


        final ImageView iv_gathering_banner;

        GatheringsAdapterViewHolder(View view) {
            super(view);
            tv_gathering_name = (TextView) view.findViewById(R.id.tv_gathering_name);
            tv_gathering_city = (TextView) view.findViewById(R.id.tv_gathering_city);
            tv_gathering_date = (TextView) view.findViewById(R.id.tv_gathering_date);
            tv_gathering_location = (TextView) view.findViewById(R.id.tv_gathering_location);
            tv_gathering_country = (TextView) view.findViewById(R.id.tv_gathering_country);

            tv_gathering_topic = (TextView) view.findViewById(R.id.tv_gathering_topic);
            tv_gathering_type = (TextView) view.findViewById(R.id.tv_gathering_type);

            tv_gathering_createdAt = (TextView) view.findViewById(R.id.tv_gathering_createdAt);


            iv_gathering_banner = (ImageView) view.findViewById(R.id.iv_gathering_banner);

            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * date.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {

            Log.i(Constants.TAG, "Onclick Called");
            int adapterPosition = getAdapterPosition();

            mCursor.moveToPosition(adapterPosition);
            long id = mCursor.getLong(Constants.COL_ID);
            Log.i(Constants.TAG, "id returned:" + id);


            //int position = (int) v.getTag();
            //Log.i(Constants.TAG, "Position:" + position);

            //mClickHandler.onClick(getGathering(position));

            mClickHandler.onClick(id);
        }
    }
}