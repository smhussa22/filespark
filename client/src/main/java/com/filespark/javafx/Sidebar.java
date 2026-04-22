package com.filespark.javafx;

import java.util.function.Consumer;

import com.filespark.Config;
import com.filespark.client.User;

import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {

    public enum Nav { DOWNLOADS, LINK_DASHBOARD, CLIENT_SETTINGS, HOTKEY_SETTINGS }

    private SidebarItem selectedItem = null;

    public Sidebar(User user, Consumer<Nav> onNavigate) {

        setPrefWidth(250);
        setPadding(new Insets(20));
        setSpacing(12);

        setStyle(
            "-fx-background-color: " + Config.mainBlack + ";" +
            "-fx-border-color: transparent " + Config.mainGrey + " transparent transparent;" +
            "-fx-border-width: 0 1px 0 0;"
        );

        SidebarSection browse = new SidebarSection("Browse");
        SidebarItem downloads = item("sitem-downloads.png", "Downloads", onNavigate, Nav.DOWNLOADS);
        SidebarItem dashboard = item("sitem-default.png", "Link Dashboard", onNavigate, Nav.LINK_DASHBOARD);
        browse.addItem(downloads);
        browse.addItem(dashboard);

        SidebarSection settings = new SidebarSection("Settings");
        SidebarItem client = item("sitem-default.png", "Client Settings", onNavigate, Nav.CLIENT_SETTINGS);
        SidebarItem hotkey = item("sitem-default.png", "Hotkey Settings", onNavigate, Nav.HOTKEY_SETTINGS);
        settings.addItem(client);
        settings.addItem(hotkey);

        Region space = new Region();
        VBox.setVgrow(space, Priority.ALWAYS);

        UserPanel userPanel = new UserPanel(user);

        getChildren().addAll(browse, settings, space, userPanel);
        setSelect(downloads);

    }

    private SidebarItem item(String icon, String label, Consumer<Nav> onNavigate, Nav nav) {

        SidebarItem item = new SidebarItem(icon, label);
        item.setOnMouseClicked(e -> {
            setSelect(item);
            if (onNavigate != null) onNavigate.accept(nav);
        });
        return item;

    }

    private void setSelect(SidebarItem item) {

        if (selectedItem != null) selectedItem.setSelected(false);
        selectedItem = item;
        item.setSelected(true);

    }

}
