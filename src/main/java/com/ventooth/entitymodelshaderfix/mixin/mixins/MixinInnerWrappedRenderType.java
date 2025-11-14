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

package com.ventooth.entitymodelshaderfix.mixin.mixins;

import net.irisshaders.iris.layer.InnerWrappedRenderType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import traben.entity_texture_features.mixin.mixins.mods.iris.MixinOuterWrappedRenderType;
import traben.entity_texture_features.utils.ETFRenderLayerWithTexture;

import java.util.Optional;

/**
 * Required in case Iris wraps an instance of {@link ETFRenderLayerWithTexture}
 * <p>
 * This is assumed to be required, whereas I know {@link MixinOuterWrappedRenderType} is required.
 */
@Pseudo
@SuppressWarnings("ALL") // Added to ignore warnings not owned by EntityModelShaderFix
@Mixin(value = InnerWrappedRenderType.class)
public abstract class MixinInnerWrappedRenderType implements ETFRenderLayerWithTexture {
    @Shadow(remap = false)
    public abstract RenderType unwrap();

    @Override
    public Optional<ResourceLocation> etf$getId() {
        if (unwrap() instanceof ETFRenderLayerWithTexture etf)
            return etf.etf$getId();
        return Optional.empty();
    }
}
