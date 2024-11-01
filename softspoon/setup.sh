#!/bin/sh

mkdir "buildtools" || true
(
    cd buildtools || exit
    wget https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar
    #java -jar BuildTools.jar --rev 4369 # random 1.21.3 version
)

echo "copying spigot-mapped"
mkdir "spigot-mapped" || true
cp -r buildtools/work/decompile-7331f017/classes spigot-mapped

echo "copying spigot-decomp"
mkdir "spigot-decomp" || true
cp -r buildtools/work/decompile-7331f017/net spigot-decomp/net

echo "making vine-decomp"
mkdir "vine-decomp" || true
wget https://s01.oss.sonatype.org/content/repositories/snapshots/org/vineflower/vineflower/1.11.0-SNAPSHOT/vineflower-1.11.0-20240911.205325-50.jar -O vineflower.jar
# todo double check vineflower version and arguments, rn this is mache stuff
java -jar vineflower.jar --synthetic-not-set=true --ternary-constant-simplification=true --include-runtime=current --decompile-complex-constant-dynamic=true --indent-string="    " --decompile-inner=true --remove-bridge=true --decompile-generics=true --ascii-strings=false --remove-synthetic=true --include-classpath=true --inline-simple-lambdas=true --ignore-invalid-bytecode=false --bytecode-source-mapping=true --dump-code-lines=true --override-annotation=false --skip-extra-files=true spigot-mapped/classes vine-decomp

# todo spigot and vf decompile a paper (old) remapped jar (just steal from some paper project cache folder)
