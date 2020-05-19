package part2;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;
import part2.messaging.ClientRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class App {
    public static void main(String[] args) throws Exception {
        final ActorSystem system = ActorSystem.create("local_system");

        final ActorRef client = system.actorOf(Props.create(ClientActor.class), "client");
        final ActorRef server = system.actorOf(Props.create(ServerActor.class), "server");

        // Create db table
        Class.forName("org.sqlite.JDBC");
        SQLiteConfig config = new SQLiteConfig();
        config.setOpenMode(SQLiteOpenMode.FULLMUTEX);
        Connection c = DriverManager.getConnection(Constants.CONNECTION_STRING, config.toProperties());

        Statement stmt = c.createStatement();
        String sql = "drop table if exists log;" +
                "create table log" +
                "(name varchar(256) not null," +
                "count int not null);";
        stmt.executeUpdate(sql);
        stmt.close();
        c.close();

        System.out.println("Starting...");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = br.readLine();
            if (line.equals("q")) {
                break;
            }

            client.tell(new ClientRequest(server, line), null);
        }

        system.terminate();
    }
}
