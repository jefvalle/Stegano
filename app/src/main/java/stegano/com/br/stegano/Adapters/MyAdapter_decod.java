package stegano.com.br.stegano.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import stegano.com.br.stegano.Decodificar.Tab_Image_decod;
import stegano.com.br.stegano.Decodificar.Tab_texto_decod;

/**
 * Created by Jeferson
 */
public class MyAdapter_decod extends FragmentPagerAdapter {
    private String[] mTabTitles = {"Imagem","Texto"};
    private Context mContext;

    public MyAdapter_decod(FragmentManager fm, Context c) {
        super(fm);
        mContext = c;
        //this.mTabTitles = mTabTitles;
    }
    @Override
    public Fragment getItem(int position) {

        Fragment frag = null;

        if (position == 0)
            frag = new Tab_Image_decod();
        else if (position == 1){
            frag = new Tab_texto_decod();
        }

        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);

        return frag;
    }
    @Override
    public int getCount() {
        return this.mTabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return this.mTabTitles[position];
    }
}