package inc.asharfi.wordstock;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;

class Tile {
	private int tile_drawable;
	private String tile_name;
	Tile (String tile_name, int tile_drawable) {
		this.tile_drawable = tile_drawable;
		this.tile_name = tile_name;
	}
	
	int getDrawable() {
		return tile_drawable;
	}

	String getName() {
		return tile_name;
	}
}

public class MainActivity extends FragmentActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks, TextToSpeech.OnInitListener {
	
	private AssessmentFragment assessmentFragment = null;
	private PlaceholderFragment placeholderFragment = null;
	private SettingsFragment settingsFragment = null;
	private int lastSectionNumber = 1;
	private TextToSpeech tts;
	private boolean isTTSEnabled;
	private Boolean exit = false;
	private MainActivity ma;
	final String PREFS_NAME = "MyPrefsFile";
	private Dialog mBuilder, rateBuilder;
	private final int textShadow = Color.DKGRAY;
	private ArrayList<Tile> wallpapers;
	private DatabaseHandler db;
	private TestDatabaseHandler testDB;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private static Menu mMenu = null;

	/**
	 * Used to store the last screen title
	 */
	private CharSequence mTitle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		wallpapers = new ArrayList<>();

		wallpapers.add(new Tile("Default", R.drawable.bg_default));
		wallpapers.add(new Tile("Autumn", R.drawable.bg_autumn));
		wallpapers.add(new Tile("Back to School", R.drawable.bg_bts));
		wallpapers.add(new Tile("Birds (Deviant Art)", R.drawable.bg_birds));
		wallpapers.add(new Tile("Brick Wall", R.drawable.bg_bricks));
		wallpapers.add(new Tile("Carboard Life (Tumblr)", R.drawable.bg_cardboard_life));
		wallpapers.add(new Tile("Cats (Deviant Art)", R.drawable.bg_cats));
		wallpapers.add(new Tile("Comic Book", R.drawable.bg_comic));
		wallpapers.add(new Tile("Crystals", R.drawable.bg_crystals));
		wallpapers.add(new Tile("Easter", R.drawable.bg_easter));
		wallpapers.add(new Tile("Dogs (Deviant Art)", R.drawable.bg_dogs));
		wallpapers.add(new Tile("Floral", R.drawable.bg_floral));
		wallpapers.add(new Tile("Grass", R.drawable.bg_grass));
		wallpapers.add(new Tile("Halloween (Tumblr)", R.drawable.bg_halloween));
		wallpapers.add(new Tile("Hearts", R.drawable.bg_hearts));
		wallpapers.add(new Tile("Inline", R.drawable.bg_inline));
		wallpapers.add(new Tile("Leaves", R.drawable.bg_leaves));
		wallpapers.add(new Tile("Lights", R.drawable.bg_lights));
		wallpapers.add(new Tile("Night Sky", R.drawable.bg_night_sky));
		wallpapers.add(new Tile("Scapegoat", R.drawable.bg_scapegoat));
		wallpapers.add(new Tile("Spacecat (Tumblr)", R.drawable.bg_spacecat));
		wallpapers.add(new Tile("Spring", R.drawable.bg_spring));
		wallpapers.add(new Tile("Watermelon (Tumblr)", R.drawable.bg_watermelon));
		wallpapers.add(new Tile("Wooden Wall", R.drawable.bg_wooden_wall));

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		/* Set up the drawer */
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		if (!getFlag("appInitialized")) {
		    /* the app is being launched for the first time on this device */

			copyDataBase();
			/* copyToons(); */
			/* fillDatabase(); */

		    /*
		     * record the fact that the app has been started at least once
			 * setFlag("dataUnpacked", true);
			 */
		    setFlag("appInitialized", true);
		    setFlag("ws_expand_definition", true);
		    setInt("wall_selected_index", 0);
		} /**
		    * else {
			* // these are initial (before second update) users
			* if (!getFlag("dataUnpacked")) {
			* 	copyToons();
			* 	setFlag("dataUnpacked", true);
			* }
		    * }
		 	*/

