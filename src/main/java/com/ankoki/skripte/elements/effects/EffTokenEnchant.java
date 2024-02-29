package com.ankoki.skripte.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.ankoki.skripte.Skripte;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

@Name("Token Enchant")
@Description({"Enchants an item with the given token enchant."})
@Examples({"tenchant player's tool with \"JackHammer\" 10 using player"})
@Since("1.0")
@RequiredPlugins({"TokenEnchant"})
public class EffTokenEnchant extends Effect {

    private Expression<ItemStack> itemExpr;

    private Expression<String> enchantExpr;

    private Expression<Number> levelExpr;

    private Expression<Player> playerExpr;

    static {
        Skript.registerEffect(EffTokenEnchant.class, "t[oken][ ]enchant %itemstack% with %string% [level] %number% (from|using) %player%");
    }

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.itemExpr = (Expression<ItemStack>) exprs[0];
        this.enchantExpr = (Expression<String>) exprs[1];
        this.levelExpr = (Expression<Number>) exprs[2];
        this.playerExpr = (Expression<Player>) exprs[3];
        return true;
    }

    protected void execute(Event event) {
        String enchant = this.enchantExpr.getSingle(event);
        Number number = this.levelExpr.getSingle(event);
        ItemStack item = this.itemExpr.getSingle(event);
        Player player = this.playerExpr.getSingle(event);
        if (enchant == null || item == null || player == null || number == null)
            return;
        int level = number.intValue();
        this.itemExpr.change(event, new ItemStack[]{Skripte.getInstance().getTokenEnchant().enchant(player, item, enchant, level, true, 0.0D, true)}, Changer.ChangeMode.SET);
    }

    public String toString(Event event, boolean debug) {
        return "token enchant " + this.itemExpr.toString(event, debug) + " with " + this.enchantExpr.toString(event, debug) + " level " + this.levelExpr.toString(event, debug) + " using " + this.playerExpr
                .toString(event, debug);
    }

}
