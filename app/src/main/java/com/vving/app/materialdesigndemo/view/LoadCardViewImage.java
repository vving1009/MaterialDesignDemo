package com.vving.app.materialdesigndemo.view;

import java.io.File;

/**
 * Created by VV on 2017/11/1.
 */

public interface LoadCardViewImage {
    void refreshUI(File image);

    void networkFailed();
}
