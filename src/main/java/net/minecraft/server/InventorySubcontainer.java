package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class InventorySubcontainer implements IInventory, AutoRecipeOutput {

    private final int a;
    public final NonNullList<ItemStack> items;
    private List<IInventoryListener> c;

    public InventorySubcontainer(int i) {
        this.a = i;
        this.items = NonNullList.a(i, ItemStack.b);
    }

    public InventorySubcontainer(ItemStack... aitemstack) {
        this.a = aitemstack.length;
        this.items = NonNullList.a(ItemStack.b, aitemstack);
    }

    public void a(IInventoryListener iinventorylistener) {
        if (this.c == null) {
            this.c = Lists.newArrayList();
        }

        this.c.add(iinventorylistener);
    }

    public void b(IInventoryListener iinventorylistener) {
        this.c.remove(iinventorylistener);
    }

    @Override
    public ItemStack getItem(int i) {
        return i >= 0 && i < this.items.size() ? (ItemStack) this.items.get(i) : ItemStack.b;
    }

    public List<ItemStack> f() {
        List<ItemStack> list = (List) this.items.stream().filter((itemstack) -> {
            return !itemstack.isEmpty();
        }).collect(Collectors.toList());

        this.clear();
        return list;
    }

    @Override
    public ItemStack splitStack(int i, int j) {
        ItemStack itemstack = ContainerUtil.a(this.items, i, j);

        if (!itemstack.isEmpty()) {
            this.update();
        }

        return itemstack;
    }

    public ItemStack a(Item item, int i) {
        ItemStack itemstack = new ItemStack(item, 0);

        for (int j = this.a - 1; j >= 0; --j) {
            ItemStack itemstack1 = this.getItem(j);

            if (itemstack1.getItem().equals(item)) {
                int k = i - itemstack.getCount();
                ItemStack itemstack2 = itemstack1.cloneAndSubtract(k);

                itemstack.add(itemstack2.getCount());
                if (itemstack.getCount() == i) {
                    break;
                }
            }
        }

        if (!itemstack.isEmpty()) {
            this.update();
        }

        return itemstack;
    }

    public ItemStack a(ItemStack itemstack) {
        ItemStack itemstack1 = itemstack.cloneItemStack();

        this.d(itemstack1);
        if (itemstack1.isEmpty()) {
            return ItemStack.b;
        } else {
            this.c(itemstack1);
            return itemstack1.isEmpty() ? ItemStack.b : itemstack1;
        }
    }

    public boolean b(ItemStack itemstack) {
        boolean flag = false;
        Iterator iterator = this.items.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack1 = (ItemStack) iterator.next();

            if (itemstack1.isEmpty() || this.a(itemstack1, itemstack) && itemstack1.getCount() < itemstack1.getMaxStackSize()) {
                flag = true;
                break;
            }
        }

        return flag;
    }

    @Override
    public ItemStack splitWithoutUpdate(int i) {
        ItemStack itemstack = (ItemStack) this.items.get(i);

        if (itemstack.isEmpty()) {
            return ItemStack.b;
        } else {
            this.items.set(i, ItemStack.b);
            return itemstack;
        }
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        this.items.set(i, itemstack);
        if (!itemstack.isEmpty() && itemstack.getCount() > this.getMaxStackSize()) {
            itemstack.setCount(this.getMaxStackSize());
        }

        this.update();
    }

    @Override
    public int getSize() {
        return this.a;
    }

    @Override
    public boolean isEmpty() {
        Iterator iterator = this.items.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.isEmpty());

        return false;
    }

    @Override
    public void update() {
        if (this.c != null) {
            Iterator iterator = this.c.iterator();

            while (iterator.hasNext()) {
                IInventoryListener iinventorylistener = (IInventoryListener) iterator.next();

                iinventorylistener.a(this);
            }
        }

    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return true;
    }

    @Override
    public void clear() {
        this.items.clear();
        this.update();
    }

    @Override
    public void a(AutoRecipeStackManager autorecipestackmanager) {
        Iterator iterator = this.items.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            autorecipestackmanager.b(itemstack);
        }

    }

    public String toString() {
        return ((List) this.items.stream().filter((itemstack) -> {
            return !itemstack.isEmpty();
        }).collect(Collectors.toList())).toString();
    }

    private void c(ItemStack itemstack) {
        for (int i = 0; i < this.a; ++i) {
            ItemStack itemstack1 = this.getItem(i);

            if (itemstack1.isEmpty()) {
                this.setItem(i, itemstack.cloneItemStack());
                itemstack.setCount(0);
                return;
            }
        }

    }

    private void d(ItemStack itemstack) {
        for (int i = 0; i < this.a; ++i) {
            ItemStack itemstack1 = this.getItem(i);

            if (this.a(itemstack1, itemstack)) {
                this.b(itemstack, itemstack1);
                if (itemstack.isEmpty()) {
                    return;
                }
            }
        }

    }

    private boolean a(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack.getItem() == itemstack1.getItem() && ItemStack.equals(itemstack, itemstack1);
    }

    private void b(ItemStack itemstack, ItemStack itemstack1) {
        int i = Math.min(this.getMaxStackSize(), itemstack1.getMaxStackSize());
        int j = Math.min(itemstack.getCount(), i - itemstack1.getCount());

        if (j > 0) {
            itemstack1.add(j);
            itemstack.subtract(j);
            this.update();
        }

    }

    public void a(NBTTagList nbttaglist) {
        for (int i = 0; i < nbttaglist.size(); ++i) {
            ItemStack itemstack = ItemStack.a(nbttaglist.getCompound(i));

            if (!itemstack.isEmpty()) {
                this.a(itemstack);
            }
        }

    }

    public NBTTagList g() {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.getSize(); ++i) {
            ItemStack itemstack = this.getItem(i);

            if (!itemstack.isEmpty()) {
                nbttaglist.add(itemstack.save(new NBTTagCompound()));
            }
        }

        return nbttaglist;
    }
}
