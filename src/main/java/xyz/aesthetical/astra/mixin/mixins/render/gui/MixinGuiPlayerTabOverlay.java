package xyz.aesthetical.astra.mixin.mixins.render.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.aesthetical.astra.features.modules.miscellaneous.ExtraTab;
import xyz.aesthetical.astra.managers.commands.text.ChatColor;

import java.util.List;

@Mixin(GuiPlayerTabOverlay.class)
public class MixinGuiPlayerTabOverlay extends Gui {
    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Ljava/util/List;subList(II)Ljava/utl/List;", remap = false))
    public List<NetworkPlayerInfo> hookRenderPlayerlist(List<NetworkPlayerInfo> players, int from, int to) {
        return players.subList(from, ExtraTab.instance.isToggled() ? Math.min(ExtraTab.instance.players.getValue().intValue(), players.size()) : to);
    }

    @Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
    public void getPlayerName(NetworkPlayerInfo playerInfo, CallbackInfoReturnable<String> info) {
        if (ExtraTab.instance.isToggled() && playerInfo.getDisplayName() != null) {
            if (ExtraTab.instance.highlightFriends.getValue()) {
                info.setReturnValue(ChatColor.Blue.text(playerInfo.getDisplayName().getUnformattedText()));
            }

            return;
        }

        info.setReturnValue(playerInfo.getDisplayName() != null ? playerInfo.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(playerInfo.getPlayerTeam(), playerInfo.getGameProfile().getName()));
    }
}
