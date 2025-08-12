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

package gg.makera.mcsf.bukkit.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.io.File;

@Getter
public class MCSFVelocityBootstrap {

    private final ProxyServer server;
    private final Logger logger;
    private final File dataFolder = new File("plugins/mcsf-plugin/");
    private final MCSFVelocityPlugin plugin;

    public MCSFVelocityBootstrap(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        if (!dataFolder.exists()) dataFolder.mkdirs();
        this.plugin = new MCSFVelocityPlugin(this);
    }

    @Subscribe
    public void onInit(ProxyInitializeEvent event) {
        plugin.initialize();
    }

    @Subscribe
    public void onShut(ProxyShutdownEvent event) {
        plugin.shutdown();
    }

}
