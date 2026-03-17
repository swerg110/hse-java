package hse.java.commander;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.nio.file.*;
import java.util.stream.Stream;

public class MainController {

    @FXML public ListView<String> left;
    @FXML public ListView<String> right;
    @FXML public Label leftLabel;
    @FXML public Label rightLabel;
    @FXML public Button copy;
    @FXML public Button move;
    @FXML public Button delete;

    private Path leftPath;
    private Path rightPath;
    private ListView<String> activePanel;

    public void setInitialDirs(Path leftStart, Path rightStart) {
        leftPath = leftStart;
        rightPath = rightStart;
        update();
    }

    public void initialize() {
        activePanel = left;

        left.setOnMouseClicked(e -> {
            activePanel = left;
            if (e.getClickCount() == 2) open(left);
        });

        right.setOnMouseClicked(e -> {
            activePanel = right;
            if (e.getClickCount() == 2) open(right);
        });

        copy.setOnAction(e -> copy());
        move.setOnAction(e -> move());
        delete.setOnAction(e -> delete());
    }

    private void dir(ListView<String> panel, Path path) {
        panel.getItems().clear();
        panel.getItems().add("...");
        try (Stream<Path> f = Files.list(path)) {
            f.map(p -> p.getFileName().toString()).sorted().forEach(panel.getItems()::add);
        } catch (Exception e) {}
        Label label = panel == left ? leftLabel : rightLabel;
        if (label != null) label.setText(path.toString());
    }

    private void update() {
        dir(left, leftPath);
        dir(right, rightPath);
    }

    private void open(ListView<String> panel) {
        String name = panel.getSelectionModel().getSelectedItem();
        if (name == null) return;

        Path curr = panel == left ? leftPath : rightPath;

        if (name.equals("...")) {
            Path parent = curr.getParent();
            if (parent != null) {
                if (panel == left) leftPath = parent;
                else rightPath = parent;
                update();
            }
            return;
        }

        Path next = curr.resolve(name);
        if (Files.isDirectory(next)) {
            if (panel == left) leftPath = next;
            else rightPath = next;
            update();
        }
    }

    @FXML
    public void copy() {
        if (activePanel == null) return;
        String name = activePanel.getSelectionModel().getSelectedItem();
        if (name == null || name.equals("...")) return;

        Path src = (activePanel == left ? leftPath : rightPath).resolve(name);
        Path dst = (activePanel == left ? rightPath : leftPath).resolve(name);

        try {
            Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {}
        update();
    }

    @FXML
    public void move() {
        if (activePanel == null) return;
        String name = activePanel.getSelectionModel().getSelectedItem();
        if (name == null || name.equals("...")) return;

        Path src = (activePanel == left ? leftPath : rightPath).resolve(name);
        Path dst = (activePanel == left ? rightPath : leftPath).resolve(name);

        try {
            Files.move(src, dst, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {}
        update();
    }

    @FXML
    public void delete() {
        if (activePanel == null) return;
        String name = activePanel.getSelectionModel().getSelectedItem();
        if (name == null || name.equals("...")) return;

        Path file = (activePanel == left ? leftPath : rightPath).resolve(name);

        try {
            Files.deleteIfExists(file);
        } catch (Exception e) {}
        update();
    }
}
