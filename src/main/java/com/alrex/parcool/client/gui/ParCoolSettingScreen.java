package com.alrex.parcool.client.gui;

import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.ActionList;
import com.alrex.parcool.common.info.ActionInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static com.alrex.parcool.ParCoolConfig.CONFIG_CLIENT;

public class ParCoolSettingScreen extends Screen {
	private final ConfigSet[] configItemList = new ConfigSet[]{
			new ConfigSet("Activate ParCool", CONFIG_CLIENT.parCoolActivation::set, CONFIG_CLIENT.parCoolActivation::get),
			new ConfigSet("Infinite stamina", CONFIG_CLIENT.infiniteStamina::set, CONFIG_CLIENT.infiniteStamina::get),
			new ConfigSet("Enable actions needing Fast-Run with normal sprint", CONFIG_CLIENT.substituteSprintForFastRun::set, CONFIG_CLIENT.substituteSprintForFastRun::get),
			new ConfigSet("Always do Fast-Run when doing sprint", CONFIG_CLIENT.replaceSprintWithFastRun::set, CONFIG_CLIENT.replaceSprintWithFastRun::get),
			new ConfigSet("Hide stamina HUD", CONFIG_CLIENT.hideStaminaHUD::set, CONFIG_CLIENT.hideStaminaHUD::get),
			new ConfigSet("Use light Stamina HUD", CONFIG_CLIENT.useLightHUD::set, CONFIG_CLIENT.useLightHUD::get),
			new ConfigSet("Disable a camera rotation of Rolling", CONFIG_CLIENT.disableCameraRolling::set, CONFIG_CLIENT.disableCameraRolling::get),
			new ConfigSet("Disable a camera rotation of Flipping", CONFIG_CLIENT.disableCameraFlipping::set, CONFIG_CLIENT.disableCameraFlipping::get),
			new ConfigSet("Disable a camera animation of Horizontal Wall-Run", CONFIG_CLIENT.disableCameraHorizontalWallRun::set, CONFIG_CLIENT.disableCameraHorizontalWallRun::get),
			new ConfigSet("Disable a camera animation of Vault", CONFIG_CLIENT.disableCameraVault::set, CONFIG_CLIENT.disableCameraVault::get),
			new ConfigSet("Disable double-tapping for dodge", CONFIG_CLIENT.disableDoubleTappingForDodge::set, CONFIG_CLIENT.disableDoubleTappingForDodge::get),
			new ConfigSet("Disable crawl in air", CONFIG_CLIENT.disableCrawlInAir::set, CONFIG_CLIENT.disableCrawlInAir::get),
			new ConfigSet("Disable vault in air", CONFIG_CLIENT.disableVaultInAir::set, CONFIG_CLIENT.disableVaultInAir::get),
			new ConfigSet("Disable falling animation", CONFIG_CLIENT.disableFallingAnimation::set, CONFIG_CLIENT.disableFallingAnimation::get),
			new ConfigSet("Disable all animations", CONFIG_CLIENT.disableAnimation::set, CONFIG_CLIENT.disableAnimation::get),
			new ConfigSet("Disable first person view animations", CONFIG_CLIENT.disableFPVAnimation::set, CONFIG_CLIENT.disableFPVAnimation::get),
			new ConfigSet("Enable roll when player is creative", CONFIG_CLIENT.enableRollWhenCreative::set, CONFIG_CLIENT.enableRollWhenCreative::get)
	};

	private enum SettingMode {Actions, Configs, Limitations}

	private SettingMode mode = SettingMode.Actions;
	private final CheckboxWidget[] configButtons = new CheckboxWidget[configItemList.length];
	private final ActionConfigSet[] actionList = new ActionConfigSet[ActionList.ACTIONS.size()];
	private final InfoSet[] infoList;
	private final CheckboxWidget[] actionButtons = new CheckboxWidget[actionList.length];
	private final ModeSet[] modeMenuList = new ModeSet[]{
			new ModeSet(new TranslatableText("parcool.gui.text.action"), SettingMode.Actions, 0, this),
			new ModeSet(new TranslatableText("parcool.gui.text.config"), SettingMode.Configs, 1, this),
			new ModeSet(new TranslatableText("parcool.gui.text.limitation"), SettingMode.Limitations, 2, this)
	};
	private int modeIndex;

