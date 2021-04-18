package me.larux.lsupport.menu;

import me.larux.lsupport.LaruxSupportCore;
import me.larux.lsupport.file.FileCreator;
import me.larux.lsupport.gui.GuiPage;
import me.larux.lsupport.gui.SimpleGui;
import me.larux.lsupport.gui.SimpleGuiPage;
import me.larux.lsupport.gui.item.GuiItem;
import me.larux.lsupport.gui.item.SimpleGuiItem;
import me.larux.lsupport.gui.item.buttom.Button;
import me.larux.lsupport.gui.item.buttom.NextPageButton;
import me.larux.lsupport.gui.item.buttom.PreviousPageButton;
import me.larux.lsupport.storage.object.Partner;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class LaruxSupportMenu {

    private final LaruxSupportCore core;

    public LaruxSupportMenu(LaruxSupportCore core) {
        this.core = core;
    }

    public void openMenu(Player player) {

        FileCreator menuFile = core.getMenu();
        int slots = menuFile.getInt("menu.slots");

        SimpleGui.Builder gui = new SimpleGui.Builder().provider(core.getGui());
        GuiPage.Builder page = new SimpleGuiPage.Builder(menuFile.getString("menu.name"), slots);

        List<Integer> occupiedSlots = menuFile.getIntegerList("menu.excluded");
        List<Integer> menuOccupied = new ArrayList<>();

        if (menuFile.getBoolean("menu.back-page.enabled")) {
            menuOccupied.add(menuFile.getInt("menu.back-page.slot"));
            page.addButton(buildBackPageItem());
        }

        if (menuFile.getBoolean("menu.next-page.enabled")) {
            menuOccupied.add(menuFile.getInt("menu.next-page.slot"));
            page.addButton(buildNextPageItem());
        }

        for (Partner partner : core.getStorage().get().values()) {
            if (menuOccupied.size() + occupiedSlots.size() <= slots-1) {
                for (int i = 0 ; i < slots ; i++) {
                    if (!occupiedSlots.contains(i) && !menuOccupied.contains(i)) {
                        page.addItem(buildGuiItem(i, partner));
                        menuOccupied.add(i);
                        break;
                    }
                }
            } else {
                gui.addPage(page.build());
                page = new SimpleGuiPage.Builder(menuFile.getString("menu.name"), slots);
                menuOccupied.clear();
                menuOccupied.add(menuFile.getInt("menu.back-page.slot"));
                page.addButton(buildBackPageItem());
                menuOccupied.add(menuFile.getInt("menu.next-page.slot"));
                page.addButton(buildNextPageItem());
                for (int i = 0 ; i < slots ; i++) {
                    if (!occupiedSlots.contains(i) && !menuOccupied.contains(i)) {
                        page.addItem(buildGuiItem(i, partner));
                        menuOccupied.add(i);
                        break;
                    }
                }
            }
        }

        gui.addPage(page.build());
        gui.build().openMenu(player);
    }

    private GuiItem buildGuiItem(int slot, Partner partner) {
        FileCreator menuFile = core.getMenu();
        ItemStack item;

        if (menuFile.getString("menu.item.type").equalsIgnoreCase("SKULL")) {
            item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            skullMeta.setOwner(partner.getPlayerFromId().getName());
            buildItemLoreAndName(skullMeta, partner);
            item.setItemMeta(skullMeta);
        } else {
            item = new ItemStack(Material.valueOf(menuFile.getString("menu.item.material")));
            ItemMeta itemMeta = item.getItemMeta();
            buildItemLoreAndName(itemMeta, partner);
            item.setItemMeta(itemMeta);
        }
        return new SimpleGuiItem.Builder()
                .slot(slot)
                .item(item)
                .action((inventoryClickEvent, gui) -> {
                            partner.setPartners(partner.getPartners() + 1);
                            Player player = (Player) inventoryClickEvent.getWhoClicked();
                            gui.closeMenu(player);
                            player.sendMessage(core.getLang().getString("messages.menu.now-supporting")
                                    .replace("%partner_name%", partner.getPlayerFromId().getName()));
                        }
                ).build();
    }

    private Button buildBackPageItem() {
        FileCreator menuFile = core.getMenu();
        ItemStack item = new ItemStack(Material.valueOf(menuFile.getString("menu.back-page.material")));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(menuFile.getString("menu.back-page.name"));
        meta.setLore(buildLore("back-page"));
        item.setItemMeta(meta);
        return new PreviousPageButton(menuFile.getInt("menu.back-page.slot"), item, true, true);
    }

    private Button buildNextPageItem() {
        FileCreator menuFile = core.getMenu();
        ItemStack item = new ItemStack(Material.valueOf(menuFile.getString("menu.next-page.material")));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(menuFile.getString("menu.next-page.name"));
        meta.setLore(buildLore("next-page"));
        item.setItemMeta(meta);
        return new NextPageButton(menuFile.getInt("menu.next-page.slot"), item, true, true);
    }

    private void buildItemLoreAndName(ItemMeta meta, Partner partner) {
        FileCreator menuFile = core.getMenu();
        meta.setDisplayName(menuFile.getString("menu.item.name")
                .replace("%partners%", String.valueOf(partner.getPartners()))
                .replace("%partner_name%", partner.getPlayerFromId().getName()));
        meta.setLore(buildLore(partner));
    }

    private List<String> buildLore(Partner partner) {
        FileCreator menuFile = core.getMenu();
        List<String> newLore = new ArrayList<>();
        List<String> lore = menuFile.getStringList("menu.item.lore");
        for (String str : lore) {
            newLore.add(str
                    .replace("&", "§")
                    .replace("%partners%", String.valueOf(partner.getPartners()))
                    .replace("%partner_name%", partner.getPlayerFromId().getName()));
        }
        return newLore;
    }

    private List<String> buildLore(String key) {
        FileCreator menuFile = core.getMenu();
        List<String> newLore = new ArrayList<>();
        List<String> lore = menuFile.getStringList("menu." + key + ".lore");
        for (String str : lore) {
            newLore.add(str
                    .replace("&", "§"));
        }
        return newLore;
    }
}
