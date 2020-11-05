import Base.BaseExtension;
import Base.BaseHandler;
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

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id = data.getInt(CmdDefine.ModuleUser.ID);

        // === Thao tác database ===
        // Lấy danh sách nhân vật
        ArrayList<M_Character> lstCharacter = new ArrayList<M_Character>();
        if(id == 1){
            int[] arrIdx = { 0, 2, 4, 6, 8 };

            for (int i = 0; i < 5; i++)
            {
                M_Character character = new M_Character();
                character.id_nv = i;
                character.id_cfg = "T100" + (new Random().nextInt(8 - 2 + 1) + 2);
                character.id_ac = id;
                character.lv = i + 1;
                character.idx = arrIdx[i];

                lstCharacter.add(character);
            }

            for (int i = 5; i < 10; i++)
            {
                M_Character character = new M_Character();
                character.id_nv = i;
                character.id_cfg = "T100" + (new Random().nextInt(8 - 2 + 1) + 2);
                character.id_ac = id;
                character.lv = new Random().nextInt(15 - 1 + 1) + 1;
                character.idx = -1;

                lstCharacter.add(character);
            }
        }

        // Lấy lịch sử phó bản
        ArrayList<M_TickMilestone> lstTick_milestone = new ArrayList<>();
        if(id == 1){
            for (int i = 0; i < 3; i++)
            {
                M_TickMilestone tickMilestone = new M_TickMilestone();
                tickMilestone.id_ml = i;
                tickMilestone.star = new Random().nextInt(4 - 1 + 1) + 1;

                lstTick_milestone.add(tickMilestone);
            }
        }

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        ISFSArray characters = new SFSArray();
        for(int i = 0; i < lstCharacter.size(); i++){
            ISFSObject obj = new SFSObject();
            obj.putInt(CmdDefine.ModuleCharacter.ID_NV, lstCharacter.get(i).id_nv);
            obj.putUtfString(CmdDefine.ModuleCharacter.ID_CFG, lstCharacter.get(i).id_cfg);
            obj.putInt(CmdDefine.ModuleCharacter.ID_AC, lstCharacter.get(i).id_ac);
            obj.putInt(CmdDefine.ModuleCharacter.LV, lstCharacter.get(i).lv);
            obj.putInt(CmdDefine.ModuleCharacter.IDX, lstCharacter.get(i).idx);

            characters.addSFSObject(obj);
        }
        packet.putSFSArray(CmdDefine.ModuleUser.CHARACTERS, characters);

        ISFSArray tick_milestones = new SFSArray();
        for(int i = 0; i < lstTick_milestone.size(); i++){
            ISFSObject tick_milestone = new SFSObject();
            tick_milestone.putInt(CmdDefine.ModuleMilestone.ID_TML, lstTick_milestone.get(i).id_tml);
            tick_milestone.putInt(CmdDefine.ModuleMilestone.ID_AC, lstTick_milestone.get(i).id_ac);
            tick_milestone.putInt(CmdDefine.ModuleMilestone.ID_ML, lstTick_milestone.get(i).id_ml);
            tick_milestone.putInt(CmdDefine.ModuleMilestone.STAR, lstTick_milestone.get(i).star);

            tick_milestones.addSFSObject(tick_milestone);
        }
        packet.putSFSArray(CmdDefine.ModuleUser.TICK_MILESTONES, tick_milestones);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GETINFO);
        this.send(CmdDefine.Module.MODULE_USER, packet, user);
    }

    private void HandleSelection(User user, ISFSObject data) {
        trace("____________________________ HandleSelection ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id = data.getInt(CmdDefine.ModuleUser.ID);
        String name = data.getUtfString(CmdDefine.ModuleUser.NAME);
        String id_cfg = data.getUtfString(CmdDefine.ModuleCharacter.ID_CFG);

        // === Thao tác database ===
        // Thay đổi tên tài khoản

        // Thêm nhân vật

        // Lấy lại danh sách nhân vật
        ArrayList<M_Character> lstCharacter = new ArrayList<M_Character>();
        M_Character character = new M_Character();
        character.id_nv = 0;
        character.id_cfg = id_cfg;
        character.id_ac = id;
        character.lv = 1;
        character.idx = 4;

        lstCharacter.add(character);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        ISFSArray characters = new SFSArray();
        for(int i = 0; i < lstCharacter.size(); i++){
            ISFSObject obj = new SFSObject();
            obj.putInt(CmdDefine.ModuleCharacter.ID_NV, lstCharacter.get(i).id_nv);
            obj.putUtfString(CmdDefine.ModuleCharacter.ID_CFG, lstCharacter.get(i).id_cfg);
            obj.putInt(CmdDefine.ModuleCharacter.ID_AC, lstCharacter.get(i).id_ac);
            obj.putInt(CmdDefine.ModuleCharacter.LV, lstCharacter.get(i).lv);
            obj.putInt(CmdDefine.ModuleCharacter.IDX, lstCharacter.get(i).idx);

            characters.addSFSObject(obj);
        }
        packet.putSFSArray(CmdDefine.ModuleUser.CHARACTERS, characters);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.SELECTION);
        this.send(CmdDefine.Module.MODULE_USER, packet, user);
    }

    private void HandlerTavern(User user, ISFSObject data) {
        trace("____________________________ HandleoTavern ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id = data.getInt(CmdDefine.ModuleUser.ID);
        int type = data.getInt(CmdDefine.ModuleUser.TYPE_TAVERN);

        // === Thao tác database ===
        String id_cfg = "T" + (1000 + (new Random().nextInt(10) + type * 10));
        // Thêm nhân vật

        // Lấy lại thông tin nhân vật
        M_Character character = new M_Character();
        character.id_nv = new Random().nextInt();
        character.id_cfg = id_cfg;
        character.id_ac = id;
        character.lv = 1;
        character.idx = -1;

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        ISFSObject obj = new SFSObject();
        obj.putInt(CmdDefine.ModuleCharacter.ID_NV, character.id_nv);
        obj.putUtfString(CmdDefine.ModuleCharacter.ID_CFG, character.id_cfg);
        obj.putInt(CmdDefine.ModuleCharacter.ID_AC, character.id_ac);
        obj.putInt(CmdDefine.ModuleCharacter.LV, character.lv);
        obj.putInt(CmdDefine.ModuleCharacter.IDX, character.idx);

        packet.putInt(CmdDefine.ModuleUser.TYPE_TAVERN, type);
        packet.putSFSObject(CmdDefine.ModuleUser.CHARACTER, obj);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.TAVERN);
        this.send(CmdDefine.Module.MODULE_USER, packet, user);
    }

    @Override
    protected void HandleServerEvent(SFSEventType type, ISFSEvent event) {
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
