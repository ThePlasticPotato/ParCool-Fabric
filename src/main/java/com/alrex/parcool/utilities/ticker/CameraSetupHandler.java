package com.alrex.parcool.utilities.ticker;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.common.action.ActionProcessor;
import io.github.fabricators_of_create.porting_lib.event.client.CameraSetupCallback;

public class CameraSetupHandler implements CameraSetupCallback {
    @Override
    public boolean onCameraSetup(CameraInfo info) {
        ParCool.ACTION_PROCESSOR.onViewRender();
        return true;
    }
}
