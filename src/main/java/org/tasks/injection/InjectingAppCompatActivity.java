package org.tasks.injection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.tasks.analytics.Tracker;
import org.tasks.locale.Locale;

import javax.inject.Inject;

public abstract class InjectingAppCompatActivity extends AppCompatActivity implements InjectingActivity {
    private ActivityComponent activityComponent;

    @Inject Tracker tracker;

    public InjectingAppCompatActivity() {
        Locale.getInstance(this).applyOverrideConfiguration(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityComponent = ((InjectingApplication) getApplication()).getComponent().plus(new ActivityModule(this));
        inject(activityComponent);
        setTitle("");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        tracker.showScreen(getClass().getSimpleName());
    }

    @Override
    public ActivityComponent getComponent() {
        return activityComponent;
    }
}
