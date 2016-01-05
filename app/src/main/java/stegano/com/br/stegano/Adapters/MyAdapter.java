package stegano.com.br.stegano.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import stegano.com.br.stegano.Codificar.Tab_Imagem;
import stegano.com.br.stegano.Codificar.Tab_revisao;
import stegano.com.br.stegano.Codificar.Tab_texto;

/**
 * Created by Jeferson on 21/12/2015.
 */
public class MyAdapter extends FragmentPagerAdapter {

    private String[] mTabTitles = {"Imagem","Texto","Revisão"};
    private Context mContext;

    public MyAdapter(FragmentManager fm, Context c) {
        super(fm);
        mContext = c;
        //this.mTabTitles = mTabTitles;
    }
    @Override
    public Fragment getItem(int position) {

        Fragment frag = null;

        if (position == 0)
            frag = new Tab_Imagem();
        else if (position == 1)
            frag = new Tab_texto();
        else if (position == 2)
            frag = new Tab_revisao();

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
