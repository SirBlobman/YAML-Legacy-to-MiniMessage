package xyz.sirblobman.application.yaml.legacy.minimessage.menu;

import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.bukkit.configuration.InvalidConfigurationException;

import xyz.sirblobman.application.yaml.legacy.minimessage.Main;

import org.jetbrains.annotations.Nullable;

public final class ConverterMenu extends JFrame {
    private final Map<String, JTextField> textFieldMap;
    private final Map<String, JCheckBox> checkBoxMap;

    public ConverterMenu() {
        super("YAML Legacy to MiniMessage Converter");
        setSize(650, 150);
        setResizable(false);

        try {
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
        } catch(Exception ex) {
            Main.print("[ConverterMenu] Failed to set application look and feel:");
            ex.printStackTrace();
        }

        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        setFont(font);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.textFieldMap = new HashMap<>();
        this.checkBoxMap = new HashMap<>();
    }

    public void initialize() {
        Container container = getContentPane();
        container.setLayout(null);
        setupIcon();

        int textFieldX = 102;
        int textFieldWidth = 500;
        int textFieldHeight = 20;

        int buttonX = (textFieldX + textFieldWidth + 4);
        int buttonWidth = 30;
        int buttonHeight = 20;

        createLabel("Input Path:", 2);
        createLabel("Output Path:", 24);
        createLabel("Strict Mode:", 44);

        createTextField("Input-File-Path", textFieldWidth, textFieldHeight, textFieldX, 2);
        createTextField("Output-File-Path", textFieldWidth, textFieldHeight, textFieldX, 24);
        createButton("...", buttonWidth, buttonHeight, buttonX, 2, this::chooseInputFile);
        createButton("...", buttonWidth, buttonHeight, buttonX, 24, this::chooseOutputFile);

        createCheckBox();
        createButton("Convert", 100, 50, 536, 50, this::convert);
    }

    public void open() {
        setVisible(true);
    }

    private void setupIcon() {
        String iconPath = ("/assets/icon.png");
        Class<? extends ConverterMenu> thisClass = getClass();
        URL iconUrl = thisClass.getResource(iconPath);
        if (iconUrl == null) {
            Main.print("[ConverterMenu] Missing file in jar: '" + iconPath + "'.");
            return;
        }

        ImageIcon icon = new ImageIcon(iconUrl);
        Image image = icon.getImage();
        setIconImage(image);
    }

    private @Nullable JTextField getTextField(String id) {
        Objects.requireNonNull(id, "id must not be null!");
        return this.textFieldMap.get(id);
    }

    private void createLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setSize(100, 20);
        label.setLocation(2, y);

        Font font = getFont();
        label.setFont(font);
        label.setHorizontalAlignment(SwingConstants.RIGHT);

        Container container = getContentPane();
        container.add(label);
    }

    private void createTextField(String id, int width, int height, int x, int y) {
        Objects.requireNonNull(id, "id must not be null!");

        JTextField textField = new JTextField();
        textField.setSize(width, height);
        textField.setLocation(x, y);
        textField.setEditable(false);

        Font font = getFont();
        textField.setFont(font);

        this.textFieldMap.put(id, textField);
        Container container = getContentPane();
        container.add(textField);
    }

    private void createButton(String text, int width, int height, int x, int y, ActionListener clickAction) {
        JButton button = new JButton(text);
        button.setSize(width, height);
        button.setLocation(x, y);

        if (clickAction != null) {
            button.addActionListener(clickAction);
        }

        Container container = getContentPane();
        container.add(button);
    }

    private void createCheckBox() {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSize(20, 20);
        checkBox.setLocation(102, 44);

        this.checkBoxMap.put("Strict-Mode", checkBox);
        Container container = getContentPane();
        container.add(checkBox);
    }

    private void chooseInputFile(ActionEvent e) {
        JTextField textFieldInput = getTextField("Input-File-Path");
        if (textFieldInput == null) {
            throw new IllegalStateException("Missing text box 'Input-File-Path'");
        }

        textFieldInput.setText("");
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setAcceptAllFileFilterUsed(false);

        String[] extensions = {"yml", "yaml"};
        FileNameExtensionFilter yamlFilter = new FileNameExtensionFilter("Existing YAML Files", extensions);
        fileChooser.setFileFilter(new ExistingExtensionFilter(yamlFilter));

        int action = fileChooser.showOpenDialog(this);
        if (action == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile == null) {
                return;
            }

            try {
                String fileName = selectedFile.getCanonicalPath();
                textFieldInput.setText(fileName);
            } catch(IOException ex) {
                String message = "An error occurred, please choose a different file.";
                JOptionPane.showMessageDialog(this, message + "\n\n" + ex.getMessage(),
                        "I/O Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void chooseOutputFile(ActionEvent e) {
        JTextField textFieldInput = getTextField("Output-File-Path");
        if (textFieldInput == null) {
            throw new IllegalStateException("Missing text box 'Input-File-Path'");
        }

        textFieldInput.setText("");
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setAcceptAllFileFilterUsed(false);

        String[] extensions = {"yml", "yaml"};
        FileNameExtensionFilter yamlFilter = new FileNameExtensionFilter("YAML Files", extensions);
        fileChooser.setFileFilter(yamlFilter);

        int action = fileChooser.showOpenDialog(this);
        if (action == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile == null) {
                return;
            }

            if (selectedFile.exists()) {
                String message = "That file already exists, please choose a different path.";
                JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String fileName = selectedFile.getCanonicalPath();
                textFieldInput.setText(fileName);
            } catch(IOException ex) {
                String message = "An error occurred, please choose a different file.";
                JOptionPane.showMessageDialog(this, message + "\n\n" + ex.getMessage(),
                        "I/O Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void convert(ActionEvent e) {
        JTextField textFieldInput = getTextField("Input-File-Path");
        JTextField textFieldOutput = getTextField("Output-File-Path");
        JCheckBox checkBoxStrict = getCheckBox();

        if (textFieldInput == null || textFieldOutput == null || checkBoxStrict == null) {
            throw new IllegalStateException("Missing components!");
        }

        String inputPathName = textFieldInput.getText();
        String outputPathName = textFieldOutput.getText();
        boolean strictMode = checkBoxStrict.isSelected();

        Path inputPath = Path.of(inputPathName);
        if(!Files.isRegularFile(inputPath)) {
            String message = "Your input path is not valid.";
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Path outputPath = Path.of(outputPathName);
        if(Files.exists(outputPath)) {
            String message = "Your output path is not valid.";
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Main.convert(inputPath, outputPath, strictMode);
            String message = "Your file was successfully converted.";
            JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch(IOException | InvalidConfigurationException ex) {
            String message = "An error occurred:";
            JOptionPane.showMessageDialog(this, message + "\n\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private @Nullable JCheckBox getCheckBox() {
        return this.checkBoxMap.get("Strict-Mode");
    }
}
