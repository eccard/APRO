package recode.appro.telas;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentDepartamentoMain extends Fragment {

	DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
	ViewPager mViewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_departamento_main, container,
				false);
		
		mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(
				getFragmentManager());
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager_departamento);
		mViewPager.setAdapter(mDemoCollectionPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {

						getActivity().getActionBar().setSelectedNavigationItem(
								position);
					}
				});
		
		getActivity().getActionBar().setSubtitle("Departamento");
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabSelected(Tab tab,
					android.app.FragmentTransaction ft) {

				mViewPager.setCurrentItem(tab.getPosition());

			}

			@Override
			public void onTabUnselected(Tab tab,
					android.app.FragmentTransaction ft) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTabReselected(Tab tab,
					android.app.FragmentTransaction ft) {
				// TODO Auto-generated method stub

			}
		};

		getActivity().getActionBar().addTab(
				getActivity().getActionBar().newTab().setText("Informações")
						.setTabListener(tabListener));
		getActivity().getActionBar().addTab(
				getActivity().getActionBar().newTab().setText("Professores")
						.setTabListener(tabListener));
		
		return view;
	}

	// Since this is an object collection, use a FragmentStatePagerAdapter,
	// and NOT a FragmentPagerAdapter.
	public class DemoCollectionPagerAdapter extends FragmentPagerAdapter {
		public DemoCollectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {

			Fragment fragment = null;

			switch (i) {

			case 0:
				fragment = new FragmentDepartamentoInfo();
				break;

			case 1:
				fragment = new FragmentDepartamentoProfessores();
				break;

			default:
				break;
			}

			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "OBJECT " + (position + 1);
		}
	}
}
