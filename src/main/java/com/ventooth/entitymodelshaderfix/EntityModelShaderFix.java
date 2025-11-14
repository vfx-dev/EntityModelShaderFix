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

package com.ventooth.entitymodelshaderfix;


import lombok.val;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Share.MOD_ID)
public final class EntityModelShaderFix {
    public EntityModelShaderFix(FMLJavaModLoadingContext context) {
        val modList = ModList.get();
        if (modList.isLoaded("entity_texture_features") && modList.isLoaded("oculus")) {
            Share.log.info("Re-attaching whiskers to cats...");
        } else {
            Share.log.info("Cat whiskers are probably still there, but go check anyway!");
        }
    }
}
