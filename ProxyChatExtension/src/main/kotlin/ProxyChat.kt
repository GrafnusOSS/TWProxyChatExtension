import com.typewritermc.core.extension.Initializable
import com.typewritermc.core.extension.annotations.Initializer


@Initializer
object ProxyChatInitializer : Initializable {

    var MessageHandler = ChannelHandler()


    /**
     * Called when the extension is initialized.
     *
     * In this case, we initialize our message handler.
     */
    override fun initialize() {
        // Do something when the extension is initialized
        MessageHandler.initialize()
    }

    /**
     * Called when the extension is shutdown.
     *
     * In this case, we destroy our message handler.
     */
    override fun shutdown() {
        // Do something when the extension is shutdown
        MessageHandler.destroy()
    }
}