package net.minecraft.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DataResult.PartialResult;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import io.netty.util.ByteProcessor;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import javax.annotation.Nullable;

import org.bukkit.craftbukkit.inventory.CraftItemStack; // CraftBukkit

public class PacketDataSerializer extends ByteBuf {

    private final ByteBuf a;

    public PacketDataSerializer(ByteBuf bytebuf) {
        this.a = bytebuf;
    }

    public static int a(int i) {
        for (int j = 1; j < 5; ++j) {
            if ((i & -1 << j * 7) == 0) {
                return j;
            }
        }

        return 5;
    }

    public <T> T a(Codec<T> codec) throws IOException {
        NBTTagCompound nbttagcompound = this.m();
        DataResult<T> dataresult = codec.parse(DynamicOpsNBT.a, nbttagcompound);

        if (dataresult.error().isPresent()) {
            throw new IOException("Failed to decode: " + ((PartialResult) dataresult.error().get()).message() + " " + nbttagcompound);
        } else {
            return dataresult.result().get();
        }
    }

    public <T> void a(Codec<T> codec, T t0) throws IOException {
        DataResult<NBTBase> dataresult = codec.encodeStart(DynamicOpsNBT.a, t0);

        if (dataresult.error().isPresent()) {
            throw new IOException("Failed to encode: " + ((PartialResult) dataresult.error().get()).message() + " " + t0);
        } else {
            this.a((NBTTagCompound) dataresult.result().get());
        }
    }

    public PacketDataSerializer a(byte[] abyte) {
        this.d(abyte.length);
        this.writeBytes(abyte);
        return this;
    }

    public byte[] a() {
        return this.b(this.readableBytes());
    }

    public byte[] b(int i) {
        int j = this.i();

        if (j > i) {
            throw new DecoderException("ByteArray with size " + j + " is bigger than allowed " + i);
        } else {
            byte[] abyte = new byte[j];

            this.readBytes(abyte);
            return abyte;
        }
    }

    public PacketDataSerializer a(int[] aint) {
        this.d(aint.length);
        int[] aint1 = aint;
        int i = aint.length;

        for (int j = 0; j < i; ++j) {
            int k = aint1[j];

            this.d(k);
        }

        return this;
    }

    public int[] b() {
        return this.c(this.readableBytes());
    }

    public int[] c(int i) {
        int j = this.i();

        if (j > i) {
            throw new DecoderException("VarIntArray with size " + j + " is bigger than allowed " + i);
        } else {
            int[] aint = new int[j];

            for (int k = 0; k < aint.length; ++k) {
                aint[k] = this.i();
            }

            return aint;
        }
    }

    public PacketDataSerializer a(long[] along) {
        this.d(along.length);
        long[] along1 = along;
        int i = along.length;

        for (int j = 0; j < i; ++j) {
            long k = along1[j];

            this.writeLong(k);
        }

        return this;
    }

    public BlockPosition e() {
        return BlockPosition.fromLong(this.readLong());
    }

    public PacketDataSerializer a(BlockPosition blockposition) {
        this.writeLong(blockposition.asLong());
        return this;
    }

    public IChatBaseComponent h() {
        return IChatBaseComponent.ChatSerializer.a(this.e(262144));
    }

    public PacketDataSerializer a(IChatBaseComponent ichatbasecomponent) {
        return this.a(IChatBaseComponent.ChatSerializer.a(ichatbasecomponent), 262144);
    }

    public <T extends Enum<T>> T a(Class<T> oclass) {
        return ((T[]) oclass.getEnumConstants())[this.i()]; // CraftBukkit - fix decompile error
    }

    public PacketDataSerializer a(Enum<?> oenum) {
        return this.d(oenum.ordinal());
    }

