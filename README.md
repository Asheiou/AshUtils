# AshUtils
This is a bespoke general plugin for the demands of asheiou.cymru. I don't recommend running it outside of this environment.    
    
## Setup
You need to ensure settings.restart_script is set correctly in spigot.yml, or have a panel or manager that automatically restarts a server when it stops normally.
Ensure the config is set up, several comamnds rely on independently configured webhooks. If these URIs aren't provided, the plugin will handle the error, but it'll log every time it fails in the console.

## Dependencies
This plugin softdepends Essentials and Vault. Without these plugins, most features of this plugin will not be available.
