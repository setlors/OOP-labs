package logic;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Editor {
    private static class Holder {
        static final Editor INSTANCE = new Editor();
    }

    public static Editor getInstance() {
        return Holder.INSTANCE;
    }
    private Editor() {
    }

    private final UndoManager undo = new UndoManager();
    private JTextArea area;
    private File file;

    public void attach(JTextArea a) {
        area = a;
        area.getDocument().addUndoableEditListener(undo);
    }

    public File getFile() {
        return file;
    }

    public void newDocument() {
        area.setText("");
        undo.discardAllEdits();
        file = null;
    }
    public void load(File f) {
        try {
            area.setText(Files.readString(f.toPath()));
            area.setCaretPosition(0);
            undo.discardAllEdits();
            file = f;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(File f) {
        try {
            Files.writeString(f.toPath(), area.getText());
            file = f;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void undo() {
        if (undo.canUndo()) undo.undo();
    }
    public void redo() {
        if (undo.canRedo()) undo.redo();
    }
    public void process(int op) {
        String selected = area.getSelectedText();
        if (selected == null) {
            area.setText(TextOperations.apply(op, area.getText()));
        } else {
            area.replaceSelection(TextOperations.apply(op, selected));
        }
    }
    public boolean find(String what) {
        String text = area.getText();
        int i = text.indexOf(what, area.getSelectionEnd());
        if (i < 0) i = text.indexOf(what);
        if (i < 0) return false;
        area.select(i, i + what.length());
        return true;
    }
    public void replaceAll(String what, String with) {
        area.setText(area.getText().replace(what, with));
    }

    public String stats() {
        String text = area.getText();
        String trimmed = text.trim();
        int words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
        int lines = text.isEmpty() ? 0 : text.split("\n", -1).length;
        return "Символів: " + text.length() + "\n"
                + "Слів: " + words + "\n"
                + "Рядків: " + lines;
    }
}