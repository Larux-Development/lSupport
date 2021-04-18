package me.larux.lsupport.gui;

import org.bukkit.entity.Player;

import java.util.*;

public class SimpleGui implements Gui {

    private final Map<String, Integer> viewers = new HashMap<>();
    private final List<GuiPage> pages;
    private final String name;

    public SimpleGui(List<GuiPage> pages, GuiHandler provider) {
        this.pages=pages;
        this.name=UUID.randomUUID().toString();

        provider.get().put(name, this);
    }

    @Override
    public Map<String, Integer> getViewers() {
        return viewers;
    }

    @Override
    public List<GuiPage> getGuiPageList() {
        return pages;
    }

    @Override
    public GuiPage getActualGuiPage(Player player) {

        for (String key : viewers.keySet()) {

            if (key.equals(player.getUniqueId().toString())) {

                int pageNumber = viewers.get(key);
                return pages.get(pageNumber);
            }
        }
        return null;
    }

    @Override
    public void openMenu(Player player) {
        GuiPage page = pages.get(0);
        player.openInventory(page.getInventory());
        viewers.putIfAbsent(player.getUniqueId().toString(), 0);
    }

    @Override
    public void closeMenu(Player player) {
        player.closeInventory();
        viewers.remove(player.getUniqueId().toString());
    }

    @Override
    public void nextPage(Player player) {
        if (!lastPage(player)) {
            int pageNumber = viewers.get(player.getUniqueId().toString()) + 1;
            viewers.put(player.getUniqueId().toString(), pageNumber);
            GuiPage newPage = getActualGuiPage(player);
            player.openInventory(newPage.getInventory());
            viewers.put(player.getUniqueId().toString(), pageNumber);
        }
    }

    @Override
    public void previousPage(Player player) {
        if (!firstPage(player)) {
            int pageNumber = viewers.get(player.getUniqueId().toString()) - 1;
            viewers.put(player.getUniqueId().toString(), pageNumber);
            GuiPage newPage = getActualGuiPage(player);
            player.openInventory(newPage.getInventory());
            viewers.put(player.getUniqueId().toString(), pageNumber);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    private boolean lastPage(Player player) {
        if (viewers.containsKey(player.getUniqueId().toString())) {
            int actualPage = viewers.get(player.getUniqueId().toString());
            return actualPage==pages.size()-1;
        }
        return false;
    }

    private boolean firstPage(Player player) {
        if (viewers.containsKey(player.getUniqueId().toString())) {
            int actualPage = viewers.get(player.getUniqueId().toString());
            return actualPage==0;
        }
        return false;
    }

    public static class Builder implements Gui.Builder {

        private final List<GuiPage> pages = new ArrayList<>();

        private GuiHandler provider;

        @Override
        public Builder provider(GuiHandler provider) {
            this.provider=provider;
            return this;
        }

        @Override
        public Builder addPage(GuiPage page) {
            pages.add(page);
            return this;
        }

        @Override
        public Gui build() {
            return new SimpleGui(pages, provider);
        }
    }
}
