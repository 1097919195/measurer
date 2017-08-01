/*
 * Copyright 2016, The Android Open Source Project
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

package stuido.tsing.iclother.utils.db;

import android.provider.BaseColumns;

/**
 * The contract used for the db to save the tasks locally.
 */
public final class MeasurementsPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private MeasurementsPersistenceContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class MeasurementEntry implements BaseColumns {
        public static final String TABLE_NAME = "measurement";
        // TODO: 2017/7/31 详细字段
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_USER_ID = "userId";
        public static final String COLUMN_NAME_DATA = "data";
        public static final String COLUMN_NAME_GENDER = "gender";
    }
}
