package id.go.dinkes.mobileedisponew.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.go.dinkes.mobileedisponew.surat.BelumDiProsesFragment
import id.go.dinkes.mobileedisponew.surat.SudahDiProsesFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */

private  const val NUM_TABS = 2
class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
       return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position){
            0 -> return SudahDiProsesFragment()
            1 -> return BelumDiProsesFragment()
        }
        return SudahDiProsesFragment()
    }
}