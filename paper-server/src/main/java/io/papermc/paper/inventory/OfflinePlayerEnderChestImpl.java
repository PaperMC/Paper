package io.papermc.paper.inventory;

import com.google.common.base.Preconditions;
import io.papermc.paper.entity.PlayerDataFile;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static net.minecraft.world.entity.player.Inventory.EQUIPMENT_SLOTS_SORTED_BY_INDEX;
import static net.minecraft.world.entity.player.Inventory.EQUIPMENT_SLOT_MAPPING;

public final class OfflinePlayerEnderChestImpl implements OfflinePlayerEnderChest{
    public final PlayerDataFile holder;
    private final Int2ObjectSortedMap<ItemStack> items=new Int2ObjectLinkedOpenHashMap<>(27);

    private final ListTag data;

    public OfflinePlayerEnderChestImpl(ListTag data, PlayerDataFile holder) throws NbtFormatException {
        byte listType=data.identifyRawElementType();
        if(listType!=Tag.TAG_COMPOUND&&listType!=Tag.TAG_END)throw new NbtFormatException("Player inventory has incorrect format");

        this.holder=holder;
        this.data=data;

        for(int i=0;i<data.size();i++){
            Optional<CompoundTag> tag=data.getCompound(i);
            if(tag.isEmpty())continue;
            CompoundTag itemTag=tag.get();

            ItemStack item=computeItem(itemTag);
            if(item==null||item.isEmpty())continue;

            Optional<Byte> slotTag=itemTag.getByte("Slot");
            if(slotTag.isEmpty())throw new NbtFormatException("Item has no slot");
            byte slot=slotTag.get();

            if(slot>26||slot<0)throw new NbtFormatException("Item slot is incorrect");

            items.put(slot,item);
        }
        items.defaultReturnValue(null);
    }

