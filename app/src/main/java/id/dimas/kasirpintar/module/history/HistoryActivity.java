package id.dimas.kasirpintar.module.history;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import id.dimas.kasirpintar.R;

public class HistoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView cvBack;
    private TextView tvLeftTitle;
    private TextView tvRightTitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cvBack = (CardView) findViewById(R.id.cvBack);
        tvLeftTitle = (TextView) findViewById(R.id.tvLeftTitle);
        tvRightTitle = (TextView) findViewById(R.id.tvRightTitle);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        cvBack.setOnClickListener(v -> finish());
        tvLeftTitle.setText("Riwayat Transaksi");
        tvRightTitle.setVisibility(View.GONE);


        // Create an adapter that returns a fragment for each section
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Connect the ViewPager with the TabLayout
        tabLayout.setupWithViewPager(viewPager);
    }

    private static class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HistoryFinishFragment();
                case 1:
                    return new HistorySplitPaymentFragment();
                case 2:
                    return new HistoryNotPaymentFragment();
                case 3:
                    return new HistoryCancelFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4; // Number of tabs
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Set tab titles
            switch (position) {
                case 0:
                    return "Selesai";
                case 1:
                    return "Belum Lunas";
                case 2:
                    return "Belum Bayar";
                case 3:
                    return "Dibatalkan";
                default:
                    return null;
            }
        }
    }
}