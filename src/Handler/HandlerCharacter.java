package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import Controls.C_Character;
import Util.CmdDefine;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;


public class HandlerCharacter extends BaseHandler {
    public HandlerCharacter(BaseExtension extension) {
        super(extension);
    }

    @Override
    protected void HandleClientRequest(int cmdId, User user, ISFSObject data) {
        switch (cmdId) {
            case CmdDefine.CMD.ARRANGE:
                HandleArrange(user, data);
                break;
            case CmdDefine.CMD.UPLEVEL:
                HandleUplevel(user, data);
                break;
        }
    }

    private void HandleArrange(User user, ISFSObject data) {
        trace("____________________________ HandleArrange ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        ISFSArray objs = data.getSFSArray(CmdDefine.ModuleAccount.CHARACTERS);

        // === Thao tác database ===
        for(int i = 0; i < objs.size(); i++){
            ISFSObject obj = objs.getSFSObject(i);
            C_Character.setIdx(obj.getInt(CmdDefine.ModuleCharacter.ID), obj.getInt(CmdDefine.ModuleCharacter.IDX));
        }

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.ARRANGE);
        this.send(CmdDefine.Module.MODULE_CHARACTER, packet, user);
    }

    private void HandleUplevel(User user, ISFSObject data) {
        trace("____________________________ HandleUplevel ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id = data.getInt(CmdDefine.ModuleCharacter.ID);

        // === Thao tác database ===
        // Up level nhân vật
        C_Character.setLv(id, C_Character.get(id).lv + 1);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.UPLEVEL);
        this.send(CmdDefine.Module.MODULE_CHARACTER, packet, user);
    }

    @Override
    protected void HandleServerEvent(SFSEventType type, ISFSEvent event) {
        switch (type) {

        }
    }

    @Override
    protected void initHandlerClientRequest() {
        this.extension.addRequestHandler(CmdDefine.Module.MODULE_CHARACTER, this);
    }

    @Override
    protected void initHandlerServerEvent() {
        this.extension.addServerHandler(CmdDefine.Module.MODULE_CHARACTER, this);
    }
}