	public ParCoolSettingScreen(BaseText titleIn, ActionInfo info, ColorTheme theme) {
		super(titleIn);
		for (int i = 0; i < actionList.length; i++) {
			actionList[i] = new ActionConfigSet(ActionList.getByIndex(i), info);
			actionButtons[i] = new CheckboxWidget(0, 0, 0, Checkbox_Item_Height, new LiteralText(actionList[i].name), actionList[i].getter.getAsBoolean());
		}
		for (int i = 0; i < configItemList.length; i++) {
			configButtons[i] = new CheckboxWidget(0, 0, 0, Checkbox_Item_Height, new TranslatableText(configItemList[i].name), configItemList[i].getter.getAsBoolean());
		}
		infoList = new InfoSet[]{
				new InfoSet(
						"Max Stamina Limitation",
						Integer.toString(info.getMaxStaminaLimitation())
				),
				new InfoSet(
						"Infinite Stamina Permission",
						Boolean.toString(info.isInfiniteStaminaPermitted())
				)
		};
		serverPermissionReceived = info.getServerLimitation()::isReceived;
		individualPermissionReceived = info.getIndividualLimitation()::isReceived;
		color = theme;
	}

	private int topIndex = 0;
	private int viewableItemCount = 0;
	private static final int Checkbox_Item_Height = 21;
	private final ColorTheme color;
	private final BooleanSupplier serverPermissionReceived;
	private final BooleanSupplier individualPermissionReceived;

	@Override
	public void close() {
		for (int i = 0; i < configItemList.length; i++) {
			configItemList[i].setter.accept(configButtons[i].active);
		}
		for (int i = 0; i < actionList.length; i++) {
			actionList[i].setter.accept(actionButtons[i].active);
		}
		super.close();
	}

	@Override
	public void resize(MinecraftClient p_231152_1_, int p_231152_2_, int p_231152_3_) {
		super.resize(p_231152_1_, p_231152_2_, p_231152_3_);
		mouseScrolled(0, 0, 0);
	}

	private static final BaseText MenuTitle = new TranslatableText("parcool.gui.title.setting");

