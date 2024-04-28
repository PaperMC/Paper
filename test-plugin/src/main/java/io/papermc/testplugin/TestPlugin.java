package io.papermc.testplugin;

import io.papermc.paper.event.player.ChatEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEvent(ChatEvent event) throws InvalidConfigurationException {
        final ItemStack inHand = event.getPlayer().getInventory().getItemInMainHand();
        if (true) this.testEquals(inHand);
        if (true) return;
        final YamlConfiguration config = new YamlConfiguration();
        config.set("item", inHand);
        System.out.println(config.saveToString());
        // config.loadFromString(OLD);
        // final ItemStack fromConfig = config.getSerializable("item", ItemStack.class);
        // final YamlConfiguration modern = new YamlConfiguration();
        // modern.loadFromString(MODERN);
        // final ItemStack fromModern = modern.getSerializable("item", ItemStack.class);
        // System.out.println(fromConfig);
        // System.out.println(inHand.equals(fromConfig));
        // System.out.println(fromConfig.equals(fromModern));
        // config.set("item", inHand);
        // System.out.println(config.saveToString());
    }

    void testEquals(ItemStack inHand) throws InvalidConfigurationException {
        final YamlConfiguration old = new YamlConfiguration();
        old.loadFromString(OLD);
        final YamlConfiguration neww = new YamlConfiguration();
        neww.loadFromString(MODERN);
        final ItemStack fromOld = old.getSerializable("item", ItemStack.class);
        final ItemStack fromNew = neww.getSerializable("item", ItemStack.class);
        System.out.println("fromOld = inHand: " + fromOld.equals(inHand));
        System.out.println("fromNew = inHand: " + fromNew.equals(inHand));
        System.out.println("fromOld = fromNew: " + fromOld.equals(fromNew));
        System.out.println("inHand = fromOld: " + inHand.equals(fromOld));
        System.out.println("inHand = fromNew: " + inHand.equals(fromNew));
    }

    static final String MODERN = """
        item:
          ==: org.bukkit.inventory.ItemStack
          v: 3837
          type: SHULKER_BOX
          meta:
            ==: ItemMeta
            snbt: |-
              {
                  "minecraft:block_entity_data": {
                      id: "minecraft:shulker_box",
                      x: 0,
                      y: 0,
                      z: 0
                  },
                  "minecraft:container": [
                      {
                          item: {
                              components: {
                                  "minecraft:stored_enchantments": {
                                      levels: {
                                          "minecraft:projectile_protection": 3
                                      }
                                  }
                              },
                              count: 1,
                              id: "minecraft:enchanted_book"
                          },
                          slot: 0
                      },
                      {
                          item: {
                              components: {
                                  "!minecraft:tool": {}
                              },
                              count: 1,
                              id: "minecraft:diamond_pickaxe"
                          },
                          slot: 1
                      }
                  ]
              }
            _version: 3837
            meta-type: PAPER_SNBT
            meta-subtype: TILE_ENTITY
            blockMaterial: SHULKER_BOX""";
    static final String OLD = """
        item:
          ==: org.bukkit.inventory.ItemStack
          v: 3837
          type: SHULKER_BOX
          meta:
            ==: ItemMeta
            meta-type: TILE_ENTITY
            internal: H4sIAAAAAAAA/22OwW7CMAyG3XZFWw/jhDQOCO01dkTiwHncqzQ1NDSxq9agsqfHZauKJnLxr+TL5z8DyOB949nWWxIn1705JhD9gJ5XiF0Ji+AIbWsO8tVVZ19jmxfcK3MdGJ39MN8g3QmGLtMcJ5BaPpNojiJ4+fYsuiWzHBomJFFq9WAVbrHMkWxlSMLv+8zjBX2XwHoCm5ZPaMV5zDXKEJnuHcauHxP8p1NxwVzDk0rRv0rzz+m3MPtRupyuS2cCU5k3ztamR5XdABHDfI5AAQAA
            blockMaterial: SHULKER_BOX""";
}
