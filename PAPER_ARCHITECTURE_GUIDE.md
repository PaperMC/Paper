# Paper Server Architecture & Development Guide

## Overview
Paper is a high-performance Minecraft server that modifies the vanilla Minecraft server source code through a patch-based system. It extends the Bukkit API and CraftBukkit implementation with performance optimizations and additional features.

**Version**: 1.21.11-R0.1-SNAPSHOT  
**Java Requirement**: JDK 21+  
**Build System**: Gradle with Paperweight plugin

---

## Project Structure

```
paper/
├── paper-api/              # Public API for plugin developers (1820 Java files)
├── paper-server/           # Server implementation (1149 Java files)
├── paper-generator/        # Code generation utilities
├── test-plugin/            # Example test plugin
├── scripts/               # Build and utility scripts
├── patches/               # Patch files for Minecraft modifications
├── gradle/                # Gradle wrapper and configuration
└── build-data/            # Build-time data and mappings
```

---

## Three-Tier Architecture

### 1. **Paper API** (`paper-api/`)
**Purpose**: Define the public plugin development interface

**Key Components**:
- **Base Interfaces**: `Plugin`, `PluginManager`, `Event`, `Listener`
- **Event System**: Core event framework for plugins
  - `Event` - Base class for all events
  - `EventHandler`, `EventPriority` - Event registration annotations
  - `HandlerList` - Event listener management
  - `Listener` - Interface for event listener classes
- **Bukkit API Extensions**: Extends Bukkit with Paper-specific features
  - `io.papermc.paper.*` - Paper-specific additions
  - `com.destroystokyo.paper.*` - Legacy Paper extensions
  - `org.bukkit.*` - Bukkit core API
- **Dependencies**:
  - Guava 33.3.1-jre
  - Gson 2.11.0
  - SnakeYAML 2.2
  - Adventure 4.25.0 (chat/messaging)
  - JOML 1.10.8 (math)
  - FastUtil 8.5.15 (efficient collections)
  - Log4j 2.24.1, SLF4J 2.0.16 (logging)
  - Brigadier 1.3.10 (command framework)

**Compilation**: Produces `paper-api-VERSION.jar` with sources and Javadocs

### 2. **Paper Server** (`paper-server/`)
**Purpose**: Implement the API and modify Minecraft server behavior

**Key Components**:
- **CraftBukkit Implementation**: 
  - `org.bukkit.craftbukkit.*` - Implementation of Bukkit API
  - `CraftServer`, `CraftPlayer`, `CraftChunk`, etc.
  - Translates high-level API calls to Minecraft internals
- **Paper-Specific Implementation**:
  - `io.papermc.paper.*` - Paper extensions implementation
  - Plugin management, event handling, optimizations
- **Minecraft Source (Patched)**:
  - Decompiled and deobfuscated Minecraft classes
  - Modified through the patch system

**Key Features**:
- Plugin system with lifecycle events
- Event manager (`PaperEventManager`)
- Performance optimizations via Moonrise
- Anti-Xray system
- Custom configurations
- Spark integration (profiling)

**Build Output**: `paper-server-VERSION.jar` (compiled server JAR)

### 3. **Gradle Build System**
**Configuration Files**:
- `build.gradle.kts` - Root build configuration
- `settings.gradle.kts` - Project structure and modules
- `gradle.properties` - Version properties
  - `mcVersion=1.21.11`
  - `apiVersion=1.21.11`
  - `version=1.21.11-R0.1-SNAPSHOT`

**Key Plugins**:
- `io.papermc.paperweight.core` - Paper's custom Gradle plugin for patch management

---

## Patch System (Core Innovation)

The patch system is the foundation of Paper. Instead of maintaining a full fork, Paper patches the original Minecraft source:

### Patch Types

#### **1. Source Patches** (`paper-server/patches/sources/`)
- Per-file patches for individual Minecraft classes
- Non-intrusive modifications to specific methods
- Applied as individual Git commits on the Minecraft source tree
- Examples:
  - `com/mojang/brigadier/exceptions/CommandSyntaxException.java.patch`
  - Patches to specific Minecraft classes

