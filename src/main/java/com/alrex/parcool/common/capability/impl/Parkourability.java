package com.alrex.parcool.common.capability.impl;

import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.ActionList;
import com.alrex.parcool.common.action.AdditionalProperties;
import com.alrex.parcool.common.capability.capabilities.Capabilities;
import com.alrex.parcool.common.info.ActionInfo;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class Parkourability implements Component {
	@Nullable
	public static Parkourability get(PlayerEntity player) {
		Parkourability optional = (Parkourability) player.getComponent(ComponentRegistry.get(Capabilities.PARKOURABILITY_LOCATION));
		return optional;
	}

	private final ActionInfo info = new ActionInfo();
	private final AdditionalProperties properties = new AdditionalProperties();

	private final List<Action> actions = ActionList.constructActionsList();
	private final HashMap<Class<? extends Action>, Action> actionsMap;

	public Parkourability() {
		actionsMap = new HashMap<>((int) (actions.size() * 1.5));
		for (short i = 0; i < actions.size(); i++) {
			Action action = actions.get(i);
			actionsMap.put(action.getClass(), action);
		}
	}

	public <T extends Action> T get(Class<T> action) {
		T value = (T) actionsMap.getOrDefault(action, null);
		if (value == null) {
			throw new IllegalArgumentException("The Action instance is not registered:" + action.getSimpleName());
		}
		return value;
	}

	public short getActionID(Action instance) {
		return ActionList.getIndexOf(instance.getClass());
	}

	@Nullable
	public Action getActionFromID(short id) {
		if (0 <= id && id < actions.size()) {
			return actions.get(id);
		}
		return null;
	}

	public AdditionalProperties getAdditionalProperties() {
		return properties;
	}

	public ActionInfo getActionInfo() {
		return info;
	}

	public List<Action> getList() {
		return actions;
	}

	public void CopyFrom(Parkourability original) {
		getActionInfo().getIndividualLimitation().readTag(original.getActionInfo().getIndividualLimitation().writeTag());
		getActionInfo().getServerLimitation().readTag(original.getActionInfo().getServerLimitation().writeTag());
	}

	@Override
	public void readFromNbt(NbtCompound tag) {

	}

	@Override
	public void writeToNbt(NbtCompound tag) {

	}
}
