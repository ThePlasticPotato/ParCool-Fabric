package com.alrex.parcool.utilities;

import com.alrex.parcool.mixin.common.TimesPressedAccessor;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeybindFixer {

    public static KeybindFixer INSTANCE = new KeybindFixer();

    private Multimap<InputUtil.Key, KeyBinding> keyFixMap = ArrayListMultimap.create();

    public void putKey(InputUtil.Key key, KeyBinding keyBinding) {
        keyFixMap.put(key, keyBinding);
    }

    public void clearMap() {
        keyFixMap.clear();
    }

    public void onKeyPressed(InputUtil.Key key) {
        for (KeyBinding keyBinding : keyFixMap.get(key)) {
            ((TimesPressedAccessor) keyBinding).setTimesPressed(1);
        }
    }

    public void setKeyPressed(InputUtil.Key key, boolean pressed) {
        for (KeyBinding keyBinding : keyFixMap.get(key)) {
            keyBinding.setPressed(pressed);
        }
    }
}
