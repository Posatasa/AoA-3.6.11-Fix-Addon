# AoA3 1.16.5 Fixes

A small client-side patch add-on for [Advent of Ascension 3](https://github.com/Tslat/Advent-Of-Ascension)
**1.16.5-3.6.11** (Minecraft 1.16.5, Forge 36.x).

**Download:** [`dist/aoa3fixes-1.0.0.jar`](dist/aoa3fixes-1.0.0.jar) — drop it into `mods/`.
Checksum: [`dist/SHA256SUMS.txt`](dist/SHA256SUMS.txt).

## 1. What it fixes

* **Skills wiped on the host in LAN worlds.** When another player disconnects from a
  singleplayer/LAN world, the host's skills, resources and ability keybinds are cleared, and the
  Advent GUI wrongly reports *"Skills have been disabled by the server owner"*. Joining players
  are unaffected.
* **Entity attack range too long.** In Survival the crosshair targets and hits mobs from ~4.5
  blocks instead of vanilla's 3.

## 2. Side: BOTH

The mod is declared `side="BOTH"`. The jar is safe in `mods/` on the client, on a dedicated
server, or both at once. It adds no packet and no registry entry, so clients that do not have it
can still join normally.

## 3. AoA licensing

It contains **no AoA code or assets**. Only a few instructions are rewritten at class-load time,
and only class and method *names* are referenced — which is what any compatibility patch must do.

AoA's licence explicitly permits this:

> **Extensions/Add-ons**
> You may create, use, and distribute any extensions or addons to Advent of Ascension as wanted,
> provided that the extension or addon complies with the rest of the terms of this license.

This add-on's own code is MIT (see [`LICENSE`](LICENSE)). Advent of Ascension is made by Tslat and
is not affiliated with this add-on.

---

## 中文

针对 **Minecraft 1.16.5 + 虚无世界3（AoA3）1.16.5-3.6.11** 的**客户端侧修复附加模组**。

**下载：** [`dist/aoa3fixes-1.0.0.jar`](dist/aoa3fixes-1.0.0.jar)，丢进 `mods/` 即可。

**1. 修了什么**

* **局域网房主技能被清空**：有玩家退出单机/局域网世界时，房主自己的技能、资源与技能快捷键会被
  清空，界面还会错误提示「技能已被服务器管理员禁用」。加入的人不受影响。
* **攻击距离变长**：生存模式下准星能在约 4.5 格就选中并打到怪物，而不是原版的 3 格。

**2. side=BOTH**

模组声明为 `side="BOTH"`，放在客户端、专用服务端或两边都放的 `mods/` 里都没有问题。
它不增加任何数据包与注册项，没装的客户端照样能正常进服。

**3. 授权合规**

**不含任何 AoA 代码与素材**，只在类加载时改写两条指令，且仅引用类名与方法名——
这是任何兼容补丁都必须做的事。AoA 许可证明确允许制作与分发扩展/附加模组：

> **Extensions/Add-ons**
> You may create, use, and distribute any extensions or addons to Advent of Ascension as wanted,
> provided that the extension or addon complies with the rest of the terms of this license.

本附加模组自身代码为 MIT（见 [`LICENSE`](LICENSE)）。虚无世界3 由 Tslat 开发，与本附加模组无隶属关系。
