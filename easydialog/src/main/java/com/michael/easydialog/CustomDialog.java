package com.michael.easydialog;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CustomDialog extends DialogFragment {

    /**
     * 内容在三角形上面
     */
    public static final int GRAVITY_TOP = 0;
    /**
     * 内容在三角形下面
     */
    public static final int GRAVITY_BOTTOM = 1;
    /**
     * 内容在三角形左面
     */
    public static final int GRAVITY_LEFT = 2;
    /**
     * 内容在三角形右面
     */
    public static final int GRAVITY_RIGHT = 3;
    /**
     * 坐标
     */
    private int[] location;
    /**
     * 提醒框位置
     */
    private int gravity;
    /**
     * 外面传递进来的View
     */
    private View contentView;
    /**
     * 三角形
     */
    private ImageView ivTriangle;
    /**
     * 用来放外面传递进来的View
     */
    private LinearLayout llContent;
    /**
     * 触摸外面，是否关闭对话框
     */
    private boolean touchOutsideDismiss;
    /**
     * 提示框所在的容器
     */
    private RelativeLayout rlOutsideBackground;

    private int cornerRadius = convertPx(5);

    private int layoutResourceId = 0;

    private int showDuration = 350;
    private int dismissDuration = 350;

    private int backgroundColor;

    private int outSideColor = 0;

    /**
     * 对话框所依附的View
     */
    private View attachedView = null;

    private AnimatorSet animatorSetForDialogShow  = new AnimatorSet();
    private AnimatorSet animatorSetForDialogDismiss = new AnimatorSet();

    private List<PropertyValuesHolder> propertyValuesHoldersForDialogShow = new ArrayList<>();
    private List<PropertyValuesHolder> propertyValuesHoldersForDialogDismiss = new ArrayList<>();

    public CustomDialog(){
        ini();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(0,isFullScreen() ? android.R.style.Theme_Translucent_NoTitleBar_Fullscreen : android.R.style.Theme_Translucent_NoTitleBar);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View dialogView = inflater.inflate(R.layout.layout_dialog, container,false);
        ViewTreeObserver viewTreeObserver = dialogView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                //当View可以获取宽高的时候，设置view的位置
                relocation(location);

            }
        });

        rlOutsideBackground = (RelativeLayout) dialogView.findViewById(R.id.rlOutsideBackground);
        setTouchOutsideDismiss(true);
        ivTriangle = (ImageView) dialogView.findViewById(R.id.ivTriangle);
        llContent = (LinearLayout) dialogView.findViewById(R.id.llContent);

        if(layoutResourceId != 0){
            contentView = getActivity().getLayoutInflater().inflate(layoutResourceId, null);
        }
        if(contentView != null){
            llContent.addView(contentView);
        }

        initTouchOutSideDimiss();
        initView();
        return dialogView;
    }

    @Override
    public void onStart() {
        super.onStart();
        onDialogShowing();
    }

    private void initView(){

        /*设置顶部三角形*/
        switch (this.gravity) {
            case GRAVITY_BOTTOM:
                ivTriangle.setBackgroundResource(R.drawable.triangle_bottom);
                break;
            case GRAVITY_TOP:
                ivTriangle.setBackgroundResource(R.drawable.triangle_top);
                break;
            case GRAVITY_LEFT:
                ivTriangle.setBackgroundResource(R.drawable.triangle_left);
                break;
            case GRAVITY_RIGHT:
                ivTriangle.setBackgroundResource(R.drawable.triangle_right);
                break;
        }

        if (attachedView != null) {
            //如果用户调用setGravity()之前就调用过setLocationByAttachedView，需要再调用一次setLocationByAttachedView
            this.setLocationByAttachedView(attachedView);
        }

        /*设置内容背景 跟三角形背景颜色*/
        LayerDrawable drawableTriangle = (LayerDrawable) ivTriangle.getBackground();
        GradientDrawable shapeTriangle = (GradientDrawable) (((RotateDrawable) drawableTriangle.findDrawableByLayerId(R.id.shape_id)).getDrawable());
        if (shapeTriangle != null) {
            shapeTriangle.setColor(backgroundColor);
        } else {
            Toast.makeText(getActivity(), "shape is null", Toast.LENGTH_SHORT).show();
        }

        Drawable tempDrawable = getResources().getDrawable(R.drawable.round_corner_bg);
        GradientDrawable gradientDrawable = (GradientDrawable) tempDrawable;

        gradientDrawable.setCornerRadius(cornerRadius);
        gradientDrawable.setColor(backgroundColor);

        if (Build.VERSION.SDK_INT >= 16) {
            llContent.setBackground(gradientDrawable);
        } else {
            llContent.setBackgroundDrawable(gradientDrawable);
        }

        /*设置内容区域外的颜色*/
        rlOutsideBackground.setBackgroundColor(outSideColor);
    }

    final View.OnTouchListener outsideBackgroundListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (touchOutsideDismiss) {
                onDialogDismiss();
            }
            return false;
        }
    };


    /**
     * 初始化默认值
     */
    private void ini() {
        this.setLocation(new int[]{0, 0})
                .setGravity(GRAVITY_BOTTOM)
                .setTouchOutsideDismiss(true)
                .setOutsideColor(Color.TRANSPARENT)
                .setBackgroundColor(Color.BLUE);
    }

    /**
     * 设置提示框中要显示的内容
     */
    public CustomDialog setLayout(View layout) {
        if (layout != null) {
            this.contentView = layout;
        }
        return this;
    }


    /**
     * 设置提示框中要显示的内容的布局Id
     */
    public CustomDialog setLayoutResourceId(int layoutResourceId) {
        this.layoutResourceId = layoutResourceId;
        return this;
    }

    /**
     * 设置三角形所在的位置
     */
    public CustomDialog setLocation(int[] location) {
        this.location = location;
        return this;
    }

    /**
     * 设置三角形所在的位置
     * location.x坐标值为attachedView所在屏幕位置的中心
     * location.y坐标值依据当前的gravity，如果gravity是top，则为控件上方的y值，如果是bottom，则为控件的下方的y值
     *
     * @param attachedView 在哪个View显示提示信息
     */
    public CustomDialog setLocationByAttachedView(View attachedView) {
        if (attachedView != null) {
            this.attachedView = attachedView;
            int[] attachedViewLocation = new int[2];
            attachedView.getLocationOnScreen(attachedViewLocation);
            switch (gravity) {
                case GRAVITY_BOTTOM:
                    attachedViewLocation[0] += attachedView.getWidth() / 2;
                    attachedViewLocation[1] += attachedView.getHeight();
                    break;
                case GRAVITY_TOP:
                    attachedViewLocation[0] += attachedView.getWidth() / 2;
                    break;
                case GRAVITY_LEFT:
                    attachedViewLocation[1] += attachedView.getHeight() / 2;
                    break;
                case GRAVITY_RIGHT:
                    attachedViewLocation[0] += attachedView.getWidth();
                    attachedViewLocation[1] += attachedView.getHeight() / 2;
            }
            setLocation(attachedViewLocation);
        }
        return this;
    }


    /**
     * 设置显示的内容在上方还是下方，如果设置错误，默认是在下方
     */
    public CustomDialog setGravity(int gravity) {
        if (gravity != GRAVITY_BOTTOM && gravity != GRAVITY_TOP && gravity != GRAVITY_LEFT && gravity != GRAVITY_RIGHT)
        {
            gravity = GRAVITY_BOTTOM;
        }
        this.gravity = gravity;
        return this;
    }

    /**
     * 设置触摸对话框外面，对话框是否消失
     */
    public CustomDialog setTouchOutsideDismiss(boolean touchOutsideDismiss) {
        this.touchOutsideDismiss = touchOutsideDismiss;
        return this;
    }

    private void initTouchOutSideDimiss(){
        if(touchOutsideDismiss) {
            rlOutsideBackground.setOnTouchListener(outsideBackgroundListener);
        }
        else {
            rlOutsideBackground.setOnTouchListener(null);
        }
    }
    /**
     * 设置提醒框外部区域的颜色
     */
    public CustomDialog setOutsideColor(int color) {
        this.outSideColor = color;
        return this;
    }



    /**
     * 设置对话框的颜色
     * 三角形的图片是layer-list里面嵌套一个RotateDrawable，在设置颜色的时候需要特别处理
     * http://stackoverflow.com/questions/24492000/set-color-of-triangle-on-run-time
     * http://stackoverflow.com/questions/16636412/change-shape-solid-color-at-runtime-inside-drawable-xml-used-as-background
     */
    public CustomDialog setBackgroundColor(int color) {
        backgroundColor = color;
        return this;
    }

    /**
     *
     * @param cornerRadius in pixel value
     * @return dialog instance
     */
    public CustomDialog setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        return this;
    }



    public CustomDialog setShowAniamtion(String propertyName, float... values){
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat(propertyName,values);
        propertyValuesHoldersForDialogShow.add(valuesHolder);
        return this;
    }

    public CustomDialog setDismissAnimation(String propertyName, float... values){
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat(propertyName,values);
        propertyValuesHoldersForDialogDismiss.add(valuesHolder);
        return this;
    }

    public CustomDialog setShowDuration(int  showDuration){
        this.showDuration = showDuration;
        return this;
    }

    public CustomDialog setDissMissDuration(int dissMissDuration){
        this.dismissDuration = dissMissDuration;
        return this;
    }



    /**
     * 根据x，y，重新设置控件的位置
     * 因为setX setY为0的时候，都是在状态栏以下的，所以app不是全屏的话，需要扣掉状态栏的高度
     */
    private void relocation(int[] location) {
        float statusBarHeight = isFullScreen() ? 0.0f : getStatusBarHeight();

        ivTriangle.setX(location[0] - ivTriangle.getWidth() / 2);
        ivTriangle.setY(location[1] - ivTriangle.getHeight() / 2 - statusBarHeight);
        switch (gravity) {
            case GRAVITY_BOTTOM:
                llContent.setY(location[1] - ivTriangle.getHeight() / 2 - statusBarHeight + ivTriangle.getHeight());
                break;
            case GRAVITY_TOP:
                llContent.setY(location[1] - llContent.getHeight() - statusBarHeight - ivTriangle.getHeight() / 2);
                break;
            case GRAVITY_LEFT:
                llContent.setX(location[0] - llContent.getWidth() - ivTriangle.getWidth() / 2);
                break;
            case GRAVITY_RIGHT:
                llContent.setX(location[0] + ivTriangle.getWidth() / 2);
                break;
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llContent.getLayoutParams();
        switch (gravity) {
            case GRAVITY_BOTTOM:
            case GRAVITY_TOP:
                int triangleCenterX = (int) (ivTriangle.getX() + ivTriangle.getWidth() / 2);
                int contentWidth = llContent.getWidth();
                int rightMargin = getScreenWidth() - triangleCenterX;
                int leftMargin = getScreenWidth() - rightMargin;
                int availableLeftMargin = leftMargin - layoutParams.leftMargin;
                int availableRightMargin = rightMargin - layoutParams.rightMargin;
                int x = 0;
                if (contentWidth / 2 <= availableLeftMargin && contentWidth / 2 <= availableRightMargin) {
                    x = triangleCenterX - contentWidth / 2;
                } else {
                    if (availableLeftMargin <= availableRightMargin) {
                        x = layoutParams.leftMargin;
                    } else {
                        x = getScreenWidth() - (contentWidth + layoutParams.rightMargin);
                    }
                }
                llContent.setX(x);
                break;
            case GRAVITY_LEFT:
            case GRAVITY_RIGHT:
                int triangleCenterY = (int) (ivTriangle.getY() + ivTriangle.getHeight() / 2);
                int contentHeight = llContent.getHeight();
                int topMargin = triangleCenterY;
                int bottomMargin = getScreenHeight() - topMargin;
                int availableTopMargin = topMargin - layoutParams.topMargin;
                int availableBottomMargin = bottomMargin - layoutParams.bottomMargin;
                int y = 0;
                if (contentHeight / 2 <= availableTopMargin && contentHeight / 2 <= availableBottomMargin) {
                    y = triangleCenterY - contentHeight / 2;
                } else {
                    if (availableTopMargin <= availableBottomMargin) {
                        y = layoutParams.topMargin;
                    } else {
                        y = getScreenHeight() - (contentHeight + layoutParams.topMargin);
                    }
                }
                llContent.setY(y);
                break;
        }
    }

    /**
     * 获取屏幕的宽度
     */
    private int getScreenWidth() {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    private int getScreenHeight() {
        int statusBarHeight = isFullScreen() ? 0 : getStatusBarHeight();
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return metrics.heightPixels - statusBarHeight;
    }

    /**
     * 获取状态栏的高度
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getActivity().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getActivity().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 判断下当前要显示对话框的Activity是否是全屏
     */
    public boolean isFullScreen() {
        int flg = getActivity().getWindow().getAttributes().flags;
        boolean flag = false;
        if ((flg & 1024) == 1024) {
            flag = true;
        }
        return flag;
    }


    private int convertPx(int valueInPixels) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInPixels, metrics);
    }


    /*动画效果解决 明天再优化一下 就OK了*/
    private void onDialogShowing() {
        if(propertyValuesHoldersForDialogShow.size() == 0){
            return;
        }
        List<Animator> animators = new ArrayList<>();
        for(PropertyValuesHolder propertyValuesHolder:propertyValuesHoldersForDialogShow){
            ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(rlOutsideBackground.findViewById(R.id.rlParentForAnimate).findViewById(R.id.rlParentForAnimate),propertyValuesHolder);
            animators.add(animator);
        }

        animatorSetForDialogShow.playTogether(animators);
        animatorSetForDialogShow.setDuration(showDuration);
        animatorSetForDialogShow.start();
//        ObjectAnimator.ofFloat(rlOutsideBackground.findViewById(R.id.rlParentForAnimate), "translationX", 300, -100,0).setDuration(500).start();
        //TODO 缩放的动画效果不好，不能从控件所在的位置开始缩放
//        ObjectAnimator.ofFloat(rlOutsideBackground.findViewById(R.id.rlParentForAnimate), "scaleX", 0.3f, 1.0f).setDuration(500).start();
//        ObjectAnimator.ofFloat(rlOutsideBackground.findViewById(R.id.rlParentForAnimate), "scaleY", 0.3f, 1.0f).setDuration(500).start();
    }

    @SuppressLint("NewApi")
    private void onDialogDismiss() {

        if (animatorSetForDialogDismiss.isRunning()) {
            return;
        }

        if (animatorSetForDialogDismiss != null && propertyValuesHoldersForDialogDismiss != null && propertyValuesHoldersForDialogDismiss.size() > 0) {
            List<Animator> animators = new ArrayList<>();
            for(PropertyValuesHolder propertyValuesHolder:propertyValuesHoldersForDialogDismiss){
                ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(rlOutsideBackground.findViewById(R.id.rlParentForAnimate).findViewById(R.id.rlParentForAnimate),propertyValuesHolder);
                animators.add(animator);
            }
            animatorSetForDialogDismiss.playTogether(animators);
            animatorSetForDialogDismiss.setDuration(dismissDuration);
            animatorSetForDialogDismiss.start();
            animatorSetForDialogDismiss.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) { }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //这里有可能会有bug，当Dialog所依赖的Activity关闭的时候，如果这个时候，用户关闭对话框，由于对话框的动画关闭需要时间，当动画执行完毕后，对话框所依赖的Activity已经被销毁了，执行dismiss()就会报错
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!getActivity().isDestroyed()) {
                            dismiss();
                        }
                    } else {
                        try {
                            dismiss();
                        } catch (final IllegalArgumentException e) {

                        } catch (final Exception e) { } finally { }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            dismiss();
        }
    }
}
