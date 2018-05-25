package com.example.vidiic.proyecto_music.Home;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vidiic.proyecto_music.MainActivity;
import com.example.vidiic.proyecto_music.R;

public class HomeActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private TextView[] mDots;
    private SliderAdapter sliderAdapter;

    private Button mNextBtn;
    private Button mBackBtn;
    private Button mFinalBtn;

    private int mCurrentPage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSlideViewPager =  findViewById(R.id.slideViewPager);
        mDotLayout =  findViewById(R.id.dotsLayout);

        mNextBtn =  findViewById(R.id.nextBtn);
        mBackBtn = findViewById(R.id.prevBtn);
        mFinalBtn = findViewById(R.id.finalBtn);

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewPanda);

        //movimiento botones next, bback, final


        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage + 1);
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });


        //anem a la home

        //delcarem el valor del final
        Button btnquanta = (Button) findViewById(R.id.finalBtn);
        btnquanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //amb aixo mirme si el boto funciona, en el android monitor sortira si lo donem al boto o no "filtrem per flx"
                Log.d("flx", "onClick()");
                //option + enter i importa
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }


    public void addDotsIndicator(int position) {


        mDots = new TextView[3];
        mDotLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {

            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));

        }
    }





    ViewPager.OnPageChangeListener viewPanda = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);

            mCurrentPage = position;

            if(position == 0) {
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(false);
                mFinalBtn.setEnabled(false);
                mBackBtn.setVisibility(View.INVISIBLE);
                mNextBtn.setVisibility(View.VISIBLE);
                mFinalBtn.setVisibility(View.INVISIBLE);


                mNextBtn.setText("Siguiente");
                mBackBtn.setText("");
                mFinalBtn.setText("");

            } else if (position == mDots.length - 1) {

                mNextBtn.setEnabled(false);
                mBackBtn.setEnabled(true);
                mFinalBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setVisibility(View.INVISIBLE);
                mFinalBtn.setVisibility(View.VISIBLE);



                mNextBtn.setText("");
                mBackBtn.setText("Atrás");
                mFinalBtn.setText("Empezar");

            } else {

                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mFinalBtn.setEnabled(false);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setVisibility(View.VISIBLE);
                mFinalBtn.setVisibility(View.INVISIBLE);


                mNextBtn.setText("Siguiente");
                mBackBtn.setText("Atrás");
                mFinalBtn.setText("");
            }




        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



}
