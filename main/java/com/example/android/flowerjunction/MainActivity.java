package com.example.android.flowerjunction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCE_FILE = "shared_preference_file";
    static int bucketCost;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;


    final static ArrayList<Flower> birthdayFlower = new ArrayList<>();
    final static ArrayList<Flower> weddingFlower = new ArrayList<>();
    final static ArrayList<Flower> valentineFlower = new ArrayList<>();

    static ArrayList<Order> last_orders = new ArrayList<>();

    static int[] birthdayCount = new int[] {0 , 0 , 0 , 0 , 0 , 0};
    static int[] weddingCount = new int[] {0 , 0 , 0 , 0 , 0 , 0};
    static int[] valentineCount = new int[] {0 , 0 , 0 , 0 , 0 , 0};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bucketCost = 0;

        // getting shared preferences file
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_FILE , Context.MODE_PRIVATE);

        // creating toolbar, with hamburger button
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_drawer);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout , toolbar , R.string.open , R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // setting navigation header text
        View view = navigationView.getHeaderView(0);
        TextView nameInDrawer = view.findViewById(R.id.navigation_drawer_text);
        nameInDrawer.setText("Hi, " + sharedPreferences.getString("userName" , "Saurav"));

        // navigation , on menu item click
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.close();
                switch (item.getItemId()){
                    case R.id.menu_item_basket:
                        replaceFragment(1);
                        break;
                    case R.id.menu_item_placed_orders:
                        replaceFragment(2);
                        break;
                    case R.id.menu_item_user_detail:
                        replaceFragment(3);
                        break;
                    case R.id.menu_item_about_us:
                        replaceFragment(4);
                        break;
                    case R.id.menu_item_logout:
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(getApplicationContext() , SplashScreen.class));
                        finish();
                    default: return false;
                }
                return true;
            }
        });

        weddingFlower.add(new Flower("New sunrise" , "wedding" , R.drawable.wedding_1, 7, 0));
        weddingFlower.add(new Flower("Pink rose" , "wedding" , R.drawable.wedding_2, 5, 0));
        weddingFlower.add(new Flower("Girl power" , "wedding" , R.drawable.wedding_3, 5, 0));
        weddingFlower.add(new Flower("Joyful memory" , "wedding" , R.drawable.wedding_4, 8, 0));
        weddingFlower.add(new Flower("Modern royalty" , "wedding" , R.drawable.wedding_5, 4, 0));
        weddingFlower.add(new Flower("White rose" , "wedding" , R.drawable.wedding_6, 6, 0));


        birthdayFlower.add(new Flower("Your smile" , "birthday" , R.drawable.birthday_1, 4, 0));
        birthdayFlower.add(new Flower("Happy blooms" , "birthday" , R.drawable.birthday_2, 7, 0));
        birthdayFlower.add(new Flower("Sun dance" , "birthday" , R.drawable.birthday_3, 6, 0));
        birthdayFlower.add(new Flower("Birthday cheer" , "birthday" , R.drawable.birthday_4, 7, 0));
        birthdayFlower.add(new Flower("Honey comb" , "birthday" , R.drawable.birthday_5, 5, 0));
        birthdayFlower.add(new Flower("New day" , "birthday" , R.drawable.birthday_6, 4, 0));

        valentineFlower.add(new Flower("Red rose" , "valentine" , R.drawable.valentine_1, 6, 0));
        valentineFlower.add(new Flower("Secret kiss" , "valentine" , R.drawable.valentine_2, 10, 0));
        valentineFlower.add(new Flower("Fascinating" , "valentine" , R.drawable.valentine_3, 8, 0));
        valentineFlower.add(new Flower("U r my angel" , "valentine" , R.drawable.valentine_4, 6, 0));
        valentineFlower.add(new Flower("Shiny day" , "valentine" , R.drawable.valentine_5, 8, 0));
        valentineFlower.add(new Flower("Heartfelt" , "valentine" , R.drawable.valentine_6, 6, 0));

        replaceFragment(0);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("last_orders" , null);
        Type type = new TypeToken<ArrayList<Order>>(){}.getType();
        last_orders = gson.fromJson(json, type);
        if(last_orders == null){
            last_orders = new ArrayList<Order>();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(last_orders);
        editor.putString("last_orders" , json);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.option_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.go_to_home){
            replaceFragment(0);
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(int idx){
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.host_container , idx == 0? HomeFragment.class : (idx == 1? Basket.class : (idx == 2 ? PlacedOrders.class : (idx == 3 ? UserDetail.class : AboutUs.class))) , null)
                .commit();
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isOpen()){
            drawerLayout.close();
        }
        moveTaskToBack(true);
    }

}