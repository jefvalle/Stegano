package stegano.com.br.stegano.Decodificar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import stegano.com.br.stegano.Adapters.MyAdapter_decod;
import stegano.com.br.stegano.Extras.SlidingTabLayout;
import stegano.com.br.stegano.R;

/**
 * Created by Jeferson on 22/12/2015.
 */

public class Decodificar_tabs extends Fragment{

    private View rootview;
    private CharSequence mTitle;

    private SlidingTabLayout mTabLayout;
    private ViewPager mviewPager;
    public String tempPath1 = "";

    private FragmentPagerAdapter mAdapter;

    @Nullable
    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.tab_layout_decod, container, false);

        mTabLayout = (SlidingTabLayout) rootview.findViewById(R.id.tabs_decod);
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.white));
        mTabLayout.setDistributeEvenly(true);


        mviewPager = (ViewPager) rootview.findViewById(R.id.viewpager_decod);
        mviewPager.setOffscreenPageLimit(1);
        mAdapter = new MyAdapter_decod(this.getChildFragmentManager(), this.getActivity());

        mviewPager.setAdapter(mAdapter);

        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                mTabLayout.setViewPager(mviewPager);
            }
        });

        return rootview;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragments = getChildFragmentManager().getFragments();

        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
                Log.i("in activity result", "");
            }
        }
    }

    public void openFragment()
    {
        Fragment fr = new Fragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.linear_layout_decod, fr);
        fragmentTransaction.commit();
    }

    public void onClick(View v) {
        Tab_Image_decod fragment1 = new Tab_Image_decod();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment1);
        fragmentTransaction.commit();
    }
}