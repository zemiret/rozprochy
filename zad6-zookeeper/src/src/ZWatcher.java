import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ZWatcher implements AsyncCallback.StatCallback, AsyncCallback.Children2Callback {
    private final ZooKeeper zk;
    private List<String> lastRes = new LinkedList<>();
    private final String watchFilename;
    private final String[] executable;
    private Process process = null;

    ZWatcher(String watchFilename, ZooKeeper zk, String[] executable) {
        this.zk = zk;
        this.watchFilename = watchFilename;
        this.executable = executable;
    }

    public void startWatch() {
        zk.exists(watchFilename, true, this, null);
        zk.getChildren(watchFilename, true, this, null);
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (rc == KeeperException.Code.OK.intValue()) {
            if(process == null) {
                System.out.println("z node added (starting program)");
                zk.getChildren(watchFilename, true, this, null);

                ProcessBuilder pb = new ProcessBuilder();
                pb.command(executable);

                try {
                    this.process = pb.start();
                } catch (IOException e) {
                    System.err.println("Error starting executable");
                    e.printStackTrace();
                }
            }
        } else if (rc == KeeperException.Code.NONODE.intValue()) {
            if (this.process != null) {
                System.out.println("z node deleted (stopping program)");
                this.process.destroy(); // kill the process
                this.process = null;
            }
        } else {
            System.err.println("Some unknown exception in processResult... " + rc);
        }

        // resubscribe
        zk.exists(watchFilename, true, this, null);
    }

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> list, Stat stat) {
        if (!list.equals(lastRes) ) {
            lastRes = list;
            if (rc == KeeperException.Code.OK.intValue()) {
                System.out.println("changed child of z node, now there are: " + list.size() + " children");
            }
        }

        // resubscribe
        zk.getChildren(watchFilename, true, this, null);
    }
}
