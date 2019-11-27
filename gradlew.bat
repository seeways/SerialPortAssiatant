/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.policy;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Size;
import android.view.Gravity;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Calculates the snap targets and the snap position for the PIP given a position and a velocity.
 * All bounds are relative to the display top/left.
 */
public class PipSnapAlgorithm {

    // The below SNAP_MODE_* constants correspond to the config resource value
    // config_pictureInPictureSnapMode and should not be changed independently.
    // Allows snapping to the four corners
    private static final int SNAP_MODE_CORNERS_ONLY = 0;
    // Allows snapping to the four corners and the mid-points on the long edge in each orientation
    private static final int SNAP_MODE_CORNERS_AND_SIDES = 1;
    // Allows snapping to anywhere along the edge of the screen
    private static final int SNAP_MODE_EDGE = 2;
    // Allows snapping anywhere along the edge of the screen and magnets towards corners
    private static final int SNAP_MODE_EDGE_MAGNET_CORNERS = 3;
    // Allows snapping on the long edge in each orientation and magnets towards corners
    private static final int SNAP_MODE_LONG_EDGE_MAGNET_CORNERS = 4;

    // Threshold to magnet to a corner
    private static final float CORNER_MAGNET_THRESHOLD = 0.3f;

    private final Context mContext;

    private final ArrayList<Integer> 