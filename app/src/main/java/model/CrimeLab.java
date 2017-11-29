package model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class CrimeLab
{
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    public static CrimeLab get(Context context)
    {
        if (sCrimeLab == null)
        {
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }

    private CrimeLab(Context context)
    {
        mCrimes = new ArrayList<>();
    }

    public Crime getCrime(UUID id)
    {
        for (Crime crime : mCrimes)
        {
            if (crime.getId().equals(id))
            {
                return crime;
            }
        }

        return null;
    }

    public List<Crime> getCrimes()
    {
        return mCrimes;
    }

    public void addCrime(Crime crime)
    {
        mCrimes.add(crime);
    }

    public void deleteCrime(UUID id)
    {
        Iterator<Crime> iterator = mCrimes.iterator();

        while (iterator.hasNext())
        {
            Crime crime = iterator.next();
            if (crime.getId().equals(id))
            {
                iterator.remove();
            }
        }
    }
}
