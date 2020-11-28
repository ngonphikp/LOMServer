package Base;

import Util.CmdDefine;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.IServerEventHandler;

public abstract class BaseHandler extends BaseClientRequestHandler implements IServerEventHandler {
    protected BaseExtension extension;
    protected String module;

    public BaseHandler(BaseExtension extension, String module) {
        super();
        this.extension = extension;
        this.module = module;
        initHandlerClientRequest();
        initHandlerServerEvent();
    }

    @Override
    public final void handleClientRequest(User user, ISFSObject isfsObject) {
        int cmdId = isfsObject.getInt(CmdDefine.CMD_ID);
        HandleClientRequest(cmdId, user, isfsObject);
    }

    protected abstract void HandleClientRequest(int cmdId, User user, ISFSObject data);

    @Override
    public final void handleServerEvent(ISFSEvent isfsEvent) {
        SFSEventType type = isfsEvent.getType();
        HandleServerEvent(type, isfsEvent);
    }

    protected abstract void HandleServerEvent(SFSEventType type, ISFSEvent event);

    protected abstract void initHandlerClientRequest();

    protected abstract void initHandlerServerEvent();
}
