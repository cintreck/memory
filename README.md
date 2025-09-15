# Memory (Fabric)

Memory automatically captures a screenshot every five minutes (configurable) using the exact flow that pressing **F2** triggers. Drop it into your client alongside Fabric API and the game will quietly catalog your play session.

![Scary encounter..](https://cdn.modrinth.com/data/cached_images/155fc926410a73f3a04c8a68e8a778bc0a45ad6f.png)

## Features
- Mirrors vanilla screenshots: resolution, file naming, chat message, and storage under `screenshots/`.
- Adjustable cadence and chat visibility through `config/memory.toml` (default 300 seconds, message hidden) or an optional Mod Menu + Cloth Config screen.
- Runs entirely on the client; no server install or configuration is required.
- Pauses the timer whenever you are in menus or the game is paused so pictures happen in-world only.

## Install
- Requires [Fabric](https://fabricmc.net/) and [Fabric API](https://modrinth.com/mod/fabric-api).
- Optional UI: add [Mod Menu](https://modrinth.com/mod/modmenu) and [Cloth Config](https://modrinth.com/mod/cloth-config) to tweak settings in-game.
- Place the built jar in `mods/` on the client. Servers never need it.

## Build
- Java 21 with Gradle + Fabric Loom.
- Versions live in `gradle.properties`. Run `./gradlew build`.

## License
- CC0-1.0. Use it freely.
