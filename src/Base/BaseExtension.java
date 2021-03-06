package Base;

import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.IClientRequestHandler;
import com.smartfoxserver.v2.extensions.IServerEventHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseExtension extends SFSExtension {
    private final Map<String, IServerEventHandler> handlers = new ConcurrentHashMap();

    @Override
    public void addRequestHandler(String requestId, IClientRequestHandler requestHandler) {
        super.addRequestHandler(requestId, requestHandler);
    }

    @Override
    public void addEventHandler(SFSEventType eventType, IServerEventHandler handler) {
        super.addEventHandler(eventType, handler);
    }

    @Override
    public void removeRequestHandler(String requestId) {
        super.removeRequestHandler(requestId);
    }

    @Override
    protected void removeEventHandler(SFSEventType eventType) {
        super.removeEventHandler(eventType);
    }

    public abstract void initModule();

    @Override
    public void init() {
        initModule();
    }
    public final void addServerHandler(String handlerType, IServerEventHandler handler) {
        handlers.put(handlerType, handler);
    }
}
