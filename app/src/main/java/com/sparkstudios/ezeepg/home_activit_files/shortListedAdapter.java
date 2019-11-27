package com.sparkstudios.ezeepg.home_activit_files;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sparkstudios.ezeepg.R;
import com.sparkstudios.ezeepg.dataUtils.pgAppData;
import java.util.List;
public class shortListedAdapter extends RecyclerView.Adapter<shortListedAdapter.shortListedViewHolder> {
private Context ctx;
private List<pgAppData> pgAppDataList;
private sorted_click_lstner click_listener;

     shortListedAdapter(Context ctx, List<pgAppData> pgAppDataList) {
        this.ctx = ctx;
        this.pgAppDataList = pgAppDataList;
    }
    @NonNull
    @Override
    public shortListedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.shortlisteddesign,viewGroup,false);
        return new shortListedViewHolder(v);
    }
    public void setClick_listener(com.sparkstudios.ezeepg.home_activit_files.sorted_click_lstner click_listener) {
        this.click_listener = click_listener;
    }
    public List<pgAppData> getPgAppDataList() {
        return pgAppDataList;
    }

    @Override
    public void onBindViewHolder(@NonNull final shortListedViewHolder holder, int i) {
         holder.position = holder.getAdapterPosition();
        pgAppData model = pgAppDataList.get(i);
        holder.pg_name.setText(model.getPgName());
        holder.pg_type.setText("Room Type :"+model.getPgRoomType());
        holder.pg_price.setText("₹" + model.getPgPrice());
        holder.pgType.setText(""+model.getPgType()+" Pg");
        holder.pgRatingBar.setNumStars(5);
        holder.pgRatingBar.setIsIndicator(true);
        holder.pgRatingBar.setMax(5);
        holder.pgRatingBar.setRating(model.getPgRating());
        AdRequest adRequest = new AdRequest.Builder().build();
        holder.mAdView.loadAd(adRequest);
    }

    @Override
    public int getItemCount() {
        return pgAppDataList.size();
    }


 public class shortListedViewHolder extends RecyclerView.ViewHolder {

        TextView pg_name,pg_price,pg_type,pgType;
        int position =0;
        RatingBar pgRatingBar ;
        ProgressBar progressBar;
        public  CardView cardView;
        public  RelativeLayout viewBackground;
        AdView mAdView;
         shortListedViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click_listener.onItemClick(view,position);
                }
            });
            viewBackground = itemView.findViewById(R.id.view_background);
            pg_name = itemView.findViewById(R.id.pg_name);
            pg_price = itemView.findViewById(R.id.pg_price);
            pg_type = itemView.findViewById(R.id.pg_room_type);
            pgType = itemView.findViewById(R.id.pgType);
            pgRatingBar = itemView.findViewById(R.id.pgRatingBar);
            progressBar = itemView.findViewById(R.id.progressBar);
            cardView = itemView.findViewById(R.id.cardview);
             mAdView = itemView.findViewById(R.id.adView);
        }
    }
   public  void removeItem(int position) {
        pgAppDataList.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(pgAppData pgAppData) {
        int position = pgAppDataList.indexOf(pgAppData);
         pgAppDataList.remove(pgAppData);
        notifyItemRemoved(position);
    }
}