package com.simplemobiletools.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;

public class RemoteControlReceiver extends BroadcastReceiver {
    private static final int MAX_CLICK_DURATION = 700;

    private static Context mContext;
    private static Handler mHandler = new Handler();

    private static int mClicksCnt;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            final KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event.getAction() == KeyEvent.ACTION_UP && KeyEvent.KEYCODE_HEADSETHOOK == event.getKeyCode()) {
                mClicksCnt++;

                mHandler.removeCallbacks(runnable);
                if (mClicksCnt >= 3) {
                    mHandler.post(runnable);
                } else {
                    mHandler.postDelayed(runnable, MAX_CLICK_DURATION);
                }
            }
        }
    }

    private static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mClicksCnt == 0)
                return;

            if (mClicksCnt == 1) {
                Utils.sendIntent(mContext, Constants.PLAYPAUSE);
            } else if (mClicksCnt == 2) {
                Utils.sendIntent(mContext, Constants.NEXT);
            } else {
                Utils.sendIntent(mContext, Constants.PREVIOUS);
            }
            mClicksCnt = 0;
        }
    };
}