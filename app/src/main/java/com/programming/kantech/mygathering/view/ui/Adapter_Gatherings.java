package com.programming.kantech.mygathering.view.ui;

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
 * Created by patrick keogh on 2017-05-18.
 */

//import com.example.android.sunshine.utilities.SunshineDateUtils;
//import com.example.android.sunshine.utilities.SunshineWeatherUtils;

/**
 * {@link Adapter_Gatherings} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.support.v7.widget.RecyclerView}.
 */
public class Adapter_Gatherings extends RecyclerView.Adapter<Adapter_Gatherings.GatheringsAdapterViewHolder> {

    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;

    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our ForecastAdapter, we receive an instance of a class that has implemented
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
                .inflate(R.layout.gathering_list_item, viewGroup, false);

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



        Log.i(Constants.TAG, "Banner URL:" + gathering.getBanner_url());

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
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

            try {
                Date date = format.parse(gathering.getStart_date());
                viewHolder.tv_gathering_date.setText(Utils_DateFormatting.getFormattedGatheringDate(date));

            } catch (ParseException e) {
                e.printStackTrace();
            }


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
     * the weather data is reset. When this method is called, we assume we have a completely new
     * set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newCursor the new cursor to use as ForecastAdapter's data source
     */
    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
//      COMPLETED (12) After the new Cursor is set, call notifyDataSetChanged
        notifyDataSetChanged();
    }

    /**
     * Get the Patient object related to an item position.
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

//public class Adapter_Gatherings extends RecyclerView.Adapter<Adapter_Gatherings.GatheringViewHolder> {
//
//    /*
//     * An on-click handler that we've defined to make it easy for an Activity to interface with
//     * our RecyclerView
//     */
//    final private ListItemClickListener mOnClickListener;
//
//    /*
//     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
//     * constructor of our Adapter_Gathering, we receive an instance of a class that has implemented
//     * said interface. We store that instance in this variable to call the onClick method whenever
//     * an item is clicked in the list.
//     */
//    final private AdapterGatheringOnClickHandler mClickHandler;
//
//    /**
//     * The interface that receives onClick messages.
//     */
//    public interface AdapterGatheringOnClickHandler {
//        void onClick(int clickedItemIndex, Gathering gathering);
//    }
//
//    /* The context we use with utility methods, app resources and layout inflaters */
//    private final Context mContext;
//
//    // Number of views to create at any one time (4 more than will show on screen)
//    private static int viewHolderCount;
//
//    private int mNumberItems;
//
//    private Cursor mCursor;
//
//    private List<Gathering> mGatherings = Collections.emptyList();
//    ;
//
//    /**
//     * Creates a Gatherings Adapter
//     *
//     * @param context      Used to talk to the UI and app resources
//     * @param clickHandler The on-click handler for this adapter. This single handler is called
//     *                     when an item is clicked.
//     */
//    public Adapter_Gatherings(@NonNull Context context, AdapterGatheringOnClickHandler clickHandler) {
//        mContext = context;
//        mClickHandler = clickHandler;
//        viewHolderCount = 0;
//    }
//
//    /**
//     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
//     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
//     *
//     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
//     * @param viewType  If your RecyclerView has more than one type of item (like ours does) you
//     *                  can use this viewType integer to provide a different layout. See
//     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
//     *                  for more details.
//     * @return A new ForecastAdapterViewHolder that holds the View for each list item
//     */
//    @Override
//    public GatheringAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//
//        View view = LayoutInflater
//                .from(mContext)
//                .inflate(R.layout.gathering_list_item, viewGroup, false);
//
//        view.setFocusable(true);
//
//        return new GatheringAdapterViewHolder(view);
//    }
//
////    /**
////     *
////     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
////     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
////     *
////     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
////     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
////     *                  can use this viewType integer to provide a different layout. See
////     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
////     *                  for more details.
////     * @return A new NumberViewHolder that holds the View for each list item
////     */
////    @Override
////    public GatheringViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
////
////        Context context = viewGroup.getContext();
////        int layoutIdForListItem = R.layout.gathering_list_item;
////        LayoutInflater inflater = LayoutInflater.from(context);
////        boolean shouldAttachToParentImmediately = false;
////
////        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
////        GatheringViewHolder viewHolder = new GatheringViewHolder(view);
////
////        //viewHolder.viewHolderIndex.setText("ViewHolder index: " + viewHolderCount);
////
////        int backgroundColorForViewHolder = ColorUtils
////                .getViewHolderBackgroundColorFromInstance(context, viewHolderCount);
////        viewHolder.itemView.setBackgroundColor(backgroundColorForViewHolder);
////
////        viewHolderCount++;
////        Log.d(Constants.TAG, "onCreateViewHolder: number of ViewHolders created: "
////                + viewHolderCount);
////
////        return viewHolder;
////    }
//
//    /**
//     * This method is used to set the weather forecast on a ForecastAdapter if we've already
//     * created one. This is handy when we get new data from the web but don't want to create a
//     * new ForecastAdapter to display it.
//     *
//     * @param gatherings_in The data to be displayed.
//     */
//    public void setGatheringData(List<Gathering> gatherings_in) {
//        mGatherings = gatherings_in;
//        notifyDataSetChanged();
//    }
//
//    /**
//     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
//     * position. In this method, we update the contents of the ViewHolder to display the correct
//     * indices in the list for this particular position, using the "position" argument that is conveniently
//     * passed into us.
//     *
//     * @param holder   The ViewHolder which should be updated to represent the contents of the
//     *                 item at the given position in the data set.
//     * @param position The position of the item within the adapter's data set.
//     */
//    @Override
//    public void onBindViewHolder(GatheringAdapterViewHolder holder, int position) {
////        Log.d(Constants.TAG, "#" + position);
////        Gathering gathering = mGatherings.get(position);
////
////
////        holder.bind(gathering);
//
//        mCursor.moveToPosition(position);
//
//
//
//
//    }
//
//    /**
//     * This method simply returns the number of items to display. It is used behind the scenes
//     * to help layout our Views and for animations.
//     *
//     * @return The number of gatherings available
//     */
//    @Override
//    public int getItemCount() {
//        if (null == mCursor) return 0;
//        return mCursor.getCount();
//    }
//
//    /**
//     * Swaps the cursor used by the GatheringsAdapter for its data. This method is called by
//     * MainActivity after a load has finished, as well as when the Loader responsible for loading
//     * the weather data is reset. When this method is called, we assume we have a completely new
//     * set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
//     *
//     * @param newCursor the new cursor to use as ForecastAdapter's data source
//     */
//    void swapCursor(Cursor newCursor) {
//        mCursor = newCursor;
//        notifyDataSetChanged();
//    }
//
//    /**
//     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
//     * a cache of the child views for a forecast item. It's also a convenient place to set an
//     * OnClickListener, since it has access to the adapter and the views.
//     */
//    class GatheringAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        final TextView tv_gathering_name;
//        final TextView tv_gathering_date;
//        final TextView tv_gathering_location;
//        final TextView tv_gathering_city;
//        final TextView tv_gathering_country;
//
//        final TextView tv_gathering_topic;
//        final TextView tv_gathering_type;
//
//
//        final ImageView iv_gathering_banner;
//
//        GatheringAdapterViewHolder(View view) {
//            super(view);
//            tv_gathering_name = (TextView) itemView.findViewById(R.id.tv_gathering_name);
//            tv_gathering_city = (TextView) itemView.findViewById(R.id.tv_gathering_city);
//            tv_gathering_date = (TextView) itemView.findViewById(R.id.tv_gathering_date);
//            tv_gathering_location = (TextView) itemView.findViewById(R.id.tv_gathering_location);
//            tv_gathering_country = (TextView) itemView.findViewById(R.id.tv_gathering_country);
//
//            tv_gathering_topic = (TextView) itemView.findViewById(R.id.tv_gathering_topic);
//            tv_gathering_type = (TextView) itemView.findViewById(R.id.tv_gathering_type);
//
//
//            iv_gathering_banner = (ImageView) itemView.findViewById(R.id.iv_gathering_banner);
//
//            view.setOnClickListener(this);
//        }
//
//        /**
//         * This gets called by the child views during a click. We fetch the date that has been
//         * selected, and then call the onClick handler registered with this adapter, passing that
//         * date.
//         *
//         * @param v the View that was clicked
//         */
//        @Override
//        public void onClick(View v) {
//            //  COMPLETED (13) Instead of passing the String from the data array, use the weatherSummary text!
//            //String weatherForDay = weatherSummary.getText().toString();
//            //mClickHandler.onClick(weatherForDay);
//
//            int clickedPosition = getAdapterPosition();
//            mClickHandler.onClick(clickedPosition, );
////          mOnClickListener.onListItemClick(clickedPosition, mGatherings.get(clickedPosition));
//        }
//    }
//
////    // COMPLETED (5) Implement OnClickListener in the NumberViewHolder class
////    /**
////     * Cache of the children views for a list item.
////     */
////    class GatheringViewHolder extends RecyclerView.ViewHolder
////            implements View.OnClickListener {
////
////        // Will display the position in the list, ie 0 through getItemCount() - 1
////        TextView tv_gathering_name;
////        TextView tv_gathering_date;
////        TextView tv_gathering_location;
////        TextView tv_gathering_city;
////        TextView tv_gathering_country;
////
////        TextView tv_gathering_topic;
////        TextView tv_gathering_type;
////
////
////        ImageView iv_gathering_banner;
////
////        /**
////         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
////         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
////         * onClick method below.
////         * @param itemView The View that you inflated in
////         *                 {@link Adapter_Gatherings#onCreateViewHolder(ViewGroup, int)}
////         */
////        public GatheringViewHolder(View itemView) {
////            super(itemView);
////
////            tv_gathering_name = (TextView) itemView.findViewById(R.id.tv_gathering_name);
////            tv_gathering_city = (TextView) itemView.findViewById(R.id.tv_gathering_city);
////            tv_gathering_date = (TextView) itemView.findViewById(R.id.tv_gathering_date);
////            tv_gathering_location = (TextView) itemView.findViewById(R.id.tv_gathering_location);
////            tv_gathering_country = (TextView) itemView.findViewById(R.id.tv_gathering_country);
////
////            tv_gathering_topic = (TextView) itemView.findViewById(R.id.tv_gathering_topic);
////            tv_gathering_type = (TextView) itemView.findViewById(R.id.tv_gathering_type);
////
////
////            iv_gathering_banner = (ImageView) itemView.findViewById(R.id.iv_gathering_banner);
////
////
////
////            //viewHolderIndex = (TextView) itemView.findViewById(R.id.tv_view_holder_instance);
////            // COMPLETED (7) Call setOnClickListener on the View passed into the constructor (use 'this' as the OnClickListener)
////            itemView.setOnClickListener(this);
////        }
////
////        /**
////         * A method we wrote for convenience. This method will take an integer as input and
////         * use that integer to display the appropriate text within a list item.
////         * @param gathering A gathering to be shown in the textview
////         */
////        void bind(Gathering gathering) {
////
////            Banner banner = gathering.getBanner();
////
////            if(banner == null) {
////
////                Picasso.with(mContext)
////                        .load(R.drawable.blank)
////                        .into(iv_gathering_banner);
////            }else{
////                Log.d(Constants.TAG, "Banner:" + banner.toString());
////
////                Picasso.with(mContext)
////                        .load(banner.getUrl())
////                        .into(iv_gathering_banner);
////            }
////
////            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
////
////            try {
////                Date date = format.parse(gathering.getGatheringStartDateTime());
////                tv_gathering_date.setText(Utils_DateFormatting.getFormattedGatheringDate(date));
////
////            } catch (ParseException e) {
////                e.printStackTrace();
////            }
////
////
////            tv_gathering_name.setText(gathering.getName());
////
////
////
////            Location location = gathering.getLocation();
////            tv_gathering_location.setText(location.getName());
////            tv_gathering_city.setText(location.getLocality());
////            tv_gathering_country.setText(location.getCountry());
////
////            List<GatheringType> type = gathering.getType();
////            tv_gathering_type.setText(type.get(0).getId());
////
////            List<GatheringTopic> topic = gathering.getTopic();
////            tv_gathering_topic.setText(topic.get(0).getId());
////
////
////        }
////
////
////        /**
////         * Called whenever a user clicks on an item in the list.
////         * @param v The View that was clicked
////         */
////        @Override
////        public void onClick(View v) {
////            int clickedPosition = getAdapterPosition();
////            mOnClickListener.onListItemClick(clickedPosition, mGatherings.get(clickedPosition));
////        }
////    }
//}
//
