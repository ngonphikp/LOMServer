package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import Base.QueueManage;
import Base.RoomManage;
import Util.CmdDefine;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.Random;


public class HandlerPvP extends BaseHandler {
    public HandlerPvP(BaseExtension extension) {
        super(extension, CmdDefine.Module.MODULE_PVP);
    }

    @Override
    protected void HandleClientRequest(int cmdId, User user, ISFSObject data) {
        switch (cmdId) {
            case CmdDefine.CMD.START_PVP:
                HandleStart(user, data);
                break;
            case CmdDefine.CMD.CANCLE_PVP:
                HandleCancle(user, data);
                break;
        }
    }

    private void HandleStart(User user, ISFSObject data) {
        trace("____________________________ HandleStart ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // === Thao tác queue ===
        QueueManage queueManager = (QueueManage) extension.getParentZone().getProperty("queue");
        queueManager.putUser(user);

        if (queueManager.mapQueue.size() > 1){
            //pop userThis
            queueManager.popUser(user);

            //pop userThat
            // Lấy ngẫu nhiên trong queue
            Random random = new Random();
            Object[] values = queueManager.mapQueue.values().toArray();
            User userThat = (User) values[random.nextInt(values.length)];

            queueManager.popUser(userThat);

            // Thao tác với room Game
            RoomManage manage = new RoomManage(this.getParentExtension());
            manage.initRoom("Base.GameExtension", user.getName() + "-" + userThat.getName(), CmdDefine.Room.Game, 2);
            Room room = manage.getRoom(user.getName() + "-" + userThat.getName());

            manage.userJoinRoom(user, room);
            manage.userJoinRoom(userThat, room);
        }

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.START_PVP);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleCancle(User user, ISFSObject data) {
        trace("____________________________ HandleCancle ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // === Thao tác database ===
        QueueManage queueManager = (QueueManage) extension.getParentZone().getProperty("queue");
        queueManager.popUser(user);
        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.CANCLE_PVP);
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
        this.extension.addRequestHandler(CmdDefine.Module.MODULE_PVP, this);
    }

    @Override
    protected void initHandlerServerEvent() {
        this.extension.addServerHandler(CmdDefine.Module.MODULE_PVP, this);
    }
}
