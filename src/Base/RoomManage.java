package Base;

import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.SFSRoomRemoveMode;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class RoomManage {

    public static void initRoom(SFSExtension sfsExtension, String name, String groupId, int maxUser){
        System.out.println("________________________________________________________Int room: " + name + " (" + groupId + " )");
        CreateRoomSettings roomSetting = new CreateRoomSettings();

        roomSetting.setName(name);
        roomSetting.setGroupId(groupId);
        roomSetting.setMaxUsers(maxUser);
        roomSetting.setGame(false);

        roomSetting.setDynamic(true);
        roomSetting.setAutoRemoveMode(SFSRoomRemoveMode.NEVER_REMOVE);

        roomSetting.setExtension(new CreateRoomSettings.RoomExtensionSettings("LOM","Base.RoomExtension"));

        try {
            sfsExtension.getApi().createRoom(sfsExtension.getParentZone(), roomSetting, null, true, null);
        } catch (SFSCreateRoomException e) {
            e.printStackTrace();
        }
    }

    public static void userJoinRoom(SFSExtension sfsExtension, User user, Room room){
        System.out.println("________________________________________________________" + user.getName() + " join room: " + room.getName());
        try {
            sfsExtension.getApi().joinRoom(user, room, null, false, null);
        } catch (SFSJoinRoomException e) {
            e.printStackTrace();
        }
    }

    public static void userOutRoom(User user, Room room){
        System.out.println("________________________________________________________" + user.getName() + " out room: " + room.getName());
        room.removeUser(user);
    }

    public static  void removeRoom(SFSExtension sfsExtension, Room room){
        System.out.println("________________________________________________________Remove room: " + room.getName());
        sfsExtension.getApi().removeRoom(room);
    }
}
