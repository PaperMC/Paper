package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.DummyGeneratorAccess;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.world.StructureGrowEvent;
// CraftBukkit end

public interface IDispenseBehavior {

    IDispenseBehavior NONE = (isourceblock, itemstack) -> {
        return itemstack;
    };

    ItemStack dispense(ISourceBlock isourceblock, ItemStack itemstack);

    static void c() {
        BlockDispenser.a((IMaterial) Items.ARROW, (IDispenseBehavior) (new DispenseBehaviorProjectile() {
            @Override
            protected IProjectile a(World world, IPosition iposition, ItemStack itemstack) {
                EntityTippedArrow entitytippedarrow = new EntityTippedArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());

                entitytippedarrow.fromPlayer = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        }));
        BlockDispenser.a((IMaterial) Items.TIPPED_ARROW, (IDispenseBehavior) (new DispenseBehaviorProjectile() {
            @Override
            protected IProjectile a(World world, IPosition iposition, ItemStack itemstack) {
                EntityTippedArrow entitytippedarrow = new EntityTippedArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());

                entitytippedarrow.b(itemstack);
                entitytippedarrow.fromPlayer = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        }));
        BlockDispenser.a((IMaterial) Items.SPECTRAL_ARROW, (IDispenseBehavior) (new DispenseBehaviorProjectile() {
            @Override
            protected IProjectile a(World world, IPosition iposition, ItemStack itemstack) {
                EntitySpectralArrow entityspectralarrow = new EntitySpectralArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());

                entityspectralarrow.fromPlayer = EntityArrow.PickupStatus.ALLOWED;
                return entityspectralarrow;
            }
        }));
        BlockDispenser.a((IMaterial) Items.EGG, (IDispenseBehavior) (new DispenseBehaviorProjectile() {
            @Override
            protected IProjectile a(World world, IPosition iposition, ItemStack itemstack) {
                return (IProjectile) SystemUtils.a((new EntityEgg(world, iposition.getX(), iposition.getY(), iposition.getZ())), (entityegg) -> { // CraftBukkit - decompile error
                    entityegg.setItem(itemstack);
                });
            }
        }));
        BlockDispenser.a((IMaterial) Items.SNOWBALL, (IDispenseBehavior) (new DispenseBehaviorProjectile() {
            @Override
            protected IProjectile a(World world, IPosition iposition, ItemStack itemstack) {
                return (IProjectile) SystemUtils.a((new EntitySnowball(world, iposition.getX(), iposition.getY(), iposition.getZ())), (entitysnowball) -> { // CraftBukkit - decompile error
                    entitysnowball.setItem(itemstack);
                });
            }
        }));
        BlockDispenser.a((IMaterial) Items.EXPERIENCE_BOTTLE, (IDispenseBehavior) (new DispenseBehaviorProjectile() {
            @Override
            protected IProjectile a(World world, IPosition iposition, ItemStack itemstack) {
                return (IProjectile) SystemUtils.a((new EntityThrownExpBottle(world, iposition.getX(), iposition.getY(), iposition.getZ())), (entitythrownexpbottle) -> { // CraftBukkit - decompile error
                    entitythrownexpbottle.setItem(itemstack);
                });
            }

            @Override
            protected float a() {
                return super.a() * 0.5F;
            }

            @Override
            protected float getPower() {
                return super.getPower() * 1.25F;
            }
        }));
        BlockDispenser.a((IMaterial) Items.SPLASH_POTION, new IDispenseBehavior() {
            @Override
            public ItemStack dispense(ISourceBlock isourceblock, ItemStack itemstack) {
                return (new DispenseBehaviorProjectile() {
                    @Override
                    protected IProjectile a(World world, IPosition iposition, ItemStack itemstack1) {
                        return (IProjectile) SystemUtils.a((new EntityPotion(world, iposition.getX(), iposition.getY(), iposition.getZ())), (entitypotion) -> { // CraftBukkit - decompile error
                            entitypotion.setItem(itemstack1);
                        });
                    }

                    @Override
                    protected float a() {
                        return super.a() * 0.5F;
                    }

                    @Override
                    protected float getPower() {
                        return super.getPower() * 1.25F;
                    }
                }).dispense(isourceblock, itemstack);
            }
        });
        BlockDispenser.a((IMaterial) Items.LINGERING_POTION, new IDispenseBehavior() {
            @Override
            public ItemStack dispense(ISourceBlock isourceblock, ItemStack itemstack) {
                return (new DispenseBehaviorProjectile() {
                    @Override
                    protected IProjectile a(World world, IPosition iposition, ItemStack itemstack1) {
                        return (IProjectile) SystemUtils.a((new EntityPotion(world, iposition.getX(), iposition.getY(), iposition.getZ())), (entitypotion) -> { // CraftBukkit - decompile error
                            entitypotion.setItem(itemstack1);
                        });
                    }

                    @Override
                    protected float a() {
                        return super.a() * 0.5F;
                    }

                    @Override
                    protected float getPower() {
                        return super.getPower() * 1.25F;
                    }
                }).dispense(isourceblock, itemstack);
            }
        });
        DispenseBehaviorItem dispensebehavioritem = new DispenseBehaviorItem() {
            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
                EntityTypes<?> entitytypes = ((ItemMonsterEgg) itemstack.getItem()).a(itemstack.getTag());

                // CraftBukkit start
                WorldServer worldserver = isourceblock.getWorld();
                ItemStack itemstack1 = itemstack.cloneAndSubtract(1);
                org.bukkit.block.Block block = worldserver.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

                BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(0, 0, 0));
                if (!BlockDispenser.eventFired) {
                    worldserver.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    itemstack.add(1);
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    itemstack.add(1);
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(eventStack.getItem());
                    if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }

