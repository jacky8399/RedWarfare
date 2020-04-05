package me.libraryaddict.core.inventory;

import me.libraryaddict.core.C;
import me.libraryaddict.core.inventory.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ConfirmInventory extends BasicInventory
{
    public ConfirmInventory(Player player, ItemStack displayItem, Runnable onPurchase, Runnable onCancel)
    {
        super(player, "Confirm Purchase");

        addItem(4, displayItem);

        addButton(20, new ItemBuilder(Material.LIME_WOOL).setTitle(C.DGreen + C.Bold + "CONFIRM").build(), clickType -> {
            onPurchase.run();
            return true;
        });

        addButton(24, new ItemBuilder(Material.RED_WOOL).setTitle(C.DRed + C.Bold + "CANCEL").build(), clickType -> {
            onCancel.run();
            return true;
        });
    }

}
