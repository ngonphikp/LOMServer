import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.exceptions.IErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.IServerEventHandler;

public abstract class BaseHandler extends BaseClientRequestHandler implements IServerEventHandler {
    protected BaseExtension extension;

    public BaseHandler(BaseExtension extension) {
        super();
        this.extension = extension;
        initHandlerClientRequest();
        initHandlerServerEvent();
    }

    @Override
    public final void handleClientRequest(User user, ISFSObject isfsObject) {
        try {
            int cmdId = isfsObject.getInt(CmdDefine.CMD_ID);
            HandleClientRequest(cmdId, user, isfsObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void HandleClientRequest(int cmdId, User user, ISFSObject data);

    @Override
    public final void handleServerEvent(ISFSEvent isfsEvent) throws SFSException {
        SFSEventType type = isfsEvent.getType();
        try{
            HandleServerEvent(type, isfsEvent);
        }catch (Exception e){
            throw e;
        }
    }

    protected abstract void HandleServerEvent(SFSEventType type, ISFSEvent event) throws SFSException;

    protected abstract void initHandlerClientRequest();

    protected abstract void initHandlerServerEvent();

    public void throwLoginExcepsion(IErrorCode iErrorCode, String param, String messageServer) throws SFSException {
        SFSErrorData errorData = new SFSErrorData(iErrorCode);
        errorData.addParameter(param);

        throw new SFSLoginException(messageServer, errorData);
    }
}
