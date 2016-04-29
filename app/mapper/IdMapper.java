package mapper;

import domain.ID;

/**
 * Id库
 * Created by howen on 15/11/25.
 */
public interface IdMapper {

    ID getID(Long userId) throws Exception;
}
