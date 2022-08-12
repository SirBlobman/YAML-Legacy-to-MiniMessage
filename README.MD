# YAML Legacy to MiniMessage
Converts a YAML file with legacy Bukkit color codes into a new YAML file with Adventure MiniMessage text.

## Requirements
- Java 17
- A YML file to convert.

## Command-Line Usage
Run the following command:
- `java -jar "yaml-legacy-to-minimessage.jar" "input.yml" "output.yml"`

## How to compile?
Run the following command:
- `./gradlew build`

## Example
**Input:**
```yaml
prefix: "&f&l[&6CombatLogX&f&l]&r"

broadcast:
  on-load: "&a&lCombatLogX wurde erfolgreich geladen."
  on-enable: "&a&lCombatLogX wurde aktiviert."
  on-disable: "&c&lCombatLogX ist nun deaktiviert"
```

**Output:**
```yaml
prefix: <bold><white>[</white></bold><gold>CombatLogX</gold><bold><white>]

broadcast:
  on-load: <bold><green>CombatLogX wurde erfolgreich geladen.
  on-enable: <bold><green>CombatLogX wurde aktiviert.
  on-disable: <bold><red>CombatLogX ist nun deaktiviert
```
