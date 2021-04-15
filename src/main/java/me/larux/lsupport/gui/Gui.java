package me.larux.lsupport.gui;

import me.raider.commons.utils.Nameable;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public interface Gui extends Nameable {

    Map<String, Integer> getViewers();

    List<GuiPage> getGuiPageList();

    GuiPage getActualGuiPage(Player player);

    void openMenu(Player player);

    void closeMenu(Player player);

    void nextPage(Player player);

    void previousPage(Player player);

    interface Builder {

        static Builder create() {
            return new SimpleGui.Builder();
        }

        Builder provider(GuiHandler provider);

        Builder addPage(GuiPage page);

        Gui build();

    }

}
