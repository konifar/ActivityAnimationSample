/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.konifar.activitytransitionsample;

import java.util.ArrayList;
import java.util.List;

public class PhotoUtils {

    final static int[] SAMPLE_PHOTO_RESOURCE_IDS = {
            R.drawable.magika1,
            R.drawable.magika2,
            R.drawable.magika3,
            R.drawable.magika4,
            R.drawable.magika5
    };

    public static List<PhotoModel> getSamplePhotos(int photosCount) {
        List<PhotoModel> pictures = new ArrayList<>();
        for (int i = 0; i < photosCount; ++i) {
            int randomPos = (int) (Math.random() * SAMPLE_PHOTO_RESOURCE_IDS.length);
            int resourceId = SAMPLE_PHOTO_RESOURCE_IDS[randomPos];
            pictures.add(new PhotoModel(resourceId));
        }
        return pictures;
    }

}
