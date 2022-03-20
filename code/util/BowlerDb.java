package util;

import model.Bowler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class BowlerDb {

    /**
     * Retrieves bowler information from the database and returns a entity.Bowler objects with populated fields.
     *
     * @param nickName    the nickName of the bolwer to retrieve
     *
     * @return a entity.Bowler object
     *
     */

    public static Bowler getBowlerInfo(String nickName)
            throws SQLException {

        Connection conn = DbConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement("select * from bowler where nick=?");
        statement.setString(1, nickName);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Bowler bowler = new Bowler(
                    resultSet.getString("nick"),
                    resultSet.getString("full"),
                    resultSet.getString("email")
            );
            return bowler;
        }
        return null;
    }

    /**
     * Stores a entity.Bowler in the database
     *
     * @param bowler
     *
     */

    public static void storeBowlerInfo(Bowler bowler)
            throws SQLException {

        String query = "insert into bowler(nick,full,email) values(?,?,?)";
        Connection conn = DbConnection.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, bowler.getNickName());
        preparedStatement.setString(2, bowler.getFullName());
        preparedStatement.setString(3, bowler.getEmail());
        preparedStatement.execute();
    }

    /**
     * Retrieves a list of nicknames in the bowler database
     *
     * @return a Vector of Strings
     *
     */

    public static Vector getBowlers()
            throws SQLException {

        Connection conn = DbConnection.getConnection();
        PreparedStatement statement = conn.prepareStatement("select nick from bowler");
        ResultSet resultSet = statement.executeQuery();
        Vector<String> vector = new Vector<>();
        while (resultSet.next()) {
            vector.add(resultSet.getString("nick"));
        }
        return vector;
    }

}
