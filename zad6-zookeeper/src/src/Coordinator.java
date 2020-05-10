import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Coordinator implements Watcher {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Wrong arguments number");
            System.err.println("USAGE: Coordinator connectionString program [args ...]");
            System.exit(123);
        }
        String connectionString = args[0];
        String[] exec = new String[args.length - 1];
        String watchFilename = "/z";
        System.arraycopy(args, 1, exec, 0, exec.length);

        Coordinator c = new Coordinator(connectionString, watchFilename, exec);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Press any key to see the list of nodes under z");
        while (true) {
            try {
                br.readLine();
                c.listChildren();
            } catch(IOException e) {
                System.err.println("Error reading line...");
            }
        }
    }

    private final String watchFilename;
    private ZooKeeper zk;
    private ZWatcher watcher;

    public Coordinator(String connectionString, String watchFilename, String[] exec) {
        this.watchFilename = watchFilename;

        try {
            this.zk = new ZooKeeper(connectionString, 5000, this);
            this.watcher = new ZWatcher(watchFilename, zk, exec);
            watcher.startWatch();
        } catch(IOException e) {
            System.err.println("Error creating coordinator...");
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
    }

    private void listChildren() {
        try {
            List<String> children = this.zk.getChildren(watchFilename, false);
            System.out.println("Children listing for: " + watchFilename);

            printChildren(children, watchFilename);

        } catch (InterruptedException| KeeperException e) {
            System.err.println("Error listing children... Possibly such node does not exist?");
        }
    }

    private void printChildren(List<String> children, String base) throws InterruptedException, KeeperException {
        for (String child : children) {
            String qualifiedPath = base + "/" + child;
            System.out.println(qualifiedPath);
            List<String> nestedChildren = this.zk.getChildren(qualifiedPath, false);
            printChildren(nestedChildren, qualifiedPath);
        }
    }
}
