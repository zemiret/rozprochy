package part2;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;
import part2.messaging.DBResponse;
import part2.messaging.DBSaveRequest;

import java.sql.*;

public class DBRequestHandler extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final Connection c;

    DBRequestHandler() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        SQLiteConfig config = new SQLiteConfig();
        config.setOpenMode(SQLiteOpenMode.FULLMUTEX);
        this.c = DriverManager.getConnection(Constants.CONNECTION_STRING, config.toProperties());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DBSaveRequest.class, this::saveAndReturn)
                .matchAny(o -> log.info("Unexpected message: " + o))
                .build();
    }

    private void saveAndReturn(DBSaveRequest dbSaveRequest) throws SQLException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "select * from log where name='" + dbSaveRequest.prodName + "';" );
        int count = -1;

        if(rs.isBeforeFirst()) {
            count = rs.getInt("count") + 1;
            stmt.executeUpdate("update log set count = " + count + " where name='" + dbSaveRequest.prodName + "';");

        } else {
            stmt.executeUpdate("insert into log (name, count) values ('" +
                    dbSaveRequest.prodName + "', 1);"
            );
            count = 1;
        }

        dbSaveRequest.respondTo.tell(new DBResponse(dbSaveRequest.requestID, count), getSelf());

        rs.close();
        stmt.close();
        c.close();
    }
}
