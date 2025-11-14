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

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.irisshaders.batchedentityrendering.impl.FullyBufferedMultiBufferSource;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import traben.entity_texture_features.features.ETFRenderContext;

/**
 * this is a copy of {@link net.minecraft.client.renderer.MultiBufferSource.BufferSource} but for iris's
 * custom entity {@link MultiBufferSource}
 * <p>
 * this should have no negative impact on iris's render process, other than of course adding more code that needs to run
 */
@Pseudo
@SuppressWarnings("ALL") // Added to ignore warnings not owned by EntityModelShaderFix
@Mixin(FullyBufferedMultiBufferSource.class)
public class MixinFullyBufferedMultiBufferSource {
    @ModifyVariable(method = "getBuffer",
                    at = @At(value = "HEAD"),
                    index = 1,
                    argsOnly = true)
    private RenderType etf$modifyRenderLayer(RenderType value) {
        RenderType newLayer = ETFRenderContext.modifyRenderLayerIfRequired(value);
        return newLayer == null ? value : newLayer;
    }


    @Inject(method = "getBuffer",
            at = @At(value = "RETURN"))
    private void etf$injectIntoGetBufferReturn(RenderType renderLayer, CallbackInfoReturnable<VertexConsumer> cir) {
        ETFRenderContext.insertETFDataIntoVertexConsumer((MultiBufferSource) this, renderLayer, cir.getReturnValue());
    }
}

