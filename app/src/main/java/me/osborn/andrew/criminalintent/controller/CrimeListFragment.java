package me.osborn.andrew.criminalintent.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import me.osborn.andrew.criminalintent.R;
import model.Crime;
import model.CrimeLab;

public class CrimeListFragment extends Fragment
{
    private static final int REQUEST_CRIME = 1;
    private static final String SELECTED_CRIME_POSITION =
            "selected_crime_position";
    public static final String EXTRA_SUBTITLE_VISIBLE =
            "me.osborn.andrew.criminalintent.subtitle_visible";
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int selectedCrimePosition;
    private boolean mSubtitleVisible;
    private TextView mAddCrimeHintTextView;
    private ImageButton mAddCrimeImageButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getActivity().getIntent().getExtras() != null)
        {
            mSubtitleVisible = getActivity().getIntent()
                    .getBooleanExtra(EXTRA_SUBTITLE_VISIBLE, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        if (savedInstanceState != null)
        {
            selectedCrimePosition = savedInstanceState.getInt(SELECTED_CRIME_POSITION);
        }

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAddCrimeHintTextView = (TextView) view.findViewById(R.id.crime_empty_list_hint);

        mAddCrimeImageButton = (ImageButton) view.findViewById(R.id.crime_list_add);
        mAddCrimeImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addCrime();
            }
        });

        updateUI();

        if (savedInstanceState != null)
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);

        return view;
    }

    private void updateUI()
    {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (crimes.size() > 0)
        {
            mAddCrimeHintTextView.setVisibility(View.INVISIBLE);
            mAddCrimeImageButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            mAddCrimeHintTextView.setVisibility(View.VISIBLE);
            mAddCrimeImageButton.setVisibility(View.VISIBLE);
        }

        if (mAdapter == null)
        {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else
        {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener
    {
        private Crime mCrime;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox)
                    itemView.findViewById(R.id.list_item_crime_solved_check_box);
            mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean solved)
                {
                    mCrime.setSolved(solved);
                }
            });
        }

        public void bindCrime(Crime crime)
        {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View view)
        {
            selectedCrimePosition = getAdapterPosition();
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            intent.putExtra(EXTRA_SUBTITLE_VISIBLE, mSubtitleVisible);
            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>
    {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes)
        {
            mCrimes = crimes;
        }

        // Called by RecyclerView when it needs a new View to display an item
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_crime, parent, false);

            return new CrimeHolder(view);
        }


        // Binds a ViewHolder's View to model object
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position)
        {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount()
        {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes)
        {
            mCrimes = crimes;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CRIME)
        {
            // Handle result
        }
    }

    public void returnResult()
    {
        getActivity().setResult(Activity.RESULT_OK, null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SELECTED_CRIME_POSITION, selectedCrimePosition);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible)
            subtitleItem.setTitle(R.string.hide_subtitle);
        else
            subtitleItem.setTitle(R.string.show_subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_item_new_crime:
                addCrime();
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addCrime()
    {
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).addCrime(crime);
        Intent intent = CrimePagerActivity
                .newIntent(getActivity(), crime.getId());
        startActivity(intent);
    }

    private void updateSubtitle()
    {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeSize = crimeLab.getCrimes().size();
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, crimeSize, crimeSize);

        if (!mSubtitleVisible)
            subtitle = null;

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }
}