**How They Work**:
1. Minecraft source is decompiled and deobfuscated
2. Per-file patches are applied as Git commits
3. Each patch modifies one or a few specific methods
4. Easier to maintain during Minecraft updates

#### **2. Feature Patches** (`paper-server/patches/features/`)
- Large-scale optimizations and features (32 patches, ~80k lines)
- Can be optionally dropped during Minecraft updates
- Each adds significant functionality

**Notable Feature Patches**:
- `0001-Moonrise-optimisation-patches.patch` (~1.7MB)
  - Starlight lighting engine
  - Chunk system optimizations
  - Entity tracker improvements
  - Collision optimizations
  - Random block ticking optimizations
- `0002-Rewrite-dataconverter-system.patch` (~2.1MB)
- `0003-Optimize-Network-Manager.patch`
- `0005-Entity-Activation-Range-2.0.patch` (40KB)
- `0015-Eigencraft-redstone-implementation.patch` (55KB)
- `0016-Add-Alternate-Current-redstone-implementation.patch` (93KB)
- `0028-Optimize-Hoppers.patch`
- `0029-Anti-Xray.patch` (40KB)

#### **3. Resource Patches** (`paper-server/patches/resources/`)
- Patches to Minecraft data files (JSON, etc.)
- Modifications to world generation, loot tables, etc.
- Examples:
  - `data/minecraft/worldgen/noise_settings/*.json.patch`
  - `data/minecraft/loot_table/`.patch files

### Patch Workflow

**Applying Patches**:
```bash
./gradlew applyPatches
```
- Creates `paper-server/src/minecraft/` directory
- Applies all patches on top of decompiled Minecraft source
- Creates a Git repository in this directory

**Modifying Patches**:

1. **Make changes** in `paper-server/src/minecraft/`
2. **Fix patches**:
   ```bash
   ./gradlew fixupSourcePatches
   ```
3. **Rebuild patches**:
   ```bash
   ./gradlew rebuildPatches
   ```

**Adding Feature Patches**:
```bash
cd paper-server/src/minecraft
git add .
git commit -m "Feature: My optimization"
cd ../../../
./gradlew rebuildPatches
```

**Resolving Conflicts**:
```bash
cd paper-server/src/minecraft/java
git rebase -i base
# Edit conflicts and continue
git rebase --continue
./gradlew rebuildPatches
```

---

## Build Process & JAR Generation

### Build Tasks

**Main Build Tasks** (from README):
```bash
# Download Minecraft and apply patches
./gradlew applyPatches

# Build the final Paperclip JAR
./gradlew createMojmapBundlerJar

# List all available tasks
./gradlew tasks
```

### JAR Build Pipeline

1. **Compile paper-api**
   - Produces API JAR with Bukkit interfaces
   
2. **Compile paper-server**
   - Compiles implementation code
   - Includes patched Minecraft classes
   - Links against paper-api
   
3. **Generate Development Bundle**
   - Creates bundle with all dependencies
   - Allows plugin developers to compile against Paper
   
4. **Create Paperclip JAR**
   - Final executable server JAR
   - Contains all dependencies
   - Can be run directly: `java -jar paper.jar`

### Key JAR Configuration

From `paper-server/build.gradle.kts`:
```properties
Main-Class: org.bukkit.craftbukkit.Main
Implementation-Title: Paper
Implementation-Version: <mc-version>-<build>-<git-hash>
Brand-Id: papermc:paper
Brand-Name: Paper
```

**Manifest Metadata** included:
- Git commit hash
- Build number (from CI)
- Build timestamp
- Git branch
- Specification version

---

## Core Classes & Hierarchies

### Event System

```
Event (org.bukkit.event)
├── PlayerEvent
│   ├── PlayerJoinEvent
│   ├── PlayerQuitEvent
│   └── PlayerMoveEvent
├── BlockEvent
│   ├── BlockBreakEvent
│   └── BlockPlaceEvent
├── EntityEvent
│   └── EntityDamageEvent
└── ServerEvent
    └── ServerStartEvent

Listener (interface) - Mark event listener classes
HandlerList - Manages listeners for a specific event
EventPriority - LOWEST, LOW, NORMAL, HIGH, HIGHEST
```

