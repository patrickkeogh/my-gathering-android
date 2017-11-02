package com.programming.kantech.mygathering.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.programming.kantech.mygathering.R;
import com.programming.kantech.mygathering.data.model.mongo.Banner;
import com.programming.kantech.mygathering.data.retrofit.ApiClient_FileStack;
import com.programming.kantech.mygathering.data.retrofit.ApiInterface_FileStack;
import com.programming.kantech.mygathering.utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.filepicker.Filepicker;
import io.filepicker.FilepickerCallback;
import io.filepicker.models.FPFile;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by patrick keogh on 2017-10-19.
 *
 */

public class Fragment_Add_Banner extends Fragment {

    private String mImageUrl = "";
    private ApiInterface_FileStack apiServiceFileStack;

    @BindView(R.id.btn_next_to_save)
    Button btn_proceed;

    @BindView(R.id.iv_add_banner)
    ImageView iv_add_banner;

    @BindView(R.id.iv_banner)
    ImageView iv_banner;

    // Define a new interface AddBannerListener that triggers a callback in the host activity
    BannerListener mCallback;

    // AddBannerListener interface, calls a method in the host activity
    // after a callback has been triggered
    public interface BannerListener {
        void addBanner(String nextFrag, Banner banner);
    }

    /**
     * Static factory method that
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */
    public static Fragment_Add_Banner newInstance() {
        return new Fragment_Add_Banner();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Get the fragment layout for the driving list
        final View rootView = inflater.inflate(R.layout.fragment_banner, container, false);

        ButterKnife.bind(this, rootView);

        Filepicker.setKey(Constants.KEY_FILESTACK);
        Filepicker.setAppName("My Name");

        apiServiceFileStack =
                ApiClient_FileStack.getClient().create(ApiInterface_FileStack.class);


        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupForm();
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (Fragment_Add_Banner.BannerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DetailsListener");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Filepicker.REQUEST_CODE_GETFILE) {
            if (resultCode == RESULT_OK) {

//                if (data != null) {
//                    dumpIntent(data);
//                }

                //Log.i(Constants.TAG, "Data return from File stack:" + data.toString());

                // Filepicker always returns array of FPFile objects
                ArrayList<FPFile> fpFiles = null;

                if (data != null) {
                    fpFiles = data.getParcelableArrayListExtra(Filepicker.FPFILES_EXTRA);
                }

                // Option multiple was not set so only 1 object is expected
                final FPFile file = fpFiles.get(0);
                mImageUrl = file.getUrl();


                //Log.i(Constants.TAG, "FileObject URL:" + file.getUrl());

                Picasso.with(getContext()).load(file.getUrl()).into(iv_banner, new Callback() {
                    @Override
                    public void onSuccess() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                // Get the bitmap from the imageview
                                Bitmap innerBitmap = ((BitmapDrawable) iv_banner.getDrawable()).getBitmap();
                                calculateMaxCrop(innerBitmap.getHeight(), innerBitmap.getWidth(), file.getUrl());
                            }
                        }, 100);
                    }

                    @Override
                    public void onError() {

                    }
                });

