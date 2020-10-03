package net.minecraft.server;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;

public enum EnumProtocol {

    HANDSHAKING(-1, b().a(EnumProtocolDirection.SERVERBOUND, (new EnumProtocol.a<>()).a(PacketHandshakingInSetProtocol.class, PacketHandshakingInSetProtocol::new))), PLAY(0, b().a(EnumProtocolDirection.CLIENTBOUND, (new EnumProtocol.a<>()).a(PacketPlayOutSpawnEntity.class, PacketPlayOutSpawnEntity::new).a(PacketPlayOutSpawnEntityExperienceOrb.class, PacketPlayOutSpawnEntityExperienceOrb::new).a(PacketPlayOutSpawnEntityLiving.class, PacketPlayOutSpawnEntityLiving::new).a(PacketPlayOutSpawnEntityPainting.class, PacketPlayOutSpawnEntityPainting::new).a(PacketPlayOutNamedEntitySpawn.class, PacketPlayOutNamedEntitySpawn::new).a(PacketPlayOutAnimation.class, PacketPlayOutAnimation::new).a(PacketPlayOutStatistic.class, PacketPlayOutStatistic::new).a(PacketPlayOutBlockBreak.class, PacketPlayOutBlockBreak::new).a(PacketPlayOutBlockBreakAnimation.class, PacketPlayOutBlockBreakAnimation::new).a(PacketPlayOutTileEntityData.class, PacketPlayOutTileEntityData::new).a(PacketPlayOutBlockAction.class, PacketPlayOutBlockAction::new).a(PacketPlayOutBlockChange.class, PacketPlayOutBlockChange::new).a(PacketPlayOutBoss.class, PacketPlayOutBoss::new).a(PacketPlayOutServerDifficulty.class, PacketPlayOutServerDifficulty::new).a(PacketPlayOutChat.class, PacketPlayOutChat::new).a(PacketPlayOutTabComplete.class, PacketPlayOutTabComplete::new).a(PacketPlayOutCommands.class, PacketPlayOutCommands::new).a(PacketPlayOutTransaction.class, PacketPlayOutTransaction::new).a(PacketPlayOutCloseWindow.class, PacketPlayOutCloseWindow::new).a(PacketPlayOutWindowItems.class, PacketPlayOutWindowItems::new).a(PacketPlayOutWindowData.class, PacketPlayOutWindowData::new).a(PacketPlayOutSetSlot.class, PacketPlayOutSetSlot::new).a(PacketPlayOutSetCooldown.class, PacketPlayOutSetCooldown::new).a(PacketPlayOutCustomPayload.class, PacketPlayOutCustomPayload::new).a(PacketPlayOutCustomSoundEffect.class, PacketPlayOutCustomSoundEffect::new).a(PacketPlayOutKickDisconnect.class, PacketPlayOutKickDisconnect::new).a(PacketPlayOutEntityStatus.class, PacketPlayOutEntityStatus::new).a(PacketPlayOutExplosion.class, PacketPlayOutExplosion::new).a(PacketPlayOutUnloadChunk.class, PacketPlayOutUnloadChunk::new).a(PacketPlayOutGameStateChange.class, PacketPlayOutGameStateChange::new).a(PacketPlayOutOpenWindowHorse.class, PacketPlayOutOpenWindowHorse::new).a(PacketPlayOutKeepAlive.class, PacketPlayOutKeepAlive::new).a(PacketPlayOutMapChunk.class, PacketPlayOutMapChunk::new).a(PacketPlayOutWorldEvent.class, PacketPlayOutWorldEvent::new).a(PacketPlayOutWorldParticles.class, PacketPlayOutWorldParticles::new).a(PacketPlayOutLightUpdate.class, PacketPlayOutLightUpdate::new).a(PacketPlayOutLogin.class, PacketPlayOutLogin::new).a(PacketPlayOutMap.class, PacketPlayOutMap::new).a(PacketPlayOutOpenWindowMerchant.class, PacketPlayOutOpenWindowMerchant::new).a(PacketPlayOutEntity.PacketPlayOutRelEntityMove.class, PacketPlayOutEntity.PacketPlayOutRelEntityMove::new).a(PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook.class, PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook::new).a(PacketPlayOutEntity.PacketPlayOutEntityLook.class, PacketPlayOutEntity.PacketPlayOutEntityLook::new).a(PacketPlayOutEntity.class, PacketPlayOutEntity::new).a(PacketPlayOutVehicleMove.class, PacketPlayOutVehicleMove::new).a(PacketPlayOutOpenBook.class, PacketPlayOutOpenBook::new).a(PacketPlayOutOpenWindow.class, PacketPlayOutOpenWindow::new).a(PacketPlayOutOpenSignEditor.class, PacketPlayOutOpenSignEditor::new).a(PacketPlayOutAutoRecipe.class, PacketPlayOutAutoRecipe::new).a(PacketPlayOutAbilities.class, PacketPlayOutAbilities::new).a(PacketPlayOutCombatEvent.class, PacketPlayOutCombatEvent::new).a(PacketPlayOutPlayerInfo.class, PacketPlayOutPlayerInfo::new).a(PacketPlayOutLookAt.class, PacketPlayOutLookAt::new).a(PacketPlayOutPosition.class, PacketPlayOutPosition::new).a(PacketPlayOutRecipes.class, PacketPlayOutRecipes::new).a(PacketPlayOutEntityDestroy.class, PacketPlayOutEntityDestroy::new).a(PacketPlayOutRemoveEntityEffect.class, PacketPlayOutRemoveEntityEffect::new).a(PacketPlayOutResourcePackSend.class, PacketPlayOutResourcePackSend::new).a(PacketPlayOutRespawn.class, PacketPlayOutRespawn::new).a(PacketPlayOutEntityHeadRotation.class, PacketPlayOutEntityHeadRotation::new).a(PacketPlayOutMultiBlockChange.class, PacketPlayOutMultiBlockChange::new).a(PacketPlayOutSelectAdvancementTab.class, PacketPlayOutSelectAdvancementTab::new).a(PacketPlayOutWorldBorder.class, PacketPlayOutWorldBorder::new).a(PacketPlayOutCamera.class, PacketPlayOutCamera::new).a(PacketPlayOutHeldItemSlot.class, PacketPlayOutHeldItemSlot::new).a(PacketPlayOutViewCentre.class, PacketPlayOutViewCentre::new).a(PacketPlayOutViewDistance.class, PacketPlayOutViewDistance::new).a(PacketPlayOutSpawnPosition.class, PacketPlayOutSpawnPosition::new).a(PacketPlayOutScoreboardDisplayObjective.class, PacketPlayOutScoreboardDisplayObjective::new).a(PacketPlayOutEntityMetadata.class, PacketPlayOutEntityMetadata::new).a(PacketPlayOutAttachEntity.class, PacketPlayOutAttachEntity::new).a(PacketPlayOutEntityVelocity.class, PacketPlayOutEntityVelocity::new).a(PacketPlayOutEntityEquipment.class, PacketPlayOutEntityEquipment::new).a(PacketPlayOutExperience.class, PacketPlayOutExperience::new).a(PacketPlayOutUpdateHealth.class, PacketPlayOutUpdateHealth::new).a(PacketPlayOutScoreboardObjective.class, PacketPlayOutScoreboardObjective::new).a(PacketPlayOutMount.class, PacketPlayOutMount::new).a(PacketPlayOutScoreboardTeam.class, PacketPlayOutScoreboardTeam::new).a(PacketPlayOutScoreboardScore.class, PacketPlayOutScoreboardScore::new).a(PacketPlayOutUpdateTime.class, PacketPlayOutUpdateTime::new).a(PacketPlayOutTitle.class, PacketPlayOutTitle::new).a(PacketPlayOutEntitySound.class, PacketPlayOutEntitySound::new).a(PacketPlayOutNamedSoundEffect.class, PacketPlayOutNamedSoundEffect::new).a(PacketPlayOutStopSound.class, PacketPlayOutStopSound::new).a(PacketPlayOutPlayerListHeaderFooter.class, PacketPlayOutPlayerListHeaderFooter::new).a(PacketPlayOutNBTQuery.class, PacketPlayOutNBTQuery::new).a(PacketPlayOutCollect.class, PacketPlayOutCollect::new).a(PacketPlayOutEntityTeleport.class, PacketPlayOutEntityTeleport::new).a(PacketPlayOutAdvancements.class, PacketPlayOutAdvancements::new).a(PacketPlayOutUpdateAttributes.class, PacketPlayOutUpdateAttributes::new).a(PacketPlayOutEntityEffect.class, PacketPlayOutEntityEffect::new).a(PacketPlayOutRecipeUpdate.class, PacketPlayOutRecipeUpdate::new).a(PacketPlayOutTags.class, PacketPlayOutTags::new)).a(EnumProtocolDirection.SERVERBOUND, (new EnumProtocol.a<>()).a(PacketPlayInTeleportAccept.class, PacketPlayInTeleportAccept::new).a(PacketPlayInTileNBTQuery.class, PacketPlayInTileNBTQuery::new).a(PacketPlayInDifficultyChange.class, PacketPlayInDifficultyChange::new).a(PacketPlayInChat.class, PacketPlayInChat::new).a(PacketPlayInClientCommand.class, PacketPlayInClientCommand::new).a(PacketPlayInSettings.class, PacketPlayInSettings::new).a(PacketPlayInTabComplete.class, PacketPlayInTabComplete::new).a(PacketPlayInTransaction.class, PacketPlayInTransaction::new).a(PacketPlayInEnchantItem.class, PacketPlayInEnchantItem::new).a(PacketPlayInWindowClick.class, PacketPlayInWindowClick::new).a(PacketPlayInCloseWindow.class, PacketPlayInCloseWindow::new).a(PacketPlayInCustomPayload.class, PacketPlayInCustomPayload::new).a(PacketPlayInBEdit.class, PacketPlayInBEdit::new).a(PacketPlayInEntityNBTQuery.class, PacketPlayInEntityNBTQuery::new).a(PacketPlayInUseEntity.class, PacketPlayInUseEntity::new).a(PacketPlayInJigsawGenerate.class, PacketPlayInJigsawGenerate::new).a(PacketPlayInKeepAlive.class, PacketPlayInKeepAlive::new).a(PacketPlayInDifficultyLock.class, PacketPlayInDifficultyLock::new).a(PacketPlayInFlying.PacketPlayInPosition.class, PacketPlayInFlying.PacketPlayInPosition::new).a(PacketPlayInFlying.PacketPlayInPositionLook.class, PacketPlayInFlying.PacketPlayInPositionLook::new).a(PacketPlayInFlying.PacketPlayInLook.class, PacketPlayInFlying.PacketPlayInLook::new).a(PacketPlayInFlying.class, PacketPlayInFlying::new).a(PacketPlayInVehicleMove.class, PacketPlayInVehicleMove::new).a(PacketPlayInBoatMove.class, PacketPlayInBoatMove::new).a(PacketPlayInPickItem.class, PacketPlayInPickItem::new).a(PacketPlayInAutoRecipe.class, PacketPlayInAutoRecipe::new).a(PacketPlayInAbilities.class, PacketPlayInAbilities::new).a(PacketPlayInBlockDig.class, PacketPlayInBlockDig::new).a(PacketPlayInEntityAction.class, PacketPlayInEntityAction::new).a(PacketPlayInSteerVehicle.class, PacketPlayInSteerVehicle::new).a(PacketPlayInRecipeSettings.class, PacketPlayInRecipeSettings::new).a(PacketPlayInRecipeDisplayed.class, PacketPlayInRecipeDisplayed::new).a(PacketPlayInItemName.class, PacketPlayInItemName::new).a(PacketPlayInResourcePackStatus.class, PacketPlayInResourcePackStatus::new).a(PacketPlayInAdvancements.class, PacketPlayInAdvancements::new).a(PacketPlayInTrSel.class, PacketPlayInTrSel::new).a(PacketPlayInBeacon.class, PacketPlayInBeacon::new).a(PacketPlayInHeldItemSlot.class, PacketPlayInHeldItemSlot::new).a(PacketPlayInSetCommandBlock.class, PacketPlayInSetCommandBlock::new).a(PacketPlayInSetCommandMinecart.class, PacketPlayInSetCommandMinecart::new).a(PacketPlayInSetCreativeSlot.class, PacketPlayInSetCreativeSlot::new).a(PacketPlayInSetJigsaw.class, PacketPlayInSetJigsaw::new).a(PacketPlayInStruct.class, PacketPlayInStruct::new).a(PacketPlayInUpdateSign.class, PacketPlayInUpdateSign::new).a(PacketPlayInArmAnimation.class, PacketPlayInArmAnimation::new).a(PacketPlayInSpectate.class, PacketPlayInSpectate::new).a(PacketPlayInUseItem.class, PacketPlayInUseItem::new).a(PacketPlayInBlockPlace.class, PacketPlayInBlockPlace::new))), STATUS(1, b().a(EnumProtocolDirection.SERVERBOUND, (new EnumProtocol.a<>()).a(PacketStatusInStart.class, PacketStatusInStart::new).a(PacketStatusInPing.class, PacketStatusInPing::new)).a(EnumProtocolDirection.CLIENTBOUND, (new EnumProtocol.a<>()).a(PacketStatusOutServerInfo.class, PacketStatusOutServerInfo::new).a(PacketStatusOutPong.class, PacketStatusOutPong::new))), LOGIN(2, b().a(EnumProtocolDirection.CLIENTBOUND, (new EnumProtocol.a<>()).a(PacketLoginOutDisconnect.class, PacketLoginOutDisconnect::new).a(PacketLoginOutEncryptionBegin.class, PacketLoginOutEncryptionBegin::new).a(PacketLoginOutSuccess.class, PacketLoginOutSuccess::new).a(PacketLoginOutSetCompression.class, PacketLoginOutSetCompression::new).a(PacketLoginOutCustomPayload.class, PacketLoginOutCustomPayload::new)).a(EnumProtocolDirection.SERVERBOUND, (new EnumProtocol.a<>()).a(PacketLoginInStart.class, PacketLoginInStart::new).a(PacketLoginInEncryptionBegin.class, PacketLoginInEncryptionBegin::new).a(PacketLoginInCustomPayload.class, PacketLoginInCustomPayload::new)));

