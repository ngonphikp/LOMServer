import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;

public class HandlerUser extends BaseHandler {


    public HandlerUser(BaseExtension extension) {
        super(extension);
    }

    @Override
    protected void HandleClientRequest(int cmdId, User user, ISFSObject data) {
        switch (cmdId) {
            case CmdDefine.CMD.GETINFO:
                HandleGetInfo(user, data);
                break;
            case CmdDefine.CMD.SELECTION:
                HandleSelection(user, data);
                break;
            case CmdDefine.CMD.TAVERN:
                HandlerTavern(user, data);
                break;
        }
    }

    private void HandleGetInfo(User user, ISFSObject data) {
        trace("____________________________ HandleGetInfo ____________________________");
    }

    private void HandleSelection(User user, ISFSObject data) {
        trace("____________________________ HandleSelection ____________________________");
    }

    private void HandlerTavern(User user, ISFSObject data) {
        trace("____________________________ HandleoTavern ____________________________");
    }

    @Override
    protected void HandleServerEvent(SFSEventType type, ISFSEvent event) throws SFSException {
        switch (type) {

        }
    }

    @Override
    protected void initHandlerClientRequest() {
        this.extension.addRequestHandler(CmdDefine.Module.MODULE_USER, this);
    }

    @Override
    protected void initHandlerServerEvent() {
        this.extension.addServerHandler(CmdDefine.Module.MODULE_USER, this);
    }
}
