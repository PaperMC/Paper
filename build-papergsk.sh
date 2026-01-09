#!/bin/bash
# papergsk-server Build Script
# Builds custom Paper 1.21.11 server JAR named papergsk-server.jar

set -e

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║      papergsk-server.jar Builder (Minecraft 1.21.11)           ║"
echo "║              Owner: GSK | Version: 1.0                         ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Check if in correct directory
if [ ! -f "build.gradle.kts" ]; then
    echo -e "${RED}✗ Error: build.gradle.kts not found${NC}"
    echo "  Make sure you're in the Paper root directory"
    exit 1
fi

echo -e "${YELLOW}[1/4]${NC} Checking Java version..."
JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "\K[0-9]+')
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo -e "${RED}✗ Java 21+ required (found Java $JAVA_VERSION)${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Java $JAVA_VERSION detected${NC}"

echo
echo -e "${YELLOW}[2/4]${NC} Applying patches..."
./gradlew applyPatches --quiet
echo -e "${GREEN}✓ Patches applied${NC}"

echo
echo -e "${YELLOW}[3/4]${NC} Building JAR (this may take 5-10 minutes)..."
./gradlew clean createMojmapBundlerJar --quiet
echo -e "${GREEN}✓ Build complete${NC}"

echo
echo -e "${YELLOW}[4/4]${NC} Finalizing..."

# Find the built JAR
JAR_PATH=$(find paper-server/build/libs -name "paper-server-*-mojmap.jar" -type f | head -1)

if [ -z "$JAR_PATH" ]; then
    echo -e "${RED}✗ Error: JAR file not found${NC}"
    exit 1
fi

# Copy to root as papergsk-server.jar
cp "$JAR_PATH" "papergsk-server.jar"
echo -e "${GREEN}✓ JAR ready: papergsk-server.jar${NC}"

# Get file size
SIZE=$(du -h "papergsk-server.jar" | cut -f1)

echo
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                    BUILD SUCCESSFUL ✓                         ║"
echo "╠════════════════════════════════════════════════════════════════╣"
echo -e "║ Output:  ${GREEN}papergsk-server.jar${NC} (${SIZE})"
echo "║"
echo "║ To run the server:"
echo "║"
echo "║  java -Xmx2G -Xms512M \\"
echo "║    -XX:+UseG1GC \\"
echo "║    -XX:MaxGCPauseMillis=30 \\"
echo "║    -XX:+UseStringDeduplication \\"
echo "║    papergsk-server.jar nogui"
echo "║"
echo "╚════════════════════════════════════════════════════════════════╝"
