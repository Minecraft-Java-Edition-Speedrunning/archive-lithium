package me.jellysquid.mods.lithium.mixin.entity.replace_entitytype_predicates;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.jellysquid.mods.lithium.common.world.WorldHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.function.Predicate;

@Mixin(AbstractDecorationEntity.class)
public abstract class AbstractDecorationEntityMixin extends Entity {
    @Shadow
    @Final
    protected static Predicate<Entity> PREDICATE; // entity instanceof AbstractDecorationEntity

    public AbstractDecorationEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @WrapOperation(
            method = "canStayAttached",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"
            )
    )
    private List<Entity> getAbstractDecorationEntities(World world, Entity excluded, Box box, Predicate<? super Entity> predicate, Operation<List<Entity>> original) {
        if (predicate == PREDICATE) {
            return WorldHelper.getEntitiesOfClass(world, excluded, AbstractDecorationEntity.class, box);
        }
        return original.call(world, excluded, box, predicate);
    }
}
