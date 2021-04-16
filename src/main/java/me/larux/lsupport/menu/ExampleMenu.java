package me.larux.lsupport.menu;

import me.larux.lsupport.gui.Gui;
import me.larux.lsupport.gui.SimpleGui;
import me.larux.lsupport.gui.SimpleGuiPage;
import me.larux.lsupport.gui.item.SimpleGuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ExampleMenu {

    public void openMenuToPlayer(Player player) {
        Gui gui = new SimpleGui.Builder()
                .addPage(
                        new SimpleGuiPage.Builder("test menu", 27)
                                .addItem(new SimpleGuiItem.Builder()
                                        .slot(4)
                                        .item(new ItemStack(Material.COAL))
                                        .action((inventoryClickEvent, guii) -> {
                                        })
                                        .build())
                                .build())
                .build();

        gui.openMenu(player);

    }
}
