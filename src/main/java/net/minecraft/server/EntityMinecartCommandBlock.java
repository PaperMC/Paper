package net.minecraft.server;

public class EntityMinecartCommandBlock extends EntityMinecartAbstract {

    public static final DataWatcherObject<String> COMMAND = DataWatcher.a(EntityMinecartCommandBlock.class, DataWatcherRegistry.d);
    private static final DataWatcherObject<IChatBaseComponent> c = DataWatcher.a(EntityMinecartCommandBlock.class, DataWatcherRegistry.e);
    private final CommandBlockListenerAbstract d = new EntityMinecartCommandBlock.a();
    private int e;

    public EntityMinecartCommandBlock(EntityTypes<? extends EntityMinecartCommandBlock> entitytypes, World world) {
        super(entitytypes, world);
    }

    public EntityMinecartCommandBlock(World world, double d0, double d1, double d2) {
        super(EntityTypes.COMMAND_BLOCK_MINECART, world, d0, d1, d2);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.getDataWatcher().register(EntityMinecartCommandBlock.COMMAND, "");
        this.getDataWatcher().register(EntityMinecartCommandBlock.c, ChatComponentText.d);
    }

    @Override
    protected void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.d.b(nbttagcompound);
        this.getDataWatcher().set(EntityMinecartCommandBlock.COMMAND, this.getCommandBlock().getCommand());
        this.getDataWatcher().set(EntityMinecartCommandBlock.c, this.getCommandBlock().j());
    }

    @Override
    protected void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        this.d.a(nbttagcompound);
    }

    @Override
    public EntityMinecartAbstract.EnumMinecartType getMinecartType() {
        return EntityMinecartAbstract.EnumMinecartType.COMMAND_BLOCK;
    }

    @Override
    public IBlockData q() {
        return Blocks.COMMAND_BLOCK.getBlockData();
    }

    public CommandBlockListenerAbstract getCommandBlock() {
        return this.d;
    }

    @Override
    public void a(int i, int j, int k, boolean flag) {
        if (flag && this.ticksLived - this.e >= 4) {
            this.getCommandBlock().a(this.world);
            this.e = this.ticksLived;
        }

    }

    @Override
    public EnumInteractionResult a(EntityHuman entityhuman, EnumHand enumhand) {
        return this.d.a(entityhuman);
    }

    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        super.a(datawatcherobject);
        if (EntityMinecartCommandBlock.c.equals(datawatcherobject)) {
            try {
                this.d.b((IChatBaseComponent) this.getDataWatcher().get(EntityMinecartCommandBlock.c));
            } catch (Throwable throwable) {
                ;
            }
        } else if (EntityMinecartCommandBlock.COMMAND.equals(datawatcherobject)) {
            this.d.setCommand((String) this.getDataWatcher().get(EntityMinecartCommandBlock.COMMAND));
        }

    }

    @Override
    public boolean ci() {
        return true;
    }

    public class a extends CommandBlockListenerAbstract {

        public a() {}

        @Override
        public WorldServer d() {
            return (WorldServer) EntityMinecartCommandBlock.this.world;
        }

        @Override
        public void e() {
            EntityMinecartCommandBlock.this.getDataWatcher().set(EntityMinecartCommandBlock.COMMAND, this.getCommand());
            EntityMinecartCommandBlock.this.getDataWatcher().set(EntityMinecartCommandBlock.c, this.j());
        }

        @Override
        public CommandListenerWrapper getWrapper() {
            return new CommandListenerWrapper(this, EntityMinecartCommandBlock.this.getPositionVector(), EntityMinecartCommandBlock.this.bh(), this.d(), 2, this.getName().getString(), EntityMinecartCommandBlock.this.getScoreboardDisplayName(), this.d().getMinecraftServer(), EntityMinecartCommandBlock.this);
        }
    }
}
