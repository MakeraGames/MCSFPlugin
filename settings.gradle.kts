rootProject.name = "MCSFPlugin"

for (module in arrayOf("bukkit", "bungeecord", "common", "velocity")) {
    include("mcsf-plugin-$module")
}