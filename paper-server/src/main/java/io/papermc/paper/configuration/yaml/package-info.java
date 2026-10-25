/*
 * Copied from GeyserMC's Configurate fork:
 * https://github.com/GeyserMC/Configurate/blob/f6aa72e6099f956e78b638a2a817429007c6898a/format/yaml/src/main/java/org/spongepowered/configurate/yaml/package-info.java
 *
 * Configurate
 * Copyright (C) zml and Configurate contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Configuration loader for the <a href="https://yaml.org/spec/1.1/">YAML 1.1</a> format.
 *
 * <p>While by default this loader only declares <a href="https://yaml.org/type/index.html">the YAML 1.1 global tags</a> as
 * supported types, the underlying library is capable of serializing any POJO,
 * and the {@link org.spongepowered.configurate.ConfigurationOptions}'s native
 * types field can be adjusted to customize output.
 */
@DefaultQualifier(NonNull.class)
package io.papermc.paper.configuration.yaml;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