    private static ItemStack computeItem(CompoundTag data) throws NbtFormatException{
        net.minecraft.world.item.ItemStack handler=net.minecraft.world.item.ItemStack.CODEC.decode(NbtOps.INSTANCE,data)
            .getOrThrow(NbtFormatException::new).getFirst();
        return handler.asBukkitMirror();
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public int getMaxStackSize() {
        return Item.ABSOLUTE_MAX_STACK_SIZE;
    }

    @Override
    public @Nullable ItemStack getItem(final int index) {
        if(index<0||index>=getSize())throw new IllegalArgumentException("Index out of inventory range");
        return items.get(index);
    }

    @Override
    public void setItem(final int index, @Nullable final ItemStack item) {
        if(index<0||index>=getSize())throw new IllegalArgumentException("Index out of inventory range");

        if(item==null||item.isEmpty())items.remove(index);
        else items.put(index,item);
    }

    @Override
    public @NotNull HashMap<Integer, ItemStack> addItem(final @NotNull ItemStack... itemsToAdd) throws IllegalArgumentException {
        Preconditions.checkArgument(itemsToAdd != null, "items cannot be null");
        HashMap<Integer, ItemStack> out=new HashMap<>();

        int i=0;
        while(items.size()!=27){
            ItemStack item=itemsToAdd[i];
            if(item==null||item.isEmpty()){
                i++;
                continue;
            }

            int added=0;
            int slot=0;
            while(added!=item.getAmount()&&slot!=27){
                ItemStack slotItem=items.get(slot);
                if(slotItem==null){
                    items.put(slot,item.clone());
                    added+=item.getAmount();
                }else if(slotItem.isSimilar(item)&&slotItem.getAmount()!=slotItem.getMaxStackSize()){
                    int count=Math.min(item.getAmount(),slotItem.getMaxStackSize()-slotItem.getAmount());
                    added+=count;
                    slotItem.setAmount(slotItem.getAmount()+count);
                }
                slot++;
            }
            if(added!=item.getAmount()){
                ItemStack cl=item.clone();
                cl.setAmount(item.getAmount()-added);

                out.put(i,cl);
            }

            i++;
        }
        if(i!=itemsToAdd.length-1)while(i!=itemsToAdd.length-1){
            ItemStack item=itemsToAdd[i];
            if(item!=null&&!item.isEmpty())out.put(i,item.clone());
            i++;
        }

        return out;
    }

    @Override
    public @NotNull HashMap<Integer, ItemStack> removeItem(final @NotNull ItemStack... itemsToRemove) throws IllegalArgumentException {
        Preconditions.checkArgument(itemsToRemove != null, "items cannot be null");
        HashMap<Integer, ItemStack> out=new HashMap<>();
        ObjectSortedSet<Int2ObjectMap.Entry<ItemStack>> itemSet=new ObjectRBTreeSet<>(items.int2ObjectEntrySet());

        for(int i=0;i<itemsToRemove.length;i++){
            ItemStack item=itemsToRemove[i];
            if(item==null||item.isEmpty())continue;

            AtomicInteger toRemove=new AtomicInteger(item.getAmount());
            itemSet.stream()
                .filter(ent->{
                    ItemStack it=ent.getValue();
                    return it!=null&&it.isSimilar(item);
                })
                .takeWhile(ent->{
                    ItemStack it=ent.getValue();
                    int remove=Math.min(toRemove.get(),it.getAmount());
                    if(remove==it.getAmount())items.remove(ent.getIntKey());
                    else it.setAmount(it.getAmount()-remove);

                    return toRemove.addAndGet(-remove)!=0;
                });

            int least=toRemove.get();
            if(least!=0){
                ItemStack cl=item.clone();
                cl.setAmount(least);
                out.put(i,cl);
            }
        }

        return out;
    }

    @Override
    public @Nullable ItemStack @NotNull [] getContents() {
        List<ItemStack> out=new ArrayList<>();

        for(int i=0;i<getSize();i++){
            out.add(items.get(i));
        }

        return out.toArray(ItemStack[]::new);
    }

    @Override
    public void setContents(final @Nullable ItemStack @NotNull [] itemsToSet) throws IllegalArgumentException {
        Preconditions.checkArgument(itemsToSet != null, "items cannot be null");

        int sizeToCheck=Math.min(itemsToSet.length,getSize());
        for(int i=0;i<sizeToCheck;i++){
            ItemStack item=itemsToSet[i];
            if(item==null||item.isEmpty())items.remove(i);
            else items.put(i,item);
        }
    }

    @Override
    public boolean contains(@NotNull final Material material) throws IllegalArgumentException {
        Preconditions.checkArgument(material != null, "material cannot be null");
        if(material==Material.AIR)return false;

        return items.values().stream().anyMatch(it->it.getType()==material);
    }

    @Override
    public boolean contains(@Nullable final ItemStack item) {
        if(item==null||item.isEmpty())return false;
        return items.values().stream().anyMatch(it->it.equals(item));
    }

    @Override
    public boolean contains(@NotNull final Material material, final int amount) throws IllegalArgumentException {
        Preconditions.checkArgument(material != null, "material cannot be null");
        if(amount<=0||material==Material.AIR)return false;

        return items.values().stream().anyMatch(it->it.getType()==material&&it.getAmount()==amount);
    }

    @Override
    public boolean contains(@Nullable final ItemStack item, final int amount) {
        if(item==null||item.isEmpty()||amount<=0)return false;
        return items.values().stream().anyMatch(it->it.isSimilar(item)&&it.getAmount()==amount);
    }

    @Override
    public boolean containsAtLeast(@Nullable final ItemStack item, final int amount) {
        if(item==null||item.isEmpty()||amount<=0)return false;

        AtomicInteger cAmount=new AtomicInteger(amount);
        items.values().stream().takeWhile(i->{
            int now=cAmount.get();
            if(now==0)return false;
            if(i.isSimilar(item))cAmount.addAndGet(-Math.min(now,i.getAmount()));
            return true;
        });

        return cAmount.get()==0;
    }

    @Override
    public @NotNull HashMap<Integer, ? extends ItemStack> all(final @NotNull Material material) throws IllegalArgumentException {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        HashMap<Integer,ItemStack> out=new HashMap<>();
        if(material==Material.AIR)return out;

        items.int2ObjectEntrySet().stream()
            .filter(ent->ent.getValue().getType()==material)
            .forEach(ent->out.put(ent.getIntKey(),ent.getValue()));

        return out;
    }

    @Override
    public @NotNull HashMap<Integer, ? extends ItemStack> all(@Nullable final ItemStack item) {
        HashMap<Integer,ItemStack> out=new HashMap<>();
        if(item==null||item.isEmpty())return out;

        items.int2ObjectEntrySet().stream()
            .filter(ent->ent.getValue().equals(item))
            .forEach(ent->out.put(ent.getIntKey(),ent.getValue()));

        return out;
    }

    @Override
    public int first(@NotNull final Material material) throws IllegalArgumentException {
        Preconditions.checkArgument(material != null, "material cannot be null");
        if(material==Material.AIR)return -1;

        return items.int2ObjectEntrySet().stream()
            .filter(ent->ent.getValue().getType()==material)
            .findFirst().map(Int2ObjectMap.Entry::getIntKey).orElse(-1);
    }

    @Override
    public int first(@NotNull final ItemStack item) {
        Preconditions.checkArgument(item != null, "item cannot be null");
        if(item.isEmpty())return -1;

        return items.int2ObjectEntrySet().stream()
            .filter(ent->ent.getValue().equals(item))
            .findFirst().map(Int2ObjectMap.Entry::getIntKey).orElse(-1);
    }

    @Override
    public int firstEmpty() {
        for(int i=0;i<items.size();i++){
            if(!items.containsKey(i))return i;
        }

        return -1;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public void remove(@NotNull final Material material) throws IllegalArgumentException {
        Preconditions.checkArgument(material != null, "material cannot be null");
        if(material==Material.AIR)return;

        Set<Int2ObjectMap.Entry<ItemStack>> itemSet=new HashSet<>(items.int2ObjectEntrySet());
        itemSet.stream().filter(ent->ent.getValue().getType()==material).forEach(ent->items.remove(ent.getIntKey()));
    }

    @Override
    public void remove(@NotNull final ItemStack item) {
        Preconditions.checkArgument(item != null, "item cannot be null");
        if(item.isEmpty())return;

        Set<Int2ObjectMap.Entry<ItemStack>> itemSet=new HashSet<>(items.int2ObjectEntrySet());
        itemSet.stream().filter(ent->ent.getValue().equals(item)).forEach(ent->items.remove(ent.getIntKey()));
    }

    @Override
    public void clear(final int index) {
        if(index<0||index>=getSize())throw new IllegalArgumentException("Index out of inventory range");
        items.remove(index);
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public @Nullable PlayerDataFile getHolder() {
        return holder;
    }

    @Override
    public @NotNull ListIterator<ItemStack> iterator() {
        return items.values().stream().toList().listIterator();
    }

    @Override
    public @NotNull ListIterator<ItemStack> iterator(final int index) {
        return items.values().stream().toList().listIterator(index);
    }

    @Override
    public @Nullable Location getLocation() {
        return holder.getLocation();
    }

    public void save() throws NbtFormatException{
        data.clear();

        items.forEach((slot,stack)->{
            CompoundTag tag=encodeItem(stack);
            tag.putByte("Slot",slot.byteValue());

            data.add(tag);
        });
    }

    private CompoundTag encodeItem(ItemStack item){
        return (CompoundTag)net.minecraft.world.item.ItemStack.CODEC
            .encode(CraftItemStack.unwrap(item),NbtOps.INSTANCE,new CompoundTag())
            .getOrThrow(NbtFormatException::new);
    }
}
