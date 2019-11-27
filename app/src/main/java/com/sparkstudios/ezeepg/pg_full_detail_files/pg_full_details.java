package com.sparkstudios.ezeepg.pg_full_detail_files;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sparkstudios.ezeepg.R;
import com.sparkstudios.ezeepg.dataUtils.OnSwipeTouchListener;
import com.sparkstudios.ezeepg.dataUtils.pgAppData;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class pg_full_details extends AppCompatActivity {
private RecyclerView iamge_gallery_container,facility_container;
private String pg_id;
private FirestoreRecyclerAdapter<gallery_images_list,MyGalleryViewHolder> galleryAdapter;
private FirestoreRecyclerAdapter<pg_facility_list,pgFacilityViewHolder> facilityAdapter;
private static  clickListener click_Listener;
private List<gallery_images_list> galleryImagesLists = new ArrayList<>();
private pgAppData  pgAppData;
private   TextView wifi,ac,water,lift,campus,meals,cctv,tea;
private CardView otherFacilitiesCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg_full_details);
        Intent i = getIntent();
        pgAppData =  i.getParcelableExtra("pg_details");
         setUpBasicDetails();
         hideCommonFacilities();
         setUpImage_container();
         setUpFacility_container();
        prepareCommonPgFaciities();
}

    private void hideCommonFacilities() {
    wifi.setVisibility(View.GONE);
    ac.setVisibility(View.GONE);
    water.setVisibility(View.GONE);
    lift.setVisibility(View.GONE);
    campus.setVisibility(View.GONE);
    meals.setVisibility(View.GONE);
    cctv.setVisibility(View.GONE);
    tea.setVisibility(View.GONE);
        otherFacilitiesCard.setVisibility(View.GONE);

    }

    private void setUpBasicDetails() {
        TextView pg_name_text,pgRoom,pgType,pgPrice,pgAddress;
        pg_name_text = findViewById(R.id.pg_name_details);
        pgPrice = findViewById(R.id.pgPrice_full);
        pgRoom = findViewById(R.id.pgRoomType_full);
        pgType = findViewById(R.id.pgType_full);
        pgAddress = findViewById(R.id.pgAddress);
        wifi = findViewById(R.id.pgWifi);
        ac = findViewById(R.id.pgAcNonAc);
        water = findViewById(R.id.pgRo);
        lift = findViewById(R.id.pgLift);
        campus = findViewById(R.id.pgNear);
        meals = findViewById(R.id.pgMeals);
        cctv = findViewById(R.id.pgCctv);
        tea = findViewById(R.id.pgTea);
        otherFacilitiesCard = findViewById(R.id.cardview_facility_up);
        pg_name_text.setText(pgAppData.getPgName());
        pg_id = pgAppData.getPgId();
        setUpRatinBar(pgAppData.getPgRating());
        pgPrice.setText("₹ "+pgAppData.getPgPrice()+" / per month");
        pgRoom.setText(pgAppData.getPgRoomType());
        pgType.setText(pgAppData.getPgType());
        pgAddress.setText(pgAppData.getPgAddress());

    }

    private void setUpRatinBar(float pgRating) {
        RatingBar ratingBar = findViewById(R.id.pgRatingBar);
        ratingBar.setIsIndicator(true);
        ratingBar.setMax(5);
        ratingBar.setNumStars(5);
        ratingBar.setRating(pgRating);
    }





    private void setUpFacility_container() {
    facility_container = findViewById(R.id.pg_faclities_container);
    facility_container.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    prepare_facility_list();
    }

    private void prepare_facility_list() {

 FirebaseFirestore firestore = FirebaseFirestore.getInstance();
 Query query = firestore.collection("ezeePg").document(pg_id).collection("otherFaclities");
    FirestoreRecyclerOptions<pg_facility_list> options = new FirestoreRecyclerOptions.Builder<pg_facility_list>().setQuery(query,pg_facility_list.class).build();
    facilityAdapter = new FirestoreRecyclerAdapter<pg_facility_list, pgFacilityViewHolder>(options) {
        @Override
        protected void onBindViewHolder(@NonNull pgFacilityViewHolder holder, int position, @NonNull pg_facility_list model) {

            if(!TextUtils.isEmpty(model.getFacility())){
                otherFacilitiesCard.setVisibility(View.VISIBLE);
            holder.textView.setText(model.getFacility());

            }

        }

        @NonNull
        @Override
        public pgFacilityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.facility_container_design,viewGroup,false);
            return new pgFacilityViewHolder(view);
        }
    };

    facility_container.setAdapter(facilityAdapter);


    }

    private void setUpImage_container() {
        iamge_gallery_container = findViewById(R.id.pg_image_container);

        iamge_gallery_container.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
         loadNprepareGalleryList();
    }


    PopupWindow pwindo;
    private void initiatePopupWindow(int x) {


        try {
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater != null ? inflater.inflate(R.layout.contact_us,
                    findViewById(R.id.popup_element)) : null;
        TextView number;
        Button dial;

            pwindo = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, -100);

                  number = layout.findViewById(R.id.contact_person_number);
                  dial = layout.findViewById(R.id.openDiler);
                 String num = null;

            switch (x){
                case 1 :
                    number.setText("Contact Number : 8109716921");
                    num = "tel:+918109716921";
                    break;
                case 2 :
                    number.setText("Contact Number : 9812965265");
                    num = "tel:+919812965265";
                    break;
                case 3 :
                    number.setText("Contact Number : 8295429751");
                    num = "tel:+918295429751";
                      break;
            }
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(num));
            dial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void popup(View view) {
        int y =0;
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        y = sharedPreferences.getInt("x",0);
        if(y==0){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Random random = new Random();
        int x = random.nextInt(3);
        editor.putInt("x",x+1);
        editor.commit();
        initiatePopupWindow(x+1);
    }else{
            initiatePopupWindow(y);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        galleryAdapter.startListening();
        facilityAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        facilityAdapter.stopListening();
        galleryAdapter.stopListening();
    }

    void loadNprepareGalleryList(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("ezeePg").document(pg_id).collection("pgGallery");
        FirestoreRecyclerOptions<gallery_images_list> options = new FirestoreRecyclerOptions.Builder<gallery_images_list>().setQuery(query,gallery_images_list.class).build();
        galleryAdapter = new FirestoreRecyclerAdapter<gallery_images_list, MyGalleryViewHolder>(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
            galleryImagesLists.clear();
            }
            @Override
            protected void onBindViewHolder(@NonNull final MyGalleryViewHolder holder, int position, @NonNull gallery_images_list model) {
                holder.position = holder.getAdapterPosition();
                galleryImagesLists.add(model);
                Picasso.get().load(model.getImage()).into(holder.gallery_image, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

            }

            @NonNull
            @Override
            public MyGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.iamge_container_design,viewGroup,false);

                return new MyGalleryViewHolder(view);
            }
        };
  iamge_gallery_container.setAdapter(galleryAdapter);
    increaseScale();
    }