### Plugin System

```
Plugin (interface) - Core plugin abstraction
├── PluginBase - Abstract implementation
└── JavaPlugin - Standard implementation

PluginManager - Loads and manages plugins
├── loadPlugins() - Load from JAR files
├── disablePlugins() - Shut down all
├── registerEvents() - Register event listeners
├── callEvent() - Trigger events
└── registerCommand() - Register commands

LifecycleEventManager - Modern async plugin initialization
BootstrapContext - Early initialization phase
```

### Server Classes

```
Server (interface) - Main server interface
├── getWorld() - Get world by name
├── getOnlinePlayers() - Get connected players
├── dispatchCommand() - Execute commands
└── broadcast() - Send messages

CraftServer (implementation)
├── Manages plugins, worlds, players
├── Implements Bukkit Server interface
└── ~130KB of implementation code

ServerLevel (Minecraft) - Patched world representation
ChunkHolder - Async chunk loading/unloading
Entity - Base entity class (player, mob, item)
```

### Paper-Specific Features

```
io.papermc.paper.entity.pathfinding - Pathfinding optimizations
io.papermc.paper.world - World management
io.papermc.paper.registry - Registry access
io.papermc.paper.event - Paper-specific events
io.papermc.paper.plugin - Plugin system extensions
io.papermc.paper.antixray - Anti-Xray implementation
io.papermc.paper.threadedregions - Moonrise threading
```

---

## Key Performance Optimizations (Moonrise)

The first feature patch (`0001-Moonrise-optimisation-patches.patch`) includes:

### Chunk System
- Improved chunk loading/unloading
- Better memory management
- More efficient chunk ticking

### Lighting (Starlight)
- Rewritten lighting engine
- Faster light propagation
- More efficient storage

### Entity Tracking
- Better player distance tracking
- Optimized entity visibility calculations
- Reduced packet sends

### Collision Detection
- Faster collision checking
- Optimized AABB calculations
- Reduced CPU usage during movement

### Block Ticking
- Random block tick optimizations
- Better neighbor block handling
- More efficient biome lookups

### Bitwise Operations
- Inlined bit operations
- Removed boxing overhead
- Better JIT optimization

---

## Dependencies & Libraries

**Logging**:
- Log4j 2.24.1 (core logging)
- SLF4J 2.0.16 (logging facade)
- Terminal Console Appender (colored console output)

**Data Structures**:
- FastUtil 8.5.15 (optimized collections)
- JOML 1.10.8 (math library)
- SnakeYAML 2.2 (YAML parsing)

**Networking**:
- Netty 4.2.7.Final (async I/O)
- HAProxy support via netty-codec-haproxy
- Velocity native compression/cipher

**Build & Remapping**:
- SrgUtils 1.0.9 (mappings)
- AutoRenamingTool 2.0.3 (plugin remapping)
- Reflection-Rewriter (reflection optimization)

**Profiling**:
- Spark 1.10.152 (built-in profiler)

---

## How to Build a Forked JAR

### Step 1: Clone and Setup
```bash
git clone https://github.com/PaperMC/Paper.git
cd Paper
./gradlew applyPatches
```

### Step 2: Make Your Modifications

**For API changes**:
```bash
cd paper-api/src/main/java
# Add or modify Java files
```

**For server implementation changes**:
```bash
cd paper-server/src/main/java
# Modify CraftBukkit or Paper implementation
```

**For Minecraft behavior changes**:
```bash
cd paper-server/src/minecraft
# Edit Minecraft source files
git add .
git commit -m "My change"
cd ../../../
./gradlew fixupSourcePatches
./gradlew rebuildPatches
```

### Step 3: Build the JAR
```bash
./gradlew createMojmapBundlerJar
```

Output JAR: `paper-server/build/libs/paper-server-VERSION-mojmap.jar`

### Step 4: Run Your Server
```bash
java -jar paper-server-VERSION-mojmap.jar nogui
```

---

## File Organization Summary

