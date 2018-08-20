package com.nd.hilauncherdev.dynamic.Transfer;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nd.hilauncherdev.dynamic.R;
import com.nd.hilauncherdev.kitset.util.TelephoneUtil;


/**
 * Created by linliangbin on 2016/11/10.
 */

public class PluginDownloadAnim extends RelativeLayout {

    private Bitmap ground;
    private Bitmap stone;
    private Bitmap smoke;

    private Bitmap cat_body;
    private Bitmap cat_hand;
    private Bitmap cat_tail;

    private Bitmap car_wheel;
    private Bitmap car_body;

    ImageView iv_ground,iv_stone,iv_smoke;
    ImageView iv_cat_body,iv_cat_hand,iv_cat_tail;
    ImageView iv_car_wheel_front,iv_car_wheel_rear,iv_car_body;


    private Animation smokeAnimation;
    private ObjectAnimator frontWheelAnimator,rearWhellAnimator;

    int step_time = 10;
    int step_length = 2;
    int repeat_wait = 2000;
    public static final int MSG_WHAT = 1000;

    /** 当前是否在播放动画，避免重复播放动画问题 */
    private boolean isPlayingAnim = false;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_WHAT){

                int progress = 0;
                if(msg.obj instanceof Integer){
                    Integer integer = ((Integer) msg.obj);
                    if(integer != null){
                        progress = integer.intValue();
                    }
                }
                moveStone(progress);
                int newProgress = progress;
                int delayTime = step_time;
                if(progress <= 100){
                    newProgress = progress + step_length;
                }else{
                    reset();
                    newProgress = 0;
                    delayTime = repeat_wait;
                }
                Message message = obtainMessage();
                message.what = MSG_WHAT;
                message.obj = new Integer(newProgress);
                handler.sendMessageDelayed(message,delayTime);
            }

        }
    };

    public Bitmap getGround() {
        if(ground == null || !ground.isRecycled()){
            ground = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_plugin_download_ground);
        }
        return ground;
    }

    public Bitmap getSmoke() {
        if(smoke == null || smoke.isRecycled()){
            smoke = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_plugin_download_smoke);
        }
        return smoke;
    }

    public Bitmap getCat_body() {
        if(cat_body == null || cat_body.isRecycled()){
            cat_body = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_plugin_download_cat);
        }
        return cat_body;
    }

    public Bitmap getCat_hand() {
        if(cat_hand == null || cat_hand.isRecycled()){
            cat_hand = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_plugin_download_hand);
        }
        return cat_hand;
    }

    public Bitmap getCat_tail() {
        if(cat_tail == null || cat_tail.isRecycled()){
            cat_tail = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_plugin_download_tail);
        }
        return cat_tail;
    }

    public Bitmap getCar_wheel() {
        if(car_wheel == null || car_wheel.isRecycled()){
            car_wheel = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_plugin_download_car_wheel);
        }
        return car_wheel;
    }

    public Bitmap getCar_body() {
        if(car_body == null || car_body.isRecycled()){
            car_body = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_plugin_download_car_body);
        }
        return car_body;
    }

    public Bitmap getStone() {
        if(stone == null || stone.isRecycled()){
            stone = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_plugin_download_stone);
        }
        return stone;
    }


    public PluginDownloadAnim(Context context) {
        super(context);
        init();
    }

    public PluginDownloadAnim(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }





    private void init(){
        iv_stone = new ImageView(getContext());
        iv_stone.setImageBitmap(getStone());
        iv_stone.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(iv_stone);

        iv_ground = new ImageView(getContext());
        iv_ground.setImageBitmap(getGround());
        iv_ground.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(iv_ground);


        iv_smoke = new ImageView(getContext());
        iv_smoke.setImageBitmap(getSmoke());
        iv_smoke.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(iv_smoke);

        iv_car_wheel_front = new ImageView(getContext());
        iv_car_wheel_front.setImageBitmap(getCar_wheel());
        iv_car_wheel_front.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(iv_car_wheel_front);

        iv_car_wheel_rear = new ImageView(getContext());
        iv_car_wheel_rear.setImageBitmap(getCar_wheel());
        iv_car_wheel_rear.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(iv_car_wheel_rear);

        iv_car_body = new ImageView(getContext());
        iv_car_body.setImageBitmap(getCar_body());
        iv_car_body.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(iv_car_body);

        iv_cat_tail = new ImageView(getContext());
        iv_cat_tail.setImageBitmap(getCat_tail());
        iv_cat_tail.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(iv_cat_tail);

        iv_cat_body = new ImageView(getContext());
        iv_cat_body.setImageBitmap(getCat_body());
        iv_cat_body.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(iv_cat_body);

        iv_cat_hand = new ImageView(getContext());
        iv_cat_hand.setImageBitmap(getCat_hand());
        iv_cat_hand.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(iv_cat_hand);

    }


    float step = 1.0f;
    int l,t,r,b;
    int width,height;
    float viewScale = 1.0f;
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        try {
            width = r - l;
            height = b - t;
            this.l = l;
            this.t = t;
            this.r = r;
            this.b = b;
            int left,top,right,bottom;
            width = (int) (width*0.7);
            viewScale = (float)width/(float)getGround().getWidth();

            /** 地面 */
            left = (int) (width * 0.2);
            bottom = (int) (b);
            top = (int) (bottom - getGround().getHeight());
            right = (int) (left + getGround().getWidth()*viewScale);
            iv_ground.layout(left,top,right,bottom);

            /** 石头 */
            left = (int) (iv_ground.getLeft() * 1.1);
            bottom = (int) (iv_ground.getTop() + iv_ground.getHeight()*0.7);
            top = (int) (bottom - viewScale * getStone().getHeight());
            right = (int) (left + viewScale * getStone().getWidth());
            iv_stone.layout(left,top,right,bottom);
            iv_stone.setVisibility(VISIBLE);
            step = (float) ((iv_ground.getWidth()*0.95 - 3*(iv_ground.getLeft() * 0.1)) / 100f);

            /** 前轮 */
            float wheel_suojin = 0.2f;
            left = (int) (iv_ground.getLeft() +  getCar_wheel().getWidth() *wheel_suojin*viewScale);
            bottom = iv_ground.getTop();
            top = (int) (bottom - viewScale * getCar_wheel().getHeight());
            right = (int) (left + viewScale* getCar_wheel().getWidth());
            iv_car_wheel_front.layout(left,top,right,bottom);

            /** 后轮 */
            left = (int) ((int) (iv_ground.getRight() - getCar_wheel().getWidth() *(wheel_suojin+1) *viewScale ) * 0.95);
            top = iv_car_wheel_front.getTop();
            right = (int) (left + viewScale * getCar_wheel().getWidth());
            bottom = iv_car_wheel_front.getBottom();
            iv_car_wheel_rear.layout(left,top,right,bottom);

            /** 车身 */
            left = (int) (iv_car_wheel_front.getLeft()*1.05);
            bottom = (int) (iv_car_wheel_front.getTop() + iv_car_wheel_front.getHeight() * 0.6);
            right = (int) (left + viewScale*getCar_body().getWidth());
            top = (int) (bottom - getCar_body().getHeight()*viewScale);
            iv_car_body.layout(left,top,right,bottom);


            /** 猫身体 */
            left = (int) ((iv_car_body.getLeft() + iv_car_body.getWidth()/2)*0.9);
            bottom = (int) (iv_car_body.getTop() + iv_car_body.getHeight()* 0.7);
            right = (int) (left + viewScale * getCat_body().getWidth());
            top = (int) (bottom - viewScale * getCat_body().getHeight());
            iv_cat_body.layout(left,top,right,bottom);

            /** 烟 */
            left = iv_car_wheel_rear.getRight();
            bottom = (int) (iv_car_wheel_rear.getTop() + iv_car_wheel_rear.getHeight() * 0.9);
            right = (int) (left + viewScale * getSmoke().getWidth());
            top = (int) (bottom - viewScale * getSmoke().getHeight());
            iv_smoke.layout(left,top,right,bottom);

            /** 猫手 */
            left = (int) (iv_cat_body.getLeft() * 0.9);
            bottom = (int) (iv_cat_body.getTop() + iv_cat_body.getHeight()/2 *1.4 );
            right = (int) (left + viewScale * getCat_hand().getWidth());
            top = (int) (bottom - viewScale * getCat_hand().getHeight());
            iv_cat_hand.layout(left,top,right,bottom);

            /** 猫尾巴 */
            left = (int) (iv_cat_body.getRight() * 0.945);
            bottom = (int) (iv_cat_body.getBottom() * 0.9);
            right = (int) (left + viewScale * getCat_tail().getWidth());
            top = (int) (bottom - viewScale * getCat_tail().getHeight());
            iv_cat_tail.layout(left,top,right,bottom);

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    boolean shakeFront = false;
    boolean shakeRear = false;

    public  void moveStone(int progress){

        if(progress > 0 && iv_stone != null && iv_stone.getVisibility() != VISIBLE){
            iv_stone.setVisibility(VISIBLE);
        }
        int left,top,right,bottom;
        left = (int) (iv_ground.getLeft() * 1.1+ progress * step);
        bottom = iv_stone.getBottom();
        top = iv_stone.getTop();
        right = (int) (left + viewScale * getStone().getWidth());

        iv_stone.layout(left,top,right,bottom);

        if(!shakeFront  && left != 0 && Math.abs(left - (iv_car_wheel_front.getLeft() + iv_car_wheel_front.getWidth()/2)) < 2.5 *viewScale*getStone().getWidth() ){
            shakeFront = true;
            startShakeCar(true);
        }

        if(!shakeRear && left != 0 && Math.abs(left - (iv_car_wheel_rear.getLeft() + iv_car_wheel_rear.getWidth()/2)) < 2.5*viewScale *getStone().getWidth()){
            shakeRear = true;
            startShakeCar(false);
        }
    }

    AnimatorSet frontShakeCarAnimatorSet,rearShakeCarAnimatorSet;
    public void startShakeCar(boolean isFront){
        int timeLong = 700;
        ObjectAnimator catShakeY = ObjectAnimator.ofFloat(iv_cat_body,"translationY",0,0);
        catShakeY.setEvaluator(new IncreaseDecreaseEvaluator(-((float)(viewScale * getCar_body().getHeight()*0.04))));
        catShakeY.setInterpolator(new AccelerateDecelerateInterpolator());
        catShakeY.setDuration(timeLong);

        ObjectAnimator catScaleY = ObjectAnimator.ofFloat(iv_cat_body,"scaleY",1.0f,1.0f);
        catScaleY.setEvaluator(new IncreaseDecreaseEvaluator(0.05f));
        catScaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        catScaleY.setDuration(timeLong);

        ObjectAnimator catHandShake = ObjectAnimator.ofFloat(iv_cat_hand,"translationY",0,0);
        catHandShake.setEvaluator(new IncreaseDecreaseEvaluator(-((float)(viewScale*getCar_body().getHeight()*0.1))));
        catHandShake.setDuration(timeLong);
        catHandShake.setInterpolator(new AccelerateDecelerateInterpolator());


        ObjectAnimator catTailShake = ObjectAnimator.ofFloat(iv_cat_tail,"translationY",0,0);
        catTailShake.setEvaluator(new IncreaseDecreaseEvaluator(-((float)(viewScale*getCar_body().getHeight()*0.1))));
        catTailShake.setDuration(timeLong);
        catTailShake.setInterpolator(new AccelerateDecelerateInterpolator());

        if(TelephoneUtil.getApiLevel() > 11){
            iv_car_body.setPivotX(iv_car_body.getWidth()/2);
            iv_car_body.setPivotY(iv_car_body.getHeight()/2);
        }
        ObjectAnimator carScale = ObjectAnimator.ofFloat(iv_car_body,"scaleY",1.0f,1.0f);
        carScale.setDuration(timeLong);
        carScale.setInterpolator(new AccelerateDecelerateInterpolator());
        if(isFront){
            carScale.setEvaluator(new IncreaseDecreaseEvaluator(0.03f));
        }else{
            carScale.setEvaluator(new IncreaseDecreaseEvaluator(0.02f));
        }

        ObjectAnimator carTranslate = ObjectAnimator.ofFloat(iv_car_body,"translationY",0,0);
        carTranslate.setDuration(timeLong);
        carTranslate.setEvaluator(new IncreaseDecreaseEvaluator(-((float)(viewScale*getStone().getHeight()*0.2))));
        carTranslate.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator carRotate = ObjectAnimator.ofFloat(iv_car_body,"rotation",0.0f,0.0f);
        carRotate.setDuration(timeLong);
        carRotate.setInterpolator(new AccelerateDecelerateInterpolator());
        if(isFront){
            carRotate.setEvaluator(new IncreaseDecreaseEvaluator(((float)(0.5))));
            if(TelephoneUtil.getApiLevel() > 11){
                iv_car_body.setPivotX(iv_car_body.getWidth());
                iv_car_body.setPivotY(iv_car_body.getHeight());
            }
        }else{
            carRotate.setEvaluator(new IncreaseDecreaseEvaluator(-((float)(2))));
            if(TelephoneUtil.getApiLevel() > 11){
                iv_car_body.setPivotX(0);
                iv_car_body.setPivotY(iv_car_body.getHeight());
            }
        }

        ObjectAnimator carWheelShake;
        if(isFront){
            carWheelShake = ObjectAnimator.ofFloat(iv_car_wheel_front,"translationY",0,0);
            carWheelShake.setEvaluator(new IncreaseDecreaseEvaluator(-viewScale*getStone().getHeight()*0.5f));
        }else{
            carWheelShake = ObjectAnimator.ofFloat(iv_car_wheel_rear,"translationY",0,0);
            carWheelShake.setEvaluator(new IncreaseDecreaseEvaluator(-viewScale*getStone().getHeight()*0.5f));
        }
        carWheelShake.setInterpolator(new AccelerateDecelerateInterpolator());
        carWheelShake.setDuration(timeLong);

        if(isFront){
            frontShakeCarAnimatorSet = new AnimatorSet();
            frontShakeCarAnimatorSet.playTogether(carScale,carTranslate,carRotate,catShakeY,catScaleY,catHandShake,catTailShake,carWheelShake);
            frontShakeCarAnimatorSet.start();
        }else{
            rearShakeCarAnimatorSet = new AnimatorSet();
            rearShakeCarAnimatorSet.playTogether(carScale,carTranslate,carRotate,catShakeY,catScaleY,catHandShake,catTailShake,carWheelShake);
            rearShakeCarAnimatorSet.start();
        }

    }

    public void startWheelRotate(){
        frontWheelAnimator = ObjectAnimator.ofFloat(iv_car_wheel_front,"rotation",0,-360);
        frontWheelAnimator.setRepeatCount(ValueAnimator.INFINITE);
        frontWheelAnimator.setRepeatMode(ValueAnimator.RESTART);
        frontWheelAnimator.setInterpolator(new LinearInterpolator());
        frontWheelAnimator.setDuration(1500);
        frontWheelAnimator.start();


        rearWhellAnimator = ObjectAnimator.ofFloat(iv_car_wheel_rear,"rotation",0,-360);
        rearWhellAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rearWhellAnimator.setRepeatMode(ValueAnimator.RESTART);
        rearWhellAnimator.setInterpolator(new LinearInterpolator());
        rearWhellAnimator.setDuration(1500);
        rearWhellAnimator.start();

    }

    public void reset(){
        shakeFront = false;
        shakeRear = false;
    }

    public void startAnim(){
        if(isPlayingAnim){
            return;
        }
        startSmoke();
        startWheelRotate();
        Message message = handler.obtainMessage();
        message.what = MSG_WHAT;
        message.obj = new Integer(0);
        handler.sendMessage(message);
        isPlayingAnim = true;
    }

    public void stopAnim(){
        try {
            if(handler != null){
                handler.removeMessages(MSG_WHAT);
                handler.removeCallbacks(null);
            }
            if(frontShakeCarAnimatorSet != null && rearShakeCarAnimatorSet.isStarted()){
                frontShakeCarAnimatorSet.end();
            }
            if(rearShakeCarAnimatorSet != null && rearShakeCarAnimatorSet.isStarted()){
                rearShakeCarAnimatorSet.end();
            }
            if(iv_smoke != null){
                iv_smoke.clearAnimation();
            }
            if(frontWheelAnimator != null){
                frontWheelAnimator.cancel();
            }
            if(rearWhellAnimator != null){
                rearWhellAnimator.cancel();
            }
            isPlayingAnim = false;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void startSmoke(){
        smokeAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.plugin_download_car_smoke);
        smokeAnimation.setInterpolator(new LinearInterpolator());
        smokeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv_smoke.startAnimation(smokeAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        iv_smoke.startAnimation(smokeAnimation);
    }


    /** 从0到MaxValue 又从MaxValue 到 0*/
    public static class IncreaseDecreaseEvaluator implements TypeEvaluator {
        float maxValue = 1;

        IncreaseDecreaseEvaluator(float maxValue) {
            this.maxValue = maxValue;
        }

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            float start = ((Number) startValue).floatValue();
            float end = ((Number) endValue).floatValue();
            return start + Math.sin(fraction * Math.PI)*maxValue;

        }

    }

}
