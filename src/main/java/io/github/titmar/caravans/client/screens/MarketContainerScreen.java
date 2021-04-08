package io.github.titmar.caravans.client.screens;

import io.github.titmar.caravans.Caravans;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MarketContainerScreen extends Screen {

	protected MarketContainerScreen(ITextComponent titleIn) {
		super(titleIn);		
	}
	
	public MarketContainerScreen() {
		this(new TranslationTextComponent(Caravans.MOD_ID + ".market_container_screen"));
	}

	
	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}
}
