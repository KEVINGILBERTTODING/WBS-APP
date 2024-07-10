package com.example.wbs.features.dashboard.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.wbs.R;
import com.example.wbs.features.dashboard.ui.fragments.DashboardFragment;
import com.example.wbs.features.pengaduan.ui.fragments.PengaduanFragment;
import com.example.wbs.features.profile.ui.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainDashboardActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_dashboard);
        bottomNavigationView = findViewById(R.id.bottomBar);

        fragmentTransaction(new DashboardFragment());

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuHome) {
                    fragmentTransaction(new DashboardFragment());
                    return true;
                } else if (item.getItemId() == R.id.menuPendauan) {
                    fragmentTransaction(new PengaduanFragment());
                    return true;
                } else if (item.getItemId() == R.id.menuProfile) {
                    fragmentTransaction(new ProfileFragment());
                    return true;
                }
                return false;
            }
        });




    }

    private void fragmentTransaction(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameDashboard, fragment)
                .commit();
    }
}