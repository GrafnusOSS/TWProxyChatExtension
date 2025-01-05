package de.grafnus.typewriter;

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListenerPriority
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.ServerConnectedEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.messages.ChannelIdentifier
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier
import io.github.retrooper.packetevents.velocity.factory.VelocityPacketEventsBuilder
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.slf4j.Logger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.nio.file.Path
import java.util.*

@Plugin(
    id = "typewritervelocitychat", name = "TypeWriterVelocityChat", version = "1.0.0", authors = ["Grafnus"]
)
class VelocityChat @Inject constructor(val proxy: ProxyServer, val logger: Logger, @DataDirectory val dataDirectory: Path) : KoinComponent {

    val identifier: ChannelIdentifier = MinecraftChannelIdentifier.from("playerchatchanel:main");
    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        val modules = module {
            single {this@VelocityChat} withOptions
                    {
                        named("plugin")
                        bind<VelocityChat>()
                        createdAtStart()
                    }
            single { MessageStore() } withOptions {
                named("messageStore")
                createdAtStart()
            }
            single { MessagingHandler() } withOptions {
                named("messagingHandler")
                createdAtStart()
            }
        }
        startKoin {
            modules(modules)
        }
        var init = false

        val container: Optional<PluginContainer>? = proxy.pluginManager.getPlugin("typewritervelocitychat")
        container?.ifPresent {
            PacketEvents.setAPI(VelocityPacketEventsBuilder.build(proxy, container.get(), logger, dataDirectory))
            PacketEvents.getAPI().load()

            PacketEvents.getAPI().eventManager.registerListener(
                ChatMessageListener(), PacketListenerPriority.NORMAL
            )

            PacketEvents.getAPI().init()



            proxy.channelRegistrar.register(identifier);
            init = true
        }
        if (!init) {
            logger.warn("Proxy Plugin TypeWriterVelocityChat could not be initialized!")
        }

    }

    @Subscribe
    fun onPlayerServerConnect(event: ServerConnectedEvent) {
        val msgHandler: MessagingHandler = get()
        msgHandler.handleServerSwitch(event.player.uniqueId)
    }
}
