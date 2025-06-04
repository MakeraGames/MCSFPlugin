/*
 * MIT License
 *
 * Copyright (c) 2025 Makera Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gg.makera.noteblock.plugin.common.configuration;

import gg.makera.noteblock.plugin.common.configuration.yaml.YamlConfigurationAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class registers and holds configuration adapters to make their retrieve easier.
 */
public class ConfigurationAdapters {

    private static final Map<Class<?>, ConfigurationAdapter<?>> ADAPTER_MAP = new HashMap<>();

    static {
        ADAPTER_MAP.put(YamlConfigurationAdapter.class, new YamlConfigurationAdapter());
    }

    /**
     * Retrieves an adapter by its class type.
     *
     * @param adapterClass Adapter class type
     * @return The registered adapter
     * @param <A> Adapter generic type
     * @throws IllegalArgumentException If the provided class is not registered yet
     */
    public static <A extends ConfigurationAdapter<?>> A getAdapter(final Class<A> adapterClass) {
        ConfigurationAdapter<?> adapter = ADAPTER_MAP.get(adapterClass);
        if (adapter == null) {
            throw new IllegalArgumentException("No adapter found for class " + adapterClass.getName());
        }
        return adapterClass.cast(adapter);
    }

    public static void registerAdapter(ConfigurationAdapter<?> adapter) {
        ADAPTER_MAP.put(adapter.getClass(), adapter);
    }

}
