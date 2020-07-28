package com.michael.easydialogdemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.michael.easydialog.CustomDialog;
import com.michael.easydialog.EasyDialog;


/**
 * This class shows how to use EasyDialog
 * <p/>
 * Created by michael on 15/4/15.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener
{
    private RelativeLayout rlBackground;
    private Button btnTopLeft;
    private Button btnTopRight;
    private Button btnMiddleTop;
    private Button btnMiddleLeft;
    private Button btnMiddleRight;
    private Button btnMiddleBottom;
    private Button btnBottomLeft;
    private Button btnBottomRight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniComponent();
    }

    private void iniComponent()
    {
        rlBackground = (RelativeLayout) findViewById(R.id.rlBackground);
        btnTopLeft = (Button) findViewById(R.id.btnTopLeft);
        btnTopRight = (Button) findViewById(R.id.btnTopRight);
        btnMiddleTop = (Button) findViewById(R.id.btnMiddleTop);
        btnMiddleLeft = (Button) findViewById(R.id.btnMiddleLeft);
        btnMiddleRight = (Button) findViewById(R.id.btnMiddleRight);
        btnMiddleBottom = (Button) findViewById(R.id.btnMiddleBottom);
        btnBottomLeft = (Button) findViewById(R.id.btnBottomLeft);
        btnBottomRight = (Button) findViewById(R.id.btnBottomRight);

        btnTopLeft.setOnClickListener(this);
        btnTopRight.setOnClickListener(this);
        btnMiddleTop.setOnClickListener(this);
        btnMiddleLeft.setOnClickListener(this);
        btnMiddleRight.setOnClickListener(this);
        btnMiddleBottom.setOnClickListener(this);
        btnBottomLeft.setOnClickListener(this);
        btnBottomRight.setOnClickListener(this);
        rlBackground.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                int[] location = new int[2];
                location[0] = (int) event.getX();
                location[1] = (int) event.getY();
                location[1] = location[1] + getActionBarHeight() + getStatusBarHeight();
                Toast.makeText(MainActivity.this, "x:" + location[0] + " y:" + location[1], Toast.LENGTH_SHORT).show();

//                View easyView = MainActivity.this.getLayoutInflater().inflate(R.layout.layout_tip_list_view, null);

                CustomDialog customDialog = new CustomDialog()
//                        .setLayout(easyView)
                        .setLayoutResourceId(R.layout.layout_tip_content_horizontal)
                        .setBackgroundColor(MainActivity.this.getResources().getColor(R.color.background_color_blue))
                        .setLocation(location)
                        .setGravity(EasyDialog.GRAVITY_BOTTOM)
                        .setTouchOutsideDismiss(true)
                        .setOutsideColor(MainActivity.this.getResources().getColor(R.color.transparent_background));

                customDialog.show(getFragmentManager(),"");

//                ListView listView = (ListView) easyView.findViewById(R.id.lvList);
//                List<String> items = new ArrayList<String>();
//                for(int i = 0; i < 20; i++)
//                {
//                    items.add(""+i);
//                }
//                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items);
//                listView.setAdapter(itemsAdapter);

                return false;
            }
        });
    }

    @Override
    public void onClick(View v)
    {

        CustomDialog customDialog = null;
        switch (v.getId())
        {
            case R.id.btnTopLeft:
                View view = this.getLayoutInflater().inflate(R.layout.layout_tip_content_horizontal, null);
                customDialog = new CustomDialog()
//                        .setLayoutResourceId(R.layout.layout_tip_content_horizontal)//layout resource id
                        .setLayout(view)
                        .setBackgroundColor(MainActivity.this.getResources().getColor(R.color.background_color_black))
//                        .setLocation(new location[])//point in screen
                        .setLocationByAttachedView(btnTopLeft)
                        .setGravity(EasyDialog.GRAVITY_BOTTOM)
                        .setShowAniamtion("translationX", -600, 100, -50, 50, 0)
                        .setShowAniamtion("alpha",0.3f, 1.0f)
                        .setDismissAnimation("translationX",-50, 800)
                        .setDismissAnimation("alpha",1.0f, 0.0f)
                        .setTouchOutsideDismiss(true)
                        .setOutsideColor(MainActivity.this.getResources().getColor(R.color.outside_color_trans));

                break;

            case R.id.btnTopRight:
                customDialog = new CustomDialog()
                        .setLayoutResourceId(R.layout.layout_tip_image_text)
                        .setGravity(EasyDialog.GRAVITY_BOTTOM)
                        .setBackgroundColor(MainActivity.this.getResources().getColor(R.color.background_color_black))
                        .setLocationByAttachedView(btnTopRight)
                        .setShowAniamtion("translationX", 400, 0)
                        .setDismissAnimation("translationX",0, 400)
                        .setTouchOutsideDismiss(true)
                        .setOutsideColor(MainActivity.this.getResources().getColor(R.color.outside_color_trans));

                break;
            case R.id.btnMiddleTop:
                customDialog = new CustomDialog()
                        .setLayoutResourceId(R.layout.layout_tip_content_horizontal)
                        .setBackgroundColor(MainActivity.this.getResources().getColor(R.color.background_color_blue))
                        .setLocationByAttachedView(btnMiddleTop)
                        .setShowAniamtion("translationY", -800, 100, -50, 50, 0)
                        .setShowAniamtion("scaleX",0.5f,1f)
                        .setDismissAnimation("translationY",0, -800)
                        .setGravity(EasyDialog.GRAVITY_TOP)
                        .setTouchOutsideDismiss(true)
                        .setOutsideColor(MainActivity.this.getResources().getColor(R.color.transparent_background));
                break;
            case R.id.btnMiddleLeft:
                customDialog = new CustomDialog()
                        .setLayoutResourceId(R.layout.layout_tip_text)
                        .setBackgroundColor(MainActivity.this.getResources().getColor(R.color.background_color_purple))
                        .setLocationByAttachedView(btnMiddleLeft)
                        .setGravity(EasyDialog.GRAVITY_RIGHT)
                        .setShowAniamtion("alpha",0.0f, 1.0f)
                        .setDismissAnimation("alpha",1.0f, 0.0f)
                        .setTouchOutsideDismiss(true)
                        .setOutsideColor(MainActivity.this.getResources().getColor(R.color.outside_color_gray));
                break;
            case R.id.btnMiddleRight:
                customDialog = new CustomDialog()
                        .setLayoutResourceId(R.layout.layout_tip_text)
                        .setBackgroundColor(MainActivity.this.getResources().getColor(R.color.background_color_red))
                        .setLocationByAttachedView(btnMiddleRight)
                        .setGravity(EasyDialog.GRAVITY_LEFT)
                        .setShowAniamtion("alpha",0.0f, 1.0f)
                        .setDismissAnimation("alpha",1.0f, 0.0f)
                        .setTouchOutsideDismiss(true)
                        .setOutsideColor(MainActivity.this.getResources().getColor(R.color.outside_color_gray));
                break;
            case R.id.btnMiddleBottom:
                customDialog = new CustomDialog()
                        .setLayoutResourceId(R.layout.layout_tip_content_horizontal)
                        .setGravity(EasyDialog.GRAVITY_BOTTOM)
                        .setBackgroundColor(MainActivity.this.getResources().getColor(R.color.background_color_brown))
                        .setLocationByAttachedView(btnMiddleBottom)
                        .setShowAniamtion("translationY", -100, -50, 50, 0)
                        .setShowAniamtion("alpha",0.3f, 1.0f)
                        .setDismissAnimation("translationY",0, 800)
                        .setTouchOutsideDismiss(true)
                        .setOutsideColor(MainActivity.this.getResources().getColor(R.color.outside_color_gray));
                break;
            case R.id.btnBottomLeft:
                customDialog = new CustomDialog()
                        .setLayoutResourceId(R.layout.layout_tip_text)
                        .setBackgroundColor(MainActivity.this.getResources().getColor(R.color.background_color_pink))
                        .setLocationByAttachedView(btnBottomLeft)
                        .setGravity(EasyDialog.GRAVITY_TOP)
                        .setShowAniamtion("alpha",0.0f, 1.0f)
                        .setDismissAnimation("alpha",1.0f, 0.0f)
                        .setTouchOutsideDismiss(true)
                        .setOutsideColor(MainActivity.this.getResources().getColor(R.color.outside_color_trans));
                break;
            case R.id.btnBottomRight:
                customDialog = new CustomDialog()
                        .setLayoutResourceId(R.layout.layout_tip_image_text)
                        .setBackgroundColor(MainActivity.this.getResources().getColor(R.color.background_color_yellow))
                        .setLocationByAttachedView(btnBottomRight)
                        .setGravity(EasyDialog.GRAVITY_TOP)
                        .setShowAniamtion("translationX",400, 0)
                        .setShowAniamtion("translationY",  400, 0)
                        .setDismissAnimation("translationX", 0, 400)
                        .setDismissAnimation("translationY",0, 400)
                        .setTouchOutsideDismiss(true)
                        .setOutsideColor(MainActivity.this.getResources().getColor(R.color.outside_color_trans));
                break;
        }


        customDialog.show(getFragmentManager(),"");
    }

    private int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getActionBarHeight()
    {
        return this.getSupportActionBar().getHeight();
    }
}

