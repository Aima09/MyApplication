package com.example.popupwindowdemo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.popupButton)
    Button popupButton;

    private View popupListView;
    private PopupWindow popupWindow;
    private PopupWindowAdapter popupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
       // initPopupWindow();
    }

    public void initPopupWindow() {
        String[] str = {"本地音源", "网络音源", "外部音源", "蓝牙", "话筒输入", "无限话筒输入"};

// TODO: 2016/5/17 构建一个popupwindow的布局
        popupListView = MainActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_list_main, null);

        // TODO: 2016/5/17 为了演示效果，简单的设置了一些数据，实际中大家自己设置数据即可，相信大家都会。
        RecyclerView lsvMore = popupListView.findViewById(R.id.popup_lv);
        popupAdapter = new PopupWindowAdapter(this, str);
        popupAdapter.setItemClickListener(new PopupWindowAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                popupAdapter.setSelectedPosition(position);
                popupAdapter.notifyDataSetChanged();
            }
        });
        lsvMore.setAdapter(popupAdapter);

        // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
        popupWindow = new PopupWindow(popupListView, 400, 600);
        // TODO: 2016/5/17 设置动画
        popupWindow.setAnimationStyle(R.style.popup_window_anim);
        // TODO: 2016/5/17 设置背景颜色
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        // TODO: 2016/5/17 设置可以获取焦点
        popupWindow.setFocusable(true);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        popupWindow.setOutsideTouchable(true);
        // TODO：更新popupwindow的状态
        popupWindow.update();
        // TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
        popupWindow.showAsDropDown(popupButton, 0, 20);
    }

    @OnClick(R.id.popupButton)
    public void onViewClicked(View view) {
        initPopupWindow();

//        if (popupWindow.isShowing()) {
//            popupWindow.dismiss();
//        } else {
//
//            // 设置好参数之后再show
//            popupListView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//            int popupWidth = view.getMeasuredWidth();    //  获取测量后的宽度
//            int popupHeight = view.getMeasuredHeight();  //获取测量后的高度
//            int[] location = new int[2];
//            popupButton.getLocationOnScreen(location);
//            Log.i(TAG, "popupWidth = " + popupWidth + ",popupHeight = " + popupHeight);
//            Log.i(TAG, "location[0] = " + location[0] + ",location[1] = " + location[1]);
////                    int windowPos[] = PopupWindowUtil.calculatePopWindowPos(mPlaySource, popupWindowView);
////                    int xOff = 20; // 可以自己调整偏移
////                    windowPos[0] -= xOff;                                                                                                     //+mPlaySource.getHeight()
//            popupWindow.showAtLocation(popupButton, Gravity.NO_GRAVITY, location[0] - (popupWidth / 2 + popupButton.getWidth()), location[1] - (popupHeight * 4));
//            popupAdapter.setSelectedPosition(0);
//            //弹出的时候选中当前音源
   //    }
    }
}
