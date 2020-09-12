package com.example.eren.myapplication;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eren.myapplication.Adapter.CustomExpandableListAdapter;
import com.example.eren.myapplication.Fragments.FragmentBasket;
import com.example.eren.myapplication.Fragments.FragmentProducts;
import com.example.eren.myapplication.Fragments.FragmentSettings;
import com.example.eren.myapplication.Helper.FragmentNavigationManager;
import com.example.eren.myapplication.Interface.IOnBackPressed;
import com.example.eren.myapplication.Interface.NavigationManager;
import com.example.eren.myapplication.Models.TopCategories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private String[] items;
    public static ActionBar actionBar;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter adapter;
    private List<String> lstTitle;
    private Map<String,List<String>> lstChild;
    public static NavigationManager navigationManager;

    public static BottomNavigationView bottomNavigationView;
    private TextView txt_get_all_cat;
    private int lastExpandedPosition = -1;

    private Bundle savedInstanceState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.savedInstanceState=savedInstanceState;
        //initview
        mDrawerLayout=findViewById(R.id.drawer_layout);
        mActivityTitle=getTitle().toString();
        expandableListView=findViewById(R.id.nav_list);
        navigationManager= FragmentNavigationManager.getmInstance(this);
      //  initItems();
        actionBar=getSupportActionBar();

        View listHeaderView=getLayoutInflater().inflate(R.layout.nav_header,null,false);
        txt_get_all_cat=listHeaderView.findViewById(R.id.txt_get_all_categories);
        expandableListView.addHeaderView(listHeaderView);
        setTopCategories();
      //  genData();
      //  addDrawersItem();
        setupDrawer();

        initNav();

        if(savedInstanceState==null)
            selectFirstItemAsDefault();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

    }

    private void initNav() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.navigation_home:
                        getSupportActionBar().setTitle("Ürünler");
                        navigationManager.showFragment(FragmentProducts.newInstance(""),false);
                        return true;
                    case R.id.navigation_basket:
                        getSupportActionBar().setTitle("Sepetim");
                        navigationManager.showFragment(FragmentBasket.newInstance(""),false);
                        return true;
                    case R.id.navigation_settings:
                        getSupportActionBar().setTitle("Profil");
                        navigationManager.showFragment(FragmentSettings.newInstance(""),false);
                        return true;
                }
                return false;
            }
        });
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectFirstItemAsDefault() {
        if(navigationManager!=null){
            //String firstItem=lstTitle.get(0);
            String firstItem="Tüm Ürünler";
            navigationManager.showFragment(FragmentProducts.newInstance(""),false);
            getSupportActionBar().setTitle(firstItem);
        }

    }

    private void setupDrawer() {
        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
              //  getSupportActionBar().setTitle("Marketim");
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
              //  getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void addDrawersItem() {
        adapter=new CustomExpandableListAdapter(this,lstTitle,lstChild);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                getSupportActionBar().setTitle(lstTitle.get(groupPosition).toString());
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                bottomNavigationView.setSelectedItemId(R.id.navigation_home);

                String selectedItem=((List)(lstChild.get(lstTitle.get(groupPosition)))).get(childPosition).toString();
                if(selectedItem=="Tümü")
                    selectedItem=lstTitle.get(groupPosition);
                getSupportActionBar().setTitle(selectedItem);

               // if(items[0].equals(lstTitle.get(groupPosition))){
                 navigationManager.showFragment(FragmentProducts.newInstance(hmapcategories.get(selectedItem)),false);
                  //  navigationManager.showFragment(selectedItem);
              //  }else
              //      throw new IllegalArgumentException("Not supported Fragment");
                parent.collapseGroup(groupPosition);
                mDrawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });

        txt_get_all_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                getSupportActionBar().setTitle("Tüm Ürünler");
                navigationManager.showFragment(FragmentProducts.newInstance(""),false);
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }



    public RequestQueue mQueue;
    public ArrayList<TopCategories> topCategories;
    public static final String TAG = TopCategories.class.getSimpleName();
    public ArrayList<String> topCategoryTitle=new ArrayList<>();
    public ArrayList<String> topCategoryTitleID=new ArrayList<>();
    HashMap<String, String> hmapcategories = new HashMap<String, String>();

    public void setTopCategories(){
        String url = "http://10.0.3.2:3000/categories";
        mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        topCategories= new ArrayList<>();
                        try {
                            JSONArray jsonArray = response.getJSONArray("categories");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject topCategory = jsonArray.getJSONObject(i);
                                String id = topCategory.getString("_id");
                                String name = topCategory.getString("name");
                                String node_id = topCategory.getString("node_id");
                                TopCategories p=new TopCategories(id,name,node_id);
                                topCategories.add(p);
                            }
                            refreshData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);

    }

    public void refreshData() {

        if (topCategories.isEmpty()) {
            Log.i(TAG,"Veri Yok");
        } else {
            for (int i = 0; i < topCategories.size(); i++) {
                hmapcategories.put(topCategories.get(i).getName(),topCategories.get(i).getId());
                if(topCategories.get(i).getNodeId().equals("0")){
                    topCategoryTitle.add(topCategories.get(i).getName());
                    topCategoryTitleID.add(topCategories.get(i).getId());
                }
            }
            List<String> childitem= Arrays.asList("Beginner","Intermediate","Advanced","Professional");
            Map<String,ArrayList<String>> subcategories=new TreeMap<>();
            ArrayList<String> subcategory=new ArrayList<>();
            lstChild=new TreeMap<>();


            for(int j=0;j<topCategoryTitle.size();j++){
                subcategory=new ArrayList<>();
                for (int i = 0; i < topCategories.size(); i++) {
                    if (topCategoryTitleID.get(j).equals(topCategories.get(i).getNodeId())) {
                        subcategory.add(topCategories.get(i).getName());
                    }
                }
                Collections.sort(subcategory);
                subcategory.add(0,"Tümü");
                lstChild.put(topCategoryTitle.get(j),subcategory);
            }

            lstTitle=new ArrayList<>(lstChild.keySet());
            addDrawersItem();


        }

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {

            super.onBackPressed();
        }

    }
}
