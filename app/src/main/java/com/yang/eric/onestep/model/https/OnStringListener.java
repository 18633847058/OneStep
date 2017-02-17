package com.yang.eric.onestep.model.https;

import okhttp3.Response;

/**
 * Created by Yang on 2017/2/17.
 */
public interface OnStringListener {

	void onSuccess(Response result);

	void onError(Exception e);
}