    public int i() {
        int i = 0;
        int j = 0;

        byte b0;

        do {
            b0 = this.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }

    public long j() {
        long i = 0L;
        int j = 0;

        byte b0;

        do {
            b0 = this.readByte();
            i |= (long) (b0 & 127) << j++ * 7;
            if (j > 10) {
                throw new RuntimeException("VarLong too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }

    public PacketDataSerializer a(UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
        return this;
    }

    public UUID k() {
        return new UUID(this.readLong(), this.readLong());
    }

    public PacketDataSerializer d(int i) {
        while ((i & -128) != 0) {
            this.writeByte(i & 127 | 128);
            i >>>= 7;
        }

        this.writeByte(i);
        return this;
    }

    public PacketDataSerializer b(long i) {
        while ((i & -128L) != 0L) {
            this.writeByte((int) (i & 127L) | 128);
            i >>>= 7;
        }

        this.writeByte((int) i);
        return this;
    }

    public PacketDataSerializer a(@Nullable NBTTagCompound nbttagcompound) {
        if (nbttagcompound == null) {
            this.writeByte(0);
        } else {
            try {
                NBTCompressedStreamTools.a(nbttagcompound, (DataOutput) (new ByteBufOutputStream(this)));
            } catch (Exception ioexception) { // CraftBukkit - IOException -> Exception
                throw new EncoderException(ioexception);
            }
        }

        return this;
    }

    @Nullable
    public NBTTagCompound l() {
        return this.a(new NBTReadLimiter(2097152L));
    }

    @Nullable
    public NBTTagCompound m() {
        return this.a(NBTReadLimiter.a);
    }

    @Nullable
    public NBTTagCompound a(NBTReadLimiter nbtreadlimiter) {
        int i = this.readerIndex();
        byte b0 = this.readByte();

        if (b0 == 0) {
            return null;
        } else {
            this.readerIndex(i);

            try {
                return NBTCompressedStreamTools.a((DataInput) (new ByteBufInputStream(this)), nbtreadlimiter);
            } catch (IOException ioexception) {
                throw new EncoderException(ioexception);
            }
        }
    }

    public PacketDataSerializer a(ItemStack itemstack) {
        if (itemstack.isEmpty() || itemstack.getItem() == null) { // CraftBukkit - NPE fix itemstack.getItem()
            this.writeBoolean(false);
        } else {
            this.writeBoolean(true);
            Item item = itemstack.getItem();

            this.d(Item.getId(item));
            this.writeByte(itemstack.getCount());
            NBTTagCompound nbttagcompound = null;

            if (item.usesDurability() || item.n()) {
                nbttagcompound = itemstack.getTag();
            }

            this.a(nbttagcompound);
        }

        return this;
    }

    public ItemStack n() {
        if (!this.readBoolean()) {
            return ItemStack.b;
        } else {
            int i = this.i();
            byte b0 = this.readByte();
            ItemStack itemstack = new ItemStack(Item.getById(i), b0);

            itemstack.setTag(this.l());
            // CraftBukkit start
            if (itemstack.getTag() != null) {
                CraftItemStack.setItemMeta(itemstack, CraftItemStack.getItemMeta(itemstack));
            }
            // CraftBukkit end
            return itemstack;
        }
    }

    public String e(int i) {
        int j = this.i();

        if (j > i * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + i * 4 + ")");
        } else if (j < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            String s = this.toString(this.readerIndex(), j, StandardCharsets.UTF_8);

            this.readerIndex(this.readerIndex() + j);
            if (s.length() > i) {
                throw new DecoderException("The received string length is longer than maximum allowed (" + j + " > " + i + ")");
            } else {
                return s;
            }
        }
    }

    public PacketDataSerializer a(String s) {
        return this.a(s, 32767);
    }

    public PacketDataSerializer a(String s, int i) {
        byte[] abyte = s.getBytes(StandardCharsets.UTF_8);

        if (abyte.length > i) {
            throw new EncoderException("String too big (was " + abyte.length + " bytes encoded, max " + i + ")");
        } else {
            this.d(abyte.length);
            this.writeBytes(abyte);
            return this;
        }
    }

    public MinecraftKey p() {
        return new MinecraftKey(this.e(32767));
    }

    public PacketDataSerializer a(MinecraftKey minecraftkey) {
        this.a(minecraftkey.toString());
        return this;
    }

    public Date q() {
        return new Date(this.readLong());
    }

    public PacketDataSerializer a(Date date) {
        this.writeLong(date.getTime());
        return this;
    }

    public MovingObjectPositionBlock r() {
        BlockPosition blockposition = this.e();
        EnumDirection enumdirection = (EnumDirection) this.a(EnumDirection.class);
        float f = this.readFloat();
        float f1 = this.readFloat();
        float f2 = this.readFloat();
        boolean flag = this.readBoolean();

        return new MovingObjectPositionBlock(new Vec3D((double) blockposition.getX() + (double) f, (double) blockposition.getY() + (double) f1, (double) blockposition.getZ() + (double) f2), enumdirection, blockposition, flag);
    }

    public void a(MovingObjectPositionBlock movingobjectpositionblock) {
        BlockPosition blockposition = movingobjectpositionblock.getBlockPosition();

        this.a(blockposition);
        this.a((Enum) movingobjectpositionblock.getDirection());
        Vec3D vec3d = movingobjectpositionblock.getPos();

        this.writeFloat((float) (vec3d.x - (double) blockposition.getX()));
        this.writeFloat((float) (vec3d.y - (double) blockposition.getY()));
        this.writeFloat((float) (vec3d.z - (double) blockposition.getZ()));
        this.writeBoolean(movingobjectpositionblock.d());
    }

    public int capacity() {
        return this.a.capacity();
    }

    public ByteBuf capacity(int i) {
        return this.a.capacity(i);
    }

    public int maxCapacity() {
        return this.a.maxCapacity();
    }

    public ByteBufAllocator alloc() {
        return this.a.alloc();
    }

    public ByteOrder order() {
        return this.a.order();
    }

    public ByteBuf order(ByteOrder byteorder) {
        return this.a.order(byteorder);
    }

    public ByteBuf unwrap() {
        return this.a.unwrap();
    }

    public boolean isDirect() {
        return this.a.isDirect();
    }

    public boolean isReadOnly() {
        return this.a.isReadOnly();
    }

    public ByteBuf asReadOnly() {
        return this.a.asReadOnly();
    }

    public int readerIndex() {
        return this.a.readerIndex();
    }

    public ByteBuf readerIndex(int i) {
        return this.a.readerIndex(i);
    }

    public int writerIndex() {
        return this.a.writerIndex();
    }

    public ByteBuf writerIndex(int i) {
        return this.a.writerIndex(i);
    }

    public ByteBuf setIndex(int i, int j) {
        return this.a.setIndex(i, j);
    }

    public int readableBytes() {
        return this.a.readableBytes();
    }

    public int writableBytes() {
        return this.a.writableBytes();
    }

    public int maxWritableBytes() {
        return this.a.maxWritableBytes();
    }

    public boolean isReadable() {
        return this.a.isReadable();
    }

    public boolean isReadable(int i) {
        return this.a.isReadable(i);
    }

    public boolean isWritable() {
        return this.a.isWritable();
    }

    public boolean isWritable(int i) {
        return this.a.isWritable(i);
    }

    public ByteBuf clear() {
        return this.a.clear();
    }

    public ByteBuf markReaderIndex() {
        return this.a.markReaderIndex();
    }

    public ByteBuf resetReaderIndex() {
        return this.a.resetReaderIndex();
    }

    public ByteBuf markWriterIndex() {
        return this.a.markWriterIndex();
    }

    public ByteBuf resetWriterIndex() {
        return this.a.resetWriterIndex();
    }

    public ByteBuf discardReadBytes() {
        return this.a.discardReadBytes();
    }

    public ByteBuf discardSomeReadBytes() {
        return this.a.discardSomeReadBytes();
    }

    public ByteBuf ensureWritable(int i) {
        return this.a.ensureWritable(i);
    }

    public int ensureWritable(int i, boolean flag) {
        return this.a.ensureWritable(i, flag);
    }

    public boolean getBoolean(int i) {
        return this.a.getBoolean(i);
    }

    public byte getByte(int i) {
        return this.a.getByte(i);
    }

    public short getUnsignedByte(int i) {
        return this.a.getUnsignedByte(i);
    }

    public short getShort(int i) {
        return this.a.getShort(i);
    }

    public short getShortLE(int i) {
        return this.a.getShortLE(i);
    }

    public int getUnsignedShort(int i) {
        return this.a.getUnsignedShort(i);
    }

    public int getUnsignedShortLE(int i) {
        return this.a.getUnsignedShortLE(i);
    }

    public int getMedium(int i) {
        return this.a.getMedium(i);
    }

    public int getMediumLE(int i) {
        return this.a.getMediumLE(i);
    }

    public int getUnsignedMedium(int i) {
        return this.a.getUnsignedMedium(i);
    }

    public int getUnsignedMediumLE(int i) {
        return this.a.getUnsignedMediumLE(i);
    }

    public int getInt(int i) {
        return this.a.getInt(i);
    }

    public int getIntLE(int i) {
        return this.a.getIntLE(i);
    }

    public long getUnsignedInt(int i) {
        return this.a.getUnsignedInt(i);
    }

    public long getUnsignedIntLE(int i) {
        return this.a.getUnsignedIntLE(i);
    }

    public long getLong(int i) {
        return this.a.getLong(i);
    }

    public long getLongLE(int i) {
        return this.a.getLongLE(i);
    }

    public char getChar(int i) {
        return this.a.getChar(i);
    }

    public float getFloat(int i) {
        return this.a.getFloat(i);
    }

    public double getDouble(int i) {
        return this.a.getDouble(i);
    }

    public ByteBuf getBytes(int i, ByteBuf bytebuf) {
        return this.a.getBytes(i, bytebuf);
    }

    public ByteBuf getBytes(int i, ByteBuf bytebuf, int j) {
        return this.a.getBytes(i, bytebuf, j);
    }

    public ByteBuf getBytes(int i, ByteBuf bytebuf, int j, int k) {
        return this.a.getBytes(i, bytebuf, j, k);
    }

    public ByteBuf getBytes(int i, byte[] abyte) {
        return this.a.getBytes(i, abyte);
    }

    public ByteBuf getBytes(int i, byte[] abyte, int j, int k) {
        return this.a.getBytes(i, abyte, j, k);
    }

    public ByteBuf getBytes(int i, ByteBuffer bytebuffer) {
        return this.a.getBytes(i, bytebuffer);
    }

    public ByteBuf getBytes(int i, OutputStream outputstream, int j) throws IOException {
        return this.a.getBytes(i, outputstream, j);
    }

    public int getBytes(int i, GatheringByteChannel gatheringbytechannel, int j) throws IOException {
        return this.a.getBytes(i, gatheringbytechannel, j);
    }

    public int getBytes(int i, FileChannel filechannel, long j, int k) throws IOException {
        return this.a.getBytes(i, filechannel, j, k);
    }

    public CharSequence getCharSequence(int i, int j, Charset charset) {
        return this.a.getCharSequence(i, j, charset);
    }

    public ByteBuf setBoolean(int i, boolean flag) {
        return this.a.setBoolean(i, flag);
    }

    public ByteBuf setByte(int i, int j) {
        return this.a.setByte(i, j);
    }

    public ByteBuf setShort(int i, int j) {
        return this.a.setShort(i, j);
    }

    public ByteBuf setShortLE(int i, int j) {
        return this.a.setShortLE(i, j);
    }

    public ByteBuf setMedium(int i, int j) {
        return this.a.setMedium(i, j);
    }

    public ByteBuf setMediumLE(int i, int j) {
        return this.a.setMediumLE(i, j);
    }

    public ByteBuf setInt(int i, int j) {
        return this.a.setInt(i, j);
    }

    public ByteBuf setIntLE(int i, int j) {
        return this.a.setIntLE(i, j);
    }

    public ByteBuf setLong(int i, long j) {
        return this.a.setLong(i, j);
    }

    public ByteBuf setLongLE(int i, long j) {
        return this.a.setLongLE(i, j);
    }

    public ByteBuf setChar(int i, int j) {
        return this.a.setChar(i, j);
    }

    public ByteBuf setFloat(int i, float f) {
        return this.a.setFloat(i, f);
    }

    public ByteBuf setDouble(int i, double d0) {
        return this.a.setDouble(i, d0);
    }

    public ByteBuf setBytes(int i, ByteBuf bytebuf) {
        return this.a.setBytes(i, bytebuf);
    }

    public ByteBuf setBytes(int i, ByteBuf bytebuf, int j) {
        return this.a.setBytes(i, bytebuf, j);
    }

    public ByteBuf setBytes(int i, ByteBuf bytebuf, int j, int k) {
        return this.a.setBytes(i, bytebuf, j, k);
    }

    public ByteBuf setBytes(int i, byte[] abyte) {
        return this.a.setBytes(i, abyte);
    }

    public ByteBuf setBytes(int i, byte[] abyte, int j, int k) {
        return this.a.setBytes(i, abyte, j, k);
    }

    public ByteBuf setBytes(int i, ByteBuffer bytebuffer) {
        return this.a.setBytes(i, bytebuffer);
    }

    public int setBytes(int i, InputStream inputstream, int j) throws IOException {
        return this.a.setBytes(i, inputstream, j);
    }

    public int setBytes(int i, ScatteringByteChannel scatteringbytechannel, int j) throws IOException {
        return this.a.setBytes(i, scatteringbytechannel, j);
    }

    public int setBytes(int i, FileChannel filechannel, long j, int k) throws IOException {
        return this.a.setBytes(i, filechannel, j, k);
    }

    public ByteBuf setZero(int i, int j) {
        return this.a.setZero(i, j);
    }

    public int setCharSequence(int i, CharSequence charsequence, Charset charset) {
        return this.a.setCharSequence(i, charsequence, charset);
    }

    public boolean readBoolean() {
        return this.a.readBoolean();
    }

    public byte readByte() {
        return this.a.readByte();
    }

    public short readUnsignedByte() {
        return this.a.readUnsignedByte();
    }

    public short readShort() {
        return this.a.readShort();
    }

    public short readShortLE() {
        return this.a.readShortLE();
    }

    public int readUnsignedShort() {
        return this.a.readUnsignedShort();
    }

    public int readUnsignedShortLE() {
        return this.a.readUnsignedShortLE();
    }

    public int readMedium() {
        return this.a.readMedium();
    }

    public int readMediumLE() {
        return this.a.readMediumLE();
    }

    public int readUnsignedMedium() {
        return this.a.readUnsignedMedium();
    }

    public int readUnsignedMediumLE() {
        return this.a.readUnsignedMediumLE();
    }

    public int readInt() {
        return this.a.readInt();
    }

    public int readIntLE() {
        return this.a.readIntLE();
    }

    public long readUnsignedInt() {
        return this.a.readUnsignedInt();
    }

    public long readUnsignedIntLE() {
        return this.a.readUnsignedIntLE();
    }

    public long readLong() {
        return this.a.readLong();
    }

    public long readLongLE() {
        return this.a.readLongLE();
    }

    public char readChar() {
        return this.a.readChar();
    }

    public float readFloat() {
        return this.a.readFloat();
    }

    public double readDouble() {
        return this.a.readDouble();
    }

    public ByteBuf readBytes(int i) {
        return this.a.readBytes(i);
    }

    public ByteBuf readSlice(int i) {
        return this.a.readSlice(i);
    }

    public ByteBuf readRetainedSlice(int i) {
        return this.a.readRetainedSlice(i);
    }

    public ByteBuf readBytes(ByteBuf bytebuf) {
        return this.a.readBytes(bytebuf);
    }

    public ByteBuf readBytes(ByteBuf bytebuf, int i) {
        return this.a.readBytes(bytebuf, i);
    }

    public ByteBuf readBytes(ByteBuf bytebuf, int i, int j) {
        return this.a.readBytes(bytebuf, i, j);
    }

    public ByteBuf readBytes(byte[] abyte) {
        return this.a.readBytes(abyte);
    }

    public ByteBuf readBytes(byte[] abyte, int i, int j) {
        return this.a.readBytes(abyte, i, j);
    }

    public ByteBuf readBytes(ByteBuffer bytebuffer) {
        return this.a.readBytes(bytebuffer);
    }

    public ByteBuf readBytes(OutputStream outputstream, int i) throws IOException {
        return this.a.readBytes(outputstream, i);
    }

    public int readBytes(GatheringByteChannel gatheringbytechannel, int i) throws IOException {
        return this.a.readBytes(gatheringbytechannel, i);
    }

    public CharSequence readCharSequence(int i, Charset charset) {
        return this.a.readCharSequence(i, charset);
    }

    public int readBytes(FileChannel filechannel, long i, int j) throws IOException {
        return this.a.readBytes(filechannel, i, j);
    }

    public ByteBuf skipBytes(int i) {
        return this.a.skipBytes(i);
    }

    public ByteBuf writeBoolean(boolean flag) {
        return this.a.writeBoolean(flag);
    }

    public ByteBuf writeByte(int i) {
        return this.a.writeByte(i);
    }

    public ByteBuf writeShort(int i) {
        return this.a.writeShort(i);
    }

    public ByteBuf writeShortLE(int i) {
        return this.a.writeShortLE(i);
    }

    public ByteBuf writeMedium(int i) {
        return this.a.writeMedium(i);
    }

    public ByteBuf writeMediumLE(int i) {
        return this.a.writeMediumLE(i);
    }

    public ByteBuf writeInt(int i) {
        return this.a.writeInt(i);
    }

    public ByteBuf writeIntLE(int i) {
        return this.a.writeIntLE(i);
    }

    public ByteBuf writeLong(long i) {
        return this.a.writeLong(i);
    }

    public ByteBuf writeLongLE(long i) {
        return this.a.writeLongLE(i);
    }

    public ByteBuf writeChar(int i) {
        return this.a.writeChar(i);
    }

    public ByteBuf writeFloat(float f) {
        return this.a.writeFloat(f);
    }

    public ByteBuf writeDouble(double d0) {
        return this.a.writeDouble(d0);
    }

    public ByteBuf writeBytes(ByteBuf bytebuf) {
        return this.a.writeBytes(bytebuf);
    }

    public ByteBuf writeBytes(ByteBuf bytebuf, int i) {
        return this.a.writeBytes(bytebuf, i);
    }

    public ByteBuf writeBytes(ByteBuf bytebuf, int i, int j) {
        return this.a.writeBytes(bytebuf, i, j);
    }

    public ByteBuf writeBytes(byte[] abyte) {
        return this.a.writeBytes(abyte);
    }

    public ByteBuf writeBytes(byte[] abyte, int i, int j) {
        return this.a.writeBytes(abyte, i, j);
    }

    public ByteBuf writeBytes(ByteBuffer bytebuffer) {
        return this.a.writeBytes(bytebuffer);
    }

    public int writeBytes(InputStream inputstream, int i) throws IOException {
        return this.a.writeBytes(inputstream, i);
    }

    public int writeBytes(ScatteringByteChannel scatteringbytechannel, int i) throws IOException {
        return this.a.writeBytes(scatteringbytechannel, i);
    }

    public int writeBytes(FileChannel filechannel, long i, int j) throws IOException {
        return this.a.writeBytes(filechannel, i, j);
    }

    public ByteBuf writeZero(int i) {
        return this.a.writeZero(i);
    }

    public int writeCharSequence(CharSequence charsequence, Charset charset) {
        return this.a.writeCharSequence(charsequence, charset);
    }

    public int indexOf(int i, int j, byte b0) {
        return this.a.indexOf(i, j, b0);
    }

    public int bytesBefore(byte b0) {
        return this.a.bytesBefore(b0);
    }

    public int bytesBefore(int i, byte b0) {
        return this.a.bytesBefore(i, b0);
    }

    public int bytesBefore(int i, int j, byte b0) {
        return this.a.bytesBefore(i, j, b0);
    }

    public int forEachByte(ByteProcessor byteprocessor) {
        return this.a.forEachByte(byteprocessor);
    }

    public int forEachByte(int i, int j, ByteProcessor byteprocessor) {
        return this.a.forEachByte(i, j, byteprocessor);
    }

    public int forEachByteDesc(ByteProcessor byteprocessor) {
        return this.a.forEachByteDesc(byteprocessor);
    }

    public int forEachByteDesc(int i, int j, ByteProcessor byteprocessor) {
        return this.a.forEachByteDesc(i, j, byteprocessor);
    }

    public ByteBuf copy() {
        return this.a.copy();
    }

    public ByteBuf copy(int i, int j) {
        return this.a.copy(i, j);
    }

    public ByteBuf slice() {
        return this.a.slice();
    }

    public ByteBuf retainedSlice() {
        return this.a.retainedSlice();
    }

    public ByteBuf slice(int i, int j) {
        return this.a.slice(i, j);
    }

    public ByteBuf retainedSlice(int i, int j) {
        return this.a.retainedSlice(i, j);
    }

    public ByteBuf duplicate() {
        return this.a.duplicate();
    }

    public ByteBuf retainedDuplicate() {
        return this.a.retainedDuplicate();
    }

    public int nioBufferCount() {
        return this.a.nioBufferCount();
    }

    public ByteBuffer nioBuffer() {
        return this.a.nioBuffer();
    }

    public ByteBuffer nioBuffer(int i, int j) {
        return this.a.nioBuffer(i, j);
    }

    public ByteBuffer internalNioBuffer(int i, int j) {
        return this.a.internalNioBuffer(i, j);
    }

    public ByteBuffer[] nioBuffers() {
        return this.a.nioBuffers();
    }

    public ByteBuffer[] nioBuffers(int i, int j) {
        return this.a.nioBuffers(i, j);
    }

    public boolean hasArray() {
        return this.a.hasArray();
    }

    public byte[] array() {
        return this.a.array();
    }

    public int arrayOffset() {
        return this.a.arrayOffset();
    }

    public boolean hasMemoryAddress() {
        return this.a.hasMemoryAddress();
    }

    public long memoryAddress() {
        return this.a.memoryAddress();
    }

    public String toString(Charset charset) {
        return this.a.toString(charset);
    }

    public String toString(int i, int j, Charset charset) {
        return this.a.toString(i, j, charset);
    }

    public int hashCode() {
        return this.a.hashCode();
    }

    public boolean equals(Object object) {
        return this.a.equals(object);
    }

    public int compareTo(ByteBuf bytebuf) {
        return this.a.compareTo(bytebuf);
    }

    public String toString() {
        return this.a.toString();
    }

    public ByteBuf retain(int i) {
        return this.a.retain(i);
    }

    public ByteBuf retain() {
        return this.a.retain();
    }

    public ByteBuf touch() {
        return this.a.touch();
    }

    public ByteBuf touch(Object object) {
        return this.a.touch(object);
    }

    public int refCnt() {
        return this.a.refCnt();
    }

    public boolean release() {
        return this.a.release();
    }

    public boolean release(int i) {
        return this.a.release(i);
    }
}
