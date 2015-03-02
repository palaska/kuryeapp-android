package kuryeapp.tabsswipe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mobile.kuryeapp.kuryeappv01.AddressFragment;
import com.mobile.kuryeapp.kuryeappv01.PaymentFragment;


public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabs = { "Adres Takibi", "Odeme al" };

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return new AddressFragment();
            case 1:
                return new PaymentFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
