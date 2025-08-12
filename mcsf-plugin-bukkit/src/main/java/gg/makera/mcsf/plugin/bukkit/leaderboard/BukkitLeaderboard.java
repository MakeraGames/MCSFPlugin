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

package gg.makera.mcsf.plugin.bukkit.leaderboard;

import gg.makera.mcsf.api.MCSFAPI;
import gg.makera.mcsf.api.response.LeaderboardInfoResponse;
import gg.makera.mcsf.plugin.bukkit.MCSFBukkitPlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class BukkitLeaderboard {

    private final MCSFBukkitPlugin plugin;
    private final LeaderboardInfoResponse.Leaderboard leaderboard;
    private final String placeholder;

    public BukkitLeaderboard(@NotNull MCSFBukkitPlugin plugin,
                             @NotNull LeaderboardInfoResponse.Leaderboard leaderboard,
                             @NotNull String placeholder) {
        this.plugin = plugin;
        this.leaderboard = leaderboard;
        this.placeholder = placeholder;
    }

    private final Set<Player> cachedPlayers = new HashSet<>();
    private final Map<String, Double> offlineCache = new HashMap<>();

    public List<BukkitLeaderboardEntry> getTopEntries() {
        int playersLimit = leaderboard.getPlayersLimit();
        
        List<BukkitLeaderboardEntry> entries = new ArrayList<>();
        for (Player player : cachedPlayers) {
            if (player.isOnline()) {

                String parsed = PlaceholderAPI.setPlaceholders(player, placeholder);
                if (!isNumeric(parsed)) continue;

                double value = Double.parseDouble(parsed);
                BukkitLeaderboardEntry entry = new BukkitLeaderboardEntry(player.getUniqueId(), player.getName(), value);
                entries.add(entry);
            } else {
                BukkitLeaderboardEntry entry = new BukkitLeaderboardEntry(player.getUniqueId(), player.getName(),
                        offlineCache.get(player.getName()));
                entries.add(entry);
            }
        }
        entries.sort(Comparator.comparing(BukkitLeaderboardEntry::getValue).reversed());
        
        // Now, let's substitute the array.
        if (entries.size() > playersLimit) {
            entries = entries.subList(0, playersLimit);
        }
        
        return entries;
    }

    public void cacheOnline(Player player) {
        offlineCache.remove(player.getName());
        cachedPlayers.add(player);
    }

    public void cacheOffline(Player player) {
        cachedPlayers.remove(player);
        String parsed = PlaceholderAPI.setPlaceholders(player, placeholder);
        if (!isNumeric(parsed)) {
            return;
        }
        double value = Double.parseDouble(parsed);
        offlineCache.put(player.getName(), value);
    }

    public void performLeaderboardUpdate() {
        MCSFAPI api = plugin.getApi();
        CompletableFuture.allOf(getTopEntries().stream()
                .map(entry -> api.updateLeaderboard(
                        plugin.getServer().getId(),
                        leaderboard.getName(),
                        entry.getUuid(),
                        entry.getName(),
                        entry.getValue()
                ).exceptionally(throwable -> {
                    throwable.printStackTrace(System.err);
                    return null;
                })).toArray(CompletableFuture[]::new)).thenRun(() -> {
                    cachedPlayers.clear();
                    offlineCache.clear();
                });
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
