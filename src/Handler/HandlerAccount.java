package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import Controls.C_Account;
import Controls.C_Character;
import Controls.C_TickMilestone;
import Models.M_Character;
import Models.M_TickMilestone;
import Util.CmdDefine;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;
import java.util.Random;

public class HandlerAccount extends BaseHandler {

    public HandlerAccount(BaseExtension extension) {
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

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id = data.getInt(CmdDefine.ModuleAccount.ID);

        // === Thao tác database ===
        // Lấy danh sách nhân vật
        ArrayList<M_Character> lstCharacter = C_Character.gets(id);

        // Lấy lịch sử phó bản
        ArrayList<M_TickMilestone> lstTick_milestone = C_TickMilestone.gets(id);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        ISFSArray characters = new SFSArray();
        for(int i = 0; i < lstCharacter.size(); i++){
            characters.addSFSObject(lstCharacter.get(i).parse());
        }
        packet.putSFSArray(CmdDefine.ModuleAccount.CHARACTERS, characters);

        ISFSArray tick_milestones = new SFSArray();
        for(int i = 0; i < lstTick_milestone.size(); i++){
            tick_milestones.addSFSObject(lstTick_milestone.get(i).parse());
        }
        packet.putSFSArray(CmdDefine.ModuleAccount.TICK_MILESTONES, tick_milestones);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GETINFO);
        this.send(CmdDefine.Module.MODULE_ACCOUNT, packet, user);
    }

    private void HandleSelection(User user, ISFSObject data) {
        trace("____________________________ HandleSelection ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id = data.getInt(CmdDefine.ModuleAccount.ID);
        String name = data.getUtfString(CmdDefine.ModuleAccount.NAME);
        String id_cfg = data.getUtfString(CmdDefine.ModuleCharacter.ID_CFG);

        // === Thao tác database ===
        // Thay đổi tên tài khoản
        C_Account.setName(id, name);
        // Thêm nhân vật
        C_Character.insert(id, id_cfg, 1, 4);
        // Lấy lại danh sách nhân vật
        ArrayList<M_Character> lstCharacter = C_Character.gets(id);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        ISFSArray characters = new SFSArray();
        for(int i = 0; i < lstCharacter.size(); i++){
            characters.addSFSObject(lstCharacter.get(i).parse());
        }
        packet.putSFSArray(CmdDefine.ModuleAccount.CHARACTERS, characters);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.SELECTION);
        this.send(CmdDefine.Module.MODULE_ACCOUNT, packet, user);
    }

    private void HandlerTavern(User user, ISFSObject data) {
        trace("____________________________ HandleoTavern ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id_ac = data.getInt(CmdDefine.ModuleAccount.ID);
        int type = data.getInt(CmdDefine.ModuleAccount.TYPE_TAVERN);

        // === Thao tác database ===
        String id_cfg = "T" + (1000 + (new Random().nextInt(10) + type * 10));
        // Thêm nhân vật
        int id = C_Character.insert(id_ac, id_cfg, 1, -1);
        // Lấy lại thông tin nhân vật
        M_Character character = C_Character.get(id);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.ModuleAccount.TYPE_TAVERN, type);
        packet.putSFSObject(CmdDefine.ModuleAccount.CHARACTER, character.parse());

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.TAVERN);
        this.send(CmdDefine.Module.MODULE_ACCOUNT, packet, user);
    }

    @Override
    protected void HandleServerEvent(SFSEventType type, ISFSEvent event) {
        switch (type) {

        }
    }

    @Override
    protected void initHandlerClientRequest() {
        this.extension.addRequestHandler(CmdDefine.Module.MODULE_ACCOUNT, this);
    }

    @Override
    protected void initHandlerServerEvent() {
        this.extension.addServerHandler(CmdDefine.Module.MODULE_ACCOUNT, this);
    }
}
