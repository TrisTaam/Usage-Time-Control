package com.tristaam.usagetimecontrol.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.tristaam.usagetimecontrol.R;
import com.tristaam.usagetimecontrol.Controller.Util.CONSTANT;
import com.tristaam.usagetimecontrol.Controller.Util.CustomFormatter;
import com.tristaam.usagetimecontrol.Controller.Util.ImageProcessing;
import com.tristaam.usagetimecontrol.Controller.Util.ScreenFunc;
import com.tristaam.usagetimecontrol.Model.FollowApp;

import java.util.List;

public class FollowAppAdapter extends RecyclerView.Adapter<FollowAppAdapter.DataViewHolder> {
    private List<FollowApp> appList;
    private Context context;

    public FollowAppAdapter(Context context, List<FollowApp> appList) {
        this.appList = appList;
        this.context = context;
    }

    public List<FollowApp> getApplicationList() {
        return appList;
    }

    public void setApplicationList(List<FollowApp> appList) {
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
        holder.iconApp1.setImageBitmap(ImageProcessing.byteArrayToBitmap(appList.get(position).getByteArray()));
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
        private ConstraintLayout expandLayout;
        private NumberPicker hourPicker;
        private NumberPicker minutePicker;
        private NumberPicker secondPicker;
        private ViewGroup.LayoutParams layoutParams;
        private float densityRatio;

        public DataViewHolder(View itemView) {
            super(itemView);
            imgView1 = itemView.findViewById(R.id.imgView1);
            name1 = itemView.findViewById(R.id.name1);
            packageName1 = itemView.findViewById(R.id.packageName1);
            iconApp1 = itemView.findViewById(R.id.iconApp1);
            swtFollow = itemView.findViewById(R.id.swtFollow);
            btnExpand = itemView.findViewById(R.id.btnExpand);
            expandLayout = itemView.findViewById(R.id.expandLayout);
            hourPicker = itemView.findViewById(R.id.hourPicker);
            minutePicker = itemView.findViewById(R.id.minutePicker);

            hourPicker.setMinValue(0);
            hourPicker.setMaxValue(23);
            hourPicker.setFormatter(new CustomFormatter());

            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(59);
            minutePicker.setFormatter(new CustomFormatter());

            isExpand = false;

            densityRatio = ScreenFunc.getDensityRatio(context);

            layoutParams = imgView1.getLayoutParams();
            layoutParams.height = CONSTANT.START_DP * (int) densityRatio;
            imgView1.setLayoutParams(layoutParams);
            expandLayout.setVisibility(View.GONE);

            btnExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeViewSize();
                }
            });
        }

        public void setViewSize(boolean isExpand) {
            ValueAnimator animator;
            if (isExpand) {
                animator = ValueAnimator.ofInt(CONSTANT.START_DP * (int) densityRatio, CONSTANT.END_DP * (int) densityRatio);
                animator.setDuration(300);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        layoutParams.height = (int) valueAnimator.getAnimatedValue();
                        imgView1.setLayoutParams(layoutParams);
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        expandLayout.setVisibility(View.VISIBLE);
                    }
                });
                animator.start();
            } else {
                animator = ValueAnimator.ofInt(CONSTANT.END_DP * (int) densityRatio, CONSTANT.START_DP * (int) densityRatio);
                animator.setDuration(300);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        layoutParams.height = (int) valueAnimator.getAnimatedValue();
                        imgView1.setLayoutParams(layoutParams);
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        expandLayout.setVisibility(View.GONE);
                    }
                });
                animator.start();
            }
        }

        public void changeViewSize() {
            isExpand = !isExpand;
            setViewSize(isExpand);
            ObjectAnimator animator;
            if (isExpand) {
                animator = ObjectAnimator.ofFloat(btnExpand, "rotation", 0.f, 180.f);
            } else {
                animator = ObjectAnimator.ofFloat(btnExpand, "rotation", 180.f, 0.f);
            }
            animator.setDuration(300);
            animator.start();
        }
    }
}
