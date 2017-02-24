package com.yang.eric.onestep.model.https;

import java.io.Reader;

/**
 * Created by Yang on 2017/2/17.
 */
public interface OnStringListener {

	void onSuccess(Reader result);

	void onError();
}
