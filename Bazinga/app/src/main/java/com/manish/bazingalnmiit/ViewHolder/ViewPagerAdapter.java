package com.manish.bazingalnmiit.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.manish.bazingalnmiit.BannerFood;
import com.manish.bazingalnmiit.Commondata.Commondata;
import com.manish.bazingalnmiit.R;
import com.manish.bazingalnmiit.navigationhome;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] images;
    LinearLayout sliderdots;
    private int dotscount;
    private ImageView[] dots;
    private String[] BannerName;
    private String[] FoodId;
    navigationhome home;

    private GestureDetector gestureDetector;


    public ViewPagerAdapter(Context context, Integer[] images, String[] bannerName, String[] foodId) {
        this.context = context;
        this.images = images;
        BannerName = bannerName;
        FoodId = foodId;
    }

    @Override
    public int getCount() {

        if(images!=null&&images.length>0) {
            return images.length*(1000);
        }
        else
        {
            return 1;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        position=(position%images.length);
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        gestureDetector = new GestureDetector(context, new SingleTapConfirm());
        View view=layoutInflater.inflate(R.layout.bannerlayout,null);
        ImageView bannerimage=(ImageView)view.findViewById(R.id.bannerimage);
        bannerimage.setImageResource(images[position]);
        //bannerimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        TextView bannerName=(TextView)view.findViewById(R.id.bannername);
        bannerName.setText(BannerName[position]);
        sliderdots=(LinearLayout)view.findViewById(R.id.dots);
        dotscount=images.length;
        dots=new ImageView[dotscount];

        final int finalPosition = position;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Commondata.isConnectedToInternet(context)) {
                    Intent intent = new Intent(context, BannerFood.class);
                    intent.putExtra("FoodId", FoodId[finalPosition]);
                    context.startActivity(intent);
                }
                else
                {
                    Toast.makeText(context, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });


        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                    int action = event.getActionMasked();
                    //Log.i("xxxx12", action + "  ");
                    switch (action) {

                        case MotionEvent.ACTION_BUTTON_PRESS:
                           // Log.d("xxxx12", "pressed");
                            break;

                            case MotionEvent.ACTION_SCROLL:
                                //Log.d("xxxx12", "scrooll");
                                break;

                        case MotionEvent.ACTION_MOVE:
                            home = (navigationhome) context;
                            //Log.d("xxxx12", "moved");
                            home.timer.cancel();
                            home.reScheduleTimer(4000, 2000);
                            break;

                        case MotionEvent.ACTION_DOWN:
                            home = (navigationhome) context;
                            //Log.d("xxxx12", "moved");
                            home.timer.cancel();
                            home.reScheduleTimer(4000, 2000);
                            break;
                    }

                    return false;
                }
        });


        for (int i=0;i<dotscount;i++)
        {
            dots[i]=new ImageView(context);

            if(i!=position) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.nonactive_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8, 7, 8, 0);

                sliderdots.addView(dots[i], params);
            }
            else
            {
                dots[position].setImageDrawable(ContextCompat.getDrawable(context,R.drawable.active_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8, 0, 8, 0);
                sliderdots.addView(dots[position], params);

            }
        }




        ViewPager vp=(ViewPager) container;

        vp.addView(view,0);

        return  view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp=(ViewPager)container;
        View view=(View)object;
        vp.removeView(view);
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return true;

        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return true;
        }
    }
}
