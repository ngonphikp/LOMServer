import Base.BaseExtension;
import Handler.HandlerGame;

public class GameExtension extends BaseExtension {

    @Override
    public void initModule() {
        trace("____________________________ GameExtension ____________________________");
    }

    @Override
    public void init() {
        new HandlerGame(this);
    }
}
