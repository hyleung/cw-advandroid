/***
  Copyright (c) 2008-2011 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Advanced Android Development_
    http://commonsware.com/AdvAndroid
*/

package com.commonsware.android.feedfrags;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import org.mcsoxford.rss.RSSItem;

public class FeedsTabActivity extends AbstractFeedsActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_nav);
    
    for (final Feed feed : Feed.getFeeds()) {
      addNewFeed(feed);
    }
    
    ActionBar bar=getActionBar();
    
    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
    bar.setDisplayHomeAsUpEnabled(true);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    new MenuInflater(this).inflate(R.menu.feeds_nav_options, menu);

    return(super.onCreateOptionsMenu(menu));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
    
        return(true);
    }
    
    return(super.onOptionsItemSelected(item));
  }
  
  private void removeFragments(FragmentManager fragMgr,
                               FragmentTransaction xaction) {
    ItemsFragment items=(ItemsFragment)fragMgr.findFragmentById(R.id.second_pane);
      
    if (items!=null) {
      xaction.remove(items);
      
      ContentFragment content=
        (ContentFragment)fragMgr.findFragmentById(R.id.third_pane);
        
      if (content!=null && !content.isRemoving()) {
        xaction.remove(content);
        fragMgr.popBackStack();
      }
    }
  }
  
  public void addNewFeed(final Feed feed) {
    ActionBar bar=getActionBar();
    
    bar.addTab(bar
                .newTab()
                .setText(feed.toString())
                .setTabListener(new TabListener(feed)));
  }
  
  private class TabListener implements ActionBar.TabListener {
    Feed feed=null;
    
    TabListener(Feed feed) {
      this.feed=feed;
    }
    
    public void onTabSelected(ActionBar.Tab tab,
                              android.app.FragmentTransaction unused) {
      FragmentManager fragMgr=getSupportFragmentManager();
      FragmentTransaction xaction=fragMgr.beginTransaction();
      
      addItems(xaction, feed);
      xaction.commit();
    }

    public void onTabUnselected(ActionBar.Tab tab,
                                android.app.FragmentTransaction unused) {
      FragmentManager fragMgr=getSupportFragmentManager();
      FragmentTransaction xaction=fragMgr.beginTransaction();
      
      removeFragments(fragMgr, xaction);
      xaction.commit();
    }

    public void onTabReselected(ActionBar.Tab tab,
                                android.app.FragmentTransaction xaction) {
      // NO-OP
    }
  }
}