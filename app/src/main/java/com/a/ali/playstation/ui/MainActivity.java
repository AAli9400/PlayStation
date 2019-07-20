package com.a.ali.playstation.ui;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.a.ali.playstation.R;

public class MainActivity extends AppCompatActivity {
    private NavController mNavController;
    private AppBarConfiguration mAppBarConfiguration;

    private boolean mIsCurrentFragmentReport = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment host = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.navHostFragment);

        ActionBar actionBar = getSupportActionBar();
        if (host != null && actionBar != null) {
            mNavController = host.getNavController();

            mAppBarConfiguration = new AppBarConfiguration
                    .Builder(mNavController.getGraph())
                    .build();

            NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);

            mNavController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                switch (destination.getId()) {
                    case R.id.loginFragment:
                        actionBar.hide();
                        mIsCurrentFragmentReport = false;
                        break;

                    case R.id.consoleFragment:
                        actionBar.setTitle(getString(R.string.home));
                        actionBar.show();
                        actionBar.setDisplayHomeAsUpEnabled(false);
                        mIsCurrentFragmentReport = false;
                        break;

                    case R.id.selectReportFragment:
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        mIsCurrentFragmentReport = true;
                        break;

                    case R.id.IpFragment:
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        mIsCurrentFragmentReport = false;
                        break;

                    case R.id.logFragment:
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        mIsCurrentFragmentReport = false;
                        break;
                }
            });
        } else {
            String errorMessage = "HOST FRAGMENT OR ACTION BAR IS NULL.";
            throw new NullPointerException(errorMessage);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        NavDestination navController = mNavController.getCurrentDestination();
//
//        if (navController != null && navController.getId() == R.id.loginFragment) {
//            this.finish();
//        } else {
//            super.onBackPressed();
//        }
//    }
}