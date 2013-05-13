package ru.romanov.schedule.adapters;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nikita
 * Date: 13.05.13
 * Time: 22:01
 * To change this template use File | Settings | File Templates.
 */
public class ScheduleListPagerAdapter extends PagerAdapter {

    public List<View> pages = null;

    public ScheduleListPagerAdapter(List<View> pages) {
        super();
        this.pages = pages;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        super.instantiateItem(container, position);
        View v = pages.get(position);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        container.removeViewAt(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return false;
    }

    @Override
    public Parcelable saveState() {
        return super.saveState();
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }
}
