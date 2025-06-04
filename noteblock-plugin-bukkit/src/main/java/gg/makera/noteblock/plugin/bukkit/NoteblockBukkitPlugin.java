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

package gg.makera.noteblock.plugin.bukkit;

import gg.makera.noteblock.api.response.LeaderboardInfoResponse;
import gg.makera.noteblock.plugin.bukkit.leaderboard.BukkitLeaderboard;
import gg.makera.noteblock.plugin.bukkit.leaderboard.BukkitLeaderboardUpdateScheduler;
import gg.makera.noteblock.plugin.common.NoteblockConfiguration;
import gg.makera.noteblock.plugin.common.NoteblockPlugin;
import gg.makera.noteblock.plugin.common.configuration.ConfigurationKey;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class NoteblockBukkitPlugin extends NoteblockPlugin {

    private final NoteblockBukkitBootstrap bootstrap;
    @Getter
    private final Set<BukkitLeaderboard> leaderboards = new HashSet<>();

    public NoteblockBukkitPlugin(@NotNull NoteblockBukkitBootstrap bootstrap) {
        super(bootstrap.getDataFolder(), bootstrap.getLogger());
        this.bootstrap = bootstrap;
    }

    @Override
    protected void onStart() {
        ConfigurationKey<NoteblockConfiguration.LeaderboardSettings> leaderboardSettingsConfigurationKey = ConfigurationKey.of(
                "leaderboardSettings",
                NoteblockConfiguration.LeaderboardSettings.class,
                new NoteblockConfiguration.LeaderboardSettings()
        );
        NoteblockConfiguration.LeaderboardSettings leaderboardSettings = getConfigHandler().get(leaderboardSettingsConfigurationKey);
        if (leaderboardSettings.isEnabled()) {
            initializeLeaderboards(leaderboardSettings);
        }
    }

    private void initializeLeaderboards(NoteblockConfiguration.LeaderboardSettings leaderboardSettings) {
        for (NoteblockConfiguration.LeaderboardSettings.Leaderboard settingLeaderboard : leaderboardSettings.getLeaderboards()) {
            LeaderboardInfoResponse.Leaderboard leaderboard = getLeaderboard(settingLeaderboard.getId());
            if (leaderboard == null) {
                getLogger().severe("Could not find leaderboard with id " + settingLeaderboard.getId());
                continue;
            }
            BukkitLeaderboard bukkitLeaderboard = new BukkitLeaderboard(this, leaderboard, settingLeaderboard.getPlaceholder());
            leaderboards.add(bukkitLeaderboard);
        }
        int interval = leaderboardSettings.getUpdateInterval();

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
