package com.wapplix;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.wapplix.social.Sharable;

/**
 *
 * @author Michaël André <michael.andre@live.fr>
 */
public class ActivityHelper {
    
    public static void showMessage(final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void handleException(Activity activity, Exception ex, String message) {
        showMessage(activity, message + "\n" + ex.getMessage());
        Log.e("Wapplix", "Exception", ex);
    }
    public static void handleException(Activity activity, Exception ex) {
        handleException(activity, ex, "Impossible de terminer l'opération, une erreur s'est produite.");
    }
    
    public static void createShareOptionMenu(final Activity activity, Menu menu, final Sharable sharable) {
        MenuItem shareMenu = menu.add("Partager");
        shareMenu.setIcon(android.R.drawable.ic_menu_share);
        shareMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                try {
                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TITLE, sharable.getSharableSubject());
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, sharable.getSharableSubject());
                    shareIntent.putExtra(Intent.EXTRA_TEXT, sharable.getSharableText());
                    activity.startActivity(Intent.createChooser(shareIntent, "Partager"));
                } catch (Exception ex) {
                    ActivityHelper.handleException(activity, ex);
                }
                return true;
            }
        });
    }
    
    /*public static void createRefreshOptionMenu(final Activity activity, Menu menu, final Refreshable refreshable) {
        MenuItem refreshMenu = menu.add("Actualiser");
        refreshMenu.setIcon(R.drawable.ic_menu_refresh);
        refreshMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                try {
                    refreshable.refreshData();
                } catch (Exception ex) {
                    ActivityHelper.handleException(activity, ex);
                }
                return true;
            }
        });
    }*/
    
    public static void createPreferencesOptionMenu(final Activity activity, Menu menu, final Class<?> preferencesActivity) {
        MenuItem preferenceMenu = menu.add("Options");
        preferenceMenu.setIcon(android.R.drawable.ic_menu_preferences);
        preferenceMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                try {
                    activity.startActivity(new Intent(activity.getBaseContext(), preferencesActivity));
                } catch (Exception ex) {
                    ActivityHelper.handleException(activity, ex);
                }
                return true;
            }
        });
    }

    public static void createInfosOptionMenu(final Activity activity, Menu menu, final Class<?> infosActivity) {
        MenuItem preferenceMenu = menu.add("Infos");
        preferenceMenu.setIcon(android.R.drawable.ic_menu_info_details);
        preferenceMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                try {
                    activity.startActivity(new Intent(activity.getBaseContext(), infosActivity));
                } catch (Exception ex) {
                    ActivityHelper.handleException(activity, ex);
                }
                return true;
            }
        });
    }
    
}