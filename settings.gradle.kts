rootProject.name = "NoteblockPlugin"

for (module in arrayOf("bukkit", "bungeecord", "common", "velocity")) {
    include("noteblock-plugin-$module")
}