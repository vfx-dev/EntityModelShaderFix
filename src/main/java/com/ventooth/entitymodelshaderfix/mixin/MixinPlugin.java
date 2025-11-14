/*
 * This file is part of EntityModelShaderFix.
 *
 * Copyright (C) 2025 Ven
 * All Rights Reserved
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * EntityModelShaderFix is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, only version 3 of the License.
 *
 * EntityModelShaderFix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with EntityModelShaderFix. If not, see <https://www.gnu.org/licenses/>.
 */

package com.ventooth.entitymodelshaderfix.mixin;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import lombok.NoArgsConstructor;
import lombok.val;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
public final class MixinPlugin implements IMixinConfigPlugin {
    public static final Logger log = LoggerFactory.getLogger("EntityModelShaderFix|Mixins");

    private boolean shouldApply = false;

    @Override
    public void onLoad(final String mixinPackage) {
        MixinExtrasBootstrap.init();

        val hasOculus = hasClass("net.irisshaders.iris.Iris");
        if (hasOculus) {
            log.debug("Found Oculus");
        }
        val hasEntityTextureFeatures = hasClass("traben.entity_texture_features.ETF");
        if (hasEntityTextureFeatures) {
            log.debug("Found Entity Texture Features");
        }

        shouldApply = hasOculus && hasEntityTextureFeatures;
        if (shouldApply) {
            log.debug("Will do work");
        } else {
            log.debug("Nothing to do!");
            if (!hasOculus) {
                log.debug("Missing Oculus");
            }
            if (!hasEntityTextureFeatures) {
                log.debug("Missing Entity Texture Features");
            }
        }
    }

    @Override
    public boolean shouldApplyMixin(final String targetClassName, final String mixinClassName) {
        return shouldApply;
    }

    private static boolean hasClass(final String className) {
        try {
            MixinService.getService()
                        .getBytecodeProvider()
                        .getClassNode(className);
            return true;
        } catch (ClassNotFoundException | IOException e) {
            return false;
        }
    }

    // region Unused
    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
    // endregion
}