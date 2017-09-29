package com.example.jh.guokrchoice.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import com.example.jh.guokrchoice.R;
import com.example.jh.guokrchoice.adapter.ReaderFragmentAdapter;
import com.example.jh.guokrchoice.ui.fragment.ArticleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dazz on 2016/4/2.
 */
public class ContainerActivity extends AppCompatActivity {

    ViewPager mViewPager;
    int ids[];
    int defaultPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ids = getIntent().getIntArrayExtra("ids");
        defaultPage = getIntent().getIntExtra("default", defaultPage);
        setContentView(R.layout.activity_article);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        initViewPage();
    }

    private void initViewPage() {
        List<Fragment> fragments = new ArrayList<>();
        for (int id : ids) {
            Fragment fragment = new ArticleFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);

            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        mViewPager.setAdapter(new ReaderFragmentAdapter(getSupportFragmentManager(), fragments));
        mViewPager.setCurrentItem(defaultPage, false);
    }
}
