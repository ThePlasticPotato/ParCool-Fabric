package com.alrex.parcool.common.capability;

import com.alrex.parcool.common.capability.capabilities.Capabilities;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;


public interface IStamina extends Component, AutoSyncedComponent, CommonTickingComponent {
	@Nullable
	public static IStamina get(PlayerEntity player) {
		LazyOptional<IStamina> optional = LazyOptional.of(() -> player.getComponent(Capabilities.STAMINA_CAPABILITY));
		return optional.orElse(null);
	}

	public int getMaxStamina();

	public int getActualMaxStamina();

	public void setMaxStamina(int value);

	public int get();

	public int getOldValue();

	public void consume(int value);

	public void recover(int value);

	public boolean isExhausted();

	public void setExhaustion(boolean value);

	public void tick();

	public void set(int value);
}
