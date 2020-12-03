package Base;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.extensions.SFSExtension;

import java.util.HashMap;
import java.util.Map;

public class QueueManage extends QueueExtension{

    public Map<String, User> mapQueue;

    public QueueManage(Zone zone, SFSExtension sfsExtension) {
        super(zone, sfsExtension);
        mapQueue = new HashMap<>();
    }

    public static void init(SFSExtension extension){
        Zone zone = extension.getParentZone();
        QueueManage queueManager = new QueueManage(zone, extension);
        zone.setProperty("queue", queueManager);
    }

    public void putUser(User user){
        System.out.println("________________________________________________________Queue put: " + user.getName());
        mapQueue.put(user.getName(), user);
    }

    public void popUser(User user){
        System.out.println("________________________________________________________Queue pop: " + user.getName());
        mapQueue.remove(user.getName());
    }
}
