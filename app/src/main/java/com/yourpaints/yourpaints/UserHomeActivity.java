package com.yourpaints.yourpaints;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.yourpaints.yourpaints.fragments.EventsFragment;
import com.yourpaints.yourpaints.fragments.HomeFragment;
import com.yourpaints.yourpaints.fragments.MessagesFragment;
import com.yourpaints.yourpaints.fragments.RequestsFragment;
import com.yourpaints.yourpaints.fragments.UserProfileFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserHomeActivity extends AppCompatActivity {

    @BindView(R.id.container)
    ViewPager mViewPager;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setUpViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_requests);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_event);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_message);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_person);

        tabLayout.getTabAt(0).setText("").setTag("Home");
        tabLayout.getTabAt(1).setText("").setTag("Requests");
        tabLayout.getTabAt(2).setTag("Event").setText("");
        tabLayout.getTabAt(3).setText("").setTag("Messages");
        tabLayout.getTabAt(4).setText("").setTag("Profile");

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    private void setUpViewPager(ViewPager mViewPager) {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(HomeFragment.newInstance(), "Home");
        sectionsPagerAdapter.addFragment(RequestsFragment.newInstance(), "Requests");
        sectionsPagerAdapter.addFragment(EventsFragment.newInstance(), "Event");
        sectionsPagerAdapter.addFragment(MessagesFragment.newInstance(), "Messages");
        sectionsPagerAdapter.addFragment(UserProfileFragment.newInstance(), "Profile");

        mViewPager.setAdapter(sectionsPagerAdapter);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserHomeActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "User logged out.", Toast.LENGTH_LONG).show();
                finish();
                return true;
            case R.id.action_feedback:
                Intent intent1 = new Intent(Intent.ACTION_SENDTO);
                intent1.setData(Uri.parse("mailto:sharma.pranjul1998@gmail.com"));
                intent1.putExtra(Intent.EXTRA_SUBJECT, "feedback mail from 'username' for Your Paints Android App.");                intent1.putExtra(Intent.EXTRA_TEXT, "Message for the feedback will go here.");
                startActivity(intent1);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
