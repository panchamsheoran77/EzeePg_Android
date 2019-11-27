package com.sparkstudios.ezeepg.home_activit_files;

import android.
        app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdTargetingOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sparkstudios.ezeepg.R;
import com.sparkstudios.ezeepg.dataUtils.itemTouchHelperRecyclerView;
import com.sparkstudios.ezeepg.dataUtils.pgAppData;
import com.sparkstudios.ezeepg.pg_full_detail_files.pg_full_details;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,itemTouchHelperRecyclerView.RecyclerItemTouchHelperListener {

    private int llimit=1000,ulimit=20000;
    private static  click_listener click_Listener,shorter_clickListener;
    private  RecyclerView data_recycler;
    private ArrayList<pgAppData> pgAppDataList = new ArrayList<>();
    private ArrayList<pgAppData>shortListedpgAppDataList = new ArrayList<>();
    private  FirestoreRecyclerAdapter<com.sparkstudios.ezeepg.dataUtils.pgAppData,MyViewHolder> adapter;
    private String pgType = "Boys";
    private  FirebaseFirestore dp = FirebaseFirestore.getInstance();
    private shortListedAdapter shortListedAdapter;
    private int adapterPositon;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getGender();
        data_recycler = findViewById(R.id.data_container);
        data_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        setupSeekBar();
        setUpData_Container();
        setUpShortListedPg_container();
        // creating notification cannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("pushNotification", "Push Notification", "Important Update");
        }

        // regigtring user to a general topic for notification
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfull";
                        if (!task.isSuccessful()) {
                            msg = "failed";
                        }

                    }
                });

        AdRegistration.setAppKey("0123456789ABCDEF0123456789ABCDEF");
    }
    protected void createNotificationChannel(String id, String name, String description) {
        NotificationManager notificationManager =   (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(id, name, importance);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setDescription(description);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.enableLights(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setLightColor(Color.RED);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.enableVibration(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setVibrationPattern(    new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }

    }
    private void setUpShortListedPg_container() {
        RecyclerView shortListed_conatiner = findViewById(R.id.shortlistedPg_container);
    shortListedAdapter = new shortListedAdapter(this,shortListedpgAppDataList);
    shortListed_conatiner.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    shortListed_conatiner.setAdapter(shortListedAdapter);
        shortListed_containerClick();
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new itemTouchHelperRecyclerView(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(shortListed_conatiner);
        }
    private void getGender() {
        SharedPreferences spreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
       pgType =  spreferences.getString("gender","");
    }
    private void setupSeekBar() {
        SeekBar seekBar = findViewById(R.id.priceRange);
final TextView textView = findViewById(R.id.ulimit);
    seekBar.setMax(40);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(0);
        }
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            ulimit =i*500;
            textView.setText(llimit+" - "+ulimit);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
adapter.stopListening();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        setUpData_Container();
           adapter.startListening();
        }
    });
seekBar.setOnTouchListener(new View.OnTouchListener() {

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                // Disallow Drawer to intercept touch events
                v.getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_UP:
                // Allow Drawer to intercept touch events.
                v.getParent().requestDisallowInterceptTouchEvent(false);
                break;

        }

        // Handle seekbar touch events.
        v.onTouchEvent(event);
        return true;
    }
});

    }
