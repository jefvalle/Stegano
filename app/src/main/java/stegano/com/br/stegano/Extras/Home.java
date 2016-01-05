package stegano.com.br.stegano.Extras;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import stegano.com.br.stegano.R;

/**
 * Created by Jeferson on 04/01/2016.
 */
public class Home extends Fragment {

    private View rootview;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.home,null);

        return rootview;
    }
}
