package com.yang.eric.onestep.model.https;

import com.android.volley.VolleyError;

/**
 * Created by Yang on 2017/2/17.
 */
public interface OnStringListener {

	void onSuccess(String result);

	void onError(VolleyError error);
}
