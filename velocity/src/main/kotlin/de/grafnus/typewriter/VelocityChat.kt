package de.grafnus.typewriter;

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import org.slf4j.Logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.MESSAGE
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

@Plugin(
    id = "typewritervelocitychat", name = "TypeWriterVelocityChat", version = "1.0.0", authors = ["Grafnus"]
)
class VelocityChat @Inject constructor(val logger: Logger) {

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        val modules = module {
            single {this@VelocityChat} withOptions
                    {
                        named("plugin")
                        bind<VelocityChat>()
                        createdAtStart()
                    }
            single<MessageStore> { MessageStore() } withOptions {
                named("messageStore")
                createdAtStart()
            }
            single<MessagingHandler> { MessagingHandler() } withOptions {
                named("messagingHandler")
                createdAtStart()
            }
        }
        startKoin {
            modules(modules)
        }
    }
}
