package Base;

import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.SFSRoomRemoveMode;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class RoomManage {
    private SFSExtension extension;

    public RoomManage(SFSExtension extension){
        this.extension = extension;
    }

    public void initRoom(String className, String name, String groupId, int maxUser){
        System.out.println("________________________________________________________Room int: " + name + " (" + groupId + " )");
        CreateRoomSettings roomSetting = new CreateRoomSettings();

        roomSetting.setName(name);
        roomSetting.setGroupId(groupId);
        roomSetting.setMaxUsers(maxUser);
        roomSetting.setGame(false);

        roomSetting.setDynamic(true);
        roomSetting.setAutoRemoveMode(SFSRoomRemoveMode.NEVER_REMOVE);

        roomSetting.setExtension(new CreateRoomSettings.RoomExtensionSettings("LOM", className));

        try {
            extension.getApi().createRoom(extension.getParentZone(), roomSetting, null, true, null);
        } catch (SFSCreateRoomException e) {
            e.printStackTrace();
        }
    }

    public Room getRoom(String name){
         return extension.getParentZone().getRoomByName(name);
    }

    public void userJoinRoom(User user, Room room){
        System.out.println("________________________________________________________Room join: " + user.getName() + " => " + room.getName());
        try {
            extension.getApi().joinRoom(user, room, null, false, null);
        } catch (SFSJoinRoomException e) {
            e.printStackTrace();
        }
    }

    public void userOutRoom(User user, Room room){
        System.out.println("________________________________________________________Room out: " + user.getName() + " => " + room.getName());
        room.removeUser(user);
    }

    public  void removeRoom(Room room){
        System.out.println("________________________________________________________Room remove: " + room.getName());
        extension.getApi().removeRoom(room);
    }
}
