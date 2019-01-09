package org.jraf.android.nolock.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import org.jraf.android.nolock.AppWidget;
import org.jraf.android.nolock.NoLockService;
import org.jraf.android.nolock.R;

public class MainActivity extends Activity {
    SharedPreferences sp;
    TextView textView1;
    TextView textView2;
    private final OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            int i = 0;
            Editor edit = MainActivity.this.sp.edit();
            edit.putBoolean("PREF_LOCKING", z);
            edit.commit();
            if (z) {
                MainActivity.this.textView1.setVisibility(View.VISIBLE);
                MainActivity.this.textView2.setVisibility(View.GONE);
            } else {
                MainActivity.this.textView2.setVisibility(View.VISIBLE);
                MainActivity.this.textView1.setVisibility(View.GONE);
            }
            MainActivity.this.startService(new Intent(MainActivity.this, NoLockService.class));
            AppWidgetManager instance = AppWidgetManager.getInstance(MainActivity.this);
            int[] appWidgetIds = instance.getAppWidgetIds(new ComponentName(MainActivity.this, AppWidget.class));
            int length = appWidgetIds.length;
            while (i < length) {
                AppWidget.init(MainActivity.this, instance, appWidgetIds[i]);
                i++;
            }
        }
    };

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);
        this.sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean z = this.sp.getBoolean("PREF_LOCKING", true);
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setChecked(z);
        toggleButton.setOnCheckedChangeListener(this.onCheckedChangeListener);
        this.textView1 = (TextView) findViewById(R.id.textLocking);
        this.textView2 = (TextView) findViewById(R.id.textUnlocking);
        if (z) {
            this.textView1.setVisibility(View.VISIBLE);
            this.textView2.setVisibility(View.GONE);
        } else {
            this.textView1.setVisibility(View.GONE);
            this.textView2.setVisibility(View.VISIBLE);
        }
        startService(new Intent(this, NoLockService.class));
        if (!this.sp.getBoolean("PREF_SEEN_WELCOME", false)) {
            showDialog(1);
            this.sp.edit().putBoolean("PREF_SEEN_WELCOME", true).commit();
        }
    }

    @Override
    protected Dialog onCreateDialog(int i) {
        Builder builder = new Builder(this);
        View inflate;
        switch (i) {
            case 0:
                builder.setTitle(R.string.dialog_about_title);
                inflate = View.inflate(this, R.layout.dialog_about, null);
                TextView textView = (TextView) inflate.findViewById(R.id.aboutTextView);
                textView.setText(Html.fromHtml(getString(R.string.dialog_about_message)));
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                builder.setView(inflate);
                builder.setPositiveButton(R.string.common_ok, null);
                break;
            case 1:
                builder.setTitle(R.string.dialog_welcome_title);
                inflate = View.inflate(this, R.layout.dialog_about, null);
                ((TextView) inflate.findViewById(R.id.aboutTextView)).setText(Html.fromHtml(getString(R.string.dialog_welcome_message)));
                builder.setView(inflate);
                builder.setPositiveButton(R.string.common_ok, null);
                break;
        }
        return builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_about:
                showDialog(0);
                break;
            case R.id.menu_help:
                showDialog(1);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
