package cn.dreeam.surf.modules.antiillegal;

import cn.dreeam.surf.Surf;
import cn.dreeam.surf.config.Config;
import cn.dreeam.surf.checks.CheckManager;
import cn.dreeam.surf.util.ItemUtil;
import cn.dreeam.surf.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CheckIllegal implements Listener {


    @EventHandler(ignoreCancelled = true)
    @AntiIllegal(EventName = "PlayerJoinEvent")
    private void onJoin(PlayerJoinEvent event) {
        if (!Config.antiIllegalCheckWhenPlayerJoinEnabled) return;

        Player player = event.getPlayer();
        Inventory inv = player.getInventory();

        // 使用新的CheckManager进行检查，支持OP跳过
        CheckManager.cleanInventory(inv, player.getName(), CheckManager.canBypassCheck(player));
    }

    @EventHandler(ignoreCancelled = true)
    @AntiIllegal(EventName = "InventoryMoveItemEvent")
    private void onInventoryMove(InventoryMoveItemEvent event) {
        if (!Config.antiIllegalCheckWhenHopperTransferEnabled) return;

        Inventory inv = event.getSource();

        if (!inv.getType().equals(InventoryType.CRAFTING)) return;

        // 漏斗传输不需要OP跳过检查
        CheckManager.cleanInventory(inv, inv.getType().name(), false);
    }

    @EventHandler
    @AntiIllegal(EventName = "InventoryCloseEvent")
    private void onInventoryClose(InventoryCloseEvent event) {
        if (!Config.antiIllegalCheckWhenInventoryCloseEnabled) return;

        Player player = (Player) event.getPlayer();
        Inventory inv = player.getInventory();

        if (!inv.getType().equals(InventoryType.PLAYER)) return;

        // 使用新的CheckManager进行检查，支持OP跳过
        CheckManager.cleanInventory(inv, player.getName(), CheckManager.canBypassCheck(player));
    }

    @EventHandler
    @AntiIllegal(EventName = "InventoryOpenEvent")
    private void onInventoryOpen(InventoryOpenEvent event) {
        if (!Config.antiIllegalCheckWhenInventoryOpenEnabled) return;

        Player player = (Player) event.getPlayer();
        Inventory inv = player.getInventory();

        if (!inv.getType().equals(InventoryType.PLAYER)) return;

        // 使用新的CheckManager进行检查，支持OP跳过
        CheckManager.cleanInventory(inv, player.getName(), CheckManager.canBypassCheck(player));
    }

    @EventHandler(ignoreCancelled = true)
    @AntiIllegal(EventName = "BlockDispenseArmorEvent")
    private void onDispenseEquip(BlockDispenseArmorEvent event) {
        if (!Config.antiIllegalCheckWhenItemPickupEnabled) return;

        ItemStack i = event.getItem();

        // 使用新的CheckManager进行检查
        if (CheckManager.isIllegal(i)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    @AntiIllegal(EventName = "EntityPickupItemEvent")
    private void onPickup(EntityPickupItemEvent event) {
        if (Surf.getInstance().isRoseStackerEnabled) return;

        if (!Config.antiIllegalCheckWhenItemPickupEnabled) return;

        ItemStack i = event.getItem().getItemStack();

        // 使用新的CheckManager进行检查
        if (CheckManager.isIllegal(i)) {
            // 如果是玩家且可以跳过检查，则允许拾取
            if (event.getEntity() instanceof Player player && CheckManager.canBypassCheck(player)) {
                return;
            }
            
            event.setCancelled(true);
            event.getItem().remove();
            if (event.getEntity() instanceof Player player) {
                MessageUtil.sendMessage(player, "&6你不能拾取这个违法物品。");
            } else {
                MessageUtil.println(String.format(
                        "%s 尝试拾取违法物品在 %s",
                        event.getEntity().getName().toLowerCase(),
                        MessageUtil.locToString(event.getItem().getLocation())
                ));
            }
        }
    }
}