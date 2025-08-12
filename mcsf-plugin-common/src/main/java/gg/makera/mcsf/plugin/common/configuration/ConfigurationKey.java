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

package gg.makera.mcsf.plugin.common.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class represents a configuration key holding the necessary attributes for it.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ConfigurationKey<T> {

    private final String path;
    private final Class<T> type;
    private final T defaultValue;

    /**
     * Creates a new instance of ConfigurationKey.
     *
     * @param path Key's path
     * @param type Key's type
     * @param defaultValue Default value of the key in the configuration
     * @return New instance of ConfigurationKey
     * @param <T> Generic type of the key
     */
    public static <T> ConfigurationKey<T> of(final String path, final Class<T> type, final T defaultValue) {
        return new ConfigurationKey<>(path, type, defaultValue);
    }

}
