package fuzs.armorstatues.client.gui.screens.armorstand;

import fuzs.armorstatues.client.gui.screens.armorstand.data.ArmorStandScreenType;
import fuzs.armorstatues.world.inventory.ArmorStandMenu;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;

public interface ArmorStandScreen {

    ArmorStandScreenType getScreenType();

    <T extends Screen & MenuAccess<ArmorStandMenu> & ArmorStandScreen> T createScreenType(ArmorStandScreenType screenType);
}