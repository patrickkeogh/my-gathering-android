package com.programming.kantech.mygathering.view.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.mongo.Banner;
import com.programming.kantech.mygathering.data.model.mongo.Gathering;
import com.programming.kantech.mygathering.data.model.pojo.Gathering_Pojo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by patrick keogh on 2017-10-24.
 */

public class ImageAdapter extends BaseAdapter {

    // Member variables
    private Context mContext;
    private List<Gathering_Pojo> mGatherings;
    private static LayoutInflater inflater = null;

    public ImageAdapter(Context context, List<Gathering_Pojo> gatherings) {
        this.mContext = context;
        this.mGatherings = gatherings;
        this.inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mGatherings.size();
    }

    @Override
    public Gathering_Pojo getItem(int i) {
        return mGatherings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

//        View gridViewAndroid;
//        LayoutInflater inflater = (LayoutInflater) mContext
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        if (convertView == null) {
//
//            gridViewAndroid = new View(mContext);
//            gridViewAndroid = inflater.inflate(R.layout.item_grid_banner, null);
//            //TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
//            ImageView iv_gathering_banner = (ImageView) gridViewAndroid.findViewById(R.id.iv_gathering_banner);
//            //textViewAndroid.setText(gridViewString[i]);
//            //imageViewAndroid.setImageResource(gridViewImageId[i]);
//
//            Gathering gathering = getItem(i);
//
//            Banner banner = gathering.getBanner();
//
//            Picasso.with(mContext)
//                    .load(banner.getUrl())
//                    .into(iv_gathering_banner);
//        } else {
//            gridViewAndroid = (View) convertView;
//        }

        if (convertView == null) {
            // If convertView is null then inflate the appropriate layout file
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_banner, null);
        }
        else {

        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_gathering_banner);
        TextView text = (TextView) convertView.findViewById(R.id.tv_gathering_banner_title);


        Gathering_Pojo gathering = getItem(i);

        text.setText(gathering.getName());

        Picasso.with(mContext)
                .load(gathering.getBanner_url())
                .into(imageView);

        return convertView;


//        LayoutInflater inflater = (LayoutInflater) mContext
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View gridView;
//        ImageView iv_gathering_banner = null;
//
//        if (convertView == null) {
//
//            gridView = new View(mContext);
//
//            // get layout from mobile.xml
//            gridView = inflater.inflate(R.layout.item_grid_banner, null);
//
//            // set image based on selected text
//            iv_gathering_banner = (ImageView) gridView.findViewById(R.id.iv_gathering_banner);
//
//            Gathering gathering = getItem(i);
//
//            Banner banner = gathering.getBanner();
//
//            Picasso.with(mContext)
//                    .load(banner.getUrl())
//                    .into(iv_gathering_banner);
//
//        } else {
//            gridView = (View) convertView;
//        }
//
//
//        return gridView;

    }
}
