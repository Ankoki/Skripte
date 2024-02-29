package com.ankoki.skripte;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.ankoki.skripte.misc.Console;
import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Skripte extends JavaPlugin {

    private static Skripte instance;

    private TokenEnchantAPI tokenEnchant;

    private SkriptAddon addon;

    public void onEnable() {
        instance = this;
        this.tokenEnchant = (TokenEnchantAPI) Bukkit.getPluginManager().getPlugin("TokenEnchant");
        this.addon = Skript.registerAddon(this);
        loadElements();
    }

    public static Skripte getInstance() {
        return instance;
    }

    public TokenEnchantAPI getTokenEnchant() {
        return this.tokenEnchant;
    }

    private void loadElements() {
        try {
            this.addon.loadClasses("com.ankoki.skripte.elements", "expressions", "effects", "events", "conditions");
        } catch (IOException ex) {
            Console.error("Something went horribly wrong, please report this to 'ankokii' on discord.");
            ex.printStackTrace();
        }
    }

}