                entitytypes.spawnCreature(isourceblock.getWorld(), itemstack, (EntityHuman) null, isourceblock.getBlockPosition().shift(enumdirection), EnumMobSpawn.DISPENSER, enumdirection != EnumDirection.UP, false);
                // itemstack.subtract(1); // Handled during event processing
                // CraftBukkit end
                return itemstack;
            }
        };
        Iterator iterator = ItemMonsterEgg.f().iterator();

        while (iterator.hasNext()) {
            ItemMonsterEgg itemmonsteregg = (ItemMonsterEgg) iterator.next();

            BlockDispenser.a((IMaterial) itemmonsteregg, (IDispenseBehavior) dispensebehavioritem);
        }

        BlockDispenser.a((IMaterial) Items.ARMOR_STAND, (IDispenseBehavior) (new DispenseBehaviorItem() {
            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
                BlockPosition blockposition = isourceblock.getBlockPosition().shift(enumdirection);
                WorldServer worldserver = isourceblock.getWorld();

                // CraftBukkit start
                ItemStack itemstack1 = itemstack.cloneAndSubtract(1);
                org.bukkit.block.Block block = worldserver.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

                BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(0, 0, 0));
                if (!BlockDispenser.eventFired) {
                    worldserver.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    itemstack.add(1);
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    itemstack.add(1);
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(eventStack.getItem());
                    if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }
                // CraftBukkit end

                EntityArmorStand entityarmorstand = new EntityArmorStand(worldserver, (double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D);

                EntityTypes.a((World) worldserver, (EntityHuman) null, (Entity) entityarmorstand, itemstack.getTag());
                entityarmorstand.yaw = enumdirection.o();
                worldserver.addEntity(entityarmorstand);
                // itemstack.subtract(1); // CraftBukkit - Handled during event processing
                return itemstack;
            }
        }));
        BlockDispenser.a((IMaterial) Items.SADDLE, (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                List<EntityLiving> list = isourceblock.getWorld().a(EntityLiving.class, new AxisAlignedBB(blockposition), (entityliving) -> {
                    if (!(entityliving instanceof ISaddleable)) {
                        return false;
                    } else {
                        ISaddleable isaddleable = (ISaddleable) entityliving;

                        return !isaddleable.hasSaddle() && isaddleable.canSaddle();
                    }
                });

                if (!list.isEmpty()) {
                    ((ISaddleable) list.get(0)).saddle(SoundCategory.BLOCKS);
                    itemstack.subtract(1);
                    this.a(true);
                    return itemstack;
                } else {
                    return super.a(isourceblock, itemstack);
                }
            }
        }));
        DispenseBehaviorMaybe dispensebehaviormaybe = new DispenseBehaviorMaybe() {
            @Override
            protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                List<EntityHorseAbstract> list = isourceblock.getWorld().a(EntityHorseAbstract.class, new AxisAlignedBB(blockposition), (entityhorseabstract) -> {
                    return entityhorseabstract.isAlive() && entityhorseabstract.fs();
                });
                Iterator iterator1 = list.iterator();

                EntityHorseAbstract entityhorseabstract;

                do {
                    if (!iterator1.hasNext()) {
                        return super.a(isourceblock, itemstack);
                    }

                    entityhorseabstract = (EntityHorseAbstract) iterator1.next();
                } while (!entityhorseabstract.l(itemstack) || entityhorseabstract.ft() || !entityhorseabstract.isTamed());

                entityhorseabstract.a_(401, itemstack.cloneAndSubtract(1));
                this.a(true);
                return itemstack;
            }
        };

        BlockDispenser.a((IMaterial) Items.LEATHER_HORSE_ARMOR, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.IRON_HORSE_ARMOR, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.GOLDEN_HORSE_ARMOR, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.DIAMOND_HORSE_ARMOR, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.fM, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.fN, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.fV, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.fX, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.fY, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.gb, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.fT, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.fZ, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.fP, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.fU, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.fR, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.fO, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.fS, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.fW, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.ga, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.fQ, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.cy, (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                List<EntityHorseChestedAbstract> list = isourceblock.getWorld().a(EntityHorseChestedAbstract.class, new AxisAlignedBB(blockposition), (entityhorsechestedabstract) -> {
                    return entityhorsechestedabstract.isAlive() && !entityhorsechestedabstract.isCarryingChest();
                });
                Iterator iterator1 = list.iterator();

                EntityHorseChestedAbstract entityhorsechestedabstract;

                do {
                    if (!iterator1.hasNext()) {
                        return super.a(isourceblock, itemstack);
                    }

                    entityhorsechestedabstract = (EntityHorseChestedAbstract) iterator1.next();
                } while (!entityhorsechestedabstract.isTamed() || !entityhorsechestedabstract.a_(499, itemstack));

                itemstack.subtract(1);
                this.a(true);
                return itemstack;
            }
        }));
        BlockDispenser.a((IMaterial) Items.FIREWORK_ROCKET, (IDispenseBehavior) (new DispenseBehaviorItem() {
            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
                // CraftBukkit start
                WorldServer worldserver = isourceblock.getWorld();
                ItemStack itemstack1 = itemstack.cloneAndSubtract(1);
                org.bukkit.block.Block block = worldserver.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

                BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(enumdirection.getAdjacentX(), enumdirection.getAdjacentY(), enumdirection.getAdjacentZ()));
                if (!BlockDispenser.eventFired) {
                    worldserver.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    itemstack.add(1);
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    itemstack.add(1);
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(eventStack.getItem());
                    if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }

                itemstack1 = CraftItemStack.asNMSCopy(event.getItem());
                EntityFireworks entityfireworks = new EntityFireworks(isourceblock.getWorld(), itemstack, isourceblock.getX(), isourceblock.getY(), isourceblock.getX(), true);

                IDispenseBehavior.a(isourceblock, entityfireworks, enumdirection);
                entityfireworks.shoot((double) enumdirection.getAdjacentX(), (double) enumdirection.getAdjacentY(), (double) enumdirection.getAdjacentZ(), 0.5F, 1.0F);
                isourceblock.getWorld().addEntity(entityfireworks);
                // itemstack.subtract(1); // Handled during event processing
                // CraftBukkit end
                return itemstack;
            }

            @Override
            protected void a(ISourceBlock isourceblock) {
                isourceblock.getWorld().triggerEffect(1004, isourceblock.getBlockPosition(), 0);
            }
        }));
        BlockDispenser.a((IMaterial) Items.FIRE_CHARGE, (IDispenseBehavior) (new DispenseBehaviorItem() {
            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
                IPosition iposition = BlockDispenser.a(isourceblock);
                double d0 = iposition.getX() + (double) ((float) enumdirection.getAdjacentX() * 0.3F);
                double d1 = iposition.getY() + (double) ((float) enumdirection.getAdjacentY() * 0.3F);
                double d2 = iposition.getZ() + (double) ((float) enumdirection.getAdjacentZ() * 0.3F);
                WorldServer worldserver = isourceblock.getWorld();
                Random random = worldserver.random;
                double d3 = random.nextGaussian() * 0.05D + (double) enumdirection.getAdjacentX();
                double d4 = random.nextGaussian() * 0.05D + (double) enumdirection.getAdjacentY();
                double d5 = random.nextGaussian() * 0.05D + (double) enumdirection.getAdjacentZ();

                // CraftBukkit start
                ItemStack itemstack1 = itemstack.cloneAndSubtract(1);
                org.bukkit.block.Block block = worldserver.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

                BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(d3, d4, d5));
                if (!BlockDispenser.eventFired) {
                    worldserver.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    itemstack.add(1);
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    itemstack.add(1);
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(eventStack.getItem());
                    if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }

                EntitySmallFireball entitysmallfireball = new EntitySmallFireball(worldserver, d0, d1, d2, event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ());
                entitysmallfireball.setItem(itemstack1);
                entitysmallfireball.projectileSource = new org.bukkit.craftbukkit.projectiles.CraftBlockProjectileSource((TileEntityDispenser) isourceblock.getTileEntity());

                worldserver.addEntity(entitysmallfireball);
                // itemstack.subtract(1); // Handled during event processing
                // CraftBukkit end
                return itemstack;
            }

            @Override
            protected void a(ISourceBlock isourceblock) {
                isourceblock.getWorld().triggerEffect(1018, isourceblock.getBlockPosition(), 0);
            }
        }));
        BlockDispenser.a((IMaterial) Items.OAK_BOAT, (IDispenseBehavior) (new DispenseBehaviorBoat(EntityBoat.EnumBoatType.OAK)));
        BlockDispenser.a((IMaterial) Items.SPRUCE_BOAT, (IDispenseBehavior) (new DispenseBehaviorBoat(EntityBoat.EnumBoatType.SPRUCE)));
        BlockDispenser.a((IMaterial) Items.BIRCH_BOAT, (IDispenseBehavior) (new DispenseBehaviorBoat(EntityBoat.EnumBoatType.BIRCH)));
        BlockDispenser.a((IMaterial) Items.JUNGLE_BOAT, (IDispenseBehavior) (new DispenseBehaviorBoat(EntityBoat.EnumBoatType.JUNGLE)));
        BlockDispenser.a((IMaterial) Items.DARK_OAK_BOAT, (IDispenseBehavior) (new DispenseBehaviorBoat(EntityBoat.EnumBoatType.DARK_OAK)));
        BlockDispenser.a((IMaterial) Items.ACACIA_BOAT, (IDispenseBehavior) (new DispenseBehaviorBoat(EntityBoat.EnumBoatType.ACACIA)));
        DispenseBehaviorItem dispensebehavioritem1 = new DispenseBehaviorItem() {
            private final DispenseBehaviorItem b = new DispenseBehaviorItem();

            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                ItemBucket itembucket = (ItemBucket) itemstack.getItem();
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                WorldServer worldserver = isourceblock.getWorld();

                // CraftBukkit start
                int x = blockposition.getX();
                int y = blockposition.getY();
                int z = blockposition.getZ();
                IBlockData iblockdata = worldserver.getType(blockposition);
                Material material = iblockdata.getMaterial();
                if (worldserver.isEmpty(blockposition) || !material.isBuildable() || material.isReplaceable() || ((iblockdata.getBlock() instanceof IFluidContainer) && ((IFluidContainer) iblockdata.getBlock()).canPlace(worldserver, blockposition, iblockdata, itembucket.fluidType))) {
                    org.bukkit.block.Block block = worldserver.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
                    CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                    BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(x, y, z));
                    if (!BlockDispenser.eventFired) {
                        worldserver.getServer().getPluginManager().callEvent(event);
                    }

                    if (event.isCancelled()) {
                        return itemstack;
                    }

                    if (!event.getItem().equals(craftItem)) {
                        // Chain to handler for new item
                        ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                        IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(eventStack.getItem());
                        if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
                            idispensebehavior.dispense(isourceblock, eventStack);
                            return itemstack;
                        }
                    }

                    itembucket = (ItemBucket) CraftItemStack.asNMSCopy(event.getItem()).getItem();
                }
                // CraftBukkit end

                if (itembucket.a((EntityHuman) null, (World) worldserver, blockposition, (MovingObjectPositionBlock) null)) {
                    itembucket.a((World) worldserver, itemstack, blockposition);
                    // CraftBukkit start - Handle stacked buckets
                    Item item = Items.BUCKET;
                    itemstack.subtract(1);
                    if (itemstack.isEmpty()) {
                        itemstack.setItem(Items.BUCKET);
                        itemstack.setCount(1);
                    } else if (((TileEntityDispenser) isourceblock.getTileEntity()).addItem(new ItemStack(item)) < 0) {
                        this.b.dispense(isourceblock, new ItemStack(item));
                    }
                    // CraftBukkit end
                    return itemstack;
                } else {
                    return this.b.dispense(isourceblock, itemstack);
                }
            }
        };

        BlockDispenser.a((IMaterial) Items.LAVA_BUCKET, (IDispenseBehavior) dispensebehavioritem1);
        BlockDispenser.a((IMaterial) Items.WATER_BUCKET, (IDispenseBehavior) dispensebehavioritem1);
        BlockDispenser.a((IMaterial) Items.SALMON_BUCKET, (IDispenseBehavior) dispensebehavioritem1);
        BlockDispenser.a((IMaterial) Items.COD_BUCKET, (IDispenseBehavior) dispensebehavioritem1);
        BlockDispenser.a((IMaterial) Items.PUFFERFISH_BUCKET, (IDispenseBehavior) dispensebehavioritem1);
        BlockDispenser.a((IMaterial) Items.TROPICAL_FISH_BUCKET, (IDispenseBehavior) dispensebehavioritem1);
        BlockDispenser.a((IMaterial) Items.BUCKET, (IDispenseBehavior) (new DispenseBehaviorItem() {
            private final DispenseBehaviorItem b = new DispenseBehaviorItem();

            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                WorldServer worldserver = isourceblock.getWorld();
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                IBlockData iblockdata = worldserver.getType(blockposition);
                Block block = iblockdata.getBlock();

                if (block instanceof IFluidSource) {
                    FluidType fluidtype = ((IFluidSource) block).removeFluid(DummyGeneratorAccess.INSTANCE, blockposition, iblockdata); // CraftBukkit

                    if (!(fluidtype instanceof FluidTypeFlowing)) {
                        return super.a(isourceblock, itemstack);
                    } else {
                        Item item = fluidtype.a();

                        // CraftBukkit start
                        org.bukkit.block.Block bukkitBlock = worldserver.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
                        CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                        BlockDispenseEvent event = new BlockDispenseEvent(bukkitBlock, craftItem.clone(), new org.bukkit.util.Vector(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
                        if (!BlockDispenser.eventFired) {
                            worldserver.getServer().getPluginManager().callEvent(event);
                        }

                        if (event.isCancelled()) {
                            return itemstack;
                        }

                        if (!event.getItem().equals(craftItem)) {
                            // Chain to handler for new item
                            ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                            IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(eventStack.getItem());
                            if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
                                idispensebehavior.dispense(isourceblock, eventStack);
                                return itemstack;
                            }
                        }

                        fluidtype = ((IFluidSource) block).removeFluid(worldserver, blockposition, iblockdata); // From above
                        // CraftBukkit end

                        itemstack.subtract(1);
                        if (itemstack.isEmpty()) {
                            return new ItemStack(item);
                        } else {
                            if (((TileEntityDispenser) isourceblock.getTileEntity()).addItem(new ItemStack(item)) < 0) {
                                this.b.dispense(isourceblock, new ItemStack(item));
                            }

                            return itemstack;
                        }
                    }
                } else {
                    return super.a(isourceblock, itemstack);
                }
            }
        }));
        BlockDispenser.a((IMaterial) Items.FLINT_AND_STEEL, (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            @Override
            protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                WorldServer worldserver = isourceblock.getWorld();

                // CraftBukkit start
                org.bukkit.block.Block bukkitBlock = worldserver.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                BlockDispenseEvent event = new BlockDispenseEvent(bukkitBlock, craftItem.clone(), new org.bukkit.util.Vector(0, 0, 0));
                if (!BlockDispenser.eventFired) {
                    worldserver.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(eventStack.getItem());
                    if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }
                // CraftBukkit end

                this.a(true);
                EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
                BlockPosition blockposition = isourceblock.getBlockPosition().shift(enumdirection);
                IBlockData iblockdata = worldserver.getType(blockposition);

                if (BlockFireAbstract.a((World) worldserver, blockposition, enumdirection)) {
                    // CraftBukkit start - Ignition by dispensing flint and steel
                    if (!org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(worldserver, blockposition, isourceblock.getBlockPosition()).isCancelled()) {
                        worldserver.setTypeUpdate(blockposition, BlockFireAbstract.a((IBlockAccess) worldserver, blockposition));
                    }
                    // CraftBukkit end
                } else if (BlockCampfire.h(iblockdata)) {
                    worldserver.setTypeUpdate(blockposition, (IBlockData) iblockdata.set(BlockProperties.r, true));
                } else if (iblockdata.getBlock() instanceof BlockTNT) {
                    BlockTNT.a((World) worldserver, blockposition);
                    worldserver.a(blockposition, false);
                } else {
                    this.a(false);
                }

                if (this.a() && itemstack.isDamaged(1, worldserver.random, (EntityPlayer) null)) {
                    itemstack.setCount(0);
                }

                return itemstack;
            }
        }));
        BlockDispenser.a((IMaterial) Items.BONE_MEAL, (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            @Override
            protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                this.a(true);
                WorldServer worldserver = isourceblock.getWorld();
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                // CraftBukkit start
                org.bukkit.block.Block block = worldserver.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(0, 0, 0));
                if (!BlockDispenser.eventFired) {
                    worldserver.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(eventStack.getItem());
                    if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }

                worldserver.captureTreeGeneration = true;
                // CraftBukkit end

                if (!ItemBoneMeal.a(itemstack, (World) worldserver, blockposition) && !ItemBoneMeal.a(itemstack, (World) worldserver, blockposition, (EnumDirection) null)) {
                    this.a(false);
                } else if (!worldserver.isClientSide) {
                    worldserver.triggerEffect(2005, blockposition, 0);
                }
                // CraftBukkit start
                worldserver.captureTreeGeneration = false;
                if (worldserver.capturedBlockStates.size() > 0) {
                    TreeType treeType = BlockSapling.treeType;
                    BlockSapling.treeType = null;
                    Location location = new Location(worldserver.getWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ());
                    List<org.bukkit.block.BlockState> blocks = new java.util.ArrayList<>(worldserver.capturedBlockStates.values());
                    worldserver.capturedBlockStates.clear();
                    StructureGrowEvent structureEvent = null;
                    if (treeType != null) {
                        structureEvent = new StructureGrowEvent(location, treeType, false, null, blocks);
                        org.bukkit.Bukkit.getPluginManager().callEvent(structureEvent);
                    }
                    if (structureEvent == null || !structureEvent.isCancelled()) {
                        for (org.bukkit.block.BlockState blockstate : blocks) {
                            blockstate.update(true);
                        }
                    }
                }
                // CraftBukkit end

                return itemstack;
            }
        }));
        BlockDispenser.a((IMaterial) Blocks.TNT, (IDispenseBehavior) (new DispenseBehaviorItem() {
            @Override
            protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                WorldServer worldserver = isourceblock.getWorld();
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                // EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D, (EntityLiving) null);

                // CraftBukkit start
                ItemStack itemstack1 = itemstack.cloneAndSubtract(1);
                org.bukkit.block.Block block = worldserver.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

                BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector((double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D));
                if (!BlockDispenser.eventFired) {
                   worldserver.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    itemstack.add(1);
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    itemstack.add(1);
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(eventStack.getItem());
                    if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }

                EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldserver, event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), (EntityLiving) null);
                // CraftBukkit end

                worldserver.addEntity(entitytntprimed);
                worldserver.playSound((EntityHuman) null, entitytntprimed.locX(), entitytntprimed.locY(), entitytntprimed.locZ(), SoundEffects.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                // itemstack.subtract(1); // CraftBukkit - handled above
                return itemstack;
            }
        }));
        DispenseBehaviorMaybe dispensebehaviormaybe1 = new DispenseBehaviorMaybe() {
            @Override
            protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                this.a(ItemArmor.a(isourceblock, itemstack));
                return itemstack;
            }
        };

        BlockDispenser.a((IMaterial) Items.CREEPER_HEAD, (IDispenseBehavior) dispensebehaviormaybe1);
        BlockDispenser.a((IMaterial) Items.ZOMBIE_HEAD, (IDispenseBehavior) dispensebehaviormaybe1);
        BlockDispenser.a((IMaterial) Items.DRAGON_HEAD, (IDispenseBehavior) dispensebehaviormaybe1);
        BlockDispenser.a((IMaterial) Items.SKELETON_SKULL, (IDispenseBehavior) dispensebehaviormaybe1);
        BlockDispenser.a((IMaterial) Items.PLAYER_HEAD, (IDispenseBehavior) dispensebehaviormaybe1);
        BlockDispenser.a((IMaterial) Items.WITHER_SKELETON_SKULL, (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            @Override
            protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                WorldServer worldserver = isourceblock.getWorld();
                EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
                BlockPosition blockposition = isourceblock.getBlockPosition().shift(enumdirection);

                // CraftBukkit start
                org.bukkit.block.Block bukkitBlock = worldserver.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                BlockDispenseEvent event = new BlockDispenseEvent(bukkitBlock, craftItem.clone(), new org.bukkit.util.Vector(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
                if (!BlockDispenser.eventFired) {
                    worldserver.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(eventStack.getItem());
                    if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }
                // CraftBukkit end

                if (worldserver.isEmpty(blockposition) && BlockWitherSkull.b((World) worldserver, blockposition, itemstack)) {
                    worldserver.setTypeAndData(blockposition, (IBlockData) Blocks.WITHER_SKELETON_SKULL.getBlockData().set(BlockSkull.a, enumdirection.n() == EnumDirection.EnumAxis.Y ? 0 : enumdirection.opposite().get2DRotationValue() * 4), 3);
                    TileEntity tileentity = worldserver.getTileEntity(blockposition);

                    if (tileentity instanceof TileEntitySkull) {
                        BlockWitherSkull.a((World) worldserver, blockposition, (TileEntitySkull) tileentity);
                    }

                    itemstack.subtract(1);
                    this.a(true);
                } else {
                    this.a(ItemArmor.a(isourceblock, itemstack));
                }

                return itemstack;
            }
        }));
        BlockDispenser.a((IMaterial) Blocks.CARVED_PUMPKIN, (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            @Override
            protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                WorldServer worldserver = isourceblock.getWorld();
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                BlockPumpkinCarved blockpumpkincarved = (BlockPumpkinCarved) Blocks.CARVED_PUMPKIN;

                // CraftBukkit start
                org.bukkit.block.Block bukkitBlock = worldserver.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                BlockDispenseEvent event = new BlockDispenseEvent(bukkitBlock, craftItem.clone(), new org.bukkit.util.Vector(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
                if (!BlockDispenser.eventFired) {
                    worldserver.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(eventStack.getItem());
                    if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }
                // CraftBukkit end

                if (worldserver.isEmpty(blockposition) && blockpumpkincarved.a((IWorldReader) worldserver, blockposition)) {
                    if (!worldserver.isClientSide) {
                        worldserver.setTypeAndData(blockposition, blockpumpkincarved.getBlockData(), 3);
                    }

                    itemstack.subtract(1);
                    this.a(true);
                } else {
                    this.a(ItemArmor.a(isourceblock, itemstack));
                }

                return itemstack;
            }
        }));
        BlockDispenser.a((IMaterial) Blocks.SHULKER_BOX.getItem(), (IDispenseBehavior) (new DispenseBehaviorShulkerBox()));
        EnumColor[] aenumcolor = EnumColor.values();
        int i = aenumcolor.length;

        for (int j = 0; j < i; ++j) {
            EnumColor enumcolor = aenumcolor[j];

            BlockDispenser.a((IMaterial) BlockShulkerBox.a(enumcolor).getItem(), (IDispenseBehavior) (new DispenseBehaviorShulkerBox()));
        }

        BlockDispenser.a((IMaterial) Items.GLASS_BOTTLE.getItem(), (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            private final DispenseBehaviorItem b = new DispenseBehaviorItem();

            private ItemStack a(ISourceBlock isourceblock, ItemStack itemstack, ItemStack itemstack1) {
                itemstack.subtract(1);
                if (itemstack.isEmpty()) {
                    return itemstack1.cloneItemStack();
                } else {
                    if (((TileEntityDispenser) isourceblock.getTileEntity()).addItem(itemstack1.cloneItemStack()) < 0) {
                        this.b.dispense(isourceblock, itemstack1.cloneItemStack());
                    }

                    return itemstack;
                }
            }

            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                this.a(false);
                WorldServer worldserver = isourceblock.getWorld();
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                IBlockData iblockdata = worldserver.getType(blockposition);

                // CraftBukkit start
                org.bukkit.block.Block bukkitBlock = worldserver.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                BlockDispenseEvent event = new BlockDispenseEvent(bukkitBlock, craftItem.clone(), new org.bukkit.util.Vector(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
                if (!BlockDispenser.eventFired) {
                    worldserver.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(eventStack.getItem());
                    if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }
                // CraftBukkit end

                if (iblockdata.a((Tag) TagsBlock.BEEHIVES, (blockbase_blockdata) -> {
                    return blockbase_blockdata.b(BlockBeehive.b);
                }) && (Integer) iblockdata.get(BlockBeehive.b) >= 5) {
                    ((BlockBeehive) iblockdata.getBlock()).a(worldserver, iblockdata, blockposition, (EntityHuman) null, TileEntityBeehive.ReleaseStatus.BEE_RELEASED);
                    this.a(true);
                    return this.a(isourceblock, itemstack, new ItemStack(Items.HONEY_BOTTLE));
                } else if (worldserver.getFluid(blockposition).a((Tag) TagsFluid.WATER)) {
                    this.a(true);
                    return this.a(isourceblock, itemstack, PotionUtil.a(new ItemStack(Items.POTION), Potions.WATER));
                } else {
                    return super.a(isourceblock, itemstack);
                }
            }
        }));
        BlockDispenser.a((IMaterial) Items.dq, (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
                BlockPosition blockposition = isourceblock.getBlockPosition().shift(enumdirection);
                WorldServer worldserver = isourceblock.getWorld();
                IBlockData iblockdata = worldserver.getType(blockposition);

                this.a(true);
                if (iblockdata.a(Blocks.RESPAWN_ANCHOR)) {
                    if ((Integer) iblockdata.get(BlockRespawnAnchor.a) != 4) {
                        BlockRespawnAnchor.a((World) worldserver, blockposition, iblockdata);
                        itemstack.subtract(1);
                    } else {
                        this.a(false);
                    }

                    return itemstack;
                } else {
                    return super.a(isourceblock, itemstack);
                }
            }
        }));
        BlockDispenser.a((IMaterial) Items.SHEARS.getItem(), (IDispenseBehavior) (new DispenseBehaviorShears()));
    }

    static void a(ISourceBlock isourceblock, Entity entity, EnumDirection enumdirection) {
        entity.setPosition(isourceblock.getX() + (double) enumdirection.getAdjacentX() * (0.5000099999997474D - (double) entity.getWidth() / 2.0D), isourceblock.getY() + (double) enumdirection.getAdjacentY() * (0.5000099999997474D - (double) entity.getHeight() / 2.0D) - (double) entity.getHeight() / 2.0D, isourceblock.getZ() + (double) enumdirection.getAdjacentZ() * (0.5000099999997474D - (double) entity.getWidth() / 2.0D));
    }
}
