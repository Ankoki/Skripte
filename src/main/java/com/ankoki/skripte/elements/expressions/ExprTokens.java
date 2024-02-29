package com.ankoki.skripte.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.RequiredPlugins;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.ankoki.skripte.Skripte;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

@Name("Tokens")
@Description({"Controls the amount of tokens a player has."})
@Examples({"set player's tokens to 50"})
@Since("1.0")
@RequiredPlugins({"TokenEnchant"})
public class ExprTokens extends SimpleExpression<Number> {

    private Expression<OfflinePlayer> playerExpr;

    static {
        Skript.registerExpression(ExprTokens.class, Number.class, ExpressionType.SIMPLE, "%offlineplayer%'s tokens", "tokens of %offlineplayer%");
    }

    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.playerExpr = (Expression<OfflinePlayer>) exprs[0];
        return true;
    }

    protected Number[] get(Event event) {
        OfflinePlayer player = this.playerExpr.getSingle(event);
        if (player == null)
            return new Number[0];
        return new Number[] {Skripte.getInstance().getTokenEnchant().getTokens(player)};
    }

    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.DELETE)
            return new Class[0];
        return (Class<?>[]) CollectionUtils.array(new Class[]{Number.class});
    }

    public void change(Event event, Object[] delta, Changer.ChangeMode mode) {
        Number number;
        OfflinePlayer player = this.playerExpr.getSingle(event);
        if (player == null)
            return;
        if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.REMOVE_ALL || mode == Changer.ChangeMode.DELETE)
            Skripte.getInstance().getTokenEnchant().setTokens(player, 0.0D);
        if (delta.length != 1)
            return;
        Object object = delta[0];
        if (object instanceof Number)
            number = (Number)object;
        else
            return;
        double difference = number.doubleValue();
        switch (mode) {
            case SET:
                Skripte.getInstance().getTokenEnchant().setTokens(player, difference);
                break;
            case ADD:
                Skripte.getInstance().getTokenEnchant().addTokens(player, difference);
                break;
            case REMOVE:
                Skripte.getInstance().getTokenEnchant().removeTokens(player, difference);
                break;
        }
    }

    public boolean isSingle() {
        return true;
    }

    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    public String toString(Event event, boolean debug) {
        return "tokens of " + this.playerExpr.toString(event, debug);
    }

}
