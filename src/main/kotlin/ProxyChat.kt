import com.typewritermc.core.extension.Initializable
import com.typewritermc.core.extension.annotations.Initializer


@Initializer
object ProxyChatInitializer : Initializable {

    var MessageHandler = ChannelHandler()

    override fun initialize() {
        // Do something when the extension is initialized
        MessageHandler.initialize()
    }

    override fun shutdown() {
        // Do something when the extension is shutdown
        MessageHandler.destroy()
    }
}