	@Override
	public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float p_230430_4_) {
		super.render(matrixStack, mouseX, mouseY, p_230430_4_);
		renderBackground(matrixStack, 0);
		int topBarHeight = textRenderer.fontHeight * 2;
		int topBarItemWidth = (int) (1.6 * Arrays.stream(modeMenuList).map(it -> textRenderer.width(it.title)).max(Integer::compareTo).orElse(0));
		int topBarOffsetX = width - topBarItemWidth * modeMenuList.length;
		fillGradient(matrixStack, 0, 0, this.width, topBarHeight, color.getTopBar1(), color.getTopBar2());
		drawWithShadow(
				matrixStack, textRenderer, (OrderedText) MenuTitle,
				10,
				topBarHeight / 4 + 1,
				color.getText()
		);
		for (int i = 0; i < modeMenuList.length; i++) {
			ModeSet item = modeMenuList[i];
			item.y = 0;
			item.x = topBarOffsetX + i * topBarItemWidth;
			item.width = topBarItemWidth;
			item.height = topBarHeight;
			boolean selected = (item.mode == mode) || item.isMouseIn(mouseX, mouseY);
			drawCenteredText(
					matrixStack, textRenderer, item.title,
					topBarOffsetX + i * topBarItemWidth + topBarItemWidth / 2,
					topBarHeight / 4 + 1,
					selected ? color.getText() : color.getSubText()
			);
			fill(matrixStack, item.x, 2, item.x + 1, topBarHeight - 3, color.getSeparator());
		}
		fill(matrixStack, 0, topBarHeight - 1, width, topBarHeight, color.getSeparator());
		switch (mode) {
			case Actions:
				renderActions(matrixStack, mouseX, mouseY, p_230430_4_, topBarHeight);
				break;
			case Configs:
				renderConfigs(matrixStack, mouseX, mouseY, p_230430_4_, topBarHeight);
				break;
			case Limitations:
				renderLimitations(matrixStack, mouseX, mouseY, p_230430_4_, topBarHeight);
		}
	}

	private static final BaseText Header_ActionName = new TranslatableText("parcool.gui.text.actionName");
	private static final BaseText Header_ServerPermission = new LiteralText("G");
	private static final BaseText Header_ServerPermissionText = new TranslatableText("parcool.gui.text.globalPermission");
	private static final BaseText Header_IndividualPermission = new LiteralText("I");
	private static final BaseText Header_IndividualPermissionText = new TranslatableText("parcool.gui.text.individualPermission");
	private static final BaseText Permission_Permitted = new LiteralText("✓");
	private static final BaseText Permission_Denied = new LiteralText("×");
	private static final BaseText Permission_Not_Received = new LiteralText("§4Error:Permissions are not sent from a server.\nPlease check whether ParCool is installed or re-login to the server.§r");

	private void renderActions(MatrixStack matrixStack, int mouseX, int mouseY, float p_230430_4_, int offsetY) {
		int offsetX = 40, headerHeight = (int) (textRenderer.fontHeight * 1.5f);
		int headerOffsetY = offsetY + textRenderer.fontHeight * 2;
		int contentOffsetY = headerOffsetY + headerHeight + 2;
		int permissionColumnWidth = textRenderer.getWidth(Permission_Permitted) * 5;
		int nameColumnWidth = width - offsetX * 2 - permissionColumnWidth * 2;
		int contentHeight = height - contentOffsetY - textRenderer.fontHeight * 2;
		viewableItemCount = contentHeight / Checkbox_Item_Height;
		int headerTextY = headerOffsetY + headerHeight / 2 - textRenderer.fontHeight / 2 + 1;
		drawWithShadow(matrixStack, textRenderer, (OrderedText) Header_ActionName, offsetX + 5, headerTextY, color.getText());
		drawCenteredText(matrixStack, textRenderer, Header_ServerPermission, offsetX + nameColumnWidth + permissionColumnWidth / 2, headerTextY, color.getText());
		drawCenteredText(matrixStack, textRenderer, Header_IndividualPermission, offsetX + nameColumnWidth + permissionColumnWidth + permissionColumnWidth / 2, headerTextY, color.getText());
		for (CheckboxWidget actionButton : actionButtons) {
			actionButton.setWidth(0);
		}
		for (int i = 0; i < viewableItemCount && i + topIndex < actionButtons.length; i++) {
			CheckboxWidget button = actionButtons[i + topIndex];
			button.x = offsetX + 1;
			button.y = contentOffsetY + Checkbox_Item_Height * i;
			button.setWidth(nameColumnWidth - 5);
			//todo fix set height you goon
//			button.setHeight(20);
			button.render(matrixStack, mouseX, mouseY, p_230430_4_);
			fill(matrixStack, offsetX, button.y + button.getHeight(), width - offsetX, button.y + button.getHeight() + 1, color.getSubSeparator());
			int rowY = contentOffsetY + Checkbox_Item_Height * i + Checkbox_Item_Height / 2;
			boolean permitted = actionList[topIndex + i].serverWideLimitation.getAsBoolean();
			drawCenteredText(
					matrixStack, textRenderer,
					permitted ? Permission_Permitted : Permission_Denied,
					offsetX + nameColumnWidth + permissionColumnWidth / 2,
					rowY - textRenderer.fontHeight / 2,
					permitted ? 0x00AA00 : 0xAA0000
			);
			permitted = actionList[topIndex + i].individualLimitation.getAsBoolean();
			drawCenteredText(
					matrixStack, textRenderer,
					permitted ? Permission_Permitted : Permission_Denied,
					offsetX + nameColumnWidth + permissionColumnWidth + permissionColumnWidth / 2,
					rowY - textRenderer.fontHeight / 2,
					permitted ? 0x00AA00 : 0xAA0000
			);
		}
		fillGradient(matrixStack, 0, offsetY, width, headerOffsetY, color.getHeader1(), color.getHeader2());
		fillGradient(matrixStack, 0, contentOffsetY + contentHeight, width, height, color.getHeader1(), color.getHeader2());
		drawCenteredText(matrixStack, textRenderer, modeMenuList[0].title, width / 2, offsetY + textRenderer.fontHeight / 2 + 2, color.getStrongText());
		if (topIndex + viewableItemCount < actionButtons.length)
			drawCenteredText(matrixStack, textRenderer, new LiteralText("↓"), width / 2, height - textRenderer.fontHeight - textRenderer.fontHeight / 2, color.getStrongText());
		//draw separators
		fill(matrixStack, offsetX, contentOffsetY, width - offsetX, contentOffsetY - 1, color.getSeparator());
		fill(matrixStack, offsetX, headerOffsetY, offsetX + 1, contentOffsetY + contentHeight, color.getSeparator());
		fill(matrixStack, offsetX + nameColumnWidth, headerOffsetY, offsetX + nameColumnWidth + 1, contentOffsetY + contentHeight, color.getSeparator());
		fill(matrixStack, offsetX + nameColumnWidth + permissionColumnWidth, headerOffsetY, offsetX + nameColumnWidth + permissionColumnWidth + 1, contentOffsetY + contentHeight, color.getSeparator());
		fill(matrixStack, offsetX + nameColumnWidth + permissionColumnWidth * 2, headerOffsetY, offsetX + nameColumnWidth + permissionColumnWidth * 2 + 1, contentOffsetY + contentHeight, color.getSeparator());
		{// draw tooltip
			int columnCenter = offsetX + nameColumnWidth + permissionColumnWidth / 2;
			if ((headerOffsetY < mouseY && mouseY < headerOffsetY + headerHeight)
					&& (columnCenter - permissionColumnWidth / 2 < mouseX && mouseX < columnCenter + permissionColumnWidth / 2)
			) {
				if (serverPermissionReceived.getAsBoolean())
					renderTooltip(matrixStack, Collections.singletonList(Header_ServerPermissionText), mouseX, mouseY);
				else
					renderTooltip(
							matrixStack,
							Arrays.asList(Header_ServerPermissionText, Permission_Not_Received),
							mouseX, mouseY);
			}

			columnCenter = offsetX + nameColumnWidth + permissionColumnWidth + permissionColumnWidth / 2;
			if ((headerOffsetY < mouseY && mouseY < headerOffsetY + headerHeight)
					&& (columnCenter - permissionColumnWidth / 2 < mouseX && mouseX < columnCenter + permissionColumnWidth / 2)
			) {
				if (individualPermissionReceived.getAsBoolean())
					renderTooltip(matrixStack, Collections.singletonList(Header_IndividualPermissionText), mouseX, mouseY);
				else
					renderTooltip(
							matrixStack,
							Arrays.asList(Header_IndividualPermissionText, Permission_Not_Received),
							mouseX, mouseY);
			}
		}
	}

	private void renderConfigs(MatrixStack matrixStack, int mouseX, int mouseY, float p_230430_4_, int offsetY) {
		int offsetX = 40;
		int contentWidth = width - offsetX * 2;
		int contentHeight = height - offsetY - textRenderer.fontHeight * 4;
		int contentOffsetY = offsetY + textRenderer.fontHeight * 2;
		viewableItemCount = contentHeight / Checkbox_Item_Height;
		for (CheckboxWidget configButton : configButtons) {
			configButton.setWidth(0);
		}
		for (int i = 0; i < viewableItemCount && i + topIndex < configItemList.length; i++) {
			CheckboxWidget button = configButtons[i + topIndex];
			button.x = offsetX + 1;
			button.y = offsetY + textRenderer.fontHeight * 2 + Checkbox_Item_Height * i;
			button.setWidth(contentWidth);
			button.render(matrixStack, mouseX, mouseY, p_230430_4_);
			fill(matrixStack, offsetX, button.y + button.getHeight(), width - offsetX, button.y + button.getHeight() + 1, color.getSubSeparator());
		}
		fill(matrixStack, width - offsetX, contentOffsetY, width - offsetX - 1, contentOffsetY + contentHeight, color.getSeparator());
		fill(matrixStack, offsetX, contentOffsetY, offsetX + 1, contentOffsetY + contentHeight, color.getSeparator());
		fillGradient(matrixStack, 0, offsetY, width, offsetY + textRenderer.fontHeight * 2, color.getHeader1(), color.getHeader2());
		fillGradient(matrixStack, 0, offsetY + contentHeight + textRenderer.fontHeight * 2, width, height, color.getHeader1(), color.getHeader2());
		drawCenteredText(matrixStack, textRenderer, modeMenuList[1].title, width / 2, offsetY + textRenderer.fontHeight / 2 + 2, color.getStrongText());
		if (topIndex + viewableItemCount < configButtons.length)
			drawCenteredText(matrixStack, textRenderer, new LiteralText("↓"), width / 2, height - textRenderer.fontHeight - textRenderer.fontHeight / 2, color.getSubText());
	}

	private void renderLimitations(MatrixStack matrixStack, int mouseX, int mouseY, float p_230430_4_, int offsetY) {
		int offsetX = 40;
		int contentHeight = height - offsetY - textRenderer.fontHeight * 4;
		int contentOffsetY = offsetY + textRenderer.fontHeight * 2;
		int itemHeight = textRenderer.fontHeight * 2;
		int valueWidth = Arrays.stream(infoList).map(it -> textRenderer.getWidth(it.value)).max(Integer::compareTo).orElse(0);
		for (int i = 0; i < infoList.length; i++) {
			InfoSet item = infoList[i];
			drawStringWithShadow(
					matrixStack, textRenderer,
					item.name,
					offsetX + 5,
					contentOffsetY + itemHeight * i + itemHeight / 2 - textRenderer.fontHeight / 2,
					color.getText()
			);
			drawStringWithShadow(
					matrixStack, textRenderer,
					item.value,
					width - offsetX - 5 - valueWidth,
					contentOffsetY + itemHeight * i + itemHeight / 2 - textRenderer.fontHeight / 2,
					color.getText()
			);
			fill(matrixStack, offsetX, contentOffsetY + itemHeight * (i + 1), width - offsetX, contentOffsetY + itemHeight * (i + 1) + 1, color.getSubSeparator());
		}
		fill(matrixStack, width - offsetX, contentOffsetY, width - offsetX - 1, contentOffsetY + contentHeight, color.getSeparator());
		fill(matrixStack, offsetX, contentOffsetY, offsetX + 1, contentOffsetY + contentHeight, color.getSeparator());
		fillGradient(matrixStack, 0, offsetY, width, offsetY + textRenderer.fontHeight * 2, color.getHeader1(), color.getHeader2());
		fillGradient(matrixStack, 0, offsetY + contentHeight + textRenderer.fontHeight * 2, width, height, color.getHeader1(), color.getHeader2());
		drawCenteredText(matrixStack, textRenderer, modeMenuList[2].title, width / 2, offsetY + textRenderer.fontHeight / 2 + 2, 0x8888FF);
	}

	@Override
	public void renderBackground(@NotNull MatrixStack p_238651_1_, int p_238651_2_) {
		fill(p_238651_1_, 0, 0, this.width, this.height, color.getBackground());
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		switch (keyCode) {
			case GLFW.GLFW_KEY_RIGHT:
				if (modeIndex < modeMenuList.length - 1) {
					modeIndex++;
					modeMenuList[modeIndex].set();
				}
				break;
			case GLFW.GLFW_KEY_LEFT:
				if (modeIndex > 0) {
					modeIndex--;
					modeMenuList[modeIndex].set();
				}
				break;
			case GLFW.GLFW_KEY_UP:
				mouseScrolled(0, 0, 1);
				break;
			case GLFW.GLFW_KEY_DOWN:
				mouseScrolled(0, 0, -1);
				break;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean mouseScrolled(double x, double y, double value) {
		topIndex -= (int) Math.signum(value);
		switch (mode) {
			case Actions:
				if (topIndex > actionButtons.length - viewableItemCount)
					topIndex = actionButtons.length - viewableItemCount;
				break;
			case Configs:
				if (topIndex > configButtons.length - viewableItemCount)
					topIndex = configButtons.length - viewableItemCount;
		}
		if (topIndex < 0) topIndex = 0;
		return true;
	}

	private static class ModeSet {
		final BaseText title;
		final SettingMode mode;
		final ParCoolSettingScreen parent;
		final int index;
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;

		ModeSet(BaseText title, SettingMode mode, int index, ParCoolSettingScreen parent) {
			this.title = title;
			this.index = index;
			this.mode = mode;
			this.parent = parent;
		}

		void set() {
			MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
			parent.topIndex = 0;
			parent.mode = mode;
			parent.modeIndex = index;
		}

		boolean isMouseIn(int mouseX, int mouseY) {
			return (x < mouseX && mouseX < x + width) && (y < mouseY && mouseY < y + height);
		}
	}

	private static class ConfigSet {
		final String name;
		final Consumer<Boolean> setter;
		final BooleanSupplier getter;

		ConfigSet(String name, Consumer<Boolean> setter, BooleanSupplier getter) {
			this.name = name;
			this.getter = getter;
			this.setter = setter;
		}
	}

	private static class InfoSet {
		final String name;
		final String value;

		InfoSet(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}

	private static class ActionConfigSet {
		final String name;
		final Consumer<Boolean> setter;
		final BooleanSupplier getter;
		final BooleanSupplier serverWideLimitation;
		final BooleanSupplier individualLimitation;

		ActionConfigSet(Class<? extends Action> action, ActionInfo info) {
			name = new TranslatableText("parcool.action." + action.getSimpleName()).getString();
			ForgeConfigSpec.BooleanValue config = CONFIG_CLIENT.getPossibilityOf(action);
			setter = config::set;
			getter = config::get;
			serverWideLimitation = () -> info.getServerLimitation().isPermitted(action);
			individualLimitation = () -> info.getIndividualLimitation().isPermitted(action);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int type) {//type:1->right 0->left
		for (ModeSet modeSet : modeMenuList) {
			if (modeSet.isMouseIn((int) mouseX, (int) mouseY) && type == 0) {
				modeSet.set();
				return true;
			}
		}
		switch (mode) {
			case Actions:
				for (CheckboxWidget button : actionButtons) {
					button.mouseClicked(mouseX, mouseY, type);
				}
				break;
			case Configs:
				for (CheckboxWidget button : configButtons) {
					button.mouseClicked(mouseX, mouseY, type);
				}
		}
		return false;
	}
}
