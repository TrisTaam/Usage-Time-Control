package com.tristaam.usagetimecontrol.Controller.Listener;

public interface FollowAppListener {
    public interface OnClickListener {
        public void onClickDelete(int position);
        public void onClickSave(int position, long limitTime);
    }
}
