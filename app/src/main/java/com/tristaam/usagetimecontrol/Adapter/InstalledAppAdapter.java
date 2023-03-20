package com.tristaam.usagetimecontrol.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usagetimecontrol.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tristaam.usagetimecontrol.Controller.Util.ImageProcessing;
import com.tristaam.usagetimecontrol.Controller.Listener.InstalledAppListener;
import com.tristaam.usagetimecontrol.Model.App;

import java.util.List;

public class InstalledAppAdapter extends RecyclerView.Adapter<InstalledAppAdapter.DataViewHolder> implements InstalledAppListener {
    private List<App> appList;
    private Context context;
    private int lastPosition;
    private OnClickListener listener;

    public InstalledAppAdapter(Context context, List<App> appList, OnClickListener listener) {
        this.appList = appList;
        this.context = context;
        this.lastPosition = -1;
        this.listener = listener;
    }

    public List<App> getApplicationList() {
        return appList;
    }

    public void setApplicationList(List<App> appList) {
        this.appList = appList;
    }

    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.installed_app_view, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.name.setText(appList.get(position).getName());
        holder.packageName.setText(appList.get(position).getPackageName());
        holder.iconApp.setImageBitmap(ImageProcessing.ByteArrayToBitmap(appList.get(position).getByteArray()));
        setAnimation(holder.itemView, position);
    }

    public void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_right_in);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return appList == null ? 0 : appList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView packageName;
        private ImageView iconApp;
        private FloatingActionButton btnAddApp;

        public DataViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name2);
            packageName = itemView.findViewById(R.id.packageName2);
            iconApp = itemView.findViewById(R.id.iconApp2);
            btnAddApp = itemView.findViewById(R.id.btnAddApp2);
            btnAddApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.OnClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
