#!/bin/sh

find ../PaperSpigot-Server/src/ -type f -exec sed -i 's/net.minecraft.util.//g' {} \;
find ../Spigot-Server/src/ -type f -exec sed -i 's/net.minecraft.util.//g' {} \;
find ../Spigot-Server-Patches/ -type f -exec sed -i 's/net.minecraft.util.//g' {} \;
find ../CraftBukkit-Patches/ -type f -exec sed -i 's/net.minecraft.util.//g' {} \;

find ../PaperSpigot-Server/src/ -type f -exec sed -i 's/com.mojang.authlib./net.minecraft.util.com.mojang.authlib./g' {} \;
find ../Spigot-Server/src/ -type f -exec sed -i 's/com.mojang.authlib./net.minecraft.util.com.mojang.authlib./g' {} \;
find ../CraftBukkit-Patches/ -type f -exec sed -i 's/com.mojang.authlib./net.minecraft.util.com.mojang.authlib./g' {} \;
find ../Spigot-Server-Patches/ -type f -exec sed -i 's/com.mojang.authlib./net.minecraft.util.com.mojang.authlib./g' {} \;
