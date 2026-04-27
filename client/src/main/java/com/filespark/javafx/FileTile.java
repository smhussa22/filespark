// a part of the file grid displaying a singular file along with its preview

package com.filespark.javafx;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.filespark.Config;
import com.filespark.client.UploadManager;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class FileTile extends StackPane {

    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private Node videoPoster;

    public FileTile(File file) {

        this(file, null);

    }

    public FileTile(File file, Runnable onDeleted) {

        String fileName = file.getName();

        MenuItem uploadItem = new MenuItem("Upload As Embedded Link");
        MenuItem showItem = new MenuItem("Show In Folder");
        MenuItem deleteItem = new MenuItem("Delete From Disk");

        ContextMenu menu = new ContextMenu(uploadItem, showItem, deleteItem);
        menu.getStyleClass().add("context-menu");
        uploadItem.getStyleClass().add("menu-item");
        showItem.getStyleClass().add("menu-item");
        deleteItem.getStyleClass().add("menu-item");

        uploadItem.setOnAction(event -> { UploadManager.startUpload(file); });
        showItem.setOnAction(event -> showInFolder(file));
        deleteItem.setOnAction(event -> handleDelete(file, onDeleted));

        Rectangle clip = new Rectangle(Config.fileTileWidth, Config.fileTileHeight);
        clip.setArcWidth(10);
        clip.setArcHeight(10);

        Rectangle border = new Rectangle(Config.fileTileWidth, Config.fileTileHeight);
        border.setFill(Color.web(Config.bgSurface));
        border.setStroke(Color.web(Config.borderSubtle));
        border.setStrokeWidth(1);
        border.setArcWidth(10);
        border.setArcHeight(10);

        String mimeType = probeMime(file);
        Node previewNode = buildPreview(file, mimeType, clip);

        StackPane imageHolder = new StackPane(border, previewNode);
        Label label = new Label(shortenFileName(fileName));

        label.setStyle("-fx-font-size: 12px; -fx-font-weight: 500;");
        label.setTextFill(Color.web(Config.textPrimary));

        VBox layout = new VBox(Config.space2, imageHolder, label);

        layout.setAlignment(Pos.CENTER);
        getChildren().add(layout);

        layout.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {

            if (event.getButton() == MouseButton.SECONDARY) {

                menu.show(this, event.getScreenX(), event.getScreenY());

            }
            else {

                menu.hide();

            }

        });

        layout.setOnMouseEntered(event -> {

            layout.setCursor(javafx.scene.Cursor.HAND);
            border.setStroke(Color.web(Config.borderStrong));
            startVideo();

        });

        layout.setOnMouseExited(event -> {

            layout.setCursor(javafx.scene.Cursor.DEFAULT);
            border.setStroke(Color.web(Config.borderSubtle));
            stopVideo();

        });

    }

    private Node buildPreview(File file, String mimeType, Rectangle clip) {

        if (mimeType != null && mimeType.startsWith("image/")) {

            ImageView imageView = new ImageView();
            Image preview = new Image(file.toURI().toString(), Config.fileTileWidth, Config.fileTileHeight, true, true, true);
            preview.errorProperty().addListener((obs, old, error) -> {
                if (error) imageView.setImage(getDefaultIcon(file));
            });
            imageView.setImage(preview);
            imageView.setFitWidth(Config.fileTileWidth);
            imageView.setFitHeight(Config.fileTileHeight);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setClip(clip);
            return imageView;

        }

        if (mimeType != null && mimeType.startsWith("video/")) {

            try {

                Media media = new Media(file.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setMute(true);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

                final boolean[] posterCaptured = { false };

                // play briefly on READY so the first frame renders into MediaView, then pause.
                // After that flag is set, subsequent play()/pause() calls behave normally for hover.
                mediaPlayer.setOnReady(() -> mediaPlayer.play());
                mediaPlayer.setOnPlaying(() -> {
                    if (!posterCaptured[0]) {
                        posterCaptured[0] = true;
                        // small delay so a frame is actually rendered before pausing
                        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.millis(80));
                        delay.setOnFinished(ev -> {
                            mediaPlayer.pause();
                            mediaPlayer.seek(javafx.util.Duration.ZERO);
                        });
                        delay.play();
                    }
                });

                mediaPlayer.setOnError(() -> {
                    if (mediaView != null) mediaView.setVisible(false);
                    if (videoPoster != null) videoPoster.setVisible(true);
                });

                mediaView = new MediaView(mediaPlayer);
                mediaView.setFitWidth(Config.fileTileWidth);
                mediaView.setFitHeight(Config.fileTileHeight);
                mediaView.setPreserveRatio(true);
                mediaView.setSmooth(true);
                mediaView.setClip(clip);
                mediaView.setVisible(true);

                // fallback shown only if MediaPlayer errors (codec unsupported)
                videoPoster = iconNode(file);
                videoPoster.setVisible(false);

                StackPane wrap = new StackPane(mediaView, videoPoster);
                wrap.setMinSize(Config.fileTileWidth, Config.fileTileHeight);
                wrap.setPrefSize(Config.fileTileWidth, Config.fileTileHeight);
                wrap.setMaxSize(Config.fileTileWidth, Config.fileTileHeight);
                return wrap;

            }
            catch (Exception ignored) {

                // fall through to icon

            }

        }

        return iconNode(file);

    }

    private Node iconNode(File file) {

        ImageView iv = new ImageView(getDefaultIcon(file));
        iv.setFitWidth(64);
        iv.setFitHeight(64);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setOpacity(0.85);
        return iv;

    }

    private void startVideo() {

        if (mediaPlayer == null) return;
        try {
            mediaPlayer.seek(javafx.util.Duration.ZERO);
            mediaPlayer.play();
        } catch (Exception ignored) {}

    }

    private void stopVideo() {

        if (mediaPlayer == null) return;
        try {
            mediaPlayer.pause();
            mediaPlayer.seek(javafx.util.Duration.ZERO);
        } catch (Exception ignored) {}

    }

    public void disposePreview() {

        if (mediaPlayer != null) {
            try { mediaPlayer.stop(); } catch (Exception ignored) {}
            try { mediaPlayer.dispose(); } catch (Exception ignored) {}
            mediaPlayer = null;
        }

    }

    private String probeMime(File file) {

        try {
            return Files.probeContentType(file.toPath());
        } catch (IOException ignore) {
            return null;
        }

    }

    private void showInFolder(File file) {

        try {

            new ProcessBuilder("explorer.exe", "/select,", file.getAbsolutePath()).start();

        }
        catch (Exception ignored) {}

    }

    private void handleDelete(File file, Runnable onDeleted) {

        boolean confirmed = ConfirmDialog.show(
            getScene() != null ? getScene().getWindow() : null,
            "Delete file from disk?",
            "This will permanently delete \"" + file.getName() + "\" from your computer. This cannot be undone.",
            "Delete"
        );
        if (!confirmed) return;

        try {

            java.nio.file.Files.delete(file.toPath());
            disposePreview();
            NotificationService.show(new BaseNotification("Deleted: " + file.getName(), "success.png"));
            if (onDeleted != null) onDeleted.run();

        }
        catch (Exception ex) {

            NotificationService.show(new BaseNotification("Delete failed: " + ex.getMessage(), "error.png"));

        }

    }

    private String shortenFileName(String fileName) {

        if (fileName.length() <= Config.fileTileNameLength) return fileName;

        int dotExtension = fileName.lastIndexOf('.');
        if (dotExtension <= 0) return fileName.substring(0, Config.fileTileNameLength - 3) + "...";

        String name = fileName.substring(0, dotExtension);
        String extension = fileName.substring(dotExtension);

        int allowedNameLength = Config.fileTileNameLength - extension.length() - 3;
        if (allowedNameLength <= 0) {

            return "..." + extension.substring(extension.length(), (Config.fileTileNameLength - 3));

        }

        return name.substring (0, allowedNameLength) + "..." + extension;

    }

    private Image getDefaultIcon(File file) {

        String path = getMimeIconPath(file);
        return new Image(getClass().getResourceAsStream(path));

    }

    private String getMimeIconPath(File file) {

        try {

            String mimeType = Files.probeContentType(file.toPath());

            if (mimeType == null) return "/icons/default.png";
            if (mimeType.startsWith("image/")) return "/icons/image.png";
            if (mimeType.startsWith("video/")) return "/icons/video.png";
            if (mimeType.startsWith("application/")) return "/icons/application.png";
            if (mimeType.startsWith("audio/")) return "/icons/audio.png";

        }
        catch (IOException ignore) {}

        return "/icons/default.png";

    }


}
