package com.tristaam.usagetimecontrol.Adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usagetimecontrol.R;
import com.tristaam.usagetimecontrol.Activity.MainActivity;
import com.tristaam.usagetimecontrol.Controller.ImageProcessing;
import com.tristaam.usagetimecontrol.Model.App;

import java.util.List;

public class FollowAppAdapter extends RecyclerView.Adapter<FollowAppAdapter.DataViewHolder> {
    private List<App> appList;
    private Context context;

    public FollowAppAdapter(Context context, List<App> appList) {
        this.appList = appList;
        this.context = context;
    }

    public List<App> getApplicationList() {
        return appList;
    }

    public void setApplicationList(List<App> appList) {
        this.appList = appList;
    }

    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_app_view, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.name1.setText(appList.get(position).getName());
        holder.packageName1.setText(appList.get(position).getPackageName());
        holder.iconApp1.setImageBitmap(ImageProcessing.ByteArrayToBitmap(appList.get(position).getByteArray()));
    }

    @Override
    public int getItemCount() {
        return appList == null ? 0 : appList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgView1;
        private TextView name1;
        private TextView packageName1;
        private ImageView iconApp1;
        private Switch swtFollow;
        private ImageView btnExpand;
        private boolean isExpand;

        public DataViewHolder(View itemView) {
            super(itemView);
            imgView1=itemView.findViewById(R.id.imgView1);
            name1 = itemView.findViewById(R.id.name1);
            packageName1 = itemView.findViewById(R.id.packageName1);
            iconApp1=itemView.findViewById(R.id.iconApp1);
            swtFollow=itemView.findViewById(R.id.swtFollow);
            btnExpand=itemView.findViewById(R.id.btnExpand);
            isExpand=false;

            btnExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float startRotation=btnExpand.getRotation();
                    float endRotation;
                    ViewGroup.LayoutParams layoutParams=imgView1.getLayoutParams();
                    isExpand=!isExpand;
                    if (isExpand){
                        layoutParams.height+=100;
                        endRotation=startRotation+180.f;
                    }
                    else{
                        layoutParams.height-=100;
                        endRotation=startRotation-180.f;
                    }
                    imgView1.setLayoutParams(layoutParams);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(btnExpand, "rotation", startRotation, endRotation);
                    animator.setDuration(1000);
                    animator.start();
                }
            });
        }
    }
}