//                Picasso.with(getContext())
//                        .load(file.getUrl())
//                        .into(picassoImageTarget(getContext(), "imageDir", "my_image.jpeg"));

            } else {
                // Handle errors here
                Log.e(Constants.TAG, " No file was selected.");
            }

        }
    }

    private void calculateMaxCrop(int height, int width, String url) {

        int crop_width;
        int crop_height;
        int crop_y = 0;
        int crop_x = 0;

        if ((width == (height * 2)) && (height == (width / 2))) {
            Log.i(Constants.TAG, "NO CROPPING REQUIRED");
            crop_width = width;
            crop_height = height;
            //crops width
        } else if (width > (height * 2)) {
            Log.i(Constants.TAG, "CROP WIDTH [" + (height * 2) + "," + height + "]");

            crop_width = height * 2;
            crop_height = height;

            // Crop equal amount from right and left side of image
            crop_x = (width - crop_width) / 2;

            // Crops height
        } else {
            Log.i(Constants.TAG, "CROP HEIGHT [" + width + "," + (width / 2) + "]");
            crop_width = width;
            crop_height = width / 2;

            // Crop equal amount from top and bottom of image
            crop_y = (height - crop_height) / 2;
        }


        String final_url = Constants.KEY_FILESTACK +
                "/crop=dim:[" + crop_x + "," + crop_y + "," + crop_width + "," + crop_height + "]/" + url;

        Log.i(Constants.TAG, "FINAL URL:" + final_url);
        Call<ResponseBody> call = apiServiceFileStack.getCroppedImage(final_url);

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                //Log.i(Constants.TAG, "Response:" + response);

                if (response.isSuccessful()) {
                    // Code 200, 201
                    ResponseBody body = response.body();

                    // Get the croppedimage returned from Filestack
                    byte[] bytes;
                    try {
                        if (body != null) {
                            bytes = body.bytes();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            saveImageToExternalStorage(bitmap);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e(Constants.TAG, "Image could not be cropped");
                    // TODO: Add code to deal with unsuccessful crop requests
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                // Log error here since request failed
                Log.e(Constants.TAG, t.toString());
                // TODO: Add code here to notify user crop failed
            }
        });


    }

    @OnClick(R.id.iv_add_banner)
    public void getBannerFromFileStack() {

        Intent intent = new Intent(getActivity(), Filepicker.class);
        //intent.putExtra("location", "S3");
        startActivityForResult(intent, Filepicker.REQUEST_CODE_GETFILE);
    }

    @OnClick(R.id.btn_next_to_save)
    public void proceedToNext() {

        Banner banner = new Banner();
        banner.setUrl(mImageUrl);

        mCallback.addBanner(Constants.TAG_FRAGMENT_ADD_SAVE, banner);
    }

//    public static void dumpIntent(Intent i) {
//
//        Bundle bundle = i.getExtras();
//        if (bundle != null) {
//            Set<String> keys = bundle.keySet();
//            Iterator<String> it = keys.iterator();
//            Log.e(Constants.TAG, "Dumping Intent start");
//            while (it.hasNext()) {
//                String key = it.next();
//                Log.e(Constants.TAG, "[" + key + "=" + bundle.get(key) + "]");
//            }
//            Log.e(Constants.TAG, "Dumping Intent end");
//        }
//    }

    @Override
    public void onDestroy() {
        Filepicker.unregistedLocalFileUploadCallbacks();
        super.onDestroy();
    }

    private void saveImageToFileStack(Uri uri) {
        Log.i(Constants.TAG, "saveImageToFileStack called");

        Filepicker.uploadLocalFile(uri, getContext(), new FilepickerCallback() {
            @Override
            public void onFileUploadSuccess(final FPFile fpFile) {
                // Do something on success
                Log.i(Constants.TAG, "File has been uploaded to Filestack:" + fpFile.getUrl());
                mImageUrl = fpFile.getUrl();

                // Update the form from the main thread
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        setupForm();
                    }
                });
            }

            @Override
            public void onFileUploadError(Throwable error) {
                // Do something on error
            }

            @Override
            public void onFileUploadProgress(Uri uri, float progress) {
                // Do something on progress
            }
        });
    }

//    private void loadDownloadedImage(final Bitmap bitmap) {
//        //Picasso.with(getContext()).load(myImageFile).into(iv_downloaded_image);
//
//        Handler uiHandler = new Handler(Looper.getMainLooper());
//        uiHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                iv_downloaded_image.setImageBitmap(bitmap);
//                //Picasso.with(getContext()).load.load(bitmap).into(iv_downloaded_image);
//            }
//        });
//    }

    private void saveImageToExternalStorage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Uri uri = Uri.fromFile(file);
        Log.i(Constants.TAG, "URI:" + uri);

        saveImageToFileStack(uri);

        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(getContext(), new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i(Constants.TAG, "Scanned " + path + ":");
                        Log.i(Constants.TAG, "-> uri=" + uri);
                    }
                });

    }

    private void setupForm() {
        Log.i(Constants.TAG, "Setup Form called");

        if (mImageUrl.length() == 0) {
            Log.i(Constants.TAG, "Url NULL:" + mImageUrl);
            btn_proceed.setEnabled(false);
            btn_proceed.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));
            iv_banner.setVisibility(View.GONE);

        } else {
            Log.i(Constants.TAG, "Url NOT NULL:" + mImageUrl);

            btn_proceed.setEnabled(true);
            btn_proceed.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            iv_banner.setVisibility(View.VISIBLE);

        }
    }
}
