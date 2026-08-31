/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.gui.tabs.builtin;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.tabs.WindowTabScreen;
import meteordevelopment.meteorclient.gui.widgets.WItemWithLabel;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.scripting.PotionScripting;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Items;
import net.minecraft.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Addon manager, laid out like the vanilla resource pack screen: an "Available" list and
 * an "Enabled" list, with buttons to move addons between them.
 */
public class PotionTab extends Tab {
    public PotionTab() {
        super("Potion");
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new PotionScreen(theme, this);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof PotionScreen;
    }

    private static class PotionScreen extends WindowTabScreen {
        private final Set<String> enabled = new LinkedHashSet<>(PotionScripting.getEnabledNames());

        private WContainer availableList;
        private WContainer enabledList;

        public PotionScreen(GuiTheme theme, Tab tab) {
            super(theme, tab);
        }

        @Override
        public void initWidgets() {
            add(theme.label("Addons folder: " + PotionScripting.getAddonsDir().getAbsolutePath())).expandX();
            add(theme.label("Changes require a game restart to take effect.")).expandX();

            add(theme.horizontalSeparator()).expandX();

            WHorizontalList lists = add(theme.horizontalList()).expandX().widget();

            WContainer availableColumn = lists.add(theme.verticalList()).expandX().widget();
            availableColumn.add(theme.label("Available")).expandX();
            availableList = availableColumn.add(theme.verticalList()).expandX().minWidth(220).widget();

            WContainer enabledColumn = lists.add(theme.verticalList()).expandX().widget();
            enabledColumn.add(theme.label("Enabled")).expandX();
            enabledList = enabledColumn.add(theme.verticalList()).expandX().minWidth(220).widget();

            reloadTables();

            add(theme.horizontalSeparator()).expandX();

            WHorizontalList row1 = add(theme.horizontalList()).expandX().widget();

            WButton addAddon = row1.add(theme.button("Add Addon")).expandX().widget();
            addAddon.tooltip = "Creates a new addon script from a template in the addons folder.";
            addAddon.action = () -> {
                try {
                    File file = PotionScripting.createNewAddon();
                    enabled.add(file.getName());
                    PotionScripting.setEnabledNames(enabled);
                    ChatUtils.info("Created (highlight)%s(default). Edit it, then restart the game to load it.", file.getName());
                    reloadTables();
                } catch (IOException e) {
                    MeteorClient.LOG.error("Failed to create a new addon.", e);
                    ChatUtils.error("Failed to create a new addon: %s", e.getMessage());
                }
            };

            WButton openFolder = row1.add(theme.button("Open Addons Folder")).expandX().widget();
            openFolder.tooltip = "Opens the addons folder in your file explorer.";
            openFolder.action = () -> Util.getOperatingSystem().open(PotionScripting.getAddonsDir());

            WButton refresh = row1.add(theme.button("Refresh List")).expandX().widget();
            refresh.tooltip = "Rescans the addons folder for addon scripts.";
            refresh.action = this::reloadTables;
        }

        private void reloadTables() {
            availableList.clear();
            enabledList.clear();

            File[] scripts = PotionScripting.listAddonScripts();

            boolean anyAvailable = false;
            boolean anyEnabled = false;

            for (File script : scripts) {
                String name = script.getName();

                if (enabled.contains(name)) {
                    addRow(enabledList, name, "Disable", () -> {
                        enabled.remove(name);
                        PotionScripting.setEnabledNames(enabled);
                        reloadTables();
                    });
                    anyEnabled = true;
                } else {
                    addRow(availableList, name, "Enable", () -> {
                        enabled.add(name);
                        PotionScripting.setEnabledNames(enabled);
                        reloadTables();
                    });
                    anyAvailable = true;
                }
            }

            if (!anyAvailable) availableList.add(theme.label("(none)"));
            if (!anyEnabled) enabledList.add(theme.label("(none)"));
        }

        private void addRow(WContainer column, String name, String buttonText, Runnable action) {
            WHorizontalList row = column.add(theme.horizontalList()).expandX().widget();

            WItemWithLabel icon = row.add(theme.itemWithLabel(Items.PAPER.getDefaultStack(), name)).expandCellX().widget();
            icon.tooltip = "Potion addon script";

            WButton button = row.add(theme.button(buttonText)).widget();
            button.action = action;
        }
    }
}
