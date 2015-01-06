package com.konifar.activitytransitionsample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity {

    private static final int SAMPLE_COUNTS = 30;

    @InjectView(R.id.grid_main)
    GridView mGridMain;

    private PhotosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initGridView();

        List<PhotoModel> photos = PhotoUtils.getSamplePhotos(SAMPLE_COUNTS);
        for (PhotoModel photo : photos) {
            adapter.add(photo);
        }
    }

    private void initGridView() {
        adapter = new PhotosAdapter(this);
        mGridMain.setAdapter(adapter);
        mGridMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoModel photo = adapter.getItem(position);
                DetailActivity.start(MainActivity.this, view, photo);
            }
        });
    }

    static class ViewHolder {
        @InjectView(R.id.img_preview)
        AspectRatioImageView mImgPreview;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    private class PhotosAdapter extends ArrayAdapter<PhotoModel> {

        public PhotosAdapter(Context context) {
            super(context, R.layout.item_photo, new ArrayList<PhotoModel>());
        }

        @Override
        public View getView(int pos, View view, ViewGroup parent) {
            ViewHolder holder;
            final PhotoModel photo = getItem(pos);

            if (view == null || view.getTag() == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
                holder = new ViewHolder(view);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.mImgPreview.setImageResource(photo.resId);
            view.setTag(holder);

            return view;
        }

    }

}
