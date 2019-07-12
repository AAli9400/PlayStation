package com.a.ali.playstation.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.a.ali.playstation.R;

public class MainActivity extends AppCompatActivity {
    private NavController mNavController;
    private AppBarConfiguration mAppBarConfiguration;

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
                        break;

                    case R.id.homeFragment:
                        actionBar.show();
                        actionBar.setTitle(getString(R.string.home));
                        actionBar.setDisplayHomeAsUpEnabled(false);
                        break;

                    default:
                        actionBar.setDisplayHomeAsUpEnabled(true);
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
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration);
    }

    @Override
    public void onBackPressed() {
        NavDestination navController = mNavController.getCurrentDestination();

        if (navController != null && navController.getId() == R.id.homeFragment) {
            this.finish();
        } else {
            super.onBackPressed();
        }
    }
}