void shortListed_containerClick(){
        shortListedAdapter.setClick_listener(new sorted_click_lstner() {
            @Override
            public void onItemClick(View view, int position) {
                String pgId = shortListedpgAppDataList.get(position).getPgId();
                if(!TextUtils.isEmpty(pgId)){
                    Intent i = new Intent(getApplicationContext(),pg_full_details.class);
                    i.putExtra("pg_details",shortListedpgAppDataList.get(position));
                    startActivity(i);
                }
                else{
                    Toast.makeText(MainActivity.this, "Pg details not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
}
    private void setUpData_Container() {

        MobileAds.initialize(this, "ca-app-pub-6959993616164778~9698778588");
        Query query = dp.collection("ezeePg").whereEqualTo("pgType", pgType).whereLessThanOrEqualTo("pgPrice", ulimit);

        FirestoreRecyclerOptions<pgAppData> options = new FirestoreRecyclerOptions.Builder<pgAppData>().setQuery(query, com.sparkstudios.ezeepg.dataUtils.pgAppData.class).build();
        adapter = new FirestoreRecyclerAdapter<com.sparkstudios.ezeepg.dataUtils.pgAppData, MyViewHolder>(options) {


            @Override
            public void onDataChanged() {
           pgAppDataList.clear();
            }

            @Override
            protected void onBindViewHolder(@NonNull final MyViewHolder holder, int position, @NonNull pgAppData model) {
                holder.position = holder.getAdapterPosition();

                Picasso.get().load(model.getPgImage()).into(holder.pg_image, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.isClickEnalbed = true;
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                holder.pg_name.setText(model.getPgName());
                holder.pg_type.setText("Room Type :"+model.getPgRoomType());
                holder.pg_price.setText("₹" + model.getPgPrice());
                holder.pgType.setText(""+model.getPgType()+" Pg  ");
                holder.pgRatingBar.setNumStars(5);
                holder.pgRatingBar.setIsIndicator(true);
                holder.pgRatingBar.setMax(5);
                holder.pgRatingBar.setRating(model.getPgRating());
                pgAppDataList.add(model);
                holder.mAdView = new AdLayout(getApplicationContext());
                AdTargetingOptions adOptions = new AdTargetingOptions();
                holder.mAdView.loadAd(adOptions);


            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.design_data_container,parent,false);
                return new MyViewHolder(view);
            }
        };
        data_recycler.setAdapter(adapter);

        pg_details_all();

        setOnShorter_clickListener(new click_listener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onImageViewRemove(View view, int position) {

            }
            @Override
            public void onImageViewClick(View v, int position , ImageView shorter) {
                if(shortListedpgAppDataList.contains(pgAppDataList.get(position))){
                    shortListedAdapter.removeItem(pgAppDataList.get(position));
                    shorter.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                 }else {
                    shortListedpgAppDataList.add(pgAppDataList.get(position));
                    shortListedAdapter.notifyDataSetChanged();
                    shorter.setImageResource(R.drawable.ic_bookmark_black_filled_24dp);
                }

            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        data_recycler.smoothScrollToPosition(adapterPositon);
    }
 private void pg_details_all(){
        setOnItemClickListener(new click_listener() {
            @Override
            public void onItemClick(View view, int position) {
                    adapterPositon = position;
               String pgId = pgAppDataList.get(position).getPgId();
               if(!TextUtils.isEmpty(pgId)){
                Intent i = new Intent(getApplicationContext(),pg_full_details.class);
                i.putExtra("pg_details",pgAppDataList.get(position));
                startActivity(i);
               }
                else{
                   Toast.makeText(MainActivity.this, "Pg details not available", Toast.LENGTH_SHORT).show();

               }
            }
            @Override
            public void onImageViewRemove(View view, int position) {

            }

            @Override
            public void onImageViewClick(View v, int position,ImageView shorter) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void setOnItemClickListener(click_listener ClickListener){
        click_Listener=ClickListener;
    }
    public void setOnShorter_clickListener(click_listener clickListener){
        shorter_clickListener = clickListener;
    }

    public void chngeTypeToGirls(View view) {
        adapter.stopListening();
        pgType = "Girls";
        setUpData_Container();
        adapter.startListening();
    }

    public void chngeTypeToBoys(View view) {
        adapter.stopListening();
        pgType = "Boys";
        setUpData_Container();
        adapter.startListening();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
if(viewHolder instanceof shortListedAdapter.shortListedViewHolder){
    shortListedAdapter.removeItem(viewHolder.getAdapterPosition());
}
    }
    class  MyViewHolder extends RecyclerView.ViewHolder{

     @BindView(R.id.shortListButton)
             ImageView shorter;
        ImageView pg_image;
        TextView pg_name,pg_price,pg_type,pgType;
        int position =0;
        RatingBar pgRatingBar ;
        ProgressBar progressBar;
        CardView cardView;
         AdLayout mAdView;
         ConstraintLayout constraintLayout;
         boolean isClickEnalbed = false;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isClickEnalbed){
                    click_Listener.onItemClick(view,position);
                }else {
                        Toast.makeText(MainActivity.this, "Loading Info Plz wait ...", Toast.LENGTH_SHORT).show();
                    }}
            });

           shorter.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

               shorter_clickListener.onImageViewClick(view,position, shorter);
              }
           });
            pg_image = itemView.findViewById(R.id.pg_image_thumb);
            pg_name = itemView.findViewById(R.id.pg_name);
            pg_price = itemView.findViewById(R.id.pg_price);
            pg_type = itemView.findViewById(R.id.pg_room_type);
            pgType = itemView.findViewById(R.id.pgType);
            pgRatingBar = itemView.findViewById(R.id.pgRatingBar);
            progressBar = itemView.findViewById(R.id.progressBar);
            cardView = itemView.findViewById(R.id.cardview);
            mAdView = itemView.findViewById(R.id.adView);
            constraintLayout = itemView.findViewById(R.id.cons);

        }
    }
}
