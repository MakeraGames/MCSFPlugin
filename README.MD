[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]

<br />
<div align="center">
  <a href="https://github.com/MakeraGames/NoteBlockPlugin">
    <img height="50%" width="auto" src=".github/images/noteblock-logo.svg" alt="Logo">
  </a>

<h1 align="center">NoteBlock Minecraft Plugin</h1>

  <p align="center">
    The official Minecraft Plugin for <a href="https://noteblock.gg">NoteBlock</a> Platform
    <br />
    <a href="https://github.com/MakeraGames/NoteBlockPlugin/issues/new?assignees=&labels=Requires+Testing&template=bug_report.yml">Report issue</a>
    •
    <a href="https://github.com/MakeraGames/NoteBlockPlugin/issues/new?assignees=&labels=Enhancement&template=feature_request.yml">Request feature</a>
    <br />
  </p>
</div>

## What is NoteBlock?
NoteBlock.gg is a modern platform for discovering Minecraft servers, launched in April 2025. It features curated server
listings across categories like PvP, Creative, Adventure, Minigames, and more. With real-time stats, community feedback,
and trending picks, NoteBlock.gg makes it easy for players to find their next favorite server.

## Supported Platforms
This plugin supports various platforms, current supported ones are:
- **Velocity**: through the module `noteblock-plugin-velocity`
- **BungeeCord**: through the module `noteblock-plugin-bungeecord`
- **Bukkit/Spigot/Paper and other Bukkit-based forks**: through the module `noteblock-plugin-bukkit`

## Installation
1. Get your Noteblock API key from the [Settings](https://noteblock.gg/settings/api-keys) page.
2. Get your Server ID from **Edit Server** page.
3. Download the plugin you want to include in your platform from the
    [Releases](https://github.com/MakeraGames/NoteBlockPlugin/releases) page.
4. Drop the plugin in your `plugins/` directory.
5. Start your server to generate the configuration file.
6. Turn off your server and go to your plugin `config.yml` (located in `plugins/noteblock-plugin/` for velocity,
    and `plugins/NoteBlockPlugin/` for other platforms) put the API key there:
    ```yaml
   settings:
       apiKey: "put-your-api-key-here"
       serverId: 0 # Your Server ID here
    ```
7. Done! the plugin is ready-to-go.

## Contributions
We welcome community contributions through pull requests!

# Licensing
This project is licensed under [MIT License](https://opensource.org/licenses/MIT),
you can read it at [LICENSE](https://github.com/MakeraGames/NoteBlockPlugin/LICENSE) file

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/MakeraGames/NoteBlock-Java.svg?style=for-the-badge
[contributors-url]: https://github.com/MakeraGames/NoteBlockPlugin/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/MakeraGames/NoteBlock-Java.svg?style=for-the-badge
[forks-url]: https://github.com/MakeraGames/NoteBlockPlugin/network/members
[stars-shield]: https://img.shields.io/github/stars/MakeraGames/NoteBlock-Java.svg?style=for-the-badge
[stars-url]: https://github.com/MakeraGames/NoteBlockPlugin/stargazers
[issues-shield]: https://img.shields.io/github/issues/MakeraGames/NoteBlock-Java.svg?style=for-the-badge
[issues-url]: https://github.com/MakeraGames/NoteBlockPlugin/issues
[license-shield]: https://img.shields.io/github/license/MakeraGames/NoteBlock-Java.svg?style=for-the-badge
[license-url]: https://github.com/MakeraGames/NoteBlockPlugin/blob/master/LICENSE
[product-screenshot]: .github/images/noteblock-logo.svg