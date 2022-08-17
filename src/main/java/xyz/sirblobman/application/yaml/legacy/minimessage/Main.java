package xyz.sirblobman.application.yaml.legacy.minimessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class Main {
    public static void main(String... args) {
        if(args.length < 2) {
            print("Missing Arguments.");
            print("Usage: converter.jar <input.yml> <output.yml>");
            System.exit(1);
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

        MiniMessage miniMessage = MiniMessage.miniMessage();
        LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacyAmpersand();

        try {
            String inputString = Files.readString(inputFilePath, StandardCharsets.UTF_8);
            YamlConfiguration configuration = new YamlConfiguration();
            configuration.loadFromString(inputString);

            Set<String> keySet = configuration.getKeys(true);
            for (String key : keySet) {
                if(configuration.isString(key)) {
                    String legacyString = configuration.getString(key, "");
                    Component component = legacySerializer.deserialize(legacyString);
                    String miniMessageString = miniMessage.serialize(component);
                    configuration.set(key, miniMessageString);
                }

                if(configuration.isList(key)) {
                    List<?> objectList = configuration.getList(key);
                    if(objectList == null) {
                        continue;
                    }

                    List<Object> newList = new ArrayList<>();
                    for (Object object : objectList) {
                        if(object instanceof String legacyString) {
                            Component component = legacySerializer.deserialize(legacyString);
                            String miniMessageString = miniMessage.serialize(component);
                            newList.add(miniMessageString);
                        } else {
                            newList.add(object);
                        }
                    }

                    configuration.set(key, newList);
                }
            }

            String outputString = configuration.saveToString();
            Files.writeString(outputFilePath, outputString, StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE_NEW);
        } catch(IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
            System.exit(4);
        }
    }

    private static void print(String message) {
        System.out.println(message);
    }
}
