package com.wiser.carmodeldemo;

import android.content.Intent;

import com.wiser.library.base.WISERActivity;
import com.wiser.library.base.WISERBuilder;

public class MainActivity extends WISERActivity {

    @Override
    protected WISERBuilder build(WISERBuilder builder) {
        builder.layoutId(R.layout.activity_main);
        return builder;
    }

    @Override
    protected void initData(Intent intent) {

    }

}
