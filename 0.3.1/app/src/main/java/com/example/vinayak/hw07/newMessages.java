package com.example.vinayak.hw07;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;


public class newMessages extends AppCompatActivity implements View.OnClickListener{
    private TextView topBar;
    private TextView tabDeal;
    private TextView tabPoi;
    private TextView tabUser;
    private SearchView searchView;

    private FrameLayout ly_content;

    private OnlyRecommendation f1;
    private OnlyGroups f2;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        bindView();
        tabDeal.performClick();

    }

    //UI组件初始化与事件绑定
    private void bindView() {
        topBar = (TextView)this.findViewById(R.id.txt_top);
        tabDeal = (TextView)this.findViewById(R.id.txt_deal);
        tabPoi = (TextView)this.findViewById(R.id.txt_poi);
        tabUser = (TextView)this.findViewById(R.id.txt_user);
        ly_content = (FrameLayout) findViewById(R.id.fragment_container);
        searchView = (SearchView) findViewById(R.id.searchView);

        tabDeal.setOnClickListener(this);
        tabUser.setOnClickListener(this);
        tabPoi.setOnClickListener(this);

    }

    //重置所有文本的选中状态
    public void selected(){
        tabDeal.setSelected(false);
        tabPoi.setSelected(false);
        tabUser.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(f1!=null){
            transaction.hide(f1);
        }
        if(f2!=null){
            transaction.hide(f2);
        }

    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch(v.getId()){
            case R.id.txt_deal:
                selected();
                tabDeal.setSelected(true);
                searchView.setVisibility(View.VISIBLE);
                if(f1==null){
                    f1 = new OnlyRecommendation();
                    transaction.add(R.id.fragment_container,f1);
                }else{
                    transaction.show(f1);
                }
                break;

            case R.id.txt_poi:
                selected();
                tabPoi.setSelected(true);
                searchView.setVisibility(View.GONE);
                if(f2==null){
                    f2 = new OnlyGroups();
                    transaction.add(R.id.fragment_container,f2);
                }else{
                    transaction.show(f2);
                }
                break;

        }

        transaction.commit();
    }
}