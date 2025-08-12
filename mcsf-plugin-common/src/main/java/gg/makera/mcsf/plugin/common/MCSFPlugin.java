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

package gg.makera.mcsf.plugin.common;

import gg.makera.mcsf.api.MCSFAPIFactory;
import gg.makera.mcsf.api.MCSFAPI;
import gg.makera.mcsf.api.response.*;
import gg.makera.mcsf.plugin.common.MCSFConfiguration.*;
import gg.makera.mcsf.plugin.common.configuration.ConfigurationAdapters;
import gg.makera.mcsf.plugin.common.configuration.ConfigurationKey;
import gg.makera.mcsf.plugin.common.configuration.yaml.YamlConfigurationAdapter;
import gg.makera.mcsf.plugin.common.configuration.yaml.YamlConfigurationHandler;
import gg.makera.mcsf.plugin.common.utils.JarUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Getter
public abstract class MCSFPlugin {

    private final Logger logger;
    private final Map<String, LeaderboardInfoResponse.Leaderboard> leaderboardMap = new HashMap<>();
    private YamlConfigurationHandler configHandler;
    private final File pluginDir;
    private MCSFAPI api;
    private User user;
    private Server server;

    public MCSFPlugin(@NotNull File pluginDir) {
        this.pluginDir = pluginDir;
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    public MCSFPlugin(@NotNull File pluginDir, @NotNull Logger logger) {
        this.pluginDir = pluginDir;
        this.logger = logger;
    }

    public final void initialize() {
        // Load configuration file
        File file = new File(pluginDir, "config.yml");
        if (!file.exists()) {
            logger.info("config.yml does not exist, creating...");
            JarUtils.extract("config.yml", file);
        }
        // Load YAML configuration
        YamlConfigurationAdapter configurationAdapter = ConfigurationAdapters.getAdapter(YamlConfigurationAdapter.class);
        try {
            this.configHandler = configurationAdapter.load(file);
        } catch (IOException exception) {
            logger.severe("Failed to load config.yml: " + exception.getMessage());
            throw new RuntimeException(exception);
        }

        // Load API settings from config
        ConfigurationKey<Settings> settingsKey = ConfigurationKey.of(
                "settings",
                Settings.class,
                new Settings()
        );
        Settings settings = this.configHandler.get(settingsKey);

        this.api = MCSFAPIFactory.create(settings.getApiKey());


        UserInfoResponse userInfoResponse = api.getUserInfo().join();
        this.user = userInfoResponse.getUser();
        // An exception will be thrown if the API key is wrong
        logger.info("Welcome " + user.getName() + "!");

        // Retrieve server
        ServerInfoResponse serverInfoResponse = api.getServerInfo(settings.getServerId()).join();
        this.server = serverInfoResponse.getServer();
        logger.info("Successfully connected to MCServerFinder server: " + server.getSlug());

        // Load leaderboards
        ConfigurationKey<LeaderboardSettings> leaderboardSettingsConfigurationKey = ConfigurationKey.of(
                "leaderboardSettings",
                LeaderboardSettings.class,
                new LeaderboardSettings()
        );
        LeaderboardSettings leaderboardSettings = this.configHandler.get(leaderboardSettingsConfigurationKey);
        for (LeaderboardSettings.Leaderboard leaderboard : leaderboardSettings.getLeaderboards()) {
            LeaderboardInfoResponse leaderboardInfoResponse = api.getLeaderboardInfo(server.getId(), leaderboard.getId()).join();
            LeaderboardInfoResponse.Leaderboard responseLeaderboard = leaderboardInfoResponse.getLeaderboard();
            logger.info("Successfully retrieved '" + responseLeaderboard.getLegend() + "' limited to " +
                    responseLeaderboard.getPlayersLimit() + " entries");
            leaderboardMap.put(responseLeaderboard.getName(), responseLeaderboard);
        }

        onStart();
    }

    public final Collection<LeaderboardInfoResponse.Leaderboard> getLeaderboards() {
        return leaderboardMap.values();
    }

    public final LeaderboardInfoResponse.Leaderboard getLeaderboard(@NotNull String name) {
        return leaderboardMap.get(name);
    }

    public final void shutdown() {
        onStop();
        api.close();
    }

    protected abstract void onStart();

    protected abstract void onStop();


}
