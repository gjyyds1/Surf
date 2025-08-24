# Surf - 中文说明

本仓库为原Surf的fork，清理了冗余无用代码，并将大部分已弃用的API更换为现代API，并移除低版本支持，现需要Java17及以上版本的Java才可运行。

Surf是[LeeesExploitFixer](https://github.com/XeraPlugins/LeeesExploitFixer-3.0)的分支，而LeeesExploitFixer又是254nm的[L2X9Core](https://github.com/254nm/L2X9Core)的分支。

这是一个专为无政府服务器设计的插件，旨在修复漏洞、检测和移除违法/NBT物品。

建议与AnarchyExploitFixes和Panilla一起使用以实现100%功能性。

## 兼容性

- 支持Java 17及更高版本
- 最新Minecraft版本 (1.21.8)
- 兼容Paper / Paper分支
- 支持Folia

___

## 功能特性

* 防止所有已知的崩溃漏洞
* 防止区块封禁(ChunkBan)
* 修复书本封禁而不禁用潜影盒预览
* 防止末地传送门破坏
* 修复玩家使用某些作弊客户端的副手崩溃模块通过书本崩溃服务器
* 防止玩家使用违法物品
* 移除玩家身上的违法药水效果
* 移除下落方块服务器崩溃器
* 积极开发维护

## 配置说明

### OP跳过检测功能

插件现在支持OP玩家跳过大部分检测，可通过配置文件控制：

```yaml
# 是否允许OP跳过检测 (默认: true)
opBypassEnabled: true
```

当启用此功能时，拥有OP权限的玩家可以：
- 跳过违法物品检测
- 跳过区块封禁检测
- 跳过NBT封禁检测
- 正常使用可能被标记为违法的物品

## 联系方式

- 📫 Discord: `dreeam___` | QQ: `2682173972`

---

# Surf - English Documentation

# Surf

Surf is a fork of [LeeesExploitFixer](https://github.com/XeraPlugins/LeeesExploitFixer-3.0) of a fork of
254nm's [L2X9Core](https://github.com/254nm/L2X9Core)

A plugin for anarchy servers that aim to fix exploits, detect and remove illegal/NBT items.

Recommended use as a addition with AnarchyExploitFixes and Panilla to become 100% functional.

## Compatibility

- Support Java 17 and higher
- Latest Minecraft version (1.21.8)
- Compatible with Paper / Paper Forks
- Folia Support

___

## Features

* Prevent all crash exploits that I know of
* Prevent ChunkBan
* Patch BookBan without disabling shulker peek
* Prevent EndPortal greifing
* Patch players using the OffHand crash module in certan hacked clients to crash the server with books
* Prevent players from using illegal items
* Remove illegal potion effects from players
* Remove falling block server crashers
* Active development

## Contact

- 📫 Discord: `dreeam___` | QQ: `2682173972`

## 已完成功能

✅ **Prefix配置** - 已实现配置文件中的prefix设置  
✅ **模块重构** - 将检查功能从ItemUtil移动到checks包  
✅ **ChunkBan模块** - 完整实现区块封禁防护功能  
✅ **NBTBAN模块** - 完整实现NBT封禁防护功能  
✅ **OP跳过检测** - 实现OP玩家可跳过检测的功能  
✅ **中文翻译** - 为README添加完整的中文说明文档  

## 开发说明

本插件采用模块化设计：
- `checks` 包：包含所有检查逻辑，分为通用检查和特定物品检查
- `CheckManager`：统一管理所有检查功能，支持OP跳过逻辑
- 各个监听器通过CheckManager进行物品检查和清理

感谢Panilla项目提供的部分检查逻辑参考。
