package me.itswagpvp.banknotesplus.events;

import me.itswagpvp.banknotesplus.BanknotesPlus;
import me.itswagpvp.banknotesplus.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class RightClickClaimNote implements Listener {

    private static BanknotesPlus plugin;

    @EventHandler
    public void onPlayerClaimNote(org.bukkit.event.player.PlayerInteractEvent event) {

        // Check if we need to use /deposit or if we can right click
        if (!plugin.getConfig().getBoolean("Allow-Right-Click-To-Deposit-Notes", true)) {
            return;
        }

        // Check the action
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        // Verify that this is a real banknote
        if (Utils.isBanknote(item)) {
            return;
        }

        long amount = Utils.getBanknoteAmount(item);

        // Negative banknotes are not allowed
        if (Double.compare(amount, 0) < 0) {
            return;
        }

        BanknotesPlus.getEconomy().depositPlayer(player, (double) amount);

        // Deposit the money
        player.sendMessage(plugin.getMessage("Redeemed")
                .replace("%money%", "" + amount));

        event.setCancelled(true);

    }
}
