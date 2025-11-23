package org.furious.furiousencantamentos.events;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.furious.furiousencantamentos.FuriousEncantamentos;
import org.furious.furiousencantamentos.model.Cuboid;
import org.furious.furiousencantamentos.model.CustomEnchantment;
import org.furious.furiousencantamentos.model.EnchantmentTableInventoryHolder;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class OpenEnchantmentTableEvent implements Listener {
    private final FuriousEncantamentos furiousEncantamentos;
    private final int[] glassPane = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 29, 31, 33, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
    private final int[] enchantmentBooks = new int[]{28, 30, 32, 34};

    public OpenEnchantmentTableEvent(FuriousEncantamentos furiousEncantamentos) {
        this.furiousEncantamentos = furiousEncantamentos;
    }

    @EventHandler
    public void openEnchantmentTableAndCreateCustomEnchantmentInventory(InventoryOpenEvent event) {

        Player player = (Player) event.getPlayer();
        if (event.getInventory().getType().equals(InventoryType.ENCHANTING)) {
            Inventory customEnchantingInventory = Bukkit.getServer().createInventory(new EnchantmentTableInventoryHolder(), 45, ChatColor.LIGHT_PURPLE + "Encantamento");

            addPurpleGlassPane(customEnchantingInventory);
            addBarriers(customEnchantingInventory);

            event.setCancelled(true);
            player.openInventory(customEnchantingInventory);
        }
    }

    @EventHandler
    public void onOpenEnchantmentTable(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getClickedBlock() != null) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (event.getClickedBlock().getType().equals(Material.ENCHANTMENT_TABLE)) {
                    Integer requiredBookshelfs = furiousEncantamentos.getEnchantmentConfig().getBookshelfs();
                    AtomicReference<Integer> placedBookshelfs = new AtomicReference<>(0);

                    Location border1 = new Location(player.getWorld(), event.getClickedBlock().getX() - 2, event.getClickedBlock().getY(), event.getClickedBlock().getZ() + 2);
                    Location border2 = new Location(player.getWorld(), event.getClickedBlock().getX() + 2, event.getClickedBlock().getY() + 5, event.getClickedBlock().getZ() - 2);
                    new Cuboid(border1, border2).getBlocks().forEach(block -> {
                        if (block.getType().equals(Material.BOOKSHELF)) {
                            placedBookshelfs.updateAndGet(v -> v + 1);
                        }
                    });


                    if (requiredBookshelfs > placedBookshelfs.get()) {
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', furiousEncantamentos.getEnchantmentConfig().getMensagens().get("estanteDeLivrosInsuficientes").replace("$numeroDeEstantes", requiredBookshelfs - placedBookshelfs.get() + "")));
                    }
                }
            }
        }
    }

    private void addPurpleGlassPane(Inventory inventory) {
        ItemStack purpleGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 10);
        ItemMeta purpleGlassPaneMeta = purpleGlassPane.getItemMeta();
        purpleGlassPaneMeta.setDisplayName(" ");
        purpleGlassPane.setItemMeta(purpleGlassPaneMeta);
        for (int i : glassPane) {
            inventory.setItem(i, purpleGlassPane);
        }
    }

    private void addBarriers(Inventory inventory) {
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setDisplayName(" ");
        barrier.setItemMeta(barrierMeta);
        for (int i : enchantmentBooks) {
            inventory.setItem(i, barrier);
        }
    }

    @EventHandler
    public void onClickGlassPanes(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof EnchantmentTableInventoryHolder) {
            if (Arrays.stream(glassPane).anyMatch(i -> i == event.getRawSlot()) || Arrays.stream(enchantmentBooks).anyMatch(i -> i == event.getRawSlot())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getInventory().getHolder() instanceof EnchantmentTableInventoryHolder) {
            ItemStack item = event.getInventory().getItem(13);
            if (item != null) {
                player.getInventory().addItem(item);
            }
        }
    }


    @EventHandler
    public void onAddItemToEnchant(InventoryClickEvent event) {
        Inventory enchantmentInventory = event.getInventory();
        if (enchantmentInventory.getHolder() instanceof EnchantmentTableInventoryHolder) {
            if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) || event.getAction().equals(InventoryAction.PLACE_ALL) || event.getClick().isShiftClick()) {
                Bukkit.getScheduler().runTaskLater(furiousEncantamentos, () -> {
                    if (enchantmentInventory.getItem(13) != null) {
                        ItemStack itemStack = enchantmentInventory.getItem(13);
                        List<Enchantment> applicableEnchantments = getApplicableEnchantments(itemStack);
                        List<CustomEnchantment> selectedEnchantments = chooseEnchantments(4, itemStack);

                        if (!applicableEnchantments.isEmpty() && !selectedEnchantments.isEmpty()) {
                            List<ItemStack> books = new ArrayList<>();
                            selectedEnchantments.forEach((enchantment) -> {
                                ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
                                ItemMeta bookMeta = book.getItemMeta();
                                bookMeta.addEnchant(enchantment.getEnchantment(), enchantment.getLevel(), true);
                                bookMeta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', furiousEncantamentos.getEnchantmentConfig().getMensagens().get("custoXp").replace("$xp", enchantment.getXpCost() + ""))));
                                book.setItemMeta(bookMeta);
                                books.add(book);
                            });
                            for (int i = 0; i < enchantmentBooks.length; i++) {
                                enchantmentInventory.setItem(enchantmentBooks[i], books.get(i));
                            }
                        } else {
                            addBarriers(enchantmentInventory);
                        }
                    }
                }, 1);

            }
        }
    }

    @EventHandler
    public void onRemoveItemFromEnchantmentTable(InventoryClickEvent event) {
        Inventory enchantmentInventory = event.getInventory();
        if (enchantmentInventory.getHolder() instanceof EnchantmentTableInventoryHolder) {
            if ((event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) || event.getAction().equals(InventoryAction.PICKUP_ALL)) && event.getRawSlot() == 13) {
                addBarriers(enchantmentInventory);
            }
        }
    }

    private List<CustomEnchantment> chooseEnchantments(int numberOfEnchantments, ItemStack itemStack) {
        List<Map<String, List<Integer>>> enchantments = furiousEncantamentos.getEnchantmentConfig().getEnchantments();
        List<Map<Integer, Map<String, Integer>>> levels = furiousEncantamentos.getEnchantmentConfig().getLevels();
        List<Enchantment> applicableEnchantments = getApplicableEnchantments(itemStack);

        List<CustomEnchantment> selectedEnchantments = new ArrayList<>();
        List<String> enchantmentNames = new ArrayList<>();
        Map<String, List<Integer>> enchantmentMap = new HashMap<>();
        for (Map<String, List<Integer>> enchantment : enchantments) {
            for (Map.Entry<String, List<Integer>> entry : enchantment.entrySet()) {
                enchantmentNames.add(entry.getKey());
                enchantmentMap.put(entry.getKey(), entry.getValue());
            }
        }

        Collections.shuffle(enchantmentNames);
        Random random = new Random();
        for (int i = 0; i < enchantmentNames.toArray().length; i++) {
            String enchantmentName = enchantmentNames.get(i);
            if (applicableEnchantments.stream().anyMatch(enchantment -> enchantment.equals(Enchantment.getByName(enchantmentName)))) {
                List<Integer> levelsList = enchantmentMap.get(enchantmentName);
                int levelIndex = getRandomLevel(levelsList, levels, random);
                int level = levelsList.get(levelIndex);
                Map<String, Integer> selectedLevel = levels.get(level - 1).get(level);
                int xpCost = selectedLevel.get("xpCost");
                CustomEnchantment enchantment = new CustomEnchantment(Enchantment.getByName(enchantmentName), level, xpCost);
                selectedEnchantments.add(enchantment);
            }
        }

        selectedEnchantments = selectedEnchantments.subList(0, 4);
        return selectedEnchantments.size() < numberOfEnchantments ? Collections.emptyList() : selectedEnchantments;
    }

    private static int getRandomLevel(List<Integer> levelsList, List<Map<Integer, Map<String, Integer>>> levels, Random random) {
        int totalChance = 0;
        for (Integer level : levelsList) {
            totalChance += levels.get(level - 1).get(level).get("showChance");
        }

        int randomChance = random.nextInt(totalChance);
        int currentChance = 0;

        for (int i = 0; i < levelsList.size(); i++) {
            int level = levelsList.get(i);
            currentChance += levels.get(level - 1).get(level).get("showChance");
            if (randomChance < currentChance) {
                return i;
            }
        }
        return levelsList.size() - 1;
    }

    @EventHandler
    public void onClickEnchantmentBooks(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;
        if (clickedInventory.getHolder() instanceof EnchantmentTableInventoryHolder) {
            int slot = event.getRawSlot();
            ItemStack item = clickedInventory.getItem(13);
            if (item != null) {
                AtomicReference<ItemMeta> itemMeta = new AtomicReference<>(item.getItemMeta());
                Map<Enchantment, Integer> currentEnchantments = item.getEnchantments();
                if (Arrays.stream(enchantmentBooks).anyMatch(i -> i == slot)) {
                    Player player = (Player) event.getWhoClicked();
                    ItemStack enchantmentBook = clickedInventory.getItem(slot);
                    Map<Enchantment, Integer> enchantments = enchantmentBook.getEnchantments();

                    ItemStack barrier = new ItemStack(Material.BARRIER);
                    ItemMeta barrierMeta = barrier.getItemMeta();

                    enchantments.forEach((enchantment, level) -> {
                        if (player.getLevel() < level) {
                            barrierMeta.setDisplayName(ChatColor.RED + "Você não possui xp suficiente!");
                            barrier.setItemMeta(barrierMeta);
                            clickedInventory.setItem(slot, barrier);
                            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 6.0F, 6.0F);
                        } else {
                            if (enchantment.getItemTarget() == null || enchantment.getItemTarget().includes(item) || (enchantment.getItemTarget().equals(EnchantmentTarget.WEAPON) && item.getType().equals(Material.DIAMOND_AXE))) {
                                if ((currentEnchantments.get(enchantment) == null)) {
                                    itemMeta.set(encantaItem(player, item, enchantment, level));
                                } else if (currentEnchantments.get(enchantment) > level) {
                                    barrierMeta.setDisplayName(ChatColor.RED + "Você não pode encantar o item com um nível menor!");
                                    barrier.setItemMeta(barrierMeta);
                                    clickedInventory.setItem(slot, barrier);
                                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 6.0F, 6.0F);

                                } else if (currentEnchantments.get(enchantment).equals(level)) {
                                    barrierMeta.setDisplayName(ChatColor.RED + "Você não pode encantar esse item!");
                                    barrier.setItemMeta(barrierMeta);
                                    clickedInventory.setItem(slot, barrier);
                                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 6.0F, 6.0F);

                                } else {
                                    itemMeta.set(encantaItem(player, item, enchantment, level));
                                }
                            } else {
                                barrierMeta.setDisplayName(ChatColor.RED + "Você não pode encantar esse item!");
                                barrier.setItemMeta(barrierMeta);
                                clickedInventory.setItem(slot, barrier);
                            }
                        }

                        Bukkit.getScheduler().runTaskLater(furiousEncantamentos, () -> {
                            clickedInventory.setItem(slot, enchantmentBook);
                        }, 20);
                    });
                    item.setItemMeta(itemMeta.get());
                }
            }
        }
    }

    private ItemMeta encantaItem(Player player, ItemStack item, Enchantment enchantment, int level) {
        ItemMeta itemMeta = item.getItemMeta();
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 6.0F, 6.0F);
        itemMeta.addEnchant(enchantment, level, true);
        player.setLevel(player.getLevel() - level);
        return itemMeta;
    }

    private List<Enchantment> getApplicableEnchantments(ItemStack item) {
        return Arrays.stream(Enchantment.values()).filter(e -> {
            if (e.getItemTarget() != null) {
                if (e.getItemTarget().equals(EnchantmentTarget.WEAPON) && item.getType().equals(Material.DIAMOND_AXE)) {
                    return true;
                }
                return e.getItemTarget().includes(item);
            } else {
                return true;
            }
        }).collect(Collectors.toList());
    }
}
