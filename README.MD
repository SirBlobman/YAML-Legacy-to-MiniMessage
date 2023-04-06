# YAML Legacy to MiniMessage

Converts a YAML file with legacy Bukkit color codes into a new YAML file with Adventure MiniMessage text.

## Requirements

- Java 17
- A YAML file to convert.

## Command-Line Usage

Make sure to replace the following variables in these commands:

- `converter.jar`: The path to the jar file for this project.
- `legacy.yml`: The path to the old YAML file that contains the legacy color codes.
- `new.yml`: The path to the new YAML file that will be created.

Run the following command:

```sh
java -jar "converter.jar" "legacy.yml" "new.yml"
```

Strict Mode:

```sh
java -jar "converter.jar" "legacy.yml" "new.yml" --strict
```

## GUI Usage

Open the application with Java 17. You should see the following menu:  
[Application Screenshot](images/screenshot.png)

## How to compile?

Run the following command:

Unix:

```shell
$ ./gradlew build
```

Windows:
```shell
> gradlew.bat build
```

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
