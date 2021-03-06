package io.github.titmar.caravans.client.screens;

import java.util.HashSet;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import io.github.titmar.caravans.Caravans;
import io.github.titmar.caravans.common.container.MarketContainer;
import io.github.titmar.caravans.core.network.CaravansNetwork;
import io.github.titmar.caravans.core.network.message.ChangeActiveInventoryMessage;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MarketScreen extends ContainerScreen<MarketContainer> {

	private static final ResourceLocation MARKET_GUI = new ResourceLocation(Caravans.MOD_ID, "textures/gui/market.png");

	public MarketScreen(MarketContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);

		this.guiLeft = 0;
		this.guiTop = 0;
		this.xSize = 276;
		this.ySize = 226;
		this.titleX = 7;
		this.titleY = 6;
	}

	@Override
	protected void init() {
		super.init();
		this.addButtons();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
		this.font.func_243248_b(matrixStack, this.container.te.getDisplayName(), (float) this.titleX,
				(float) this.titleY, 4210752); // title
		renderButtonInfo(matrixStack);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX,
			int mouseY) {
		RenderSystem.color4f(1f, 1f, 1f, 1f);
		this.minecraft.textureManager.bindTexture(MARKET_GUI);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		blit(matrixStack, x, y, this.getBlitOffset(), 0.0F, 0.0F, this.xSize, this.ySize, 256, 512);
	}

	private void addButtons() {
		HashSet<BlockPos> list = container.te.getContainers();
		int buttonX = ((this.width - this.xSize) / 2) + 5;
		int buttonY = ((this.height - this.ySize) / 2) + 18;
		for (BlockPos pos : list) {
			this.addButton(new Button(buttonX, buttonY, 89, 20, StringTextComponent.EMPTY, (b) -> {
				this.changeInventory(pos);
			}));
			buttonY += 20;
		}
	}

	private void changeInventory(BlockPos pos) {
		this.container.setActiveInventory(pos);
		CaravansNetwork.CHANNEL.sendToServer(new ChangeActiveInventoryMessage(pos));
	}

	private void renderButtonInfo(MatrixStack matrixStack) {
		int iconX = 7;
		int iconY = 20;
		for (BlockPos pos : container.te.getContainers()) {
			LockableLootTileEntity te = (LockableLootTileEntity) container.te.getWorld().getTileEntity(pos);
			ItemStack item = new ItemStack(te.getBlockState().getBlock().asItem());
			this.itemRenderer.renderItemIntoGUI(item, iconX, iconY);
			this.itemRenderer.renderItemOverlays(font, item, iconX, iconY);
			this.font.func_243248_b(matrixStack, te.getDisplayName(), iconX + 16 + 3, iconY + 4, 4210752);
			iconY += 20;
		}
	}



}
