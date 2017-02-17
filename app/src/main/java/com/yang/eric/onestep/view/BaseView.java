package com.yang.eric.onestep.view;

import android.view.View;

/**
 * Created by Yang on 2017/2/17.
 */

public interface BaseView<T> {

	//为View设置Preenter
	void setPresenter(T presenter);
	//初始化界面控件
	void initViews(View view);
}
