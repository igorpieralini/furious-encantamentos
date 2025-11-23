# FURIOUS-ENCANTAMENTOS Plugin ✨

**FURIOUS-ENCANTAMENTOS** is a Minecraft Paper plugin (version 1.8.8) that enhances the enchanting experience. When a player clicks on an enchanting table, a **custom GUI** opens, allowing them to enchant items with better results while consuming more XP. Developed in **Java** using **Maven**, it improves gameplay and provides a more interactive enchantment system.

## Features ✨

* Opens a custom enchanting GUI when interacting with an enchanting table.
* Enhanced enchantment results compared to vanilla Minecraft.
* XP consumption scales according to enchantment power.
* Seamless integration with Paper 1.8.8 servers.

## Installation 🚀

1. Download the latest `FURIOUS-ENCANTAMENTOS.jar` from the repository or build it using Maven:

```bash
mvn clean package
```

2. Place the `.jar` file in your server’s `plugins` folder.
3. Restart or reload your server.

---

## Configuration ⚙️

Edit the `config.yml` file (generated on first run):

```yaml
enhanced-enchant: true
xp-multiplier: 2.0
gui-title: "§6Enhanced Enchantments"
```

* `enhanced-enchant`: Enable or disable the enhanced enchantment system.
* `xp-multiplier`: Factor by which XP cost increases for better enchantments.
* `gui-title`: Title displayed on the custom enchantment GUI.

---

## Permissions 🔑

* `furiousench.use` – Allows players to use the enhanced enchanting GUI.

> Make sure your player groups have the appropriate permission to access the enhanced enchantments.

---

## Usage 🎮

1. Approach an enchanting table in-game.
2. Click on the table to open the custom GUI.
3. Select the item and desired enchantment.
4. XP will be consumed according to the selected enchantment power.

---

## Development 🛠️

* Java 8 compatible.
* Built with Maven.
* Developer: **Igor Pieralini**

---