| Directory | Purpose | Key Files |
|-----------|---------|-----------|
| `paper-api/src/main/java` | Public plugin API | 1820 Java files |
| `paper-server/src/main/java` | Server implementation | 1149 Java files |
| `paper-server/src/minecraft` | Patched Minecraft source | Generated from patches |
| `paper-server/patches/features/` | Large optimization patches | 32 patches, ~80KB lines |
| `paper-server/patches/sources/` | Per-file Minecraft patches | Hundreds of individual patches |
| `paper-server/patches/resources/` | Data file patches | JSON, configuration modifications |
| `build-data/` | Build metadata | Mappings, access wideners |
| `gradle/` | Build system | Wrapper, toolchain |

---

## Development Workflow

### Typical Plugin Developer Flow
1. Download Paper JAR from papermc.io
2. Add paper-api as dependency
3. Create plugin extending `JavaPlugin`
4. Register event listeners
5. Build plugin JAR
6. Drop in `plugins/` folder

### Paper Contributor Flow
1. Fork Paper repository
2. Create feature branch from `main`
3. Make changes (API, implementation, or patches)
4. Run `./gradlew rebuildPatches` if modifying Minecraft
5. Submit PR (Paper will handle rebasing)
6. Paper maintainers review and merge

### Fork Server Builder Flow
1. Clone Paper repository
2. Make modifications to patches or implementation
3. Update `gradle.properties` if needed (version)
4. Run `./gradlew applyPatches && ./gradlew createMojmapBundlerJar`
5. Test the resulting JAR
6. Distribute custom JAR to your server

---

## Key Configuration Files

### gradle.properties
```properties
group=io.papermc.paper
version=1.21.11-R0.1-SNAPSHOT
mcVersion=1.21.11
apiVersion=1.21.11
updatingMinecraft=false

org.gradle.configuration-cache=true
org.gradle.caching=true
org.gradle.parallel=true
```

### build.gradle.kts (Root)
- Applies Java 21 toolchain
- Configures publishing
- Sets encoding and compilation options
- Defines test logging

### paper-server/build.gradle.kts
- Applies Paperweight plugin
- Configures Minecraft version
- Manages dependencies
- Creates JAR with manifest

### settings.gradle.kts
- Requires `.git` directory (must clone, not download ZIP)
- Includes `paper-api`, `paper-server`, `paper-generator`, `test-plugin`
- Configures plugin repositories

---

## Important Notes

1. **Git Required**: Must clone via Git, not download ZIP (checked in `settings.gradle.kts`)

2. **JDK 21**: Compilation requires JDK 21+, but Gradle can provision it if only JRE 17+ is available

3. **Patch Management**: All Minecraft modifications flow through the patch system for maintainability

4. **Version Numbering**: 
   - `1.21.11` - Minecraft version
   - `R0.1` - Paper revision
   - `SNAPSHOT` - Development version

5. **API/Implementation Split**: 
   - API is stable and versioned
   - Implementation can change frequently
   - Plugins compile against API only

6. **Backward Compatibility**: Paper maintains Bukkit compatibility while adding extensions

---

## Resources

- **Documentation**: https://docs.papermc.io
- **Downloads**: https://papermc.io/downloads/paper
- **API Javadocs**: https://papermc.io/javadocs
- **Forums**: https://forums.papermc.io
- **Discord**: https://discord.gg/papermc
- **Contributing Guide**: [CONTRIBUTING.md](CONTRIBUTING.md)

---

## Summary

Paper is structured as:
1. **Paper API** - Public plugin interface (extends Bukkit)
2. **Paper Server** - Implementation (extends CraftBukkit with optimizations)
3. **Minecraft Source** - Patched vanilla server code via Git-based patch system
4. **Build System** - Gradle with Paperweight managing patch application and JAR creation

To create a forked server:
1. Clone Paper
2. Apply patches (`./gradlew applyPatches`)
3. Make your modifications
4. Build JAR (`./gradlew createMojmapBundlerJar`)
5. Run or distribute the JAR

The patch system allows maintenance of significant modifications to Minecraft while keeping the codebase organized and updatable.
