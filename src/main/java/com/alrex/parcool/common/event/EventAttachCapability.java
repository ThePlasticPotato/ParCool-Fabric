//package com.alrex.parcool.common.event;
//
//import com.alrex.parcool.ParCoolConfig;
//import com.alrex.parcool.common.capability.IStamina;
//import com.alrex.parcool.common.capability.capabilities.Capabilities;
//import com.alrex.parcool.common.capability.impl.Animation;
//import com.alrex.parcool.common.capability.impl.Parkourability;
//import com.alrex.parcool.common.capability.impl.Stamina;
//import com.alrex.parcool.common.capability.storage.ParkourabilityStorage;
//import com.alrex.parcool.common.capability.storage.StaminaStorage;
//
//import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
//import io.github.fabricators_of_create.porting_lib.util.INBTSerializable;
//import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.nbt.NbtCompound;
//
//
//;
//
//public class EventAttachCapability {
//
//	public static void onAttachCapability(c) {
//		if (!(event.getObject() instanceof PlayerEntity)) return;
//		PlayerEntity player = (PlayerEntity) event.getObject();
//		//Parkourability
//		{
//			Parkourability instance = new Parkourability();
//			LazyOptional<Parkourability> optional = LazyOptional.of(() -> instance);
//			INBTSerializable<NbtCompound> provider = new INBTSerializable<>() {
//				@Override
//				public NbtCompound serializeNBT() {
//					return (NbtCompound) new ParkourabilityStorage().writeTag(
//							Capabilities.PARKOURABILITY_CAPABILITY,
//							instance,
//							null
//					);
//				}
//
//				@Override
//				public void deserializeNBT(NbtCompound nbt) {
//					new ParkourabilityStorage().readTag(
//							Capabilities.PARKOURABILITY_CAPABILITY,
//							instance,
//							null,
//							nbt
//					);
//				}
//
//			};
//			event.addCapability(Capabilities.PARKOURABILITY_LOCATION, provider);
//		}
//		//Stamina
//		{
//			IStamina instance = null;
//			if (player.isLocalPlayer() && FeathersManager.isUsingFeathers()) {
//				instance = FeathersManager.newFeathersStaminaFor(player);
//			}
//			if (instance == null) {
//				instance = new Stamina(player);
//			}
//			final IStamina finalInstance = instance;
//			LazyOptional<IStamina> optional = LazyOptional.of(() -> finalInstance);
//			if (player.isLocalPlayer()) {
//				instance.setMaxStamina(ParCoolConfig.CONFIG_CLIENT.staminaMax.get());
//			}
//			ComponentProvider provider = new ICapabilitySerializable<CompoundTag>() {
//				@Override
//				public CompoundTag serializeNBT() {
//					return (CompoundTag) new StaminaStorage().writeTag(
//							Capabilities.STAMINA_CAPABILITY,
//							finalInstance,
//							null
//					);
//				}
//
//				@Override
//				public void deserializeNBT(CompoundTag nbt) {
//					new StaminaStorage().readTag(
//							Capabilities.STAMINA_CAPABILITY,
//							finalInstance,
//							null,
//							nbt
//					);
//				}
//
//				@Nonnull
//				@Override
//				public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//					if (cap == Capabilities.STAMINA_CAPABILITY) {
//						return optional.cast();
//					}
//					return LazyOptional.empty();
//				}
//			};
//			event.addCapability(Capabilities.STAMINA_LOCATION, provider);
//		}
//		if (event.getObject().level.isClientSide) {
//			//Animation
//			{
//				Animation instance = new Animation();
//				LazyOptional<Animation> optional = LazyOptional.of(() -> instance);
//				ICapabilityProvider provider = new ICapabilityProvider() {
//					@Nonnull
//					@Override
//					public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//						if (cap == Capabilities.ANIMATION_CAPABILITY) {
//							return optional.cast();
//						}
//						return LazyOptional.empty();
//					}
//				};
//				event.addCapability(Capabilities.ANIMATION_LOCATION, provider);
//			}
//		}
//	}
//}
