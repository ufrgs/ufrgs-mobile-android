/*
 * Copyright 2016 Universidade Federal do Rio Grande do Sul
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cpd.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cpd.fragments.LibraryRootFragment;
import com.cpd.fragments.NewsFragment;
import com.cpd.fragments.RuFragment;
import com.cpd.ufrgsmobile.R;

/**
 * Adapter that holds the information about the tabs to be displayed on the MainActivity.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = ViewPagerAdapter.class.getSimpleName();

    private int mTabIcon[] = {
            R.drawable.tab_drawable_news,
            R.drawable.tab_drawable_library,
            R.drawable.tab_drawable_restaurant
    };

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Returns the fragment for the every position in the View Pager
     * @param position Page position
     * @return The fragment required in that position
     */
    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 0:
                return new NewsFragment();
            case 1:
                return new LibraryRootFragment();
            case 2:
                return new RuFragment();
        }

        return new NewsFragment();

    }

    /**
     * Returns the title of the page
     */
    @Override
    public CharSequence getPageTitle(int position) {
        // We use icons instead of titles
        return null;
    }


    /**
     * Gets the number of tabs to be placed
     */
    @Override
    public int getCount() {
        return mTabIcon.length;
    }

    @Override
    public int getItemPosition(Object object) {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
        return POSITION_NONE;
    }

    /**
     * Gets the icon reference in that specific position
     */
    public int getIconAt(int pos){
        if(pos >= 0 && pos < mTabIcon.length){
            return mTabIcon[pos];
        }

        throw new IndexOutOfBoundsException("No icon for this position");
    }

}
