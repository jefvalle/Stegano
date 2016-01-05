package stegano.com.br.stegano;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import stegano.com.br.stegano.Codificar.Codificar_tabs;
import stegano.com.br.stegano.Decodificar.Decodificar_tabs;
import stegano.com.br.stegano.Extras.Home;
import stegano.com.br.stegano.Extras.SlidingTabLayout;
import stegano.com.br.stegano.Extras.StegoData;

public class MainActivity extends AppCompatActivity{

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPage;

    private StegoData stegoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the Codificar_tabs as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new Home()).commit();


        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                if (menuItem.getItemId() == R.id.nav_item_decodificar) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, new Decodificar_tabs()).commit();
                    //getSupportActionBar().setTitle("Decodificar");
                }

                if (menuItem.getItemId() == R.id.nav_item_codificar) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new Codificar_tabs()).commit();
                }

                if (menuItem.getItemId() == R.id.nav_item_principal) {
                    FragmentTransaction yfragmentTransaction = mFragmentManager.beginTransaction();
                    yfragmentTransaction.replace(R.id.containerView, new Home()).commit();
                }


                return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name, R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }

    public void closeNavDrawer()
    {
        this.mDrawerLayout.closeDrawers();
    }
}