private  void increaseScale(){

setOnItemClickListener(new clickListener() {
    @Override
    public void onItemClick(View view, int position) {
     initiatePopupWndow(position);

    }
});

}

     void setOnItemClickListener(clickListener ClickListener){
        click_Listener=ClickListener;
    }


    class MyGalleryViewHolder extends RecyclerView.ViewHolder {

        ImageView gallery_image;
        ProgressBar progressBar;
        int position;
        MyGalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click_Listener.onItemClick(view,position);
                }
            });
            gallery_image = itemView.findViewById(R.id.gallery_image);
            progressBar = itemView.findViewById(R.id.galleryProgessBar);
        }
    }
    class  pgFacilityViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
         pgFacilityViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.pg_facility);
        }
    }



    PopupWindow pwndo;
    private void initiatePopupWndow(final int x) {
         iamge_gallery_container.scrollToPosition(x+1);

        try {
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater != null ? inflater.inflate(R.layout.imageonscreen,
                    findViewById(R.id.popup)) : null;
            assert layout != null;
            final ImageView imageView = layout.findViewById(R.id.fullScreenImage);
            pwndo = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            pwndo.showAtLocation(layout, Gravity.CENTER, 0, 0);
        Picasso.get().load(galleryImagesLists.get(x).getImage()).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                layout.findViewById(R.id.pgBar).setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()){
            int y =x;
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                y =y+1;

                layout.findViewById(R.id.pgBar).setVisibility(View.VISIBLE);
                if(y<galleryImagesLists.size()){
                    iamge_gallery_container.scrollToPosition(y+1);
                    Picasso.get().load(galleryImagesLists.get(y).getImage()).into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            layout.findViewById(R.id.pgBar).setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }else{
                    y = galleryImagesLists.size()-1;

                }
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                y =y-1;
                iamge_gallery_container.scrollToPosition(y);
                layout.findViewById(R.id.pgBar).setVisibility(View.VISIBLE);
             if(y>=0){
                 Picasso.get().load(galleryImagesLists.get(y).getImage()).into(imageView, new Callback() {
                     @Override
                     public void onSuccess() {
                         layout.findViewById(R.id.pgBar).setVisibility(View.GONE);
                     }

                     @Override
                     public void onError(Exception e) {

                     }
                 });
             }

            else {
                y=0;
             }
            }
        });

         } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void prepareCommonPgFaciities(){

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("ezeePg").document(pg_id).collection("faclities")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                for(QueryDocumentSnapshot queryDocument  : task.getResult()){
                    commonPgFacilities commonPgFacilities ;
                    commonPgFacilities = queryDocument.toObject(com.sparkstudios.ezeepg.pg_full_detail_files.commonPgFacilities.class);

                    if(commonPgFacilities.isWifi()){
                        wifi.setVisibility(View.VISIBLE);
                    }else{
                        wifi.setVisibility(View.GONE);
                    }

                    if(commonPgFacilities.isAc()){
                        ac.setVisibility(View.VISIBLE);
                    }else{
                        ac.setVisibility(View.GONE);
                    }
                    if(commonPgFacilities.isLift()){
                        lift.setVisibility(View.VISIBLE);
                    }else{
                        lift.setVisibility(View.GONE);
                    }
                    if(commonPgFacilities.isRoWater()){
                        water.setVisibility(View.VISIBLE);
                    }else{
                        water.setVisibility(View.GONE);
                    }
                    if(commonPgFacilities.isMeals()){
                        meals.setVisibility(View.VISIBLE);
                    }else{
                        meals.setVisibility(View.GONE);
                    }
                    if(commonPgFacilities.isNearCampus()){
                        campus.setVisibility(View.VISIBLE);
                    }else{
                        campus.setVisibility(View.GONE);
                    }
                    if(commonPgFacilities.isCctv()){
                        cctv.setVisibility(View.VISIBLE);
                    }else{
                        cctv.setVisibility(View.GONE);
                    }
                    if(commonPgFacilities.isTea()){
                        tea.setVisibility(View.VISIBLE);
                    }else{
                        tea.setVisibility(View.GONE);
                    }
                }
            }}
        });





    }









}