    private static final EnumProtocol[] e = new EnumProtocol[4];
    private static final Map<Class<? extends Packet<?>>, EnumProtocol> f = Maps.newHashMap();
    private final int g;
    private final Map<EnumProtocolDirection, ? extends EnumProtocol.a<?>> h;

    private static EnumProtocol.b b() {
        return new EnumProtocol.b();
    }

    private EnumProtocol(int i, EnumProtocol.b enumprotocol_b) {
        this.g = i;
        this.h = enumprotocol_b.a;
    }

    @Nullable
    public Integer a(EnumProtocolDirection enumprotocoldirection, Packet<?> packet) {
        return ((EnumProtocol.a) this.h.get(enumprotocoldirection)).a(packet.getClass());
    }

    @Nullable
    public Packet<?> a(EnumProtocolDirection enumprotocoldirection, int i) {
        return ((EnumProtocol.a) this.h.get(enumprotocoldirection)).a(i);
    }

    public int a() {
        return this.g;
    }

    @Nullable
    public static EnumProtocol a(int i) {
        return i >= -1 && i <= 2 ? EnumProtocol.e[i - -1] : null;
    }

    public static EnumProtocol a(Packet<?> packet) {
        return (EnumProtocol) EnumProtocol.f.get(packet.getClass());
    }

