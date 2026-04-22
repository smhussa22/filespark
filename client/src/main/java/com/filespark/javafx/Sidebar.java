package com.filespark.javafx;

import com.filespark.Config;
import com.filespark.client.User;

import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {

    // the one that is currently selected
    private SidebarItem selectedItem = null;

    public Sidebar(User user) {

        setPrefWidth(250);
        setPadding(new Insets(20));
        setSpacing(12);
        //@todo: sidebar border disappears when out of focus

        setStyle(
            "-fx-background-color: " + Config.mainBlack + ";" +
            "-fx-border-color: transparent " + Config.mainGrey + " transparent transparent;" +
            "-fx-border-width: 0 1px 0 0;"
        );

        SidebarSection browse = new SidebarSection("Browse");
        SidebarItem downloads = item("sitem-downloads.png", "Downloads");
        SidebarItem dashboard = item("sitem-default.png", "Link Dashboard");
        browse.addItem(downloads);
        browse.addItem(dashboard);

        SidebarSection settings = new SidebarSection("Settings");
        SidebarItem client = item("sitem-default.png", "Client Settings");
        SidebarItem hotkey = item("sitem-default.png", "Hotkey Settings");
        settings.addItem(client);
        settings.addItem(hotkey);

        Region space = new Region();
        VBox.setVgrow(space, Priority.ALWAYS);

        UserPanel userPanel = new UserPanel(user);

        getChildren().addAll(browse, settings, space, userPanel);
        setSelect(downloads); // default to downloads

    }

    private SidebarItem item (String icon, String label) {

        SidebarItem item = new SidebarItem(icon, label);
        item.setOnMouseClicked(e -> setSelect(item));
        return item;

    }

    private void setSelect(SidebarItem item){

        if (selectedItem != null) selectedItem.setSelected(false);
        selectedItem = item;
        item.setSelected(true);
        
    }

}
