package cn.dreeam.surf.modules.patch;

import cn.dreeam.surf.config.Config;
import cn.dreeam.surf.checks.CheckManager;
import cn.dreeam.surf.util.MessageUtil;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

public class ChunkBan implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (!Config.perChunkLimitEnabled) return;

        Block block = event.getBlock();
        Player player = event.getPlayer();
        Chunk chunk = block.getChunk();

        // 检查OP跳过权限
        if (CheckManager.canBypassCheck(player) || player.hasPermission("surf.bypass.chunkban")) return;

        if (isTileEntity(block.getType()) && chunk.getTileEntities().length > Config.perChunkLimitTitleEntityMax) {
            event.setCancelled(true);
            MessageUtil.sendMessage(player, Config.perChunkLimitMessage);
            return;
        }

        if (isSkull(block.getType())) {
            // get chunk skull count
            long skullCount = Arrays.stream(chunk.getTileEntities()).filter(tileEntity -> isSkull(tileEntity.getType())).count();
            if (skullCount > Config.perChunkLimitSkullMax) {
                event.setCancelled(true);
                MessageUtil.sendMessage(player, Config.perChunkLimitMessage);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSpawn(PlayerInteractEvent event) {
        if (!Config.perChunkLimitEnabled || event.getClickedBlock() == null || event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getItem() == null) return;

        Player player = event.getPlayer();
        // 检查OP跳过权限
        if (CheckManager.canBypassCheck(player) || player.hasPermission("surf.bypass.chunkban")) return;

        Chunk chunk = event.getClickedBlock().getChunk();
        if (event.getItem().getType().equals(XMaterial.ITEM_FRAME.get())) {
            long amount = Arrays.stream(chunk.getEntities()).filter(entity -> entity instanceof ItemFrame).count();
            if (amount + chunk.getTileEntities().length > Config.perChunkLimitTitleEntityMax) {
                event.setCancelled(true);
                MessageUtil.sendMessage(event.getPlayer(), Config.perChunkLimitMessage);
            }
        }
    }

    private boolean isTileEntity(Material m) {
        return switch (m) {
            // 容器类
            case FURNACE, BLAST_FURNACE, SMOKER, TRAPPED_CHEST, CHEST, ENDER_CHEST, HOPPER, DROPPER, DISPENSER, BREWING_STAND, BARREL, SHULKER_BOX,
                 // 标牌类
                 ACACIA_SIGN, ACACIA_WALL_SIGN, BIRCH_SIGN, BIRCH_WALL_SIGN, DARK_OAK_SIGN, DARK_OAK_WALL_SIGN, JUNGLE_SIGN, JUNGLE_WALL_SIGN,
                 OAK_SIGN, OAK_WALL_SIGN, SPRUCE_SIGN, SPRUCE_WALL_SIGN, CRIMSON_SIGN, CRIMSON_WALL_SIGN, WARPED_SIGN, WARPED_WALL_SIGN,
                 MANGROVE_SIGN, MANGROVE_WALL_SIGN, BAMBOO_SIGN, BAMBOO_WALL_SIGN, CHERRY_SIGN, CHERRY_WALL_SIGN,
                 // 旗帜类
                 BLACK_BANNER, BLUE_BANNER, BROWN_BANNER, CYAN_BANNER, GRAY_BANNER, GREEN_BANNER, LIGHT_BLUE_BANNER, LIGHT_GRAY_BANNER,
                 LIME_BANNER, MAGENTA_BANNER, ORANGE_BANNER, PINK_BANNER, PURPLE_BANNER, RED_BANNER, WHITE_BANNER, YELLOW_BANNER,
                 BLACK_WALL_BANNER, BLUE_WALL_BANNER, BROWN_WALL_BANNER, CYAN_WALL_BANNER, GRAY_WALL_BANNER, GREEN_WALL_BANNER,
                 LIGHT_BLUE_WALL_BANNER, LIGHT_GRAY_WALL_BANNER, LIME_WALL_BANNER, MAGENTA_WALL_BANNER, ORANGE_WALL_BANNER,
                 PINK_WALL_BANNER, PURPLE_WALL_BANNER, RED_WALL_BANNER, WHITE_WALL_BANNER, YELLOW_WALL_BANNER,
                 // 头颅类
                 PLAYER_HEAD, PLAYER_WALL_HEAD, SKELETON_SKULL, SKELETON_WALL_SKULL, WITHER_SKELETON_SKULL, WITHER_SKELETON_WALL_SKULL,
                 ZOMBIE_HEAD, ZOMBIE_WALL_HEAD, CREEPER_HEAD, CREEPER_WALL_HEAD, DRAGON_HEAD, DRAGON_WALL_HEAD, PIGLIN_HEAD, PIGLIN_WALL_HEAD,
                 // 其他功能方块
                 BEACON, FLOWER_POT, ENCHANTING_TABLE, ANVIL, CHIPPED_ANVIL, DAMAGED_ANVIL, LECTERN, LOOM, CARTOGRAPHY_TABLE,
                 FLETCHING_TABLE, SMITHING_TABLE, GRINDSTONE, STONECUTTER, BELL, CAMPFIRE, SOUL_CAMPFIRE, RESPAWN_ANCHOR,
                 LODESTONE, SCULK_SENSOR, SCULK_SHRIEKER, CALIBRATED_SCULK_SENSOR, CHISELED_BOOKSHELF, DECORATED_POT,
                 SUSPICIOUS_SAND, SUSPICIOUS_GRAVEL, TRIAL_SPAWNER, VAULT, CRAFTER -> true;
            default -> m.name().contains("SHULKER_BOX") || m.name().contains("BED");
        };
    }

    private boolean isSkull(Material material) {
        return material.equals(XMaterial.PLAYER_HEAD.get())
                || material.equals(XMaterial.PLAYER_WALL_HEAD.get());
    }
}
