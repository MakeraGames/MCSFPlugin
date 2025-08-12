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

package gg.makera.mcsf.plugin.bukkit;

import gg.makera.mcsf.api.response.LeaderboardInfoResponse;
import gg.makera.mcsf.plugin.bukkit.leaderboard.BukkitLeaderboard;
import gg.makera.mcsf.plugin.bukkit.leaderboard.BukkitLeaderboardListener;
import gg.makera.mcsf.plugin.bukkit.leaderboard.BukkitLeaderboardUpdateScheduler;
import gg.makera.mcsf.plugin.common.MCSFConfiguration;
import gg.makera.mcsf.plugin.common.MCSFPlugin;
import gg.makera.mcsf.plugin.common.configuration.ConfigurationKey;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class MCSFBukkitPlugin extends MCSFPlugin {

    private final MCSFBukkitBootstrap bootstrap;
    @Getter
    private final Set<BukkitLeaderboard> bukkitLeaderboards = new HashSet<>();

    public MCSFBukkitPlugin(@NotNull MCSFBukkitBootstrap bootstrap) {
        super(bootstrap.getDataFolder(), bootstrap.getLogger());
        this.bootstrap = bootstrap;
    }

    @Override
    protected void onStart() {
        initializeLeaderboards();
    }

    private void initializeLeaderboards() {
        ConfigurationKey<MCSFConfiguration.LeaderboardSettings> leaderboardSettingsConfigurationKey = ConfigurationKey.of(
                "leaderboardSettings",
                MCSFConfiguration.LeaderboardSettings.class,
                new MCSFConfiguration.LeaderboardSettings()
        );
        MCSFConfiguration.LeaderboardSettings leaderboardSettings = getConfigHandler().get(leaderboardSettingsConfigurationKey);

        if (!leaderboardSettings.isEnabled()) return;

        for (MCSFConfiguration.LeaderboardSettings.Leaderboard settingLeaderboard : leaderboardSettings.getLeaderboards()) {
            LeaderboardInfoResponse.Leaderboard leaderboard = getLeaderboard(settingLeaderboard.getId());
            if (leaderboard == null) {
                getLogger().severe("Could not find leaderboard with id " + settingLeaderboard.getId());
                continue;
            }
            BukkitLeaderboard bukkitLeaderboard = new BukkitLeaderboard(this, leaderboard, settingLeaderboard.getPlaceholder());
            bukkitLeaderboards.add(bukkitLeaderboard);
        }
        int interval = leaderboardSettings.getUpdateInterval();

        bootstrap.registerListeners(new BukkitLeaderboardListener(this));

        new BukkitLeaderboardUpdateScheduler(this).runTaskTimerAsynchronously(
                bootstrap,
                (long) interval * 20 * 60,
                (long) interval * 20 * 60
        );
    }

    @Override
    protected void onStop() {

    }
}
