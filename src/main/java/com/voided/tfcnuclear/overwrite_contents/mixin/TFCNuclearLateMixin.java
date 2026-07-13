package com.voided.tfcnuclear.overwrite_contents.mixin;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Collections;
import java.util.List;

public class TFCNuclearLateMixin implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("mixins.tfcnuclear.json");
    }
}