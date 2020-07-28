


![](demo.gif)




<br/>

```java
     View view = this.getLayoutInflater().inflate(R.layout.layout_tip_content_horizontal, null);
     new EasyDialog(MainActivity.this)
     // .setLayoutResourceId(R.layout.layout_tip_content_horizontal)//layout resource id
        .setLayout(view)
        .setBackgroundColor(MainActivity.this.getResources().getColor(R.color.background_color_black))
     // .setLocation(new location[])//point in screen
        .setLocationByAttachedView(btnTopLeft)
        .setGravity(EasyDialog.GRAVITY_BOTTOM)
        .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 1000, -600, 100, -50, 50, 0)
        .setAnimationAlphaShow(1000, 0.3f, 1.0f)
        .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, -50, 800)
        .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
        .setTouchOutsideDismiss(true)
        .setMatchParent(true)
        .setMarginLeftAndRight(24, 24)
        .setOutsideColor(MainActivity.this.getResources().getColor(R.color.outside_color_trans))
        .show();
```

```java
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
                        
                        customDialog.show(getFragmentManager(),"");
```

## License

Use and distribution of licensed under the Apache2.0 license. See the [LICENSE](https://github.com/michaelye/EasyDialog/blob/master/LICENSE) file for full text.





