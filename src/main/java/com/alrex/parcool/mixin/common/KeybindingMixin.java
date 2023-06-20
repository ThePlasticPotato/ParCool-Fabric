package com.alrex.parcool.mixin.common;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.alrex.parcool.utilities.KeybindFixer;

import java.util.Map;

@Mixin(KeyBinding.class)
public abstract class KeybindingMixin {

    @Final
    @Shadow private static Map<String, KeyBinding> KEYS_BY_ID;
    @Shadow private InputUtil.Key boundKey;

    @Inject(method = "onKeyPressed", at = @At(value = "HEAD"), cancellable = true)
    private static void onKeyPressedFixed(InputUtil.Key key, CallbackInfo ci){
        KeybindFixer.INSTANCE.onKeyPressed(key);
        ci.cancel();
    }

    @Inject(method = "setKeyPressed", at = @At(value = "HEAD"), cancellable = true)
    private static void setKeyPressedFixed(InputUtil.Key key, boolean pressed, CallbackInfo ci){
        KeybindFixer.INSTANCE.setKeyPressed(key, pressed);
        ci.cancel();
    }

    @Inject(method = "updateKeysByCode", at = @At(value = "TAIL"))
    private static void updateByCodeToMultiMap(CallbackInfo ci) {
        KeybindFixer.INSTANCE.clearMap();
        for (KeyBinding keyBinding : KEYS_BY_ID.values()) {
            KeybindFixer.INSTANCE.putKey(((BoundKeyAccessor) keyBinding).getBoundKey(),keyBinding);
        }
    }

    @Inject(method = "<init>(Ljava/lang/String;Lnet/minecraft/client/util/InputUtil$Type;ILjava/lang/String;)V", at = @At(value = "TAIL"))
    private void putToMultiMap(String translationKey, InputUtil.Type type, int code, String category, CallbackInfo ci){
        KeybindFixer.INSTANCE.putKey(boundKey, (KeyBinding) (Object) this);
    }

}
