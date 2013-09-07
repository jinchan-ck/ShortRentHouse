/*
 * Copyright (C) 2013 47 Degrees, LLC
 *  http://47deg.com
 *  hello@47deg.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package tk.sweetvvck.swipeview.util;

import tk.sweetvvck.swipeview.SwipeListView;

public class SettingsManager {
	/**
	 * 滑动方向： 只能左滑（left）；只能右滑（right）；两边都可以（both）。
	 * */
    private int swipeMode = SwipeListView.SWIPE_MODE_BOTH;
    /**
     * 长按列表项是否展开
     * */
    private boolean swipeOpenOnLongPress = true;
    /**
     * 滑动时是否关闭所有展开项
     * */
    private boolean swipeCloseAllItemsWhenMoveList = true;
    /**
     * 滑动动画时间：  默认 0（系统默认）；数值越大，动画时间越长。
     * */
    private long swipeAnimationTime = 0;
    /**
     * 左滑动停止预留宽度
     * */
    private float swipeOffsetLeft = 0;
    /**
     * 右滑动停止预留宽度
     * */
    private float swipeOffsetRight = 0;
    /**
     * 左滑动作：滑动展示（REVEAL）；删掉该项（DISMISS）；选择该项，自动回弹功能（CHOICE）；未知（NONE）
     * */
    private int swipeActionLeft = SwipeListView.SWIPE_ACTION_REVEAL;
    /**
     * 右滑动作：滑动展示（REVEAL）；删掉该项（DISMISS）；选择该项，自动回弹功能（CHOICE）；未知（NONE）
     * */
    private int swipeActionRight = SwipeListView.SWIPE_ACTION_REVEAL;

    
    
    
    private static SettingsManager settingsManager = new SettingsManager();

    public static SettingsManager getInstance() {
        return settingsManager;
    }

    public static SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public static void setSettingsManager(SettingsManager settingsManager) {
        SettingsManager.settingsManager = settingsManager;
    }

    public long getSwipeAnimationTime() {
        return swipeAnimationTime;
    }

    public void setSwipeAnimationTime(long swipeAnimationTime) {
        this.swipeAnimationTime = swipeAnimationTime;
    }

    public boolean isSwipeCloseAllItemsWhenMoveList() {
        return swipeCloseAllItemsWhenMoveList;
    }

    public void setSwipeCloseAllItemsWhenMoveList(boolean swipeCloseAllItemsWhenMoveList) {
        this.swipeCloseAllItemsWhenMoveList = swipeCloseAllItemsWhenMoveList;
    }

    public int getSwipeMode() {
        return swipeMode;
    }

    public void setSwipeMode(int swipeMode) {
        this.swipeMode = swipeMode;
    }

    public float getSwipeOffsetLeft() {
        return swipeOffsetLeft;
    }

    public void setSwipeOffsetLeft(float swipeOffsetLeft) {
        this.swipeOffsetLeft = swipeOffsetLeft;
    }

    public float getSwipeOffsetRight() {
        return swipeOffsetRight;
    }

    public void setSwipeOffsetRight(float swipeOffsetRight) {
        this.swipeOffsetRight = swipeOffsetRight;
    }

    public boolean isSwipeOpenOnLongPress() {
        return swipeOpenOnLongPress;
    }

    public void setSwipeOpenOnLongPress(boolean swipeOpenOnLongPress) {
        this.swipeOpenOnLongPress = swipeOpenOnLongPress;
    }

    public int getSwipeActionLeft() {
        return swipeActionLeft;
    }

    public void setSwipeActionLeft(int swipeActionLeft) {
        this.swipeActionLeft = swipeActionLeft;
    }

    public int getSwipeActionRight() {
        return swipeActionRight;
    }

    public void setSwipeActionRight(int swipeActionRight) {
        this.swipeActionRight = swipeActionRight;
    }
}
