package com.example.g102.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.g102.R;
import com.example.g102.fragment.chatsFragment;
import com.example.g102.fragment.filtersFragment;
import com.example.g102.fragment.homeFragment;
import com.example.g102.fragment.profileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation=findViewById(R.id.bottom_navigation);
      bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
      openFragment( new homeFragment());

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                   if(item.getItemId()==R.id.itemHome){
                       openFragment(new homeFragment());
                   }else if(item.getItemId()==R.id.itemFilter){
                       openFragment(new filtersFragment());
                   }else if(item.getItemId()==R.id.itemChats){
                       openFragment(new chatsFragment());
                   }else if(item.getItemId()==R.id.itemPerfil){
                       openFragment(new profileFragment());
                   }
                       return true;
                }
            };
}

