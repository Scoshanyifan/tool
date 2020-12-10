package com.kunbu.common.util.biz.scene.handler;

import com.kunbu.common.util.biz.scene.entity.dto.action.SceneActionBase;
import com.kunbu.common.util.biz.scene.entity.dto.action.DelaySceneAction;
import com.kunbu.common.util.biz.scene.entity.dto.action.DeviceSceneAction;
import com.kunbu.common.util.biz.scene.entity.dto.action.NotifySceneAction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kunbu
 * @date 2020/12/2 11:45
 **/
public class SceneActionHandler {

    public static List<SceneActionBase> init() {
        List<SceneActionBase> sceneActionBaseList = new ArrayList<>();
        sceneActionBaseList.add(new DeviceSceneAction("attr", 1, true));
        sceneActionBaseList.add(new DelaySceneAction(10L));
        sceneActionBaseList.add(new NotifySceneAction("内容内容内容", "18809090809"));
        return sceneActionBaseList;
    }

    public static void splitAction(List<SceneActionBase> sceneActionBaseList) {

    }

    public static void main(String[] args) {

    }


}