    static {
        EnumProtocol[] aenumprotocol = values();
        int i = aenumprotocol.length;

        for (int j = 0; j < i; ++j) {
            EnumProtocol enumprotocol = aenumprotocol[j];
            int k = enumprotocol.a();

            if (k < -1 || k > 2) {
                throw new Error("Invalid protocol ID " + Integer.toString(k));
            }

            EnumProtocol.e[k - -1] = enumprotocol;
            enumprotocol.h.forEach((enumprotocoldirection, enumprotocol_a) -> {
                enumprotocol_a.a().forEach((oclass) -> {
                    if (EnumProtocol.f.containsKey(oclass) && EnumProtocol.f.get(oclass) != enumprotocol) {
                        throw new IllegalStateException("Packet " + oclass + " is already assigned to protocol " + EnumProtocol.f.get(oclass) + " - can't reassign to " + enumprotocol);
                    } else {
                        EnumProtocol.f.put(oclass, enumprotocol);
                    }
                });
            });
        }

    }

    static class b {

        private final Map<EnumProtocolDirection, EnumProtocol.a<?>> a;

        private b() {
            this.a = Maps.newEnumMap(EnumProtocolDirection.class);
        }

        public <T extends PacketListener> EnumProtocol.b a(EnumProtocolDirection enumprotocoldirection, EnumProtocol.a<T> enumprotocol_a) {
            this.a.put(enumprotocoldirection, enumprotocol_a);
            return this;
        }
    }

