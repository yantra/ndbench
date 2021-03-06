/*
 *  Copyright 2016 Netflix, Inc.
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
 *
 */

package com.netflix.ndbench.core.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.netflix.ndbench.core.util.NdBenchConstants;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Properties;

/**
 * Loads {@link System#getProperties()} as a source.
 * <p/>
 * Implementation note: {@link #set(String, String)} does not write to system properties, but will write to a new map.
 * This means that setting values to this source has no effect on system properties or other instances of this class.
 */
public final class SystemPropertiesConfigSource extends AbstractConfigSource {
    private final Map<String, String> data = Maps.newConcurrentMap();

    @Override
    public void initialize() {
        super.initialize();

        Properties systemProps = System.getProperties();

        for (final String key : systemProps.stringPropertyNames()) {
            if (!key.startsWith(NdBenchConstants.WEBAPP_NAME)) {
                continue;
            }
            final String value = systemProps.getProperty(key);
            if (!StringUtils.isEmpty(value)) {
                data.put(key, value);
            }
        }
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public String get(final String key) {
        return data.get(key);
    }

    @Override
    public void set(final String key, final String value) {
        Preconditions.checkNotNull(value, "Value can not be null for configurations");
        data.put(key, value);
    }
}