package xyz.sirblobman.application.yaml.legacy.minimessage.menu;

import java.io.File;
import java.util.Objects;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class ExistingExtensionFilter extends FileFilter {
    private final FileNameExtensionFilter originalFilter;

    public ExistingExtensionFilter(FileNameExtensionFilter originalFilter) {
        this.originalFilter = Objects.requireNonNull(originalFilter, "originalFilter must not be null!");
    }

    @Override
    public boolean accept(File f) {
        return this.originalFilter.accept(f) && f.exists();
    }

    @Override
    public String getDescription() {
        return this.originalFilter.getDescription();
    }
}
