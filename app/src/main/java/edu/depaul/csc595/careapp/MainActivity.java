package edu.depaul.csc595.careapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.depaul.csc595.careapp.Helpers.FacebookUserInfo;
import edu.depaul.csc595.careapp.Helpers.FacebookUserProfileInfo;
import edu.depaul.csc595.careapp.ListData.Card;
import edu.depaul.csc595.careapp.ListData.MaintenanceList;
import edu.depaul.csc595.careapp.main_fragments.CardListAdapter;
import edu.depaul.csc595.careapp.main_fragments.GamesFragment;
import edu.depaul.csc595.careapp.main_fragments.MyCarFragment;
import edu.depaul.csc595.careapp.main_fragments.ProfileFragment;
import edu.depaul.csc595.careapp.main_fragments.RewardsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    MenuItem facelogout;

    private FloatingActionButton fbLeft;
    private FloatingActionButton fbCenter;
    private FloatingActionButton fbRight;
    private FloatingActionButton addMaintenence;

    private SparseArray<View.OnClickListener> floatingActionButtonOnClickListeners;

    public static String names [];
    public static String ids [];

    static final int UNKNOWN_DRIVER_RIDE = 1;
    static final int ADD_MAINTENANCE_REMINDER = 2;

    // Gambiarra
    public static MaintenanceList maintenanceList;

    public static FacebookUserInfo f = new FacebookUserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gambiarra
        maintenanceList = new MaintenanceList();

        //Toolbar support code
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_icons_toolbar);
        setSupportActionBar(toolbar);

        fbLeft = (FloatingActionButton) findViewById(R.id.buttonLeft);
        fbCenter = (FloatingActionButton) findViewById(R.id.buttonCenter);
        fbRight = (FloatingActionButton) findViewById(R.id.buttonRight);
        addMaintenence = (FloatingActionButton) findViewById(R.id.buttonAddMaintenence);

        addMaintenence.hide();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView profileName = (TextView) header.findViewById(R.id.username);
        CircleImageView profilePicture = (CircleImageView) header.findViewById(R.id.imgRoundedRight);

        //Facebook Information \o/
        try
        {
            f = new FacebookUserProfileInfo(getApplicationContext(), AccessToken.getCurrentAccessToken()).execute().get();

            profileName.setText(f.getUserName());
            profilePicture.setImageBitmap(f.getUserPicture());

        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        //Sidebar Menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        facelogout = (MenuItem) findViewById(R.id.face_logout);

        // TabView Elements
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            if (extras.getBoolean("EXTRA_CHALLENGED"))
            {
                Snackbar.make(viewPager, "You have challenged " + extras.getString("EXTRA_FRIEND_NAME") + " successfully.", Snackbar.LENGTH_LONG).show();
            }

        }


        addMaintenence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MaintenanceActivity.class);
                startActivityForResult(intent, ADD_MAINTENANCE_REMINDER);
            }
        });

        fbLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        fbCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RideActivity.class);
                startActivity(intent);
            }
        });

        fbRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FriendChallenge.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == ADD_MAINTENANCE_REMINDER)
        {
            if (resultCode == RESULT_OK)
            {
                ListView lv = (ListView) findViewById(R.id.maintenance_list);
                CardListAdapter adapter = (CardListAdapter) lv.getAdapter();
                adapter.cardList.addItem(new Card(Card.Type.type_8,
                        data.getIntExtra("Icon", -1),
                        data.getStringExtra("ContentTitle"),
                        data.getStringExtra("Line1")));
                adapter.notifyDataSetChanged();
            }
        }
    }


    //region SIDEMENU
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here. bunda
        int id = item.getItemId();

        if (id == R.id.face_logout) {
            // TODO: Acrescentar FacebookLogoutActivity ou Ação de logout.
            //Toast.makeText(MainActivity.this, "Replace with your own action", Toast.LENGTH_SHORT).show();
            LoginManager.getInstance().logOut();

            SharedPreferences settings = getSharedPreferences(TabbedScreen.FACEBOOK_PREFS, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("loginSucessful", true);
            editor.commit();


            Intent intent = new Intent();
            intent.setClass(MainActivity.this, TabbedScreen.class);
            intent.putExtra("REQ", 1);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //endregion

    //region TAB_VIEW
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.change_mycare_icon_status);
        tabLayout.getTabAt(1).setIcon(R.drawable.change_playcare_icon_status);
        tabLayout.getTabAt(2).setIcon(R.drawable.change_takecare_status);
        tabLayout.getTabAt(3).setIcon(R.drawable.change_carerewards_status);
    }

    /**
     * Adding fragments to ViewPager
     * @param viewPager
     */

    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ProfileFragment(), "PROFILE");
        adapter.addFrag(new GamesFragment(), "GAME");
        adapter.addFrag(new MyCarFragment(), "MY CAR");
        adapter.addFrag(new RewardsFragment(), "REWARDS");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(position == 0) {
                    fbLeft.show();
                    fbCenter.show();
                    fbRight.show();
                }
                else{
                    fbLeft.hide();
                    fbCenter.hide();
                    fbRight.hide();
                }


                if(position == 2) {
                    addMaintenence.show();
                }
                else{
                    addMaintenence.hide();
                }

                switch (position){
                    case 0: setTitle("My Care");
                            break;
                    case 1: setTitle("Play Care");
                        break;
                    case 2: setTitle("Take Care");
                        break;
                    case 3: setTitle("Care Rewards");
                        break;
                    default: setTitle("");
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                viewPager.getCurrentItem();

                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        if(viewPager.getCurrentItem() != 0) {
                            fbLeft.hide();
                            fbCenter.hide();
                            fbRight.hide();
                        }
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if(viewPager.getCurrentItem() == 0) {
                            fbLeft.show();
                            fbCenter.show();
                            fbRight.show();
                        }
                }
            }
        });
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) { return null; }
    }
    //endregion
}
