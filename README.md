![Version](https://img.shields.io/badge/dynamic/xml?color=blue&label=Version&logo=github&query=%2F%2A%5Blocal-name%28%29%3D'project'%5D%2F%2A%5Blocal-name%28%29%3D'version'%5D&url=https%3A%2F%2Fraw.githubusercontent.com%2FAeoliaXYZ%2FAshUtils%2Fmaster%2Fpom.xml)
[![GPL 3.0](https://img.shields.io/github/license/AeoliaXYZ/AshUtils?&logo=github&label=License)](LICENSE)
[![Build](https://github.com/AeoliaXYZ/AshUtils/actions/workflows/maven.yml/badge.svg)](https://github.com/AeoliaXYZ/AshUtils/actions)
[![Dependency review](https://github.com/AeoliaXYZ/AshUtils/actions/workflows/dependency-review.yml/badge.svg)](https://github.com/AeoliaXYZ/AshUtils/actions)

# AshUtils
This is a bespoke general plugin for the demands of aeolia.xyz. We don't recommend running it outside of this environment.    
    
## Setup
You need to ensure settings.restart_script is set correctly in spigot.yml, or have a panel or manager that automatically restarts a server when it stops normally.
Ensure the config is set up, several commands rely on independently configured webhooks. If these URIs aren't provided, the plugin will handle the error, but it'll log every time it fails in the console.

## Dependencies
This plugin softdepends Essentials, LuckPerms, and Vault. Without these plugins, most features of this plugin will not be available.
