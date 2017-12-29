package me.osborn.andrew.criminalintent.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

import me.osborn.andrew.criminalintent.R;
import model.Crime;
import model.CrimeLab;

public class CrimePagerActivity extends AppCompatActivity
    implements CrimeFragment.Callbacks
{
    private static final String EXTRA_CRIME_ID =
            "me.osborn.andrew.criminalintent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context packageContext, UUID crimeId)
    {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);

        return intent;
    }

    @Override
    public Intent getParentActivityIntent()
    {
        Intent upIntent = super.getParentActivityIntent();
        upIntent.putExtra(CrimeListFragment.EXTRA_SUBTITLE_VISIBLE,
                getIntent().getBooleanExtra(CrimeListFragment.EXTRA_SUBTITLE_VISIBLE, false));
        return upIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager)
        {
            @Override
            public Fragment getItem(int position)
            {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount()
            {
                return mCrimes.size();
            }
        });

        for (int i = 0; i < mCrimes.size(); i++)
        {
            if (mCrimes.get(i).getId().equals(crimeId))
            {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    // Must be implemented in all activities that host CrimeFragment
    @Override
    public void onCrimeUpdated(Crime crime)
    {

    }
}
