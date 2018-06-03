package com.example.vidiic.proyecto_music.Home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.vidiic.proyecto_music.R;

import org.w3c.dom.Text;

/**
 * Created by poldominguez on 23/5/18.
 */

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {

        this.context = context;

        slide_headings = new String[]{

                context.getResources().getString(R.string.sliderShare),
                context.getResources().getString(R.string.slideListen),
                context.getResources().getString(R.string.sliderFindOut)
        };

        slide_desc = new String[]{
                context.getResources().getString(R.string.sliderShareText),
                context.getResources().getString(R.string.slideListenText),
                context.getResources().getString(R.string.slideFindOutText)
        };

    }

    public int[] slide_images = {

            R.drawable.share,
            R.drawable.headphones,
            R.drawable.eyes
    };

    String[] slide_headings;

    String[] slide_desc;



    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == (RelativeLayout) o;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = view.findViewById(R.id.slide_image);
        TextView slideHeading = view.findViewById(R.id.slide_heading);
        TextView slideDescription = view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_desc[position]);

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
         container.removeView((RelativeLayout)object);
    }
}

























