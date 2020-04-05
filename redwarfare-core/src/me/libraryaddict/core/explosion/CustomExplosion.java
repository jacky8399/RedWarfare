package me.libraryaddict.core.explosion;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import me.libraryaddict.core.damage.AttackType;
import me.libraryaddict.core.damage.CustomDamageEvent;
import me.libraryaddict.core.damage.DamageManager;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.ObjectListIterator;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Damageable;
import org.bukkit.util.Vector;

import java.util.*;

public class CustomExplosion extends Explosion {
    private AttackType _attackType;
    private float _blockExplosionSize;
    private boolean _createFire;
    private float _damage;
    private boolean _damageBlocks = true;
    private boolean _damageBlocksEqually;
    private boolean _dropItems = true;
    private boolean _ignoreNonLiving;
    private boolean _ignoreRate = true;
    private float _maxDamage = 1000;
    private org.bukkit.entity.LivingEntity _owner;
    private AttackType _selfAttackType;
    private float _size;
    private boolean _useCustomDamage;
    private World _world;
    private double posX, posY, posZ;

    public CustomExplosion(Location loc, float explosionSize, AttackType attackType) {
        super(((CraftWorld) loc.getWorld()).getHandle(), null, loc.getX(), loc.getY(), loc.getZ(), explosionSize, false, Effect.NONE);

        posX = loc.getX();
        posY = loc.getY();
        posZ = loc.getZ();

        _world = ((CraftWorld) loc.getWorld()).getHandle();
        _blockExplosionSize = _size = explosionSize;
        _attackType = attackType;
        _selfAttackType = attackType;
    }

    public CustomExplosion(Location loc, float explosionSize, AttackType attackType, AttackType selfAttackType) {
        this(loc, explosionSize, attackType);

        _selfAttackType = selfAttackType;
    }

