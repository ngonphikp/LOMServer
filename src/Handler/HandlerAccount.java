package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import Base.RoomManage;
import Controls.C_Account;
import Controls.C_Character;
import Controls.C_Guild;
import Models.M_Account;
import Models.M_Character;
import Util.C_Util;
import Util.CmdDefine;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;
import java.util.Random;

public class HandlerAccount extends BaseHandler {

    public HandlerAccount(BaseExtension extension) {
        super(extension, CmdDefine.Module.MODULE_ACCOUNT);
    }

    @Override
    protected void HandleClientRequest(int cmdId, User user, ISFSObject data) {
        switch (cmdId) {
            case CmdDefine.CMD.GET_INFO:
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

        // === Thay đổi user name = id ===
        user.setName(id + "");

        // === Thao tác với room thế giới
        Room room = this.getParentExtension().getParentZone().getRoomByName(CmdDefine.Room.Global);
        RoomManage.userJoinRoom(this.extension, user, room);

        // === Thao tác database ===
        // Lấy id guild
        int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, C_Guild.getKey(id));

        // === Thao tác với room guild
        if(id_guild != -1){
            Room roomG = this.getParentExtension().getParentZone().getRoomByName(CmdDefine.Room.Guild + id_guild);
            if(roomG == null){
                // Tạo room guild + id
                RoomManage.initRoom(this.extension, CmdDefine.Room.Guild + id_guild, CmdDefine.Room.Guild, 50);

                roomG = this.getParentExtension().getParentZone().getRoomByName(CmdDefine.Room.Guild + id_guild);
            }
            RoomManage.userJoinRoom(this.extension, user, roomG);
        }

        // Lấy danh sách nhân vật
        ArrayList<M_Character> lstCharacter = C_Character.gets(id);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.ModuleAccount.ID_GUILD, id_guild);

        ISFSArray characters = new SFSArray();
        for(int i = 0; i < lstCharacter.size(); i++){
            characters.addSFSObject(lstCharacter.get(i).parse());
        }
        packet.putSFSArray(CmdDefine.ModuleAccount.CHARACTERS, characters);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_INFO);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleSelection(User user, ISFSObject data) {
        trace("____________________________ HandleSelection ____________________________");

        int id = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        String name = data.getUtfString(CmdDefine.ModuleAccount.NAME);
        String id_cfg = data.getUtfString(CmdDefine.ModuleCharacter.ID_CFG);

        // === Thao tác database ===
        // Thay đổi tên tài khoản
        M_Account account = C_Account.get(id);
        account.name = name;
        C_Account.set(account);
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
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandlerTavern(User user, ISFSObject data) {
        trace("____________________________ HandlerTavern ____________________________");

        int id_ac = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

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
        trace(packet.getDump());
        this.send(this.module, packet, user);
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
