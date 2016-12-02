package org.literacyapp;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.literacyapp.content.letter.LettersActivity;
import org.literacyapp.content.multimedia.video.VideosActivity;
import org.literacyapp.content.number.NumbersActivity;
import org.literacyapp.util.MediaPlayerHelper;

/**
 * Activity for selecting between literacy, numeracy, and videos.
 */
public class CategoryActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    ImageView indicator0;
    ImageView indicator1;
    ImageView indicator2;
    ImageView[] indicators;

    int lastLeftValue = 0;

    CoordinatorLayout mCoordinator;

    int pagePosition = 0;

    static View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);


        // Create the adapter that will return a fragment for each of the
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        indicator0 = (ImageView) findViewById(R.id.intro_indicator_0);
        indicator1 = (ImageView) findViewById(R.id.intro_indicator_1);
        indicator2 = (ImageView) findViewById(R.id.intro_indicator_2);

        mCoordinator = (CoordinatorLayout) findViewById(R.id.main_content);


        indicators = new ImageView[]{
                indicator0,
                indicator1,
                indicator2
        };

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.categoryContainer);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(pagePosition);
        updateIndicators(pagePosition);

        final int color1 = ContextCompat.getColor(this, R.color.green);
        final int color2 = ContextCompat.getColor(this, R.color.blue);
        final int color3 = ContextCompat.getColor(this, R.color.purple);

        final int[] colorList = new int[]{color1, color2, color3};

        final ArgbEvaluator evaluator = new ArgbEvaluator();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d(getClass(), "onPageScrolled");

                /*
                color update
                 */
                int colorUpdate = (Integer) evaluator.evaluate(positionOffset, colorList[position], colorList[position == (indicators.length - 1) ? position : position + 1]);
                mViewPager.setBackgroundColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(getClass().getName(), "onPageSelected: " + position);

                pagePosition = position;

                updateIndicators(pagePosition);

                switch (position) {
                    case 0:
                        mViewPager.setBackgroundColor(color1);
                        break;
                    case 1:
                        mViewPager.setBackgroundColor(color2);
                        break;
                    case 2:
                        mViewPager.setBackgroundColor(color3);
                        break;
                }

                animateImage((ImageView) findViewById(R.id.section_img));

                MediaPlayerHelper.play(getApplicationContext(), R.raw.vocal_plop);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i(getClass().getName(), "onPageScrollStateChanged");
            }
        });
    }

    private void animateImage(final ImageView imageView) {
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Play a subtle animation
                final long duration = 300;

                final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, View.SCALE_X, 1f, 1.2f, 1f);
                scaleXAnimator.setDuration(duration);
                scaleXAnimator.setRepeatCount(1);

                final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 1f, 1.2f, 1f);
                scaleYAnimator.setDuration(duration);
                scaleYAnimator.setRepeatCount(1);

                scaleXAnimator.start();
                scaleYAnimator.start();

                final AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(scaleXAnimator).with(scaleYAnimator);
                animatorSet.start();
            }
        }, 400);
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();
    }

    void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        ImageView img;

        int[] bgs = new int[]{
                R.drawable.gesture,
                R.drawable.ic_grain_black_24dp,
                R.drawable.ic_play_circle_outline_black_24dp
        };

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_category, container, false);

            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            Log.i(getClass().getName(), "sectionNumber: " + sectionNumber);

            img = (ImageView) rootView.findViewById(R.id.section_img);
            img.setBackgroundResource(bgs[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);

            if (sectionNumber == 1) {
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "onClick");
                        Intent intent = new Intent(getActivity(), LettersActivity.class);
                        startActivity(intent);
                    }
                });
            } else if (sectionNumber == 2) {
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "onClick");
                        Intent intent = new Intent(getActivity(), NumbersActivity.class);
                        startActivity(intent);
                    }
                });
            } else if (sectionNumber == 3) {
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(getClass().getName(), "onClick");
                        Intent intent = new Intent(getActivity(), VideosActivity.class);
                        startActivity(intent);
                    }
                });
            }

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * indicator1 of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given pagePosition.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);

        }

        @Override
        public int getCount() {
            // Total pages
            return indicators.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
