package cn.dreeam.surf.checks;

import cn.dreeam.surf.config.Config;
import cn.dreeam.surf.util.ItemUtil;
import org.bukkit.inventory.ItemStack;

/**
 * 通用物品检查类
 */
public class GeneralCheck extends BaseCheck {
    
    @Override
    public boolean isIllegal(ItemStack item) {
        if (item == null || ItemUtil.isAir(item)) {
            return false;
        }
        
        return ItemUtil.isIllegalItem(item) || 
               ItemUtil.isEnchantedBlock(item) || 
               ItemUtil.isIllegalPotion(item) || 
               ItemUtil.hasIllegalDurability(item) ||
               ItemUtil.isUnbreakable(item) || 
               ItemUtil.hasIllegalEnchants(item) || 
               ItemUtil.hasIllegalItemFlag(item) || 
               ItemUtil.hasIllegalAttributes(item);
    }
    
    @Override
    public ItemStack clean(ItemStack item) {
        if (item == null || ItemUtil.isAir(item)) {
            return ItemStack.empty();
        }
        
        if (ItemUtil.isIllegalItem(item) || ItemUtil.isIllegalPotion(item)) {
            return ItemStack.empty();
        }
        
        if (Config.antiIllegalDeleteIllegalsWhenFoundEnabled && isIllegal(item)) {
            return ItemStack.empty();
        }
        
        // 这里可以调用ItemUtil中的清理方法
        // 为了保持兼容性，暂时返回原物品
        return item;
    }

}