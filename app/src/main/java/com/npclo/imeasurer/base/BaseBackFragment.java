package com.npclo.imeasurer.base;

import android.support.v7.widget.Toolbar;

import com.npclo.imeasurer.R;

import me.yokeyword.fragmentation.SupportFragment;


public class BaseBackFragment extends SupportFragment {
    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.mipmap.left);
        toolbar.setNavigationOnClickListener(__ -> pop());
    }
}