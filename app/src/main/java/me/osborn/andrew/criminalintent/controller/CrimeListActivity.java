package me.osborn.andrew.criminalintent.controller;

import android.content.Intent;
import android.support.v4.app.Fragment;

import me.osborn.andrew.criminalintent.R;
import model.Crime;

public class CrimeListActivity extends SingleFragmentActivity
    implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks
{
    @Override
    protected Fragment createFragment()
    {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime)
    {
        // Checks if the detail container exists -- whether or not two-pane layout is present
        if (findViewById(R.id.detail_fragment_container) == null)
        {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        }
        else
        {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }


    @Override
    public void onCrimeUpdated(Crime crime)
    {
        CrimeListFragment listFragment = (CrimeListFragment)
                getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