    @Override
    public void a() {
        /* if (Math.max(_blockExplosionSize, _size) < 0.1F) {
            return;
        } */

        if (_size >= 0.1F) {
            Set<BlockPosition> set = Sets.newHashSet();
            boolean flag = true;

            int i;
            int j;
            for(int k = 0; k < 16; ++k) {
                for(i = 0; i < 16; ++i) {
                    for(j = 0; j < 16; ++j) {
                        if (k == 0 || k == 15 || i == 0 || i == 15 || j == 0 || j == 15) {
                            double d0 = (double)((float)k / 15.0F * 2.0F - 1.0F);
                            double d1 = (double)((float)i / 15.0F * 2.0F - 1.0F);
                            double d2 = (double)((float)j / 15.0F * 2.0F - 1.0F);
                            double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                            d0 /= d3;
                            d1 /= d3;
                            d2 /= d3;
                            float f = _size * (0.7F + _world.random.nextFloat() * 0.6F);
                            double d4 = this.posX;
                            double d5 = this.posY;
                            double d6 = this.posZ;

                            for(float var21 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                                BlockPosition blockposition = new BlockPosition(d4, d5, d6);
                                IBlockData iblockdata = _world.getType(blockposition);
                                Fluid fluid = _world.getFluid(blockposition);
                                if (!iblockdata.isAir() || !fluid.isEmpty()) {
                                    float f2 = Math.max(
                                            _damageBlocksEqually ? Blocks.DIRT.getDurability() : iblockdata.getBlock().getDurability(),
                                            fluid.k());
                                    if (this.source != null) {
                                        f2 = this.source.a(this, _world, blockposition, iblockdata, fluid, f2);
                                    }

                                    f -= (f2 + 0.3F) * 0.3F;
                                }

                                if (f > 0.0F && (this.source == null || this.source.a(this, _world, blockposition, iblockdata, f)) && blockposition.getY() < 256 && blockposition.getY() >= 0) {
                                    set.add(blockposition);
                                }

                                d4 += d0 * 0.30000001192092896D;
                                d5 += d1 * 0.30000001192092896D;
                                d6 += d2 * 0.30000001192092896D;
                            }
                        }
                    }
                }
            }

            this.getBlocks().addAll(set);
            float f3 = _size * 2.0F;
            i = MathHelper.floor(this.posX - (double)f3 - 1.0D);
            j = MathHelper.floor(this.posX + (double)f3 + 1.0D);
            int l = MathHelper.floor(this.posY - (double)f3 - 1.0D);
            int i1 = MathHelper.floor(this.posY + (double)f3 + 1.0D);
            int j1 = MathHelper.floor(this.posZ - (double)f3 - 1.0D);
            int k1 = MathHelper.floor(this.posZ + (double)f3 + 1.0D);
            List<Entity> list = _world.getEntities(this.source, new AxisAlignedBB((double)i, (double)l, (double)j1, (double)j, (double)i1, (double)k1));
            Vec3D vec3d = new Vec3D(this.posX, this.posY, this.posZ);

            for(int l1 = 0; l1 < list.size(); ++l1) {
                Entity entity = (Entity)list.get(l1);
                if (!entity.ca()) {
                    double d7 = (double)(MathHelper.sqrt(entity.c(vec3d)) / f3);
                    if (d7 <= 1.0D) {
                        double d8 = entity.locX() - this.posX;
                        double d9 = entity.getHeadY() - this.posY;
                        double d10 = entity.locZ() - this.posZ;
                        double d11 = (double)MathHelper.sqrt(d8 * d8 + d9 * d9 + d10 * d10);
                        if (d11 != 0.0D) {
                            d8 /= d11;
                            d9 /= d11;
                            d10 /= d11;


                            double d12 = (double)a(vec3d, entity);
                            double d13 = (1.0D - d7) * d12;

                            /*
                            CraftEventFactory.entityDamage = this.source;
                            entity.forceExplosionKnockback = false;
                            boolean wasDamaged = entity.damageEntity(this.b(), (float)((int)((d13 * d13 + d13) / 2.0D * 7.0D * (double)f3 + 1.0D)));
                            CraftEventFactory.entityDamage = null;
                            if (wasDamaged || entity instanceof EntityTNTPrimed || entity instanceof EntityFallingBlock || entity.forceExplosionKnockback) {
                                double d14 = d13;
                                if (entity instanceof EntityLiving) {
                                    d14 = EnchantmentProtection.a((EntityLiving)entity, d13);
                                }

                                entity.setMot(entity.getMot().add(d8 * d14, d9 * d14, d10 * d14));
                                if (entity instanceof EntityHuman) {
                                    EntityHuman entityhuman = (EntityHuman)entity;
                                    if (!entityhuman.isSpectator() && (!entityhuman.isCreative() || !entityhuman.abilities.isFlying)) {
                                        this.c().put(entityhuman, new Vec3D(d8 * d13, d9 * d13, d10 * d13));
                                    }
                                }
                            }*/

                            float damage;

                            if (_useCustomDamage) {
                                damage = Math.max(0, (int) ((_damage * d9) * (d8 / _size)));
                            } else {
                                damage = (int) ((d10 * d10 + d10) / 2.0D * 8.0D * this._size + 1.0D);
                                damage = Math.min(damage, _maxDamage);
                            }

                            if (entity.getBukkitEntity() instanceof Damageable) {

                                DamageManager manager = ExplosionManager.explosionManager.getDamageManager();

                                CustomDamageEvent event = manager.createEvent(entity.getBukkitEntity(),
                                        entity.getBukkitEntity() == _owner ? _selfAttackType : _attackType, damage, _owner);

                                event.setIgnoreRate(_ignoreRate);

                                Vector vec = new Vector(d8 * d13, d9 * d13, d10 * d13);

                                event.setKnockback(vec);
                                manager.callDamage(event);
                            } else {
                                CraftEventFactory.entityDamage = this.source;
                                entity.damageEntity(DamageSource.explosion(this), damage);
                                CraftEventFactory.entityDamage = null;
                            }

                            if (((entity instanceof EntityHuman)) && (!((EntityHuman) entity).abilities.isInvulnerable)) {
                                this.c().put((EntityHuman) entity, new Vec3D(d8 * d13, d9 * d13, d10 * d13)); //XXX
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public void a(boolean flag) {
        Location loc = new Location(_world.getWorld(), posX, posY, posZ);
        if (_world.isClientSide) {
            _world.a(this.posX, this.posY, this.posZ, SoundEffects.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (_world.random.nextFloat() - _world.random.nextFloat()) * 0.2F) * 0.7F, false);
        }

        if (flag) {
            if (_blockExplosionSize >= 2.0F && _damageBlocks) {
                _world.addParticle(Particles.EXPLOSION_EMITTER, this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D);
            } else {
                _world.addParticle(Particles.EXPLOSION, this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D);
            }
        }

        if (_damageBlocks) {
            ObjectArrayList<Pair<ItemStack, BlockPosition>> objectarraylist = new ObjectArrayList();
            Collections.shuffle(this.getBlocks(), _world.random);
            Iterator iterator;
            org.bukkit.World bworld = _world.getWorld();
            org.bukkit.entity.Entity explode = this.source == null ? null : this.source.getBukkitEntity();
            Location location = new Location(bworld, this.posX, this.posY, this.posZ);
            ArrayList<org.bukkit.block.Block> blockList = new ArrayList<>();

            for(int i1 = this.getBlocks().size() - 1; i1 >= 0; --i1) {
                BlockPosition cpos = this.getBlocks().get(i1);
                org.bukkit.block.Block bblock = bworld.getBlockAt(cpos.getX(), cpos.getY(), cpos.getZ());
                if (!bblock.getType().isAir()) {
                    blockList.add(bblock);
                }
            }

            ExplosionEvent event = new ExplosionEvent(this, blockList);

            this._world.getServer().getPluginManager().callEvent(event);

            this.getBlocks().clear();

            for (org.bukkit.block.Block bblock : event.getBlocks()) {
                BlockPosition coords = new BlockPosition(bblock.getX(), bblock.getY(), bblock.getZ());
                this.getBlocks().add(coords);
            }

            if (getBlocks().isEmpty()) {
                this.wasCanceled = true;
                return;
            }

            /*
            boolean cancelled;
            List bukkitBlocks;
            float yield;
            if (explode != null) {
                EntityExplodeEvent event = new EntityExplodeEvent(explode, location, blockList, this.b == Explosion.Effect.DESTROY ? 1.0F / this.size : 1.0F);
                _world.getServer().getPluginManager().callEvent(event);
                cancelled = event.isCancelled();
                bukkitBlocks = event.blockList();
                yield = event.getYield();
            } else {
                BlockExplodeEvent event = new BlockExplodeEvent(location.getBlock(), blockList, this.b == Explosion.Effect.DESTROY ? 1.0F / this.size : 1.0F);
                _world.getServer().getPluginManager().callEvent(event);
                cancelled = event.isCancelled();
                bukkitBlocks = event.blockList();
                yield = event.getYield();
            }

            this.blocks.clear();
            Iterator var13 = bukkitBlocks.iterator();

            while(var13.hasNext()) {
                org.bukkit.block.Block bblock = (org.bukkit.block.Block)var13.next();
                BlockPosition coords = new BlockPosition(bblock.getX(), bblock.getY(), bblock.getZ());
                this.blocks.add(coords);
            }

            if (cancelled) {
                this.wasCanceled = true;
                return;
            }*/

            iterator = this.getBlocks().iterator();

            label111:
            while(true) {
                BlockPosition blockposition;
                IBlockData iblockdata;
                net.minecraft.server.v1_15_R1.Block block;
                do {
                    if (!iterator.hasNext()) {
                        ObjectListIterator objectlistiterator = objectarraylist.iterator();

                        while(objectlistiterator.hasNext()) {
                            Pair<ItemStack, BlockPosition> pair = (Pair)objectlistiterator.next();
                            net.minecraft.server.v1_15_R1.Block.a(_world, (BlockPosition)pair.getSecond(), (ItemStack)pair.getFirst());
                        }
                        break label111;
                    }

                    blockposition = (BlockPosition)iterator.next();
                    iblockdata = _world.getType(blockposition);
                    block = iblockdata.getBlock();
                } while(iblockdata.isAir());

                //BlockPosition blockposition1 = blockposition.immutableCopy();
                _world.getMethodProfiler().enter("explosion_blocks");
                if (block.a(this) && _world instanceof WorldServer) {
                    TileEntity tileentity = block.isTileEntity() ? _world.getTileEntity(blockposition) : null;
                    LootTableInfo.Builder loottableinfo_builder = (new LootTableInfo.Builder((WorldServer)_world)).a(_world.random).set(LootContextParameters.POSITION, blockposition).set(LootContextParameters.TOOL, ItemStack.a).setOptional(LootContextParameters.BLOCK_ENTITY, tileentity).setOptional(LootContextParameters.THIS_ENTITY, this.source);
                    if (_damageBlocks && _dropItems) {
                        loottableinfo_builder.set(LootContextParameters.EXPLOSION_RADIUS, 1.0F);
                    }

                    iblockdata.a(loottableinfo_builder).forEach((itemstack) -> bworld.dropItemNaturally(location, CraftItemStack.asBukkitCopy(itemstack)));
                }

                _world.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 3);
                block.wasExploded(_world, blockposition, this);
                _world.getMethodProfiler().exit();
            }
        }

        if (_createFire) {
            Iterator iterator1 = this.getBlocks().iterator();

            while(iterator1.hasNext()) {
                BlockPosition blockposition2 = (BlockPosition)iterator1.next();
                if (_world.random.nextBoolean() && _world.getType(blockposition2).isAir() && _world.getType(blockposition2.down()).g(_world, blockposition2.down()) && !CraftEventFactory.callBlockIgniteEvent(_world, blockposition2.getX(), blockposition2.getY(), blockposition2.getZ(), this).isCancelled()) {
                    _world.setTypeUpdate(blockposition2, Blocks.FIRE.getBlockData());
                }
            }
        }

    }

    public CustomExplosion explode() {
        /*new BukkitRunnable()
        {
        	long started = System.currentTimeMillis();

        	public void run()
        	{
        		if (UtilTime.elasped(started, 20000))
        		{
        			this.cancel();
        		}

        		UtilParticle.playParticle(ParticleType.FLAME, new Location(_world.getWorld(), posX, posY, posZ));
        	}
        }.runTaskTimer(Bukkit.getPluginManager().getPlugins()[0], 0, 5);*/
        // Explode
        a();
        a(true);

        return this;
    }

    public float getSize() {
        return _size;
    }

    public void setAttackType(AttackType attackType) {
        _attackType = attackType;
    }

    public CustomExplosion setBlockExplosionSize(float explosionSize) {
        _blockExplosionSize = explosionSize;

        return this;
    }

    public CustomExplosion setBlocksDamagedEqually(boolean damageEqually) {
        _damageBlocksEqually = damageEqually;

        return this;
    }

    public CustomExplosion setDamageBlocks(boolean damageBlocks) {
        _damageBlocks = damageBlocks;

        return this;
    }

    public CustomExplosion setDamager(org.bukkit.entity.Player player) {
        _owner = player;

        return this;
    }

    public CustomExplosion setDropItems(boolean dropItems) {
        _dropItems = dropItems;

        return this;
    }

    /**
     * Center of explosion does this much damage
     */
    public CustomExplosion setExplosionDamage(float damage) {
        _damage = damage;
        _useCustomDamage = true;

        return this;
    }

    public CustomExplosion setIgnoreNonLiving(boolean ignoreNonLiving) {
        _ignoreNonLiving = ignoreNonLiving;

        return this;
    }

    public CustomExplosion setIgnoreRate(boolean ignoreRate) {
        _ignoreRate = ignoreRate;

        return this;
    }

    public void setIncinderary(boolean fire) {
        _createFire = fire;
    }

    public CustomExplosion setMaxDamage(float maxDamage) {
        _maxDamage = maxDamage;

        return this;
    }
}