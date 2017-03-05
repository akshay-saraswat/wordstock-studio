package inc.asharfi.wordstock;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.Pair;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	/**
	 * Per the design guidelines, you should show the drawer on launch until the
	 * user manually expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	private View mFragmentContainerView;

	private int mCurrentSelectedPosition = 1;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;
	private Runnable mPendingRunnable;
	private Dialog mBuilder;

	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Read in the flag indicating whether or not the user has demonstrated
		// awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, true);

		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState
					.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		}

		// Select either the default item (0) or the last selected item.
		selectItem(mCurrentSelectedPosition);
		if (mPendingRunnable != null) {
        	new Handler().post(mPendingRunnable);
            mPendingRunnable = null;
            //dismissSpinner();
        }
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mDrawerListView = (ListView) inflater.inflate(
				R.layout.fragment_navigation_drawer, container, false);

		mDrawerListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						selectItem(position);

					}
				});

		ArrayList<Pair<String, Integer>> classes = new ArrayList<Pair<String, Integer>>();
		classes.add(Pair.create(getString(R.string.app_name), R.drawable.wordstock_header2));
		classes.add(Pair.create(getString(R.string.title_section1), R.drawable.stock_on_icon));
		classes.add(Pair.create(getString(R.string.title_section2), R.drawable.flashcard_on_icon));
		classes.add(Pair.create(getString(R.string.title_section3), R.drawable.test_on_icon));
		classes.add(Pair.create(getString(R.string.title_section4), R.drawable.equivalence_on_icon));
		classes.add(Pair.create(getString(R.string.title_section5), R.drawable.stats_on_icon));
		classes.add(Pair.create(getString(R.string.title_section6), R.drawable.settings_on_icon));
		classes.add(Pair.create(getString(R.string.title_section7), R.drawable.announce));

		// add all items
		ArrayAdapter<Pair<String, Integer>> listadaptor = new ArrayAdapter<Pair<String, Integer>>(getActionBar().getThemedContext(),
				R.layout.nav_drawer_item, R.id.item_text, classes) {
				   public View getView(int position, View convertView, ViewGroup container) {
						if (position == 0) {
							Pair<String, Integer> item = getItem(position);
							convertView = inflater.inflate(R.layout.drawer_header, container, false);
							//convertView.setBackgroundResource(item.second);
							ImageView img = (ImageView)convertView.findViewById(R.id.headerImage);
							img.setBackgroundResource(item.second);
						} else {
							convertView = inflater.inflate(R.layout.nav_drawer_item, container, false);
							TextView title = (TextView) convertView.findViewById(R.id.item_text);
							Pair<String, Integer> item = getItem(position);
							title.setText(item.first);
							WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
							Display display = wm.getDefaultDisplay();
							Point size = new Point();
							display.getSize(size);
							//title.setShadowLayer(((3 * size.x) / 1080), ((1 * size.x) / 1080), ((2 * size.x) / 1080), Color.BLACK);
							ImageView image = (ImageView) convertView.findViewById(R.id.item_image);
							image.setBackgroundResource(item.second);
						}

						return convertView;
				    }

				   public boolean isEnabled(int position) {
					   if (position == 0)
						   return false;
					   else
						   return true;
					   
				   }
		 };

		mDrawerListView.setAdapter(listadaptor);
		mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
		return mDrawerListView;
	}
	
	public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.navigation_drawer_open, /*
										 * "open drawer" description for
										 * accessibility
										 */
		R.string.navigation_drawer_close /*
										 * "close drawer" description for
										 * accessibility
										 */
		) {
			@SuppressLint("NewApi")
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}

				getActionBar().show();
				//setMenuSelectorVisible(true);
                setMenuButtonVisible(true);
                if (mPendingRunnable != null) {
                	drawerView.post(mPendingRunnable);
                    mPendingRunnable = null;
                    //dismissSpinner();
                }
        			
				//getActivity().invalidateOptionsMenu();
			}

			@SuppressLint("NewApi")
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}

				getActionBar().hide();

				if (!mUserLearnedDrawer) {
					// The user manually opened the drawer; store this flag to
					// prevent auto-showing
					// the navigation drawer automatically in the future.
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
							.apply();
				}

				//setMenuSelectorVisible(false);
                setMenuButtonVisible(false);
                //getActivity().invalidateOptionsMenu();
			}
		};

		// If the user hasn't 'learned' about the drawer, open it to introduce
		// them to the drawer,
		// per the navigation drawer design guidelines.
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void dismissSpinner() {
		if (mBuilder != null) {
			mBuilder.dismiss();
			mBuilder = null;
		}
		System.gc();
	}

	private void showSpinner() {
		mBuilder = new Dialog(getActivity());
		mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mBuilder.getWindow().setGravity(Gravity.CENTER);
		mBuilder.getWindow().setBackgroundDrawable(
	        new ColorDrawable(android.graphics.Color.TRANSPARENT));
		//mBuilder.setCancelable(false);
		mBuilder.setCanceledOnTouchOutside(false);
		mBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
	        @Override
	        public void onDismiss(DialogInterface dialogInterface) {
	            //nothing;
	        }
	    });

	    ProgressBar pb = new ProgressBar(getActivity());
	    pb.setIndeterminateDrawable(getResources().getDrawable(R.drawable.my_spinner));
	    //pb.setInterpolator(getActivity(), android.R.anim.bounce_interpolator);
	    pb.setIndeterminate(true);
	    mBuilder.addContentView(pb, new RelativeLayout.LayoutParams(
	            ViewGroup.LayoutParams.MATCH_PARENT, 
	            ViewGroup.LayoutParams.MATCH_PARENT));
	    mBuilder.show();
	}

	private void selectItem(final int position) {
		mCurrentSelectedPosition = position;
		if (mDrawerListView != null) {
			mDrawerListView.setItemChecked(position, true);
		}

		//showSpinner();

		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}

		mPendingRunnable = new Runnable() {
	        @Override
	        public void run() {
	            // update the main content by replacing fragments
	        	if (mCallbacks != null) {
	    			mCallbacks.onNavigationDrawerItemSelected(position);
	    		}
	        }
	    };
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	public void reInit () {
		if (((MainActivity) this.getActivity()).getFlag("night_mode"))
			mDrawerListView.setBackgroundColor(Color.argb(255, 51, 51, 51));
		else
			mDrawerListView.setBackgroundColor(Color.argb(0xFF, 0xF0, 0xF0, 0xF0));
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar.
		// See also
		// showGlobalContextActionBar, which controls the top-left area of the
		// action bar.
		if (mDrawerLayout != null && isDrawerOpen()) {
			inflater.inflate(R.menu.global, menu);
			showGlobalContextActionBar();
		}

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		/*if (item.getItemId() == R.id.action_example) {
			//Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
			return true;
		}*/

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to
	 * show the global app 'context', rather than just what's in the current
	 * screen.
	 */
	@SuppressWarnings("deprecation")
	private void showGlobalContextActionBar() {
		Log.d("Akshay", "showGlobalContextActionBar");
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		//actionBar.setTitle(R.string.app_name);
	}

	private ActionBar getActionBar() {
		return getActivity().getActionBar();
	}

    /* Be careful in calling these functions, menu items need to go back to normal
     * before drawer is opened or closed again
     */
    public void setMenuButton(String title){
		//mMenu.findItem(R.id.action_example).setTitle(title);
    }

    public void setMenuButtonClickable(boolean clickable){
        //mMenu.findItem(R.id.action_example).setEnabled(clickable);
    }

    public void setMenuButtonVisible(boolean visibility){
        //mMenu.findItem(R.id.action_example).setVisible(visibility);
    }

    public void setMenuSelectorVisible(boolean visibility){
        //mMenu.findItem(R.id.action_home).setVisible(visibility);
    }

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position);
	}
}
