package xyz.sirblobman.application.yaml.legacy.minimessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import xyz.sirblobman.application.yaml.legacy.minimessage.menu.ConverterMenu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.MiniMessage.Builder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class Main {
    public static void main(String... args) {
        if(args.length < 2) {
            print("Missing Arguments.");
            print("CLI Usage: 'java -jar <converter.jar> <input.yml> <output.yml>'");
            print("Opening menu for non-CLI usage...");

            ConverterMenu converterMenu = new ConverterMenu();
            converterMenu.initialize();
            converterMenu.open();
            return;
        }

        String inputFilePathString = args[0];
        Path inputFilePath = Paths.get(inputFilePathString);
        if(!Files.isRegularFile(inputFilePath)) {
            print("Invalid Input File '" + inputFilePathString + "'.");
            System.exit(2);
        }

        String outputFilePathString = args[1];
        Path outputFilePath = Paths.get(outputFilePathString);
        if(Files.exists(outputFilePath)) {
            print("Output File '" + inputFilePathString + "' already exists.");
            System.exit(3);
        }

        try {
            boolean useStrictMode = (args.length > 2 && args[2].equals("--strict"));
            convert(inputFilePath, outputFilePath, useStrictMode);
        } catch(IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
            System.exit(4);
        }
    }

    public static void print(String message) {
        System.out.println(message);
    }

    private static MiniMessage getMiniMessage(boolean strict) {
        Builder builder = MiniMessage.builder();
        builder.strict(strict);
        return builder.build();
    }

    private static String getMiniMessageString(String original, boolean strict) {
        MiniMessage miniMessage = getMiniMessage(strict);
        LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacyAmpersand();

        Component component = legacySerializer.deserialize(original);
        return miniMessage.serialize(component);
    }

    private static void fixString(ConfigurationSection section, String path, String original, boolean strict) {
        String miniMessageString = getMiniMessageString(original, strict);
        section.set(path, miniMessageString);
    }

    private static void fixList(ConfigurationSection section, String path, List<?> original, boolean strict) {
        List<Object> newList = new ArrayList<>();
        for (Object originalObject : original) {
            if (originalObject instanceof String string) {
                String miniMessageString = getMiniMessageString(string, strict);
                newList.add(miniMessageString);
            } else {
                newList.add(originalObject);
            }
        }

        section.set(path, newList);
    }

    public static void convert(Path inputFile, Path outputFile, boolean strict) throws IOException,
            InvalidConfigurationException {
        String inputString = Files.readString(inputFile, StandardCharsets.UTF_8);
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.loadFromString(inputString);

        Set<String> keySet = configuration.getKeys(true);
        for (String key : keySet) {
            if(configuration.isString(key)) {
                String legacyString = configuration.getString(key, "");
                fixString(configuration, key, legacyString, strict);
            }

            if(configuration.isList(key)) {
                List<?> objectList = configuration.getList(key);
                if(objectList == null) {
                    continue;
                }

                fixList(configuration, key, objectList, strict);
            }
        }

        String outputString = configuration.saveToString();
        OpenOption[] openOptions = {StandardOpenOption.CREATE_NEW};
        Files.writeString(outputFile, outputString, StandardCharsets.UTF_8, openOptions);
    }
}