		if (!getFlag("didUserReviewApp")) {
			long notificationGap = (System.nanoTime() - getLong("lastnotificationTime")) / 1000000000;
			if (notificationGap < 0)
				setLong("lastnotificationTime", System.nanoTime());
			if (getInt("appLaunchCount") == 5 || notificationGap > (24 * 3600)) {
				setInt("appLaunchCount", 0);
				setLong("lastnotificationTime", System.nanoTime());
				rateMyApp();
			} else {
				setInt("appLaunchCount", getInt("appLaunchCount") + 1);
			}
		}
		tts = new TextToSpeech(this, this);
		ma = this;
	}

	private void rateMyApp () {
		rateBuilder = new Dialog(this);
		rateBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		rateBuilder.getWindow().setGravity(Gravity.CENTER);
		rateBuilder.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		rateBuilder.setCanceledOnTouchOutside(false);
		rateBuilder.setCancelable(false);
		rateBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				/* nothing */
			}
		});
		rateBuilder.setContentView(R.layout.rating_popup);

		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		/* set the custom dialog components - text, image and button */
		TextView text = (TextView) rateBuilder.findViewById(R.id.request);
		text.setText(Html.fromHtml("<b><center>Like Us ?</center></b><br><br>Appreciate our work.<br>Please rate us in the Play Store."));
		text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		text.setTextColor(Color.DKGRAY);
		text.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Regular.ttf"));
		text.setShadowLayer(((3 * size.x) / 1080), 1, 1, textShadow);

		final Context mContext = this.getApplicationContext();

		Button okButton = (Button) rateBuilder.findViewById(R.id.ok);
		okButton.setTextColor(Color.WHITE);
		/* if button is clicked, close the custom dialog */
		okButton.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				rateBuilder.dismiss();
				rateBuilder = null;
				Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
				Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
				/*
				 * To count with Play market BackStack, After pressing back button,
				 * to taken back to our application, we need to add following flags to intent.
				 */
				goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
						Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
						Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
				try {
					startActivity(goToMarket);
				} catch (ActivityNotFoundException e) {
					startActivity(new Intent(Intent.ACTION_VIEW,
							Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
				}
				setFlag("didUserReviewApp", true);
			}
		});

		Button laterButton = (Button) rateBuilder.findViewById(R.id.later);
		laterButton.setTextColor(Color.WHITE);
		/* if button is clicked, close the custom dialog */
		laterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rateBuilder.dismiss();
				rateBuilder = null;
			}
		});

		Button neverButton = (Button) rateBuilder.findViewById(R.id.never);
		neverButton.setTextColor(Color.WHITE);
		/* if button is clicked, close the custom dialog */
		neverButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setFlag("didUserReviewApp", true);
				rateBuilder.dismiss();
				rateBuilder = null;
			}
		});
		rateBuilder.show();
	}
	
	public ArrayList<Tile> getWallpapers() {
		return wallpapers;
	}

	public int getSectionNumber () {
		return lastSectionNumber;
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
    	final int sectionNumber = position;

    	if (placeholderFragment == null) {
			assessmentFragment = new AssessmentFragment();
			transaction
			.add(R.id.container, assessmentFragment, assessmentFragment.TAG)
			.setBreadCrumbShortTitle(assessmentFragment.TAG);

			settingsFragment = new SettingsFragment();
			transaction
			.add(R.id.container, settingsFragment, settingsFragment.TAG)
			.setBreadCrumbShortTitle(settingsFragment.TAG);

			placeholderFragment = new PlaceholderFragment();
			placeholderFragment.setData(0, assessmentFragment);
			transaction
			.add(R.id.container, placeholderFragment, placeholderFragment.TAG)
			.setBreadCrumbShortTitle(placeholderFragment.TAG);

			transaction.hide(assessmentFragment);
			transaction.hide(settingsFragment);
			transaction.commit();
    	} else {
			View v = mNavigationDrawerFragment.getView();
			if (v != null) {
				v.post(new Runnable() {
					@Override
					public void run() {
						if (transaction != null) {
							if (placeholderFragment != null && placeholderFragment.isVisible())
								transaction.hide(placeholderFragment);

							if (assessmentFragment != null && assessmentFragment.isVisible())
								transaction.hide(assessmentFragment);

							if (settingsFragment != null && settingsFragment.isVisible())
								transaction.hide(settingsFragment);

							switch (sectionNumber) {
								case 1:
									changeActionSearch(true);
									if (assessmentFragment != null && assessmentFragment.isWSAvailable()) {
										assessmentFragment.changeAdapter(sectionNumber);
										changeActionFinish(false, false);
										transaction.show(assessmentFragment);
									} else if (placeholderFragment != null) {
										placeholderFragment.changeAdapter(sectionNumber);
										transaction.show(placeholderFragment);
									}
									break;
								case 2:
									changeActionSearch(false);
									if (assessmentFragment != null && assessmentFragment.isFlashcardAvailable()) {
										assessmentFragment.changeAdapter(sectionNumber);
										transaction.show(assessmentFragment);
									} else if (placeholderFragment != null) {
										placeholderFragment.changeAdapter(sectionNumber);
										transaction.show(placeholderFragment);
									}
									break;
								case 3:
									changeActionSearch(false);
									if (assessmentFragment != null && assessmentFragment.isCompletionAvailable()) {
										assessmentFragment.changeAdapter(sectionNumber);
										transaction.show(assessmentFragment);
									} else if (placeholderFragment != null) {
										placeholderFragment.changeAdapter(sectionNumber);
										transaction.show(placeholderFragment);
									}
									break;
								case 4:
									changeActionSearch(false);
									if (assessmentFragment != null && assessmentFragment.isEquivalenceAvailable()) {
										assessmentFragment.changeAdapter(sectionNumber);
										transaction.show(assessmentFragment);
									} else if (placeholderFragment != null) {
										placeholderFragment.changeAdapter(sectionNumber);
										transaction.show(placeholderFragment);
									}
									break;
								case 5:
									changeActionSearch(false);
									if (placeholderFragment != null) {
										placeholderFragment.changeAdapter(sectionNumber);
										transaction.show(placeholderFragment);
									}
									break;
								case 6:
									changeActionSearch(false);
									if (settingsFragment != null)
										transaction.show(settingsFragment);
									break;
								case 7:
									changeActionSearch(false);
									if (assessmentFragment != null) {
										assessmentFragment.changeAdapter(sectionNumber);
										transaction.show(assessmentFragment);
									}
									break;
								default:
									break;
							}

							transaction.commit();
						}
					}
				});
			}
    	}

		lastSectionNumber = sectionNumber;
		onSectionAttached(sectionNumber);
	}

	public void onSectionAttached(int number) {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			switch (number) {
				case 1:
					actionBar.setIcon(R.drawable.stock_on_icon);
					break;
				case 2:
					actionBar.setIcon(R.drawable.flashcard_on_icon);
					break;
				case 3:
					actionBar.setIcon(R.drawable.test_on_icon);
					break;
				case 4:
					actionBar.setIcon(R.drawable.equivalence_on_icon);
					break;
				case 5:
					actionBar.setIcon(R.drawable.stats_on_icon);
					break;
				case 6:
					getActionBar().setIcon(R.drawable.settings_on_icon);
					break;
				case 7:
					getActionBar().setIcon(R.drawable.announce);
					break;
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			actionBar.setDisplayShowTitleEnabled(true);
			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(153, 0, 0, 0)));
			actionBar.setTitle(mTitle);
		}
	}

    public void changeActionButton(String title, boolean clickable, boolean visible) {
    	ActionBar actionBar = getActionBar();
    	if (actionBar != null) {
    		if (visible) {
	    		actionBar.setTitle(title);
	    	} else {
	    		actionBar.setTitle("");
	    	}
	    }
    }

	public void changeActionFinish(boolean clickable, boolean visible) {
		if (mMenu != null) {
			mMenu.findItem(R.id.action_finish)
					.setEnabled(clickable)
					.setVisible(visible);
		}
	}

	public void changeActionSearch(boolean visible) {
		if (mMenu != null)
			mMenu.findItem(R.id.action_search).setVisible(visible);
	}

    protected void cleanUp() {

		/* Close SQLite and Give up DataBase Handler */
		closeDataBase();

    	if (isTTSEnabled) {
    		tts.shutdown();
    		tts = null;
    	}

    	if (assessmentFragment != null)
    		assessmentFragment.cleanUp();

    	if (placeholderFragment != null)
    		placeholderFragment.cleanUp();

    	if (settingsFragment != null)
    		settingsFragment.cleanUp();
    }

    protected void reInit() {

		/* Get DataBase Handler and open SQLite */
		openDataBase();

		if (tts == null)
    		tts = new TextToSpeech(this, this);

    	if (assessmentFragment != null)
    		assessmentFragment.reInit();

    	if (placeholderFragment != null)
    		placeholderFragment.reInit();

    	if (settingsFragment != null)
    		settingsFragment.reInit();

    	if (mNavigationDrawerFragment != null)
    		mNavigationDrawerFragment.reInit();
    }

	/*public boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	} */

	@Override
	public void onPause () {
		super.onPause();
	}

	@Override
	public void onStop (){
		cleanUp();
		super.onStop();
	}

	@Override
	public void onStart(){
		super.onStart();
		reInit();
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState (Bundle outState) {
	}

	@Override
	protected void onRestoreInstanceState (Bundle outState) {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
    public void onBackPressed() {
        if (exit) {
        	System.gc();
            finish(); /* finish activity */
        } else {

			if (lastSectionNumber > 4
					|| (lastSectionNumber == 2 && assessmentFragment.isFlashcardAvailable())
					|| (lastSectionNumber == 3 && assessmentFragment.isCompletionAvailable())
					|| (lastSectionNumber == 4 && assessmentFragment.isEquivalenceAvailable())
					|| (placeholderFragment.isVisible() && placeholderFragment.getSectionNumber() != 0)) {
					Toast.makeText(this, "Press Back again to Exit.",
							Toast.LENGTH_SHORT).show();
					exit = true;
					System.gc();
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							System.gc();
							exit = false;
						}
					}, 2 * 1000);
			} else {
				if (lastSectionNumber == 1 && assessmentFragment.isWSAvailable()) {
					placeholderFragment.reInit(lastSectionNumber);
					assessmentFragment.resetWS();
				} else {
					if (lastSectionNumber == 1)
						setInt("VisibleList", 0);
					placeholderFragment.changeAdapter(lastSectionNumber);
				}
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				transaction.hide(assessmentFragment);
				transaction.show(placeholderFragment);
				transaction.commit();
			}
        }
    }

	public void dismissSpinner() {
		if (mBuilder != null) {
			mBuilder.dismiss();
			mBuilder = null;
		}
		System.gc();
	}

	private void showSpinner() {
		mBuilder = new Dialog(this);
		mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mBuilder.getWindow().setGravity(Gravity.CENTER);
		mBuilder.getWindow().setBackgroundDrawable(
	        new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mBuilder.setCanceledOnTouchOutside(false);
		mBuilder.setCancelable(false);
		mBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
	        @Override
	        public void onDismiss(DialogInterface dialogInterface) {
	            /* nothing; */
	        }
	    });

		mBuilder.setContentView(R.layout.custom_dialog);

	    TextView pt = (TextView) mBuilder.findViewById(R.id.progress_text); 
	    pt.setText("Initiating Database ..");
	    pt.setTextColor(Color.WHITE);
	    pt.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Quicksand-Regular.ttf"));
	    mBuilder.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			/*
			 * Only show items in the action bar relevant to this screen
			 * if the drawer is not showing. Otherwise, let the drawer
			 * decide what to show in the action bar.
			 */
			mMenu = menu;
			getMenuInflater().inflate(R.menu.main, menu);

			/* Associate searchable configuration with the SearchView */
			SearchManager searchManager =
					(SearchManager) getSystemService(Context.SEARCH_SERVICE);
			SearchView searchView =
					(SearchView) menu.findItem(R.id.action_search).getActionView();
			searchView.setSearchableInfo(
					searchManager.getSearchableInfo(getComponentName()));
			searchView.setQueryHint("Type a Word");

			final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
			searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

				@Override
				public boolean onMenuItemActionExpand(MenuItem item) {
					ActionBar ab = getActionBar();
					if (ab != null)
						ab.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
					return true; /* KEEP IT TO TRUE OR IT DOESN'T OPEN !! */
				}

				@Override
				public boolean onMenuItemActionCollapse(MenuItem item) {
					onSectionAttached(lastSectionNumber);
					return true; /* OR FALSE IF YOU DIDN'T WANT IT TO CLOSE! */
				}
			});

			searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
				@Override
				public boolean onQueryTextSubmit(String query) {
					query = query.replaceAll("\\s+", "");
					int position = placeholderFragment.fetchPosition(query);
					if (position > -1) {
						placeholderFragment.fetchCard(position);
						searchMenuItem.collapseActionView();
					} else {
						Toast.makeText(getApplicationContext(), query + " not found in this list",
								Toast.LENGTH_LONG).show();
					}
					return false;
				}

				@Override
				public boolean onQueryTextChange(String newText) {
					
					if (newText.isEmpty())
						return false;

					List<String> products;

					if (placeholderFragment != null) {
						products = placeholderFragment.fetchSearchList(newText);
					} else {
						DatabaseHandler db = new DatabaseHandler(ma.getApplicationContext());
						products = db.getAlphaWords(newText, 0, true);
						db.close();
					}

					final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
					AutoCompleteTextView mSearchAutoComplete = (AutoCompleteTextView)findViewById(getResources().getIdentifier("android:id/search_src_text", null, null));
					ArrayAdapter<String> adapter = new ArrayAdapter<>(ma, R.layout.search_drop_down_item, R.id.item_text, products);
					mSearchAutoComplete.setAdapter(adapter);
					mSearchAutoComplete.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							/* Goto the definition card for selected word */
							String data = (String)parent.getItemAtPosition(position);
							searchView.setQuery(data, true);
						}
					});
					return false;
				}
			});

			restoreActionBar();
			changeActionFinish(false, false);
			changeActionButton("", false, false);
		}

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * Handle action bar item clicks here. The action bar will
		 * automatically handle clicks on the Home/Up button, so long
		 * as you specify a parent activity in AndroidManifest.xml.
		 */
		int id = item.getItemId();

		if (id == R.id.action_finish) {

			assessmentFragment.setResultData(lastSectionNumber);
			assessmentFragment.showResult(lastSectionNumber);
			item.setVisible(false);
		}

		return super.onOptionsItemSelected(item);
	}

	/* Inserting Words and related info */
	public void fillDatabase() {
		DatabaseHandler db = new DatabaseHandler(this);
		WorkbookSettings ws;
  		Workbook workbook;
  		Sheet s;
  		Cell[] rowData;

  		try {
  			ws = new WorkbookSettings();
  			ws.setLocale(new Locale("en", "EN"));
  			InputStream is = this.getApplicationContext().getResources().getAssets().open("sheets/data_v3.xls");
  			workbook = Workbook.getWorkbook(is, ws);

  			/* Getting Default Sheet i.e. 0 */
  			s = workbook.getSheet(0);
   
  			/* Total No Of Rows in Sheet, will return you no of rows that are occupied with some data */
  			int rowCount = s.getRows();

  			for (int i = 0; i < rowCount; i++) {
  				/* Get Individual Row */
  	  			rowData = s.getRow(i);
  	  			String[] data = new String[12];
  	  			for (int j = 0; j < 12; j++) {
  	  				if (rowData.length > j && !rowData[j].getContents().isEmpty() && rowData[j].getContents() != null)
  	  					data[j] = rowData[j].getContents();
  	  				else
  	  					data[j] = "nil";
  	  			}

  	  			if (data[3].equals("nil") && !data[10].equals("nil")) {
  	  				data[3] = data[10];
  				} else if (!data[3].equals("nil") && !data[10].equals("nil")) {
  	  				String[] units = data[10].split(", ");
  	  				for (int k = 0; k < units.length; k++) {
  	  					if (!data[3].contains(units[k]))
  	  						data[3] = data[10] + ", " + data[3];
  	  				}
  	  			}

  	  			String[] units = data[9].split("~~~");
  	  			String temp = "";
  	  			for (int k = 0; k < units.length; k++) {
  	  				if (units[k].contains("; "))
  	  					units[k] = units[k].replace("; ", " (") + ")";
  	  				if (units[k].charAt(0) == '(' && units[k].indexOf(")") != (units[k].length() - 1))
  	  					units[k] = units[k].substring(units[k].indexOf(")") + 1);
  	  				if (units[k].charAt(0) == ' ' && units[k].length() != 1)
  	  					units[k] = units[k].substring(1);

  	  				temp += units[k] + "~~~";
  	  			}

  	  			if (temp.length() > 3)
  	  				data[9] = temp.substring(0, temp.length() - 3);

  	  			db.addWord(new Word(data[0], data[1], data[2], data[3],
									data[4], data[5], data[6], data[7],
									data[8], data[9], data[11], "todo", "false", getBaseContext()));
  			}

  			workbook.close();

  		} catch (IOException e) {
			Log.e("WordStock", "IOException");
  			e.printStackTrace();
  		} catch (BiffException e) {
			Log.e("WordStock", "BiffException");
  			e.printStackTrace();
  		}
	}

	public void copyDB(String inputFile, String outputFile) {
		byte[] buffer = new byte[1024];
		OutputStream myOutput;
		int length;
		/* Open your local db as the input stream */
		InputStream myInput;
		try
		{
		    myInput = this.getAssets().open(inputFile);
		    /* transfer bytes from the input file to the output file */
		    myOutput = new FileOutputStream(new File(outputFile));
		    while((length = myInput.read(buffer)) > 0)
		    {
		        myOutput.write(buffer, 0, length);
		    }
		    myOutput.close();
		    myOutput.flush();
		    myInput.close();

		}
		catch(IOException e)
		{
		    e.printStackTrace();
		}
	}

	private void copyDataBase()
    {
		openDataBase();

		/* Use when DB file is not compressed */
		if (db != null) {
			Log.e("[Akshay]: ", db.getDBPath());
			copyDB("db/wordsManager", db.getDBPath());
		}
    }

	private void openDataBase() {
		if (db == null)
			db = new DatabaseHandler(this);
		if (testDB == null)
			testDB = new TestDatabaseHandler(this);
	}

	private void closeDataBase() {
		if (db != null) {
			db.cleanUp();
			db.close();
			db = null;
		}
		if (testDB != null) {
			testDB.close();
			testDB = null;
		}
	}

	protected DatabaseHandler getDataBase() {
		if (db == null)
			db = new DatabaseHandler(this);

		return db;
	}

	protected TestDatabaseHandler getTestDataBase() {
		if (testDB == null)
			testDB = new TestDatabaseHandler(this);

		return testDB;
	}

	private static void extractFile(ZipInputStream in, File outdir, String name) throws IOException
	{
		byte[] buffer = new byte[4096];
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(outdir,name)));
		int count;
		while ((count = in.read(buffer)) != -1)
			out.write(buffer, 0, count);
		out.close();
	}

	private static void mkdirs(File outdir,String path)
	{
		File d = new File(outdir, path);
		if( !d.exists() )
			if (d.mkdirs())
				Log.e("WordStock", "could not create directory");
	}

	private static String dirPart(String name)
	{
		int s = name.lastIndexOf( File.separatorChar );
		return s == -1 ? null : name.substring( 0, s );
	}

	/*
	 * Extract ZipFile and copy to Out Dir with complete directory structure
	 */
	public void copyToons()
	{
		try
		{
			File outDir = this.getApplicationContext().getFilesDir();
			ZipInputStream zin = new ZipInputStream(this.getAssets().open("toons.zip"));
			ZipEntry entry;
			String name, dir;
			while ((entry = zin.getNextEntry()) != null)
			{
				name = entry.getName();
				if( entry.isDirectory() )
				{
					mkdirs(outDir,name);
					continue;
				}
				/*
				 * This part is necessary because file entry can come before
				 * directory entry where the file is located
				 * e.g.:
				 *   /foo/foo.txt
				 *   /foo/
				 */
				dir = dirPart(name);
				if( dir != null )
					mkdirs(outDir,dir);

				extractFile(zin, outDir, name);
			}
			zin.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = TextToSpeech.LANG_MISSING_DATA;
			if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP) {
				try {
					result = tts.setLanguage(tts.getVoice().getLocale());
				} catch (Exception e) {
					Toast.makeText(this, "Sorry! Found spooky OS version.\t\r\nPlease check Text-To-Speech or OS settings.",
							Toast.LENGTH_LONG).show();
				}
			} else if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
				result = tts.setLanguage(tts.getDefaultLanguage());
			} else {
				result = tts.setLanguage(tts.getLanguage());
			}

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            	isTTSEnabled = false;
            	Toast.makeText(this, "This voice is not available.\t\r\nPlease check Text-To-Speech settings.",
                        Toast.LENGTH_LONG).show();
            } else {
            	isTTSEnabled = true;
            }
 
        } else {
            Log.e("TTS", "Initialization Failed!");
        }
	}

	public void speakOut(String text) {
		if (isTTSEnabled) {
			if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP)
				tts.speak(text, TextToSpeech.QUEUE_ADD, null, text);
			else
				tts.speak(text, TextToSpeech.QUEUE_ADD, null);
		}

	}

	private void setFlag (String flagKey, boolean flagVal) {		
		SharedPreferences sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();                                                         			
		editor.putBoolean(flagKey, flagVal);
		editor.apply();
	}
	
	public boolean getFlag (String flagKey) {		
		SharedPreferences sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);		
		return sp.getBoolean(flagKey, false);
	}

	public void setInt (String flagKey, int flagVal) {
		SharedPreferences sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(flagKey, flagVal);
		editor.apply();
	}

	public int getInt (String flagKey) {
		SharedPreferences sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return sp.getInt(flagKey, 0);
	}

	private void setLong (String flagKey, long flagVal) {
		SharedPreferences sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong(flagKey, flagVal);
		editor.apply();
	}

	public long getLong (String flagKey) {
		SharedPreferences sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		return sp.getLong(flagKey, 0);
	}

	public int getBG() {
		if (getFlag("night_mode")) {
			return Color.argb(255, 20, 20, 20);
		} else {
			Tile tile = getWallpapers().get(getInt("wall_selected_index"));
			return tile.getDrawable();
		}
	}

	public void handleSwitch(View v) {
		CompoundButton mCB = (CompoundButton) v;
		boolean flagValue = mCB.isChecked();

		switch (v.getId()) {
		case R.id.flashcard_reverse_switch:
			setFlag("flashcard_reverse", flagValue);
			break;
		case R.id.ws_random_switch:
			assessmentFragment.resetWS();
			setFlag("ws_random", flagValue);
			break;
		case R.id.ws_expand_definition_switch:
			assessmentFragment.resetWS();
			setFlag("ws_expand_definition", flagValue);
			break;
		case R.id.ws_expand_examples_switch:
			assessmentFragment.resetWS();
			setFlag("ws_expand_examples", flagValue);
			break;
		case R.id.ws_expand_synonyms_switch:
			assessmentFragment.resetWS();
			setFlag("ws_expand_synonyms", flagValue);
			break;
		case R.id.ws_expand_antonyms_switch:
			assessmentFragment.resetWS();
			setFlag("ws_expand_antonyms", flagValue);
			break;
		case R.id.ws_expand_origin_switch:
			assessmentFragment.resetWS();
			setFlag("ws_expand_origin", flagValue);
			break;
		case R.id.ws_speak_definition_switch:
			setFlag("ws_speak_definition", flagValue);
			break;
		case R.id.ws_speak_example_switch:
			setFlag("ws_speak_example", flagValue);
			break;
		case R.id.ws_speak_synonym_switch:
			setFlag("ws_speak_synonym", flagValue);
			break;
		case R.id.ws_speak_antonym_switch:
			setFlag("ws_speak_antonym", flagValue);
			break;
		case R.id.ws_speak_origin_switch:
			setFlag("ws_speak_origin", flagValue);
			break;
		case R.id.night_mode_switch:
			setFlag("night_mode", flagValue);
			reInit();
			break;
		default:
			break;
		}
	}
}