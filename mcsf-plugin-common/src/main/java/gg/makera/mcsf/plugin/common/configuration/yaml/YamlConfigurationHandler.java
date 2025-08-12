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

package gg.makera.mcsf.plugin.common.configuration.yaml;

import gg.makera.mcsf.plugin.common.configuration.ConfigurationHandler;
import gg.makera.mcsf.plugin.common.configuration.ConfigurationKey;
import gg.makera.mcsf.plugin.common.configuration.ConfigurationKeyTypeException;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * YAML Configuration implementation.
 */
public class YamlConfigurationHandler implements ConfigurationHandler {

    private final File file;
    private final Map<String, Object> data;
    private final Yaml yaml = new Yaml();

    @SuppressWarnings({"ResultOfMethodCallIgnored", "unchecked"})
    protected YamlConfigurationHandler(final File file) throws IOException {
        this.file = file;
        if (!file.exists())
            file.createNewFile();
        try (InputStream inputStream = new FileInputStream(file)) {
            Object loaded = yaml.load(inputStream);
            this.data = loaded instanceof Map ? (Map<String, Object>) loaded : new HashMap<>();
        }
    }

    @Override
    public <T> T get(ConfigurationKey<T> key) {
        Object value = resolvePath(key.getPath());
        if (value == null) {
            T defaultValue = key.getDefaultValue();
            if (defaultValue != null) {
                set(key, defaultValue);
                return defaultValue;
            }
            return null;
        }

        if (value instanceof Map && !isPrimitiveOrWrapper(key.getType())) {
            return yaml.loadAs(yaml.dump(value), key.getType());
        }

        if (!key.getType().isInstance(value)) {
            throw new ConfigurationKeyTypeException(key.getPath(), key.getType(), value);
        }

        return key.getType().cast(value);
    }

    @Override
    public <T> void set(ConfigurationKey<T> key, T value) {
        setPath(key.getPath(), value);
    }

    @Override
    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            yaml.dump(data, writer);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    @SuppressWarnings("unchecked")
    private Object resolvePath(String path) {
        String[] parts = path.split("\\.");
        Map<String, Object> current = data;
        for (int i = 0; i < parts.length - 1; i++) {
            Object next = current.get(parts[i]);
            if (!(next instanceof Map)) return null;
            current = (Map<String, Object>) next;
        }
        return current.get(parts[parts.length - 1]);
    }

    @SuppressWarnings("unchecked")
    private void setPath(String path, Object value) {
        String[] parts = path.split("\\.");
        Map<String, Object> current = data;
        for (int i = 0; i < parts.length - 1; i++) {
            current = (Map<String, Object>) current.computeIfAbsent(parts[i], k -> new HashMap<>());
        }
        current.put(parts[parts.length - 1], value);
    }

    private boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive()
                || type == String.class
                || type == Integer.class
                || type == Boolean.class
                || type == Double.class
                || type == Long.class
                || type == Float.class
                || type == Short.class
                || type == Byte.class;
    }
}