    static class a<T extends PacketListener> {

        private final Object2IntMap<Class<? extends Packet<T>>> a;
        private final List<Supplier<? extends Packet<T>>> b;

        private a() {
            this.a = (Object2IntMap) SystemUtils.a((Object) (new Object2IntOpenHashMap()), (object2intopenhashmap) -> {
                object2intopenhashmap.defaultReturnValue(-1);
            });
            this.b = Lists.newArrayList();
        }

        public <P extends Packet<T>> EnumProtocol.a<T> a(Class<P> oclass, Supplier<P> supplier) {
            int i = this.b.size();
            int j = this.a.put(oclass, i);

            if (j != -1) {
                String s = "Packet " + oclass + " is already registered to ID " + j;

                LogManager.getLogger().fatal(s);
                throw new IllegalArgumentException(s);
            } else {
                this.b.add(supplier);
                return this;
            }
        }

        @Nullable
        public Integer a(Class<?> oclass) {
            int i = this.a.getInt(oclass);

            return i == -1 ? null : i;
        }

        @Nullable
        public Packet<?> a(int i) {
            Supplier<? extends Packet<T>> supplier = (Supplier) this.b.get(i);

            return supplier != null ? (Packet) supplier.get() : null;
        }

        public Iterable<Class<? extends Packet<?>>> a() {
            return Iterables.unmodifiableIterable(this.a.keySet());
        }
    }
}
