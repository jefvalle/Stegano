package stegano.com.br.stegano.Codificar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import stegano.com.br.stegano.Adapters.MyAdapter;
import stegano.com.br.stegano.Extras.SlidingTabLayout;
import stegano.com.br.stegano.R;

/**
 * Created by Ratan on 7/27/2015.
 */
public class Codificar_tabs extends Fragment {

    public static int int_items = 3 ;
    private View rootview;
    private CharSequence mTitle;
    DrawerLayout mDrawerLayout;

    private SlidingTabLayout mTabLayout;
    private ViewPager mviewPager;
    public String tempPath1 = "";
    public static String POSITION = "POSITION";

    Tab_Imagem tab_im;
    Tab_texto tab_text;
    Tab_revisao tab_rev;

    private FragmentPagerAdapter mAdapter;

    @Nullable
    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.tab_layout, container, false);

        mTabLayout = (SlidingTabLayout) rootview.findViewById(R.id.tabs);
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.white));
        mTabLayout.setDistributeEvenly(true);
        //mTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        mviewPager = (ViewPager) rootview.findViewById(R.id.viewpager);
        mviewPager.setOffscreenPageLimit(2);
        mAdapter = new MyAdapter(this.getChildFragmentManager(), this.getActivity());
        //mAdapter = new MyAdapter(getSupportFragmentManager(), this);
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

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Ciclo", "Fragment: Metodo onResume() chamado");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Ciclo", "Fragment: Metodo onPause() chamado");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Ciclo", "Fragment: Metodo onStop() chamado");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Ciclo", "Fragment: Metodo onDestroyView() chamado");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Ciclo", "Fragment: Metodo onDestroy() chamado");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Ciclo", "Fragment: Metodo onDetach() chamado");
    }
}