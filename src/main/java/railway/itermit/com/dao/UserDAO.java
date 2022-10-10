package railway.itermit.com.dao;


import railway.itermit.com.db.DBException;
import railway.itermit.com.dao.entity.User;

public interface UserDAO extends DAO<User> {

    User get(User user) throws DBException;

}
